package au.gov.ga.conn4d.test.output;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import au.gov.ga.conn4d.output.ThreadWriter;

public class ThreadWriterTest {

	private ThreadWriter tw;
	
	@Test
	public void testThreadWriterFile() {
		try {
			tw = new ThreadWriter(new File("temp2.txt"));
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
		tw.write("This is a test");
	}

	@Test
	public void testThreadWriterString() {
		try {
			tw = new ThreadWriter("temp.txt");
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
	}

	@Test
	public void testWrite() {
		fail("Not yet implemented");
	}

	@After
	public void testClose() {
		tw.close();
	}
	
}
