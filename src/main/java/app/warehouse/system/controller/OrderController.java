package app.warehouse.system.controller;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/new")
    public MessageHandler addNewOrder(@RequestBody OrderDto orderDto) {
        return orderService.addNewOrder(orderDto);
    }

    @PostMapping(value = "/add/item")
    public MessageHandler addItemToOrder(@RequestParam Long orderId, @RequestBody Set<ItemDto> itemDtoSet) {
        return orderService.addItemToOrder(orderId, itemDtoSet);
    }

    @PutMapping(value = "/remove/item")
    public MessageHandler removeItemFromOrder(@RequestParam Long orderId, @RequestBody Set<ItemDto> itemDtoSet) {
        return orderService.removeItemFromOrder(orderId, itemDtoSet);
    }

    @PutMapping(value = "/cancel")
    public MessageHandler cancelOrder(@RequestParam Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PutMapping(value = "/submit")
    public MessageHandler submitOrder(@RequestParam Long orderId) {
        return orderService.submitOrder(orderId);
    }
}
