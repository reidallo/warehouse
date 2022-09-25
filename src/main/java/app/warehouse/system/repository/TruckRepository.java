package app.warehouse.system.repository;

import app.warehouse.system.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    @Query("SELECT t FROM Truck t WHERE t.truckId NOT IN" +
            "(SELECT d FROM Delivery d WHERE d.deliveryDate = ?1)")
    List<Truck> findAllTrucksNotInDate(Date date);

    @Query(value = "SELECT tr.* FROM warehouse.wr_truck tr WHERE tr.id NOT IN \n" +
            "(SELECT de.fk_truck FROM warehouse.wr_delivery de WHERE de.delivery_date = '2022-10-10')", nativeQuery = true)
    List<Truck> findAvailableTrucks(Date date);
}
