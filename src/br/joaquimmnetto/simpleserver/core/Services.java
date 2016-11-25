package br.joaquimmnetto.simpleserver.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class that manages the Serices that a server provides
 * @author KithLenovo
 *
 */
public class Services {
	
	/**
	 * Dictionary associating each service with its alias.
	 */
	private Map<String, Service> services = new HashMap<>();

	
	/**
	 * Registers a new service on the services map.
	 * @param serviceAlias - The new Service command name, or alias.
	 * @param service - The new Service implementation
	 */
	public void registerService(String serviceAlias, Service service) {
		services.put(serviceAlias.toLowerCase(), service);
	}
	
	/**
	 * Execute a serice with the given parameters
	 * @param serviceAlias - Alias of the service to be executed
	 * @param params - Params to be given to that service.
	 * @return
	 */
	public JSONObject executeService(String serviceAlias, JSONArray params){
		return services.get(serviceAlias.toLowerCase()).processCommand(params);
	}

}
