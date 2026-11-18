package htw.freiheit.studi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ein Nutzer, wie ihn die API zurückgibt (ohne Passwort)")
public record UserResponseDTO(
        @Schema(description = "Von der Datenbank vergebene ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(example = "anna", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @Schema(example = "anna@htw-berlin.de", format = "email", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        RoleResponseDTO role) {
}
