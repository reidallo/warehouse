package app.warehouse.system.mapper.implementation;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.mapper.CustomerMapper;
import app.warehouse.system.mapper.OrderMapper;
import app.warehouse.system.model.Customer;
import app.warehouse.system.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class CustomerMapperImpl implements CustomerMapper {

    private final OrderMapper orderMapper;

    @Override
    public Customer toEntity(CustomerDto dto) {
        if (dto == null)
            return null;
        Customer entity = new Customer();
        if (dto.getCustomerId() != null)
            entity.setCustomerId(dto.getCustomerId());
        if (dto.getAddress() != null)
            entity.setAddress(dto.getAddress());
        if (dto.getFirstName() != null)
            entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            entity.setLastName(dto.getLastName());
        if (dto.getCity() != null)
            entity.setCity(dto.getCity());
        if (dto.getState() != null)
            entity.setState(dto.getState());
        if (dto.getPhone() != null)
            entity.setPhone(dto.getPhone());
        if (dto.getPostalCode() != null)
            entity.setPostalCode(dto.getPostalCode());
        if (dto.getOrders() != null)
            entity.setOrders(orderMapper.toEntitySetFromDtoOut(dto.getOrders()));
        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            entity.setUser(user);
        }
        return entity;
    }

    @Override
    public CustomerDto toDto(Customer entity) {
        if (entity == null)
            return null;
        CustomerDto dto = new CustomerDto();
        if (entity.getCustomerId() != null)
            dto.setCustomerId(entity.getCustomerId());
        if (entity.getFirstName() != null)
            dto.setFirstName(entity.getFirstName());
        if (entity.getLastName() != null)
            dto.setLastName(entity.getLastName());
        if (entity.getAddress() != null)
            dto.setAddress(entity.getAddress());
        if (entity.getCity() != null)
            dto.setCity(entity.getCity());
        if (entity.getPhone() != null)
            dto.setPhone(entity.getPhone());
        if (entity.getState() != null)
            dto.setState(entity.getState());
        if (entity.getPostalCode() != null)
            dto.setPostalCode(entity.getPostalCode());
        if (entity.getOrders() != null)
            dto.setOrders(orderMapper.toDtoOutSet(entity.getOrders()));
        if (entity.getUser() != null)
            dto.setUserId(entity.getUser().getUserId());
        return dto;
    }

    @Override
    public Set<Customer> toEntitySet(Set<CustomerDto> dtoSet) {
        Set<Customer> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<CustomerDto> toDtoSet(Set<Customer> entitySet) {
        Set<CustomerDto> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDto(entity)));
        return dtoSet;
    }
}
