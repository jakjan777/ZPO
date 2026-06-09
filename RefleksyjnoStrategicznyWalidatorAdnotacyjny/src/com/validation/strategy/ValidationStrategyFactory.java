package com.validation.strategy;

import java.lang.annotation.Annotation; 
import java.util.HashMap; 
import java.util.Map; 
import com.validation.annotation.*; 

public class ValidationStrategyFactory {
	// fragment Class<? extends Annotation> określa, że kluczem mapy mogą być  
	// tylko klasy, które rozszerzają lub implementują interfejs Annotation 
	private static final Map<Class<? extends Annotation>, ValidationStrategy>  
	strategies = new HashMap<>(); 
	static { 
		strategies.put(NotNull.class, new NotNullStrategy()); 
		
		strategies.put(NrIndeksu.class, new NrIndeksuStrategy()); 
		
		strategies.put(NotEmpty.class, new NotEmptyStrategy()); 
		
		strategies.put(Size.class, new SizeStrategy()); 
		
		strategies.put(Email.class, new EmailStrategy()); 
		
	} 
	private ValidationStrategyFactory() { 
	} 
	public static ValidationStrategy getStrategy(Annotation annotation) { 
		return strategies.get(annotation.annotationType()); 
	}
}
