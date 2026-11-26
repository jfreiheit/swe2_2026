package htw.freiheit.studi.repository;

import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@Transactional
class UserRepositoryPostgresTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User newUser(String username, String email) {
        Role admin = roleRepository.findByName("admin").orElseThrow();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("HASH");
        user.setRole(admin);
        return user;
    }

    @Test
    void existsByUsernameAndIdNot_ShouldIgnoreOwnRecord() {
        User anna = userRepository.save(newUser("anna", "anna@htw-berlin.de"));

        assertThat(userRepository.existsByUsernameAndIdNot("anna", anna.getId())).isFalse();
        assertThat(userRepository.existsByUsernameAndIdNot("anna", anna.getId() + 1)).isTrue();
    }

    @Test
    void save_WithDuplicateEmail_ShouldViolateUniqueConstraint() {
        userRepository.saveAndFlush(newUser("anna", "anna@htw-berlin.de"));

        assertThatThrownBy(() -> userRepository.saveAndFlush(newUser("bert", "anna@htw-berlin.de")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
