import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Currency;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;

/*
 * There are a number of Prime-Path entries that are not coverable for this function, given
 * the structure of the first two loops. There is no way for us to cause the loops to end early,
 * which means that any path that tries to go follow the paths [4,12] and [6,11] without going
 * into their respective loops are invalid.
 */
public class GetAvailableCurrencies {

	@Test
	public void AssertAvailableCurrenciesExist() {
		try {
			Field f = Currency.class.getDeclaredField("available");
			f.setAccessible(true);
			try {
				assertNotNull("'available' field set to null.", f.get(null));
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		writeTestCase("getAvailableCurrencies", "1,2,14,15");
	}
	
	@Test
	public void AssertAvailableCurrenciesDoNotExist() {
		try {
			Field f = Currency.class.getDeclaredField("available");
			f.setAccessible(true);
			try {
				f.set(null, null);
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		writeTestCase("getAvailableCurrencies", "1,2,3,4,5,6,7,8,9,10,6,7,8,9.10,6,11,4,12,13,12,13,12,14,15");
	}
	
	@Test
	public void AssertAvailableNull_NotAllOtherCurrencies() {
		try {
			Field f = Currency.class.getDeclaredField("otherCurrencies");
			f.setAccessible(true);
			try {
				f.set(null, "ZZA");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		for (Currency c : s) {
			if (c.getCurrencyCode().equalsIgnoreCase("ZZZ")) {
				fail("Found currency that shouldn't exist.");
			}
		}
		writeTestCase("getAvailableCurrencies", "1,2,3,4,5,6,7,8,9,10,6,7,8,9.10,6,11,4,12,13,12,14,15");
	}
	
	@Test
	public void AssertAvailableNull_NoOtherCurrencies() {
		try {
			Field f = Currency.class.getDeclaredField("otherCurrencies");
			f.setAccessible(true);
			try {
				f.set(null, "");
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		for (Currency c : s) {
			if (c.getCurrencyCode().equalsIgnoreCase("ZZZ")) {
				fail("Found currency that shouldn't exist.");
			}
		}
		writeTestCase("getAvailableCurrencies", "1,2,3,4,5,6,7,8,9,10,6,7,8,9.10,6,11,4,12,14,15");
	}
	
	@Test
	public void InvalidTableEntry_DoesntMatchMask() {
		try {
			Field f = Currency.class.getDeclaredField("available");
			f.setAccessible(true);
			Class<?>[] params = {char.class, char.class, int.class};
			Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
			m.setAccessible(true);
			try {
				f.set(null, null);
				m.invoke(null, 'U', 'S', 129);
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		} catch (NoSuchMethodException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		for (Currency c : s) {
			if (c.getCurrencyCode() == "USD") {
				fail("The USD currency shouldn't be included in the list.");
			}
		}
		writeTestCase("getAvailableCurrencies", "1,2,3,4,5,6,7,10,6,7,8,9.10,6,11,4,12,14,15");
	}
	
	@Test
	public void InvalidTableEntry_InvalidCountryEntry() {
		try {
			Field f = Currency.class.getDeclaredField("available");
			f.setAccessible(true);
			Class<?>[] params = {char.class, char.class, int.class};
			Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
			m.setAccessible(true);
			try {
				f.set(null, null);
				m.invoke(null, 'U', 'S', 0x0000007F);
			} catch (IllegalArgumentException e) {
				fail("Unexpected exception.");
			} catch (IllegalAccessException e) {
				fail("Unexpected exception.");
			} catch (InvocationTargetException e) {
				fail("Unexpected exception.");
			}
		} catch (NoSuchFieldException e1) {
			fail("Unexpected exception.");
		} catch (SecurityException e1) {
			fail("Unexpected exception.");
		} catch (NoSuchMethodException e1) {
			fail("Unexpected exception.");
		}
		Set<Currency> s = Currency.getAvailableCurrencies();
		assertNotNull("No currencies returned.", s);
		for (Currency c : s) {
			if (c.getCurrencyCode() == "USD") {
				fail("The USD currency shouldn't be included in the list.");
			}
		}
		writeTestCase("getAvailableCurrencies", "1,2,3,4,5,6,7,8,10,6,7,8,9.10,6,11,4,12,14,15");
	}

	private void writeTestCase(String functionName, String path) {
		System.out.println(String.format("Function: %s    Path Covered: %s", functionName, path));
	}

}
