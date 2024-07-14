package project.backend.global.validator;


import project.backend.global.annotation.ValidEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private String message;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 값 허용. 허용하지 않으려면 false 반환.
        }

        boolean isValid = Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equalsIgnoreCase(value));

        if (!isValid) {
            String enumValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.replace("{enumValues}", enumValues))
                    .addConstraintViolation();
        }

        return isValid;
    }
}