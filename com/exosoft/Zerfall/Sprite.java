package com.exosoft.Zerfall;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite extends Main {
	BufferedImage spriteSheet = null;
	BufferedImage[] sprites;
	int spriteWidth = 0;
	int spriteHeight = 0;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch(type) {
		case HORIZONTAL:
			for (int i = 0; i < 8; i++) {
				sprites[i] = spriteSheet.getSubimage(i * spriteWidth, 0, 175, 161);
			}
			break;
		case RECTANGULAR:
			break;
		case VERTICAL:
			break;
		default:
			break;
			
		}
	}
}
