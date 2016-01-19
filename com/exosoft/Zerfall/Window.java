package com.exosoft.Zerfall;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class Window extends JFrame implements KeyListener, MouseListener {
	public Window() throws HeadlessException {
		super("Zerfall");
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(Main.sheet);
		setVisible(true);
		setResizable(false);
		addKeyListener(this);
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
