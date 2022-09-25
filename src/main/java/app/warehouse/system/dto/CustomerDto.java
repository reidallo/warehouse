package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CustomerDto {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String address;
    private Integer postalCode;
    private String city;
    private String state;
    private String phone;
    private Set<OrderDtoOut> orders;
    private Long userId;
}
