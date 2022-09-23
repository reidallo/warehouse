package app.warehouse.system.controller;

import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/new")
    public MessageHandler addNewOrder(@RequestBody OrderDto orderDto) throws ParseException {
        return orderService.addNewOrder(orderDto);
    }
}
