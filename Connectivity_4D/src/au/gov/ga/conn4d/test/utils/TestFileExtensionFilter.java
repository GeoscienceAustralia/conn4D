package au.gov.ga.conn4d.test.utils;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.FileExtensionFilter;

public class TestFileExtensionFilter {

	FileExtensionFilter fef = new FileExtensionFilter(".txt");
	
	@Test
	public void test() {
		Assert.assertFalse(fef.accept(null, "txt"));
		Assert.assertTrue(fef.accept(null, ".txt"));
		Assert.assertFalse(fef.accept(null, "nothing.here"));
		Assert.assertFalse(fef.accept(null, "nothing.txt.here"));
		Assert.assertTrue(fef.accept(null, "testme.txt"));
		Assert.assertFalse(fef.accept(null, "thistxt.ack"));
		Assert.assertFalse(fef.accept(null, "thistxt."));
	}
}
