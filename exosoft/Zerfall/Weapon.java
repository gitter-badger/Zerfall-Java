package exosoft.Zerfall;

import java.io.File;

public class Weapon extends Avatar {
    Sound gunshot;
    Sound reload;
    int clipSize;
    int clipRounds;
    int damage;
    double fireRate;
    String name;
    
    Weapon() {
        gunshot = null;
        reload = null;
        clipSize = 0;
        clipRounds = 0;
        damage = 0;
        fireRate = 0.0;
        name = null;
    }
}