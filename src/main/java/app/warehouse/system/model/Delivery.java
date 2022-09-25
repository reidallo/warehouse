package app.warehouse.system.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "wr_delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long deliveryId;
    @Column(name = "delivery_date")
    private Date deliveryDate;
    @Column(name = "delivery_code")
    private String deliveryCode;
    @ManyToOne
    @JoinColumn(name = "fk_order", referencedColumnName = "id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "fk_truck", referencedColumnName = "id")
    private Truck truck;
}
