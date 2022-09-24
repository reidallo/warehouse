package app.warehouse.system.mapper;

import app.warehouse.system.dto.OrderDtoIn;
import app.warehouse.system.dto.OrderDtoOut;
import app.warehouse.system.model.Order;

import java.util.Set;

public interface OrderMapper extends IMapper<Order, OrderDtoIn>{

    OrderDtoOut toDtoOut(Order entity);

    Set<OrderDtoOut> toDtoOutSet(Set<Order> entitySet);
}
