package app.warehouse.system.controller;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PutMapping(value = "/")
    public MessageHandler updateOrderItem(@RequestBody ItemDto itemDto) {
        return itemService.updateOrderItem(itemDto);
    }
}
