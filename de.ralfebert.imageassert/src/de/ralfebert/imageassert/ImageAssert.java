package de.ralfebert.imageassert;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import de.ralfebert.imageassert.compare.ICompareResultHandler;
import de.ralfebert.imageassert.compare.junit.JUnitCompareResultHandler;

public class ImageAssert {

	private final ICompareResultHandler compareHandler;

	public ImageAssert(ICompareResultHandler compareHandler) {
		this.compareHandler = compareHandler;
	}

	public ImageAssert() {
		this.compareHandler = new JUnitCompareResultHandler();
	}

	private void assertImageEquals(final File expectedFile, final File actualFile,
			final BufferedImage expected, BufferedImage actual) {
		boolean equal = Arrays.equals(extractPixels(expected), extractPixels(actual));
		if (!equal) {
			compareHandler.onImageNotEqual(expectedFile, actualFile, expected, actual);
		}
	}

	private int[] extractPixels(BufferedImage image) {
		return image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), (int[]) null);
	}

	public void assertPdfEquals(URL expected, InputStream actual) {

		File tempDir = null;
		try {
			tempDir = new File("/tmp/pdfassert");
			if (tempDir.exists())
				FileUtils.deleteDirectory(tempDir);
			tempDir.mkdirs();

			File actualFile = new File(tempDir, "actual.pdf");
			IOUtils.copy(actual, new FileOutputStream(actualFile));
			File expectedFile = new File(expected.toURI());
			IOUtils.copy(new FileInputStream(expectedFile), new FileOutputStream(new File(tempDir,
					"expected.pdf")));

			// TODO: code duplication
			// TODO: explicit /opt/local/bin/ under OS X required
			new ProcessBuilder("pdftk", "actual.pdf", "burst", "output", "actual_%02d.pdf")
					.directory(tempDir).start().waitFor();

			new ProcessBuilder("pdftk", "expected.pdf", "burst", "output", "expected_%02d.pdf")
					.directory(tempDir).start().waitFor();

			File[] actualFiles = tempDir.listFiles((FilenameFilter) new WildcardFileFilter(
					"actual_??.pdf"));
			File[] expectedFiles = tempDir.listFiles((FilenameFilter) new WildcardFileFilter(
					"expected_??.pdf"));

			Arrays.sort(actualFiles);
			Arrays.sort(expectedFiles);

			if (expectedFiles.length < 0)
				fail("No pages in expected PDF!");

			for (int i = 0; i < expectedFiles.length; i++) {
				File expectedPageFile = expectedFiles[i];
				if (actualFiles.length < i) {
					fail("Actual page has not enough pages!: " + actualFiles.length + " < "
							+ expectedPageFile.length());
				}

				File actualPageFile = actualFiles[i];

				File expectedPng = convertPdfToPng(expectedPageFile);
				File actualPng = convertPdfToPng(actualPageFile);

				// TODO: find a better way to do this
				File expectedSrcFile = new File(expectedFile.getAbsolutePath().replace(
						"/target/test-classes/", "/src/test/java/"));

				assertImageEquals(expectedSrcFile, actualFile, ImageIO.read(expectedPng), ImageIO
						.read(actualPng));
			}

			if (actualFiles.length != expectedFiles.length) {
				fail("Page count doesn't match, was: " + actualFiles.length + ", expected "
						+ expectedFiles.length);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static File convertPdfToPng(File pdfFile) {
		File destFile;
		try {
			String src = pdfFile.getAbsolutePath();
			String dest = src.replaceAll(".pdf$", ".png");
			// if (new ProcessBuilder("/usr/bin/sips", "--resampleWidth",
			// "1000", "-s", "format",
			// "png", src, "--out", dest).start().waitFor() != 0)
			// TODO: support sips and imagemagick
			// TODO: split might not be necessary when using imagemagick
			if (new ProcessBuilder("convert", "-density", "300", src, "-resize", "800x", dest)
					.start().waitFor() != 0)
				throw new RuntimeException("convert pdf2png returned error");
			destFile = new File(dest);
			if (!destFile.exists())
				throw new RuntimeException(dest + " not found");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return destFile;
	}

}
