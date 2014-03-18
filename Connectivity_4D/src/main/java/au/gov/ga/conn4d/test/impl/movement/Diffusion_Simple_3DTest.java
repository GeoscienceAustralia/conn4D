package au.gov.ga.conn4d.test.impl.movement;

import junit.framework.Assert;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import au.gov.ga.conn4d.Particle;

import au.gov.ga.conn4d.impl.movement.Diffusion_Simple_3D;
import au.gov.ga.conn4d.utils.CoordinateMath;

public class Diffusion_Simple_3DTest {

	Diffusion_Simple_3D ds3 = new Diffusion_Simple_3D();
	int n = 10000;

	//@Test
	public void test() {

		Coordinate[] ca = new Coordinate[n];

		for (int i = 0; i < n; i++) {
			Particle p = new Particle();

			for (int j = 0; j < 12; j++) {
				ds3.apply(p);
			}
			ca[i] = new Coordinate(p.getX(), p.getY(), p.getZ());
		}
		
		Coordinate avg = CoordinateMath.average(ca);
		double length = CoordinateMath.length3D(avg, new Coordinate());
		//Assert.assertTrue(length<5);
		
	}
}
