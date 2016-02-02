package br.joaquimmnetto.simpleserver.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Services {
	
	private Map<String, Service> services = new HashMap<>();

	

	public void registerService(String serviceAlias, Service service) {
		services.put(serviceAlias.toLowerCase(), service);
	}
	
	public JSONObject executeService(String serviceAlias, JSONArray params){
		return services.get(serviceAlias.toLowerCase()).processCommand(params);
	}

}
