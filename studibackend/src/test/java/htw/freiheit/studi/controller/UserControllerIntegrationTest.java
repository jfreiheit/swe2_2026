package htw.freiheit.studi.controller;

import htw.freiheit.studi.entity.User;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userJson(String username, String email) {
        Long adminId = roleRepository.findByName("admin").orElseThrow().getId();
        return """
                {"username": "%s", "password": "geheim123", "email": "%s", "roleId": %d}
                """.formatted(username, email, adminId);
    }

    @Test
    void createUser_WithValidData_ShouldStoreHashedPassword() {
        assertThat(mvc.post().uri("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("anna", "anna@htw-berlin.de")))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .extractingPath("$.role.name").isEqualTo("admin");

        User saved = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals("anna"))
                .findFirst()
                .orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo("geheim123");
        assertThat(passwordEncoder.matches("geheim123", saved.getPassword())).isTrue();
    }

    @Test
    void createUser_WithExistingUsername_ShouldReturnConflict() {
        assertThat(mvc.post().uri("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("anna", "anna@htw-berlin.de")))
                .hasStatus(HttpStatus.CREATED);

        assertThat(mvc.post().uri("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("anna", "andere@htw-berlin.de")))
                .hasStatus(HttpStatus.CONFLICT);
    }
}
