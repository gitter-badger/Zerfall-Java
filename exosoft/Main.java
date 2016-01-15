package exosoft;

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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
class Main extends JFrame implements KeyListener {
	static boolean keys[] = new boolean[256];
	static BufferedImage map = null;
	static BufferedImage bitmap = null;
	static Sheet sheet = new Sheet();
	static Player player = new Player();
	static Timer drawTimer, logicTimer;
	static BufferedImage foreground = null;

	Main() {
		super("Zerfall");
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(sheet);
		setVisible(true);
		setResizable(false);
		addKeyListener(this);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
		drawTimer = new Timer(1000 / 60, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sheet.repaint();
			}
		});
		logicTimer = new Timer(1000 / 120, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.logic();
			}
		});

		try {
			map = ImageIO.read(new File("resources/Maps/background.png"));
			bitmap = ImageIO.read(new File("resources/Maps/bitmap.png"));
			foreground = ImageIO
					.read(new File("resources/Maps/foreground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		drawTimer.start();
		logicTimer.start();
	}

	static class Sheet extends JPanel {
		@Override
		public void paintComponent(Graphics g1) {
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.translate(
					(int) -(player.xPos + player.sprites[0].getWidth() / 2 - getWidth() / 2),
					(int) -(player.yPos + player.sprites[0].getHeight() / 2 - getHeight() / 2));
			g.drawImage(bitmap, 0, 0, map.getWidth(), map.getHeight(), null);
			g.drawImage(player.sprites[player.spriteNum], (int) player.xPos,
					(int) player.yPos, null);
			g.drawImage(foreground, 0, 0, null);
			g.dispose();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = false;
		}
	}

	public static class Player {
		BufferedImage sprites[] = new BufferedImage[8];
		Rectangle bounds = new Rectangle();
		double xPos = 2270;
		double yPos = 1240;
		double yVel = 0;
		int spriteNum = 0;
		boolean collision[] = new boolean[5];

		public Player() {
			BufferedImage temp = null;
			try {
				temp = ImageIO.read(new File("resources/sprites/player.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 8; i++) {
				sprites[i] = temp.getSubimage(i * 175, 0, 175, 161);
			}
		}

		void logic() {
			int c;
			for (int i = 0; i < 5; i++)
				collision[i] = false;
			lowerLoop: for (int x = (int) (xPos + 25); x <= xPos + 150; x += 10) {
				for (int y = (int) (yPos + 161); y <= yPos + 162
						+ Math.abs(yVel); y += 10) {
					c = bitmap.getRGB((int) x / 10, (int) y / 10);
					switch (c) {
					case 0xFF000000:
						collision[1] = true;
						break lowerLoop;
					case 0xFF0000FF:
						collision[0] = true;
						break lowerLoop;
					}
				}
			}
			upperLoop: for (int x = (int) (xPos + 25); x <= xPos + 150; x += 10) {
				for (int y = (int) yPos; y <= yPos - 1 - Math.abs(yVel); y -= 10) {
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
			if (collision[1] == false) {
				yVel += .4;
			} else {
				yVel = 0;
				spriteNum -= spriteNum % 2;
			}
			if (keys[KeyEvent.VK_A] && collision[3] == false) {
				xPos -= 4;
				if (collision[1] && spriteNum != 0) {
					spriteNum = 0;
				}
			}
			if (keys[KeyEvent.VK_D] && collision[4] == false) {
				xPos += 4;
				if (collision[1] && spriteNum != 2)
					spriteNum = 2;
			}
			if (keys[KeyEvent.VK_W]) {
				if (collision[0]) {
					yVel = -4;
				} else if (collision[1]) {
					yVel -= 12;
					if (spriteNum % 2 == 0) {
						spriteNum++;
					}
				}

			}
			yPos += yVel;
		}

		public void openDoor(int givenX, int givenY) {
			int x = givenX / 10;
			int y = givenY / 10;
			int h = 0;
			boolean bool[] = new boolean[4];
			int doorColor = 0xFFFF0000;
			while (!(bool[1] && bool[3])) {
				if ((bool[1] = (bitmap.getRGB(x, y - 1) != doorColor)) == false) {
					y--;
				}
				if ((bool[3] = (bitmap.getRGB(x, y + h + 1) != doorColor)) == false) {
					h++;
				}
				System.out.println(bool[1] + ", " + bool[3]);
			}
			h++;
			System.out.println(y + ", " + h);
			Graphics2D foregroundGraphics = (Graphics2D) foreground.getGraphics();
			Graphics2D bitmapGraphics = (Graphics2D) bitmap.getGraphics();
			bitmapGraphics.setColor(Color.white);
			bitmapGraphics.fillRect(x, y, 1, h);
			bitmapGraphics.dispose();
			foregroundGraphics.setComposite(AlphaComposite.Clear);
			foregroundGraphics.fillRect(x * 10, y * 10, 11, h * 10 + 1);
			foregroundGraphics.dispose();
		}
	}
}
