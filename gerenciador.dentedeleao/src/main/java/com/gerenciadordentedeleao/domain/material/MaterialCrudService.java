package com.gerenciadordentedeleao.domain.material;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.dto.MaterialRequestDTO;
import com.gerenciadordentedeleao.domain.material.stock.MaterialStockEntity;
import com.gerenciadordentedeleao.domain.material.stock.MaterialStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// TO DO: Implementar o método de exclusão lógica e ajustar erro no 'setMaterialStock'
@Service
public class MaterialCrudService extends AbstractCrudService<MaterialEntity> {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialStockRepository stockRepository;

    @Autowired
    protected MaterialCrudService(MaterialRepository repository) {
        super(repository);
    }

    public MaterialEntity createMaterial(MaterialRequestDTO dto) {
        MaterialEntity material = new MaterialEntity();
        material.setName(dto.getName());
        material.setCategoryId(dto.getCategoryId());

        MaterialStockEntity material_stock = new MaterialStockEntity();
        material_stock.setStockQuantity(dto.getStockQuantity());

        return materialRepository.save(material);
    }

    public MaterialEntity updateMaterial(UUID id, MaterialRequestDTO dto) {
        MaterialEntity material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material não encontrado com o ID: " + id));

        material.setName(dto.getName());
        material.setCategoryId(dto.getCategoryId());

        MaterialStockEntity materialStockEntity = new MaterialStockEntity();
        materialStockEntity.setStockQuantity(dto.getStockQuantity());

        return materialRepository.save(material);
    }
}
