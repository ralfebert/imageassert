package de.ralfebert.imageassert.compare;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ICompareResultHandler {

	void onImageNotEqual(File expectedFile, File actualFile, BufferedImage expected,
			BufferedImage actual);

}
