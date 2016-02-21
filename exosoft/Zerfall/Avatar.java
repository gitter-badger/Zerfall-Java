package exosoft.Zerfall;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class Avatar extends Sprite {
	Rectangle bounds = new Rectangle();
	private Gun[] gunSwitcher = { new akfs(), new aug(), new drgv(), new fal(), new fms(), new hkg3(), new colt(), new m60() };
	private double xPos = 4500;
	private double yPos = 1240;
	private double yVel = 0;
	private boolean collision[] = new boolean[5];
	private Gun currentGun = new Gun();
	private Timer swapGun;

	public Avatar() {
		super(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
		currentGun = new colt();
		swapGun = new Timer(250, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentGun = currentGun.swap(getCurrentGun(), gunSwitcher);
				if (!getKey(KeyEvent.VK_1)) {
					swapGun.stop();
				}
			}
		});
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

	public int getSpriteNum() {
		return spriteNum;
	}

	public void setSpriteNum(int spriteNum) {
		this.spriteNum = spriteNum;
	}

	synchronized void logic() {
		collision = collision();
		if (collision[1]) {
			yVel = 0;
		} else {
			yVel += .4;
		}
		if (getKey(KeyEvent.VK_A)) {
			if (!collision[3]) {
				xPos -= 4;
			}
		}
		if (getKey(KeyEvent.VK_D)) {
			if (!collision[4]) {
				xPos += 4;
			}
		}
		if (getKey(KeyEvent.VK_W)) {
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
     boolean[] collision() {
          int c;
          boolean[] collision = new boolean[5];
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
					if (getKey(KeyEvent.VK_E)) {
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
					if (getKey(KeyEvent.VK_E)) {
						openDoor(x, y);
					}
				case 0xFF000000:
					collision[4] = true;
					break rightLoop;
				}
			}
		}
		return collision;
     }
     synchronized void gunLogic() {
		if (getKey(KeyEvent.VK_SPACE) && !currentGun.fullFire.isRunning()) {
			currentGun.fire();
		}
		if (getKey(KeyEvent.VK_R) && !currentGun.reloadMag.isRunning()) {
			currentGun.reload();
		}
		if (getKey(KeyEvent.VK_1) && !currentGun.reloadMag.isRunning() && !swapGun.isRunning()) {
			swapGun.start();
		}
	}

	synchronized void visualLogic() {
		if (collision[1]) {
			setSpriteNum(getSpriteNum() - getSpriteNum() % 2);
		}
		if (getKey(KeyEvent.VK_D)) {
			switch (getSpriteNum()) {
			case 0:
			case 1:
			case 4:
			case 5:
				setSpriteNum(getSpriteNum() + 2);
				break;
			}
		}
		if (getKey(KeyEvent.VK_A)) {
			switch (getSpriteNum()) {
			case 2:
			case 3:
			case 6:
			case 7:
				setSpriteNum(getSpriteNum() - 2);
				break;
			}
		}
		if (getKey(KeyEvent.VK_W) && collision[1]) {
			setSpriteNum(getSpriteNum() + (getSpriteNum() + 1) % 2);
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

	class colt extends Gun {
		colt() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "colt"));
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
	
	class m60 extends Gun {
		m60() {
			super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "m60"));
		}
	}
	
	enum weaponType {
		FULL, SEMI, BOLT
	}
	
	public class Gun {
		Sound gunshotSND;
		Music reloadSND;
		int clipSize;
		int clipRounds;
		int damage;
		double fireRate;
		weaponType type;
		String name;
		int boltPosition;
		Timer fullFire, reloadMag, swapGun;
		Timer semiFire;
		boolean canFire = true;
		Timer boltFire;

		Gun() {
			gunshotSND = null;
			reloadSND = null;
			clipSize = 0;
			clipRounds = 0;
			damage = 0;
			fireRate = 0.0;
			name = null;
		}

		Gun(Map<String, Object> data) {
			try {
				clipRounds = clipSize = Integer.valueOf(data.get("clip").toString());
				fireRate = (Integer.valueOf(data.get("rpm").toString()));
				damage = (int) Integer.valueOf(data.get("eti").toString());
				type = weaponType.valueOf(data.get("type").toString().toUpperCase());
				name = data.get("name").toString();
				reloadSND = TinySound
						.loadMusic(new File("resources/sounds/guns/" + data.get("id").toString() + "_reload.au"));
				gunshotSND = TinySound
						.loadSound(new File("resources/sounds/guns/" + data.get("id").toString() + "_gunshot.au"));
				fullFire = new Timer((int) ((60 * 1000) / fireRate), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (Main.player.getSpriteNum() < 4) {
							Main.player.setSpriteNum(Main.player.getSpriteNum() + 4);
						}
						gunshotSND.play();
						clipRounds--;
						if (getKey(KeyEvent.VK_SPACE) || clipRounds < 1 || !reloadMag.isRunning()) {
							fullFire.stop();
						}
					}
				});
				semiFire = new Timer(10, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (getKey(KeyEvent.VK_SPACE)) {
							if (Main.player.getSpriteNum() < 4) {
								Main.player.setSpriteNum(Main.player.getSpriteNum() + 4);
							}
							gunshotSND.play();
							clipRounds--;
							canFire = false;
						}
						if (!getKey(KeyEvent.VK_SPACE)) {
							canFire = true;
							semiFire.stop();
						}
					}
				});
				reloadMag = new Timer(100, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (reloadSND.done()) {
							reloadSND.stop();
							clipRounds = clipSize;
							reloadMag.stop();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void fire() {
			if (getClipRounds() > 0 && !reloadMag.isRunning()) {
				switch (type) {
				case FULL:
					fullFire.start();
					break;
				case SEMI:
					if (canFire) {
						semiFire.start();
					}
					break;
				case BOLT:
					if (canFire) {
						boltFire.start();
					}
					break;
				default:
					break;
				}
			}
		}

		void reload() {
			reloadSND.play(false);
			reloadMag.start();
		}

		public int getClipSize() {
			return clipSize;
		}

		public int getClipRounds() {
			return clipRounds;
		}

		public int getDamage() {
			return damage;
		}

		public int getFireRate() {
			return (int) Math.round(fireRate);
		}

		public weaponType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public void setClipSize(int clipSize) {
			this.clipSize = clipSize;
		}

		public void setClipRounds(int clipRounds) {
			this.clipRounds = clipRounds;
		}

		public void setDamage(int damage) {
			this.damage = damage;
		}

		public void setFireRate(double fireRate) {
			this.fireRate = fireRate;
		}

		public Gun swap(Gun gun, Gun[] gunSwitcher) {
			Class<? extends Gun> gunClass = gun.getClass();
			if (gunSwitcher[gunSwitcher.length - 1].getClass() == gunClass) {
				return gunSwitcher[0];
			} else {
				for (int i = 0; i < gunSwitcher.length - 1; i++) {
					if (gunSwitcher[i].getClass() == gunClass) {
						return gunSwitcher[i + 1];
					}

				}
			}
			return gun;
		}
	}
}
