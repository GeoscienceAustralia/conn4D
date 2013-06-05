package au.gov.ga.conn4d.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import au.gov.ga.conn4d.test.impl.ReleaseTest;
import au.gov.ga.conn4d.test.impl.ReleaseFactoryTest;
import au.gov.ga.conn4d.test.impl.behavior.Mortality_ExponentialTest;
import au.gov.ga.conn4d.test.impl.behavior.Mortality_NoneTest;
import au.gov.ga.conn4d.test.impl.behavior.Mortality_WeibullTest;
import au.gov.ga.conn4d.test.impl.collision.CollisionDetection_3D_RasterTest;
import au.gov.ga.conn4d.test.impl.collision.Intersector_3D_PolyTest;
import au.gov.ga.conn4d.test.impl.movement.Movement_RK4_3DTest;
import au.gov.ga.conn4d.test.impl.readers.BathymetryReader_GridTest;
import au.gov.ga.conn4d.test.impl.readers.Boundary_NetCDF_GridTest;
import au.gov.ga.conn4d.test.impl.readers.Reader_NetCDF_4DTest;
import au.gov.ga.conn4d.test.impl.readers.VelocityReader_HYCOMList_4DTest;
import au.gov.ga.conn4d.test.impl.readers.VelocityReader_NetCDF_4DTest;
import au.gov.ga.conn4d.test.impl.writers.TrajectoryWriter_TextTest;
import au.gov.ga.conn4d.test.input.ParameterOverrideTest;
import au.gov.ga.conn4d.test.utils.ArraySearchTest;
import au.gov.ga.conn4d.test.utils.CoordinateMathTest;
import au.gov.ga.conn4d.test.utils.FileExtensionFilterTest;
import au.gov.ga.conn4d.test.utils.GeometryUtilsTest;
import au.gov.ga.conn4d.test.utils.IndexLookup_CellTest;
import au.gov.ga.conn4d.test.utils.IndexLookup_NearestTest;
import au.gov.ga.conn4d.test.utils.ReferenceGridTest;
import au.gov.ga.conn4d.test.utils.VectorMathTest;
import au.gov.ga.conn4d.test.utils.VectorUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ ReleaseTest.class, ReleaseFactoryTest.class,
		Mortality_ExponentialTest.class, Mortality_NoneTest.class, Mortality_WeibullTest.class,
		CollisionDetection_3D_RasterTest.class, Intersector_3D_PolyTest.class,
		Movement_RK4_3DTest.class, BathymetryReader_GridTest.class,
		Boundary_NetCDF_GridTest.class, Reader_NetCDF_4DTest.class,
		VelocityReader_HYCOMList_4DTest.class,
		VelocityReader_NetCDF_4DTest.class, TrajectoryWriter_TextTest.class,
		ParameterOverrideTest.class, ArraySearchTest.class,
		CoordinateMathTest.class, FileExtensionFilterTest.class,
		GeometryUtilsTest.class, IndexLookup_CellTest.class,
		IndexLookup_NearestTest.class, ReferenceGridTest.class,
		VectorMathTest.class, VectorUtilsTest.class })
public class AllTests {
}
