package de.ralfebert.imageassert.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;


public class TemporaryFolder {

	private final static Logger log = Logger.getLogger("TemporaryFolder");

	private final String name;
	private File folder;

	public TemporaryFolder(String name) {
		this.name = name;
	}

	public TemporaryFolder(Object forObject) {
		this.name = forObject.getClass().getSimpleName();
	}

	public File createFile(String filename) {
		return new File(getFolder(), filename);
	}

	public File getFolder() {
		if (folder == null) {
			try {
				folder = File.createTempFile(name, "");
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
			if (folder.exists()) {
				folder.delete();
			}
			if (!this.folder.mkdirs()) {
				throw new RuntimeIOException(String.format("Temporary folder '%s' could not be created: ", folder));
			}
			log.info("Created temporary folder " + folder);
		}
		return folder;
	}

	public void dispose() {
		if (this.folder != null) {
			try {
				FileUtils.deleteDirectory(this.folder);
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
			log.info("Disposed temporary folder " + folder);
		}
	}

}
