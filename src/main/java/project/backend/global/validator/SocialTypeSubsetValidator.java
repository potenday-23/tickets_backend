package project.backend.global.validator;

import project.backend.domain.member.entity.SocialType;
import project.backend.global.annotation.SocialTypeSubset;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SocialTypeSubsetValidator implements ConstraintValidator<SocialTypeSubset, SocialType> {
    private SocialType[] subset;

    @Override
    public void initialize(SocialTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(SocialType value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.asList(subset).contains(value);
    }
}