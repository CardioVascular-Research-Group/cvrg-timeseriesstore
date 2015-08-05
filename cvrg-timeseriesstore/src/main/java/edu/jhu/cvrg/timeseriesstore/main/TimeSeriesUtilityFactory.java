package edu.jhu.cvrg.timeseriesstore.main;
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
import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesDatabaseType;
import edu.jhu.cvrg.timeseriesstore.enums.EnumTimeSeriesStorerType;
import edu.jhu.cvrg.timeseriesstore.opentsdb.store.OpenTSDBTimeSeriesStorer;
import edu.jhu.cvrg.timeseriesstore.opentsdb.store.OpenTSDBTimeSeriesStorerFactory;
import edu.jhu.cvrg.timeseriesstore.query.OpenTSDBTimeSeriesRetrieverWrapper;
import edu.jhu.cvrg.timeseriesstore.query.GenericTimeSeriesRetriever;


public class TimeSeriesUtilityFactory {

	private TimeSeriesUtilityFactory(){}
	
	public static OpenTSDBTimeSeriesStorer returnTimeSeriesStorerUtility(EnumTimeSeriesDatabaseType timeSeriesDatabase, EnumTimeSeriesStorerType timeSeriesStorerType){

		switch(timeSeriesDatabase){
		case OPENTSDB:
			return OpenTSDBTimeSeriesStorerFactory.returnFileStore(timeSeriesStorerType);
		default:
			return null;	
		}
	}
	
	public static GenericTimeSeriesRetriever returnTimeSeriesRetrieverUtility(EnumTimeSeriesDatabaseType timeSeriesDatabase){

		switch(timeSeriesDatabase){
		case OPENTSDB:
			return new OpenTSDBTimeSeriesRetrieverWrapper();
		default:
			return null;	
		}
	}
	
}
