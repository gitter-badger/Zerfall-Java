package exosoft.zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import exosoft.iso.Environment;
import exosoft.iso.Framework;
import exosoft.iso.KeyObserver;
import exosoft.iso.Object;
import exosoft.iso.Sprite.SheetType;
import exosoft.util.Window;
import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
public class Main extends Framework {
	static Character player;
	static Timer metaHandler;
	static JTextField console;

	public static void main(String[] uselessbullshit) {
		TinySound.init();
		gameFrequency = 120;
		drawRate = 60;
		sheet = new Sheet();
		keywatch = new KeyObserver();
		gameWorld = new Environment();
		player = new Character(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161, keywatch);
		player.setLocation(0, 0);
		player.setVelocity(0);
		gameWorld.addObject(
				new Object(new Point(250, 200), new Point(720, 200), new Point(720, 225), new Point(250, 225)));
		gameWorld.addObject(
				new Object(new Point(50, 600), new Point(1080, 600), new Point(720, 625), new Point(250, 625)));
		gameWorld.spawnEntity(player, 0, 0);
		console = new JTextField(10);
		console.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (console.getText() != null) {
					readConsoleInput(console.getText());
				}
			}
			
		});

		metaHandler = new Timer(1000 / 120, new ActionListener() {
			boolean pauseAvailable;
			boolean consoleAvailable;

			@Override
			public void actionPerformed(ActionEvent event) {
				detectPauseAction();
				detectConsoleAction();
			}
			
			private void detectConsoleAction() {
				if (keywatch.getKey(KeyEvent.VK_BACK_QUOTE) && !consoleAvailable) {
					consoleAvailable = true;
				} else if (!keywatch.getKey(KeyEvent.VK_BACK_QUOTE) && consoleAvailable) {
					consoleAvailable = false;
				}
				if (consoleAvailable) {
					detectTheFuckingConsoleAction();
				}
			}

			public void detectPauseAction() {
				if (keywatch.getKey(KeyEvent.VK_ESCAPE) && !pauseAvailable) {
					pauseAvailable = true;
				} else if (!keywatch.getKey(KeyEvent.VK_ESCAPE) && pauseAvailable) {
					pauseAvailable = false;
				}
				if (pauseAvailable) {
					detectTheFuckingPauseAction();
				}
			}
		});

		visualHandler = new Timer(1000 / drawRate, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.visual();
				sheet.repaint();
			}
		});

		gameHandler = new Timer(1000 / gameFrequency, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.movement();
				player.physics();
				gameWorld.execute();
			}
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Window window = new Window("Zerfall", 1280, 720);
				window.add(sheet);
				sheet.add(console);
				console.setSize(1260, 50);
				console.setVisible(false);
				console.setLocation(10, 10);
				window.setFocusable(true);
				window.addKeyListener(keywatch);
			}
		});
		metaHandler.start();
		gameHandler.start();
		visualHandler.start();
	}

	protected static void detectTheFuckingPauseAction() {
		if (keywatch.getKey(KeyEvent.VK_ESCAPE)) {
			if (gameHandler.isRunning()) {
				gameHandler.stop();
			} else {
				gameHandler.start();
			}
		}
	}
	
	protected static void detectTheFuckingConsoleAction() {
		if (keywatch.getKey(KeyEvent.VK_BACK_QUOTE)) {
			if (console.isVisible()) {
				console.setVisible(false);
			} else {
				console.setVisible(true);
				console.requestFocus();
			}
		}
	}
	
	private static void readConsoleInput(String data) {
		String[] splitData = data.split(" ");
		if (splitData.length == 2) {
			switch (splitData[0]) {
			case "dev-mode":
				switch (splitData[1]) {
				case "on":
					gameWorld.setDevmode(true);
					break;
				case "off":
					gameWorld.setDevmode(false);
					break;
				case "toggle":
					gameWorld.setDevmode(!gameWorld.isDevmode());
					break;
				default:
					System.err.println("Incorrect modifier. List of modifiers: {on, off, toggle}");
				}
				break;
			}
		} else if (splitData.length > 2) {
			System.err.println("Too many arguments given. Maximum of two arguments.");
		} else if (splitData.length < 2) {
			System.err.println("Not enough arguments given. Please specify a command and a modifier");
		}
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(player.getSprite(player.getSpriteNum()), player.getIntX(), player.getIntY(), null);
			g.setColor(Color.red);
			g.draw(player.getBounds());
			g.setColor(Color.blue);
			g = gameWorld.drawWorld(g);
			g.dispose();
		}
	}

}