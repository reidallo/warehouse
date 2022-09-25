package app.warehouse.system.repository;

import app.warehouse.system.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByDeliveryDateBetween(Date todayDate, Date deadlineDate);
}
