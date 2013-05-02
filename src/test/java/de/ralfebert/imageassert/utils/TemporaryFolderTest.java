package de.ralfebert.imageassert.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TemporaryFolderTest {

	@Test
	public void testCreateFile() throws Exception {
		TemporaryFolder folder = new TemporaryFolder(this);
		File file = folder.createFile("test.txt");
		assertNotNull(file);
		final FileOutputStream fos = new FileOutputStream(file);
		IOUtils.write("test", fos);
		IOUtils.closeQuietly(fos);
		assertTrue(file.exists());
		assertEquals("test", IOUtils.toString(new FileInputStream(file)));
		folder.dispose();
		assertFalse(file.exists());
	}

}
