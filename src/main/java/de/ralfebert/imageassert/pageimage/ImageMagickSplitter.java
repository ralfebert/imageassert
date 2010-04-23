package de.ralfebert.imageassert.pageimage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.utils.TemporaryFolder;
import de.ralfebert.imageassert.utils.UnixLauncher;

public class ImageMagickSplitter implements IPdfImageSplitter {

	private TemporaryFolder temporaryFolder;
	private final UnixLauncher launcher = new UnixLauncher();
	private int dpi = 150;

	@Override
	public void setTemporaryFolder(TemporaryFolder temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
	}

	@Override
	public PageImage[] convert(File pdf) {
		String src = pdf.getAbsolutePath();
		String dest = src.replaceAll(".pdf$", ".png");

		ProcessBuilder convertProcess = new ProcessBuilder("convert", "-density", String
				.valueOf(dpi), src, "-resize", "700x", dest);
		launcher.launch(convertProcess);

		String wildcard = FilenameUtils.getBaseName(pdf.getAbsolutePath()) + "*.png";
		File[] pngFiles = temporaryFolder.getFolder().listFiles(
				(FilenameFilter) new WildcardFileFilter(wildcard));
		Arrays.sort(pngFiles);

		PageImage[] pages = new PageImage[pngFiles.length];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new PageImage(pngFiles[i], pdf);
		}

		return pages;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

}