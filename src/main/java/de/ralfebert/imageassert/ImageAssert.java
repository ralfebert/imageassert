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

import de.ralfebert.imageassert.compare.ICompareResultHandler;
import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.compare.junit.JUnitCompareResultHandler;
import de.ralfebert.imageassert.pageimage.IPdfImageSplitter;
import de.ralfebert.imageassert.pageimage.ImageMagickSplitter;
import de.ralfebert.imageassert.utils.RuntimeIOException;
import de.ralfebert.imageassert.utils.TemporaryFolder;

public class ImageAssert {

	private ICompareResultHandler compareResultHandler = new JUnitCompareResultHandler();
	private IPdfImageSplitter pdfImageSplitter = new ImageMagickSplitter();

	public ImageAssert() {
		this(!GraphicsEnvironment.isHeadless());
	}

	public ImageAssert(boolean showCompareDialog) {
		if (showCompareDialog) {
			showCompareDialog();
		}
	}

	private void showCompareDialog() {
		try {
			if (Class.forName("org.eclipse.swt") != null) {

			}
		} catch (ClassNotFoundException e) {
			// setCompareResultHandler(new SwingCompareResultHandler());
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
			PageImage[] expectedPages = extractPages(expected, "expected.pdf", temporaryFolder);
			PageImage[] actualPages = extractPages(actual, "actual.pdf", temporaryFolder);

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

	private PageImage[] extractPages(InputStream pdfStream, String pdfName, TemporaryFolder temp) {
		pdfImageSplitter.setTemporaryFolder(temp);
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
		return pdfImageSplitter.convert(actualFile);
	}

	public void setCompareResultHandler(ICompareResultHandler compareResultHandler) {
		this.compareResultHandler = compareResultHandler;
	}

	public void setPdfImageSplitter(IPdfImageSplitter pdfImageSplitter) {
		this.pdfImageSplitter = pdfImageSplitter;
	}

}