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

package au.gov.ga.conn4d.test.output;

import static org.junit.Assert.fail;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import au.gov.ga.conn4d.output.ThreadWriter;

public class ThreadWriterTest {

	private ThreadWriter tw;
	
	@Test
	public void testThreadWriterFile() {
		File file = new File("./files/temp_output.txt");
		try {
			tw = new ThreadWriter(file);
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
		tw.write("This is a test\n");
		tw.close();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String ln = br.readLine();
			assertTrue(ln.equals("This is a test\n"));
		} catch (FileNotFoundException e) {
			fail("Test output file could not be found.");
		} catch (IOException e) {
			fail("Test output file could not be accessed.");
		}
		file.delete();
	}

	@Test
	public void testThreadWriterString() {
		File file = new File("temp_output.txt");
		try {
			tw = new ThreadWriter("temp.txt");
		} catch (IOException e) {
			fail();
		}
		tw.open();
		tw.run();
		tw.write("This is a test\n");
		tw.close();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String ln = br.readLine();
			assertTrue(ln.equals("This is a test\n"));
		} catch (FileNotFoundException e) {
			fail("Test output file could not be found.");
		} catch (IOException e) {
			fail("Test output file could not be accessed.");
		}
		file.delete();
	}

	@After
	public void testClose() {
		tw.close();
	}
}
