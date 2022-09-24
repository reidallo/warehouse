package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {

    private Long inventoryId;
    private String name;
    private Long quantity;
    private Double price;
    private boolean active;
}
