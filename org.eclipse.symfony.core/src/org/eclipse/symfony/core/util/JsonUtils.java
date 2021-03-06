package org.eclipse.symfony.core.util;

import org.eclipse.symfony.core.log.Logger;
import org.eclipse.symfony.core.model.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Encoding / Decoding for json metadata in the SqlIndex.  
 * 
 * 
 * 
 * @author Robert Gruendler <r.gruendler@gmail.com>
 *
 */
public class JsonUtils {

	private static JSONParser parser = new JSONParser();
	
	@SuppressWarnings("unchecked")
	public static String createReference(String elementName, String qualifier, String viewPath) {
		
		
		JSONObject data = new JSONObject();
		data.put("elementName", elementName);
		data.put("qualifier", qualifier);
		data.put("viewPath", viewPath);
		
		JSONObject header = new JSONObject();
		header.put("type", "reference");
		header.put("data", data);
		
		return header.toString();		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static String createDefaultSyntheticServices() {
		
		JSONArray data = new JSONArray();
		
		JSONObject request = new JSONObject();
		request.put(Service.NAME, "request");
		request.put(Service.CLASS, "Symfony\\Component\\HttpFoundation\\Request");
				
		data.add(request);
		return data.toString();		
		
	}

	public static String getElementType(String metadata) {

		try {
			
			JSONObject json = (JSONObject) parser.parse(metadata);
			String type = (String) json.get("type");
			return type;
		} catch (ParseException e) {
			Logger.logException(e);
		}		
		
		return null;
	}
	

	public static JSONObject getReferenceData(String metadata) {

		try {
			JSONObject header = (JSONObject) parser.parse(metadata);
			return (JSONObject) header.get("data");
		} catch (ParseException e) {

			Logger.logException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject createService(String id, String className) {

		JSONObject service = new JSONObject();
		service.put(Service.NAME, id);
		service.put(Service.CLASS, className);
		return service;		

	}

	public static JSONArray parseArray(String defaults) {

		try {
			return (JSONArray) parser.parse(defaults);
		} catch (Exception e) {			
			Logger.logException(e);
			return new JSONArray();
		}
	}
}
