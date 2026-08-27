package htw.freiheit.studi.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderConfigTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoderConfig().passwordEncoder();

    @Test
    void encodesAndMatchesPassword() {
        String rawPassword = "geheim123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }
}
