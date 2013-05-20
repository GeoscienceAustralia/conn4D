package au.gov.ga.conn4d.test.utils;

import java.util.Arrays;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import au.gov.ga.conn4d.utils.ReferenceGrid;

public class TestReferenceGrid {
	//ReferenceGrid rg = new ReferenceGrid(91.992620468,-60.0074367,.0099983215);
	ReferenceGrid rg = new ReferenceGrid(0,0,1);
	public static void main(String[] args){
		TestReferenceGrid tdda = new TestReferenceGrid();
		tdda.go();
	}
	
	//@Test
	public void go(){
		//Coordinate p0 = new Coordinate(0,0);
		//Coordinate p1 = new Coordinate(-8,-8);
		Coordinate p0 = new Coordinate(113.444241038,-27.66748601);
		Coordinate p1 = new Coordinate(113.426673018,-27.6563639);

		LineSegment ls = new LineSegment(p0,p1);
		rg.setLine(ls);
		
		//System.out.println(Arrays.toString(dda.real2int(new double[]{0,0})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{1,1})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{1.5,1.5})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{2,2})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{4,8})));
		
		System.out.println(Arrays.toString(rg.nextCell()));
		System.out.println(Arrays.toString(rg.nextCell()));
		System.out.println(Arrays.toString(rg.nextCell()));
		System.out.println(Arrays.toString(rg.nextCell()));
		System.out.println(Arrays.toString(rg.nextCell()));
		System.out.println(Arrays.toString(rg.nextCell()));
	}
	
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
