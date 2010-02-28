package de.ralfebert.imageassert.compare;

import java.awt.image.BufferedImage;
import java.io.File;

public interface IImageCompareHandler {

	void onImageNotEqual(File expectedFile, File actualFile, BufferedImage expected,
			BufferedImage actual);

}
