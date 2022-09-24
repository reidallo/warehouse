package app.warehouse.system.service;

import app.warehouse.system.dto.InventoryDto;
import org.springframework.data.domain.Page;

public interface InventoryService {

    Page<InventoryDto> getInventory(Integer pageNo, Integer pageSize, String sortBy);
}
