package br.joaquimmnetto.simpleserver.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.joaquimmnetto.simpleserver.logger.ServerLog;
import br.joaquimmnetto.simpleserver.utils.JSONUtils;

public class UDPWorker implements Runnable {
	
	
	private DatagramPacket recvPack;
	private DatagramSocket sock;
	
	private Services services;
	
	private ServerLog log;

	public UDPWorker(DatagramSocket sock, DatagramPacket pack, Services services, ServerLog log) {
		this.services = services;
		this.sock = sock;
		this.recvPack = pack;
	}

	@Override
	public void run() {
		String command = null;
		try {
			JSONObject request = new JSONObject(new String(recvPack.getData()));

			log.println("Lookup recebido:\n" + request.toString(4));

			command = request.getString("comando");
			JSONArray args = request.optJSONArray("args");

			JSONObject response = services.executeService(command,args);

			String responseStr = response.toString(4);

			DatagramPacket sendPack = new DatagramPacket(responseStr.getBytes(), responseStr.length(),
					recvPack.getAddress(), recvPack.getPort());
			log.println("Enviando resposta:\n" + response.toString(4));
			sock.send(sendPack);

		} catch (IOException | JSONException e) {
			log.printException(e, command);
			JSONUtils.criarJSONFalha(e);
		}

	}
}
