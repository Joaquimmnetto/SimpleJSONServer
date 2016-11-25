package br.joaquimmnetto.simpleserver.logger;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerLogImpl implements ServerLog {

	private PrintStream out;
	private PrintStream err;
	private PrintStream file;

	private boolean enabled;

	private static ServerLogImpl standardLog;
	
	private static final String LOG_FILE_NAME_PREFIX = "Log_";
	private static final String LOG_FILE_NAME_SUFIX = ".log";

	private static final Executor logExecutor = Executors.newSingleThreadExecutor();
	
	
	public static void generateNewStandardLog() {

		try {
			Calendar current = Calendar.getInstance();
			String logFileName = LOG_FILE_NAME_PREFIX + current.get(Calendar.DATE) + "-"
					+ (current.get(Calendar.MONTH) + 1) + "-" + current.get(Calendar.YEAR) + "_"
					+ current.get(Calendar.HOUR_OF_DAY) + "-" + (current.get(Calendar.MINUTE)) + "-"
					+ current.get(Calendar.SECOND) + LOG_FILE_NAME_SUFIX;
			standardLog = new ServerLogImpl(System.out, System.err, new PrintStream(logFileName));
		} catch (FileNotFoundException e) {
			// TODO: vish, e ai? Stack trace no stdout mesmo?
			e.printStackTrace();
		}
	}

	public static ServerLogImpl getStandardLog() {
		if (standardLog == null) {
			generateNewStandardLog();
		}
		return standardLog;
	}

	public ServerLogImpl(PrintStream mainOut, PrintStream mainError) {
		this.out = mainOut;
		this.err = mainError;

	}

	public ServerLogImpl(PrintStream mainOut, PrintStream mainError, PrintStream secondary)
			throws FileNotFoundException {
		this.out = mainOut;
		this.err = mainError;
		this.enabled = true;
		if (secondary != null) {
			this.file = secondary;

		}
	}

	public synchronized void println(String message) {
		message = getTimestamp() + message;
		println(out, message);
		if (file != null) {
			println(file, message);
		}

	}

	public synchronized void printlnErr(String message) {
		message = getTimestamp() + message;
		println(err, message);
		if (file != null) {
			println(file, message);
		}
	}

	public synchronized void printException(Exception e) {
		printException(e, null);
	}

	public synchronized void printException(Exception e, String command) {
		if (command != null) {
			printlnErr("Erro enquanto executando: " + command);
			printlnErr("");
		}
		e.printStackTrace(err);
		if (file != null) {
			e.printStackTrace(file);
		}
	}

	private synchronized void println(final PrintStream stream, final String message) {
		if (enabled) {
			logExecutor.execute(new Runnable() {
				@Override
				public void run() {
					stream.println(message);
				}
			});
		}
	}

	public PrintStream getMainPrintStream() {
		return out;
	}

	public PrintStream getErrorWriter() {
		return err;
	}

	public PrintStream getSecondaryWriter() {
		return file;
	}

	private String getTimestamp() {

		Date current = new Date();

		StringBuilder timestamp = new StringBuilder();

		timestamp.append('[').append(current).append(']');
		timestamp.append('\t');

		return timestamp.toString();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
