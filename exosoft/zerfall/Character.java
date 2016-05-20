package exosoft.zerfall;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import exosoft.iso.Entity;
import exosoft.iso.KeyObserver;
import exosoft.iso.Moveable;

class Character extends Entity implements Moveable {
	KeyObserver keywatch;
	public Character(SheetType type, String sheetPath, int spriteWidth, int spriteHeight, KeyListener keywatch) {
		super(type, sheetPath, spriteWidth, spriteHeight);
		this.keywatch = (KeyObserver) keywatch;
	}
	public void movement() {
		if (keywatch.getKey(KeyEvent.VK_D)) {
			moveRight(1);
		}
		if (keywatch.getKey(KeyEvent.VK_A)) {
			moveLeft(1);
		}
		if (keywatch.getKey(KeyEvent.VK_W)) {
			moveUp(1);
		}
		if (atRest && spriteNum % 2 == 1) {
		    spriteNum--;
		}
	}

	@Override
	public void moveLeft(double multiplier) {
		setX(getX() - 5);
		if (spriteNum % 4 > 1) {
		    spriteNum -= 2;
		}
	}

	@Override
	public void moveRight(double multiplier) {
		setX(getX() + 5);
		if (spriteNum % 4 < 2) {
		    spriteNum += 2;
		}
	}

	@Override
	public void moveUp(double multiplier) {
		if (atRest) {
			velocity = -10;
			atRest = false;
			if (spriteNum % 2 == 0) {
			    spriteNum++;
			}
		}
	}

	@Override
	public void moveDown(double multiplier) {
		// TODO Auto-generated method stub
	}
	@Override
	public void visual() {
		while (spriteNum >= sprites.length) {
		    spriteNum -= 2;
		}
		while (spriteNum < 0) {
		    spriteNum += 2;
		}
		activeSprite = getSprite(spriteNum);
	}
}