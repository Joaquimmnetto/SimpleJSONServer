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

/**
 * An UDP implementation of server.
 * @author KithLenovo
 *
 */
public class UDPServer implements Server{

	private DatagramSocket sock;
	private byte[] buffer;
	
	private Services services = new Services();
	private ServerLog log;
	
	private Executor workerExec;
	private Thread serverThread;
	private boolean running;
	/**
	 * 
	 * @param port - Port to which this server should listen to
	 * @param poolSize - Max number of simultaneous workers
	 * @param bufferSize - Size of the buffer used to store requests from client
	 * @param log - Log associated to this server
	 */
	public UDPServer(int port, int poolSize, int bufferSize, ServerLog log) {
		try {
			this.log = log;
			workerExec = Executors.newFixedThreadPool(poolSize);
			sock = new DatagramSocket(port);
			buffer = new byte[bufferSize];
		} catch (SocketException e) {
			log.printException(e, null);
		}
	}
	/**
	 * The standard size for the buffer on this constructor is 512, and the standard log will be used.
	 * @param lookupPort
	 * @param poolSize
	 */
	public UDPServer(int lookupPort, int poolSize) {
		this(lookupPort, poolSize, 512, ServerLogImpl.getStandardLog());
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
		DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
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
