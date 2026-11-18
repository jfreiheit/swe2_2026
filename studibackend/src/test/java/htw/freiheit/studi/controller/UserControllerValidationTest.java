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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerValidationTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_WithInvalidData_ShouldReturn400WithFieldErrors() {
        String json = """
                {"username": "", "password": "12345678", "email": "keine-mail", "roleId": 1}
                """;

        assertThat(mvc.post().uri("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {
                          "status": 400,
                          "detail": "Die Eingabedaten sind ungültig.",
                          "errors": {
                            "email": ["E-Mail-Adresse ist ungültig"],
                            "password": ["Dieses Passwort ist zu leicht zu erraten"],
                            "username": [
                              "Nutzername darf nicht leer sein",
                              "Nutzername muss zwischen 3 und 30 Zeichen lang sein"
                            ]
                          }
                        }
                        """);

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_WithoutPassword_ShouldReturn400() {
        String json = """
                {"username": "anna", "email": "anna@htw-berlin.de", "roleId": 1}
                """;

        assertThat(mvc.post().uri("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.errors.password[0]").isEqualTo("Passwort darf nicht leer sein");

        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_WithoutPassword_ShouldBeAccepted() {
        UserResponseDTO anna = new UserResponseDTO(1L, "anna", "anna@htw-berlin.de", new RoleResponseDTO(1L, "user"));
        when(userService.update(eq(1L), any(UserRequestDTO.class))).thenReturn(anna);
        String json = """
                {"username": "anna", "email": "anna@htw-berlin.de", "roleId": 1}
                """;

        assertThat(mvc.put().uri("/user/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(json))
                .hasStatusOk();

        verify(userService).update(1L, new UserRequestDTO("anna", null, "anna@htw-berlin.de", 1L));
    }
}
