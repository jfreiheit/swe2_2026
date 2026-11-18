package htw.freiheit.studi.dto;

import htw.freiheit.studi.validation.KeinTrivialesPasswort;
import htw.freiheit.studi.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Daten zum Anlegen oder Aktualisieren eines Nutzers")
public record UserRequestDTO(
        @Schema(description = "Eindeutiger Nutzername", example = "anna")
        @NotBlank(message = "Nutzername darf nicht leer sein")
        @Size(min = 3, max = 30, message = "Nutzername muss zwischen 3 und 30 Zeichen lang sein")
        @Pattern(regexp = "^[a-zA-Z0-9._-]*$",
                message = "Nutzername darf nur Buchstaben, Ziffern, Punkt, Unterstrich und Bindestrich enthalten")
        String username,

        @Schema(description = "Passwort im Klartext; wird nur gehasht gespeichert. "
                + "Pflicht beim Anlegen (POST), beim Aktualisieren (PUT) optional: "
                + "weglassen, um das bisherige Passwort zu behalten.",
                example = "geheim123", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(groups = OnCreate.class, message = "Passwort darf nicht leer sein")
        @Size(min = 8, max = 72, message = "Passwort muss zwischen 8 und 72 Zeichen lang sein")
        @KeinTrivialesPasswort
        String password,

        @Schema(description = "Eindeutige E-Mail-Adresse", example = "anna@htw-berlin.de")
        @NotBlank(message = "E-Mail-Adresse darf nicht leer sein")
        @Email(message = "E-Mail-Adresse ist ungültig")
        @Size(max = 254, message = "E-Mail-Adresse darf höchstens 254 Zeichen lang sein")
        String email,

        @Schema(description = "ID einer vorhandenen Rolle", example = "1")
        @NotNull(message = "Rolle muss angegeben werden")
        @Positive(message = "Rollen-ID muss positiv sein")
        Long roleId) {
}
