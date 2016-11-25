package br.joaquimmnetto.simpleserver.logger;

import java.io.PrintStream;

/**
 * Interface to log stream that will store server info.
 * Composed by diverse prints models, and it may write to differents streams
 * @author Joaquim Neto
 *
 */
public interface ServerLog {
	
	public void println(String message);

	public void printlnErr(String message);

	public void printException(Exception e);

	public void printException(Exception e, String command);
	
	public PrintStream getMainPrintStream();	

	public PrintStream getErrorWriter();

	public PrintStream getSecondaryWriter();

	public void setEnabled(boolean enabled);
	
}
