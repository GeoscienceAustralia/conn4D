package au.gov.ga.conn4d.test.input;

import static org.junit.Assert.*;

import org.junit.Test;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.input.ParameterOverride;
import au.gov.ga.conn4d.parameters.Parameters_Test;

public class TestParameterOverride {
	
	ParameterOverride po = new ParameterOverride(); 
	
	@Test
	public void testReadFile() {
		po.readFile("H:/Transfer/programming/workspace/Connectivity_4D/test.prm");
		
		assertFalse(po.centroid);
		assertEquals(po.competencyStart,"31");
		assertEquals(po.competencyStartUnits,"Seconds");
		assertEquals(po.diffusionType,"Test");
		assertFalse(po.effectiveMigration);
		assertEquals(po.h,"1");
		assertEquals(po.hUnits,"Minute");
		assertEquals(po.initialPositionType,"Test");
		assertEquals(po.minTime,"01/01/1999");
		assertEquals(po.minTimeUnits,"Date");
		assertEquals(po.maxTime,"12/31/2002");
		assertEquals(po.minTimeUnits,"Date");
		
		Parameters prm = new Parameters_Test();
		po.setParameters(prm);
		
		
	}

}
