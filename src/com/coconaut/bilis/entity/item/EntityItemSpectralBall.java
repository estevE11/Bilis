package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.Level;

public class EntityItemSpectralBall extends EntityItem {
	
	public EntityItemSpectralBall() {
		super();
		this.spr = Resources.i.spr_item_spectral_ball;
		
		this.name = "Spectral Slime Balls";
	}
	
	public void update() {
		super.update();
	}
	
	public void render(Screen screen) {
		super.render(screen);
	}
	
	public void onCollect(Player player) {
		super.onCollect(player);
		player.setSpectralBalls(true);
		this.kill();
	}
}
