package exosoft.Zerfall;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Avatar extends Sprite {
	Rectangle bounds = new Rectangle();
	private Gun[] gunSwitcher = {new akfs(), new aug(), new drgv(), new fal(), new fms(), new hkg3()};
	private double xPos = 4500;
	private double yPos = 1240;
	private double yVel = 0;
	private int spriteNum = 0;
	private boolean collision[] = new boolean[5];
	private Gun currentGun = new Gun();

	public Avatar() {
		super(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
		currentGun = new akfs();
	}

	public int getxPos() {
		return (int) Math.round(xPos);
	}

	public void setxPos(double xPos) {
		if (xPos > 0 && xPos < map.getWidth()) {
			this.xPos = xPos;
		} else if (xPos < 0) {
			this.xPos = 0;
		} else if (xPos > map.getWidth() - 1) {
			this.xPos = map.getWidth() - 1;
		}
	}

	public int getyPos() {
		return (int) Math.round(yPos);
	}

	public void setyPos(double yPos) {
		if (yPos > -1 && yPos < map.getHeight()) {
			this.yPos = yPos;
		} else if (yPos < 0) {
			this.yPos = 0;
		} else if (yPos > map.getHeight() - 1) {
			this.yPos = map.getHeight() - 1;
		}
	}

	public Gun getCurrentGun() {
		return currentGun;
	}

	public void setCurrentGun(Gun currentGun) {
		this.currentGun = currentGun;
	}

	synchronized void logic() {
		int c;
		for (int i = 0; i < 5; i++)
			collision[i] = false;
		lowerLoop: for (int x = (int) (xPos + 25); x <= xPos + 150; x += 10) {
			for (int y = (int) (yPos + 161); y <= yPos + 162 + Math.abs(yVel); y += 10) {
				c = bitmap.getRGB((int) x / 10, (int) y / 10);
				switch (c) {
				case 0xFF000000:
					collision[1] = true;
					yPos = Math.round((y - 161) / 10) * 10;
					break lowerLoop;
				case 0xFF0000FF:
					collision[0] = true;
					break lowerLoop;
				}
			}
		}
		upperLoop: for (int x = (int) (xPos + 25); x <= xPos + 150; x += 10) {
			for (int y = (int) yPos; y >= yPos - 1 - Math.abs(yVel); y -= 10) {
				c = bitmap.getRGB((int) x / 10, (int) y / 10);
				switch (c) {
				case 0xFF000000:
					collision[2] = true;
					break upperLoop;
				}
			}
		}
		leftLoop: for (int x = (int) (xPos + 20); x <= xPos + 25; x += 10) {
			for (int y = (int) yPos; y <= yPos + 150; y += 10) {
				c = bitmap.getRGB((int) x / 10, (int) y / 10);
				switch (c) {
				case 0xFFFF0000:
					if (keys[KeyEvent.VK_E]) {
						openDoor(x, y);
					}
				case 0xFF000000:
					collision[3] = true;
					break leftLoop;
				}
			}
		}
		rightLoop: for (int x = (int) (xPos + 150); x <= xPos + 155; x += 10) {
			for (int y = (int) yPos; y <= yPos + 150; y += 10) {
				c = bitmap.getRGB((int) x / 10, (int) y / 10);
				switch (c) {
				case 0xFFFF0000:
					if (keys[KeyEvent.VK_E]) {
						openDoor(x, y);
					}
				case 0xFF000000:
					collision[4] = true;
					break rightLoop;
				}
			}
		}
		if (collision[1]) {
			yVel = 0;
		} else {
			yVel += .4;
		}
		if (keys[KeyEvent.VK_A]) {
			if (!collision[3]) {
				xPos -= 4;
			}
		}
		if (keys[KeyEvent.VK_D]) {
			if (!collision[4]) {
				xPos += 4;
			}
		}
		if (keys[KeyEvent.VK_W]) {
			if (collision[0]) {
				yVel = -4;
			} else if (collision[1]) {
				yVel -= 12;
			}
		}
		if (collision[2]) {
			yVel = Math.abs(yVel);
		}
		yPos += yVel;
	}

	synchronized void gunLogic() {
		if (keys[KeyEvent.VK_SPACE] && !currentGun.fullFire.isRunning()) {
			currentGun.fire();
		}
		if (keys[KeyEvent.VK_R] && !currentGun.reloadMag.isRunning()) {
			currentGun.reload();
		}
		if (keys[KeyEvent.VK_1] && !currentGun.reloadMag.isRunning() && !currentGun.reloadMag.isRunning()) {
			if (gunSwitcher[gunSwitcher.length - 2] == getCurrentGun()) {
				currentGun = gunSwitcher[0];
			} else {
				currentGun = gunSwitcher[1];
			}
		}
	}

	synchronized void visualLogic() {
		if (collision[1]) {
			spriteNum -= spriteNum % 2;
		}
		if (keys[KeyEvent.VK_D]) {
			switch (spriteNum) {
			case 0:
			case 1:
			case 4:
			case 5:
				spriteNum += 2;
				break;
			}
		}
		if (keys[KeyEvent.VK_A]) {
			switch (spriteNum) {
			case 2:
			case 3:
			case 6:
			case 7:
				spriteNum -= 2;
				break;
			}
		}
		if (keys[KeyEvent.VK_W] && collision[1]) {
			spriteNum += (spriteNum + 1) % 2;
		}
	}

	synchronized void openDoor(int x, int y) {
		x /= 10;
		y /= 10;
		int h = 0;
		boolean bool[] = new boolean[4];
		int doorColor = 0xFFFF0000;
		while (!(bool[1] && bool[3])) {
			if (!(bool[1] = (bitmap.getRGB(x, y - 1) != doorColor))) {
				y--;
			}
			if (!(bool[3] = (bitmap.getRGB(x, y + h) != doorColor))) {
				h++;
			}
		}
		Graphics2D foregroundGraphics = (Graphics2D) foreground.getGraphics();
		Graphics2D bitmapGraphics = (Graphics2D) bitmap.getGraphics();
		bitmapGraphics.setColor(Color.white);
		bitmapGraphics.fillRect(x, y, 1, h);
		bitmapGraphics.dispose();
		foregroundGraphics.setComposite(AlphaComposite.Clear);
		foregroundGraphics.fillRect(x * 10, y * 10, 11, h * 10 + 1);
		foregroundGraphics.dispose();
	}

	class akfs extends Gun {
		akfs() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "akfs"));
		}
	}

	class aug extends Gun {
		aug() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "aug"));
		}
	}

	class drgv extends Gun {
		drgv() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "drgv"));
		}
	}

	class fal extends Gun {
		fal() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "fal"));
		}
	}

	class fms extends Gun {
		fms() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "fms"));
		}
	}

	class hkg3 extends Gun {
		hkg3() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "hkg3"));
		}
	}
}
