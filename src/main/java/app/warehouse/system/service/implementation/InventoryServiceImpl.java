package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.mapper.InventoryMapper;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.repository.InventoryRepository;
import app.warehouse.system.service.InventoryService;
import app.warehouse.system.statics.MessageStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Page<InventoryDto> getInventory(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Inventory> pagedResult = inventoryRepository.findAllByActive(pageable);
        return pagedResult.map(inventoryMapper::toDto);
    }

    @Override
    public InventoryDto getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Inventory item")));
        return inventoryMapper.toDto(inventory);
    }

    @Override
    @Transactional
    public MessageHandler updateInventory(InventoryDto inventoryDto) {

        Inventory inventory = inventoryMapper.toEntity(inventoryDto);
        if (inventory.getName() == null) {
            MessageHandler.message(MessageStatus.ERROR, "Item name can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        if (inventory.getPrice() == null) {
            MessageHandler.message(MessageStatus.ERROR, "Item price can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        if (inventory.getQuantity() == null) {
            MessageHandler.message(MessageStatus.ERROR, "Item quantity can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        inventoryRepository.save(inventory);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Inventory Item", "updated"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler disableInventory(Long inventoryId) {

        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Inventory item")));
        inventory.setActive(false);
        inventoryRepository.save(inventory);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Inventory Item", "disabled"));
        return new MessageHandler(MessageHandler.hashMap);
    }
}
