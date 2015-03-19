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
* 
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesDatabaseType;
import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesStorerType;
import edu.jhu.cvrg.timeseriesstore.main.TimeSeriesUtilityFactory;
import edu.jhu.cvrg.timeseriesstore.opentsdb.TimeSeriesRetriever;
import edu.jhu.cvrg.timeseriesstore.opentsdb.store.Hl7Storer;
import edu.jhu.cvrg.timeseriesstore.opentsdb.store.OpenTSDBTimeSeriesStorer;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	final String WFDB_INPUT_FILE_PATH = "/opt/liferay/testdata/jhu315.dat";
	final String PHILLIPS103_INPUT_FILE_PATH = "/opt/liferay/testdata/Philips103Example01.xml";
	final String PHILLIPS104_INPUT_FILE_PATH = "/opt/liferay/testdata/ecg_900657176_1.xml";
	final String MUSE_XML_INPUT_FILE_PATH = "/opt/liferay/testdata/MUSE_20080710_165457_96000.xml";
	final String MUSE_INPUT_FILE_PATH = "/opt/liferay/testdata/CTRIP_PMRV_1.txt";
	final String SCHILLER_INPUT_FILE_PATH = "/opt/liferay/testdata/00EAC8AB-DC76-44E8-AFFB-3F609E6F3E48.xml";
	final String HL7AECG_INPUT_FILE_PATH = "/opt/liferay/testdata/hl7aecg-Example2.xml";
	final String HL7_ROOT_FOLDER_PATH = "/home/WIN/cjurado2/testfiles/a7e81c324c6a72534b0b37cd455bc3f4c310708b9ec19a9371a0564c6df083a6";
	final String OPENTSDB_URL = "http://10.162.38.224:4242";
	
	private Logger log = Logger.getLogger(this.getClass());

    public AppTest( String testName )
    {
        super( testName );
    }
    
    @Test
    public void testWFDBUpload(){
    	boolean result = false;
    	try{
    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.WFDB);
	    	File file = new File(WFDB_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III"};
	    	storer.storeTimeSeries(inputStream, channels, 319096, OPENTSDB_URL, "ncc1701");
    inputStream.close();
	    	result = true;
    	}
    	catch(Exception e){
    		result = false;
    		log.error(e);
    		e.printStackTrace();
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testPhillips103Upload(){
    	
    	System.out.println("Starting Phillips103 test");
    	
    	boolean result = false;
    	try{
	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.PHILLIPS103);
	    	
	    	System.out.println("Got the storer.");
	    	
	    	File file = new File(PHILLIPS103_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "X", "Y", "Z"};
	    	
	    	System.out.println("Let's store a Phillips103 time series.");
	    	
	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "stringValue");
    
	    	inputStream.close();
	    	result = true;
    	}
    	catch(Exception e){
    		result = false;
    		e.printStackTrace();
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testPhillips104Upload(){
    	
    	System.out.println("Starting Phillips104 test");
    	
    	boolean result = false;
    	try{
	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.PHILLIPS104);
	    	File file = new File(PHILLIPS104_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "X", "Y", "Z"};
	    	
	    	System.out.println("Let's store a Phillips104 time series.");
	    	
	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "phil104nofile2");
    
    inputStream.close();
	    	result = true;
    	}
    	catch(Exception e){
    		result = false;
    		e.printStackTrace();
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testMuseXMLUpload(){
    	boolean result = false;
    	try{
	    	OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.MUSEXML);
	    	File file = new File(MUSE_XML_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
	    	
	    	System.out.println("Let's store a Muse time series.");
	    		    	
	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701C");
	    	result = false;
    
    inputStream.close();
    	}
    	catch(Exception e){
    		result = false;
    		e.printStackTrace();
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testSchillerUpload(){
    	boolean result = false;
    	try{
    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.SCHILLER);
	    	File file = new File(SCHILLER_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
	    	
	    	System.out.println("Let's store a Schiller time series.");
	    	
	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701D");
	    	result = true;
    
    inputStream.close();
    	}
    	catch(Exception e){
    		result = false;
    		log.error(e);
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testHl7AecgUpload(){

    	boolean result = false;
    	try{
    		OpenTSDBTimeSeriesStorer storer = TimeSeriesUtilityFactory.returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType.OPENTSDB, EnumTimeSeriesStorerType.HL7AECG);
	    	File file = new File(HL7AECG_INPUT_FILE_PATH);
	    	InputStream inputStream = new FileInputStream(file);
	    	String[] channels = {"I", "II", "III", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
	    	
	    	System.out.println("Let's store an HL7aECG time series.");
	    	
	    	storer.storeTimeSeries(inputStream, channels, 0, OPENTSDB_URL, "ncc1701E");
	    	
	    	inputStream.close();
	    	
	    	result = true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		result = false;
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testHl7Upload(){
    	boolean result = false;
    	try{
    		Hl7Storer.storeTimeSeries(OPENTSDB_URL, HL7_ROOT_FOLDER_PATH, "", "ncc1701F");
    	}
    	catch(Exception e){
    		result = false;
    		e.printStackTrace();
    	}
    	assertTrue(result);
    }
    
    @Test
    public void testQueryPOST(){
    	
    	HashMap<String, String> tags = new HashMap<String, String>();
    	
    	tags.put("format", "hl7aecg");

    	assertTrue(TimeSeriesRetriever.retrieveTimeSeriesPOST(OPENTSDB_URL, 1420088400000L, 1425501345000L, "ecg.I.uv", tags, true) != null);

    }
    
    @Test
    public void testQueryGET(){
    	
    	HashMap<String, String> tags = new HashMap<String, String>();
    	
    	tags.put("format", "hl7aecg");

    	assertTrue(TimeSeriesRetriever.retrieveTimeSeriesGET(OPENTSDB_URL, 1420088400000L, 1425501345000L, "ecg.I.uv", tags, false) != null);

    }

}