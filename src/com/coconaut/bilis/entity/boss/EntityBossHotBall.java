package com.coconaut.bilis.entity.boss;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;

import java.awt.*;

public class EntityBossHotBall extends EntityBoss{
	private Sprite spr = Resources.i.spr_boss_hotball;

	public void update() {
		super.update();
	}
	
	public void render(Screen screen) {
		screen.render(this.spr, this.x, this.y, (float)this.dieTick/this.dieTime);
	}
	
	public void onEntityColide(Entity other) {
		if(other.type == EntityType.PROJECTILE) this.dead = true;
	}

	public Rectangle tileB() {
		return new Rectangle(0, 0, 32, 32);
	}
}
