package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    private Long itemId;
    private Integer itemQuantity;
    private Long inventoryId;
}
