package de.ralfebert.imageassert.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import de.ralfebert.commons.lang.io.RuntimeIOException;
import de.ralfebert.commons.lang.launch.UnixLauncher;
import de.ralfebert.commons.lang.temp.TemporaryFolder;
import de.ralfebert.imageassert.compare.PageImage;

public class Pdf2Png {

	private final TemporaryFolder temporaryFolder;
	private final UnixLauncher launcher = new UnixLauncher();
	private int dpi = 150;

	public Pdf2Png(TemporaryFolder temporaryFolder) {
		super();
		this.temporaryFolder = temporaryFolder;
	}

	public PageImage[] convert(File pdf) {
		if (!pdf.exists()) {
			throw new RuntimeIOException(pdf + " doesn't exist, cannot convert to png");
		}

		String src = pdf.getAbsolutePath();
		String dest = src.replaceAll(".pdf$", ".png");

		ProcessBuilder convertProcess = new ProcessBuilder("/usr/local/bin/convert", "-density",
				String.valueOf(dpi), src, "-resize", "700x", dest);
		// TODO: IMGASSERT-1: Workaround für OS X Environment entfernen
		Map<String, String> env = convertProcess.environment();
		env.put("PATH", env.get("PATH") + ":/usr/local/bin/");
		launcher.launch(convertProcess);

		String wildcard = FilenameUtils.getBaseName(pdf.getAbsolutePath()) + "*.png";
		File[] pngFiles = temporaryFolder.getFolder().listFiles(
				(FilenameFilter) new WildcardFileFilter(wildcard));
		Arrays.sort(pngFiles);

		PageImage[] pages = new PageImage[pngFiles.length];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new PageImage(pngFiles[i]);
		}

		return pages;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

}