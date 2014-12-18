package au.gov.ga.conn4d.test.impl.readers;

import static org.junit.Assert.*;

import org.geotools.feature.FeatureIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import au.gov.ga.conn4d.impl.readers.Shapefile;

public class ShapefileTest {

	Shapefile sh;
	
	@Before
	public void setUp() throws Exception {
		sh = new Shapefile("./files/test_polys.shp");
	}
	
	@After
	public void tearDown() throws Exception {
		sh.close();
	}

	@Test
	public void testIterator() {
		FeatureIterator<SimpleFeature> it = sh.getIterator();
		int ct = 0;
		while(it.hasNext()){
			SimpleFeature ft = it.next();
			assertTrue(ft.getAttribute("X_INDEX").getClass().equals(Integer.class));
			assertTrue(ft.getAttribute("Y_INDEX").getClass().equals(Integer.class));
			assertTrue((int) ft.getAttribute("X_INDEX")==ct%10);
			assertTrue((int) ft.getAttribute("Y_INDEX")==9-ct/10);
			ct++;
		}
	}
	
	@Test
	public void testHasField(){
		// hasField(String) is case sensitive
		assertTrue(sh.hasField("X_INDEX"));
		assertTrue(!sh.hasField("X_index"));
		assertTrue(sh.hasField("Y_INDEX"));
		assertTrue(!sh.hasField(""));
	}
	
	@Test
	public void testGetSet(){
		assertEquals(sh.getMinx(),-1,1E-6);
		assertEquals(sh.getMiny(),-1,1E-6);
		assertEquals(sh.getMaxx(),1,1E-3);
		assertEquals(sh.getMaxy(),1,1E-3);
		sh.setNegLon(true);
		assertTrue(sh.hasNegLon());
		assertEquals(sh.getNPatches(),100);
		sh.setLookupField("lu_test");
		assertEquals(sh.getLookupField(),"lu_test");
	}
}
