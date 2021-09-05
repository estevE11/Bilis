package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Gamepad;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseMenu {
	private int selected = 0;
	private boolean activated = false;
	private boolean closing = false;
	
	private boolean prb = false;
	private boolean plb = false;
	
	private PauseMenuTab[] tabs = new PauseMenuTab[5];


	private final int targetX = 43, targetY = 14;
	private final int originX = Game.WIDTH, originY = targetY;
	private int x = originX, y = originY;

	//TODO: Fix offset problems (needed to add the offset in some tabs)
	
	public PauseMenu(Game game) {
		tabs[0] = new PauseMenuResume();
		tabs[1] = new PauseMenuControls();
		tabs[2] = new PauseMenuVideo();
		tabs[3] = new PauseMenuAudio();
		tabs[4] = new PauseMenuClose(game);
	}
	
	public void update() {
		if(!activated) {
			if(this.x > targetX) this.x -= 20;
			if(this.y > targetY) this.y -= 1;

			if(this.x <= this.targetX) this.x = this.targetX;
			if(this.y <= this.targetY) this.y = this.targetY;

			if(this.x <= this.targetX && this.y <= this.targetY) this.activated = true;
		} else {
			if(!this.closing) {
				if (Gamepad.checkGamepad(Gamepad.VK_B)||Gamepad.checkGamepad(Gamepad.VK_START)) {
					this.closing = true;
				}

				if (!Gamepad.checkGamepad(Gamepad.VK_RB)) prb = false;
				if (Gamepad.checkGamepad(Gamepad.VK_RB) && !prb) {
					selected++;
					prb = true;
				}

				if (!Gamepad.checkGamepad(Gamepad.VK_LB)) plb = false;
				if (Gamepad.checkGamepad(Gamepad.VK_LB) && !plb) {
					selected--;
					plb = true;
				}

				if (selected > 4) selected = 0;
				if (selected < 0) selected = 4;

				this.tabs[this.selected].update();
			} else {
				if (this.x < this.originX) this.x += 20;
				if (this.y < this.originY) this.y += 3;

				if (this.x >= this.originX) this.x = this.originX;
				if (this.y >= this.originY) this.y = this.originY;

				if(this.x == this.originX && this.y == originY) close();
			}
		}
	}
	
	public void render(Screen screen) {
		screen.renderOnHUD(Resources.i.pausemenu_back, 0, 0);
		screen.renderOnHUD(Resources.i.pausemenu, x, y);
		
		screen.fillOnHUD(x + 19 + (selected * 41), y + 33, 36, 4, Color.black);

		this.tabs[this.selected].render(this.x, this.y, screen);
	}
	
	public void onKeyDown(int key) {
		if(key == KeyEvent.VK_ESCAPE) {
			this.closing = true;
		}

		if(key == KeyEvent.VK_D) selected++;
		if(key == KeyEvent.VK_A) selected--;

		if (selected > 4) selected = 0;
		if (selected < 0) selected = 4;

		this.tabs[this.selected].onKeyDown(key);
	}

	public void onKeyUp(int key) {
		this.tabs[this.selected].onKeyUp(key);
	}

	public void close() {
		this.x = this.originX;
		this.y = this.originY;
		GameState.togglePause();
		this.closing = false;
		this.activated = false;
	}

	public class PauseMenuTab {
		public void update() {

		}

		public void render(int offx, int offy, Screen screen) {

		}

		public void onKeyDown(int key) {

		}

		public void onKeyUp(int key) {

		}
	}
	
	public class PauseMenuResume extends PauseMenuTab {
		public PauseMenuResume() {
		}
		
		public void update() {
			if(Gamepad.checkGamepad(Gamepad.VK_A)) {
				GameState.togglePause();
			}
		}
		
		public void render(int offx, int offy, Screen screen) {
			screen.renderText("", 0, 0, 30, false);
			screen.renderText(Game.lenguage.get("btn_resume"), offx + 320/2 - screen.calcTextWidth("RESUME")/2 - 43, offy + 140 - 14, 30, true);
		}
		
		public void onKeyDown(int key) {
			if(key == KeyEvent.VK_ENTER) GameState.togglePause();
		}
	}
	
	public class PauseMenuControls extends PauseMenuTab {
		public void render(int offx, int offy, Screen screen) {
			int col = 1;

			if(Game.hasJoystick) {
				if(Game.gamepad.getName().equals(Gamepad.NAME_XBOX)) col = 0;
				if(Game.gamepad.getName().equals(Gamepad.NAME_PS4)) col = 1;
			}

			render(offx, offy, Game.lenguage.get("info_jump") + ": ", 70 - 14, col, 0, 1, Game.KEY_JUMP, screen);
			render(offx, offy, Game.lenguage.get("info_move") + ": ", 85 - 14, col, 9, 1, Game.KEY_MOVE_RIGHT, screen);
			screen.renderText(Game.KeyCodeToString(Game.KEY_MOVE_LEFT), offx + 171 + screen.calcTextWidth(Game.KeyCodeToString(Game.KEY_MOVE_RIGHT)) + 7 - 43, offy + 96 - 14, 10, false);

			render(offx, offy, Game.lenguage.get("info_shoot_right") + ": ", 100 - 14, col, 13, 1, Game.KEY_SHOOT_RIGHT, screen);
			render(offx, offy, Game.lenguage.get("info_shoot_left") + ": ", 115 - 14, col, 12, 1, Game.KEY_SHOOT_LEFT, screen);
		}
		
		public void render(int offx, int offy, String text, int y, int xx, int yy, int col, int key, Screen screen) {
			int x = 60 * col;
			screen.renderText(text, offx + x - 43, offy + y + 11, 10, false);
			x = 150 * col;
			screen.renderOnHUD(Resources.i.contBtn, offx + x - 43, offy + y, xx*16, yy*16, 16, 16);
			screen.renderText(Game.KeyCodeToString(key), offx + x + 20 - 43, offy + y + 11, 10, false);
		}

		public void onKeyDown(int key) {
			
		}
	}
	
	public class PauseMenuVideo extends PauseMenuTab {
		public void render(int offx, int offy, Screen screen) {
			screen.renderText("Not available yet! WIP", offx + 100, offy + 100, 10, false);
		}
		
		public void onKeyDown(int key) {
			
		}
	}

	public class PauseMenuAudio extends PauseMenuTab {
		private String[] options = {Game.lenguage.get("btn_audio") + ": ON", Game.lenguage.get("btn_volume") + ": " + Sound.volume};
		private int selected = -1;
		
		public void update() {
			this.options[1] = "Volume: " + (int)Sound.volume;
			if(selected > options.length) selected = 0;
			if(selected < 0) selected = options.length;
		}

		public void render(int offx, int offy, Screen screen) {
			for(int i = 0; i < options.length; i++) {
				screen.renderText(options[i], offx + 60, offy + 80 + (i * 20), 20, i == selected ? true : false);
			}
		}
		

		public void onKeyDown(int key) {
			if(key == KeyEvent.VK_DOWN) {
				this.selected++;
				Sound.menu_change.play();
			}
			else
			if(key == KeyEvent.VK_UP) { 
				this.selected--;
				Sound.menu_change.play();
			}
			else
			if(selected == 1) {
				if(key == KeyEvent.VK_LEFT) {
					Sound.volDown();
					Sound.menu_change.play();
				} else
				if(key == KeyEvent.VK_RIGHT) {
					Sound.volUp();
					Sound.menu_change.play();
				}
			}
		}
	}

	public class PauseMenuClose extends PauseMenuTab {
		private Game game;

		private String[] options = {Game.lenguage.get("btn_main_menu"), Game.lenguage.get("btn_exit_game")};
		private int selected = -1;
		
		public PauseMenuClose(Game game) {
			this.game = game;
		}
		
		public void update() {
			if(Gamepad.checkGamepad(Gamepad.VK_UP)) {
				selected--;
				Sound.menu_change.play();
			}

			if(Gamepad.checkGamepad(Gamepad.VK_DOWN)) {
				selected++;
				Sound.menu_change.play();
			}

			if(Gamepad.checkGamepad(Gamepad.VK_A)) {
				if(selected == 0) game.setState(new MainMenuState(game));
				if(selected == 1) Log.shutDown(1);
			}
			
			if(selected < 0) selected = options.length-1;
			if(selected > options.length-1) selected = 0;
		}
		
		public void render(int offx, int offy, Screen screen) {
			screen.renderText("", 0, 0, 35, false);
			for(int i = 0; i < options.length; i++) {				
				screen.renderText(options[i], offx + Game.WIDTH/2 - screen.calcTextWidth(options[i])/2 - (i == selected ? 1 : 0), offy + 130 + (i * 35) - (i == selected ? 1 : 0), 35, i == selected ? true : false);
			}
		}

		public void onKeyDown(int key) {
			if(key == KeyEvent.VK_UP) {
				selected--;
				Sound.menu_change.play();
			}

			if(key == KeyEvent.VK_DOWN) {
				selected++;
				Sound.menu_change.play();
			}
			
			if(key == KeyEvent.VK_ENTER) {
				if(selected == 0) game.setState(new MainMenuState(game));
				if(selected == 1) Log.shutDown(1);
			}
		}
	}
}
