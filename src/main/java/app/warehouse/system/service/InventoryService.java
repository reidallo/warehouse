package app.warehouse.system.service;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.exception.MessageHandler;
import org.springframework.data.domain.Page;

public interface InventoryService {

    Page<InventoryDto> getInventory(Integer pageNo, Integer pageSize, String sortBy);

    InventoryDto getInventoryById(Long inventoryId);

    MessageHandler updateInventory(InventoryDto inventoryDto);

    MessageHandler disableInventory(Long inventoryId);
}
