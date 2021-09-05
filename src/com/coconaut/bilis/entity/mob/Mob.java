package com.coconaut.bilis.entity.mob;

import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;

import java.awt.*;

public class Mob extends Entity{
	protected boolean hasGravity = true;
	protected boolean inWater = false;
	public void update() {
		if(hasGravity && (this.inWater ? true : !onGround)) this.ya += this.inWater ? 0.1 : 0.5;
		else if(!hasGravity) this.ya = 0;
		super.update();
	}
	
	public void render(Screen screen) {
		
	}
	
	public void move() {
		super.move();
	}
	
	public Rectangle bounds() {
		return null;
	}
}
