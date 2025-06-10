package app.warehouse.system.repository;

import app.warehouse.system.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM warehouse.wr_customer c WHERE c.fk_user = ?1", nativeQuery = true)
    Optional<Customer> getCustomerByUserId(Long userId);

    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.active = true ")
    Page<Customer> getCustomersByActive(Pageable pageable);
}
