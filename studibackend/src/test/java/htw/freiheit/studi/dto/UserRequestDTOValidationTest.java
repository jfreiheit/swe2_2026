package htw.freiheit.studi.dto;

import htw.freiheit.studi.validation.OnCreate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserRequestDTOValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    private static UserRequestDTO valid() {
        return new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", 1L);
    }

    @Test
    void validRequest_ShouldHaveNoViolations() {
        assertThat(validator.validate(valid(), OnCreate.class)).isEmpty();
    }

    @Test
    void invalidRequest_ShouldReportEveryInvalidField() {
        UserRequestDTO request = new UserRequestDTO("a!", "kurz", "keine-mail", null);

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(request, OnCreate.class);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("username", "username", "password", "email", "roleId");
    }

    @Test
    void missingPassword_ShouldBeInvalidOnCreate() {
        UserRequestDTO request = new UserRequestDTO("anna", null, "anna@htw-berlin.de", 1L);

        assertThat(validator.validate(request, OnCreate.class))
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("Passwort darf nicht leer sein");
    }

    @Test
    void missingPassword_ShouldBeValidOnUpdate() {
        UserRequestDTO request = new UserRequestDTO("anna", null, "anna@htw-berlin.de", 1L);

        assertThat(validator.validate(request)).isEmpty();
    }
}
