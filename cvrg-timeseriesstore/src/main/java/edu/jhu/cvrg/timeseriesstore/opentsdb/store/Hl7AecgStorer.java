package edu.jhu.cvrg.timeseriesstore.opentsdb.store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.icm.ecgFormatConverter.ECGFileData;
import edu.jhu.icm.ecgFormatConverter.ECGFormatReader;
import edu.jhu.icm.enums.DataFileFormat;

public class Hl7AecgStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(InputStream inputStream, String[] channels, int samples, long epochTime) {

		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		ECGFormatReader reader = new ECGFormatReader();
		ECGFileData data = reader.read(DataFileFormat.PHILIPS103, inputStream);
		
		int[][] leadData = data.data;
		int leads = data.channels;

		for(int i=0; i < leads; i++) {
			long currentTime = epochTime;
			for(int j=0; j < leadData[0].length; j++) {
				
				String channel = getChannelName(i, channels);
			    HashMap<String, String> tags = new HashMap<String, String>();
			    tags.put("format","hl7aecg");
			    
				dataPoints.add(new IncomingDataPoint("ecg.uv." + channel, currentTime, String.valueOf(leadData[i][j]), tags));
				
				currentTime ++;
			}
		}	
		return dataPoints;
	}
}