package com.gerenciadordentedeleao.domain.material.group;

import com.gerenciadordentedeleao.application.abstractions.AbstractCrudService;
import com.gerenciadordentedeleao.domain.material.group.item.MaterialGroupItemKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        var items = entity.getItems();
        entity.setItems(new ArrayList<>());
        var savedEntity = repository.saveAndFlush(entity);
        var materialGroup = new MaterialGroupEntity();
        materialGroup.setId(savedEntity.getId());

        items.forEach(item -> {
            item.setMaterialGroupItemId(new MaterialGroupItemKey(savedEntity.getId(), item.getMaterial().getId()));
            item.setMaterialGroup(materialGroup);
        } );
        savedEntity.setItems(items);
        return repository.save(savedEntity);
    }
}
