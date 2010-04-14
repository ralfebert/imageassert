package de.ralfebert.commons.lang.launch;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.ralfebert.commons.lang.io.RuntimeIOException;

/**
 * Helper class to launch unix command line tools. System out contents are
 * returned, if the exit code is not EXIT_SUCCESS a {@link LaunchException} is
 * thrown.
 * 
 * @author Ralf Ebert
 */
public class UnixLauncher {

	private final static Logger log = Logger.getLogger("UnixLauncher");

	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = 1;

	public String launch(ProcessBuilder processBuilder) throws LaunchException {
		try {
			String commandLine = StringUtils.join(processBuilder.command().iterator(), " ");
			log.info("Launching: " + commandLine);
			Process process = processBuilder.start();
			int exitValue = process.waitFor();
			String output = IOUtils.toString(process.getInputStream());
			if (exitValue == EXIT_SUCCESS) {
				return output;
			} else {
				throw new LaunchException(exitValue, commandLine, IOUtils.toString(process
						.getInputStream()), IOUtils.toString(process.getErrorStream()));
			}
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public String launch(String... command) throws LaunchException {
		return launch(new ProcessBuilder(command));
	}

}