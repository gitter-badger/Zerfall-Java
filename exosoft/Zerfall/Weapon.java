package exosoft.Zerfall;

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
    Weapon(Map<String, Object> data) {
    	clipRounds = clipSize = (int) data.get("clip");
    	fireRate = ((int) data.get("rpm")) / (60 * logicRate);
    	damage = (int) data.get("eti");
    	type = weaponType.valueOf(data.get("type").toString());
    	name = data.get("name").toString();
    	reload = new Sound("resources/sounds/guns/" + data.get("id").toString() + "_reload.au");
    	gunshot = new Sound("resources/sounds/guns/" + data.get("id").toString() + "gunshot.au");
	}
}