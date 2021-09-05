package com.coconaut.bilis.entity;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.entity.item.EntityItem;
import com.coconaut.bilis.entity.mob.Mob;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.SpriteSheet;

import java.awt.*;

public class EntityChest extends Mob {
	private boolean isOpen = false;
	private SpriteSheet sheet;
	private EntityItem item;
	
	public EntityChest(EntityItem item) {
		this.type = EntityType.CHEST;
		this.cmdName = "entity:chest";
		this.sheet = new SpriteSheet(Resources.i.spr_chest);
		
		this.item = item;
		
		this.hasGravity = true;
	}
	
	public void update() {
		this._move(0, 5);
	}
	
	public void render(Screen screen) {
		screen.render(sheet, this.x, this.y, !isOpen ? 0 : 16, 0, 16, 16);
	}

	public void onEntityColide(Entity other) {
		if(other.type == EntityType.PLAYER) {
			if(!this.isOpen) this.level.summon(item, this.x, this.y - 16);
			this.isOpen = true;

			if(other.getY() + 14 > this.y && other.ya > 0) {
				other.setPosition(other.getX(), this.y - 13);
				other.setOnGround(true);
				return;
			}

			if(other.getY() >= this.y) {
				if (other.getX() > this.x) {
					this._move((other.getX() - this.x) - 15, 0);
				} else {
					this._move((other.getX() - this.x) + 15, 0);
				}
			}
		}
	}

    public void reset() {
        this.isOpen = false;
    }

	public Rectangle tileB() {
		return new Rectangle(1, 0, 13, 15);
	}
}
