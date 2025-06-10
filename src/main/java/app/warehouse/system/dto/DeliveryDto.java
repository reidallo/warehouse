package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDto {

    private Long deliveryId;
    private String deliveryDate;
    private String deliveryCode;
    private Long orderId;
    private Long truckId;
    private boolean status;
}
