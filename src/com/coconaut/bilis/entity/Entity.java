package com.coconaut.bilis.entity;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.entity.boss.EntityBossHotBall;
import com.coconaut.bilis.entity.item.*;
import com.coconaut.bilis.entity.particle.EntityParticleDead;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.Level;
import com.coconaut.bilis.level.tile.Tile;
import com.google.common.collect.Maps;

import java.awt.*;
import java.util.Map;

public class Entity {
	private static final Map < String, Class <? extends Entity >> NAME_TO_CLASS = Maps. < String, Class <? extends Entity >> newHashMap();
	
	protected double x, y;
	protected double xa = 0, ya = 0;

	protected boolean onGround = false;

	public EntityType type;
	public String cmdName = "entity:unknown";
	
	public enum EntityType {
			PLAYER, PROJECTILE, ENEMY, PARTICLE, CHEST, ITEM, BOSS
	}
	
	protected boolean isDead = false;
	
	protected Level level;
	
	protected Point[] points = new Point[4];
	
	public void init(Level level) {
		this.level = level;
		
		for(int i = 0; i < 4; i++) {
			points[i] = new Point(0, 0);
		}
	}
	
	public void update() {
		this.onGround = false;
		if(collision(0, 1)) {this.onGround = true; this.ya = 0;}
	}
	
	public void render(Screen screen) {
	}
	
	public void move() {
		_move(xa, ya);
	}
	
	public void _move(double xa, double ya) {
		int xx = (int)Math.abs(xa);
		int yy = (int)Math.abs(ya);
		for(int x = 0; x < xx; x++) {
			if(!collision(abs((int)xa), 0)) {
				this.x += abs((int) xa);
				
			}
		}

		for(int y = 0; y < yy; y++) {
			if(!collision(0, abs((int)ya))) {
				this.y += abs((int) ya);
			}
		}
	}
	
	public boolean collision(double xa, double ya) {
		boolean solid = false;
		for(int c = 0; c < 4; c++) {
			int xt = ((int)this.x + (int)xa) + c % 2 * tileB().width + tileB().x;
			int yt = ((int)this.y + (int)ya) + c / 2 * tileB().height + tileB().y;
			points[c] = new Point(xt, yt);
			
			if(level.getTile(xt, yt) != Tile.air) {
				solid = true;
			}
		}
		if(solid) {
			onTileColide();
		}
		return solid;
	}
	
	public void onTileColide() {
		
	}
	
	public void onEntityColide(Entity other) {
	}
	
	public static void addMappings() {
		//addMapping(Player.class, "entity:player");
		addMapping(EntityChest.class, "entity:chest");
		
		//Enemies
		addMapping(EntityHotBall.class, "entity:hot_ball");
		addMapping(EntityPolt.class, "entity:polt");
		addMapping(EntitySkull.class, "entity:skull");
        addMapping(EntityEdust.class, "entity:edust");
//		addMapping(EntityLaser.class, "entity:laser");
		//addMapping(EntitySlimeBall.class, "entity:slime_ball");
		
		//Items
		addMapping(EntityItemJumpBoost.class, "entity:item:boost_jump");
		addMapping(EntityItemSpeedBoost.class, "entity:item:boost_speed");
		addMapping(EntityItemSpectralBall.class, "entity:item:spectral_ball");
		addMapping(EntityItemBallsUp.class, "entity:item:balls_up");
		addMapping(EntityItemPowerRock.class, "entity:item:power_rock");

		//Bosses
		addMapping(EntityBossHotBall.class, "entity:boss:hot_ball");
	}
	
	public static Entity createEntityByName(String name) {
		Entity entity = null;
		
		try {
			@SuppressWarnings("unchecked")
			Class <? extends Entity> oclass = (Class<? extends Entity>)NAME_TO_CLASS.get(name);
			System.out.println(oclass);
			if(oclass != null) {
				entity = (Entity)oclass.getConstructor(new Class[] {}).newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}

	private static void addMapping(Class <? extends Entity > entityClass, String entityName) {
        if (NAME_TO_CLASS.containsKey(entityName))
        {
            Log.error("ID is already registered: " + entityName);
        }
        else
        {
            NAME_TO_CLASS.put(entityName, entityClass);
        }
    }

	public void setOnGround(boolean b) {
		this.onGround = b;
	}
	
	public Entity setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public boolean isDead() {
		return this.isDead;
	}
	
	public void kill() {
		this.isDead = true;
		for(int i = 0; i < 8; i++) {
			level.summon(new EntityParticleDead(), Game.random((int)this.x, (int)this.x + 16, 0), Game.random((int)this.y, (int)this.y + 16, 0));
		}
	}

	public void reset() {

    }
	
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {this.level = level;}
	
	public Rectangle tileB() {
		return new Rectangle(0, 0, 0, 0);
	}

	public Rectangle tileE() {
		return new Rectangle(tileB().x + (int)this.x, tileB().y + (int)this.y, tileB().width, tileB().height);
	}
	
	private int abs(int r) {
		if(r == 0) return 0;
		if(r < 0) return -1;
		return 1;
	}
	
	public String getID() {
		String[] ss = this.toString().split("\\.");
		String s = ss[ss.length-1];
		return s;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getYA() {
		return ya;
	}

	public double getXA() {
		return xa;
	}
}
