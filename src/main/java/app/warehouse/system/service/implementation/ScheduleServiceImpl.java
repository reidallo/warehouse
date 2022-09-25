package app.warehouse.system.service.implementation;

import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.model.Delivery;
import app.warehouse.system.model.Inventory;
import app.warehouse.system.model.Order;
import app.warehouse.system.model.Truck;
import app.warehouse.system.repository.DeliveryRepository;
import app.warehouse.system.repository.InventoryRepository;
import app.warehouse.system.repository.OrderRepository;
import app.warehouse.system.repository.TruckRepository;
import app.warehouse.system.service.ScheduleService;
import app.warehouse.system.statics.MessageStatus;
import app.warehouse.system.statics.OrderStatus;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final OrderRepository orderRepository;
    private final TruckRepository truckRepository;
    private final DeliveryRepository deliveryRepository;
    private final InventoryRepository inventoryRepository;

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

        if (deliveryDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deliveryDate);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                MessageHandler.message(MessageStatus.ERROR, "You can not schedule a delivery on Sunday!");
                return new MessageHandler(MessageHandler.hashMap);
            }
        }

        Long itemsNumber = order.getOrderQuantity();
        int neededTrucks = 0;
        while (itemsNumber > 0)  {
            itemsNumber -= 10;
            neededTrucks++;
        }

        List<Truck> truckList = truckRepository.findAvailableTrucks(deliveryDate);
        if (neededTrucks > truckList.size()) {
            MessageHandler.message(MessageStatus.ERROR, "There are only " + truckList.size() +
                    " available on " + date);
            return new MessageHandler(MessageHandler.hashMap);
        }

        for (int i = 0; i < neededTrucks; i++) {
            Delivery delivery = new Delivery();
            delivery.setDeliveryDate(deliveryDate);
            delivery.setDeliveryCode(RandomStringUtils.random(15, true, true));
            delivery.setOrder(order);
            delivery.setTruck(truckList.get(i));
            deliveryRepository.save(delivery);
        }
        order.setOrderStatus(OrderStatus.UNDER_DELIVERY);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Delivery", "scheduled"));
        return new MessageHandler(MessageHandler.hashMap);
    }

}
