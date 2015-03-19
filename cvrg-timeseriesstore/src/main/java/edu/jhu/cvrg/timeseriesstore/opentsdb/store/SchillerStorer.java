package edu.jhu.cvrg.timeseriesstore.opentsdb.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.cvrgrid.schiller.DecodedLead;
import org.cvrgrid.schiller.PreprocessReturn;
import org.cvrgrid.schiller.SchillerEcgFiles;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.model.TimeSeriesData;
import edu.jhu.cvrg.timeseriesstore.util.TimeSeriesUtility;

public class SchillerStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	protected ArrayList<IncomingDataPoint> extractTimePoints(
			InputStream inputStream, String[] channels, int samples, long epochTime) {
		
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		DecodedLead[] leadData = null;
		int allocatedChannels = 0;
		int time = 0;
		
		File inputFile = TimeSeriesUtility.createTempFile(inputStream, "schiller");
		PreprocessReturn ret;
		try {
			ret = SchillerEcgFiles.preprocess(inputFile);
			leadData = ret.getDecodedLeads();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		allocatedChannels = leadData.length;

		for(int i=0; i < allocatedChannels; i++) {
			long currentTime = epochTime;
			for(int j=0; j < leadData[i].size(); j++) {
				String channel = getChannelName(i, channels);
			    HashMap<String, String> tags = new HashMap<String, String>();
			    tags.put("format", "schiller");
				dataPoints.add(new IncomingDataPoint("ecg." + channel + ".uv", currentTime, String.valueOf(leadData[i].get(j)), tags));
				currentTime ++;
			}
		}
		return dataPoints;
	}
}
