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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import javax.swing.JPanel;
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

		metaHandler = new Timer(1000 / 120, new ActionListener() {
			boolean pauseAvailable;

			@Override
			public void actionPerformed(ActionEvent event) {
				detectPauseAction();
				Scanner scanInput = new Scanner(System.in);
				String data = scanInput.nextLine();
				String[] splitData = data.split(".");
				scanInput.close();
				java.lang.Object instance = null;
				try {
					instance = getClass().getDeclaredField(splitData[0]).get(this);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Method method = null;
				try {
					method = instance.getClass().getMethod(splitData[1].split("(")[0]);
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					method.invoke(splitData[1].split("(")[0]);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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