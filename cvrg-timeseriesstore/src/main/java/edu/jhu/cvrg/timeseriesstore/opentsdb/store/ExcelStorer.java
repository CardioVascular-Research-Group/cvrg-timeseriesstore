package edu.jhu.cvrg.timeseriesstore.opentsdb.store;
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
* @author Stephen Granite, Chris Jurado
* 
*/
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.model.TimeSeriesData;
import edu.jhu.cvrg.timeseriesstore.util.TimeSeriesUtility;

public class ExcelStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	public boolean storeTimeSeries(InputStream inputStream, String[] channels, int samples, String urlString, String subjectId){

		Gson gson = new Gson();
		int subjectCount = 0;

		XSSFWorkbook subjectWorkbook = getWorkbook(inputStream);
		HashMap<String,Hashtable<Date, Double>> subjectData = new HashMap<String,Hashtable<Date, Double>>();
		Hashtable<Date, Double> timeSeries = new Hashtable<Date, Double>();
		HashMap<String,String> tags = new HashMap<String,String>();
		SimpleDateFormat fromExcel = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		TreeSet<String> sortedKeys = null;
		try {
			for (int i = 0; i < subjectWorkbook.getNumberOfSheets(); i++) {
				XSSFSheet sheetIn = subjectWorkbook.getSheetAt(i);
				for (int r = 1; r <= sheetIn.getLastRowNum(); r++) {
					XSSFRow row = sheetIn.getRow(r);	
					Date reformattedTime = fromExcel.parse(row.getCell(0).getStringCellValue());		
					timeSeries.put(reformattedTime, row.getCell(1).getNumericCellValue());
				}
				subjectData.put(sheetIn.getSheetName(), timeSeries);
				timeSeries = new Hashtable<Date, Double>();
			}
			Set<String> keys = subjectData.keySet();
			sortedKeys = new TreeSet<String>(keys);
			int number = ++subjectCount;
			if (number < 10) {
				subjectId += "0000" + new Integer(number).toString();
			} else if (number < 100) {
				subjectId += "000" + new Integer(number).toString();
			} else if (number < 1000) {
				subjectId += "00" + new Integer(number).toString();
			} else if (number < 10000) {
				subjectId += "0" + new Integer(number).toString();
			} else {
				subjectId += new Integer(number).toString();						
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
			
		tags.put("subjectId", subjectId);
			
		try{
			if(sortedKeys == null){
				return false;
			}
			for (String key : sortedKeys) {		
				HttpURLConnection conn = TimeSeriesUtility.openHTTPConnectionPOST(urlString);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				String[] variableAndUnits = key.split("\\(");
				String keyReplace = variableAndUnits[0];
				keyReplace = keyReplace.replaceAll(" ", "");
				if (keyReplace.equalsIgnoreCase("PulseOximetryPeripheralHeart"))
					keyReplace += "Rate";			
					formatArray(variableAndUnits, tags);
					Set<Date> timePoints = subjectData.get(key).keySet();
					TreeSet<Date> sortedTimePoints = new TreeSet<Date>(timePoints);
					ArrayList<IncomingDataPoint> points = new ArrayList<IncomingDataPoint>();
					for (Date time : sortedTimePoints) {
//						IncomingDataPoint datapoint = new IncomingDataPoint(keyReplace, time.getTime(), new Double(subjectData.get(key).get(time)).toString(), tags);
//						points.add(datapoint);
					}
					String json = gson.toJson(points);
					wr.write(json);
					wr.flush();
					wr.close();
	
					int HttpResult = conn.getResponseCode(); 
	
					if(HttpResult == HttpURLConnection.HTTP_OK){
						TimeSeriesUtility.readHTTPConnection(conn);
					}else{
						System.out.println(conn.getResponseMessage());  
					}  
					conn.disconnect();
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		return true;
	}

	private XSSFWorkbook getWorkbook(InputStream inputStream){
		XSSFWorkbook matchWorkbook = null;
		try {
			matchWorkbook = new XSSFWorkbook(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return matchWorkbook;
	}
	
	private void formatArray(String[] variableAndUnits, HashMap<String,String> tags){
		
		if (variableAndUnits.length > 1) {
			if (variableAndUnits.length > 2)
				variableAndUnits[1] += variableAndUnits[2];
			variableAndUnits[1] = variableAndUnits[1].replaceAll("\\)", "");
			variableAndUnits[1] = variableAndUnits[1].replaceAll(" ", "");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("%", "percent");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("#", "count");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("mmhg", "mmHg");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("breathsperm", "breaths");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("breaths", "breathspermin");
			variableAndUnits[1] = variableAndUnits[1].replaceAll("cel", "Celsius");					
			tags.put("units", variableAndUnits[1]);
			if (variableAndUnits[0].equalsIgnoreCase("Pulse Oximetry Peripheral Heart"))
				tags.put("units", "per min");
		}
	}


	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(
			InputStream inputStream, String[] channels, int samples, long epochTime) {
		// TODO Auto-generated method stub
		return null;
	}
}
