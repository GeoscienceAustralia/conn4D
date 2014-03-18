package au.gov.ga.conn4d.test.impl.writers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.writers.TrajectoryWriter_Text;

public class TrajectoryWriter_TextTest {

	TrajectoryWriter_Text tw = new TrajectoryWriter_Text("./files/test.txt");
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadConstructor(){
		@SuppressWarnings("unused")
		TrajectoryWriter_Text tw_bad = new TrajectoryWriter_Text("AA:/BADDIRECTORY/test.txt");
	}

	@Test
	public void testApply() {
		Particle p = new Particle();
		p.setRecording(false);
		tw.apply(p);
		assertTrue(tw.isEmptyFile());
	}

	@Test
	public void testClose() {
		TrajectoryWriter_Text tw = new TrajectoryWriter_Text("./files/test.txt");
		tw.close();	
	}

	@Test
	public void testGetTimeUnits() {
		assertEquals(tw.getTimeUnits(), "Date");
	}

	@Test
	public void testGetDurationUnits() {
		assertEquals(tw.getTimeUnits(), "Date");
	}
	
	@After
	public void tearDown(){
		File f = new File("./files/test.txt");
		f.delete();
	}
}
