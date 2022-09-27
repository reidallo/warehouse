package app.warehouse.system.repository;

import app.warehouse.system.model.Order;
import app.warehouse.system.statics.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1")
    Page<Order> filterOrderByStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1 AND o.customer.user.userId = ?2 " +
            "AND o.customer.user.active = true ")
    Page<Order> filterOrderByStatusAndUser(OrderStatus orderStatus, Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.customer.user.userId = ?1 AND o.customer.user.active = true")
    Page<Order> filterOrderByUser(Long userId, Pageable pageable);

    @Query(value = "SELECT o.* FROM warehouse.wr_order o \n" +
            "LEFT JOIN warehouse.wr_customer c ON o.fk_customer = c.id " +
            "LEFT JOIN warehouse.wr_user u ON c.fk_user = u.id " +
            "WHERE u.id = ?1 AND u.active = true", nativeQuery = true)
    List<Order> getAllOrdersOfUser(Long userId);
}
