package exosoft.zerfall;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
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
	static JTextField consoleInput;
	static JPanel console;
	static Window window;

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
		consoleInput = new JTextField(10);
		consoleInput.setBackground(Color.gray);
		consoleInput.setSelectedTextColor(Color.white);
		consoleInput.setVisible(true);
		consoleInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (consoleInput.getText() != null) {
					readConsoleInput(consoleInput.getText());
				}
			}

		});

		metaHandler = new Timer(1000 / 120, new ActionListener() {
			boolean pauseActionAvailable;
			boolean consoleActionAvailable = true;

			@Override
			public void actionPerformed(ActionEvent event) {
				detectPauseAction();
				detectConsoleAction();
			}

			private synchronized void detectConsoleAction() {
				if (keywatch.getKey(KeyEvent.VK_BACK_QUOTE) && consoleActionAvailable) {
					consoleInput.setVisible(!consoleInput.isVisible());
					consoleInput.requestFocusInWindow();
					window.revalidate();
				}
				consoleActionAvailable = !keywatch.getKey(KeyEvent.VK_BACK_QUOTE);
			}

			public void detectPauseAction() {
				if (keywatch.getKey(KeyEvent.VK_ESCAPE) && pauseActionAvailable) {
					if (gameHandler.isRunning()) {
						gameHandler.stop();
					} else {
						gameHandler.start();
					}
				}
				pauseActionAvailable = !keywatch.getKey(KeyEvent.VK_ESCAPE);
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
				consoleInput.setHorizontalAlignment(JTextField.LEFT);
				consoleInput.setBounds(0, 0, 1280, 50);
				console = new JPanel();
				sheet.add(console);
				console.setLayout(new GridBagLayout());
				console.add(consoleInput);
				console.setOpaque(false);
				consoleInput.setVisible(false);
				window = new Window("Zerfall", 1280, 720);
				window.add(sheet);
				sheet.setLayout(new CardLayout());
				window.setComponentZOrder(sheet, 0);
				sheet.setSize(1280, 720);
				sheet.setMinimumSize(new Dimension(1280, 720));
				sheet.setVisible(true);
				window.setFocusable(true);
				window.addKeyListener(keywatch);
				consoleInput.addKeyListener(keywatch);
				window.revalidate();
				sheet.revalidate();
			}
		});
		metaHandler.start();
		gameHandler.start();
		visualHandler.start();
	}

	private static void readConsoleInput(String data) {
		String[] splitData = data.split(" ");
		if (splitData.length == 2) {
			switch (splitData[0]) {
			case "dev-mode":
				switch (splitData[1]) {
				case "on":
					gameWorld.setDevmode(true);
					System.out.println("devmode on");
					break;
				case "off":
					gameWorld.setDevmode(false);
					System.out.println("devmode off");
					break;
				case "toggle":
					gameWorld.setDevmode(!gameWorld.isDevmode());
					System.out.println("devmode toggled");
					break;
				default:
					System.err.println("Incorrect modifier. List of modifiers: {on, off, toggle}");
				}
				break;
			default:
				System.err.println("Command unrecognized");
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
			g = gameWorld.drawWorld(g);
			// g.dispose();
		}
	}

}