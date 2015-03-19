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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.icm.ecgFormatConverter.muse.MuseXML_wrapper;

public class MuseXMLStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(InputStream inputStream, String[] channels, int samples, long epochTime) {

		System.out.println("Storing MuseXML data");
		
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		MuseXML_wrapper museWrapper = new MuseXML_wrapper();
		museWrapper.parse(inputStream);
		int[][] dataList = museWrapper.getData();
		
		if(dataList == null){
			System.out.println("Returned empty data.");
			return null;
		}
		
		long sampleInterval = (long) (1000/museWrapper.getSamplingRate());
		
		
		for(int i = 0; i < dataList.length; i++){
			
			long dataTime = epochTime;
			for(int j = 0; j < dataList[i].length; j++){
				
				String channel = getChannelName(i, channels);
			    HashMap<String, String> tags = new HashMap<String, String>();
			    tags.put("format","GEMUSEXML");
				dataPoints.add(new IncomingDataPoint("ecg." + channel + ".uv", dataTime, String.valueOf(dataList[i][j]), tags));
				dataTime = dataTime + sampleInterval;
			}
		}

		return dataPoints;
	}

}
