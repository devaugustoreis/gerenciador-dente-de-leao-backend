package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryRepository;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsCrudService;
import com.gerenciadordentedeleao.domain.consultation.materials.ConsultationMaterialsRepository;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricRepository;
import com.gerenciadordentedeleao.domain.material.historic.MovementType;
import com.gerenciadordentedeleao.application.errorhandler.BusinessException;
import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;

@Service
public class MaterialCrudService {

    private final MaterialRepository repository;

    private final CategoryRepository categoryRepository;

    private final MaterialHistoricRepository materialHistoricRepository;

    private final ConsultationMaterialsRepository consultationMaterialsRepository;

    private final ConsultationMaterialsCrudService consultationMaterialsCrudService;

    private final EnumMap<MovementType, BiConsumer<MaterialEntity, MovementStockDTO>> movementActions = new EnumMap<>(MovementType.class);

    @Autowired
    public MaterialCrudService(MaterialRepository repository, CategoryRepository categoryRepository, MaterialHistoricRepository materialHistoricRepository, ConsultationMaterialsRepository consultationMaterialsRepository, @Lazy ConsultationMaterialsCrudService consultationMaterialsCrudService) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.materialHistoricRepository = materialHistoricRepository;
        this.consultationMaterialsRepository = consultationMaterialsRepository;
        this.consultationMaterialsCrudService = consultationMaterialsCrudService;
        movementActions.put(MovementType.ADDITION, this::additionMovementation);
        movementActions.put(MovementType.REMOVAL, this::removalMovementation);
    }

    public Optional<MaterialEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<MaterialEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public MaterialEntity create(CreateMaterialDTO dto) {
        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new BusinessException("Categoria não encontrada com o ID: " + dto.categoryId()));

        var material = new MaterialEntity();
        material.setName(dto.name());
        material.setCategory(category);
        material.setStockQuantity(0);
        material.setScheduledQuantity(0);
        material.setAlertQuantity(0);
        material.setHighlight(false);
        return repository.save(material);
    }

    public MaterialEntity update(UUID id, UpdateMaterialDTO dto) {
        var material = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));

        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "ID", dto.categoryId()));

        material.setName(dto.name());
        material.setCategory(category);

        return repository.save(material);
    }

    public void setScheduleQuantity(UUID materialId, int quantity) {
        MaterialEntity material = repository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", materialId));
        material.setScheduledQuantity(quantity);

        repository.save(material);
    }

    public MaterialEntity setExpectedEndDateAndHighlight(MaterialEntity material) {
        Date duasSemanasDepois = Date.from(
                LocalDate.now().plusWeeks(2)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );

        boolean quantidadeAgendadaMaiorQueEstoque = material.getScheduledQuantity() >= material.getStockQuantity();
        boolean possuiDataTermino = material.getExpectedEndDate() != null;

        if (quantidadeAgendadaMaiorQueEstoque && !possuiDataTermino) {
            Date expectedEnd = consultationMaterialsCrudService.findExpectedEndDate(material);
            material.setExpectedEndDate(expectedEnd);
        } else if (!quantidadeAgendadaMaiorQueEstoque && possuiDataTermino) {
            material.setExpectedEndDate(null);
        }

        if (material.getExpectedEndDate() != null && material.getExpectedEndDate().before(duasSemanasDepois)) {
            material.setHighlight(Boolean.TRUE);
        }else{
            material.setHighlight(Boolean.FALSE);
        }

        return material;
    }

    public MaterialEntity movementStock(UUID id, MovementStockDTO dto) {
        var material = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));

        if (dto.quantity() <= 0) {
            throw new BusinessException("A quantidade de movimentação deve ser maior que zero.");
        }

        movementActions.get(dto.movementType()).accept(material, dto);

        createMovementHistoric(dto, material);
        material = setExpectedEndDateAndHighlight(material);
        return repository.save(material);
    }

    private void additionMovementation(MaterialEntity material, MovementStockDTO dto) {
        material.setStockQuantity(material.getStockQuantity() + dto.quantity());
    }

    private void removalMovementation(MaterialEntity material, MovementStockDTO dto) {
        int stockQuantity = material.getStockQuantity();
        if (stockQuantity < dto.quantity()) {
            throw new BusinessException("A quantidade em estoque não pode ser menor que zero.");
        }

        material.setStockQuantity(stockQuantity - dto.quantity());
        material.setScheduledQuantity(consultationMaterialsRepository.countByMaterialId(material));
    }

    private void createMovementHistoric(MovementStockDTO dto, MaterialEntity material) {
        var movementation = new MaterialHistoricEntity();
        movementation.setMaterial(material);
        movementation.setMovementType(dto.movementType());
        movementation.setQuantity(dto.quantity());
        movementation.setMovementDate(LocalDateTime.now());

        materialHistoricRepository.save(movementation);
    }

    public void delete(UUID id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintViolationException) {
                String sqlState = constraintViolationException.getSQLState();
                if ("23503".equals(sqlState)) { // SQL state for FK violation in most databases
                    var material = repository.findById(id).orElseThrow();
                    material.setExcluded(true);
                    repository.save(material);
                }
            }
        }
    }

    public void uploadImage(UUID id, MultipartFile arquivo) {
        var material = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));
        try {
            material.setImage(arquivo.getBytes());
            repository.save(material);
        } catch (IOException e) {
            throw new BusinessException("Erro ao salvar a imagem do material: " + e.getMessage());
        }
    }
}
