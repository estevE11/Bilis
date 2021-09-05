 package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Gamepad;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.event.KeyEvent;

public class MainMenuState extends State {
	public Button[] buttons = {new Button(Game.lenguage.get("btn_play"), 30, 140), new Button(Game.lenguage.get("btn_config"), 30, 170), new Button(Game.lenguage.get("btn_exit"), 30, 200)};
	private int selected = -1;
	private boolean upd = false;
	private boolean downd = false;
	private int x = 30, y = 140;

	private Animation anim;
	
	public MainMenuState(Game game) {
		super(game);
		this.anim = new Animation(Resources.i.spr_anim_player_idle, 16, 16, 3, 15);
		this.anim.play();
	}
	
	public void update() {
		if(!Gamepad.checkGamepad(Gamepad.VK_UP)) {this.upd = false;}
		if(!Gamepad.checkGamepad(Gamepad.VK_DOWN)) {this.downd = false;}

		if(Gamepad.checkGamepad(Gamepad.VK_UP) && !this.upd) {selMv(-1); this.upd = true;}
		if(Gamepad.checkGamepad(Gamepad.VK_DOWN) && !this.downd) {selMv(1); this.downd = true;}
		
		if(this.selected > buttons.length) this.selected = 0;
		if(this.selected < -1) this.selected = this.buttons.length;

		for(int i = 0; i < buttons.length; i++) {
			if(i == this.selected) {this.buttons[i].selected = true; continue;}
			this.buttons[i].selected = false;
		}

		if(Gamepad.checkGamepad(Gamepad.VK_A)) {
			if(selected == 0) {
				game.setState(game.getGameState());
			}

			if(selected == 1) {
				game.setState(new SettingsState(game));
			}

			if(selected == 2) {
				Log.shutDown(1);
			}
		}

		for(int i = 0; i < buttons.length; i++) {
			this.buttons[i].update();
		}

		this.anim.update();
	}
	
	public void render(Screen screen) {
		screen.render(Resources.i.maintitle, Game.WIDTH/2 - 171/2, 30);
		
		for(int i = 0; i < buttons.length; i++) {
			this.buttons[i].render(screen);
		}

		this.anim.draw(200, 110, 16*5, 16*5, screen);
	}

	public void selMv(int q) {
		this.selected += q;
		Sound.menu_change.play();
	}
	
	public void onKeyDown(int key) {
		if(key == KeyEvent.VK_UP) selMv(-1);
		if(key == KeyEvent.VK_DOWN) selMv(1);
		
		if(key == KeyEvent.VK_ENTER) {
			if(selected == 0) {
				game.setState(game.getGameState());
			}

			if(selected == 1) {
				game.setState(new SettingsState(game));
			}

			if(selected == 2) {
				Log.shutDown(1);
			}
		}
	}

	public void onKeyUp(int key) {
		
	}

	public class Button {
		private int x, y;
		private int originX;
		private int targetX;
		private String name;

		public boolean selected = false;

		public Button(String name, int x, int y) {
			this.x = x;
			this.y = y;
			this.originX = x;
			this.targetX = x + 15;

			this.name = name;
		}

		public void update() {
			if(selected) {
				if(this.x < this.targetX) this.x += 2;
				if(this.x > this.targetX) this.x = this.targetX;
			} else {
				if(this.x > this.originX) this.x -= 1;
				if(this.x < this.originX) this.x = this.originX;
			}
		}

		public void render(Screen screen) {
			screen.renderText(this.name, this.x, this.y, 30, true);
		}
	}
}
