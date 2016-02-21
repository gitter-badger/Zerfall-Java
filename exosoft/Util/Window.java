package exosoft.Util;

import java.awt.HeadlessException;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	/**
	 * @extends JFrame
	 * Custom JFrame that can hold a JPanel for drawing.
	 * Has all the features of a JFrame, with some customizations made.
	 */
	private static final long serialVersionUID = -8208101772064469647L;

	public Window(String title, int width, int height) throws HeadlessException {
		super(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public Window(int width, int height) {
		super();
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public Window(String title) {
	     super(title);
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setVisible(true);
	}

	public Window() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void addPanel(JPanel panel) {
		add(panel);
	}
	
	public void addKeyListener(KeyListener k) {
		super.addKeyListener(k);
	}
}