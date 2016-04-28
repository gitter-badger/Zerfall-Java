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
	static Avatar player;
	static volatile Timer drawTimer;
	static volatile Timer logicTimer;
	private static boolean[] keys = new boolean[512];
	static Environment map = new Environment();

	public static void main(String[] args) {
		TinySound.init();
		player = new Avatar(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
		player.setxPosition(0);
		player.setyPosition(0);
		player.setyVelocity(0);
		player.spawn(map);
		map.addShape(new Rectangle(0, 600, 1920, 700));
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
				player.collision();
				player.physics();
			}
		});
		sheet = new Sheet();
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
			g.drawImage(player.getSprite(player.getSpriteNum()), player.getIntxPosition(), player.getIntyPosition(),
					null);
			g.setColor(Color.red);
			g.draw(player.getBounds());
			g.setColor(Color.blue);
			for (Shape object : map.getObjects()) {
				g.draw(object);
			}
			if (player.getSpriteNum() > 3) {
				player.setSpriteNum(player.getSpriteNum() - 4);
			}
			g.dispose();
		}
	}

	public static boolean getKey(int index) {
		return keys[index];
	}

	public static void setKey(int index, boolean status) {
		keys[index] = status;
	}

}