package exosoft.Zerfall;

import java.util.HashMap;
import java.util.Map;

public class Weapon extends Avatar {
    Sound gunshot;
    Sound reload;
    int clipSize;
    int clipRounds;
    int damage;
    double fireRate;
    weaponType type;
    String name;
    enum weaponType {
    	FULL, SEMI, BOLT
    }
    Weapon() {
        gunshot = null;
        reload = null;
        clipSize = 0;
        clipRounds = 0;
        damage = 0;
        fireRate = 0.0;
        name = null;
    }
    Weapon(String id) {
    	Map<String, Object> data = parseXML("resources/data/gun_data.xml", "gun", id);
    	System.out.println(data.values());
    	clipSize = (int) data.get("clip");
    	damage = (int) data.get("eti");
    	type = (weaponType) data.get("type");
    	fireRate = (double) data.get("rpm");
    }
}