package com.coconaut.bilis.entity;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.util.Random;

public class EntityPolt extends Entity {
	private Random r = new Random();
	
	private int health = 2;
	
	private Animation anim;
	private double[] vels = {1, -1};
	
	public EntityPolt() {
		this.type = EntityType.ENEMY;
		this.cmdName = "entity:polt";

		this.anim = new Animation(Resources.i.spr_polt, 16, 16, 1, 10);
		this.anim.play();
	
		this.xa = vels[r.nextInt(2)];
	}
	
	public void update() {
		this.anim.update();
		if(collision(1, 0)) this.xa = vels[1];
		if(collision(-1, 0)) this.xa = vels[0];
		if(!collision(this.xa > 0 ? 16 : -16, 16)) {
			this.xa = this.xa > 0 ? vels[1] : vels[0];
		}
		this.move();
	}

	public void onEntityColide(Entity other) {
	
		if(other.type == EntityType.PROJECTILE) {
			this.health--;
			Sound.enemy_hurt.play();
			if(!(other instanceof EntityLaser)) other.kill();
			if(health <= 0) this.kill();
		}
	}
	
	public void render(Screen screen) {
		this.anim.draw((int)this.x, (int)this.y, screen);
	}

    public void reset() {
        this.health = 2;
    }

	public Rectangle tileB() {
		return new Rectangle(1, 1, 13, 13);
	}
}
