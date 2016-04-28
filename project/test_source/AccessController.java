import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Currency;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * It is not possible for us to cover all of the testing scenarios for the function under test.
 * There are 2 reasons for this, both of which come from the same root cause.
 * 		1. The beginning of this function uses an internal file, currency.data, to load up the
 * initial currency data. Trying to mess with this file could leave the local installation in a bad
 * state, so we will not be testing around this.
 * 		2. There are several nodes that can only be hit by getting IOExceptions and simulating file
 * read issues is difficult, especially for the internal file above.
 *
 * As such, the paths [2,3], [4,5], [6,7], [4,10], [6,10], [8,10], and [13,14] are not feasible 
 * to test.
 * 
 * NOTE: Most of the tests in this class change internal Java properties to create the proper
 * test environment for a given test case, which can have certain effects on other test cases.
 * As such, these tests should be run individually, to allow for proper results. I attempted to
 * get them to be able to run collectively, but my attempts with ClassLoader proved futile.
 * 
 */
public class AccessController {

	private String origPropsVal;
	private Properties props;
	
	@Before
	public void init() {
		props = System.getProperties();
		origPropsVal = props.getProperty("java.util.currency.data");
	}
	
	@After
	public void cleanUp() {
			if (origPropsVal != null) {
				props.setProperty("java.util.currency.data", origPropsVal);
			} else {
				props.remove("java.util.currency.data");
			}
	}
	
	@Test
	public void NoPropsFileSet() {;
		Currency c = Currency.getInstance("GBP");
		assertNotNull(c);
		writeTestCase("AccessController", "1,2,4,6,8,9,11,12,13,15,16,15,16,15,17");
	}
	
	@Test
	public void PropsFileDoesntExist() {
		props.setProperty("java.util.currency.data", "thisfiledoesnotexist.props");
		Currency c = Currency.getInstance("GBP");
		assertNotNull(c);
		writeTestCase("AccessController", "1,2,4,6,8,9,12,17");
	}
	
	@Test
	public void PropsFileExists() {
		try {
			File f = File.createTempFile("custom", "props");
			FileOutputStream fos = new FileOutputStream(f);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write("US=euR,978,2,2001-01-01T00:00:00");
			bw.newLine();
			bw.close();
			
			props.setProperty("java.util.currency.data", f.getAbsolutePath());
			Currency c = Currency.getInstance(Locale.US);
			assertEquals("EUR", c.getCurrencyCode());
			writeTestCase("AccessController", "1,2,4,6,8,9,12,13,15,16,15,17");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void PropsFileExists_MultiLine() {
		try {
			File f = File.createTempFile("custom", "props");
			FileOutputStream fos = new FileOutputStream(f);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write("US=euR,978,2,2001-01-01T00:00:00");
			bw.write("FR=usD,978,2,2001-01-01T00:00:00");
			bw.newLine();
			bw.close();
			
			props.setProperty("java.util.currency.data", f.getAbsolutePath());
			Currency c = Currency.getInstance(Locale.US);
			assertEquals("EUR", c.getCurrencyCode());
			writeTestCase("AccessController", "1,2,4,6,8,9,12,13,15,16,15,16,15,17");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void PropsFileEmpty() {
		try {
			File f = File.createTempFile("custom", "props");			
			props.setProperty("java.util.currency.data", f.getAbsolutePath());
			Currency c = Currency.getInstance(Locale.US);
			assertEquals(c.getCurrencyCode(), "USD");
			writeTestCase("AccessController", "1,2,4,6,8,9,12,13,15,17");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void PropsFileCustomHomePath_EmptyFile() {
		try {
			File f = File.createTempFile("custom", "props");
			File f2 = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "lib" + File.separator + "currency.properties");
			f2.getParentFile().mkdir();
			f2.createNewFile();
			props.setProperty("java.home", f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			Currency c = Currency.getInstance(Locale.US);
			assertEquals(c.getCurrencyCode(), "USD");
			writeTestCase("AccessController", "1,2,4,6,8,9,11,12,13,15,17");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void PropsFileCustomHomePath_NonEmptyFile() {
		try {
			File f = File.createTempFile("custom", "props");
			File f2 = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "lib" + File.separator + "currency.properties");
			f2.getParentFile().mkdir();
			f2.createNewFile();
			FileOutputStream fos = new FileOutputStream(f2);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write("US=euR,978,2,2001-01-01T00:00:00");
			bw.newLine();
			bw.close();
			
			props.setProperty("java.home", f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			Currency c = Currency.getInstance(Locale.US);
			assertEquals(c.getCurrencyCode(), "USD");
			writeTestCase("AccessController", "1,2,4,6,8,9,11,12,13,15,16,15,17");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}

	private void writeTestCase(String functionName, String path) {
		System.out.println(String.format("Function: %s    Path Covered: %s", functionName, path));
	}

}
