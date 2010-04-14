package de.ralfebert.commons.lang.string;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ralfebert.commons.lang.string.StringNormalizer;

public class StringNormalizerTest {

	@Test
	public void testEmpty() {
		assertEquals("", new StringNormalizer("").toString());
	}

	@Test
	public void testNoOp() {
		assertEquals("a\nb", new StringNormalizer("a\nb").toString());
	}
	
	@Test
	public void testRemoveBlankLines() {
		StringNormalizer stringNormalizer = new StringNormalizer("a \n   \n b\n\t\nc\n\nd\n\ne");
		stringNormalizer.removeBlankLines();
		assertEquals("a \n\n b\n\nc\n\nd\n\ne", stringNormalizer.toString());
	}

	@Test
	public void testRemoveDuplicateEmptyLines() {
		StringNormalizer stringNormalizer = new StringNormalizer("\na\n\nb\n\n\nc\n");
		stringNormalizer.removeDuplicateEmptyLines();
		assertEquals("\na\n\nb\n\nc\n", stringNormalizer.toString());
	}

}
