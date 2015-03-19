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
* @author Chris Jurado
* 
*/
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.model.TimeSeriesData;

public class MuseStorer extends OpenTSDBTimeSeriesStorer {



	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(
			InputStream inputStream, String[] channels, int samples, long epochTime) {
		
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		
		DataInputStream geMuseDis = null;
		BufferedReader br = null;
		int channelCount = 0;
		
		try{
		    geMuseDis = new DataInputStream(inputStream);
		    br = new BufferedReader(new InputStreamReader(geMuseDis));
		    String strLine;
		    String[] words;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		      // Print the content on the console
		    	if(strLine.length()>0) {
		    		System.out.println (strLine);
		    		words = strLine.split("\\s");
		    		System.out.println("Words is " + words);
	    			channelCount = Integer.parseInt(words[4]);
	    			if(channelCount != channels.length){
	    				channelCount = channels.length;
	    			}
	    			if(channelCount > 12){
	    				channelCount = 12;
	    			}
		    	}
		    }

		    geMuseDis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	    String strLine;
	    String[] numbers;
	    
		try {

			while ((strLine = br.readLine()) != null)   {

	    	strLine = strLine.trim();
	    	if(strLine.length()>0) {
	    		long time = 1420088400L;
	    		numbers = strLine.split("\\s");
				for (int c = 0; c < channelCount; c++) {
					short value = Short.parseShort(numbers[c]);
					String channel = getChannelName(c, channels);
				    HashMap<String, String> tags = new HashMap<String, String>();
				    tags.put("format", "gemuse");
					dataPoints.add(new IncomingDataPoint("ecg." + channel + ".uv", time, String.valueOf(value), tags));
					time ++;
					}
		    	}
		    }


			geMuseDis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return dataPoints;
	}
}
