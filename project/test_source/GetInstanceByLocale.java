import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

/*
 * The following are infeasible test cases:
 * 		- [1,2,4,6,9,10] - if the table entry is equal to the invalid country value, then it will pass
 * the check at node 6, which means that it can't get to node 9 immediately.
 * 		- [1,2,4,6,7,9,11,13,14,15] - see below
 * 		- [1,2,4,6,7,9,11,13,14,16] - see below
 * 		- [1,2,4,6,7,9,11,12] - see below
 * 		- [1,2,4,6,7,9,11,13,15] - the four previous test cases all suffer from the same issue, which
 * is that if the code reaches node 7 and returns false, then the tableEntry is equal to the value of
 * INVALID_COUNTRY_ENTRY and it must branch into node 10.
 */
public class GetInstanceByLocale {

	@Test
	public void NoCountry() {
		Locale l = new Locale("en");
		try {
			Field f = Locale.class.getDeclaredField("baseLocale");
			f.setAccessible(true);
			try {
				f.set(l, null);
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
		try {
			Currency c = Currency.getInstance(l);
		} catch (NullPointerException e) {
			writeTestCase("getInstance(Locale)", "1,2,3");
		}
	}
	
	@Test
	public void IllegalCountryLength() {
		//Without specifying the country in the constructor call, it ends up being an empty string.
		Locale l = new Locale("en");
		try {
			Currency c = Currency.getInstance(l);
			fail("Should have thrown an exception for illegal country length.");
		} catch (IllegalArgumentException e) {
			writeTestCase("getInstance(Locale)", "1,2,4,5");
		}
	}
	
	@Test
	public void ValidLocale() {
		Locale l = Locale.US;
		Currency c = Currency.getInstance(l);
		assertEquals(c.getCurrencyCode(), "USD");
		assertEquals(c.getDefaultFractionDigits(), 2);
		assertEquals(c.getNumericCode(), 840);
		assertEquals(c.getSymbol(l), "$");
		String s = c.getSymbol(new Locale("AQ", "AQ"));
		assertEquals(s, c.toString());
		writeTestCase("getInstance(Locale)", "1,2,4,6,7,8");
	}
	

	@Test
	public void SimpleCaseCountry() {
		try {
		Set<Currency> c = Currency.getAvailableCurrencies();
		Class<?>[] params = {char.class, char.class, int.class};
		Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
		m.setAccessible(true);
		try {
			m.invoke(null, 'Z', 'Z', 0);
			Currency c2 = Currency.getInstance(new Locale("ZZ", "ZZ"));
		} catch (IllegalArgumentException e) {
			fail("Unexpected exception.");
		} catch (IllegalAccessException e) {
			fail("Unexpected exception.");
		} catch (InvocationTargetException e) {
			fail("Unexpected exception.");
		}
	} catch (SecurityException e1) {
		fail("Unexpected exception.");
	} catch (NoSuchMethodException e1) {
		fail("Unexpected exception.");
	}
	writeTestCase("getInstance(Locale)", "1,2,4,6,8");
	}
	
	@Test
	public void InvalidCountryEntry() {
		try {
			Locale l = new Locale("zz", "zz");
			Currency c = Currency.getInstance(l);
			fail("Should have thrown an exception for an invalid country entry.");
		} catch (IllegalArgumentException e) {
			writeTestCase("getInstance(Locale)", "1,2,4,6,7,9,10");
		}
	}
	
	@Test
	public void CountryWithoutCurrency() {
		Locale l = new Locale("AQ", "AQ");
		Currency c = Currency.getInstance(l);
		assertNull(c);
		writeTestCase("getInstance(Locale)", "1,2,4,6,9,11,12");
	}
	
	@Test
	public void GetCurrencyCodeInsteadOfSymbol() {
		Currency c = Currency.getInstance(Locale.US);
		String s = c.getSymbol(new Locale("AQ", "AQ"));
		assertEquals(s, c.toString());
	}
	
	@Test
	public void GetOldCurrencies_CutOverTimeMax() {
		try {
			Set<Currency> c = Currency.getAvailableCurrencies();
			Field f = Currency.class.getDeclaredField("scOldCurrencies");
			f.setAccessible(true);
			Class<?>[] params = {char.class, char.class, int.class};
			Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
			m.setAccessible(true);
			Field f2 = Currency.class.getDeclaredField("scCutOverTimes");
			f2.setAccessible(true);
			try {
				String[] oldCurs = (String[])f.get(null);
				m.invoke(null, 'Z', 'Z', 1000);
				oldCurs[oldCurs.length - 1] = "ZZA";
				int index = (1000 & 31) - 1;
				long[] scCut = (long[]) f2.get(null);
				scCut[index] = Long.MAX_VALUE ;
				Currency c2 = Currency.getInstance(new Locale("ZZ", "ZZ"));
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
		writeTestCase("getInstance(Locale)", "1,2,4,6,9,11,13,15");
	}
	
	@Test
	public void GetOldCurrencies_CutOverTimeGreaterThanSystem() {
		try {
			Set<Currency> c = Currency.getAvailableCurrencies();
			Field f = Currency.class.getDeclaredField("scOldCurrencies");
			f.setAccessible(true);
			Class<?>[] params = {char.class, char.class, int.class};
			Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
			m.setAccessible(true);
			try {
				String[] oldCurs = (String[])f.get(null);
				m.invoke(null, 'Z', 'Z', 1000);
				oldCurs[oldCurs.length - 1] = "ZZA";
				Currency c2 = Currency.getInstance(new Locale("ZZ", "ZZ"));
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
		writeTestCase("getInstance(Locale)", "1,2,4,6,9,11,13,14,15");
	}
	
	@Test
	public void GetNewCurrencies() {
		try {
		Set<Currency> c = Currency.getAvailableCurrencies();
		Field f = Currency.class.getDeclaredField("scNewCurrencies");
		f.setAccessible(true);
		Field f2 = Currency.class.getDeclaredField("scCutOverTimes");
		f2.setAccessible(true);
		Class<?>[] params = {char.class, char.class, int.class};
		Method m = Currency.class.getDeclaredMethod("setMainTableEntry", params);
		m.setAccessible(true);
		try {
			String[] newCurs = (String[])f.get(null);
			m.invoke(null, 'Z', 'Z', 1000);
			int index = (1000 & 31) - 1;
			long[] scCut = (long[]) f2.get(null);
			scCut[index] = System.currentTimeMillis() - 1000 ;
			newCurs[newCurs.length - 1] = "ZZA";
			Currency c2 = Currency.getInstance(new Locale("ZZ", "ZZ"));
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
	writeTestCase("getInstance(Locale)", "1,2,4,6,9,11,13,14,16");
	}

	private void writeTestCase(String functionName, String path) {
		System.out.println(String.format("Function: %s    Path Covered: %s", functionName, path));
	}
}
