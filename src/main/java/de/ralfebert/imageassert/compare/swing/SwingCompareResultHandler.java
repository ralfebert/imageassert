package de.ralfebert.imageassert.compare.swing;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import de.ralfebert.imageassert.compare.ICompareResultHandler;
import de.ralfebert.imageassert.compare.Page;

public class SwingCompareResultHandler implements ICompareResultHandler {

	private static final Logger log = Logger.getLogger(SwingCompareResultHandler.class.getName());

	public void onImageNotEqual(final Page expected, final Page actual) {
		ImageCompareDialog imageCompareDialog = new ImageCompareDialog(expected.getImage(),
				actual.getImage()) {

			@Override
			protected void onApply(File saveToFile) {
				try {
					FileOutputStream out = new FileOutputStream(saveToFile);
					IOUtils.copy(new FileInputStream(actual.getPdfFile()), out);
					out.close();
					log.info("Wrote PDF to " + saveToFile.getAbsolutePath());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			protected void onReject() {
				fail("Expected: " + expected.getName());
			}

		};
		imageCompareDialog.open();
	}
}
