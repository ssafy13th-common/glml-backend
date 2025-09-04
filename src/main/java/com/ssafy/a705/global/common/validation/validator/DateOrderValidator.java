package com.ssafy.a705.global.common.validation.validator;

import com.ssafy.a705.global.common.validation.annotation.DateOrder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class DateOrderValidator implements
        ConstraintValidator<DateOrder, Object> {

    private String start;
    private String end;
    private boolean allowEqual;

    @Override
    public void initialize(DateOrder annotation) {
        this.start = annotation.start();
        this.end = annotation.end();
        this.allowEqual = annotation.allowEqual();

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(value)) {
            return true;
        }

        BeanWrapperImpl bw = new BeanWrapperImpl(value);
        Object startVal = bw.getPropertyValue(start);
        Object endVal = bw.getPropertyValue(end);

        if (startVal == null || endVal == null) {
            return true;
        }

        if (!(startVal instanceof Comparable) || !(endVal instanceof Comparable)) {
            return true;
        }

        int compare = ((Comparable) endVal).compareTo(startVal);
        boolean result = allowEqual ? (compare >= 0) : (compare < 0);

        if (!result) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            constraintValidatorContext.getDefaultConstraintMessageTemplate()
                    ).addPropertyNode(end)
                    .addConstraintViolation();
        }

        return result;
    }
}
