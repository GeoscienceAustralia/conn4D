package lagrange.test.behavior;

import junit.framework.TestCase;
import lagrange.Particle;
import lagrange.impl.behavior.VerticalMigration_Text;

public class TestVerticalMigration_Text extends TestCase {

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
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		p = new Particle();
		p.setZ(5);
		vm = new VerticalMigration_Text();
		vm.setTimeInterval(Long.MAX_VALUE);
		vm.setTimeIntervalUnits("Days");
		vm.setVmtx(m1);
	}

	public void testApply() {
		
		for(int i = 1; i < 31; i++){
		
			vm.apply(p);
			System.out.print(p.getZ() + " ");
			if(i%15 == 0){System.out.print("\n");}
		
		}
	}
}
