package edu.jhu.cvrg.timeseriesstore.opentsdb.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;
import ca.uhn.hl7v2.util.Terser;
import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;
import edu.jhu.cvrg.timeseriesstore.util.MetricNameConverter;
import edu.jhu.cvrg.timeseriesstore.util.TimeSeriesUtility;

public class Hl7Storer {
	
	private static HapiContext hapiContext = new DefaultHapiContext();

	public static boolean storeTimeSeries(String openTSDBUrl, String folderPath, String paramPath, String studyId){

		boolean diditwork = false;
		long timeStamp = 1420088400L;
		
		File baseDirectory = new File(folderPath);
		String[] timePointFolders = baseDirectory.list();
		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();

		Arrays.sort(timePointFolders);

		for(String folderName : timePointFolders){

			String fullFolderPath = folderPath + File.separator + folderName;
			
			if(new File(fullFolderPath).isDirectory()){

				try {		
					ArrayList<String> messageFiles = new ArrayList<String>();
					File rootDirContents = new File (folderPath);
					getDirectoryContents(rootDirContents, messageFiles);
					for (String filePath : messageFiles) {
						FileReader reader = new FileReader(filePath);
						Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(reader);

						while (iter.hasNext()) {
							HashMap<String,String> tags = new HashMap<String,String>();
							Message next = iter.next();
							ORU_R01 oru = new ORU_R01();
							oru.parse(next.encode());

							tags.put("subjectId", Terser.get(oru.getRESPONSE().getPATIENT().getPID(), 1, 0, 1, 1));
							tags.put("format", "hl7");
							List<ORU_R01_OBSERVATION> observations = oru.getRESPONSE().getORDER_OBSERVATION().getOBSERVATIONAll();
						
							for (ORU_R01_OBSERVATION observation : observations) {
								String metric = buildMetric(observation);
								String value = Terser.get(observation.getOBX(), 5, 0, 1, 1);
								IncomingDataPoint newDataPoint = new IncomingDataPoint(metric, timeStamp, value, tags);
								System.out.println(newDataPoint.toString());
								dataPoints.add(newDataPoint);
							}
							timeStamp++;
						}
					}
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (HL7Exception e) {
					e.printStackTrace();
				}
			}
			
			TimeSeriesUtility.insertDataPoints(openTSDBUrl, dataPoints);
		}

		return diditwork;
	}

	private static String buildMetric(ORU_R01_OBSERVATION observation){
	
		String measurement = "";
		String unit = "";
		try {
			measurement = Terser.get(observation.getOBX(), 3, 0, 1, 1);
			unit = Terser.get(observation.getOBX(), 6, 0, 1, 1);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
		
//		return measurement + "." + unit;
		return MetricNameConverter.convert(unit);
	}

	private static Hl7InputStreamMessageIterator getTimePointIterator(String folderName){
		
		BufferedReader bufferedReader = null;
		Hl7InputStreamMessageIterator iterator = null;
		try{
			String[] files = new File(folderName).list();
			
			File timePointFile = new File(folderName + "/" + files[0]);

			bufferedReader = new BufferedReader(new FileReader(timePointFile));
			iterator = new Hl7InputStreamMessageIterator(bufferedReader);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return iterator;
	}
	
	private static ArrayList<String> getDirectoryContents(File dir, ArrayList<String> messageFiles) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					messageFiles = getDirectoryContents(file, messageFiles);
				} else {
					if((file.getCanonicalPath().endsWith(".txt")) || (file.getCanonicalPath().endsWith(".msg")))
						messageFiles.add(file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageFiles;
	}
}