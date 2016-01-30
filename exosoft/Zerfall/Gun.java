package exosoft.Zerfall;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.Timer;
import kuusisto.tinysound.*;

import com.sun.glass.events.KeyEvent;

public class Gun extends Main {
	Sound gunshotSND;
	Music reloadSND;
	int clipSize;
	int clipRounds;
	int damage;
	double fireRate;
	weaponType type;
	String name;
	int boltPosition;
	Timer boltRotation, reloadMag;

	enum weaponType {
		FULL, SEMI, BOLT
	}

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
			boltRotation = new Timer((int) ((60 * 1000) / fireRate), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Main.player.spriteNum < 4) {
						Main.player.spriteNum += 4;
					}
					gunshotSND.play();
					clipRounds--;
					if (!Main.keys[KeyEvent.VK_SPACE] || clipRounds < 1 || !reloadMag.isRunning()) {
						boltRotation.stop();
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
			boltRotation.start();
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

}