package com.coconaut.bilis.entity.projectile;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;

import java.awt.*;

public class EntitySlimeBall extends EntityProjectile {
	private double speed = 4.5;
	private double grav = 0.01;
	private boolean isSpectral = true;
	private Sprite spr;
	private int dir;
	
	public EntitySlimeBall(Entity owner, int dir, boolean spectral) {
        super(owner);
		this.type = EntityType.PROJECTILE;
		
		this.isSpectral = spectral;
		
		if(!(dir != 1 && dir != -1)) {
			this.dir = dir;
			this.spr = Resources.i.spr_slimeballs;
		} else {
			Log.error("EntitySlimeBall pram 1 must be 1 or -1.");
			Log.shutDown(1);
		}

		this.damage = 1;
	}
	
	public void update() {
		this.xa = speed*dir;
		this.ya += this.grav;
		if(!isSpectral) this.move();
		else {
			this.x += this.xa;
			this.y += this.ya;
		}
		if(this.y > Game.HEIGHT) this.kill();
	}
	
	public void render(Screen screen) {
		screen.render(this.spr, this.x, this.y);
	}
	
	public void onTileColide() {
		this.kill();
	}
	
	public void setSpectral(boolean bol) {
		this.isSpectral = bol;
	}
	
	public void kill() {
		this.isDead = true;
	}
	
	public int dir() {
		return dir;	
	}

	public Rectangle tileB() {
		return new Rectangle(2, 2, 4, 4);
	}
}
