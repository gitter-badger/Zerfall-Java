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

import exosoft.iso.Phys2D;
import exosoft.iso.Sprite;
import exosoft.util.ObjPhys;
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
			if (player.getSpriteNum() > 3) {
				player.setSpriteNum(player.getSpriteNum() - 4);
			}
			g.dispose();
		}
	}

	@Deprecated
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
		private double xPos = 4500;
		private double yPos = 1240;
		private double yVel = 0;
		private boolean collision[] = new boolean[5];

		public Avatar() {
			super(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
			bounds = new Rectangle(getxPos(), getyPos(), 175, 161);
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

		boolean meshClip() {
			for (Rectangle rect : mesh) {
				if (bounds.intersects(rect)) {
					return true;
				}
			}
			return false;
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

		@Override
		public void collisionLogic() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean[] collision() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}