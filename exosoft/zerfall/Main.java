package exosoft.zerfall;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exosoft.iso.Sprite;
import exosoft.util.ObjPhys;
import exosoft.util.Phys2D;
import exosoft.util.Window;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
public class Main extends Window {
	final static int logicRate = 120;
	final static int drawRate = 60;
	static BufferedImage map;
	static BufferedImage foreground;
	static BufferedImage bitmap;
	static Sheet sheet;
	static Avatar player;
	static Timer drawTimer;
	static Timer logicTimer;
	private static boolean[] keys = new boolean[512];
	static Rectangle[] mesh;
	static Phys2D physics = new Phys2D();

	public static void main(String[] args) {
		try {
			TinySound.init();
			player = new Avatar();
			map = ImageIO.read(new File("resources/maps/background.png"));
			bitmap = ImageIO.read(new File("resources/maps/bitmap.png"));
			mesh = physics.createGrid(bitmap);
			foreground = ImageIO.read(new File("resources/maps/foreground.png"));
			drawTimer = new Timer(1000 / drawRate, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					player.visualLogic();
					sheet.repaint();
				}
			});
			logicTimer = new Timer(1000 / logicRate, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.logic();
					player.gunLogic();
				}
			});
			sheet = new Sheet();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					Window window = new Window("Zerfall", 1280, 720);
					window.addPanel(sheet);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.translate((int) -(player.getxPos() + player.getSprite(0).getWidth() / 2 - getWidth() / 2),
					(int) -(player.getyPos() + player.getSprite(0).getHeight() / 2 - getHeight() / 2));
			g.drawImage(map, 0, 0, map.getWidth(), map.getHeight(), null);
			g.drawImage(player.getSprite(player.getSpriteNum()), player.getxPos(), player.getyPos(), null);
			g.drawImage(foreground, 0, 0, null);
			g.setColor(Color.blue);
			g.translate((int) (player.getxPos() + player.getSprite(0).getWidth() / 2 - getWidth() / 2),
					(int) (player.getyPos() + player.getSprite(0).getHeight() / 2 - getHeight() / 2));
			g.drawString(((Integer) player.getCurrentGun().getClipRounds()).toString(), 25, 25);
			g.drawString(player.getCurrentGun().getName(), getWidth() - 100, getHeight() - 100);
			if (player.getSpriteNum() > 3) {
				player.setSpriteNum(player.getSpriteNum() - 4);
			}
			g.dispose();
		}
	}

	static Map<String, Object> parseXMLElement(String path, String tagName, String IDTag, String elementID) {
		try {
			Map<String, Object> XMLData = new HashMap<>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(tagName);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					NamedNodeMap attributes = eElement.getAttributes();
					boolean correctItem = false;
					for (int index = 0; index < attributes.getLength(); index++) {
						if (!correctItem) {
							correctItem = (attributes.item(index).getNodeName().equals(IDTag)
									&& attributes.item(index).getTextContent().equals(elementID));
						}
						XMLData.put(attributes.item(index).getNodeName(),
								attributes.item(index).getTextContent().trim());
					}
					if (correctItem) {
						if (eElement.hasChildNodes()) {
							NodeList nodeElements = eElement.getChildNodes();
							for (int eIndex = 0; eIndex < nodeElements.getLength() - 1; eIndex++) {
								XMLData.put(nodeElements.item(eIndex).getNodeName().trim(),
										nodeElements.item(eIndex).getTextContent().trim());
							}
							return XMLData;
						}
					}
				} else {
					System.err.println("You didn't give the parser the right info you idiot!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean getKey(int index) {
		return keys[index];
	}

	public static void setKey(int index, boolean status) {
		keys[index] = status;
	}

	public static class Avatar extends Sprite implements ObjPhys {
		Rectangle bounds;
		private Gun[] gunSwitcher = { new akfs(), new aug(), new drgv(), new fal(), new fms(), new hkg3(), new colt(),
				new m60() };
		private double xPos = 4500;
		private double yPos = 1240;
		private double yVel = 0;
		private boolean collision[] = new boolean[5];
		private Gun currentGun = new Gun();
		private Timer swapGun;

		public Avatar() {
			super(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
			bounds = new Rectangle(4500, 1240, 175, 161);
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
			if (meshClip() == true) {
				System.out.println("Collision detected.");
			}
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
			bounds.setLocation((int) Math.round(xPos), (int) Math.round(yPos));
		}

		@Deprecated
		public boolean[] collision() {
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

		boolean meshClip() {
			for (Rectangle rect : mesh) {
				if (bounds.intersects(rect)) {
					return true;
				}
			}
			return false;
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

		class m9 extends Gun {
			m9() {
				super(parseXMLElement("resources/data/gun_data.xml", "gun", "id", "m9"));
			}
		}

		enum weaponType {
			FULL, SEMI, BOLT
		}

		public class Gun {
			Sound gunshotSND;
			Music reloadSND;
			int clipSize, clipRounds, damage;
			double fireRate;
			weaponType type;
			String name;
			Timer fullFire, reloadMag, swapGun, semiFire, boltFire;
			boolean canFire = true;

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
								gunshotSND.play();
								clipRounds--;
								canFire = false;
							} else if (canFire == false) {
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

			public void fire() {
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

			public void reload() {
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

		@Override
		public void collisionLogic() {
			// TODO Auto-generated method stub
		}
	}

}