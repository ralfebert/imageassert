package de.ralfebert.imageassert.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ColocatedTest {

	private static final String TEST = "test.txt";
	private static final String TEST_CONTENTS = "abc";
	private static final String DOESNT_EXIST = "xxx";

	@Test
	public void testToString() {
		assertEquals(TEST_CONTENTS, Colocated.toString(this, TEST));
	}

	@Test
	public void testToStream() throws IOException {
		InputStream stream = Colocated.toStream(this, TEST);
		assertEquals(TEST_CONTENTS, IOUtils.toString(stream));
	}

	@Test
	public void testToURL() throws IOException {
		URL url = Colocated.toURL(this, TEST);
		assertEquals(TEST_CONTENTS, IOUtils.toString(url.openStream()));
	}

	@Test(expected = RuntimeIOException.class)
	public void testToStringError() {
		Colocated.toString(this, DOESNT_EXIST);
	}

	@Test(expected = RuntimeIOException.class)
	public void testToStreamError() {
		Colocated.toStream(this, DOESNT_EXIST);
	}

	@Test(expected = RuntimeIOException.class)
	public void testToURLError() {
		Colocated.toURL(this, DOESNT_EXIST);
	}

}