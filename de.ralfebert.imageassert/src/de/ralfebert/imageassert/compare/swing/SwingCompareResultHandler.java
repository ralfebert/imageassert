package de.ralfebert.imageassert.compare.swing;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import de.ralfebert.imageassert.compare.ICompareResultHandler;

public class SwingCompareResultHandler implements ICompareResultHandler {

	@Override
	public void onImageNotEqual(final File expectedFile, final File actualFile,
			final BufferedImage expected, final BufferedImage actual) {
		ImageCompareDialog imageCompareDialog = new ImageCompareDialog(expected, actual) {
			@Override
			protected void onApply() {
				System.err.println("Wrote PDF to " + expectedFile);
				try {
					FileOutputStream out = new FileOutputStream(expectedFile);
					IOUtils.copy(new FileInputStream(actualFile), out);
					out.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			protected void onReject() {
				fail("Expected: " + expectedFile.getName());
			}
		};
		imageCompareDialog.open();
	}

}
