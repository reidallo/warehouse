package app.warehouse.system.service;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDtoIn;
import app.warehouse.system.dto.OrderDtoOut;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.statics.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface OrderService {

    MessageHandler addNewOrder(OrderDtoIn orderDto);

    MessageHandler addItemToOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler removeItemFromOrder(Long orderId, Set<ItemDto> itemDtoSet);

    MessageHandler cancelOrder(Long orderId);

    MessageHandler submitOrder(Long orderId);

    MessageHandler approveOrder(Long orderId);

    MessageHandler declineOrder(Long orderId, String message);

    MessageHandler fulfillOrder(Long orderId);

    Page<OrderDtoIn> getOrdersOfLoggedUser(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy);

    Page<OrderDtoOut> getAllOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy);

    List<OrderDtoOut> getOrdersOfUser(Long userId);

    OrderDtoIn getOrderById(Long orderId);
}
