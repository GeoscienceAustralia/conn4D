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

package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import au.gov.ga.conn4d.utils.TimeConvert;

public class TimeConvertTest {

	
	@Test
	public void testConvertSecondsFromMillis() {
		assertEquals(TimeConvert.convertFromMillis("seconds", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("second", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("SeCond", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("sec", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("secs", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("seconds", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("S", 1000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("s", 1000),1,1E-9);
	}
	
	@Test
	public void testConvertMinutesFromMillis() {
		assertEquals(TimeConvert.convertFromMillis("minutes", 60000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("MiNUTe", 60000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("Min", 60000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("minS", 60000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("M", 60000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("m", 60000),1,1E-9);
	}
	
	@Test
	public void testConvertHoursFromMillis() {
		assertEquals(TimeConvert.convertFromMillis("hours", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("HoUr", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("Hrs", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("hrs", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("hr", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("H", 3600000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("h", 3600000),1,1E-9);
	}
	
	@Test
	public void testConvertDaysFromMillis() {
		assertEquals(TimeConvert.convertFromMillis("Days", 86400000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("dAy", 86400000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("day", 86400000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("D", 86400000),1,1E-9);
		assertEquals(TimeConvert.convertFromMillis("d", 86400000),1,1E-9);
	}
	
	@Test
	public void testConvertMillisToMillis() {
		assertEquals(TimeConvert.convertToMillis("Milliseconds", 1),1,1E-9);
		assertEquals(TimeConvert.convertToMillis("millisecond", 1),1,1E-9);
		assertEquals(TimeConvert.convertToMillis("ms", 1),1,1E-9);
	}

	@Test
	public void testConvertSecondsToMillis() {
		assertEquals(TimeConvert.convertToMillis("seconds", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("second", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("SeCond", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("secs", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("sec", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("seconds", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("S", 1),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("s", 1),1000,1E-9);
	}
	
	@Test
	public void testConvertMinutesToMillis() {
		assertEquals(TimeConvert.convertToMillis("minutes", 1),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("MiNUTe", 1),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("Min", 1),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("minS", 1),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("M", 1),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("m", 1),60000,1E-9);
	}
	
	@Test
	public void testConvertHoursToMillis() {
		assertEquals(TimeConvert.convertToMillis("hours", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("HoUr", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("Hrs", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("hrs", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("hr", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("H", 1),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("h", 1),3600000,1E-9);
	}
	
	@Test
	public void testConvertDaysToMillis() {
		assertEquals(TimeConvert.convertToMillis("Days", 1),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("dAy", 1),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("day", 1),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("D", 1),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("d", 1),86400000,1E-9);
	}

	
	@Test
	public void testConvertSecondsStringToMillis() {
		assertEquals(TimeConvert.convertToMillis("seconds", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("second", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("SeCond", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("Secs", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("sec", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("seconds", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("S", "1"),1000,1E-9);
		assertEquals(TimeConvert.convertToMillis("s", "1"),1000,1E-9);
	}
	
	@Test
	public void testConvertMinutesStringToMillis() {
		assertEquals(TimeConvert.convertToMillis("minutes", "1"),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("MiNUTe", "1"),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("Min", "1"),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("minS", "1"),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("M", "1"),60000,1E-9);
		assertEquals(TimeConvert.convertToMillis("m", "1"),60000,1E-9);
	}
	
	@Test
	public void testConvertHoursStringToMillis() {
		assertEquals(TimeConvert.convertToMillis("hours", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("HoUr", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("Hrs", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("hrs", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("hr", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("H", "1"),3600000,1E-9);
		assertEquals(TimeConvert.convertToMillis("h", "1"),3600000,1E-9);
	}
	
	@Test
	public void testConvertDaysStringToMillis() {
		assertEquals(TimeConvert.convertToMillis("Days", "1"),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("dAy", "1"),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("day", "1"),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("D", "1"),86400000,1E-9);
		assertEquals(TimeConvert.convertToMillis("d", "1"),86400000,1E-9);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testInvalidConversionFrom(){
		TimeConvert.convertFromMillis("Barbarella", 18);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testInvalidConversionTo(){
		TimeConvert.convertToMillis("BondJamesBond", 007);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testInvalidConversionToString(){
		TimeConvert.convertToMillis("BondJamesBond", "007");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testImproperDate(){
		System.out.println(TimeConvert.convertToMillis("Date","2009-01-01"));
	}
	
}
