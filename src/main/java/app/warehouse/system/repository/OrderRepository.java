package app.warehouse.system.repository;

import app.warehouse.system.model.Order;
import app.warehouse.system.statics.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1")
    Page<Order> filterOrderByStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1 AND o.customer.user.userId = ?2")
    Page<Order> filterOrderByStatusAndUser(OrderStatus orderStatus, Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.customer.user.userId = ?1")
    Page<Order> filterOrderByUser(Long userId, Pageable pageable);
}
