package au.gov.ga.conn4d.test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import au.gov.ga.conn4d.test.impl.TestRelease;
import au.gov.ga.conn4d.test.impl.TestReleaseFactory;
import au.gov.ga.conn4d.test.impl.behavior.TestMortality_None;
import au.gov.ga.conn4d.test.impl.collision.TestCollisionDetection_3D_Raster;
import au.gov.ga.conn4d.test.impl.collision.TestIntersector_3D_Poly;
import au.gov.ga.conn4d.test.impl.movement.TestMovement_RK4_3D;
import au.gov.ga.conn4d.test.impl.readers.TestBathymetryReader_Grid;
import au.gov.ga.conn4d.test.impl.readers.TestBoundary_NetCDF_Grid;
import au.gov.ga.conn4d.test.impl.readers.TestVelocityReader_HYCOMList_4D;
import au.gov.ga.conn4d.test.impl.readers.TestVelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.test.input.TestParameterOverride;
import au.gov.ga.conn4d.test.utils.TestArraySearch;
import au.gov.ga.conn4d.test.utils.TestCoordinateMath;
import au.gov.ga.conn4d.test.utils.TestFMath;
import au.gov.ga.conn4d.test.utils.TestFileExtensionFilter;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Cell;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Nearest;
import au.gov.ga.conn4d.test.utils.TestTriCubicSpline;
import au.gov.ga.conn4d.test.utils.TestVectorMath;


@RunWith(Suite.class)
@SuiteClasses({ TestArraySearch.class, TestBathymetryReader_Grid.class,
		TestBoundary_NetCDF_Grid.class,  
		TestCollisionDetection_3D_Raster.class, TestCoordinateMath.class, TestFileExtensionFilter.class,
		TestFMath.class, TestIndexLookup_Cell.class, TestIndexLookup_Nearest.class, 
		TestIntersector_3D_Poly.class, TestMortality_None.class, 
		TestMovement_RK4_3D.class, TestParameterOverride.class, TestTriCubicSpline.class, 
		TestRelease.class, TestReleaseFactory.class, TestVectorMath.class,
		TestVelocityReader_NetCDF_4D.class,	TestVelocityReader_HYCOMList_4D.class })
public class AllTests {
}
