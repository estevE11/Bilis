package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Gamepad;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.event.KeyEvent;

public class SettingsState extends State {

	private String[] options = {"sfx", Game.lenguage.get("btn_fullscreen") + ": NO", Game.lenguage.get("btn_controls"), Game.lenguage.get("btn_back")};
	private int y = 60;
	private int selected = -1;

	private boolean upd = false;
	private boolean downd = false;
	private boolean leftd = false;
	private boolean rightd = false;
	
	public SettingsState(Game game) {
		super(game);
	}
	
	public void update() {
		this.options[0] = Game.lenguage.get("btn_volume") + ":  " + (int)Sound.volume;
		
		if(!Gamepad.checkGamepad(Gamepad.VK_UP)) {this.upd = false;}
		if(!Gamepad.checkGamepad(Gamepad.VK_DOWN)) {this.downd = false;}
		if(!Gamepad.checkGamepad(Gamepad.VK_LEFT)) {this.leftd = false;}
		if(!Gamepad.checkGamepad(Gamepad.VK_RIGHT)) {this.rightd = false;}

		if(Gamepad.checkGamepad(Gamepad.VK_UP) && !this.upd) {sel(-1); this.upd = true;}
		if(Gamepad.checkGamepad(Gamepad.VK_DOWN) && !this.downd) {sel(1); this.downd = true;}
		if(Gamepad.checkGamepad(Gamepad.VK_LEFT) && !this.leftd) {
			if(selected == 0) {Sound.volDown();}
			if(selected == 1) {game.setFullscreen(!Game.isFullscreen);}
			this.leftd = true;
		}
		
		if(Gamepad.checkGamepad(Gamepad.VK_RIGHT) && !this.rightd) {
			if(selected == 0) {Sound.volUp();}
			if(selected == 1) {game.setFullscreen(!Game.isFullscreen);}
			this.rightd = true;
		}
		
		if(this.selected > options.length) this.selected = 0;
		if(this.selected < -1) this.selected = this.options.length;
		
		if(Gamepad.checkGamepad(Gamepad.VK_A)) {
			if(selected == 1) {
				game.setFullscreen(!Game.isFullscreen);
				Sound.menu_change.play();
			}

			if(selected == 2) {
				game.setState(new ControlsState(game));
				Sound.menu_change.play();
			}

			if(selected == 3) {
				game.setState(new ControlsState(game));
				Sound.menu_change.play();
			}
		}
	}
	
	public void render(Screen screen) {
		screen.renderText("", 0, 0, 30, false);
		for(int i = 0; i < options.length; i++) {
			String ss = options[i];
						
			if(selected == i) screen.renderText("", 0, 0, 35, false);
			else screen.renderText("", 0, 0, 30, false);
			
			if(i == 1) ss = Game.lenguage.get("btn_fullscreen") + ": " + Game.isFullscreen;
			screen.renderText(ss, Game.WIDTH/2 - screen.calcTextWidth(ss)/2, y + (i * 30), selected == i ? 35 : 30, selected == i ? true : false);
		}
	}

	public void onKeyDown(int key) {
		if(key == KeyEvent.VK_UP) sel(-1);
		if(key == KeyEvent.VK_DOWN) sel(1);
		
		if(key == KeyEvent.VK_ENTER) {
			if(selected == 0) {
			}

			if(selected == 1) {
				game.setFullscreen(!Game.isFullscreen);
			}

			if(selected == 2) {
				game.setState(new ControlsState(game));
			}

			if(selected == 3) {
				game.setState(new MainMenuState(game));
			}
		}
		
		if(key == KeyEvent.VK_LEFT) {
			switch(selected) {
			case 0:
				Sound.volDown();
				Sound.menu_change.play();
				break;
			case 1:
				game.setFullscreen(!Game.isFullscreen);
				Sound.menu_change.play();
				break;
			}
		}

		if(key == KeyEvent.VK_RIGHT) {
			switch(selected) {
			case 0:
				Sound.volUp();
				Sound.menu_change.play();
				break;
			case 1:
				game.setFullscreen(!Game.isFullscreen);
				Sound.menu_change.play();
				break;
			}
		}
	}
	
	public void sel(int a) {
		this.selected += a;
		Sound.menu_change.play();
	}

}
