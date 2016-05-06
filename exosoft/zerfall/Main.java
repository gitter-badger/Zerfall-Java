package exosoft.zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

import exosoft.iso.Environment;
import exosoft.iso.Framework;
import exosoft.iso.Sprite.SheetType;
import exosoft.util.KeyObserver;
import exosoft.util.Window;
import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
public class Main extends Framework {
	static Sheet sheet;
	static Character player;
	static Environment map;
	static KeyListener keywatch;

	public static void main(String[] uselessbullshit) {
		TinySound.init();
		logicRate = 120;
		drawRate = 60;
		sheet = new Sheet();
		keywatch = new KeyObserver();
		map = new Environment();
		player = new Character(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161, keywatch);
		player.setLocation(0, 0);
		player.setVelocity(0);
		map.addObject(new Rectangle(0, 600, 720, 200));
		map.spawnEntity(player);

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
				window.addKeyListener(keywatch);
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
			g.draw(player.getBounds());
			g.setColor(Color.blue);
			for (Shape object : map.getObjects()) {
				g.draw(object);
			}
			g.dispose();
		}
	}

}