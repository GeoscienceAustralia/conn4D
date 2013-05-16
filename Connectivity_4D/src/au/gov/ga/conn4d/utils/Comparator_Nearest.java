package au.gov.ga.conn4d.utils;

import java.util.Comparator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class Comparator_Nearest implements Comparator<Geometry>{

	private Coordinate reference;
	
	@Override
	public int compare(Geometry o1, Geometry o2) {
		Point p1 = o1.getCentroid();
		Point p2 = o2.getCentroid();
		double d1 = p1.getCoordinate().distance(reference);
		double d2 = p2.getCoordinate().distance(reference);
		if(d1<d2){return -1;}
		if(d1>d2){return 1;}
		return 0;
	}
	
	public void setReference(Coordinate reference){
		this.reference = reference;
	}
}
