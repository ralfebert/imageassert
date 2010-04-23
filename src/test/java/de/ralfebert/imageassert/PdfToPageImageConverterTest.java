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

import de.ralfebert.commons.lang.io.Colocated;
import de.ralfebert.commons.lang.temp.TemporaryFolder;
import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.pageimage.IPdfToPageImageConverter;
import de.ralfebert.imageassert.pageimage.ImageMagickConverter;
import de.ralfebert.imageassert.pageimage.XpdfConverter;

@RunWith(Parameterized.class)
public class PdfToPageImageConverterTest {

	private TemporaryFolder tempFolder;
	private final IPdfToPageImageConverter converter;

	public PdfToPageImageConverterTest(IPdfToPageImageConverter converter) {
		super();
		this.converter = converter;
	}

	@Parameters
	public static List<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { new ImageMagickConverter() },
				{ new XpdfConverter() } });

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
		IOUtils.copy(Colocated.toStream(this, "123.pdf"), new FileOutputStream(file));
		PageImage[] pages = converter.convert(file);
		assertEquals("page count", 3, pages.length);
	}

}
