package com.coconaut.bilis.entity.particle;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.gfx.Screen;

public class EntityParticleDead extends EntityParticle {
	private Random r = new Random();
	
	public EntityParticleDead() {
		this.live = Game.random(50, 90, 0);
		this.xa = r.nextInt(4) + 1;
		this.ya = Game.random(1, 5, 1);
	}
	
	public void update() {
		super.update();
		this.ya += 0.3;
		if(this.xa > 0) this.xa -= this.xa/8;
		else if(this.xa < 0) this.xa += this.xa/8;
		this.move();
	}
	
	public void render(Screen screen) {
		screen.fill(this.x, this.y, 3, 3, Color.red);
	}
	
	public Rectangle tileB() {
		return new Rectangle(0, 0, 2, 2);
	}
}
