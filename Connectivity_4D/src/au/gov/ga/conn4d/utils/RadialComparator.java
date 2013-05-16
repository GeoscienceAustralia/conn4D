package au.gov.ga.conn4d.utils;

import java.util.Comparator;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Compares coordinates based on their position relative to a given center
 * point. Comparisons are made first on the basis of distance to the center,
 * then based on angle.
 * 
 * @author Johnathan Kool
 * 
 */

public class RadialComparator implements Comparator<Coordinate> {

	Coordinate center = new Coordinate();

	/**
	 * Constructs the comparator using a center coordinate
	 * 
	 * @param center
	 */

	public RadialComparator(Coordinate center) {
		this.center = center;
	}

	/**
	 * Compares two coordinates versus the center coordinate. Comparisons are
	 * made first on the basis of distance to the center, then based on angle.
	 */

	@Override
	public int compare(Coordinate a, Coordinate b) {
		if (a.equals(b)) {
			return 0;
		}
		double d1 = center.distance(a);
		double d2 = center.distance(b);
		if (d1 < d2) {
			return -1;
		}
		if (d1 > d2) {
			return 1;
		}
		double a1 = Angle.normalizePositive(Angle.angle(a));
		double a2 = Angle.normalizePositive(Angle.angle(a));
		if (a1 < a2) {
			return -1;
		}
		if (a1 > a2) {
			return -1;
		}
		return 0;
	}
}
