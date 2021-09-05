package com.coconaut.bilis.entity.projectile;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;

public class EntityBonemerang extends EntityProjectile {

    private Animation anim;

    private boolean isSpectral = false;
    private int dir = 0;
    private double forceBack = 0.5;
    private double initSpeed = 10;

    public EntityBonemerang(Entity owner, int dir, boolean spectral) {
        super(owner);
        this.type = Entity.EntityType.PROJECTILE;

        this.dir = dir;
        this.isSpectral = spectral;

        this.xa = this.initSpeed * this.dir;

        anim = new Animation(Resources.i.spr_bonemerang, 16, 16, 4, 4);
        anim.play();

        this.damage = 1;
    }

    public void update() {
        this.xa -= this.forceBack;
    }

    public void render(Screen screen) {
        anim.draw((int) this.x, (int) this.y, screen, this.dir);
    }
}
