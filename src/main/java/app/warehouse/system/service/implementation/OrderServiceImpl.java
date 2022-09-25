package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.ItemDto;
import app.warehouse.system.dto.OrderDtoIn;
import app.warehouse.system.dto.OrderDtoOut;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.logged.LoggedUser;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.mapper.OrderMapper;
import app.warehouse.system.model.*;
import app.warehouse.system.repository.*;
import app.warehouse.system.service.EmailService;
import app.warehouse.system.service.OrderService;
import app.warehouse.system.statics.MessageStatus;
import app.warehouse.system.statics.OrderStatus;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ItemMapper itemMapper;
    private final OrderMapper orderMapper;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final EmailService emailService;
    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public MessageHandler addNewOrder(OrderDtoIn orderDto){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Set<ItemDto> itemsDto = orderDto.getOrderItems();
        Double totalPrice = calculateItemsPrice(itemsDto);
        Integer orderQuantity = calculateItemsQuantity(itemsDto);

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

        String username = order.getCustomer().getUser().getUsername();
        checkAccess(username);

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {

            Double orderPrice = calculateItemsPrice(itemDtoSet);
            Integer orderQuantity = calculateItemsQuantity(itemDtoSet);
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

        String username = order.getCustomer().getUser().getUsername();
        checkAccess(username);

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.CANCELED)) {

            Double orderPrice = calculateItemsPrice(itemDtoSet);
            Integer orderQuantity = calculateItemsQuantity(itemDtoSet);

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

    @Override
    public MessageHandler cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        String username = order.getCustomer().getUser().getUsername();
        checkAccess(username);

        if (!order.getOrderStatus().equals(OrderStatus.FULFILLED) && !order.getOrderStatus().equals(OrderStatus.CANCELED)
                && !order.getOrderStatus().equals(OrderStatus.UNDER_DELIVERY)) {
            order.setOrderStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }
        else {
            MessageHandler.message(MessageStatus.ERROR, "You can not cancel this order!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "canceled"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler submitOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        String username = order.getCustomer().getUser().getUsername();
        checkAccess(username);

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.DECLINED)) {
            order.setOrderStatus(OrderStatus.AWAITING_APPROVAL);
            order.setSubmitDate(new Date());
            orderRepository.save(order);
        }
        else {
            MessageHandler.message(MessageStatus.ERROR, "You can not submit this order!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "submitted"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler approveOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        if (order.getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            order.setOrderStatus(OrderStatus.APPROVED);
            orderRepository.save(order);
        }
        else {
            MessageHandler.message(MessageStatus.ERROR, "You can not approve this order!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "approved"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler declineOrder(Long orderId, String message) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));
        String userEmail = order.getCustomer().getUser().getEmail();

        if (order.getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            order.setOrderStatus(OrderStatus.DECLINED);
            if (message != null) {
                String subject = "Declined Order";
                emailService.sendEmailDeclinedOrder(order.getCustomer().getFirstName(), order.getCustomer().getLastName(),
                        order.getOrderNumber(), message, userEmail, subject);
            }
            orderRepository.save(order);
        }
        else {
            MessageHandler.message(MessageStatus.ERROR, "You can not decline this order!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "declined"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler fulfillOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));
        List<Delivery> deliveryList = deliveryRepository.findByOrderId(orderId);

        if (!order.getOrderStatus().equals(OrderStatus.UNDER_DELIVERY)) {
            MessageHandler.message(MessageStatus.ERROR, "This order is not delivered yet!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        if (deliveryList.isEmpty()) {
            MessageHandler.message(MessageStatus.ERROR, "This order is not delivered yet!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        deliveryList.forEach(delivery -> {
            if (!delivery.isStatus())
                throw new ExceptionHandler("This order is not delivered yet");
        });

        order.setOrderStatus(OrderStatus.FULFILLED);
        orderRepository.save(order);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Order", "declined"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public Page<OrderDtoIn> getUserOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy) {

        User user = userRepository.findUserByUsername(LoggedUser.loggedInUsername()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "User")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Order> pagedResult;
        if (orderStatus != null)
            pagedResult = orderRepository.filterOrderByStatusAndUser(orderStatus, user.getUserId(), pageable);
        else
            pagedResult = orderRepository.filterOrderByUser(user.getUserId(), pageable);
        return pagedResult.map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDtoOut> getAllOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Order> pagedResult;
        if (orderStatus != null)
            pagedResult = orderRepository.filterOrderByStatus(orderStatus, pageable);
        else
            pagedResult = orderRepository.findAll(pageable);
        return pagedResult.map(orderMapper::toDtoOut);
    }

    @Override
    public OrderDtoIn getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));
        return orderMapper.toDto(order);
    }

    private double calculateItemsPrice(Set<ItemDto> itemDtoList) {

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

    private int calculateItemsQuantity(Set<ItemDto> itemDtoList) {

        int orderQuantity = 0;
        for (ItemDto itemDto: itemDtoList) {
            orderQuantity += itemDto.getItemQuantity();
        }
        return orderQuantity;
    }

    private void checkAccess(String username) {
        String loggedUsername = LoggedUser.loggedInUsername();
        if (!loggedUsername.equals(username))
            throw new ExceptionHandler(ExceptionHandler.NO_ACCESS);
    }
}
