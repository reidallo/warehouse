package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {

    private Long orderId;
    private String orderNumber;
    private String submitDate;
    private String deadlineDate;
    private String orderStatus;
    private Double orderPrice;
    private Long customerId;
    private List<ItemDto> orderItems;
}
