package app.warehouse.system.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "wr_truck")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long truckId;
    @Column(name = "chassis_number")
    private String chassisNumber;
    @Column(name = "license_plate")
    private String licensePlate;
    @Column(name = "active")
    private boolean active;
}
