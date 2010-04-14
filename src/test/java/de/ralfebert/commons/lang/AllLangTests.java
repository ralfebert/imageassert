package de.ralfebert.commons.lang;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.ralfebert.commons.lang.colocated.ColocatedTest;
import de.ralfebert.commons.lang.date.DateHelperTest;
import de.ralfebert.commons.lang.launch.UnixLauncherTest;
import de.ralfebert.commons.lang.random.RandomDataTest;
import de.ralfebert.commons.lang.string.StringNormalizerTest;
import de.ralfebert.commons.lang.temp.TemporaryFolderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ StringNormalizerTest.class, ColocatedTest.class, DateHelperTest.class, UnixLauncherTest.class,
		TemporaryFolderTest.class, RandomDataTest.class })
public class AllLangTests {

}
