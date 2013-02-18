package lagrange.test.behavior;

import junit.framework.Assert;
import junit.framework.TestCase;
import lagrange.Particle;
import lagrange.impl.behavior.VerticalMigration_Text2;

public class TestVerticalMigration_Text2 extends TestCase {

	VerticalMigration_Text2 am;
	Particle part;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		part = new Particle();
		part.setZ(70);
		am = new VerticalMigration_Text2("F:\\Apollo\\datain\\vert_matrix_damsel.");
	}
	
	public void testApply(){
		
		am.apply(part);
		System.out.println(part.getZ());
		am.apply(part);
		System.out.println(part.getZ());
		am.apply(part);
		System.out.println(part.getZ());
		am.apply(part);
		System.out.println(part.getZ());
		am.apply(part);
		System.out.println(part.getZ());
		am.apply(part);
		System.out.println(part.getZ());
		part.setZ(-52);
		am.apply(part);
		System.out.println(part.getZ());		
		
		Assert.assertTrue(true);
	}

}
