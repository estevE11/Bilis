package com.coconaut.bilis.entity;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.entity.projectile.EntitySlimeBall;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.util.Random;

public class EntityHotBall extends Entity{
	private Random r = new Random();
	private int vels[] = {1, -1};
	private int health = 2;
	private double kx = 0;

	private Animation anim;
	
	public EntityHotBall() {
		this.type = EntityType.ENEMY;
		this.cmdName = "entity:hot_ball";
		this.anim = new Animation(Resources.i.spr_hotball, 16, 16, 1, 12);
		this.anim.play();
		
		this.xa = vels[r.nextInt(2)];
		this.ya = vels[r.nextInt(2)];
	}
	
	public void update() {
		if(this.health <= 0) {this.kill(); return;}
		
		this.anim.update();
		if(collision(1, 0)) cRight();
		if(collision(-1, 0)) cLeft();
		if(collision(0, 1)) cDown();
		if(collision(0, -1)) cTop();
//		Log.debug(xa + ", " + ya);
		this.move();
		this._move(this.kx, 0);
		
		if(this.kx > 0) this.kx-=0.5;
		if(this.kx < 0) this.kx+=0.5;
	}
	
	public void cTop() {
		this.ya *= -1;
	}
	public void cDown() {
		this.ya *= -1;
	}
	public void cRight() {
		this.xa *= -1;
	}
	public void cLeft() {
		this.xa *= -1;
	}
		
	public void render(Screen screen) {
		this.anim.draw((int)this.x, (int)this.y, screen);
//		screen.drawRect(tileE());
	}
	
	public void onEntityColide(Entity other) {
		if(other.type == Entity.EntityType.PROJECTILE) {
			this.health--;
			if(other instanceof EntitySlimeBall) {
				if(((EntitySlimeBall) other).dir() == -1) this.kx = -5;
				if(((EntitySlimeBall) other).dir() == 1) this.kx = 5;
			}
			Sound.enemy_hurt.play();
			if(!(other instanceof EntityLaser)) other.kill();
		}
	}

	public void reset() {
	    this.health = 2;
    }
	
	public Rectangle tileB() {
		return new Rectangle(0, 0, 16, 16);
	}
}
