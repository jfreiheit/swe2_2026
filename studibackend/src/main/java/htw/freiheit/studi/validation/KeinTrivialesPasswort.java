package htw.freiheit.studi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Das Passwort darf nicht in einer Liste haeufig verwendeter Passwoerter stehen.
 */
@Documented
@Constraint(validatedBy = KeinTrivialesPasswortValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface KeinTrivialesPasswort {

    String message() default "Dieses Passwort ist zu leicht zu erraten";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
