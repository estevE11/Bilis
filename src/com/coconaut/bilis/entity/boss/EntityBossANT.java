package com.coconaut.bilis.entity.boss;

import com.coconaut.bilis.Log;
import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.entity.projectile.EntityProjectile;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;

import java.awt.*;
import java.util.Random;

public class EntityBossANT extends EntityBoss{
    private Random r = new Random();
    private Sprite spr = Resources.i.spr_boss_hotball;

    private int dir = 0;
    private double speed = 2;

    private int STAGE_ATTACK_01 = 1;

    public EntityBossANT() {
        this.hasGravity = true;
        this.maxHealth = 20;
        this.health = this.maxHealth;
    }

    public void update() {
        super.update();
        updateAI();
        this.move();
    }

    public void updateAI() {
        if(this.currentStage == STAGE_STANDBY) {
            if(this.stageTime > 3*60) {
                changeAttack();
            }
        } else if(this.currentStage == STAGE_ATTACK_01) {
            chargeAttackAI();
        }
    }

    public void chargeAttackAI() {
        if(this.stageTime == 0 || this.stageTime == 1) {
            this.dir = r.nextInt(2);
            if(this.dir == 0) this.xa = -this.speed;
            if(this.dir == 1) this.xa = this.speed;
        }

        if(collision(dir == 0 ? -4 : 4, 0)) {
            this.dir = this.dir == 0 ? 1 : 0;
            if(this.dir == 0) this.xa = -this.speed;
            if(this.dir == 1) this.xa = this.speed;
        }
    }

    public void render(Screen screen) {
        screen.render(this.spr, this.x, this.y, (float)this.dieTick/this.dieTime);
    }

    public void changeAttack() {
        this.currentStage = r.nextInt(2);
        Log.debug("changed to state " + this.currentStage);
        this.stageTime = 0;
    }

    public void onEntityColide(Entity other) {
        if(other.type == EntityType.PROJECTILE) {
            EntityProjectile o = (EntityProjectile) other;
            this.health -= ((Player)o.getOwner()).getDamage() * ((EntityProjectile)other).getDamage();
            if(!(other instanceof EntityLaser))other.kill();
            if(this.health <= 0) this.kill();
        }
    }

    public Rectangle tileB() {
        return new Rectangle(0, 0, 31, 31);
    }
}
