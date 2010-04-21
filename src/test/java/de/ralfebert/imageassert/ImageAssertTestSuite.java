package de.ralfebert.imageassert;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { PdfToPageImageConverterTest.class, ImageAssertTest.class})
public class ImageAssertTestSuite {

}
