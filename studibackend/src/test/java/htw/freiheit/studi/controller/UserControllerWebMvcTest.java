package htw.freiheit.studi.controller;

import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.dto.UserResponseDTO;
import htw.freiheit.studi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerWebMvcTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private UserService userService;

    private final UserResponseDTO anna =
            new UserResponseDTO(1L, "anna", "anna@htw-berlin.de", new RoleResponseDTO(2L, "admin"));

    @Test
    void createUser_WithValidJson_ShouldReturnCreatedAndUser() {
        when(userService.create(any(UserRequestDTO.class))).thenReturn(anna);

        String json = """
                {
                  "username": "anna",
                  "password": "geheim123",
                  "email": "anna@htw-berlin.de",
                  "roleId": 2
                }
                """;

        assertThat(mvc.post().uri("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .doesNotHavePath("$.password")
                .extractingPath("$.username").isEqualTo("anna");

        verify(userService).create(new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", 2L));
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {
        when(userService.findAll()).thenReturn(List.of(anna));

        assertThat(mvc.get().uri("/user"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[0].role.name").isEqualTo("admin");
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        when(userService.findById(42L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User nicht gefunden: 42"));

        assertThat(mvc.get().uri("/user/{id}", 42))
                .hasStatus(HttpStatus.NOT_FOUND);
    }
}
