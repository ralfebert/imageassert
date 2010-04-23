package de.ralfebert.imageassert.compare.swt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.ralfebert.imageassert.compare.PageImage;

public class ImageCompareDialog {

	private boolean applied = false;
	private final Shell shell;
	private final Label image;
	private final Image expectedImage, actualImage;

	public ImageCompareDialog(final PageImage expected, final PageImage actual) {
		shell = new Shell();

		try {
			expectedImage = new Image(shell.getDisplay(), new FileInputStream(expected.getFile()));
			actualImage = new Image(shell.getDisplay(), new FileInputStream(actual.getFile()));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		shell.setText("1: expected, 2: Actual");
		shell.setLayout(new GridLayout(1, false));

		// TODO: why does it not scroll?
		ScrolledComposite scrollPane = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		image = new Label(scrollPane, SWT.NONE);
		image.setSize(expectedImage.getImageData().width, expectedImage.getImageData().height);
		image.setImage(expectedImage);

		scrollPane.setContent(image);

		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayout(new RowLayout());

		Button applyButton = new Button(buttons, SWT.NONE);
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				onApply();
				applied = true;
				shell.close();
			}

		});

		Button saveButton = new Button(buttons, SWT.NONE);
		saveButton.setText("Save Actual PDF");
		saveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				fileDialog.setFileName("expected.pdf");
				fileDialog.setFilterNames(new String[] { "PDF" });
				fileDialog.setFilterExtensions(new String[] { "*.pdf" });
				String destFileName = fileDialog.open();
				if (destFileName != null) {
					try {
						FileUtils.copyFile(actual.getPdfFile(), new File(destFileName));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}

		});

		shell.addShellListener(new ShellAdapter() {

			@Override
			public void shellClosed(ShellEvent e) {
				if (!applied) {
					onReject();
				}
			}
		});

		shell.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scrollPane.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		image.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		buttons.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		applyButton.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == '1') {
					image.setImage(expectedImage);
				}
				if (e.character == '2') {
					image.setImage(actualImage);
				}
			}

		});

		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				expectedImage.dispose();
				actualImage.dispose();
			}
		});

		shell.pack();
	}

	protected void onApply() {

	}

	protected void onReject() {

	}

	public void open() {
		shell.open();
		// block on open
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
