package com.coconaut.bilis.entity;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.entity.projectile.EntitySlimeBall;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.util.Random;

public class EntityEdust extends Entity{
    private int tickTime = 0;
    private Random r = new Random();
    private int health = 2;
    private double kx = 0;

    private double eye_posX = 0, eye_posY = 0;

    private Sprite spr;

    public EntityEdust() {
        this.type = EntityType.ENEMY;
        this.cmdName = "entity:edust";
        this.spr = Resources.i.spr_edust;
    }

    public void update() {
        this.tickTime++;
        if(this.health <= 0) {this.kill(); return;}

        //Follow player
        Player player = level.getPlayer();
        double dx = ((player.getX() + 8) - (this.x + 4));
        double dy = ((player.getY() + 8) - (this.y + 4));
        double angle = Math.atan2(dy, dx);

        this.xa = Math.cos(angle) * 2;
        this.ya = Math.sin(angle) * 2;

        this.move();
        this._move(this.kx, 0);

        this.updateEyePosition(angle);

        if(this.kx > 0) this.kx-=0.5;
        if(this.kx < 0) this.kx+=0.5;
    }

    public void render(Screen screen) {
        this.spr.draw((int)this.x, (int)this.y, screen);
        double a = Math.sin(this.tickTime/4);
        if(a > 0)
            screen.fill(this.x + eye_posX + 4, this.y + eye_posY + 4, 1, 1, Color.red);
        else
            screen.fill(this.x + eye_posX + 4, this.y + eye_posY + 4, 1, 1, Color.white);
    }

    public void updateEyePosition(double angle) {
        this.eye_posX = Math.cos(angle) * 2;
        this.eye_posY = Math.sin(angle) * 2;
    }

    public void onEntityColide(Entity other) {
        if(other.type == Entity.EntityType.PROJECTILE) {
            this.health--;
            if(other instanceof EntitySlimeBall) {
                if(((EntitySlimeBall) other).dir() == -1) this.kx = -5;
                if(((EntitySlimeBall) other).dir() == 1) this.kx = 5;
            }
            Sound.enemy_hurt.play();
            if(!(other instanceof EntityLaser)) other.kill();
        }
    }

    public void reset() {
        this.health = 2;
    }

    public Rectangle tileB() {
        return new Rectangle(0, 0, 8, 8);
    }
}
