package exosoft.Zerfall;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Sprite extends Main {
	BufferedImage spriteSheet = null;
	BufferedImage[] sprites;
	int spriteWidth = 0;
	int spriteHeight = 0;
	int spriteNum;
	
	public enum SheetType {
		SINGLE,
		HORIZONTAL,
		VERTICAL,
		RECTANGULAR
	}
	public Sprite(SheetType type, String sheetPath, int spriteWidth, int spriteHeight) {
		try {
			spriteSheet = ImageIO.read(new File(sheetPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(type) {
		case HORIZONTAL:
			sprites = new BufferedImage[spriteSheet.getWidth() / spriteWidth];
			for (int i = 0; i < spriteSheet.getWidth() / spriteWidth; i++) {
				sprites[i] = spriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
			}
			break;
		case RECTANGULAR:
			break;
		case VERTICAL:
			sprites = new BufferedImage[spriteSheet.getHeight() / spriteHeight];
			for (int i = 0; i < spriteSheet.getHeight() / spriteHeight; i++) {
				sprites[i] = spriteSheet.getSubimage(0, i * spriteHeight, spriteWidth, spriteHeight);
			}
			break;
		default:
			break;
		}
	}
}