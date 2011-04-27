package de.ralfebert.imageassert.compare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.ralfebert.imageassert.utils.RuntimeIOException;

public final class Page {

	private final String name;
	private final BufferedImage image;
	private final File pdfFile;

	public Page(File imageFile, File pdfFile) {
		super();
		this.name = imageFile.getName();
		this.pdfFile = pdfFile;

		try {
			image = ImageIO.read(imageFile);
			if (image == null) {
				throw new RuntimeException("javax.imageio.ImageIO couldn't read " + imageFile);
			}
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public Page(BufferedImage image, String name, File pdfFile) {
		this.image = image;
		this.name = name;
		this.pdfFile = pdfFile;
	}

	public String getName() {
		return name;
	}

	public synchronized BufferedImage getImage() {
		return image;
	}

	public File getPdfFile() {
		return pdfFile;
	}

}