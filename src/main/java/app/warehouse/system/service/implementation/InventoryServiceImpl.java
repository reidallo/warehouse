package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.mapper.InventoryMapper;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.repository.InventoryRepository;
import app.warehouse.system.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Page<InventoryDto> getInventory(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Inventory> pagedResult = inventoryRepository.findAll(pageable);
        return pagedResult.map(inventoryMapper::toDto);
    }
}
