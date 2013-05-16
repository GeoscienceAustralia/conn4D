package lagrange.test.behavior;

import static org.junit.Assert.*;
import junit.framework.Assert;
import lagrange.Particle;
import lagrange.impl.behavior.Mortality_None;

import org.junit.Before;
import org.junit.Test;



public class TestMortality_None {

	Mortality_None mn = new Mortality_None();
	Particle p1;
	Particle p2;
	
	@Before
	public void setUp(){
		p1 = new Particle();
		p1.setBirthday(12345);
		p1.setComments("ALL UR BASE R BLONG 2 US");
		p1.setCompetencyStart(54321);
		p1.setDead(false);
		p1.setError(false);
		p1.setDistance(Math.PI);
		p1.setFinished(true);
		p1.setI(0);
		p1.setJ(1);
		p1.setK(2);
		p1.setLost(false);
		p1.setNearNoData(false);
		p1.setNodata(true);
		p1.setT(88);
		p1.setX(12397.123);
		p1.setY(2149234.123);
		p1.setZ(1E-32);
		p1.setPX(2342.23);
		p1.setPY(Math.E);
		p1.setZ(Double.POSITIVE_INFINITY);
		p1.setRecording(true);
		p1.setSettling(true);
		p1.setU(1.0);
		p1.setV(-1.0);
		p1.setW(0);
		p2 = p1.clone();
	}
	
	@Test
	public void test() {
		mn.apply(p2);
		Assert.assertTrue(p1.deepEquals(p2));
		mn.apply(p2,100);
		mn.setTimeInterval(1000);
		Assert.assertTrue(p1.deepEquals(p2));
	}
	
	

}
