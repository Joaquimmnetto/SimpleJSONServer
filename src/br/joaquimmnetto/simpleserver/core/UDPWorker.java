package br.joaquimmnetto.simpleserver.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.joaquimmnetto.simpleserver.logger.ServerLog;
import br.joaquimmnetto.simpleserver.utils.JSONUtils;


/**
 *  A Runnable implementation of the work to be executed by each call to the UDP server.
 * @author Joaquim Neto
 *
 */
public class UDPWorker implements Runnable {
	
	
	private DatagramPacket recvPack;
	private DatagramSocket sock;
	
	private Services services;
	
	private ServerLog log;
	/**
	 * 
	 * @param sock - the socket with the connection to the client.
	 * @param pack - The datagram package with the client request
	 * @param services - The server provided by the server
	 * @param log - The log object registered on the server.
	 */
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

			log.println("UDP message received:\n" + request.toString(4));

			command = request.getString("command");
			JSONArray args = request.optJSONArray("args");

			JSONObject response = services.executeService(command,args);

			String responseStr = response.toString(4);

			DatagramPacket sendPack = new DatagramPacket(responseStr.getBytes(), responseStr.length(),
					recvPack.getAddress(), recvPack.getPort());
			log.println("Sending UDP response:\n" + response.toString(4));
			sock.send(sendPack);

		} catch (IOException | JSONException e) {
			log.printException(e, command);
			JSONUtils.createJSONFail(e);
		}

	}
}
