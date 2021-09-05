package com.coconaut.bilis.gfx;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.entity.Entity;

public class Camera {
	public double offsetX = 0, offsetY;
	private double x = 0, y = 0;
	private int followFactor = 10;
	
	public void update(Entity follow) {
		this.x += (((follow.getX()+8) - Game.WIDTH/2) - this.x)/followFactor;
		
		if(this.x < 0) this.x = 0;
		if(this.x > (follow.getLevel().getWidth()*16)-Game.WIDTH) this.x = (follow.getLevel().getWidth()*16)-Game.WIDTH;
		
		this.offsetX = this.x * -1;
		this.offsetY = this.y * -1;
	}
	
	public void move(double xa, double ya) {
		this.x += xa;
		this.y += ya;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void reset() {
		this.setPosition(x, y);
		this.offsetX = 0;
		this.offsetY = 0;
	}
}
