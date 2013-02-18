package lagrange.utils;

import java.util.Comparator;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

public class RadialComparator implements Comparator<Coordinate>{
	
	Coordinate center = new Coordinate();
	
	public RadialComparator(Coordinate center){
		this.center = center;
	}
	
	public int compare(Coordinate a, Coordinate b){
		if(a.equals(b)){return 0;}
		double d1 = center.distance(a);
		double d2 = center.distance(b);
		if (d1<d2){return -1;}
		if (d1>d2){return 1;}
		double a1 = Angle.normalizePositive(Angle.angle(a));
		double a2 = Angle.normalizePositive(Angle.angle(a));
		if(a1 < a2){return -1;}
		if(a1 > a2){return -1;}
		return 0;
	}
}
