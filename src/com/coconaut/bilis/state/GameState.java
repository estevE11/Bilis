package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Game.Version;
import com.coconaut.bilis.Gamepad;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.entity.EntityWarp;
import com.coconaut.bilis.entity.boss.EntityBoss;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.Level;
import com.coconaut.bilis.level.Room;
import com.coconaut.bilis.level.tile.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class GameState extends State implements MouseMotionListener, MouseListener{
	private Random r = new Random();
	
	private Level level;
	private Player player;

	public static boolean paused = false;
	public static boolean pausing = false;
	private boolean pauseB = false;
	
	private boolean debug = false;
	private int mx = 0, my = 0;
	private Color mc = Color.red;
	
	private int[] usedRooms = new int[10];
	
	private PauseMenu pausemenu;
	
	public GameState(Game game) {
		super(game);
		this.player = new Player();
		game.getConsole().setPlayer(player);
		this.level = new Level();
		
		this.level.init(this);
		this.player.init(this.level);
		
		this.pausemenu = new PauseMenu(game);

		Tile.init();
		Log.debug("Loading rooms...");
		Room.loadRooms();
		Log.info("Rooms loaded");
		
		level.generateRandomLevel();

		EntityWarp endwrp = new EntityWarp() {
			public void action(Player player) {
				Level level = player.getLevel();
				player.setPosition(0, 16*10);
				GameState gs = level.getGameState();
				level = new Level();
				level.init(gs);
				level.setRoomAt(0, Room.preBossRoom);
				player.init(level);
				level.setName("level:preboss");
				gs.setLevel(level);
			}
		};
		endwrp.setSize(3, 32);
		level.summon(endwrp, 3835, 150);
	}

	public void update() {
		if(!paused) {
			Game.camera.update(player);
			player.update(level);
			level.update();
			if (!Gamepad.checkGamepad(Gamepad.VK_START)) pauseB = false;
			if (Gamepad.checkGamepad(Gamepad.VK_START) && !pauseB) {
				this.paused = true;
				pauseB = true;
			}
		} else {
			pausemenu.update();
		}
		
	}
	
	public void render(Screen screen) {
		level.render(screen);
		player.render(screen);

		renderHUD(screen);
		
		if(paused) {
			pausemenu.render(screen);
		}
		
		if(this.debug) {
			String s = "X: " + player.getX() + " Y:" + player.getY();
			screen.renderTextPlain("", 50, 50, 10, false);
			screen.fillOnHUD(50, 40, screen.calcTextWidth(s) + 4, screen.calcTextHeight(s) + 4, Color.black);
			screen.renderTextPlain(s, 50, 50, 10, false);
			screen.fillOnHUD(mx, my, 1, 1, mc);

			String ss = "FPS: " + Game.ffps;
			int x = 50, y = 50;
			screen.renderTextPlain("", 50, 50, 10, false);
			screen.fillOnHUD(x, y, screen.calcTextWidth(ss) + 4, screen.calcTextHeight(ss) + 4, Color.black);
			screen.renderTextPlain(ss, x, y+10, 10, false);

			String sss = "Tile: " + level.getTile(this.mx + (int)-Game.getCamera().offsetX, this.my + (int)-Game.getCamera().offsetY);
			screen.renderTextPlain("", 50, 50, 10, false);
			screen.fillOnHUD(x, y+10, screen.calcTextWidth(sss) + 4, screen.calcTextHeight(sss) + 4, Color.black);
			screen.renderTextPlain(sss, x, y+20, 10, false);

			String sm = "MTX: " + (this.mx + (int)-Game.getCamera().offsetX)/16 + ", MTY: " + (this.my + (int)-Game.getCamera().offsetY)/16;
			screen.renderTextPlain("", 50, 50, 10, false);
			screen.fillOnHUD(x, y+20, screen.calcTextWidth(sm) + 4, screen.calcTextHeight(sm) + 4, Color.black);
			screen.renderTextPlain(sm, x, y+30, 10, false);
		}
	}
	
	
	public void renderHUD(Screen screen) {
		screen.renderHealthBar(player.getHealth(), player.getMaxHealth(), 2, 2, 100, 18, true);
	}
	
	public void setLevel(Level level) {
		this.level = level;
		if(this.level.getName().equals("level:preboss")) {
			EntityWarp endwrp = new EntityWarp() {
				public void action(Player player) {
					Level level = player.getLevel();
					player.setPosition(2*16, 170);
					GameState gs = level.getGameState();
					level = new Level();
					level.init(gs);
					level.setRoomAt(0, Room.bossRoom);
					player.init(level);
					level.setName("level:boss");

					level.summon(EntityBoss.getRandomBoss(), 20*16/2, 15*16/2);

					gs.setLevel(level);
				}
			};
			endwrp.setSize(5, 200);
			this.level.summon(endwrp, 315, 100);
		} else if(this.level.getName().equals("level:boss")) {
			EntityWarp nextLvlWarp = new EntityWarp() {
				public void action(Player player) {
					Game.getCamera().reset();
					Level level = player.getLevel();
					player.setPosition(3 * 16, 170);

					GameState gs = level.getGameState();

					level = new Level();
					level.init(gs);
					gs.setLevel(level);

					player.init(level);

					level.generateRandomLevelFromSeed(8888);

					level.setName("level:unknown");
					level.nextLevel();

				}
			};
			nextLvlWarp.setSize(100, 5);
			this.level.summon(nextLvlWarp, 100, 230);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static void togglePause() {
		paused = !paused;
	}
	
	public void onKeyDown(int key) {
		this.player.onKeyDown(key);
		if(key == KeyEvent.VK_ESCAPE && !this.paused) {
			this.paused = true;
		} else if(paused) pausemenu.onKeyDown(key);

        if(key == KeyEvent.VK_U) {
            Game.addNotification();
        }
	}

	public void onKeyUp(int key) {
		this.player.onKeyUp(key);
		if(paused) pausemenu.onKeyUp(key);
		if((Game.bootVersion == Version.DEBUG || Game.bootVersion == Version.DEBUG_LINUX) && key == KeyEvent.VK_F2) this.debug = !this.debug;
	}

	public void mouseDragged(MouseEvent e) {
		this.mc = Color.blue;
		this.mx = e.getX()/Game.SCALE;
		this.my = e.getY()/Game.SCALE;
	}

	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX()/Game.SCALE;
		this.my = e.getY()/Game.SCALE;
	}

	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		this.mc = Color.red;
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}


}
