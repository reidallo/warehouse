package app.warehouse.system.repository;

import app.warehouse.system.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);
    Optional<ResetPassword> findByExpirationDate(Instant date);
}
