package htw.freiheit.studi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Daten zum Anlegen einer Rolle")
public record RoleRequestDTO(
        @Schema(description = "Eindeutiger Name der Rolle (nur Kleinbuchstaben)", example = "tutor")
        @NotBlank(message = "Rollenname darf nicht leer sein")
        @Size(max = 30, message = "Rollenname darf höchstens 30 Zeichen lang sein")
        @Pattern(regexp = "^[a-z]*$", message = "Rollenname darf nur Kleinbuchstaben enthalten")
        String name) {
}
