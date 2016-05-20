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
	static boolean drawFPS;
	static double framerate;

	public static void main(String[] uselessbullshit) {
		TinySound.init();
		initiateGame();
		gameWorld.addObject(
				new Object(new Point(250, 200), new Point(720, 200), new Point(720, 225), new Point(250, 225)));
		gameWorld.addObject(
				new Object(new Point(50, 600), new Point(1080, 600), new Point(720, 625), new Point(250, 625)));
		gameWorld.spawnEntity(player = new Character(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161, keywatch), 0, 0);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initiateWindow();
				initiateConsole();
				window.revalidate();
				sheet.revalidate();
			}
		});
	}
}
