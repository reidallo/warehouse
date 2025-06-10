package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDtoOut {

    private Long orderId;
    private String orderNumber;
    private String submitDate;
    private String deadlineDate;
    private String orderStatus;
    private Double orderPrice;
}
