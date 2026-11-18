package htw.freiheit.studi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;
import java.util.Set;

public class KeinTrivialesPasswortValidator implements ConstraintValidator<KeinTrivialesPasswort, String> {

    // In der Praxis: eine deutlich laengere Liste (z.B. aus einer Datei) oder ein Abgleich mit
    // bekannten Datenlecks. Hier genuegen einige Beispiele.
    private static final Set<String> TRIVIALE_PASSWOERTER = Set.of(
            "12345678", "123456789", "1234567890", "passwort", "password", "qwertz123",
            "qwerty123", "hallo123", "passwort1", "password1", "iloveyou", "11111111");

    @Override
    public boolean isValid(String passwort, ConstraintValidatorContext context) {
        // null gilt als gueltig: ob ein Wert vorhanden sein muss, pruefen @NotNull bzw. @NotBlank
        return passwort == null || !TRIVIALE_PASSWOERTER.contains(passwort.toLowerCase(Locale.ROOT));
    }
}
