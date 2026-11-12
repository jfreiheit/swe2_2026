package htw.freiheit.studi.controller;

import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.entity.User;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final JsonMapper jsonMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private Long adminId;

    @Autowired
    UserControllerIntegrationTest(MockMvc mockMvc,
                                  JsonMapper jsonMapper,
                                  UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
        this.mockMvc = mockMvc;
        this.jsonMapper = jsonMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        adminId = roleRepository.findByName("admin").orElseThrow().getId();
    }

    private ResultActions postUser(UserRequestDTO request) throws Exception {
        return mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)));
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedAndStoreHashedPassword() throws Exception {
        UserRequestDTO anna = new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", adminId);

        postUser(anna)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("anna")))
                .andExpect(jsonPath("$.email", is("anna@htw-berlin.de")))
                .andExpect(jsonPath("$.role.name", is("admin")))
                .andExpect(jsonPath("$.password").doesNotExist());

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        User saved = users.getFirst();
        assertThat(saved.getPassword()).isNotEqualTo("geheim123");
        assertThat(passwordEncoder.matches("geheim123", saved.getPassword())).isTrue();
    }

    @Test
    void createUser_WithExistingUsername_ShouldReturnConflict() throws Exception {
        postUser(new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", adminId))
                .andExpect(status().isCreated());

        postUser(new UserRequestDTO("anna", "geheim456", "andere@htw-berlin.de", adminId))
                .andExpect(status().isConflict());
    }

    @Test
    void createUser_WithExistingEmail_ShouldReturnConflict() throws Exception {
        postUser(new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", adminId))
                .andExpect(status().isCreated());

        postUser(new UserRequestDTO("bert", "geheim456", "anna@htw-berlin.de", adminId))
                .andExpect(status().isConflict());
    }

    @Test
    void createUser_WithUnknownRole_ShouldReturnNotFound() throws Exception {
        postUser(new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", 999L))
                .andExpect(status().isNotFound());

        assertThat(userRepository.count()).isZero();
    }
}
