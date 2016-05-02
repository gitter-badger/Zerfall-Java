package exosoft.zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import exosoft.iso.Avatar;
import exosoft.iso.Controllable;
import exosoft.iso.Environment;
import exosoft.iso.Sprite.SheetType;
import exosoft.util.Window;
import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
public class Main {
	final static int logicRate = 120;
	final static int drawRate = 60;
	static BufferedImage background;
	static BufferedImage foreground;
	static BufferedImage bitmap;
	static Sheet sheet;
	static Character player;
	static volatile Timer drawTimer;
	static volatile Timer logicTimer;
	private static boolean[] keys = new boolean[512];
	static Environment map;

	public static void main(String[] args) {
		TinySound.init();
		player = new Character(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
		player.setX(0);
		player.setY(0);
		player.setVelocity(0);
		map = new Environment();
		player.spawn(map);
		map.addShape(new Rectangle(0, 600, 1920, 700));
		sheet = new Sheet();
		try {
			background = ImageIO.read(new File("resources/maps/background.png"));
			bitmap = ImageIO.read(new File("resources/maps/bitmap.png"));
			foreground = ImageIO.read(new File("resources/maps/foreground.png"));
		} catch (IOException e) {
			System.err.println("One of the images did not load correctly. See below for stack trace.");
			e.printStackTrace();
		}

		drawTimer = new Timer(1000 / drawRate, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				player.visual();
				sheet.repaint();
			}
		});
		logicTimer = new Timer(1000 / logicRate, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.movement();
				player.physics();
				player.collision();
			}
		});
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Window window = new Window("Zerfall", 1280, 720);
				window.add(sheet);
				window.setFocusable(true);
				window.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						setKey(e.getKeyCode(), true);
					}

					@Override
					public void keyPressed(KeyEvent e) {
						setKey(e.getKeyCode(), true);
					}

					@Override
					public void keyReleased(KeyEvent e) {
						setKey(e.getKeyCode(), false);
					}

				});
			}
		});
		logicTimer.start();
		drawTimer.start();
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(player.getSprite(player.getSpriteNum()), player.getIntX(), player.getIntY(),
					null);
			g.setColor(Color.red);
			g.draw(player.getBounds2D());
			g.setColor(Color.blue);
			for (Shape object : map.getObjects()) {
				g.draw(object);
			}
			g.dispose();
		}
	}
	
	static class Character extends Avatar implements Controllable {

		public Character(SheetType type, String sheetPath, int spriteWidth, int spriteHeight) {
			super(type, sheetPath, spriteWidth, spriteHeight);
		}
		public void movement() {
			if (getKey(KeyEvent.VK_W)) {
				moveUp(1);
			}
			if (getKey(KeyEvent.VK_D)) {
				moveRight(1);
			}
			if (getKey(KeyEvent.VK_A)) {
				moveLeft(1);
			}
		}

		@Override
		public void moveLeft(double multiplier) {
			setX(getX() - 5);
		}

		@Override
		public void moveRight(double multiplier) {
			setX(getX() + 5);
		}

		@Override
		public void moveUp(double multiplier) {
			if (atRest) {
				velocity = -10;
				atRest = false;
			}
		}

		@Override
		public void moveDown(double multiplier) {
			// TODO Auto-generated method stub

		}
	}

	public static boolean getKey(int index) {
		return keys[index];
	}

	public static void setKey(int index, boolean status) {
		keys[index] = status;
	}

}