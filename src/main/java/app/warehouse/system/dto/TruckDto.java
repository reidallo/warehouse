package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckDto {

    private Long truckId;
    private String chassisNumber;
    private String licensePlate;
    private boolean active;
}
