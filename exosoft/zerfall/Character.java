package exosoft.zerfall;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import exosoft.iso.Entity;
import exosoft.iso.Moveable;
import exosoft.util.KeyObserver;

class Character extends Entity implements Moveable {
	KeyObserver keywatch;
	public Character(SheetType type, String sheetPath, int spriteWidth, int spriteHeight, KeyListener keywatch) {
		super(type, sheetPath, spriteWidth, spriteHeight);
		this.keywatch = (KeyObserver) keywatch;
	}
	public void movement() {
		if (keywatch.getKey(KeyEvent.VK_W)) {
			moveUp(1);
		}
		if (keywatch.getKey(KeyEvent.VK_D)) {
			moveRight(1);
		}
		if (keywatch.getKey(KeyEvent.VK_A)) {
			moveLeft(1);
		}
	}

	@Override
	public void moveLeft(double multiplier) {
		setX(getX() - 5);
	}

	@Override
	public void moveRight(double multiplier) {
		setX(getX() + 5);
	}

	@Override
	public void moveUp(double multiplier) {
		if (atRest) {
			velocity = -10;
			atRest = false;
		}
	}

	@Override
	public void moveDown(double multiplier) {
		// TODO Auto-generated method stub
	}
	@Override
	public void visual() {
		// TODO Auto-generated method stub
	}
}