package com.coconaut.bilis.state;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Gamepad;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.gfx.Screen;

public class InitState extends State{

	private int tickTime = 1;
	private boolean up = true;
	
	public InitState(Game game) {
		super(game);
	}
	
	public void update() {
		if(up) this.tickTime++; else this.tickTime--;
		if(this.tickTime >= 150)up = false;
		if(tickTime <= 0) game.setState(new MainMenuState(game));
		if(Gamepad.checkGamepad(Gamepad.VK_A)) game.setState(new MainMenuState(game));
	}
	
	public void render(Screen screen) {
		float a = tickTime/100f;
		if(a > 1) a = 1;
		screen.render(Resources.i.logo_pat, Game.WIDTH/2 - 160/2, Game.HEIGHT/2 - 160/2, 160, 160, a);
	}
	
	public void onKeyDown(int key) {
		game.setState(new MainMenuState(game));
	}

}
