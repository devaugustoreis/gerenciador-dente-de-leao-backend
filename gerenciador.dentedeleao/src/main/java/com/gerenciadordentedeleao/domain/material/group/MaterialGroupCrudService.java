package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.material.group.item.MaterialGroupItemKey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialGroupCrudService extends AbstractCrudService<MaterialGroupEntity> {

    protected MaterialGroupCrudService(MaterialGroupRepository repository) {
        super(repository);
    }

    @Override
    public String getEntityName() {
        return "Conjunto de materiais";
    }

    @Override
    public MaterialGroupEntity save(MaterialGroupEntity entity) {
        var items = List.copyOf(entity.getItems());
        entity.getItems().clear(); // Clear the existing collection
        var savedEntity = repository.saveAndFlush(entity);

        items.forEach(item -> {
            item.setMaterialGroupItemId(new MaterialGroupItemKey(savedEntity.getId(), item.getMaterial().getId()));
            item.setMaterialGroup(savedEntity);
            savedEntity.getItems().add(item); // Add the new items to the existing collection
        });

        return repository.save(savedEntity);
    }
}
