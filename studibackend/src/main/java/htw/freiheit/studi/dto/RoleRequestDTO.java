package htw.freiheit.studi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Daten zum Anlegen einer Rolle")
public record RoleRequestDTO(
        @Schema(description = "Eindeutiger Name der Rolle", example = "tutor",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name) {
}
