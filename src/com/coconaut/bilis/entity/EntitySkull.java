package com.coconaut.bilis.entity;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Mob;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.util.Random;

public class EntitySkull extends Mob {
	private Random r = new Random();
	
	private Animation anim;
	
	private int health = 3;
	private int dir = 0;
	private double speed = 1;
	
	public EntitySkull() {
		this.type = EntityType.ENEMY;
		this.cmdName = "entity:skull";
		this.hasGravity = true;
		
		this.dir = r.nextInt(2);
		
		this.anim = new Animation(Resources.i.spr_skull, 16, 16, 1, 12);
		this.anim.play();
	}
	
	public void update() {
		if(this.health <= 0) this.kill();
		
		if(this.dir != 0 && this.dir != 1) this.dir = 0;
		 if(this.dir == 0) this.xa = -this.speed;
		 else this.xa = this.speed;
		
		this.move();
		super.update();
		this.anim.update();
		
		if(collision(this.dir == 0 ? -10 : 10, 0) && this.onGround) {
			if(collision(this.dir == 0 ? -10 : 10, -16)) this.dir = this.dir == 0 ? 1 : 0;
			else {
				this.ya = -5;
				this.onGround = false;
			}
		}
		
		if(this.onGround) {
			Entity e = level.entityCollision_proj((int)this.x + (this.dir == 0 ? -10 : 10), (int)this.y, 16, 16, this);
			if(e != null) {
				if(e.type == EntityType.PROJECTILE) {
					this.ya = -5;
					this.onGround = false;
				}
			}
		}
	}
	
	public void render(Screen screen) {
		boolean flip = false;
		if(this.dir == 1) flip = true;
		if(flip) this.anim.draw((int)this.x, (int)this.y, screen, 1);
		else this.anim.draw((int)this.x, (int)this.y, screen);
//		screen.fill(this.x + (this.dir == 0 ? -10 : 10), this.y, 16, 16, Color.red);
	}
	
	public void onEntityColide(Entity other) {
		if(other.type == EntityType.PROJECTILE) {
			this.health--;
			Sound.enemy_hurt.play();
			if(!(other instanceof EntityLaser)) other.kill();
			if(health <= 0) this.kill();
		}
	}

    public void reset() {
        this.health = 3;
    }

    public Rectangle tileB() {
		return new Rectangle(0, 0, 16, 16);
	}
}
