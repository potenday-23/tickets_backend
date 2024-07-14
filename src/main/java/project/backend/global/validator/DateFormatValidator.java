package project.backend.global.validator;

import project.backend.global.annotation.ValidDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Use @NotNull for null checks
        }
        try {
            String formattedDate = date.format(FORMATTER);
            LocalDateTime.parse(formattedDate, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}