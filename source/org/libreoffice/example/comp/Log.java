package org.libreoffice.example.comp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	private static final String msLogFilename = "sidebar.log"; // "c:\\tmp\\sidebar.log";

	public static Log Instance() {
		if (maInstance == null) {
			maInstance = new Log();
			maInstance.println("");
			maInstance.println("-----------------------------------------------------------");
			maInstance.println("starting new log at " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
		}

		return maInstance;
	}

	private Log() {
	}

	public void println(final String sMessage) {
		if (msLogFilename == null)
			return;

		try {
			final PrintStream aOut = new PrintStream(new FileOutputStream(new File(System.getProperty("user.home"), msLogFilename), true));
			if (aOut != null) {
				aOut.println(sMessage);
				aOut.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void PrintStackTrace(final Exception aException) {
		if (msLogFilename == null)
			return;

		try {
			final PrintStream aOut = new PrintStream(new FileOutputStream(new File(msLogFilename), true));
			if (aOut != null) {
				aException.printStackTrace(aOut);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Log maInstance = null;
}
