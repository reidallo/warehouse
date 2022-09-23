package app.warehouse.system.service;

import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.exception.MessageHandler;

import java.text.ParseException;

public interface OrderService {

    MessageHandler addNewOrder(OrderDto orderDto) throws ParseException;
}
