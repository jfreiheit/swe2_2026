package htw.freiheit.studi.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class KeinTrivialesPasswortValidatorTest {

    private final KeinTrivialesPasswortValidator validator = new KeinTrivialesPasswortValidator();

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "passwort", "Passwort", "PASSWORD", "qwertz123"})
    void trivialPassword_ShouldBeInvalid(String passwort) {
        assertThat(validator.isValid(passwort, null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"geheim123", "Fahrrad-Batterie-Heftklammer", "x7#kL9!q"})
    void otherPassword_ShouldBeValid(String passwort) {
        assertThat(validator.isValid(passwort, null)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    void nullPassword_ShouldBeValid(String passwort) {
        assertThat(validator.isValid(passwort, null)).isTrue();
    }
}
