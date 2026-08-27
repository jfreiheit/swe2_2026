package htw.freiheit.studi.repository;

import htw.freiheit.studi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByRoleId(Long roleId);
}
