package com.coconaut.bilis.gfx;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprite {
	
	private BufferedImage image;
	private int w, h;
	
	public Sprite(String path) {
		try {
			String p = "/gfx/" + path;
			Log.info("Attempting to load " + p);
			image = ImageIO.read(Game.class.getResourceAsStream(p));
			Log.info(p + " successfuly loaded");
		} catch (IOException e) {
            Log.error(e.getMessage());
		}
		
		this.w = image.getWidth();
		this.h = image.getHeight();
	}
	
	public void draw(double x, double y, Graphics g) {
		g.drawImage(this.image, (int)x, (int)y, this.w, this.h, null);
	}

	public void draw(double x, double y, int f, Screen s) {
		s.render(this, x, y, f);
	}

	public void draw(double x, double y, Screen s) {
		s.render(this, x, y);
	}
	
	public BufferedImage getOriginalImage() {
		return image;
	}
}
