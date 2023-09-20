package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.annotation.NotSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotSpaceValidator implements ConstraintValidator<NotSpace, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.hasText(s)) {
            return !StringUtils.containsWhitespace(s);
        }
        return true;
    }
}