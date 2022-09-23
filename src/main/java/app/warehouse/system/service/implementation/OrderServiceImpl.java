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
import java.util.List;
import java.util.Locale;

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
    public MessageHandler addNewOrder(OrderDto orderDto) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        List<ItemDto> itemsDto = orderDto.getOrderItems();
        Double totalPrice = calculateItemsPrice(itemsDto);
        Long orderQuantity = calculateOrderQuantity(itemsDto);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderNumber(RandomStringUtils.random(24, false, true));
        order.setDeadlineDate(dateFormat.parse(orderDto.getDeadlineDate()));
        order.setOrderPrice(totalPrice);
        order.setOrderQuantity(orderQuantity);
        Long userId = userRepository.getUserId(LoggedUser.loggedInUsername()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Customer")));
        Customer customer = customerRepository.getCustomerByUserId(userId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Customer")));
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Item> items = itemMapper.toEntityList(itemsDto);
        items.forEach(item -> item.setOrder(order));
        order.setOrderItems(new HashSet<>(items));
        itemRepository.saveAll(items);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "created"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    double calculateItemsPrice(List<ItemDto> itemDtos) {

        double totalPrice = 0.0;
        for (ItemDto itemDto: itemDtos) {
            Inventory inventory = inventoryRepository.findById(itemDto.getInventoryId()).orElseThrow(() ->
                    new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Item")));
            if (itemDto.getItemQuantity() > inventory.getQuantity())
                throw new ExceptionHandler(String.format(ExceptionHandler.NOT_ENOUGH, inventory.getName()));
            totalPrice = totalPrice + (itemDto.getItemQuantity() * inventory.getPrice());
        }
        return totalPrice;
    }

    long calculateOrderQuantity(List<ItemDto> itemDtos) {

        long orderQuantity = 0;
        for (ItemDto itemDto: itemDtos) {
            orderQuantity += itemDto.getItemQuantity();
        }
        return orderQuantity;
    }
}
