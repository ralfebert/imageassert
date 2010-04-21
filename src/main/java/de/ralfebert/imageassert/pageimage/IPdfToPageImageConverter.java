package de.ralfebert.imageassert.pageimage;

import java.io.File;

import de.ralfebert.commons.lang.temp.TemporaryFolder;
import de.ralfebert.imageassert.compare.PageImage;

public interface IPdfToPageImageConverter {

	public void setTemporaryFolder(TemporaryFolder temporaryFolder);

	public abstract PageImage[] convert(File pdf);

}