package de.ralfebert.commons.lang.io;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(IOException cause) {
		super(cause);
	}

}
