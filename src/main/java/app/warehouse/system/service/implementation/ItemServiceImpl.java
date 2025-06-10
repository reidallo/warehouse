package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.model.Item;
import app.warehouse.system.model.Order;
import app.warehouse.system.repository.InventoryRepository;
import app.warehouse.system.repository.ItemRepository;
import app.warehouse.system.repository.OrderRepository;
import app.warehouse.system.service.ItemService;
import app.warehouse.system.statics.MessageStatus;
import app.warehouse.system.statics.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public MessageHandler updateOrderItem(ItemDto itemDto) {

        Order order = orderRepository.findById(itemDto.getOrderId()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));
        Item oldItem = itemRepository.findById(itemDto.getItemId()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Item")));
        Inventory inventory = inventoryRepository.findById(itemDto.getInventoryId()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Inventory item")));
        if (itemDto.getItemQuantity() > inventory.getQuantity()) {
            MessageHandler.message(MessageStatus.ERROR, "There are not enough " + inventory.getName() +
                    " on the warehouse!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {

            //minus price and quantity of the old item
            order.setOrderPrice(order.getOrderPrice() - (oldItem.getInventory().getPrice() * oldItem.getItemQuantity()));
            order.setOrderQuantity(order.getOrderQuantity() - oldItem.getItemQuantity());

            Item updatedItem = itemMapper.toEntity(itemDto);
            //plus price and quantity of the updated item
            order.setOrderPrice(order.getOrderPrice() + (oldItem.getInventory().getPrice() * updatedItem.getItemQuantity()));
            order.setOrderQuantity(order.getOrderQuantity() + updatedItem.getItemQuantity());

            itemRepository.save(updatedItem);
        } else {
            MessageHandler.message(MessageStatus.ERROR, "You can only update an order with status CREATED/CANCELED!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Items", "updated"));
        return new MessageHandler(MessageHandler.hashMap);
    }
}
