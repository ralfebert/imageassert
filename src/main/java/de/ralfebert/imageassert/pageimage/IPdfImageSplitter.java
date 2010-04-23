package de.ralfebert.imageassert.pageimage;

import java.io.File;

import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.utils.TemporaryFolder;

/**
 * Implementations of IPdfImageSplitter are responsible to split a multi-paged
 * PDF into separate image files represented by {@link PageImage}s.
 * 
 * @author Ralf Ebert
 */
public interface IPdfImageSplitter {

	public void setTemporaryFolder(TemporaryFolder temporaryFolder);

	public abstract PageImage[] convert(File pdf);

}