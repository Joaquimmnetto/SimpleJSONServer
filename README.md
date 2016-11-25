# SimpleJSONServer
Light, simple server with JSON queries/responses implemented on Java. This wasn't made for big, professional implementations, 
so keep that in mind :)

It functions as following: First, we start one of the implementations of the server interface(TCP and UDP implementations are provided, but you can do one of your own). Then we register some implementations of the Service class into your Server object. Each one of the Service objects represent the services that you wish your server to provide, each server must be associated with an alias, that is the name on that the client will use to call the service.

For the server implementations provided, a Worker is started for every request sent, up to a parameter-set limitation. The worker then identifies which service should be called and then returns the awnser to the client. After this, the worker dies. Everything is logged or on a default log file, or on one you can provide, implementing the ServerLog interface.

JSON formats for request JSONs are provided below:

request:
{
  'command' = service_alias
  'args' = service_arguments
}

The 'service_arguments' parameter can be anything that you wish, and will be a parameter to the Service you implemented.

The response is anything that you set on your Service implementations, but we provide a JSONUtils class that implements some simple response protocol, that simply indicates if your request was successful, and a message detailing it:

response:
{
  success:true_or_false
  message:success_or_error_details
}

Then you should, of course add your the response of your Services to the resulting JSONObject. 

Below a usage example:
    
    //Generate a log to be used by the servers
    ServerLogImpl.generateNewStandardLog();
		log = ServerLogImpl.getStandardLog();
		
		log.println("Your IP is: "+InetAddress.getLocalHost());
		
		log.println("Starting UDP server on:" + portLookup);		
    //Creating UDP Server, portLookup is the port where it should listen, workerPoolSize is the max. number of simultaneous workers.
		final UDPServer lookupServer = new UDPServer(portLookup,workerPoolSize);
    //The ServerLookup object is a implementation of the Service class, we are registering it with the "ServerLookup" alias.
		lookupServer.registerService("ServerLookup", new ServerLookup());
		lookupServer.start();
		
		
		log.println("Starting server at port " + port);
    //Creating TCP server,  portLookup is the port where it should listen, workerPoolSize is the max. number of simultaneous workers.
    //In this case, workerPoolSize is the same for both servers, but it isn't mandatory.
		final TCPServer appServer = new TCPServer(port,workerPoolSize);
    //Registering more implementations of the Service class.
		appServer.registerService("EnviarVenda", new EnviarVenda());
		appServer.registerService("ImportarProdutos", new ImportarProdutos());
		appServer.registerService("ServerStatus", new ServerStatus());
		    
    //Finnaly starting the server!
		appServer.start();
		
    //If the process dies, the servers should be stopped properly!
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				lookupServer.stop();
				appServer.stop();
				log.println("Servidor encerrado com sucesso.");
			}
		}));


