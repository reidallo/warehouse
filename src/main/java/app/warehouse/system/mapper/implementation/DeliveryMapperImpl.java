package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.DeliveryDto;
import app.warehouse.system.mapper.DeliveryMapper;
import app.warehouse.system.model.Delivery;
import app.warehouse.system.model.Order;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class DeliveryMapperImpl implements DeliveryMapper {

    @Override
    public Delivery toEntity(DeliveryDto dto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (dto == null)
            return null;
        Delivery entity = new Delivery();
        if (dto.getDeliveryId() != null)
            entity.setDeliveryId(dto.getDeliveryId());
        if (dto.getDeliveryCode() != null)
            entity.setDeliveryCode(dto.getDeliveryCode());
        if (dto.getDeliveryDate() != null) {
            try {
                entity.setDeliveryDate(dateFormat.parse(dto.getDeliveryDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (dto.getOrderId() != null) {
            Order order = new Order();
            order.setOrderId(dto.getOrderId());
            entity.setOrder(order);
        }
        entity.setStatus(dto.isStatus());
        return entity;
    }

    @Override
    public DeliveryDto toDto(Delivery entity) {
        return null;
    }

    @Override
    public Set<Delivery> toEntitySet(Set<DeliveryDto> dtoSet) {
        Set<Delivery> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<DeliveryDto> toDtoSet(Set<Delivery> entitySet) {
        return null;
    }
}
