package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;

public class EntityItemBallsUp extends EntityItem {
	public EntityItemBallsUp() {
		super();
		this.spr = Resources.i.spr_item_balls_up;
		
		this.name = "Balls Up";
	}
	
	public void update() {
		super.update();
	}
	
	public void render(Screen screen) {
		super.render(screen);
	}
	
	public void onCollect(Player player) {
		super.onCollect(player);
		player.setFireRate(20);
		this.kill();
	}
}
