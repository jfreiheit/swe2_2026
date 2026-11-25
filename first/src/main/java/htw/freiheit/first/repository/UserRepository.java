package htw.freiheit.first.repository;

import htw.freiheit.first.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    boolean existsByRoleId(Long roleId);
    boolean existsByAccount(String account);
    boolean existsByEmail(String email);
    boolean existsByAccountAndIdNot(String account, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<User> findByAccount(String account);
    Optional<User> findByEmail(String email);
}
