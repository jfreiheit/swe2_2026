package htw.freiheit.uebung_b.repository;

import htw.freiheit.uebung_b.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
