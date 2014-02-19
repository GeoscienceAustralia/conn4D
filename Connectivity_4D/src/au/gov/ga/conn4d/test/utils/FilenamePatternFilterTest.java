package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import au.gov.ga.conn4d.utils.FilenamePatternFilter;

public class FilenamePatternFilterTest {
	
	@Test
	public void test() {
		FilenamePatternFilter fpf1 = new FilenamePatternFilter(".*abcde.*");
		assertTrue(fpf1.accept(null, "abcde"));
		assertTrue(fpf1.accept(null, "zzabcdezz"));
		assertTrue(fpf1.accept(null, "zzabcde"));
		assertTrue(fpf1.accept(null, "abcdezz"));
		assertFalse(fpf1.accept(null, "nothing here"));
		assertFalse(fpf1.accept(null, ""));
	}
}
