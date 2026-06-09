package com.validation.strategy;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.regex.Pattern;
import com.validation.annotation.Email;

public class EmailStrategy implements ValidationStrategy {

	@Override
	public Optional<String> validate(Field field, Object value) {
		
		if (value == null) {
            return Optional.empty(); 
        }
		
		String emailString = value.toString();
		
		String emailPattern = "^[\\p{L}0-9._%+-]+@[\\p{L}0-9.-]+\\.[\\p{L}]{2,}$";
		
		boolean regex = Pattern.matches(emailPattern, emailString);
		
		if (field.isAnnotationPresent(Email.class) && !regex) {
		
			Email annotation = field.getAnnotation(Email.class);
			String errorInfo = String.format("Pole %s: %s", field.getName(), annotation.message());
			return Optional.of(errorInfo);

		}
		return Optional.empty();
	
	}	
}