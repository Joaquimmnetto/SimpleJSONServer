package br.joaquimmnetto.simpleserver.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import br.joaquimmnetto.simpleserver.logger.ServerLog;
import br.joaquimmnetto.simpleserver.logger.ServerLogImpl;

public class UDPServer implements Server{

	private DatagramSocket sock;
	private byte[] buffer = new byte[512];
	
	private Services services = new Services();
	private ServerLog log;
	
	private Executor workerExec;
	private Thread serverThread;
	private boolean running;

	public UDPServer(int lookupPort, int poolSize, ServerLog log) {
		try {
			this.log = log;
			workerExec = Executors.newFixedThreadPool(poolSize);
			sock = new DatagramSocket(lookupPort);
			buffer = new byte[512];
		} catch (SocketException e) {
			log.printException(e, null);
		}
	}
	
	public UDPServer(int lookupPort, int poolSize) {
		this(lookupPort, poolSize, ServerLogImpl.getStandardLog());
	}
	

	@Override
	public void registerService(String serviceAlias, Service service) {
		services.registerService(serviceAlias, service);
		
	}
	
	@Override
	public void setLoging(boolean loging) {
		log.setEnabled(loging);
	}

	@Override
	public void start() {
		if (serverThread == null) {

			serverThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (!serverThread.isInterrupted() && running) {
						UDPWorker worker = listen();
						if (worker != null) {
							workerExec.execute(worker);
						}

					}
					sock.close();
				}
			});
			running = true;
			serverThread.start();
		}
	}
	
	@Override
	public void stop() {
		running = false;
		serverThread = null;

	}

	private UDPWorker listen() {
		DatagramPacket pack = new DatagramPacket(buffer, 512);
		try {
			sock.receive(pack);
			new JSONObject(new String(pack.getData()));
			return new UDPWorker(sock, pack, services, log);

		} catch (IOException e1) {

			ServerLogImpl.getStandardLog().printException(e1, null);
		} catch (JSONException e) {
			/* DO nothing */}

		return null;

	}



}
