package edu.jhu.cvrg.timeseriesstore.query;
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
*/
import java.util.HashMap;

import org.json.JSONObject;

import edu.jhu.cvrg.timeseriesstore.opentsdb.TimeSeriesRetriever;

public class OpenTSDBTimeSeriesRetrieverWrapper extends TimeSeriesRetriever implements GenericTimeSeriesRetriever{

	@Override
	public JSONObject retrieveTimeSeriesData(String urlString, long startEpoch, long endEpoch, String metric, HashMap<String, String> tags) {
		return TimeSeriesRetriever.retrieveTimeSeries(urlString, startEpoch, endEpoch, metric, tags);
	}

	@Override
	public String retrieveTimeSeriesId(String urlString, HashMap<String, String> tags, String metric) {
		return super.findTsuid(urlString, tags, metric);
	}
}
