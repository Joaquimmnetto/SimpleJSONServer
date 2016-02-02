package br.joaquimmnetto.simpleserver.core;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Service {
	JSONObject processCommand(JSONArray params);
}
