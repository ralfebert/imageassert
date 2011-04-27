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
import de.ralfebert.imageassert.compare.Page;
import de.ralfebert.imageassert.compare.junit.JUnitCompareResultHandler;
import de.ralfebert.imageassert.compare.swing.SwingCompareResultHandler;
import de.ralfebert.imageassert.pageimage.IPdfImageSplitter;
import de.ralfebert.imageassert.pageimage.PdfRendererImageSplitter;
import de.ralfebert.imageassert.utils.RuntimeIOException;
import de.ralfebert.imageassert.utils.TemporaryFolder;

/**
 * ImageAssert compares two PDF documents in JUnit tests.
 * 
 * It creates images for every page, by default using the pdf-renderer library,
 * but it can also call ImageMagick or XPDf (see setPdfImageSplitter).
 * 
 * The images are compared pixel-by-pixel. A Swing compare dialog is shown if
 * not running with '-Djava.awt.headless=true'. You can also implement your own
 * way to show the results, see setCompareResultHandler.
 * 
 * Usage example:
 * 
 * <pre>
 * ImageAssert imageAssert = new ImageAssert();
 * imageAssert.assertPdfEquals(SomeClass.class.getResourceAsStream("expected.pdf")),
 * 		new FileInputStream(actualPdfFile));
 * </pre>
 * 
 * @author Ralf Ebert
 */
public class ImageAssert {

	private ICompareResultHandler compareResultHandler = new JUnitCompareResultHandler();
	private IPdfImageSplitter pdfImageSplitter = new PdfRendererImageSplitter();

	public ImageAssert() {
		this(!GraphicsEnvironment.isHeadless());
	}

	public ImageAssert(boolean showCompareDialog) {
		if (showCompareDialog) {
			setCompareResultHandler(new SwingCompareResultHandler());
		}
	}

	private void assertImageEquals(Page expectedImage, Page actualImage) {
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
			Page[] expectedPages = extractPages(expected, "expected.pdf", temporaryFolder);
			Page[] actualPages = extractPages(actual, "actual.pdf", temporaryFolder);

			if (expectedPages.length <= 0)
				fail("No pages in expected PDF!");

			for (int i = 0; i < expectedPages.length; i++) {
				Page expectedPage = expectedPages[i];
				if (i >= actualPages.length) {
					fail(String.format("PDF has not enough pages: was %d, expected %d",
							actualPages.length, expectedPages.length));
				}

				Page actualPage = actualPages[i];
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

	private Page[] extractPages(InputStream pdfStream, String pdfName, TemporaryFolder temp) {
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