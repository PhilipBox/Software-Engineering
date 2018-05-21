package test;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestRunner {

	@Test
	public void EqualError() {
		Rectangle cal = new Rectangle();
		
		cal.setW(2);
		cal.setH(3);
		assertEquals(5, cal.getA());
	}
	
	@Test
	public void ConditionError() {
		Rectangle cal = new Rectangle();
		
		cal.setW(2);
		cal.setH(-1);
		assertTrue(cal.getA()>0);
	}
	
	@Test
	public void NotNullError() {
		Rectangle cal = new Rectangle();
		
		cal.setW(5);
		cal.setH(10);
		assertNotNull(cal.toString());
	}
}
