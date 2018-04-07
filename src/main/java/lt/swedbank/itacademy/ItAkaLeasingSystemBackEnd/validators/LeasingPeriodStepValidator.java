package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.validators;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.constraints.LeasingPeriodStepConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Lukas
 */
public class LeasingPeriodStepValidator implements ConstraintValidator<LeasingPeriodStepConstraint, Integer> {

    @Override
    public void initialize(LeasingPeriodStepConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null){
            return false;
        }
        return value%6 == 0;
    }
}
