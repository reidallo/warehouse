package app.warehouse.system.controller;

import app.warehouse.system.dto.InventoryDto;
import app.warehouse.system.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
