package br.joaquimmnetto.simpleserver.utils;
import org.json.JSONObject;


public class JSONUtils {
	
	
	
	public static JSONObject criarJSONSucesso(){
		return new JSONObject().put("success", true)
						.put("message", "Operacao realizada com sucesso");
	}
	
	
	public static JSONObject criarJSONFalha(String message){
		return new JSONObject().put("success", false)
				.put("message", message);
	}
	
	public static JSONObject criarJSONFalha(Exception e){
		return new JSONObject().put("success", false)
				.put("message", e.getMessage());
	}
	

}
