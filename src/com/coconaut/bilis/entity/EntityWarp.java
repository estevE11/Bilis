package com.coconaut.bilis.entity;

import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;

import java.awt.*;

public class EntityWarp extends Entity {
	private int w, h;
	
	public void render(Screen screen) {
		screen.fill(this.x, this.y, this.w, this.h, Color.red);
	}
	
	public void onEntityColide(Entity other) {
		if(other.type == EntityType.PLAYER) action((Player) other);
	}
	
	public void action(Player player) {
		
	}
	
	public void setSize(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public Rectangle tileB() {
		return new Rectangle(0, 0, this.w, this.h);
	}
}
