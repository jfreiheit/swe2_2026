package htw.freiheit.uebung_b.controller;

import htw.freiheit.uebung_b.dto.UserRequestDTO;
import htw.freiheit.uebung_b.entity.Role;
import htw.freiheit.uebung_b.repository.RoleRepository;
import htw.freiheit.uebung_b.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc( addFilters = false )
public class UserControllerTest
{
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JsonMapper jsonMapper;

    @Autowired
    public UserControllerTest( MockMvc mockMvc,
                               UserRepository userRepository,
                               RoleRepository roleRepository,
                               JsonMapper jsonMapper )
    {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jsonMapper = jsonMapper;
    }

    private ResultActions registerUser(UserRequestDTO request) throws Exception {
        return mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)));
    }

    @Test
    public void registerUser_WithValidData_ExpectCreated() throws Exception {
        UserRequestDTO request = new UserRequestDTO("testuser", "test@email.de", "geheim123");

        Role userRole = new Role();
        userRole.setRole("user");
        roleRepository.save(userRole);

        ResultActions result = registerUser(request);
        result.andExpect(status().isCreated());

    }
}
