package de.ralfebert.imageassert.compare.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class ImageCompareDialog extends JDialog {

	private BufferedImage currentImage;
	private boolean applied = false;

	public ImageCompareDialog(BufferedImage expected, BufferedImage actual) {

		setTitle("1: expected, 2: Actual");

		// Create compatible images for fast scrolling
		final BufferedImage expectedImage = makeCompatible(expected);
		final BufferedImage actualImage = makeCompatible(actual);

		this.currentImage = expectedImage;

		final JComponent panel = new JComponent() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(currentImage, 0, 0, Color.WHITE, null);
			}
		};

		panel.setPreferredSize(new Dimension(expectedImage.getWidth(), expectedImage.getHeight()));
		JScrollPane scrollPane = new JScrollPane(panel);
		add(scrollPane);

		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				onApply();
				applied = true;
				setVisible(false);
			}

		});
		add(applyButton, BorderLayout.PAGE_END);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				if (!applied) {
					onReject();
				}
			}
		});

		setBackground(Color.WHITE);

		applyButton.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '1') {
					currentImage = expectedImage;
					ImageCompareDialog.this.repaint();
				}
				if (e.getKeyChar() == '2') {
					currentImage = actualImage;
					ImageCompareDialog.this.repaint();
				}
			}

		});
		setSize(expectedImage.getWidth() + 30, expectedImage.getHeight());
	}

	private BufferedImage makeCompatible(BufferedImage image) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();

		final BufferedImage compatibleImage = config.createCompatibleImage(image.getWidth(), image
				.getHeight(), Transparency.TRANSLUCENT);
		compatibleImage.getGraphics().drawImage(image, 0, 0, null);
		return compatibleImage;
	}

	protected void onApply() {

	}

	protected void onReject() {

	}

	public void open() {
		setModal(true);
		setVisible(true);
	}

}
