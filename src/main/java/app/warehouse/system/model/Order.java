package app.warehouse.system.model;

import app.warehouse.system.statics.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wr_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long orderId;
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "submitted_date")
    private Date submitDate;
    @Column(name = "deadline_date")
    private Date deadlineDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;
    @Column(name = "order_price")
    private Double orderPrice;
    @ManyToOne
    @JoinColumn(name = "fk_customer", referencedColumnName = "id")
    private Customer customer;
    @OneToMany(mappedBy = "order")
    private Set<Item> orderItems;
    @Column(name = "order_quantity")
    private Integer orderQuantity;
    @Column(name = "address")
    private String address;
}
