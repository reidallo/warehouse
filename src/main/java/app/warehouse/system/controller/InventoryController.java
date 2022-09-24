package app.warehouse.system.controller;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping(value = "/all")
    public ResponseEntity<Page<InventoryDto>> getInventory(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(inventoryService.getInventory(pageNo, pageSize, sortBy));
    }

    @GetMapping(value = "/id")
    public ResponseEntity<InventoryDto> getInventoryById(@RequestParam(name = "inventoryId")Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @PutMapping(value = "/update")
    public MessageHandler updateInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.updateInventory(inventoryDto);
    }
}
