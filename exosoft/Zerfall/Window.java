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
	 * 
	 */
	private static final long serialVersionUID = -8208101772064469647L;

	public Window(String title, int width, int height, boolean resizable) throws HeadlessException {
		super(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(Main.sheet);
		setVisible(true);
		setResizable(resizable);
	}

	public Window(String title, int width, int height) throws HeadlessException {
		super(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(Main.sheet);
		setVisible(true);
		setResizable(false);
	}

	public Window(int width, int height) {
		super();
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(Main.sheet);
		setVisible(true);
		setResizable(false);
		addKeyListener(this);
		addMouseListener(this);
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

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

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