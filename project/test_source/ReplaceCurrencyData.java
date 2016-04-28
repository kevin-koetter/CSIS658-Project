import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/*
 * The following are infeasible:
 * 		- Any paths that include both nodes 17 and 20, as we can not be at the length of the
 * array scOldCurrencies and have broken from the loop early. 
 * 		- Any paths that do not go through the loop but follow the path [15,19,21], as the index value
 * will be 0 and the length of the scOldCurrencies array must also be 0.
 */
public class ReplaceCurrencyData {
	
	private Pattern p = Pattern.compile("([A-Z]{3})\\s*,\\s*(\\d{3})\\s*,\\s*" +
            "(\\d+)\\s*,?\\s*(\\d{4}-\\d{2}-\\d{2}T\\d{2}:" +
            "\\d{2}:\\d{2})?");

	@Test
	public void InvalidCountryLength() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "A", "999");
				writeTestCase("replaceCurrencyData", "1,2,3");
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
	public void ValidCurrencyData_NoDate_RightNumberCommas_NotOldCurrency() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "MG", "MGG,999,9");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,8,12,14,15,16,18,15,16,18,15,19,20,22");
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
	public void ValidCurrencyData_NoDate_RightNumberCommas_OldCurrency() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "UK", "GBP,999,9");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,8,12,14,15,16,18,15,16,17,19,21,22");
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
	public void InvalidCurrencyData_BigDecimal() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "MG", "MGG,999,1000,2000-01-01T00:00:00");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,8,12,13");
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
	public void InvalidCurrencyData_NotPastCutoverDate() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "MG", "MGG,999,9,2019-01-01T00:00:00");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,8,9,11");
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
	public void InvalidCurrencyData_MalformedDate() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "US", "ZZZ,999,10,2000-01-01T00:00:90");
				writeTestCase("replaceCurrencyData", "1,2,4,5,8,9,10");
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
	public void WrongFormatCurrencyData() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "AQ", "-,-,-");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,7");
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
	public void NoOldCurrencies() {
		Class<?>[] params = {Pattern.class, String.class, String.class};
		try {
			Field f = Currency.class.getDeclaredField("scOldCurrencies");
			f.setAccessible(true);
			f.set(null,  new String[0]);
			Method m = Currency.class.getDeclaredMethod("replaceCurrencyData", params);
			m.setAccessible(true);
			try {
				m.invoke(null, p, "MG", "MGG,999,9,2009-01-01T00:00:00");
				writeTestCase("replaceCurrencyData", "1,2,4,5,6,7");
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
		} catch (IllegalArgumentException e1) {
			fail("Unexpected exception.");
		} catch (IllegalAccessException e1) {
			fail("Unexpected exception.");
		}
	}
	

	private void writeTestCase(String functionName, String path) {
		System.out.println(String.format("Function: %s    Path Covered: %s", functionName, path));
	}
}
