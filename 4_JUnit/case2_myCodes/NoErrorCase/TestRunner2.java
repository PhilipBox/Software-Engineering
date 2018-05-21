package test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class TestRunner2 {
	
	@Test
	public void equalsError() {
		Calculator cal = new Calculator();
		cal.setX(1,4);
		cal.setY(1,1);
		assertEquals(3.0, cal.getDistance());
	}
	
	
	@Test
	public void ConditionError() {
		Calculator cal = new Calculator();
		
		cal.setX(2,3);
		cal.setY(2,9);
		assertTrue(cal.getDistance()>0);
	}
	
	@Test
	public void NotNullError() {
		Calculator cal = new Calculator();
		cal.setX(1,2);
		cal.setY(1,1);
		assertNotNull(cal.toString());
	}
	
}


