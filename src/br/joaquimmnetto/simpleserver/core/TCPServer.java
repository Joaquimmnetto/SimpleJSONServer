package br.joaquimmnetto.simpleserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import br.joaquimmnetto.simpleserver.logger.ServerLog;
import br.joaquimmnetto.simpleserver.logger.ServerLogImpl;


/**
 * TCP implementation of Server.
 * @author Joaquim Neto
 *
 */
public class TCPServer implements Server {

	private ServerSocket sSock;

	private Services services = new Services();

	private Executor workerExec;
	private Thread serverThread;
	private boolean running;

	private ServerLog log = ServerLogImpl.getStandardLog();
	/**
	 * Creates a new TCPServer
	 * @param port - The port in which the server should be started on.
	 * @param poolSize - The maximum simultaneous number of workers to be executed on this server
	 */
	public TCPServer(int port, int poolSize) {
		try {
			workerExec = Executors.newFixedThreadPool(poolSize);
			sSock = new ServerSocket(port);
		} catch (IOException e) {
			log.printException(e);
		}
	}
	/**
	 * Creates a new TCPServer
	 * @param port - The port in which the server should be started on.
	 * @param poolSize - The maximum simultaneous number of workers to be executed on this server
	 * @param log - Custom Log to receive this server info output.
	 */
	public TCPServer(int porta, int poolSize, ServerLog log) {
		try {
			this.log = log;
			workerExec = Executors.newFixedThreadPool(poolSize);
			sSock = new ServerSocket(porta);
		} catch (IOException e) {
			log.printException(e);
		}
	}

	public void registerService(String serviceAlias, Service service) {
		services.registerService(serviceAlias, service);
	}

	@Override
	public void setLoging(boolean loging) {
		log.setEnabled(loging);
	}

	public void start() {
		if (serverThread == null) {

			serverThread = new Thread(new Runnable() {
				@Override
				public void run() {
					log.println("Aguardando conexoes...");
					while (!serverThread.isInterrupted() && running) {
						TCPWorker worker = listen();
						if (worker == null) {
							continue;
						}
						log.println("Conexao com " + worker.getAddress() + ":" + worker.getPort() + " estabelecida.");
						workerExec.execute(worker);
					}
					try {
						sSock.close();
					} catch (IOException e) {
						ServerLogImpl.getStandardLog().printException(e);
					}
				}
			});
			running = true;
			serverThread.start();
		}
	}

	public void stop() {
		running = false;
	}
	
	/**
	 * Listens to a new connection, and starts a TCPWorker when its accepted.
	 * @return
	 */
	private TCPWorker listen() {
		try {
			return new TCPWorker(sSock.accept(), services, log);
		} catch (IOException e) {
			ServerLogImpl.getStandardLog().printException(e);
		}

		return null;

	}
	/**
	 * Close the ServerSocket associated with this server.
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (sSock != null) {
			sSock.close();
		}
	}

}
