package com.validation.strategy;

import java.lang.reflect.Field;
import java.util.Optional;

import com.validation.annotation.NrIndeksu;

public class NrIndeksuStrategy implements ValidationStrategy {

	@Override
	public Optional<String> validate(Field field, Object value) {
			
		if (value == null) {
            return Optional.empty(); 
        }
		
		if (field.isAnnotationPresent(NrIndeksu.class) && value.toString().length() != 8) {
			
			NrIndeksu annotation = field.getAnnotation(NrIndeksu.class); 
			String errorInfo = String.format("Pole %s: %s", field.getName(), annotation.message()); 
			return Optional.of(errorInfo); 
		} 
		return Optional.empty();
	}
}
