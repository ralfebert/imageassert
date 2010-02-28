package de.ralfebert.imageassert;

import org.junit.Before;
import org.junit.Test;

import de.ralfebert.rcputils.lang.Colocated;

public class ImageAssertTest {

	private ImageAssert imageAssert;

	@Before
	public void setup() {
		imageAssert = new ImageAssert();
	}
	
	@Test
	public void testImageEqual() throws Exception {
		//TODO: toUrl vs. toStream
		//TODO: dependency Colocated
		imageAssert.assertPdfEquals(Colocated.toURL(this, "a.pdf"), Colocated.toStream(this, "a.pdf"));
		imageAssert.assertPdfEquals(Colocated.toURL(this, "b.pdf"), Colocated.toStream(this, "b.pdf"));
	}

	@Test
	public void testImageNotEqual() throws Exception {
		imageAssert.assertPdfEquals(Colocated.toURL(this, "a.pdf"), Colocated.toStream(this, "b.pdf"));
	}

}
