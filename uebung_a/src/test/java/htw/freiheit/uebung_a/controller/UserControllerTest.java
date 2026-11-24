package htw.freiheit.uebung_a.controller;

import htw.freiheit.uebung_a.dto.UserRequestDTO;
import htw.freiheit.uebung_a.entity.Role;
import htw.freiheit.uebung_a.repository.RoleRepository;
import htw.freiheit.uebung_a.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class UserControllerTest
{
    private final MockMvc mockMvc;
    private final JsonMapper jsonMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserControllerTest(UserRepository userRepository,
                              RoleRepository roleRepository,
                              MockMvc mockMvc,
                              JsonMapper jsonMapper,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mockMvc = mockMvc;
        this.jsonMapper = jsonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private ResultActions registerUser(UserRequestDTO userRequestDTO) throws Exception
    {
        return mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userRequestDTO)));
    }

    @Test
    public void createUser_WithValidData_ShouldReturnCreated() throws Exception
    {
        Role role = new Role();
        role.setRole("user");
        roleRepository.save(role);

        UserRequestDTO maria = new UserRequestDTO("maria@test.de", "geheim123");
        registerUser(maria)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("maria@test.de"));

        roleRepository.delete(role);
    }
}
