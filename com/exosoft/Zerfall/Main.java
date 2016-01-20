package com.exosoft.Zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
class Main {
	static int logicRate = 120, drawRate = 60;
	static boolean keys[] = new boolean[256];
	static BufferedImage map = null, foreground = null, bitmap = null;
	static Sheet sheet = new Sheet();
	static Avatar player = new Avatar();
	static Timer drawTimer, logicTimer;

	public static void main(String[] args) {
		drawTimer = new Timer(1000 / drawRate, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sheet.repaint();
			}
		});
		logicTimer = new Timer(1000 / logicRate, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.logic();
			}
		});
		try {
			map = ImageIO.read(new File("resources/maps/background.png"));
			bitmap = ImageIO.read(new File("resources/maps/bitmap.png"));
			foreground = ImageIO.read(new File("resources/maps/foreground.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Window();
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
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.translate((int) -(player.xPos + player.sprites[0].getWidth() / 2 - getWidth() / 2),
					(int) -(player.yPos + player.sprites[0].getHeight() / 2 - getHeight() / 2));
			g.drawImage(map, 0, 0, map.getWidth(), map.getHeight(), null);
			g.drawImage(player.sprites[player.spriteNum], (int) player.xPos, (int) player.yPos, null);
			g.drawImage(foreground, 0, 0, null);
			g.dispose();
		}
	}
}
