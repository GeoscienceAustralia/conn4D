package lagrange.test.utils;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import lagrange.utils.DigitalDifferentialAnalyzer;

public class TestDDA {
	DigitalDifferentialAnalyzer dda = new DigitalDifferentialAnalyzer(91.992620468,-60.0074367,.0099983215);
	//DigitalDifferentialAnalyzer2 dda = new DigitalDifferentialAnalyzer2(0,0,2);
	public static void main(String[] args){
		TestDDA tdda = new TestDDA();
		tdda.go();
	}
	public void go(){
		//Coordinate p0 = new Coordinate(0,0);
		//Coordinate p1 = new Coordinate(-8,-8);
		Coordinate p0 = new Coordinate(113.444241038,-27.66748601);
		Coordinate p1 = new Coordinate(113.426673018,-27.6563639);

		LineSegment ls = new LineSegment(p0,p1);
		dda.setLine(ls);
		
		//System.out.println(Arrays.toString(dda.real2int(new double[]{0,0})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{1,1})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{1.5,1.5})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{2,2})));
		//System.out.println(Arrays.toString(dda.real2int(new double[]{4,8})));
		
		System.out.println(Arrays.toString(dda.nextCell()));
		System.out.println(Arrays.toString(dda.nextCell()));
		System.out.println(Arrays.toString(dda.nextCell()));
		System.out.println(Arrays.toString(dda.nextCell()));
		System.out.println(Arrays.toString(dda.nextCell()));
		System.out.println(Arrays.toString(dda.nextCell()));
	}
}
