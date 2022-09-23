package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.logged.LoggedUser;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.model.Customer;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.model.Item;
import app.warehouse.system.model.Order;
import app.warehouse.system.repository.*;
import app.warehouse.system.service.OrderService;
import app.warehouse.system.statics.MessageStatus;
import app.warehouse.system.statics.OrderStatus;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final InventoryRepository inventoryRepository;
    private final ItemMapper itemMapper;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public MessageHandler addNewOrder(OrderDto orderDto){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Set<ItemDto> itemsDto = orderDto.getOrderItems();
        Double totalPrice = calculateItemsPrice(itemsDto);
        Long orderQuantity = calculateItemsQuantity(itemsDto);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderNumber(RandomStringUtils.random(24, false, true));
        try {
            order.setDeadlineDate(dateFormat.parse(orderDto.getDeadlineDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        order.setOrderPrice(totalPrice);
        order.setOrderQuantity(orderQuantity);
        Long userId = userRepository.getUserId(LoggedUser.loggedInUsername()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Customer")));
        Customer customer = customerRepository.getCustomerByUserId(userId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Customer")));
        order.setCustomer(customer);
        orderRepository.save(order);

        Set<Item> itemList = itemMapper.toEntitySet(itemsDto);
        itemList.forEach(item -> item.setOrder(order));
        order.setOrderItems(new HashSet<>(itemList));
        itemRepository.saveAll(itemList);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "created"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    @Transactional
    public MessageHandler addItemToOrder(Long orderId, Set<ItemDto> itemDtoSet) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {

            Double orderPrice = calculateItemsPrice(itemDtoSet);
            Long orderQuantity = calculateItemsQuantity(itemDtoSet);
            Set<Item> itemSet = itemMapper.toEntitySet(itemDtoSet);
            itemSet.forEach(item -> item.setOrder(order));
            itemRepository.saveAll(itemSet);

            order.setOrderPrice(order.getOrderPrice() + orderPrice);
            order.setOrderQuantity(order.getOrderQuantity() + orderQuantity);
            orderRepository.save(order);
        } else {
            MessageHandler.message(MessageStatus.ERROR, "You can only update an order with status CREATED/CANCELED!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Items", "added"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    @Transactional
    public MessageHandler removeItemFromOrder(Long orderId, Set<ItemDto> itemDtoSet) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {

            Double orderPrice = calculateItemsPrice(itemDtoSet);
            Long orderQuantity = calculateItemsQuantity(itemDtoSet);

            Set<Item> orderItems = order.getOrderItems();
            Set<Item> itemsToRemove = itemMapper.toEntitySet(itemDtoSet);
            orderItems.removeAll(itemsToRemove);

            order.setOrderItems(orderItems);
            order.setOrderPrice(order.getOrderPrice() - orderPrice);
            order.setOrderQuantity(order.getOrderQuantity() - orderQuantity);
            itemRepository.deleteAll(itemsToRemove);

        } else {
            MessageHandler.message(MessageStatus.ERROR, "You can only update an order with status CREATED/CANCELED!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Items", "removed"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    double calculateItemsPrice(Set<ItemDto> itemDtoList) {

        double totalPrice = 0.0;
        for (ItemDto itemDto: itemDtoList) {
            Inventory inventory = inventoryRepository.findById(itemDto.getInventoryId()).orElseThrow(() ->
                    new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Item")));
            if (itemDto.getItemQuantity() > inventory.getQuantity())
                throw new ExceptionHandler(String.format(ExceptionHandler.NOT_ENOUGH, inventory.getName()));
            totalPrice = totalPrice + (itemDto.getItemQuantity() * inventory.getPrice());
        }
        return totalPrice;
    }

    long calculateItemsQuantity(Set<ItemDto> itemDtoList) {

        long orderQuantity = 0;
        for (ItemDto itemDto: itemDtoList) {
            orderQuantity += itemDto.getItemQuantity();
        }
        return orderQuantity;
    }
}
