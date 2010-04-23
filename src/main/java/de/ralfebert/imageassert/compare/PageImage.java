package de.ralfebert.imageassert.compare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.ralfebert.imageassert.utils.RuntimeIOException;

public class PageImage {

	private final File file;
	private final File pdfFile;
	private BufferedImage image;

	public PageImage(File pageImageFile, File pdfFile) {
		super();
		this.file = pageImageFile;
		this.pdfFile = pdfFile;
	}

	public String getName() {
		return file.getName();
	}

	public BufferedImage getImage() {
		if (image == null) {
			try {
				image = ImageIO.read(file);
				if (image == null) {
					throw new RuntimeException("ImageIO couldn't read " + file);
				}
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}
		return image;
	}

	public File getFile() {
		return file;
	}

	public File getPdfFile() {
		return pdfFile;
	}

}
