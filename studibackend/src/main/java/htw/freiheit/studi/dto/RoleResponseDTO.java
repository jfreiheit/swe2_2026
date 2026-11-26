package htw.freiheit.studi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Eine Rolle, wie sie die API zurückgibt")
public record RoleResponseDTO(
        @Schema(description = "Von der Datenbank vergebene ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(example = "user", requiredMode = Schema.RequiredMode.REQUIRED)
        String name) {
}
