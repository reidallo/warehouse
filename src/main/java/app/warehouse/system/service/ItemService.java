package app.warehouse.system.service;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.exception.MessageHandler;


public interface ItemService {

    MessageHandler updateOrderItem(ItemDto itemDto);
}
