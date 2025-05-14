package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryRepository;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricRepository;
import com.gerenciadordentedeleao.domain.material.historic.MovementType;
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

    private EnumMap<MovementType, BiConsumer<MaterialEntity, MovementStockDTO>> movementActions = new EnumMap<>(MovementType.class);

    @Autowired
    public MaterialCrudService(MaterialRepository repository, CategoryRepository categoryRepository, MaterialHistoricRepository materialHistoricRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.materialHistoricRepository = materialHistoricRepository;
        movementActions.put(MovementType.ADDITION, this::additionMovementation);
        movementActions.put(MovementType.REMOVAL, this::removalMovementation);
        movementActions.put(MovementType.RESERVE, this::reserveMovementation);
    }

    public Optional<MaterialEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public List<MaterialEntity> findAll() {
        return repository.findAll();
    }

    public MaterialEntity create(CreateMaterialDTO dto) {
        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + dto.categoryId()));

        var material = new MaterialEntity();
        material.setName(dto.name());
        material.setCategory(category);
        material.setStockQuantity(0);
        material.setScheduledQuantity(0);
        material.setAlertQuantity(0);
        return repository.save(material);
    }

    public MaterialEntity update(UpdateMaterialDTO dto) {
        var material = repository.findById(dto.materialId())
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + dto.materialId()));

        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + dto.categoryId()));

        material.setName(dto.name());
        material.setCategory(category);

        return repository.save(material);
    }

    public MaterialEntity movementStock(MovementStockDTO dto) {
        var material = repository.findById(dto.materialId())
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + dto.materialId()));

        if (dto.quantity() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        movementActions.get(dto.movementType()).accept(material, dto);

        var movementation = new MaterialHistoricEntity();
        movementation.setMaterial(material);
        movementation.setMovementType(dto.movementType());
        movementation.setQuantity(dto.quantity());
        movementation.setMovementDate(LocalDateTime.now());

        materialHistoricRepository.save(movementation);
        return repository.save(material);
    }

    private void additionMovementation(MaterialEntity material, MovementStockDTO dto) {
        material.setStockQuantity(material.getStockQuantity() + dto.quantity());
    }

    private void removalMovementation(MaterialEntity material, MovementStockDTO dto) {
        int stockQuantity = material.getStockQuantity();
        if (stockQuantity < dto.quantity()) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser menor que zero.");
        }

        material.setStockQuantity(stockQuantity - dto.quantity());
        if (dto.reserveId() == null) {
            return;
        }
        //melhorar validação
        var reserve = materialHistoricRepository.findById(dto.reserveId()).orElseThrow();
        int newScheduleQuantity = material.getScheduledQuantity() - reserve.getQuantity();
        material.setScheduledQuantity(Math.max(newScheduleQuantity, 0));
    }

    private void reserveMovementation(MaterialEntity material, MovementStockDTO dto) {
        int newScheduleQuantity = material.getScheduledQuantity() + dto.quantity();
        if (newScheduleQuantity > material.getStockQuantity()) {
            throw new IllegalArgumentException("A quantidade agendada não pode ser maior que a quantidade em estoque.");
        }
        material.setScheduledQuantity(newScheduleQuantity);
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
