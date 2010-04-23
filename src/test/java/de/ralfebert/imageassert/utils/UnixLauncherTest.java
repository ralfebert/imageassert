package de.ralfebert.imageassert.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnixLauncherTest {

	private UnixLauncher launcher;

	@Before
	public void setup() {
		launcher = new UnixLauncher();
	}

	@Test
	public void testEcho() throws Exception {
		assertEquals("x\n", launcher.launch("echo", "x"));
	}

	@Test
	public void testError() throws Exception {
		try {
			launcher.launch("cat", "doesntexist");
			fail("Expected LaunchException");
		} catch (LaunchException e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), e.getMessage().contains("doesntexist"));
			assertTrue(e.getMessage(), e.getErrors().contains("doesntexist"));
			assertTrue(e.getExitValue() != 0);
		}
	}

}
