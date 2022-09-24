package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.mapper.InventoryMapper;
import app.warehouse.system.model.Inventory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(InventoryDto dto) {
        if (dto == null)
            return null;
        Inventory entity = new Inventory();
        if (dto.getInventoryId() != null)
            entity.setInventoryId(dto.getInventoryId());
        if (dto.getName() != null)
            entity.setName(dto.getName());
        if (dto.getPrice() != null)
            entity.setPrice(dto.getPrice());
        if (dto.getQuantity() != null)
            entity.setQuantity(dto.getQuantity());
        return entity;
    }

    @Override
    public InventoryDto toDto(Inventory entity) {
        if (entity == null)
            return null;
        InventoryDto dto = new InventoryDto();
        if (entity.getInventoryId() != null)
            dto.setInventoryId(entity.getInventoryId());
        if (entity.getName() != null)
            dto.setName(entity.getName());
        if (entity.getPrice() != null)
            dto.setPrice(entity.getPrice());
        if (entity.getQuantity() != null)
            dto.setQuantity(entity.getQuantity());
        return dto;
    }

    @Override
    public Set<Inventory> toEntitySet(Set<InventoryDto> dtoSet) {
        Set<Inventory> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<InventoryDto> toDtoSet(Set<Inventory> entitySet) {
        Set<InventoryDto> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDto(entity)));
        return dtoSet;
    }
}
