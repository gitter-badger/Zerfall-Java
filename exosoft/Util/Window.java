package exosoft.Util;

import java.awt.HeadlessException;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	/**
	 * @implements KeyListener, MouseListener
	 * @extends JFrame
	 * Custom JFrame that can hold a JPanel for drawing.
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

	public void useMouse(boolean status, MouseListener m) {
		if (status) {
			addMouseListener(m);
		} else {
			removeMouseListener(m);
		}
	}

	public void useKeys(boolean status, KeyListener k) {
		if (status) {
			addKeyListener(k);
		} else {
			removeKeyListener(k);
		}
	}
	
	public void addPanel(JPanel panel) {
		add(panel);
	}
}