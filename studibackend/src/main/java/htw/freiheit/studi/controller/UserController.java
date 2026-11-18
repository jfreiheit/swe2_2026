package htw.freiheit.studi.controller;

import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.dto.UserResponseDTO;
import htw.freiheit.studi.service.UserService;
import htw.freiheit.studi.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Nutzer anlegen, lesen, ändern und löschen")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Nutzer anlegen",
            description = "Legt einen neuen Nutzer an. Das Passwort wird gehasht gespeichert "
                    + "und nie zurückgegeben.")
    @ApiResponse(responseCode = "201", description = "Nutzer angelegt")
    @ApiResponse(responseCode = "400", description = "Eingabedaten ungültig (Details im Feld `errors`)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Rolle mit `roleId` existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "`username` oder `email` bereits vergeben",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Validated(OnCreate.class) @RequestBody UserRequestDTO request) {
        UserResponseDTO created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Alle Nutzer lesen")
    @ApiResponse(responseCode = "200", description = "Liste aller Nutzer (ggf. leer)")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Einen Nutzer lesen")
    @ApiResponse(responseCode = "200", description = "Nutzer gefunden")
    @ApiResponse(responseCode = "404", description = "Nutzer existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Nutzer aktualisieren",
            description = "Ersetzt `username`, `email` und Rolle. Fehlt `password`, bleibt das "
                    + "bisherige Passwort erhalten.")
    @ApiResponse(responseCode = "200", description = "Nutzer aktualisiert")
    @ApiResponse(responseCode = "400", description = "Eingabedaten ungültig (Details im Feld `errors`)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Nutzer oder Rolle existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "`username` oder `email` bereits vergeben",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Operation(summary = "Nutzer löschen")
    @ApiResponse(responseCode = "204", description = "Nutzer gelöscht")
    @ApiResponse(responseCode = "404", description = "Nutzer existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID des Nutzers", example = "1") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
