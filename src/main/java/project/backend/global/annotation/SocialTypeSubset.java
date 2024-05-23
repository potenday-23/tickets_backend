package project.backend.global.annotation;

import project.backend.domain.member.entity.SocialType;
import project.backend.global.validator.SocialTypeSubsetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SocialTypeSubsetValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SocialTypeSubset {
    SocialType[] anyOf();
    String message() default "Invalid social type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}