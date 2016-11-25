package br.joaquimmnetto.simpleserver.core;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Interface to be implemented with a service that the server provides. 
 * @author KithLenovo
 *
 */
public interface Service {
	/**
	 * Body of the service implementation
	 * @param params - Remote parameters sent by the client
	 * @return JSONObject with the response to be sent to the client.
	 */
	JSONObject processCommand(JSONArray params);
}
