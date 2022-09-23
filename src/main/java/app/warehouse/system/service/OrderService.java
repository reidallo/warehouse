package app.warehouse.system.service;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.exception.MessageHandler;

import java.util.Set;

public interface OrderService {

    MessageHandler addNewOrder(OrderDto orderDto);

    MessageHandler addItemToOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler removeItemFromOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler cancelOrder(Long orderId);
}
