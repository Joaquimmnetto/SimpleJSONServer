package br.joaquimmnetto.simpleserver.core;

public interface Server {
	
	public void registerService(String serviceAlias, Service service);
	public void start();
	public void stop();
	public void setLoging(boolean loging);
	
}
