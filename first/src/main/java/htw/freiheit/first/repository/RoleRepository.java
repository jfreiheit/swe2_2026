package htw.freiheit.first.repository;

import htw.freiheit.first.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    boolean existsByRole(String role);
}
