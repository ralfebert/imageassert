package de.ralfebert.imageassert;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ralfebert.commons.lang.colocated.Colocated;
import de.ralfebert.commons.lang.temp.TemporaryFolder;
import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.utils.Pdf2Png;

public class Pdf2PngTest {

	private TemporaryFolder tempFolder;

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
		Pdf2Png pdf2Png = new Pdf2Png(tempFolder);
		File file = tempFolder.createFile("123.pdf");
		IOUtils.copy(Colocated.toStream(this, "123.pdf"), new FileOutputStream(file));
		PageImage[] pages = pdf2Png.convert(file);
		assertEquals("page count", 3, pages.length);
	}

}
