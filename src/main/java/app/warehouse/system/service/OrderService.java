package app.warehouse.system.service;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDtoIn;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.statics.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface OrderService {

    MessageHandler addNewOrder(OrderDtoIn orderDto);

    MessageHandler addItemToOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler removeItemFromOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler cancelOrder(Long orderId);

    MessageHandler submitOrder(Long orderId);

    Page<OrderDtoIn> getUserOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy);
}
