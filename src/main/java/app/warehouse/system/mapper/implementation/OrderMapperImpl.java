package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.OrderDtoIn;
import app.warehouse.system.dto.OrderDtoOut;
import app.warehouse.system.mapper.ItemMapper;
import app.warehouse.system.mapper.OrderMapper;
import app.warehouse.system.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
@AllArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final ItemMapper itemMapper;

    @Override
    public Order toEntity(OrderDtoIn dto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (dto == null)
            return null;
        Order entity = new Order();
        if (dto.getOrderId() != null)
            entity.setOrderId(dto.getOrderId());
        if (dto.getDeadlineDate() != null) {
            try {
                entity.setDeadlineDate(dateFormat.parse(dto.getDeadlineDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    public OrderDtoIn toDto(Order entity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (entity == null)
             return null;
        OrderDtoIn dto = new OrderDtoIn();
        if (entity.getOrderId() != null)
            dto.setOrderId(entity.getOrderId());
        if (entity.getOrderStatus() != null)
            dto.setOrderStatus(entity.getOrderStatus().toString());
        if (entity.getDeadlineDate() != null)
            dto.setDeadlineDate(dateFormat.format(entity.getDeadlineDate()));
        if (entity.getOrderPrice() != null)
            dto.setOrderPrice(entity.getOrderPrice());
        if (entity.getOrderQuantity() != null)
            dto.setOrderQuantity(entity.getOrderQuantity());
        if (entity.getOrderNumber() != null)
            dto.setOrderNumber(entity.getOrderNumber());
        if (entity.getSubmitDate() != null)
            dto.setSubmitDate(dateFormat.format(entity.getSubmitDate()));
        if (entity.getOrderItems() != null)
            dto.setOrderItems(itemMapper.toDtoSet(entity.getOrderItems()));
        if (entity.getCustomer() != null)
            dto.setCustomerId(entity.getCustomer().getCustomerId());
        return dto;
    }

    @Override
    public Set<Order> toEntitySet(Set<OrderDtoIn> dtoSet) {
        Set<Order> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<OrderDtoIn> toDtoSet(Set<Order> entitySet) {
        Set<OrderDtoIn> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDto(entity)));
        return dtoSet;
    }

    @Override
    public OrderDtoOut toDtoOut(Order entity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        if (entity == null)
            return null;
        OrderDtoOut dto = new OrderDtoOut();
        if (entity.getOrderStatus() != null)
            dto.setOrderStatus(entity.getOrderStatus().toString());
        if (entity.getDeadlineDate() != null)
            dto.setDeadlineDate(dateFormat.format(entity.getDeadlineDate()));
        if (entity.getOrderPrice() != null)
            dto.setOrderPrice(entity.getOrderPrice());
        if (entity.getOrderNumber() != null)
            dto.setOrderNumber(entity.getOrderNumber());
        if (entity.getSubmitDate() != null)
            dto.setSubmitDate(dateFormat.format(entity.getSubmitDate()));
        return dto;
    }

    @Override
    public Set<OrderDtoOut> toDtoOutSet(Set<Order> entitySet) {
        Set<OrderDtoOut> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDtoOut(entity)));
        return dtoSet;
    }
}
