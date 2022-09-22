package app.warehouse.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "wr_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long itemId;
    @Column(name = "item_quantity")
    private Integer itemQuantity;
    @OneToOne
    @JoinColumn(name = "fk_inventory_item", referencedColumnName = "id")
    private Inventory inventory;
    @ManyToOne
    @JoinColumn(name = "fk_order", referencedColumnName = "id")
    @JsonIgnore
    private Order order;
}
