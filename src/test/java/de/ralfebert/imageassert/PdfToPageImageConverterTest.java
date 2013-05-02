package de.ralfebert.imageassert;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.ralfebert.imageassert.compare.Page;
import de.ralfebert.imageassert.pageimage.IPdfImageSplitter;
import de.ralfebert.imageassert.pageimage.ImageMagickSplitter;
import de.ralfebert.imageassert.pageimage.PdfRendererImageSplitter;
import de.ralfebert.imageassert.utils.Colocated;
import de.ralfebert.imageassert.utils.TemporaryFolder;

@RunWith(Parameterized.class)
public class PdfToPageImageConverterTest {

	private TemporaryFolder tempFolder;
	private final IPdfImageSplitter converter;

	public PdfToPageImageConverterTest(IPdfImageSplitter converter) {
		super();
		this.converter = converter;
	}

	@Parameters
	public static List<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { new ImageMagickSplitter() },
				{ new PdfRendererImageSplitter() } });

	}

	@Before
	public void setup() {
		tempFolder = new TemporaryFolder(this);
	}

	@After
	public void teardown() {
		tempFolder.dispose();
	}

	@Test
	public void testConvert() throws FileNotFoundException, IOException {
		converter.setTemporaryFolder(tempFolder);
		File file = tempFolder.createFile("123.pdf");
		final FileOutputStream fos = new FileOutputStream(file);
		IOUtils.copy(Colocated.toStream(this, "123.pdf"), fos);
		IOUtils.closeQuietly(fos);
		Page[] pages = converter.convert(file);
		assertEquals("page count", 3, pages.length);
	}

}
