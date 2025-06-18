package org.ezcode.codetest.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EnumValidatorImpl.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidator {

	Class<? extends Enum<?>> enumClass();

	String message() default "허용되지 않은 값입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
