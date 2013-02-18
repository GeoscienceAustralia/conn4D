package lagrange.test.movement;

import lagrange.Particle;
import lagrange.impl.movement.Correction_MixedLayer;
import lagrange.utils.TimeConvert;

import org.junit.Before;
import org.junit.Test;

public class TestCorrection_MixedLayer {

	Correction_MixedLayer cmx;
	String fileName = "E:/HPC/Modeling/AUS/Input/NetCDF/AUS_mld_2009.nc";
	
	@Before
	public void setUp(){
		cmx = new Correction_MixedLayer(fileName);
	}
	
	@Test
	public void test() {
		Particle p = new Particle();
		p.setX(100);
		p.setY(-50);
		p.setT(TimeConvert.HYCOMToMillis(39448));
		for(int i = 0; i < 1000; i++){
			p.setZ(0);
			cmx.apply(p);
			System.out.println(p.getZ());
		}
	}
}
