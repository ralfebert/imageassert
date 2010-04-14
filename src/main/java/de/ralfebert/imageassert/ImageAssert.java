package de.ralfebert.imageassert;

import static org.junit.Assert.fail;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import de.ralfebert.commons.lang.io.RuntimeIOException;
import de.ralfebert.commons.lang.temp.TemporaryFolder;
import de.ralfebert.imageassert.compare.ICompareResultHandler;
import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.compare.junit.JUnitCompareResultHandler;
import de.ralfebert.imageassert.compare.swt.SwtCompareResultHandler;
import de.ralfebert.imageassert.utils.Pdf2Png;

public class ImageAssert {

	private ICompareResultHandler compareResultHandler = new JUnitCompareResultHandler();

	public ImageAssert() {
		this(!GraphicsEnvironment.isHeadless());
	}

	public ImageAssert(boolean showCompareDialog) {
		if (showCompareDialog) {
			compareResultHandler = new SwtCompareResultHandler();
		}
	}

	private void assertImageEquals(PageImage expectedImage, PageImage actualImage) {
		boolean equal = Arrays.equals(extractPixels(expectedImage.getImage()),
				extractPixels(actualImage.getImage()));
		if (!equal) {
			compareResultHandler.onImageNotEqual(expectedImage, actualImage);
		}
	}

	private int[] extractPixels(BufferedImage image) {
		return image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), (int[]) null);
	}

	public void assertPdfEquals(InputStream expected, InputStream actual) {
		TemporaryFolder temporaryFolder = new TemporaryFolder(this);

		try {
			PageImage[] expectedPages = getPages(expected, "expected.pdf", temporaryFolder);
			PageImage[] actualPages = getPages(actual, "actual.pdf", temporaryFolder);

			if (expectedPages.length <= 0)
				fail("No pages in expected PDF!");

			for (int i = 0; i < expectedPages.length; i++) {
				PageImage expectedPage = expectedPages[i];
				if (i >= actualPages.length) {
					fail(String.format("PDF has not enough pages: was %d, expected %d",
							actualPages.length, expectedPages.length));
				}

				PageImage actualPage = actualPages[i];
				assertImageEquals(expectedPage, actualPage);
			}

			if (actualPages.length != expectedPages.length) {
				fail(String.format("PDF has too many pages: was %d, expected %d",
						actualPages.length, expectedPages.length));
			}

		} finally {
			temporaryFolder.dispose();
		}
	}

	private PageImage[] getPages(InputStream pdfStream, String pdfName, TemporaryFolder temp) {
		Pdf2Png pdf2png = new Pdf2Png(temp);
		File actualFile = temp.createFile(pdfName);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(actualFile);
			IOUtils.copy(pdfStream, output);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return pdf2png.convert(actualFile);
	}

	public void setCompareResultHandler(ICompareResultHandler compareResultHandler) {
		this.compareResultHandler = compareResultHandler;
	}

}