package htw.freiheit.studi.repository;

import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Role admin;

    @BeforeEach
    void setUp() {
        admin = entityManager.persist(new Role("admin"));
    }

    private User persistUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("HASH");
        user.setRole(admin);
        return entityManager.persistAndFlush(user);
    }

    @Test
    void findById_WhenUserExists_ShouldLoadUserWithRole() {
        // Arrange
        User anna = persistUser("anna", "anna@htw-berlin.de");
        entityManager.clear();

        // Act
        User found = userRepository.findById(anna.getId()).orElseThrow();

        // Assert
        assertThat(found).isNotSameAs(anna);
        assertThat(found.getUsername()).isEqualTo("anna");
        assertThat(found.getRole().getName()).isEqualTo("admin");
    }

    @Test
    void existsByEmailAndIdNot_ShouldIgnoreOwnRecord() {
        // Arrange
        User anna = persistUser("anna", "anna@htw-berlin.de");
        User bert = persistUser("bert", "bert@htw-berlin.de");

        // Act + Assert
        assertThat(userRepository.existsByEmailAndIdNot("anna@htw-berlin.de", anna.getId())).isFalse();
        assertThat(userRepository.existsByEmailAndIdNot("anna@htw-berlin.de", bert.getId())).isTrue();
    }

    @Test
    void existsByRoleId_WhenRoleIsUsed_ShouldReturnTrue() {
        // Arrange
        persistUser("anna", "anna@htw-berlin.de");

        // Act + Assert
        assertThat(userRepository.existsByRoleId(admin.getId())).isTrue();
    }

    @Test
    void existsByRoleId_WhenRoleIsUnused_ShouldReturnFalse() {
        // Arrange
        Role user = entityManager.persistAndFlush(new Role("user"));
        persistUser("anna", "anna@htw-berlin.de");

        // Act + Assert
        assertThat(userRepository.existsByRoleId(user.getId())).isFalse();
    }
}
