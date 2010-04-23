package de.ralfebert.imageassert.pageimage;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import de.ralfebert.imageassert.compare.PageImage;
import de.ralfebert.imageassert.utils.TemporaryFolder;
import de.ralfebert.imageassert.utils.UnixLauncher;

/**
 * This converter uses pdftoppm to convert PDF to PPM and uses ImageMagick to
 * convert PPM to PNG. Written because ImageMagick uses ghostscript to convert
 * PDF to image and ghostscript had a bug:
 * http://bugs.ghostscript.com/show_bug.cgi?id=691253
 * 
 * @author Ralf Ebert
 */
public class XpdfSplitter implements IPdfImageSplitter {

	private TemporaryFolder temporaryFolder;
	private final UnixLauncher launcher = new UnixLauncher();

	@Override
	public void setTemporaryFolder(TemporaryFolder temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
	}

	@Override
	public PageImage[] convert(File pdf) {
		String src = pdf.getAbsolutePath();
		String dest = src.replaceAll(".pdf$", "");

		ProcessBuilder convertProcess = new ProcessBuilder("pdftoppm", src, dest);
		launcher.launch(convertProcess);

		String wildcard = FilenameUtils.getBaseName(pdf.getAbsolutePath()) + "*.ppm";
		File[] images = temporaryFolder.getFolder().listFiles(
				(FilenameFilter) new WildcardFileFilter(wildcard));

		for (int i = 0; i < images.length; i++) {
			String ppmPath = images[i].getAbsolutePath();
			String pngPath = ppmPath.replaceAll(".ppm", ".png");
			convertProcess = new ProcessBuilder("convert", ppmPath, "-resize", "700x", pngPath);
			launcher.launch(convertProcess);
			images[i] = new File(pngPath);
		}

		Arrays.sort(images);

		PageImage[] pages = new PageImage[images.length];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = new PageImage(images[i], pdf);
		}

		return pages;
	}
}