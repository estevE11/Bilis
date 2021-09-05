package com.coconaut.bilis.gfx;

public class Animation {
	private SpriteSheet sheet;
	
	private boolean isPlaying = false;
	private int frame = 0;
	private int frames;
	private int tickCount = 0;
	private int frameTime = 0;
	
	private int w, h;
	
	
	public Animation(Sprite sprSheet, int w, int h, int frames, int time) {
		sheet = new SpriteSheet(sprSheet);
		this.frames = frames;
		this.frameTime = time;
		
		this.w = w;
		this.h = h;
	}
	
	public void update() {
		if(!isPlaying) return;
		this.tickCount++;

		if(tickCount >= this.frameTime) {
			this.frame++;
			if(this.frame > this.frames) this.frame = 0;
			this.tickCount = 0;
		}
	}
	
	public void draw(int x, int y, Screen screen, int flip) {
		screen.render(sheet, x, y, this.frame*w, 0, w, h, flip);
	}

	public void draw(int x, int y, Screen screen) {
		screen.render(sheet, x, y, this.frame*w, 0, w, h);
	}

	public void draw(int x, int y, int w, int h, Screen screen) {
		screen.render(sheet, x, y, w, h, this.frame*this.w, 0, this.w, this.h);
	}

	public int getFrame() {
		return frame;
	}
	
	public void play() {
		isPlaying = true;
	}
	
	public void stop() {
		isPlaying = false;
	}
	
	public void restart() {
		this.frames = 0;
	}
}
