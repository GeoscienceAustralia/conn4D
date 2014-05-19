/*******************************************************************************
 * Copyright 2014 Geoscience Australia (www.ga.gov.au)
 * @author - Johnathan Kool (Geoscience Australia)
 * 
 * Licensed under the BSD-3 License
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *  
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

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
import au.gov.ga.conn4d.test.impl.movement.Advection_RK4_3DTest;
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
import au.gov.ga.conn4d.test.utils.FilenamePatternFilterTest;
import au.gov.ga.conn4d.test.utils.GeometryUtilsTest;
import au.gov.ga.conn4d.test.utils.IndexLookup_CellTest;
import au.gov.ga.conn4d.test.utils.IndexLookup_NearestTest;
import au.gov.ga.conn4d.test.utils.ReferenceGridTest;
import au.gov.ga.conn4d.test.utils.TimeConvertTest;
import au.gov.ga.conn4d.test.utils.VectorMathTest;
import au.gov.ga.conn4d.test.utils.VectorUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ ReleaseTest.class, ReleaseFactoryTest.class,
		Mortality_ExponentialTest.class, Mortality_NoneTest.class, Mortality_WeibullTest.class,
		CollisionDetection_3D_RasterTest.class, Intersector_3D_PolyTest.class,
		Advection_RK4_3DTest.class, BathymetryReader_GridTest.class,
		Boundary_NetCDF_GridTest.class, Reader_NetCDF_4DTest.class,
		VelocityReader_HYCOMList_4DTest.class,
		VelocityReader_NetCDF_4DTest.class, TrajectoryWriter_TextTest.class,
		ParameterOverrideTest.class, ArraySearchTest.class,
		CoordinateMathTest.class, FileExtensionFilterTest.class,
		FilenamePatternFilterTest.class, GeometryUtilsTest.class, IndexLookup_CellTest.class,
		IndexLookup_NearestTest.class, ReferenceGridTest.class,
		TimeConvertTest.class, VectorMathTest.class, VectorUtilsTest.class })
public class AllTests {
}
