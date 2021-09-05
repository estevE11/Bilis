package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.entity.mob.Mob;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;
import com.coconaut.bilis.level.Level;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class EntityItem extends Mob {
	private static Random r = new Random();
	public static String[] items = {"entity:item:spectral_ball", "entity:item:balls_up", "entity:item:boost_jump", "entity:item:boost_speed", "entity:item:power_rock"};
	private static LinkedList<Integer> usedItems = new LinkedList<Integer>();
	
	protected String name = "No name";
	
	boolean hasAction = true;
	
	protected Sprite spr;
	private double offY;
	private int tickTime = 0;
	
	public EntityItem() {
		this.type = EntityType.ITEM;
		this.hasGravity = true;
		
		this.xa = Game.random(5, 10, 2);
		this.ya = Game.random(7, 11, 1);
	}
	
	public void update() {		
		if(this.xa < 0) {
			this.xa += -(this.xa/8);
		} else if(xa > 0) {
			this.xa -= this.xa/8;
		}
		
		super.update();
		
		if(this.onGround) {
			this.tickTime++;
			this.offY = Math.sin(this.tickTime/6) * 3;
		}
		
		this.move();
	}
	
	public void render(Screen screen) {
		spr.draw(this.x, (this.y + this.offY) - 3, screen);
	}

    public void render(int x, int y, Screen screen) {
        spr.draw(x, y, screen);
    }

	public void onCollect(Player player) {
		Sound.item_pickup.play();
		Log.info(this.name + " picked up!");
		player.addItem(this);
	}
	
	public void onEntityColide(Entity other) {
		if(other.type == EntityType.PLAYER) this.onCollect((Player)other);
	}
	
	public void action(Player player, Level level) {
		
	}
	
	public Rectangle tileB() {
		return new Rectangle(1, 1, 6, 6);
	}
	
	public void kill() {
		this.isDead = true;
	}
	
	public static EntityItem getRandomItem() {
		int i = 0;
		
		boolean ended = false;
		while(!ended) {
			i = r.nextInt(items.length);
			boolean c = false;
			for(int j = 0; j < usedItems.size() && !c; j++) {
				if(usedItems.get(j) == i) {
					c = true;
				}
			}
			
			if(!c) ended = true;
		}
		
		usedItems.add(i);
		EntityItem item = (EntityItem) Entity.createEntityByName(items[i]);
		return item;
	}
}
