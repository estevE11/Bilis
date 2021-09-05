package com.coconaut.bilis.level;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.entity.boss.EntityBoss;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.tile.Tile;
import com.coconaut.bilis.state.GameState;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Level {		
	private LinkedList<Entity> entities = new LinkedList<Entity>();
	private LinkedList<Entity> e_enemies = new LinkedList<Entity>();
	private LinkedList<Entity> e_projectiles = new LinkedList<Entity>();
	private LinkedList<Entity> e_particles = new LinkedList<Entity>();
	private Player player;

	private Room rooms[] = new Room[20];
	private int currentRoom = 0;

	public Tile tiles[][];
	private GameState gamest;
	private int w, h;
	
	private String name = "level:unknown";

	boolean onTransition = false;
	int transTime = 30;
	int transTick = 0;
    int transState = 0;

	public void init(GameState gamest) {
		this.w = 20;
		this.h = 15;
		this.tiles = new Tile[this.w][this.h];
		this.gamest = gamest;
		this.player = gamest.getPlayer();
				
		Log.debug("level init " + w + ", " + h);
	}
	
	public void update() {		
        if(this.onTransition) {
            if(this.transState == 0) {
                if(this.transTick < this.transTime) {
                    this.transTick++;
                } else if(this.transTick >= this.transTime) {
                    this.transState = 1;
                    this._nextRoom();
                }
            } else if(this.transState == 1) {
                if(this.transTick > 0) {
                    this.transTick--;
                } else if(this.transTick <= 0) {
                    this.transState = 0;
                    this.onTransition = false;
                }
            }
            return;
        }
		for(int i = 0; i < entities.size(); i++) {
			checkCollision(this.gamest.getPlayer(), this.entities.get(i));
			for(int j = i; j < entities.size(); j++)
				if(this.entities.get(i) != this.entities.get(j))checkCollision(this.entities.get(i), this.entities.get(j));
			this.entities.get(i).update();
			if(this.entities.get(i).isDead()) this.kill(this.entities.get(i));
		}
		if(this.e_enemies.size() < 1) openRoom();
	}
	
	public void render(Screen screen) {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				this.tiles[x][y].render(x, y, this, screen);
			}
		}

		for(int i = 0; i < entities.size(); i++) {
			this.entities.get(i).render(screen);
		}

		if(this.onTransition) {
		    float a = (float)this.transTick / (float)this.transTime;
		    Log.debug(a);
		    screen.render(Resources.i.trans, 0, 0, a);
		}
    }

    public void nextRoom() {
        this.onTransition = true;
    }

    public void _nextRoom() {
        this.currentRoom++;
        if(this.currentRoom < 12) this.changeRoom(this.currentRoom);
        else {
            if(this.currentRoom == 12) {
                this.changeRoom(Room.preBossRoom);
            } else if(this.currentRoom == 13) {
                this.changeRoom(Room.bossRoom);
                this.summon(EntityBoss.getRandomBoss(), 100, 100);
            } else if(this.currentRoom == 14) {
                this.nextLevel();
            }
        }
        this.gamest.getPlayer().setPosition(32, 10*16);
    }

    public void prevRoom() {
        this.currentRoom--;
        this.changeRoom(this.currentRoom);
    }

	public void changeRoom(int i) {
        this.killAll();
        for(int y = 0; y < this.h; y++) {
            for(int x = 0; x < this.w; x++) {
                this.setTile(this.rooms[i].getTile(x, y), x, y);
            }
        }

        if(i != 0) {
            this.setTile(Tile.barrier, 0, 9);
            this.setTile(Tile.barrier, 0, 10);
            this.setTile(Tile.barrier, 19, 9);
            this.setTile(Tile.barrier, 19, 10);
//            this.setTile(Tile.air, 19, 8);
//            this.setTile(Tile.air, 18, 8);
        }

        this.rooms[i].summonEntities(this);
    }

    public void changeRoom(Room room) {
        this.killAll();
        for(int y = 0; y < this.h; y++) {
            for(int x = 0; x < this.w; x++) {
                this.setTile(room.getTile(x, y), x, y);
            }
        }

        this.setTile(Tile.barrier, 0, 9);
        this.setTile(Tile.barrier, 0, 10);
        this.setTile(Tile.barrier, 19, 9);
        this.setTile(Tile.barrier, 19, 10);
//        this.setTile(Tile.air, 19, 8);
//        this.setTile(Tile.air, 18, 8);

        room.summonEntities(this);
    }

    public void openRoom() {
	    if(currentRoom == 0) return;
        this.setTile(Tile.air, 0, 9);
        this.setTile(Tile.air, 0, 10);
        this.setTile(Tile.air, 19, 9);
        this.setTile(Tile.air, 19, 10);
    }

    public void openBossRoom() {
        this.setTile(Tile.air, 0, 9);
        this.setTile(Tile.air, 0, 10);
        this.setTile(Tile.air, 19, 9);
        this.setTile(Tile.air, 19, 10);
    }

    public void nextLevel() {
        Game.level_level++;
        if(Game.level_level>3) {
            Game.level_level = 1;
            Game.level_world++;
        }

        this.currentRoom = 0;

        generateRandomLevel();
        changeRoom(0);
    }

	public void summon(Entity e, double x, double y) {
        e.setPosition(x, y);
		e.init(this);
		this.entities.add(e);

        if(e.type == Entity.EntityType.PLAYER) {
            this.player = (Player)e;
        }
		if(e.type == Entity.EntityType.ENEMY) {
			this.e_enemies.add(e);
		}
		if(e.type == Entity.EntityType.PROJECTILE) {
			this.e_projectiles.add(e);
		}
		if(e.type == Entity.EntityType.PARTICLE) {
			this.e_particles.add(e);
		}

	//	if(e.type == Entity.EntityType.ENEMY) Log.info(e.getID() + " summoned at " + x + "," + y);
	}

	public void kill(Entity e) {
		this.entities.remove(e);
		if(e.type == Entity.EntityType.ENEMY) {
			this.e_enemies.remove(e);
		}
		if(e.type == Entity.EntityType.PROJECTILE) {
			this.e_projectiles.remove(e);
		}
		if(e.type == Entity.EntityType.PARTICLE) {
			this.e_particles.remove(e);
		}
		e.reset();
	}

	public void killAll() {
	    for(int i = 0; i < entities.size(); i++) {
	        kill(entities.get(i));
        }
    }

	public void checkCollision(Entity e0, Entity e1) {
		Rectangle r0 = new Rectangle(e0.tileE().x + (int)Game.getCamera().offsetX, e0.tileE().y + (int)Game.getCamera().offsetY, e0.tileE().width, e0.tileE().height);
		Rectangle r1 = new Rectangle(e1.tileE().x + (int)Game.getCamera().offsetX, e1.tileE().y + (int)Game.getCamera().offsetY, e1.tileE().width, e1.tileE().height);

		if(r0.intersects(r1)) {
			e0.onEntityColide(e1);
			e1.onEntityColide(e0);
		}
	}

	public Entity entityCollision(int x, int y, int w, int h, Entity from) {
		Rectangle rect = new Rectangle(x + (int)Game.getCamera().offsetX, y + (int)Game.getCamera().offsetY, w, h);
		for(int i = 0; i < entities.size(); i++) {
			Entity e1 = entities.get(i);
			if(from == e1) continue;
			Rectangle r1 = new Rectangle((int)e1.getX() + (int)Game.getCamera().offsetX, (int)e1.getY() + (int)Game.getCamera().offsetY, e1.tileE().width, e1.tileE().height);

			if(rect.intersects(r1)) {
				return e1;
			}
		}
		return null;
	}

	public Entity entityCollision_proj(int x, int y, int w, int h, Entity from) {
		Rectangle rect = new Rectangle(x + (int)Game.getCamera().offsetX, y + (int)Game.getCamera().offsetY, w, h);
		for(int i = 0; i < e_projectiles.size(); i++) {
			Entity e1 = e_projectiles.get(i);
			if(from == e1) continue;
			Rectangle r1 = new Rectangle((int)e1.getX() + (int)Game.getCamera().offsetX, (int)e1.getY() + (int)Game.getCamera().offsetY, e1.tileE().width, e1.tileE().height);

			if(rect.intersects(r1)) {
				return e1;
			}
		}
		return null;
	}

	public Entity entityCollision_enemy(int x, int y, int w, int h, Entity from) {
		Rectangle rect = new Rectangle(x + (int)Game.getCamera().offsetX, y + (int)Game.getCamera().offsetY, w, h);
		for(int i = 0; i < e_enemies.size(); i++) {
			Entity e1 = e_enemies.get(i);
			if(from == e1) continue;
			Rectangle r1 = new Rectangle((int)e1.getX() + (int)Game.getCamera().offsetX, (int)e1.getY() + (int)Game.getCamera().offsetY, e1.tileE().width, e1.tileE().height);

			if(rect.intersects(r1)) {
				return e1;
			}
		}
		return null;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getPixelWidth() {
		return w*16;
	}

	public int getPixelHeight() {
		return h*16;
	}

	public GameState getGameState() {
		return gamest;
	}

	public LinkedList<Entity> getEntities() {
		return entities;
	}

	public void setRoomAt(int i, Room room) {
        this.rooms[i] = room;
	}

	public void setTile(int id, int tx, int ty) {
		this.tiles[tx][ty] = Tile.getTileById(id);
	}

	public void setTile(Tile tile, int tx, int ty) {
		this.tiles[tx][ty] = tile;
	}

	public int getTileID(int tx, int ty) {
		if(tx < 0 || ty < 0 || tx >= this.getWidth() || ty >= this.getHeight()) return Tile.air.id;
		return tiles[tx][ty].id;
	}

    public int getTileIDFT(int tx, int ty) {
        if(tx < 0 || ty < 0 || tx >= this.getWidth() || ty >= this.getHeight()) return Tile.rock.id;
        return tiles[tx][ty].id;
    }

	public Tile getTileFT(int sx, int sy) {
		int tx = (int) (sx/16);
		int ty = (int) (sy/16);
		if(tx < 0 || ty < 0 || tx >= this.getWidth() || ty >= this.getHeight()) return Tile.rock;
		return tiles[tx][ty];
	}

    public Tile getTile(int sx, int sy) {
        int tx = (int) (sx/16);
        int ty = (int) (sy/16);
        if(tx < 0 || ty < 0 || tx >= this.getWidth() || ty >= this.getHeight()) return Tile.air;
        return tiles[tx][ty];
    }

	public void generateRandomLevelFromSeed(int seed) {
		Random r = new Random(seed);
		int[] usedRooms = {69,69,69,69,69,69,69,69,69,69};

		this.setRoomAt(0, Room.mainRoom);
		for(int a = 1; a < 11; a++) {
			boolean cont = true;
			int rm = -1;
			while(cont) {
				rm = r.nextInt(Room.availableRooms);
				boolean c = false;

				for(int i = 0; i < usedRooms.length && !c; i++) {
					if(usedRooms[i] == rm) c = true;
				}

				if(!c) {
					usedRooms[a-1] = rm;
					cont = false;
				}
			}

			if(rm == -1) Log.error("rm is equals to " + rm);
			Log.debug(rm + " = "+Room.rooms[rm]);
			this.setRoomAt(a, Room.rooms[rm]);
		}
		this.setRoomAt(11, Room.lastRoom);
		this.changeRoom(0);
	}

	public void generateRandomLevel() {
		Random r = new Random();
		int[] usedRooms = new int[10];

		this.setRoomAt(0, Room.mainRoom);
		for(int a = 1; a < 11; a++) {
			boolean cont = true;
			int rm = -1;
			while(cont) {
				rm = r.nextInt(Room.availableRooms);
				boolean c = false;

				for(int i = 0; i < usedRooms.length && !c; i++) {
					if(usedRooms[i] == rm) c = true;
				}

				if(!c) {
					usedRooms[a-1] = rm;
					cont = false;
				}
			}

			if(rm == -1) Log.error("rm is equals to " + rm);
			Log.debug(rm + " = "+Room.rooms[rm]);
			this.setRoomAt(a, Room.rooms[rm]);
		}
		this.setRoomAt(11, Room.lastRoom);
		this.changeRoom(0);
	}
}
