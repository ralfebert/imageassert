package de.ralfebert.commons.lang.launch;

import org.apache.commons.lang.StringUtils;

public class LaunchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int exitValue;
	private final String commandLine;
	private final String output;
	private final String errors;

	public LaunchException(int exitValue, String commandLine, String output, String errors) {
		this.exitValue = exitValue;
		this.commandLine = commandLine;
		this.output = output;
		this.errors = errors;
	}

	@Override
	public String getMessage() {
		String message = String.format("Exit value %d from process '%s'", exitValue, commandLine);
		if (StringUtils.isNotBlank(errors)) {
			message += "\n(Errors) " + errors.trim();
		}
		if (StringUtils.isNotBlank(output)) {
			message += "\n(Output) " + output.trim();
		}
		return message;
	}

	public String getCommandLine() {
		return commandLine;
	}

	public String getErrors() {
		return errors;
	}

	public String getOutput() {
		return output;
	}

	public int getExitValue() {
		return exitValue;
	}

}
