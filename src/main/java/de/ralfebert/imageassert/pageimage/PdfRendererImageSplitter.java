package de.ralfebert.imageassert.pageimage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import de.ralfebert.imageassert.compare.Page;
import de.ralfebert.imageassert.utils.RuntimeIOException;
import de.ralfebert.imageassert.utils.TemporaryFolder;

/**
 * Renders images for PDF using PDFRenderer.
 * 
 * @see http://java.net/projects/pdf-renderer/
 * 
 * @author Jörg Steeg
 * @author Ralf Ebert
 */
public class PdfRendererImageSplitter implements IPdfImageSplitter {

	public Page[] convert(File pdf) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(pdf, "r");
			FileChannel channel = raf.getChannel();
			// NOTE! ByteBuffer will keep file from being deleted on Windows!
			ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdffile = new PDFFile(buffer);

			Page[] pages = new Page[pdffile.getNumPages()];

			for (int i = 0; i < pdffile.getNumPages(); i++) {
				PDFPage page = pdffile.getPage(i);

				Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page
						.getBBox().getHeight());

				Image img = page.getImage(rect.width, rect.height, rect, null, true, true);

				BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
						img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics2d = bufferedImage.createGraphics();
				graphics2d.drawImage(img, 0, 0, null);

				pages[i] = new Page(bufferedImage, pdf.getName(), pdf);
			}

			return pages;
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		finally {
			if(raf != null) {
				try {
					raf.close(); // Closes channel too
				}
				catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			}
		}
	}

	public void setTemporaryFolder(TemporaryFolder temporaryFolder) {
	}
}
