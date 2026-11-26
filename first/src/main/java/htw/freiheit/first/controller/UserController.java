package htw.freiheit.first.controller;

import htw.freiheit.first.dto.UserRequestDTO;
import htw.freiheit.first.dto.UserResponseDTO;
import htw.freiheit.first.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Nutzerin anlegen, lesen, ändern und löschen")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Nutzerin registrieren",
            description = "Legt eine neue Nutzerin an. Das Passwort wird gehasht gespeichert "
                    + "und nie zurückgegeben.")
    @ApiResponse(responseCode = "201", description = "Nutzerin angelegt")
    @ApiResponse(responseCode = "409", description = "`account` oder `email` bereits vergeben",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Nutzerin einloggen",
            description = "Loggt eine Nutzerin ein.")
    @ApiResponse(responseCode = "200", description = "Nutzerin eingeloggt")
    @ApiResponse(responseCode = "403", description = "account oder email existiert nicht oder Passwort falsch",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO loggedInUser = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
    }

    @Operation(summary = "Alle Nutzerinnen lesen")
    @ApiResponse(responseCode = "200", description = "Liste aller Nutzerinnen (ggf. leer)")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Eine Nutzerin lesen")
    @ApiResponse(responseCode = "200", description = "Nutzerin gefunden")
    @ApiResponse(responseCode = "404", description = "Nutzerin existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Nutzerin aktualisieren",
            description = "Ersetzt `username`, `email` und Rolle. Ist `password` leer, bleibt das "
                    + "bisherige Passwort erhalten.")
    @ApiResponse(responseCode = "200", description = "Nutzerin aktualisiert")
    @ApiResponse(responseCode = "404", description = "Nutzerin oder Rolle existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "`username` oder `email` bereits vergeben",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id,
            @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Operation(summary = "Nutzerin löschen")
    @ApiResponse(responseCode = "204", description = "Nutzerin gelöscht")
    @ApiResponse(responseCode = "404", description = "Nutzerin existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

