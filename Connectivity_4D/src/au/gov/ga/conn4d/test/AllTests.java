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
import au.gov.ga.conn4d.test.impl.readers.TestReader_NetCDF_4D;
import au.gov.ga.conn4d.test.impl.readers.TestVelocityReader_HYCOMList_4D;
import au.gov.ga.conn4d.test.impl.readers.TestVelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.test.impl.writers.TestTrajectoryWriter_Text;
import au.gov.ga.conn4d.test.input.TestParameterOverride;
import au.gov.ga.conn4d.test.utils.TestArraySearch;
import au.gov.ga.conn4d.test.utils.TestCoordinateMath;
import au.gov.ga.conn4d.test.utils.TestFMath;
import au.gov.ga.conn4d.test.utils.TestFileExtensionFilter;
import au.gov.ga.conn4d.test.utils.TestGeometryUtils;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Cell;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Nearest;
import au.gov.ga.conn4d.test.utils.TestReferenceGrid;
import au.gov.ga.conn4d.test.utils.TestTriCubicSpline;
import au.gov.ga.conn4d.test.utils.TestVectorMath;
import au.gov.ga.conn4d.test.utils.TestVectorUtils;

@RunWith(Suite.class)
@SuiteClasses({ TestRelease.class, TestReleaseFactory.class,
		TestMortality_None.class, TestCollisionDetection_3D_Raster.class,
		TestIntersector_3D_Poly.class, TestMovement_RK4_3D.class,
		TestBathymetryReader_Grid.class, TestBoundary_NetCDF_Grid.class,
		TestReader_NetCDF_4D.class, TestVelocityReader_HYCOMList_4D.class,
		TestVelocityReader_NetCDF_4D.class, TestTrajectoryWriter_Text.class,
		TestParameterOverride.class, TestArraySearch.class,
		TestCoordinateMath.class, TestFMath.class,
		TestFileExtensionFilter.class, TestGeometryUtils.class, TestIndexLookup_Cell.class,
		TestIndexLookup_Nearest.class, TestReferenceGrid.class,
		TestTriCubicSpline.class, TestVectorMath.class, TestVectorUtils.class })
public class AllTests {
}
