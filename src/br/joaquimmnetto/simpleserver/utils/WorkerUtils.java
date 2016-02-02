package br.joaquimmnetto.simpleserver.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.DatagramSocket;
import java.net.Socket;

import org.json.JSONObject;

public class WorkerUtils {

	public static void tcpSend(Socket sock, JSONObject response) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
		out.write(response.toString().getBytes());
		out.write(255);
		out.flush();
	}
	
	public static JSONObject tcpRecv(Socket sock) throws IOException {
		BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
		StringWriter inJson = new StringWriter();
		int readen = -1;
		while (true) {
			readen = in.read();

			if (readen < 0 || readen > 254) {
				break;
			}
			inJson.append((char) readen);
		}

		return new JSONObject(inJson.toString());
	}
	

}
