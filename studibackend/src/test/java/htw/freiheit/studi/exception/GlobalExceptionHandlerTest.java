package htw.freiheit.studi.exception;

import htw.freiheit.studi.controller.UserController;
import htw.freiheit.studi.dto.UserRequestDTO;
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
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class GlobalExceptionHandlerTest {

    private static final String VALID_USER = """
            {"username": "anna", "password": "geheim123", "email": "anna@htw-berlin.de", "roleId": 1}
            """;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private UserService userService;

    @Test
    void resourceNotFound_ShouldReturn404AsProblemDetail() {
        when(userService.findById(42L)).thenThrow(new ResourceNotFoundException("User", 42L));

        assertThat(mvc.get().uri("/user/{id}", 42))
                .hasStatus(HttpStatus.NOT_FOUND)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {
                          "status": 404,
                          "title": "Ressource nicht gefunden",
                          "detail": "User nicht gefunden: 42",
                          "instance": "/user/42"
                        }
                        """);
    }

    @Test
    void conflict_ShouldReturn409AsProblemDetail() {
        when(userService.create(any(UserRequestDTO.class)))
                .thenThrow(new ConflictException("username bereits vergeben: anna"));

        assertThat(mvc.post().uri("/user").contentType(MediaType.APPLICATION_JSON).content(VALID_USER))
                .hasStatus(HttpStatus.CONFLICT)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {"status": 409, "detail": "username bereits vergeben: anna"}
                        """);
    }

    @Test
    void unexpectedException_ShouldReturn500WithoutInternalDetails() {
        when(userService.findById(1L))
                .thenThrow(new IllegalStateException("Verbindung zu jdbc:postgresql://db:5432 fehlgeschlagen"));

        assertThat(mvc.get().uri("/user/{id}", 1))
                .hasStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {"status": 500, "detail": "Ein unerwarteter Fehler ist aufgetreten."}
                        """);
    }

    @Test
    void malformedJson_ShouldReturn400AsProblemDetail() {
        assertThat(mvc.post().uri("/user").contentType(MediaType.APPLICATION_JSON).content("{ kaputt"))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().isLenientlyEqualTo("""
                        {"status": 400, "title": "Bad Request"}
                        """);
    }
}
