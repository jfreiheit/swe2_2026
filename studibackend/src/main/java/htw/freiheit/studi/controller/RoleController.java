package htw.freiheit.studi.controller;

import htw.freiheit.studi.dto.RoleRequestDTO;
import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@Tag(name = "Role", description = "Rollen verwalten, die Nutzern zugeordnet werden")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Rolle anlegen")
    @ApiResponse(responseCode = "201", description = "Rolle angelegt")
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleRequestDTO request) {
        RoleResponseDTO created = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Alle Rollen lesen")
    @ApiResponse(responseCode = "200", description = "Liste aller Rollen")
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @Operation(summary = "Eine Rolle lesen")
    @ApiResponse(responseCode = "200", description = "Rolle gefunden")
    @ApiResponse(responseCode = "404", description = "Rolle existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(
            @Parameter(description = "ID der Rolle", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @Operation(summary = "Rolle löschen",
            description = "Eine Rolle kann nur gelöscht werden, wenn ihr kein Nutzer mehr zugeordnet ist.")
    @ApiResponse(responseCode = "204", description = "Rolle gelöscht")
    @ApiResponse(responseCode = "404", description = "Rolle existiert nicht",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Rolle ist noch Nutzern zugeordnet",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID der Rolle", example = "1") @PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
