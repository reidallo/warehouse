package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.OrderDto;
import app.warehouse.system.mapper.OrderMapper;
import app.warehouse.system.model.Order;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderDto dto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
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
    public OrderDto toDto(Order entity) {
        return null;
    }

    @Override
    public Set<Order> toEntitySet(Set<OrderDto> dtoList) {
        return null;
    }
}
