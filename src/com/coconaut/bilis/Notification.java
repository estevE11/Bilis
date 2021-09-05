package com.coconaut.bilis;

import com.coconaut.bilis.gfx.Screen;

public class Notification {

    private final int time = 200;
    private int tickTime = 0;
    private boolean show = true;

    private boolean dead = false;

    private double x = Game.WIDTH, y = 1;
    private double targetY = y;
    private double targetX = x - 72;
    private int position;

    public void init(int pos) {
        this.position = pos;
    }

    public void update() {
        this.targetY = this.position * 22;

        if(this.y > this.targetY) this.y-=2;
        if(this.y < this.targetY) this.y+=2;

        if(this.x != this.targetX) {
            if(this.x > this.targetX) this.x-=2;
            if(this.x < this.targetX) this.x+=2;
        }

        this.tickTime++;
        if(this.tickTime >= this.time) {
            this.targetX = Game.WIDTH;
        }

        if(this.x >= Game.WIDTH && this.tickTime >= this.time) this.dead = true;
    }

    public void render(Screen screen) {
        screen.render(Resources.i.spr_notification, x, y);
    }

    public void setPos(int i) {
        this.position = i;
    }

    public boolean isDead() {
        return this.dead;
    }
}
