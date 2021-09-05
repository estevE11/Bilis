package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.sound.Sound;

import java.awt.event.KeyEvent;

public class ControlsState extends State {

	private String[] options = {Game.lenguage.get("btn_move_left") + ": ", Game.lenguage.get("btn_move_right") + ": ", Game.lenguage.get("btn_shoot_left") + ": ", Game.lenguage.get("btn_shoot_right") + ": ", Game.lenguage.get("btn_jump") + ": ", "", Game.lenguage.get("btn_back") + ": "};
	private int selected = -1;
	private boolean waiting4key = false;
	private int tickTime = 0;
	
	public ControlsState(Game game) {
		super(game);
	}
	
	public void update() {
		if(waiting4key) {
			this.tickTime++;
		} else this.tickTime = 0;
	}
	
	public void render(Screen screen) {
		String[] keys = {Game.KeyCodeToString(Game.KEY_MOVE_LEFT),
				Game.KeyCodeToString(Game.KEY_MOVE_RIGHT),
				Game.KeyCodeToString(Game.KEY_SHOOT_LEFT),
				Game.KeyCodeToString(Game.KEY_SHOOT_RIGHT),
				Game.KeyCodeToString(Game.KEY_JUMP)};
		for(int i = 0; i < options.length; i++) {
			int x = 10 - (selected == i ? 1 : 0);
			int y = 33 + (30*i) - (selected == i ? 1 : 0);
			screen.renderText(options[i], x, y, 30, selected == i ? (this.tickTime % 60 < 30) : false);
			if(i < 5)screen.renderText(keys[i], 310 - screen.calcTextWidth(keys[i]), y, 30, selected == i ? (this.tickTime % 60 < 30) : false);
		}
	}
	
	public void onKeyDown(int key) {
		if(!waiting4key) {
			if(key == KeyEvent.VK_UP) sel(-1);
			if(key == KeyEvent.VK_DOWN) sel(1);
			if(key == KeyEvent.VK_ENTER) {
				if(selected == 6) {game.setState(new SettingsState(game)); return;}
				waiting4key = true;
			}
		} else {
			switch(selected) {
			case 0:
				Game.KEY_MOVE_LEFT = key;
				waiting4key = false;
				break;
			case 1:
				Game.KEY_MOVE_RIGHT = key;
				waiting4key = false;
				break;
			case 2:
				Game.KEY_SHOOT_LEFT = key;
				waiting4key = false;
				break;
			case 3:
				Game.KEY_SHOOT_RIGHT = key;
				waiting4key = false;
				break;
			case 4:
				Game.KEY_JUMP = key;
				waiting4key = false;
				break;
			case 6:
				game.setState(new SettingsState(game));
				break;
			}
		}
	}
	
	public void onKeyUp(int key) {
		
	}

	public void sel(int a) {
		this.selected += a;
		Sound.menu_change.play();
	}
}
