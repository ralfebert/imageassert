package de.ralfebert.imageassert;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.ralfebert.imageassert.utils.Colocated;

public class ImageAssertTest {

	private ImageAssert imageAssert;

	@Before
	public void setup() {
		imageAssert = new ImageAssert(false);
	}

	@Test
	public void testEqualPages() throws Exception {
		check("a.pdf", "a.pdf");
		check("123.pdf", "123.pdf");
	}

	@Test(expected = AssertionError.class)
	public void testDifferentPage() throws Exception {
		check("a.pdf", "1.pdf");
	}

	@Test(expected = AssertionError.class)
	public void testDifferentPages() throws Exception {
		check("123.pdf", "1a3.pdf");
	}

	@Test(expected = AssertionError.class)
	public void testPagesSwapped() throws Exception {
		check("123.pdf", "132.pdf");
	}

	@Test
	public void testNotEnoughPages() throws Exception {
		try {
			check("1.pdf", "1a3.pdf");
			fail("expected AssertionError");
		} catch (AssertionError e) {
			assertTrue(e.getMessage(), e.getMessage().contains("too many pages"));
		}
	}

	@Test
	public void testTooManyPages() throws Exception {
		try {
			check("1a3.pdf", "1.pdf");
			fail("expected AssertionError");
		} catch (AssertionError e) {
			assertTrue(e.getMessage(), e.getMessage().contains("not enough pages"));
		}
	}

	private void check(String expected, String actual) {
		imageAssert.assertPdfEquals(Colocated.toStream(this, expected), Colocated.toStream(this,
				actual));
	}

}
