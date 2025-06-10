package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.model.Item;
import app.warehouse.system.model.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
        if (dto.getOrderId() != null) {
            Order order = new Order();
            order.setOrderId(dto.getOrderId());
            entity.setOrder(order);
        }
        return entity;
    }

    @Override
    public ItemDto toDto(Item entity) {
        if (entity == null)
             return null;
        ItemDto dto = new ItemDto();
        if (entity.getItemId() != null)
            dto.setItemId(entity.getItemId());
        if (entity.getItemQuantity() != null)
            dto.setItemQuantity(entity.getItemQuantity());
        if (entity.getOrder() != null)
            dto.setOrderId(entity.getOrder().getOrderId());
        if (entity.getInventory() != null)
            dto.setInventoryId(entity.getInventory().getInventoryId());
        return dto;
    }

    @Override
    public Set<Item> toEntitySet(Set<ItemDto> dtoSet) {
        Set<Item> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<ItemDto> toDtoSet(Set<Item> entitySet) {
        Set<ItemDto> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDto(entity)));
        return dtoSet;
    }
}
