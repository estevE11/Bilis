package com.coconaut.bilis.gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpriteSheet {
	public Sprite sprite;
	
	public SpriteSheet(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void draw(double x, double y, int xx, int yy, int ww, int hh, Graphics g) {
		g.drawImage(sprite.getOriginalImage().getSubimage(xx, yy, ww, ww), (int)x, (int)y, ww, hh, null);
	}

	public void draw(double x, double y, int w, int h, int xx, int yy, int ww, int hh, Graphics g) {
		g.drawImage(sprite.getOriginalImage().getSubimage(xx, yy, ww, ww), (int)x, (int)y, w, h, null);
	}

	public BufferedImage getImage(int xx, int yy, int ww, int hh) {
		return sprite.getOriginalImage().getSubimage(xx, yy, ww, hh);
	}
}
