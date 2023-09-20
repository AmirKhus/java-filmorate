package ru.yandex.practicum.filmorate.exceptions.annotation;

import ru.yandex.practicum.filmorate.exceptions.NotSpaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NotSpaceValidator.class)
@Documented
public @interface NotSpace {

    String message() default "{NotSpaceValidator.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}