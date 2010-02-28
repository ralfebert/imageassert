package de.ralfebert.imageassert.compare.junit;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.Assert;

import de.ralfebert.imageassert.compare.IImageCompareHandler;

public class JUnitImageCompareHandler implements IImageCompareHandler {

	@Override
	public void onImageNotEqual(File expectedFile, File actualFile, BufferedImage expected,
			BufferedImage actual) {
		Assert.fail(String.format("Expected: %s, actual: %s", expectedFile, actualFile));
	}

}
