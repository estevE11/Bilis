package com.coconaut.bilis.level;

import com.coconaut.bilis.JSONUtil;
import com.coconaut.bilis.entity.*;
import com.coconaut.bilis.entity.item.EntityItem;
import com.coconaut.bilis.level.tile.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;

public class Room {
	public static Room[] rooms     = new Room[20];
	public static Room mainRoom    = new Room("room_main.json");
	public static Room preLastRoom = new Room("room_003.json");
	public static Room lastRoom    = new Room("room_last.json");
	public static Room preBossRoom = new Room("room_preboss.json");
	public static Room bossRoom = new Room("room_boos.json");

	private LinkedList<Entity> entities = new LinkedList<Entity>();
	private int[] tiles;
	private int w, h;
	public static int availableRooms = 19+1;
	
	public static void loadRooms() {
		rooms[0]  = new Room("room_001.json");
		rooms[1]  = new Room("room_002.json");
		rooms[2]  = new Room("room_005.json");
		rooms[3]  = new Room("room_006.json");
		rooms[4]  = new Room("room_007.json");
		rooms[5]  = new Room("room_011.json");
		rooms[6]  = new Room("room_012.json");
		rooms[7]  = new Room("room_013.json");
		rooms[9]  = new Room("room_014.json");
		rooms[8]  = new Room("room_022.json");
		rooms[10] = new Room("room_015.json");
		rooms[11] = new Room("room_016.json");
		rooms[12] = new Room("room_018.json");
		rooms[13] = new Room("room_019.json");
		rooms[14] = new Room("room_020.json");
		rooms[15] = new Room("room_021.json");
		rooms[16] = new Room("room_022.json");
		rooms[17] = new Room("room_023.json");
		rooms[18] = new Room("room_017.json");
		rooms[19] = new Room("room_015.json");
	}
	
	public Room(String path) {
		JSONObject jobj  = JSONUtil.load("resources/rooms/" + path);
		JSONArray jarr   = (JSONArray) jobj.get("layers");
		JSONObject layer = (JSONObject) jarr.get(0);
		JSONArray data   = (JSONArray) layer.get("data");
		long ww = (Long) jobj.get("width");
		long hh = (Long) jobj.get("height");
		this.w  = (int) ww;
		this.h  = (int) hh;
		
		this.tiles = new int[this.w * this.h];
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int i = x + y * w;
				long t = (Long) data.get(i);
				int tt = (int) t;
				if(tt != 0 && tt != 1 && tt != 2) {
					if(tt == 256)entities.add(new EntityHotBall().setPosition(x * 16, y * 16));
					if(tt == 255)entities.add(new EntityPolt().setPosition(x * 16, y * 16));
					if(tt == 253)entities.add(new EntityChest(EntityItem.getRandomItem()).setPosition(x * 16, y * 16));
					if(tt == 252)entities.add(new EntitySkull().setPosition(x * 16, y *  16));
                    if(tt == 252)entities.add(new EntityEdust().setPosition(x * 16, y *  16));
					this.tiles[i] = 1;
					continue;
				} 
				this.tiles[i] = tt;
			}
		}
	}
	
	public void summonEntities(Level level) {
		for(Entity e : entities) {
			//Entity ent = e.type == Entity.EntityType.CHEST ? new EntityChest(EntityItem.getRandomItem()) : Entity.createEntityByName(e.cmdName);
            Entity ent = e;
			level.summon(e, e.getX(), e.getY());
		}
	}
	
	public Tile getTile(int x, int y) {
		return Tile.tiles[this.tiles[x + y * this.w] -1];
	}

	public Tile getTile(int i) {
		return Tile.tiles[this.tiles[i] - 1];
	}
}
