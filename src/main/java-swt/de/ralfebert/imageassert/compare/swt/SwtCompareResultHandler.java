package de.ralfebert.imageassert.compare.swt;

import static org.junit.Assert.fail;
import de.ralfebert.imageassert.compare.ICompareResultHandler;
import de.ralfebert.imageassert.compare.PageImage;

public class SwtCompareResultHandler implements ICompareResultHandler {

	public void onImageNotEqual(final PageImage expected, final PageImage actual) {
		ImageCompareDialog imageCompareDialog = new ImageCompareDialog(expected, actual) {

			@Override
			protected void onApply() {
				// todo: try to update file in src folder
			}

			@Override
			protected void onReject() {
				fail("Expected: " + expected.getName());
			}

		};
		imageCompareDialog.open();
	}

}
