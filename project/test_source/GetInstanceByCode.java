import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Currency;

import static org.junit.Assert.*;
import org.junit.Test;

import junit.framework.Assert;

/*
 * The following test paths are infeasible:
 * 		- Any path that ends at node 19 - we can only get to this endpoint if the instance already 
 * exists, which is not possible if it was not caught at node 3.
 */
public class GetInstanceByCode {
	
	@Test
	public void AlreadyHaveInstance() {
		Currency c1 = Currency.getAvailableCurrencies().iterator().next();
		Currency c2 = Currency.getInstance(c1.getCurrencyCode());
		assertEquals(c1, c2);
		writeTestCase("getInstance(String, int, int)", "1,2,3,4");
	}
	
	@Test
	public void IllegalCurrencyCodeLength() {
		try {
			Currency c = Currency.getInstance("XXXX");
			fail("Should have thrown an exception for an illegal currency code length.");
		} catch (IllegalArgumentException e) {
			writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,7");
		}
	}

	@Test
	public void IllegalHyphenInstance() {
		try {
			Currency c = Currency.getInstance("XX-");
			fail("Should have thrown an exception for having a hyphen in the name.");
		} catch (IllegalArgumentException e) {
			writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,10,12,13");
		}
	}
	
	@Test
	public void UnknownInstance() {
		try {
			Currency c = Currency.getInstance("ZZZ");
			fail("Should have thrown an exception");
		} catch (IllegalArgumentException e) {
			writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,10,12,14,15");
		}
	}
	
	@Test
	public void CustomCurrency() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			try {
				Currency c = (Currency) m.invoke(null, "ZZZ", 2, 999);
				assertEquals(c.getCurrencyCode(), "ZZZ");
				assertEquals(c.getDefaultFractionDigits(), 2);
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,17,18,20");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void SimpleCountry() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'Z', 'Z', 31);
				Currency c = (Currency) m.invoke(null, "ZZ`", Integer.MIN_VALUE, 999);
				assertEquals("ZZ`", c.getCurrencyCode());
				assertEquals(0, c.getDefaultFractionDigits());
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,10,11,17,18,20");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void SuccessInvalidCountry() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'X', 'X', 127);
				Currency c = (Currency) m.invoke(null, "XXX", Integer.MIN_VALUE, 999);
				assertEquals("XXX", c.getCurrencyCode());
				assertEquals(-1, c.getDefaultFractionDigits());
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,12,14,16,17,18,20");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void SuccessNotSimple() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'X', 'X', 129);
				Currency c = (Currency) m.invoke(null, "XXX", Integer.MIN_VALUE, 999);
				assertEquals("XXX", c.getCurrencyCode());
				assertEquals(-1, c.getDefaultFractionDigits());
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,12,14,16,17,18,20");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void HyphenatedInvalidTableEntry() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'X', 'X', 127);
				Currency c = (Currency) m.invoke(null, "XX-", Integer.MIN_VALUE, 999);
				fail("Expected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,12,13");
			} catch (InvocationTargetException e) {
				//exception should be IllegalArgumentException, but we won't get it because of reflection
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,12,13");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void HyphenatedNotSimple() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'X', 'X', 129);
				Currency c = (Currency) m.invoke(null, "XX-", Integer.MIN_VALUE, 999);
				fail("Expected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,12,13");
			} catch (InvocationTargetException e) {
				//exception should be IllegalArgumentException, but we won't get it because of reflection
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,12,13");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void NonexistantNotSimple() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'Z', 'Z', 129);
				Currency c = (Currency) m.invoke(null, "ZZZ", Integer.MIN_VALUE, 999);
				fail("Expected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,12,14,15");
			} catch (InvocationTargetException e) {
				//exception should be IllegalArgumentException, but we won't get it because of reflection
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,12,14,15");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void NonexistantNotValid() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			try {
				m2.invoke(null, 'Z', 'Z', 127);
				Currency c = (Currency) m.invoke(null, "ZZZ", Integer.MIN_VALUE, 999);
				fail("Expected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,12,14,15");
			} catch (InvocationTargetException e) {
				//exception should be IllegalArgumentException, but we won't get it because of reflection
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,12,14,15");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void SimpleCustomCountry() {
		Class<?>[] params = {String.class, int.class, int.class};
		try {
			Method m = Currency.class.getDeclaredMethod("getInstance", params);
			m.setAccessible(true);
			Class<?>[] params2 = {char.class, char.class, int.class};
			Method m2 = Currency.class.getDeclaredMethod("setMainTableEntry", params2);
			m2.setAccessible(true);	
			Field f = Currency.class.getDeclaredField("otherCurrencies");
			f.setAccessible(true);
			try {
				String otherCurs = (String)f.get(null);
				f.set(null, otherCurs.substring(0, otherCurs.length() - 4) + "-ZZ!");
				m2.invoke(null, 'Z', 'Z', 31);
				Currency c = (Currency) m.invoke(null, "ZZ!", Integer.MIN_VALUE, 999);
				assertEquals("ZZ!", c.getCurrencyCode());
				assertEquals(2, c.getDefaultFractionDigits());
				writeTestCase("getInstance(String, int, int)", "1,2,3,5,6,8,9,10,12,14,16,17,18,20");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchMethodException e) {
			fail("Unexpected exception.");
		} catch (SecurityException e) {
			fail("Unexpected exception.");
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		}
	}
	
	private void writeTestCase(String functionName, String path) {
		System.out.println(String.format("Function: %s    Path Covered: %s", functionName, path));
	}
}
