package exosoft.zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import exosoft.iso.Avatar;
import exosoft.iso.Environment;
import exosoft.iso.Sprite.SheetType;
import exosoft.util.Window;
import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
public class Main {
	final static int logicRate = 120;
	final static int drawRate = 60;
	static BufferedImage background;
	static BufferedImage foreground;
	static BufferedImage bitmap;
	static Sheet sheet;
	static Avatar player;
	static Timer drawTimer;
	static Timer logicTimer;
	private static boolean[] keys = new boolean[512];
	static Environment map = new Environment();

	public static void main(String[] args) {
		try {
			TinySound.init();
			player = new Avatar(SheetType.HORIZONTAL, "resources/sprites/player.png", 175, 161);
			player.setxPosition(0);
			player.setyPosition(0);
			player.setyVelocity(0);
			background = ImageIO.read(new File("resources/maps/background.png"));
			bitmap = ImageIO.read(new File("resources/maps/bitmap.png"));
			foreground = ImageIO.read(new File("resources/maps/foreground.png"));
			drawTimer = new Timer(1000 / drawRate, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					player.visual();
					sheet.repaint();
				}
			});
			logicTimer = new Timer(1000 / logicRate, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.collision();
					player.physics();
				}
			});
			sheet = new Sheet();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					Window window = new Window("Zerfall", 1280, 720);
					window.add(sheet);
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
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(player.getSprite(player.getSpriteNum()), player.getIntxPosition(), player.getIntyPosition(), null);
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

}