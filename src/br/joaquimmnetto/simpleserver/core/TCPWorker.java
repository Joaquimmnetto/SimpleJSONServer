package br.joaquimmnetto.simpleserver.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.joaquimmnetto.simpleserver.logger.ServerLog;
import br.joaquimmnetto.simpleserver.utils.JSONUtils;
import br.joaquimmnetto.simpleserver.utils.WorkerUtils;

public class TCPWorker implements Runnable {

	private Services services;
	private Socket comm;
	private ServerLog log;

	public TCPWorker(Socket comm, Services services, ServerLog log) {
		this.services = services;
		this.comm = comm;
		this.log = log;
	}

	public void run() {

		JSONObject response;
		String command = null;
		// recv
		try {
			JSONObject clientMsg = WorkerUtils.tcpRecv(comm);
			
			log.println("Mensagem recebida:\n" + clientMsg.toString(4));

			command = clientMsg.getString("comando");
			JSONArray args = clientMsg.optJSONArray("args");

			response = services.executeService(command, args);

			log.println("Resposta processada:\n" + response.toString(4));

		} catch (IOException e) {
			log.printException(e, command);
			response = JSONUtils.criarJSONFalha(e);
		} catch (JSONException e) {
			log.printException(e, command);
			response = JSONUtils.criarJSONFalha(e);
		}
		// send
		try {
			WorkerUtils.tcpSend(comm, response);
		} catch (IOException e) {
			log.printException(e, command);
		}
		try {
			comm.close();
		} catch (IOException e) {
			log.printException(e, command);
		}
	}

	public synchronized InetAddress getAddress() {
		return comm.getInetAddress();
	}

	public int getPort() {
		return comm.getPort();
	}

	public void setLog(ServerLog log) {
		this.log = log;
	}

}
