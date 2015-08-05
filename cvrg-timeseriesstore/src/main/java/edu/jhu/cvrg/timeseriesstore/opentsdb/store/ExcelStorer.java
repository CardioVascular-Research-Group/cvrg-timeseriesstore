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
*/
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.jhu.cvrg.timeseriesstore.model.IncomingDataPoint;

public class ExcelStorer extends OpenTSDBTimeSeriesStorer {

	@Override
	public ArrayList<IncomingDataPoint> extractTimePoints(InputStream inputStream, String[] channels, int samples, long epochTime){

		ArrayList<IncomingDataPoint> dataPoints = new ArrayList<IncomingDataPoint>();
		XSSFWorkbook subjectWorkbook = getWorkbook(inputStream);
		HashMap<String,String> tags = new HashMap<String,String>();
		
		for (int i = 0; i < subjectWorkbook.getNumberOfSheets(); i++) {
			XSSFSheet sheetIn = subjectWorkbook.getSheetAt(i);
			for (int r = 1; r <= sheetIn.getLastRowNum(); r++) {
				long currentTime = epochTime;
				XSSFRow row = sheetIn.getRow(r);
				String channel = getChannelName(i, channels);
				dataPoints.add(new IncomingDataPoint("ecg.uv." + channel, currentTime, String.valueOf(row.getCell(1).getNumericCellValue()), tags));
				tags.put("format", "excel");
				currentTime++;
			}
		}
		return dataPoints;
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
}