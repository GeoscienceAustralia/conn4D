package au.gov.ga.conn4d.test.output;

import static org.junit.Assert.fail;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import au.gov.ga.conn4d.output.ThreadWriter;

public class ThreadWriterTest {

	private ThreadWriter tw;
	
	@Test
	public void testThreadWriterFile() {
		File file = new File("./files/temp_output.txt");
		try {
			tw = new ThreadWriter(file);
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
		tw.write("This is a test\n");
		tw.close();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String ln = br.readLine();
			assertTrue(ln.equals("This is a test\n"));
		} catch (FileNotFoundException e) {
			fail("Test output file could not be found.");
		} catch (IOException e) {
			fail("Test output file could not be accessed.");
		}
		file.delete();
	}

	@Test
	public void testThreadWriterString() {
		File file = new File("temp_output.txt");
		try {
			tw = new ThreadWriter("temp.txt");
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
		tw.write("This is a test\n");
		tw.close();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String ln = br.readLine();
			assertTrue(ln.equals("This is a test\n"));
		} catch (FileNotFoundException e) {
			fail("Test output file could not be found.");
		} catch (IOException e) {
			fail("Test output file could not be accessed.");
		}
		file.delete();
	}

	@After
	public void testClose() {
		tw.close();
	}
}
