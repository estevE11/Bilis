package com.coconaut.bilis.entity.boss;

import com.coconaut.bilis.entity.mob.Mob;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.level.tile.Tile;

public class EntityBoss extends Mob {
	protected boolean dead = false;

	protected int dieTime = 60;
	protected int dieTick = this.dieTime;

    protected int maxHealth = 20, health = maxHealth;

	protected int STAGE_STANDBY = 0;
	protected int currentStage = 0;
	protected int stageTime = 0;

	public EntityBoss() {
		this.type = EntityType.BOSS;
	}

	public void update() {
	    super.update();

	    this.stageTime++;

		if(this.dead) {
			this.dieTick--;
			if(this.dieTick <= 0) {
				this.kill();
			}
		}
	}
	
	public void render(Screen screen) {
		
	}
	
	public static EntityBoss getRandomBoss() {
		return new EntityBossANT();
	}

	public void onDie() {
		if(level.getName().equals("level:boss")) {
			this.level.setTile(Tile.air, 9, 14);
			this.level.setTile(Tile.air, 10, 14);
			this.level.setTile(Tile.air, 8, 14);
		}
	}

	public void kill() {
		this.isDead = true;
		this.onDie();
	}

	public void reset() {
	    this.health = this.maxHealth;
    }
}
