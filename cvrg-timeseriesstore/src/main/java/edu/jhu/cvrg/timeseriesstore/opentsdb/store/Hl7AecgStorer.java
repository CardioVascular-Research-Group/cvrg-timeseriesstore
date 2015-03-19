package edu.jhu.cvrg.timeseriesstore.opentsdb.store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.icm.ecgFormatConverter.hl7.HL7Reader;

public class Hl7AecgStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(InputStream inputStream, String[] channels, int samples, long epochTime) {

		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		
		HL7Reader reader = new HL7Reader(inputStream);

		reader.parse();
		int[][] leadData = reader.getData();
		int sampleCount = reader.getNumberOfPoints();
		int leads = reader.getChannels();

		for(int i=0; i < leads; i++) {
			long currentTime = epochTime;
			for(int j=0; j < leadData[0].length; j++) {
				
				String channel = getChannelName(i, channels);
			    HashMap<String, String> tags = new HashMap<String, String>();
			    tags.put("format","hl7aecg");
			    
				dataPoints.add(new IncomingDataPoint("ecg." + channel + ".uv", currentTime, String.valueOf(leadData[i][j]), tags));
				
				currentTime ++;
			}
		}	
		return dataPoints;
	}
}