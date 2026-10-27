package htw.freiheit.uebung_b.repository;

import htw.freiheit.uebung_b.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    boolean existsByEmailOrAccount(String email, String account);
    Optional<User> findByEmail(String email);
}
