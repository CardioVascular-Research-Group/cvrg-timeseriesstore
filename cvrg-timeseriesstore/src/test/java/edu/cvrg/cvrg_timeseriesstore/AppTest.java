package edu.cvrg.cvrg_timeseriesstore;
/*
Copyright 2015 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
/**
* @author Chris Jurado
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesDatabaseType;
import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesStorerType;
import edu.jhu.cvrg.timeseriesstore.main.TimeSeriesUtilityFactory;
import edu.jhu.cvrg.timeseriesstore.opentsdb.TimeSeriesRetriever;
import edu.jhu.cvrg.timeseriesstore.opentsdb.store.OpenTSDBTimeSeriesStorer;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	final String WFDB_INPUT_DATA_FILE_PATH = "/jhu315.dat";
	final String WFDB_INPUT_HEADER_FILE_PATH = "/jhu315.hea";
	final String EXCEL_INPUT_FILE_PATH = "/testexcel.xlsx";
	final String PHILLIPS103_INPUT_FILE_PATH = "/Philips103Example01.xml";
	final String PHILLIPS104_INPUT_FILE_PATH = "ecg_900657176_1.xml";
	final String MUSE_XML_INPUT_FILE_PATH = "MUSE_20080710_165457_96000.xml";
	final String MUSE_INPUT_FILE_PATH = "CTRIP_PMRV_1.txt";
	final String SCHILLER_INPUT_FILE_PATH = "00EAC8AB-DC76-44E8-AFFB-3F609E6F3E48.xml";
	final String HL7AECG_INPUT_FILE_PATH = "hl7aecg-Example2.xml";
	final String HL7_ROOT_FOLDER_PATH = "/home/WIN/cjurado2/testfiles/a7e81c324c6a72534b0b37cd455bc3f4c310708b9ec19a9371a0564c6df083a6";
	final String OPENTSDB_URL = "http://10.162.38.224:4242";
	
	private Logger log = Logger.getLogger(this.getClass());

    public AppTest( String testName )
    {
        super( testName );
    }
    
//    @Test
//    public void testWFDBUpload(){
//    	boolean result = false;
//    	try{
//    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.WFDB);
//	    	File file = new File(WFDB_INPUT_FILE_PATH);
//	    	InputStream inputStream = new FileInputStream(file);
//	    	String[] channels = {"I", "II", "III"};
//	    	storer.storeTimeSeries(inputStream, channels, 319096, OPENTSDB_URL, "ncc1701");
//	    	inputStream.close();
//	    	result = true;
//    	}
//    	catch(Exception e){
//    		result = false;
//    		log.error(e);
//    		e.printStackTrace();
//    	}
//    	assertTrue(result);
//    }
    
//    @Test
//    public void testPhillips103Upload(){
//    	    	
//    	boolean result = false;
//    	try{
//	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.PHILLIPS103);
//	    	InputStream inputStream = getClass().getResourceAsStream(PHILLIPS103_INPUT_FILE_PATH);
//	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "X", "Y", "Z"};
//
//	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "stringValue");
//    
//	    	inputStream.close();
//	    	result = true;
//    	}
//    	catch(Exception e){
//    		result = false;
//    		e.printStackTrace();
//    	}
//    	assertTrue(result);
//    }
    
//    @Test
//    public void testPhillips104Upload(){
//
//    	
//    	boolean result = false;
//    	try{
//	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.PHILLIPS104);
//	    	InputStream inputStream = getClass().getResourceAsStream(PHILLIPS104_INPUT_FILE_PATH);
//	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "X", "Y", "Z"};
//
//	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "phil104nofile2");
//    
//    inputStream.close();
//	    	result = true;
//    	}
//    	catch(Exception e){
//    		result = false;
//    		e.printStackTrace();
//    	}
//    	assertTrue(result);
//    }
//    
//    @Test
//    public void testMuseXMLUpload(){
//    	boolean result = false;
//    	try{
//	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.MUSEXML);
//	    	InputStream inputStream = getClass().getResourceAsStream(MUSE_XML_INPUT_FILE_PATH);
//	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
//	    		    	
//	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701C");
//	    	result = false;
//    
//    inputStream.close();
//    	}
//    	catch(Exception e){
//    		result = false;
//    		e.printStackTrace();
//    	}
//    	assertTrue(result);
//    }
//    
//    @Test
//    public void testSchillerUpload(){
//    	boolean result = false;
//    	try{
//    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.SCHILLER);
//    		InputStream inputStream = getClass().getResourceAsStream(SCHILLER_INPUT_FILE_PATH);
//	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
//	    	
//	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701D");
//	    	result = true;
//    
//    inputStream.close();
//    	}
//    	catch(Exception e){
//    		result = false;
//    		log.error(e);
//    	}
//    	assertTrue(result);
//    }
//    
//    @Test
//    public void testHl7AecgUpload(){
//
//    	boolean result = false;
//    	try{
//    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.HL7AECG);
//    		InputStream inputStream = getClass().getResourceAsStream(HL7AECG_INPUT_FILE_PATH);
//	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
//
//	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701E");
//	    	
//	    	inputStream.close();
//	    	
//	    	result = true;
//    	}
//    	catch(Exception e){
//    		e.printStackTrace();
//    		result = false;
//    	}
//    	assertTrue(result);
//    }
    
    @Test
    public void testQueryPOST(){
    	
    	boolean result = false;
    	HashMap<String, String> tags = new HashMap<String, String>();
    	tags.put("format", "hl7aecg");
    	JSONObject array = TimeSeriesRetriever.retrieveTimeSeries(OPENTSDB_URL, 1420088400000L, 1425501345000L, "ecg.I.uv", tags);
    	System.out.println(array.toString());
    	assertTrue(array != null);
    }
    
//    @Test
//    public void testQueryWithTsuid(){
//    	
//    	HashMap<String, String> tags = new HashMap<String, String>();
//    	tags.put("format", "hl7aecg");
//    	assertTrue(TimeSeriesRetriever.retrieveTimeSeries(OPENTSDB_URL, 1420088400000L, 1425501345000L, "ecg.I.uv", tags, true) != null);
//    }
}