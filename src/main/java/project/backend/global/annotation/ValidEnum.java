package project.backend.global.annotation;

import project.backend.global.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "유효한 값은 {enumValues} 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}