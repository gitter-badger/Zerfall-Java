package exosoft.Zerfall;


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
    Weapon(String name, String id, int clipSize, int damage, int RPM, weaponType type) {
    	clipRounds = clipSize;
    	fireRate = RPM / 60 * logicRate;
	}
}