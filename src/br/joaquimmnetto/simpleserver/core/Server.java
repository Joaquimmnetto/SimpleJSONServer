package br.joaquimmnetto.simpleserver.core;

/**
 * Base interface for both tcp and udp server. A server listens for connections, and when it receives a request, it starts a worker to execute the 
 * command associated to that service.
 * @author Joaquim Neto
 *
 */
public interface Server {
	
	/**
	 * Adds a new service to the server. A service is a function that the server does, and its associated to an alias.
	 * @param serviceAlias - A name to the service
	 * @param service - Service implementation
	 */
	public void registerService(String serviceAlias, Service service);
	/**
	 * Starts the server and listening for connections. It listens until stopped.
	 */
	public void start();
	/**
	 * Stops the server at the nexto loop
	 */
	public void stop();
	/**
	 * If the server show log to a log file
	 * @param loging
	 */
	public void setLoging(boolean loging);
	
}
