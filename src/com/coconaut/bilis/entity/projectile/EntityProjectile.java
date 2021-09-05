package com.coconaut.bilis.entity.projectile;

import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Screen;

public class EntityProjectile extends Entity {
    protected int damage;
    protected Entity owner;

    public EntityProjectile(Entity owner) {
        this.owner = owner;
    }

    public void update() {

    }

    public void render(Screen screen) {

    }

    public Entity getOwner() {
        return this.owner;
    }

    public int getDamage() {
        return this.damage;
    }
}
