package com.coconaut.bilis.level.tile;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.SpriteSheet;
import com.coconaut.bilis.level.Level;

import java.util.Random;

public class RockTile extends Tile {
	
	private SpriteSheet sheet;
	private Random r = new Random();
	private int idd;
	
	public RockTile(int id) {
		super(id);
		this.sheet = new SpriteSheet(Resources.i.spr_tile_rock);
		this.idd = this.r.nextInt(2);
	}

	public void render(int x, int y, Level level, Screen screen) {
		if(x + 16 > 0 && x < Game.WIDTH - 16 && y + 16 > 0 && y < Game.HEIGHT - 16) {
			boolean u = level.getTileIDFT(x, y-1) == this.id;
			boolean d = level.getTileIDFT(x, y+1) == this.id;
			boolean l = level.getTileIDFT(x-1, y) == this.id;
			boolean r = level.getTileIDFT(x+1, y) == this.id;
			
			if(u && d && l && r) return;
	
			if(!u && !d && !l && !r) screen.render(sheet, x*16, y*16, 4*16, 1*16, 16, 16, idd);
			
			if(!u && d && l && r) screen.render(sheet, x*16, y*16, 1*16, 0*16, 16, 16);
			if(u && !d && l && r) screen.render(sheet, x*16, y*16, 1*16, 2*16, 16, 16);
			if(u && d && !l && r) screen.render(sheet, x*16, y*16, 0*16, 1*16, 16, 16);
			if(u && d && l && !r) screen.render(sheet, x*16, y*16, 2*16, 1*16, 16, 16);
			
			if(!u && d && !l && r) screen.render(sheet, x*16, y*16, 0*16, 0*16, 16, 16);
			if(!u && d && l && !r) screen.render(sheet, x*16, y*16, 2*16, 0*16, 16, 16);
			if(u && !d && !l && r) screen.render(sheet, x*16, y*16, 0*16, 2*16, 16, 16);
			if(u && !d && l && !r) screen.render(sheet, x*16, y*16, 2*16, 2*16, 16, 16);
			
			if(u && !d && !l && !r) screen.render(sheet, x*16, y*16, 3*16, 0*16, 16, 16, 0);
			if(!u && d && !l && !r) screen.render(sheet, x*16, y*16, 3*16, 0*16, 16, 16);
			if(!u && !d && l && !r) screen.render(sheet, x*16, y*16, 4*16, 0*16, 16, 16);
			if(!u && !d && !l && r) screen.render(sheet, x*16, y*16, 4*16, 0*16, 16, 16, 1);
			
			if(u && d && !l && !r) screen.render(sheet, x*16, y*16, 5*16, 0*16, 16, 16);
			if(!u && !d && l && r) screen.render(sheet, x*16, y*16, 5*16, 1*16, 16, 16);
		}
	}
}
