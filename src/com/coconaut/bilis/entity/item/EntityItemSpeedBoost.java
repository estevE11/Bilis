package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;

public class EntityItemSpeedBoost extends EntityItem{
	
	public EntityItemSpeedBoost() {
		super();
		this.spr = Resources.i.spr_item_speed_boost;
		
		this.name = "Speed Boost";
	}
	
	public void update() {
		super.update();
	}
	
	public void render(Screen screen) {
		super.render(screen);
	}
	
	public void onCollect(Player player) {
		super.onCollect(player);
		player.addSpeed(0.6);
		this.kill();
	}
}

