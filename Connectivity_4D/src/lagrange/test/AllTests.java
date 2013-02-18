package lagrange.test;

import lagrange.test.collision.TestCollisionDetection_3D_Bathymetry;
import lagrange.test.collision.TestIntersector_3D_Poly;
import lagrange.test.collision.TestIntersector_3D_Triangles;
import lagrange.test.movement.TestMovement_RK4_3D;
import lagrange.test.readers.TestBathymetryReader_Grid;
import lagrange.test.readers.TestVelocityReader_NetCDF_3D;
import lagrange.test.utils.TestArraySearch;
import lagrange.test.utils.TestIndexLookup_Cell;
import lagrange.test.utils.TestIndexLookup_Nearest;
import lagrange.test.utils.TestTriCubicSpline;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestArraySearch.class, TestBathymetryReader_Grid.class,
	    TestCollisionDetection_3D_Bathymetry.class,
		TestIndexLookup_Cell.class, TestIndexLookup_Nearest.class,
		TestIntersector_3D_Triangles.class, TestIntersector_3D_Poly.class,
		TestMovement_RK4_3D.class, TestTriCubicSpline.class, TestRelease.class,
		TestReleaseFactory.class, TestVelocityReader_NetCDF_3D.class })
public class AllTests {
}
