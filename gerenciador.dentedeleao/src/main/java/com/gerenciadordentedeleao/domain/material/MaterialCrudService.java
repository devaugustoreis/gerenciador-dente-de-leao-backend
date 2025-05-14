package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.domain.category.CategoryRepository;
import com.gerenciadordentedeleao.domain.material.dto.CreateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.dto.MovementStockDTO;
import com.gerenciadordentedeleao.domain.material.dto.UpdateMaterialDTO;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricEntity;
import com.gerenciadordentedeleao.domain.material.historic.MaterialHistoricRepository;
import com.gerenciadordentedeleao.domain.material.stock.MaterialStockEntity;
import com.gerenciadordentedeleao.domain.material.stock.MaterialStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TO DO: Implementar o método de exclusão lógica e ajustar erro no 'setMaterialStock'
@Service
public class MaterialCrudService {

    private final MaterialRepository repository;

    private final CategoryRepository categoryRepository;

    private final MaterialHistoricRepository materialHistoricRepository;

    private final MaterialStockRepository materialStockRepository;

    @Autowired
    public MaterialCrudService(MaterialRepository repository, CategoryRepository categoryRepository, MaterialHistoricRepository materialHistoricRepository, MaterialStockRepository materialStockRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.materialHistoricRepository = materialHistoricRepository;
        this.materialStockRepository = materialStockRepository;
    }

    public Optional<MaterialEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public MaterialEntity create(CreateMaterialDTO dto) {
        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + dto.categoryId()));

        var material = new MaterialEntity();
        material.setName(dto.name());
        material.setCategory(category);
        material.setId(repository.saveAndFlush(material).getId());

        var stock = new MaterialStockEntity();
        stock.setStockQuantity(0);
        stock.setScheduledQuantity(0);
        stock.setAlertQuantity(0);
        stock.setMaterial(material);

        materialStockRepository.save(stock);
        return material;
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

        var stock = material.getStock();

        var movementation = new MaterialHistoricEntity();
        movementation.setMaterialStock(stock);
        movementation.setMovementType(dto.movementType());
        movementation.setQuantity(dto.quantity());
        movementation.setMovementDate(LocalDateTime.now());

        materialHistoricRepository.save(movementation);
        //stock.getMaterialHistoric().add(movementation);

        switch (dto.movementType()) {
            case ADDITION -> stock.setStockQuantity(stock.getStockQuantity() + dto.quantity());
            case REMOVAL -> {
                stock.setStockQuantity(stock.getStockQuantity() - dto.quantity());
                if (dto.reserveId() != null) {
                    var reserve = materialHistoricRepository.findById(dto.reserveId()).orElseThrow();
                    stock.setScheduledQuantity(stock.getScheduledQuantity() - reserve.getQuantity());
                }
            }
            case RESERVE -> stock.setScheduledQuantity(stock.getScheduledQuantity() + dto.quantity());
        }
        return repository.save(material);
    }

    public List<MaterialEntity> findAll() {
        return repository.findAll();
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
