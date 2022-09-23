package app.warehouse.system.repository;

import app.warehouse.system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM warehouse.wr_customer c WHERE c.fk_user = ?1", nativeQuery = true)
    Optional<Customer> getCustomerByUserId(Long userId);
}
