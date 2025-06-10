package app.warehouse.system.repository;

import app.warehouse.system.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.order.orderId = ?1")
    List<Delivery> findByOrderId(Long orderId);
}
