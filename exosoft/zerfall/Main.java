package exosoft.zerfall;

import java.awt.EventQueue;
import java.awt.Point;

import exosoft.iso.Framework;
import exosoft.iso.Object;
import exosoft.iso.Sprite.SheetType;
import exosoft.zerfall.Character;
import kuusisto.tinysound.TinySound;

public class Main extends Framework {

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
				initiateThreads();
			}
		});
	}
}
