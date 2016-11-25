package br.joaquimmnetto.simpleserver.utils;
import java.io.IOException;

import org.json.JSONObject;


public class JSONUtils {
	
	
	/**
	 * Creates a response JSON for a well succeded request. More atributes can(and will) be appended later, with the service results.
	 * formato{'success':true, 'message': "Operation successful"}
	 * @return.
	 */
	public static JSONObject createJSONSuccess(){
		return new JSONObject().put("success", true)
						.put("message", "Operation successful");
	}
	
	/**
	 * Creates a response JSON for the case when some fail ocours at the server.
	 * formato{'success':false, 'message': message}
	 * @return.
	 */
	public static JSONObject createJSONFail(String message){
		return new JSONObject().put("success", false)
				.put("message", message);
	}
	/**
	 * Creates a response JSON for the case when some exception ocours at the server. 
	 * formato{'success':true, 'message': e.getMessage()}
	 * @return.
	 */
	public static JSONObject createJSONFail(Exception e){
		return new JSONObject().put("success", false)
				.put("message", e.getMessage());
	}


}
