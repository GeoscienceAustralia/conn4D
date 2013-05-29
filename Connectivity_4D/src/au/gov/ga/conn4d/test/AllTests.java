package au.gov.ga.conn4d.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import au.gov.ga.conn4d.test.impl.ReleaseTest;
import au.gov.ga.conn4d.test.impl.ReleaseFactoryTest;
import au.gov.ga.conn4d.test.impl.behavior.Mortality_NoneTest;
import au.gov.ga.conn4d.test.impl.collision.CollisionDetection_3D_RasterTest;
import au.gov.ga.conn4d.test.impl.collision.Intersector_3D_PolyTest;
import au.gov.ga.conn4d.test.impl.movement.Movement_RK4_3DTest;
import au.gov.ga.conn4d.test.impl.readers.BathymetryReader_GridTest;
import au.gov.ga.conn4d.test.impl.readers.Boundary_NetCDF_GridTest;
import au.gov.ga.conn4d.test.impl.readers.Reader_NetCDF_4DTest;
import au.gov.ga.conn4d.test.impl.readers.VelocityReader_HYCOMList_4DTest;
import au.gov.ga.conn4d.test.impl.readers.VelocityReader_NetCDF_4DTest;
import au.gov.ga.conn4d.test.impl.writers.TrajectoryWriter_TextTest;
import au.gov.ga.conn4d.test.input.TestParameterOverride;
import au.gov.ga.conn4d.test.utils.TestArraySearch;
import au.gov.ga.conn4d.test.utils.TestCoordinateMath;
import au.gov.ga.conn4d.test.utils.TestFileExtensionFilter;
import au.gov.ga.conn4d.test.utils.TestGeometryUtils;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Cell;
import au.gov.ga.conn4d.test.utils.TestIndexLookup_Nearest;
import au.gov.ga.conn4d.test.utils.TestReferenceGrid;
import au.gov.ga.conn4d.test.utils.TestVectorMath;
import au.gov.ga.conn4d.test.utils.TestVectorUtils;

@RunWith(Suite.class)
@SuiteClasses({ ReleaseTest.class, ReleaseFactoryTest.class,
		Mortality_NoneTest.class, CollisionDetection_3D_RasterTest.class,
		Intersector_3D_PolyTest.class, Movement_RK4_3DTest.class,
		BathymetryReader_GridTest.class, Boundary_NetCDF_GridTest.class,
		Reader_NetCDF_4DTest.class, VelocityReader_HYCOMList_4DTest.class,
		VelocityReader_NetCDF_4DTest.class, TrajectoryWriter_TextTest.class,
		TestParameterOverride.class, TestArraySearch.class,
		TestCoordinateMath.class, TestFileExtensionFilter.class, TestGeometryUtils.class, TestIndexLookup_Cell.class,
		TestIndexLookup_Nearest.class, TestReferenceGrid.class,
		TestVectorMath.class, TestVectorUtils.class })
public class AllTests {
}
