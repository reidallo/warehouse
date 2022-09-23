package app.warehouse.system.repository;

import app.warehouse.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    @Query(value = "SELECT u.id FROM warehouse.wr_user u WHERE u.username = ?1", nativeQuery = true)
    Optional<Long> getUserId(String username);
}
