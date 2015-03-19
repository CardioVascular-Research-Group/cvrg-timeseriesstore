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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.util.TimeSeriesUtility;
import edu.jhu.icm.ecgFormatConverter.philips.Philips103_wrapper;

public class Phillips103Storer extends PhillipsStorer {

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(InputStream inputStream, String[] channels, int samples, long epochTime) {
		
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		
		Philips103_wrapper phillips103Wrapper = new Philips103_wrapper();

		try {
			phillips103Wrapper.init(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		phillips103Wrapper.parse();
		int[][] leadData = phillips103Wrapper.getData();
		int sampleCount = phillips103Wrapper.getNumberOfPoints();
		int leads = phillips103Wrapper.getChannels();

		for(int i=0; i < leads; i++) {
			long currentTime = epochTime;
			for(int j=0; j < leadData[0].length; j++) {
				
				String channel = getChannelName(i, channels);
			    HashMap<String, String> tags = new HashMap<String, String>();
			    tags.put("format","phillips103");
			    
				dataPoints.add(new IncomingDataPoint("ecg." + channel + ".uv", currentTime, String.valueOf(leadData[i][j]), tags));
				
				currentTime ++;
			}
		}	
		return dataPoints;
	}
}
