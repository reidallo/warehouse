package app.warehouse.system.repository;

import app.warehouse.system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByRoleName(String roleName);
}
