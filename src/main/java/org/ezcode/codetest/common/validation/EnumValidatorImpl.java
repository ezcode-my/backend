package org.ezcode.codetest.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

	private Class<? extends Enum<?>> enumClass;

	@Override
	public void initialize(EnumValidator annotation) {
		this.enumClass = annotation.enumClass();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return true;

		Object[] enumConstants = enumClass.getEnumConstants();
		if (enumConstants == null) return false;

		boolean matched = Arrays.stream(enumConstants)
			.map(e -> ((Enum<?>) e).name())
			.anyMatch(name -> name.equals(value.trim().toUpperCase()));

		if (!matched) {
			context.disableDefaultConstraintViolation();

			String allowed = Arrays.stream(enumConstants)
				.map(e -> ((Enum<?>) e).name())
				.collect(Collectors.joining(", "));

			context.buildConstraintViolationWithTemplate("허용된 값: " + allowed)
				.addConstraintViolation();
		}

		return matched;
	}
}
