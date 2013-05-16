package lagrange.test;

import lagrange.test.behavior.TestMortality_None;
import lagrange.test.collision.TestCollisionDetection_3D_Raster;
import lagrange.test.collision.TestIntersector_3D_Poly;
import lagrange.test.movement.TestMovement_RK4_3D;
import lagrange.test.readers.TestBathymetryReader_Grid;
import lagrange.test.readers.TestVelocityReader_HYCOMList_4D;
import lagrange.test.readers.TestVelocityReader_NetCDF_4D;
import lagrange.test.utils.TestArraySearch;
import lagrange.test.utils.TestIndexLookup_Cell;
import lagrange.test.utils.TestIndexLookup_Nearest;
import lagrange.test.utils.TestTriCubicSpline;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestArraySearch.class, TestBathymetryReader_Grid.class,
		TestCollisionDetection_3D_Raster.class, TestIndexLookup_Cell.class,
		TestIndexLookup_Nearest.class, TestIntersector_3D_Poly.class,
		TestMortality_None.class, TestMovement_RK4_3D.class,
		TestTriCubicSpline.class, TestRelease.class, TestReleaseFactory.class,
		TestVelocityReader_NetCDF_4D.class,
		TestVelocityReader_HYCOMList_4D.class })
public class AllTests {
}
