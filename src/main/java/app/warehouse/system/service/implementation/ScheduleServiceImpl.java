package app.warehouse.system.service.implementation;

import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.model.*;
import app.warehouse.system.repository.DeliveryRepository;
import app.warehouse.system.repository.InventoryRepository;
import app.warehouse.system.repository.OrderRepository;
import app.warehouse.system.repository.TruckRepository;
import app.warehouse.system.service.EmailService;
import app.warehouse.system.service.ScheduleService;
import app.warehouse.system.statics.MessageStatus;
import app.warehouse.system.statics.OrderStatus;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final OrderRepository orderRepository;
    private final TruckRepository truckRepository;
    private final DeliveryRepository deliveryRepository;
    private final InventoryRepository inventoryRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public MessageHandler scheduleOrder(Long orderId, String date) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));

        if (!order.getOrderStatus().equals(OrderStatus.APPROVED)) {
            MessageHandler.message(MessageStatus.ERROR, "This order should be approved first!");
            return new MessageHandler(MessageHandler.hashMap);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date deliveryDate = null;
        try {
            deliveryDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        checkIfDateIsSunday(deliveryDate);
        int trucksNumber = neededTrucks(order.getOrderQuantity());

        List<Truck> truckList = truckRepository.findAvailableTrucks(deliveryDate);
        if (trucksNumber > truckList.size()) {
            MessageHandler.message(MessageStatus.ERROR, "There are only " + truckList.size() +
                    " available on " + date);
            return new MessageHandler(MessageHandler.hashMap);
        }

        for (int i = 0; i < trucksNumber; i++) {

            Delivery delivery = new Delivery();
            delivery.setDeliveryDate(deliveryDate);
            delivery.setDeliveryCode(RandomStringUtils.random(15, true, true));
            delivery.setOrder(order);
            delivery.setTruck(truckList.get(i));
            deliveryRepository.save(delivery);
        }
        order.setOrderStatus(OrderStatus.UNDER_DELIVERY);
        updateInventoryQuantity(orderId);
        String subject = "Under Delivery";
        emailService.sendEmailUnderDelivery(order.getCustomer().getFirstName(), order.getCustomer().getLastName(),
                order.getOrderNumber(), order.getCustomer().getUser().getEmail(), date, subject);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Delivery", "scheduled"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    private void checkIfDateIsSunday(Date deliveryDate) {
        if (deliveryDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deliveryDate);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                throw new ExceptionHandler("You can not schedule an order on Sunday!");
        }
    }

    private int neededTrucks(Integer itemsNumber) {
        int trucksNumber = 0;
        while (itemsNumber > 0)  {
            itemsNumber -= 10;
            trucksNumber++;
        }
        return trucksNumber;
    }

    private void updateInventoryQuantity(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Order")));
        Set<Item> itemSet = order.getOrderItems();

        itemSet.forEach(item -> {
            Inventory inventory = inventoryRepository.findById(item.getInventory().getInventoryId()).orElseThrow(() ->
                    new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Item")));
            if (item.getItemQuantity() > inventory.getQuantity())
                throw new ExceptionHandler(String.format(ExceptionHandler.NOT_ENOUGH, inventory.getName()));
            Integer updatedInventoryQuantity = inventory.getQuantity() - item.getItemQuantity();
            inventory.setQuantity(updatedInventoryQuantity);

            inventoryRepository.save(inventory);
        });
    }
}
