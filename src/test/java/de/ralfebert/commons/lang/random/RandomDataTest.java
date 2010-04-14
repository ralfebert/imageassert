package de.ralfebert.commons.lang.random;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RandomDataTest {

	private RandomData r1;
	private RandomData r2;

	@Before
	public void setUp() throws Exception {
		r1 = new RandomData(1);
		r2 = new RandomData();
	}

	@Test
	public void testStaticContents() {
		String name = r1.somePersonName();
		assertEquals(name, r1.somePersonName());
		assertFalse(name.equals(r2.somePersonName()));
		r1.newData();
		assertFalse(name.equals(r1.somePersonName()));
	}

	@Test
	public void testSomeDigits() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 100; j++) {
				String digits = r1.someDigits(j);
				assertTrue(digits, digits.matches("\\d{" + j + "}"));
				r1.newData();
			}
		}
	}

	@Test
	public void testSomeNumber() {
		for (int j = 0; j < 1000; j++) {
			int number = r1.someNumber(50, 60);
			assertTrue(String.valueOf(number), number >= 50 && number < 60);
		}
	}

}
