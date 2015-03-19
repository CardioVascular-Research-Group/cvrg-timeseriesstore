package edu.jhu.cvrg.timeseriesstore.opentsdb.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.model.TimeSeriesData;

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
public class WfdbStorer extends OpenTSDBTimeSeriesStorer {
	
	String[] signalName = null;

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(
			InputStream inputStream, String[] channels, int samples, long epochTime) {
		
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();

	    InputStreamReader isr = new InputStreamReader(inputStream);
	    BufferedReader stdInputBuffer = new BufferedReader(isr);
	    
	    String line;
	    String[] aSigNames, aSample;
	    int lineNum = 0;
	    int signalCount = channels.length;
	    int[][] data = new int[signalCount][samples];

	    try {
			while ((line = stdInputBuffer.readLine()) != null) {
				System.out.println("WfdbStorer52 line: " + line);
				if(lineNum==0){
					aSigNames = line.split(",");
					signalName = new String[signalCount];
					for(int sig=1;sig<=signalCount;sig++){ // zeroth column is time, not a signal
						signalName[sig-1] = aSigNames[sig];// column names to be used later to verify the order.
					}			    	  
				}else{
					if (lineNum > 1){
						aSample = line.split(",");
						float fSamp;
						for(int sig=1;sig<=signalCount;sig++){ // zeroth column is time, not a signal
							try{ // Check if value is a not a number, e.g. "-" or "na", substitute zero so rdsamp won't break; Mike Shipway (7/21/2014)
								fSamp  = Float.parseFloat(aSample[sig]); // assumes unit is float millivolts.
							}catch(NumberFormatException nfe){
								fSamp = 0;
							}
							data[sig-1][lineNum-2] = (int)(fSamp*1000);// convert float millivolts to integer microvolts.
							for(int i = 1; i < aSample.length - 1; i++){
							    HashMap<String, String> tags = new HashMap<String, String>();
							    tags.put("channel", getSigName(i));
//								dataPoints.add(new IncomingDataPoint("millivolts", Long.valueOf(aSample[0]), aSample[i], tags));
							}
						}			    	  
					}		    	  
				}
				lineNum++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		   
		return dataPoints;
	}

	private String getSigName(int index){
		return signalName[index];
	}
}
