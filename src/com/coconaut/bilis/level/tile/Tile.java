package com.coconaut.bilis.level.tile;

import com.coconaut.bilis.Log;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.Level;

public class Tile {
	
	public static Tile[] tiles = new Tile[256];
	public static Tile air = new Tile(0);
	public static Tile entity = new Tile(2);
	public static Tile rock = new RockTile(1);
	public static Tile spikes = new RockTile(1);
    public static Tile barrier = new BarrierTile(3);
	private static Tile nil = new Tile(100);

	public byte id;
	
	public static void init() {
		tiles[0] = air;
		tiles[1] = rock;
		tiles[2] = entity;
        tiles[3] = barrier;
	}
	
	public Tile(int id) {
		this.id = (byte)id;
	}
	
	public void update(int x, int y, Level level) {
		
	}
	
	public void render(int x, int y, Level level, Screen screen) {
		
	}

	public static Tile getTileById(int id) {
		if(id < 0 || id > 1) {
			Log.error("arg1 in Tile.getTileById mustn't be higher than 1.");
			Log.shutDown(1);
		}
		return id == 0 ? Tile.air : Tile.rock;
	}
}
