package com.coconaut.bilis.entity.item;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.gfx.Screen;

public class EntityItemPowerRock extends EntityItem {

    public EntityItemPowerRock() {
        super();
        this.spr = Resources.i.spr_item_power_rock;

        this.name = "Power Rock";
    }

    public void update() {
        super.update();
    }

    public void render(Screen screen) {
        super.render(screen);
    }

    public void onCollect(Player player) {
        super.onCollect(player);
        player.enableLaser();
        this.kill();
    }
}
