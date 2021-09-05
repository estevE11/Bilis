package com.coconaut.bilis.state;

import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;

public class DeathMenu {
    private Entity killer;
    private int kills, money;
    private String level;

    public void init(Entity killer, int kills, int money, String level) {
        this.killer = killer;
        this.kills  = kills;
        this.money = money;
        this.level  = level;
    }

    public void update() {

    }

    public void render(Screen screen) {

    }
}
