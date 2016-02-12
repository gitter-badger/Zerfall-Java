package exosoft.Zerfall;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements KeyListener, MouseListener {
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

	public void useMouse(boolean status) {
		if (status) {
			addMouseListener(this);
		} else {
			removeMouseListener(this);
		}
	}

	public void useKeys(boolean status) {
		if (status) {
			addKeyListener(this);
		} else {
			removeKeyListener(this);
		}
	}
	
	public void addPanel(JPanel panel) {
		add(panel);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
          // TODO: Write mouseclick logic
	}

	@Override
	public void mousePressed(MouseEvent e) {
          // TODO: Write mousepress logic
	}

	@Override
	public void mouseReleased(MouseEvent e) {
          // TODO: Write mouserelease logic
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO: Write mouseenter logic

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO: Write mouseexit logic

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			Main.keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			Main.keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			Main.keys[e.getKeyCode()] = false;
		}
	}
}