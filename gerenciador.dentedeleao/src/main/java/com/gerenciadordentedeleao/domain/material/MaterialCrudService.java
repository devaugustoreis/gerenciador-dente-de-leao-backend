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
        material.setExcluded(false);
        material.setCategoryId(dto.getCategoryId());

        MaterialStockEntity stock = new MaterialStockEntity();
        stock.setStockQuantity(dto.getStockQuantity());
        stock.setMaterialId(material);
        material.setMaterialStock(stock);

        return materialRepository.save(material);
    }

    public Optional<MaterialEntity> updateMaterial(UUID id, MaterialRequestDTO dto) {
        return materialRepository.findById(id).map(material -> {
            material.setName(dto.getName());
            material.setCategoryId(dto.getCategoryId());

            MaterialStockEntity stock = material.getMaterialStock();
            if (stock != null) {
                stock.setStockQuantity(dto.getStockQuantity());
            }

            return materialRepository.save(material);
        });
    }
}
