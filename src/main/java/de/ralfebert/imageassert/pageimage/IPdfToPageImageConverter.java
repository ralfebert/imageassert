package de.ralfebert.imageassert.pageimage;

import java.io.File;

import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.utils.TemporaryFolder;

public interface IPdfToPageImageConverter {

	public void setTemporaryFolder(TemporaryFolder temporaryFolder);

	public abstract PageImage[] convert(File pdf);

}