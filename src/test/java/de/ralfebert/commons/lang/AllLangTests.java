package de.ralfebert.commons.lang;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.ralfebert.commons.lang.io.ColocatedTest;
import de.ralfebert.commons.lang.launch.UnixLauncherTest;
import de.ralfebert.commons.lang.temp.TemporaryFolderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ColocatedTest.class, UnixLauncherTest.class, TemporaryFolderTest.class })
public class AllLangTests {

}
