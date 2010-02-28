package de.ralfebert.imageassert.compare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PageImage {

	private final File file;
	private BufferedImage image;

	public PageImage(File file) {
		super();
		this.file = file;
	}

	public String getName() {
		return file.getName();
	}

	public BufferedImage getImage() {
		if (image == null) {
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return image;
	}

	public File getFile() {
		return file;
	}

}
