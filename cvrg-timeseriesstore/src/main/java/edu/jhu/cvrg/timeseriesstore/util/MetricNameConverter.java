package edu.jhu.cvrg.timeseriesstore.util;

public class MetricNameConverter {

	public static String convert(String term){
		String result = "";
		
		switch(term){
			case "/min": 		result = "per.min";						break;
			case "#/min": 		result = "num.per.min";					break;
			case "breaths/min": result = "breaths.per.min";				break;
			case "mm(hg)": 		result = "mm.hg";						break;
			case "cel": 		result = "degrees.celsius";				break;
			case "%": 			result = "percent";						break;
			default: 			System.out.println("No match found.");	break;
		}
		
		return result;
	}
}
