package com.coconaut.bilis.entity.projectile;

import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.Level;

import java.awt.*;

public class EntityLaser extends EntityProjectile {
	private int dir;
	
	public EntityLaser(Entity owner, int dir) {
        super(owner);
        this.type = EntityType.PROJECTILE;
		this.dir = dir;

		this.damage = 3;
    }

	public void init(Level level) {
	    super.init(level);

        if(dir == 1) this.x = owner.getX() + 16;
        else if(dir == -1) this.x = owner.getX() - 320 - 16;
        else Log.error("EntityLaser dir is not equal to 1 or -1 (dir=" + dir + ")");
    }
	
	public void update() {
		this.x += 20 * dir;
	}
	
	public void render(Screen screen) {
		if(dir == 1)screen.render(Resources.i.spr_laser, this.x, this.y);
		else if(dir == -1)screen.render(Resources.i.spr_laser, this.x, this.y, 1);
		else Log.error("EntityLaser dir is not equal to 1 or -1 (dir=" + dir + ")");
	}
	
	public Rectangle tileB() {
		return new Rectangle(0, 9, 320, 10);
	}
}
