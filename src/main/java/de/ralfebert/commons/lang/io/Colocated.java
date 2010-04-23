package de.ralfebert.commons.lang.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;


public class Colocated {

	public static String toString(Object ownerObject, String filename) {
		try {
			return IOUtils.toString(toStream(ownerObject, filename));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public static InputStream toStream(Object ownerObject, String filename) {
		Class<?> ownerClass = ownerObject.getClass();
		InputStream stream = ownerObject.getClass().getResourceAsStream(filename);
		if (stream == null) {
			throw new RuntimeIOException(String.format("%s not found in package %s", filename, ownerClass.getPackage()));
		}
		return stream;
	}

	public static URL toURL(Object ownerObject, String filename) {
		Class<?> ownerClass = ownerObject.getClass();
		URL url = ownerObject.getClass().getResource(filename);
		if (url == null) {
			throw new RuntimeIOException(String.format("%s not found in package %s", filename, ownerClass.getPackage()));
		}
		return url;
	}

}
