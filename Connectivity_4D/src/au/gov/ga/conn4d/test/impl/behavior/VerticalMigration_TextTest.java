package au.gov.ga.conn4d.test.impl.behavior;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.VerticalMigration_Text;

public class VerticalMigration_TextTest extends TestCase {

	VerticalMigration_Text vm;
	Particle p;
	double [][] m1 = {	{ 0, .05}, 
						{.2, .1}, 
						{.2, .2}, 
						{.2, .3}, 
						{.2, .2}, 
						{.2, .1}, 
						{.0, .05}};
	double[] bins = {5,15,25,40,62.5,87.5,112.5,137.5};
	
	/**
	 * Sets up the test class
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		p = new Particle();
		p.setZ(5);
		vm = new VerticalMigration_Text();
		vm.setTimeInterval(Long.MAX_VALUE);
		vm.setTimeIntervalUnits("Days");
		vm.setVmtx(m1);
	}
	
	/**
	 * Incomplete
	 */
	
	@Test
	public void testMovement() {
		
		for(int i = 1; i < 31; i++){
			vm.apply(p);
			//System.out.print(p.getZ() + " ");
			if(i%15 == 0){System.out.print("\n");}
		
		}
	}
}
