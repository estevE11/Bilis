package com.coconaut.bilis.entity.particle;

import java.awt.Color;

import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;

public class EntityParticle extends Entity {
	protected int live, tickTime;
	
	public void update() {
		this.tickTime++;
		if(this.live <= this.tickTime) this.kill();
	}
	
	public void render(Screen screen) {
	}
	
	public void kill() {
		this.isDead = true;
	}
}
