package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;

public class EntityItemJumpBoost extends EntityItem {
	public EntityItemJumpBoost() {
		super();
		this.spr = Resources.i.spr_item_jump_boost;
		
		this.name = "Jump Boost";
	}
	
	public void update() {
		super.update();
	}
	
	public void render(Screen screen) {
		super.render(screen);
	}
	
	public void onCollect(Player player) {
		super.onCollect(player);
		player.setJumpSpeed(-8);
		this.kill();
	}
}
