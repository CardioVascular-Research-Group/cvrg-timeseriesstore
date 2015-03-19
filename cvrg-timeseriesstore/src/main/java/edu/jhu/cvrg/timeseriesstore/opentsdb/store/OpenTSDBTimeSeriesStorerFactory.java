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
import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesStorerType;

public class OpenTSDBTimeSeriesStorerFactory {

	private OpenTSDBTimeSeriesStorerFactory(){}
	
	public static OpenTSDBTimeSeriesStorer returnFileStore(EnumTimeSeriesStorerType timeSeriesStorer){

		switch(timeSeriesStorer){
		case EXCEL:
			return new ExcelStorer();
		case WFDB:
			return new WfdbStorer();
		case PHILLIPS103:
			return new Phillips103Storer();
		case PHILLIPS104:
			return new Phillips104Storer();
		case MUSE:
			return new MuseStorer();
		case MUSEXML:
			return new MuseXMLStorer();
		case SCHILLER:
			return new SchillerStorer(); 
		case HL7AECG:
			return new Hl7AecgStorer();
		default:
			return null;	
		}
	}
}
