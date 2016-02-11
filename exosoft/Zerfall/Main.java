package exosoft.Zerfall;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kuusisto.tinysound.TinySound;

@SuppressWarnings("serial")
class Main {
	static int logicRate, drawRate;
	static boolean keys[] = new boolean[256];
	static BufferedImage map = null, foreground = null, bitmap = null;
	static Sheet sheet;
	static Avatar player;
	static Timer drawTimer, logicTimer;
	static boolean itemsLoaded;

	public static void main(String[] args) {
		try {
			TinySound.init();
			logicRate = 120;
			drawRate = 60;
			player = new Avatar();
			map = ImageIO.read(new File("resources/maps/background.png"));
			bitmap = ImageIO.read(new File("resources/maps/bitmap.png"));
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
					Window window = new Window("Zerfall", 1280, 720, false);
					window.useMouse(true);
					window.useKeys(true);
					window.addPanel(sheet);
				}
			});
			logicTimer.start();
			drawTimer.start();
			itemsLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			if (itemsLoaded) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				g.setColor(Color.black);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.translate((int) -(player.getxPos() + player.sprites[0].getWidth() / 2 - getWidth() / 2),
						(int) -(player.getyPos() + player.sprites[0].getHeight() / 2 - getHeight() / 2));
				g.drawImage(map, 0, 0, map.getWidth(), map.getHeight(), null);
				g.drawImage(player.sprites[player.getSpriteNum()], player.getxPos(), player.getyPos(), null);
				g.drawImage(foreground, 0, 0, null);
				g.setColor(Color.blue);
				g.translate((int) (player.getxPos() + player.sprites[0].getWidth() / 2 - getWidth() / 2),
						(int) (player.getyPos() + player.sprites[0].getHeight() / 2 - getHeight() / 2));
				g.drawString(((Integer) player.getCurrentGun().getClipRounds()).toString(), 25, 25);
				g.drawString(player.getCurrentGun().getName(), getWidth() - 100, getHeight() - 100);
				if (player.getSpriteNum() > 3) {
					player.setSpriteNum(player.getSpriteNum() - 4);
				}
				g.dispose();
			}
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
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}