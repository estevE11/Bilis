package com.coconaut.bilis.gfx;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Screen {
	public static int FLIP_H = 1;
	public static int FLIP_v = 0;

	private Graphics g;
	private Camera camera;
	
	public Screen(Camera camera) {
		this.camera = camera;
	}
	
	public void update(BufferedImage c) {
		this.g = c.getGraphics();
	}
	
	public void end() {
		g.dispose();
	}
	
	public void clear() {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	}
	
	public void render(Sprite spr, int x, int y, int w, int h, float opacity) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(spr.getOriginalImage(), x, y, w, h, null);
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
	
	public void render(SpriteSheet sheet, double x, double y, int xx, int yy, int ww, int hh) {
		sheet.draw(x + camera.offsetX, y + camera.offsetY, xx, yy, ww, hh, g);
	}

	public void render(SpriteSheet sheet, double x, double y, int w, int h, int xx, int yy, int ww, int hh) {
		sheet.draw(x + camera.offsetX, y + camera.offsetY, w, h, xx, yy, ww, hh, g);
	}

	public void render(SpriteSheet sheet, double x, double y, int xx, int yy, int ww, int hh, int flip) {
		BufferedImage image = sheet.getImage(xx, yy, ww, hh);
		
		AffineTransform tx = AffineTransform.getScaleInstance(flip == 0 ? 1 : -1, flip == 0 ? -1 : 1);
		if(flip == 0) tx.translate(0, -image.getHeight(null));
		else if(flip == 1) tx.translate(-image.getWidth(null), 0);
		else {
			Log.error("int flip must be 0 or 1");
			return;
		}
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);


		g.drawImage(image, (int)(x + camera.offsetX), (int)(y + camera.offsetY), null);
	}

	public void render(Sprite spr, double x, double y) {
		spr.draw(x + camera.offsetX, y + camera.offsetY, g);
	}

	public void renderOnHUD(Sprite spr, double x, double y) {
		spr.draw(x, y, g);
	}

	public void renderOnHUD(SpriteSheet spr, double x, double y, int xx, int yy, int ww, int hh) {
		spr.draw(x, y, xx, yy, ww, hh, g);
	}

	public void render(Sprite spr, double x, double y, float opacity) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		spr.draw(x + camera.offsetX, y + camera.offsetY, g);
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public void render(Sprite spr, double x, double y, int flip) {
		BufferedImage image = spr.getOriginalImage();
		
		AffineTransform tx = AffineTransform.getScaleInstance(flip == 0 ? 1 : -1, flip == 0 ? -1 : 1);
		if(flip == 0) tx.translate(0, -image.getHeight(null));
		else if(flip == 1) tx.translate(-image.getWidth(null), 0);
		else {
			Log.error("int flip must be 0 or 1");
			return;
		}
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);


		g.drawImage(image, (int)(x + camera.offsetX), (int)(y + camera.offsetY), null);
	}

	public void drawRect(Rectangle r, Color c) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(c);
		Rectangle rr = new Rectangle(r.x + (int)camera.offsetX, r.y + (int)camera.offsetY, r.width, r.height);
		g2d.draw(rr);
	}

    public void drawRect(Rectangle r, float opacity) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle rr = new Rectangle(r.x + (int)camera.offsetX, r.y + (int)camera.offsetY, r.width, r.height);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g2d.setColor(new Color(0, 0, 0));
        g2d.draw(rr);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

	public void drawRect(Rectangle r) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.red);
		Rectangle rr = new Rectangle(r.x + (int)camera.offsetX, r.y + (int)camera.offsetY, r.width, r.height);
		g2d.draw(rr);
	}
	
	public void renderTextOnLevel(String s, double x, double y, int size, boolean back) {
		g.setFont(new Font("Arial", Font.BOLD, size));
		if(back) {
			g.setColor(Color.red);
			g.drawString(s, (int)x+1, (int)y+1);
		}
		g.setColor(Color.white);
		g.drawString(s, (int)(x + Game.getCamera().offsetX), (int)(y + Game.getCamera().offsetY));
	}

	public void renderText(String s, int x, int y, int size, boolean back) {
		g.setFont(new Font("Arial", Font.BOLD, size));
		if(back) {
			g.setColor(Color.red);
			g.drawString(s, x+1, y+1);
		}
		g.setColor(Color.white);
		g.drawString(s, x, y);
	}
	
	public void renderTextPlain(String s, int x, int y, int size, boolean back) {
		g.setFont(new Font("Arial", Font.PLAIN, size));
		if(back) {
			g.setColor(Color.red);
			g.drawString(s, x+1, y+1);
		}
		g.setColor(Color.white);
		g.drawString(s, x, y);
	}
	
	public void fill(double x, double y, int w, int h, Color color) {
		g.setColor(color);
		g.fillRect((int)x + (int)camera.offsetX, (int)y + (int)camera.offsetY, w, h);
	}

	public void fillOnHUD(double x, double y, int w, int h, Color color) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, w, h);
	}
	
	public void renderHealthBar(int health, int maxHealth, int x, int y, int w, int h, boolean font) {
		fillOnHUD(x, y, w, h, Color.white);
		int ww = w-2;
		int hh = h-2;
		fillOnHUD(x+1, y+1, ww, hh, Color.black);
		fillOnHUD(x+1, y+1, ww * health / maxHealth, hh, Color.red);
				
		String txt = health + "/" + maxHealth;
		renderTextPlain(txt, x + (w/2 - calcTextWidth(txt)/2), y + 15, 16, false);
	}
	
	public int calcTextWidth(String msg) {
		return g.getFontMetrics().stringWidth(msg);
	}

	public int calcTextHeight(String msg) {
		return (int)g.getFontMetrics().getLineMetrics(msg, g).getHeight();
	}

	public Graphics getG() {
		return g;
	}
}
