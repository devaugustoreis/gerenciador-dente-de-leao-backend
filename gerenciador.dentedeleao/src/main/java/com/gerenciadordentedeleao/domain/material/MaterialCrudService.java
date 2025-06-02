package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryRepository;
import com.gerenciadordentedeleao.domain.consultation.material.ConsultationMaterialRepository;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricRepository;
import com.gerenciadordentedeleao.domain.material.historic.MovementType;
import com.gerenciadordentedeleao.application.errorhandler.BusinessException;
import com.gerenciadordentedeleao.application.errorhandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
public class MaterialCrudService {

    private final MaterialRepository repository;

    private final CategoryRepository categoryRepository;

    private final MaterialHistoricRepository materialHistoricRepository;

    private final ConsultationMaterialRepository consultationMaterialRepository;

    private final EnumMap<MovementType, BiConsumer<MaterialEntity, MovementStockDTO>> movementActions = new EnumMap<>(MovementType.class);

    @Autowired
    public MaterialCrudService(MaterialRepository repository, CategoryRepository categoryRepository, MaterialHistoricRepository materialHistoricRepository, ConsultationMaterialRepository consultationMaterialRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.materialHistoricRepository = materialHistoricRepository;
        this.consultationMaterialRepository = consultationMaterialRepository;
        movementActions.put(MovementType.ADDITION, this::additionMovementation);
        movementActions.put(MovementType.REMOVAL, this::removalMovementation);
    }

    public Optional<MaterialEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public List<MaterialEntity> findAll() {
        return repository.findAll();
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

    public MaterialEntity movementStock(UUID id, MovementStockDTO dto) {
        var material = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "ID", id));

        if (dto.quantity() <= 0) {
            throw new BusinessException("A quantidade de movimentação deve ser maior que zero.");
        }

        movementActions.get(dto.movementType()).accept(material, dto);

        createMovementHistoric(dto, material);
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
        material.setScheduledQuantity(consultationMaterialRepository.countByMaterialId(material));
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
}
