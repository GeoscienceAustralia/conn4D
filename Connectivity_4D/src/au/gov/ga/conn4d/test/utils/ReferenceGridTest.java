package au.gov.ga.conn4d.test.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ReferenceGrid;

import com.vividsolutions.jts.geom.Coordinate;

public class ReferenceGridTest {
	ReferenceGrid rg = new ReferenceGrid(0,0,1);
	
	@Test
	public void testIsOnVerticalEdge(){
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(0,0)));
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(0,0.5)));
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(1,0.5)));
		Assert.assertFalse(rg.isOnVerticalEdge(new Coordinate(0.5,0)));
		Assert.assertFalse(rg.isOnVerticalEdge(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testIsOnHorizontalEdge(){
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0,0)));
		Assert.assertFalse(rg.isOnHorizontalEdge(new Coordinate(0,0.5)));
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0.5,1)));
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0.5,0)));
		Assert.assertFalse(rg.isOnHorizontalEdge(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testIsOnCorner(){
		Assert.assertTrue(rg.isOnCorner(new Coordinate(0,0)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0,0.5)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0.5,1)));
		Assert.assertTrue(rg.isOnCorner(new Coordinate(5,7)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testGetCellList(){
		Coordinate c = new Coordinate(1.5,0);
		List<int[]> l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 2);
		Assert.assertArrayEquals(l.get(0),new int[]{1,0});
		Assert.assertArrayEquals(l.get(1),new int[]{1,-1});
		
		c = new Coordinate(0,1.5);
		l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 2);
		Assert.assertArrayEquals(l.get(0),new int[]{0,1});
		Assert.assertArrayEquals(l.get(1),new int[]{-1,1});
		
		c = new Coordinate(1,1);
		l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 4);
		Assert.assertArrayEquals(l.get(0),new int[]{1,1});
		Assert.assertArrayEquals(l.get(1),new int[]{1,0});
		Assert.assertArrayEquals(l.get(2),new int[]{0,1});
		Assert.assertArrayEquals(l.get(3),new int[]{0,0});
	}
}
