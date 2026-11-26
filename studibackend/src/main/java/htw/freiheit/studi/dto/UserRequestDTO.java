package htw.freiheit.studi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Daten zum Anlegen oder Aktualisieren eines Nutzers")
public record UserRequestDTO(
        @Schema(description = "Eindeutiger Nutzername", example = "anna",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @Schema(description = "Passwort im Klartext; wird nur gehasht gespeichert. "
                + "Pflicht beim Anlegen (POST), beim Aktualisieren (PUT) optional: "
                + "leer lassen, um das bisherige Passwort zu behalten.",
                example = "geheim123", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
        String password,

        @Schema(description = "Eindeutige E-Mail-Adresse", example = "anna@htw-berlin.de", format = "email",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "ID einer vorhandenen Rolle", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Long roleId) {
}
