package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toEntity(ItemDto dto) {
        if (dto == null)
            return null;
        Item entity = new Item();
        if (dto.getItemId() != null)
            entity.setItemId(dto.getItemId());
        if (dto.getItemQuantity() != null)
            entity.setItemQuantity(dto.getItemQuantity());
        if (dto.getInventoryId() != null) {
            Inventory inventory = new Inventory();
            inventory.setInventoryId(dto.getInventoryId());
            entity.setInventory(inventory);
        }
        return entity;
    }

    @Override
    public ItemDto toDto(Item entity) {
        return null;
    }

    @Override
    public List<Item> toEntityList(List<ItemDto> dtoList) {
        List<Item> entityList = new ArrayList<>();
        dtoList.forEach(dto -> entityList.add(toEntity(dto)));
        return entityList;
    }
}
