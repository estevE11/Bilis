package com.coconaut.bilis.entity.mob;

import com.coconaut.bilis.*;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.entity.item.EntityItem;
import com.coconaut.bilis.entity.item.EntityItemSpectralBall;
import com.coconaut.bilis.entity.projectile.EntityLaser;
import com.coconaut.bilis.entity.projectile.EntitySlimeBall;
import com.coconaut.bilis.gfx.Animation;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;
import com.coconaut.bilis.level.Level;
import com.coconaut.bilis.sound.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Mob {

    private LinkedList<EntityItem> items = new LinkedList<EntityItem>();

	private double jumpSpeed = -7;
	private double speed = 3;
	private int fireRate = 30;
	private int fireTime = 0;

	private boolean spectralBalls = true;
	
	private boolean laser = false;
	private int l_chargeTick = 0;
	private int l_chargeTime = 30;
	private int l_dir = 0;
	private int l_aliveTick = 0;
	private int l_aliveTime = 30;
	private boolean l_isAlive = false;
	
	private EntityLaser e_laser = null;
	
	private int maxHealth = 15;
	private int health = maxHealth;

	private int damage = 1;
	
	private boolean left = false, right = false;
	private boolean s_left = false, s_right = false;
	
	private boolean isJumping = false;
	private boolean isFalling = false;
	
	private boolean isTouchable = true;
	private int tickTouch = 0;
	private int tickTouchTime = 60;
	
	private double kx = 0, ky = 0;

	private int kill_combo = 0;
	
	private Animation idle;
	private Animation run;
	private Animation currentAnim;
	private Sprite currentSpr;
	
	public Player() {
		this.type = EntityType.PLAYER;
		
		this.x = 30;
		this.y = 100;
		this.hasGravity = true;
		
		this.idle = new Animation(Resources.i.spr_anim_player_idle, 16, 16, 3, 15);
		this.run = new Animation(Resources.i.spr_anim_player_run, 16, 16, 3, 10);
		
		this.currentAnim = this.idle;
		this.currentSpr = Resources.i.spr_player_falling;
		this.idle.play();
		this.run.play();
	}
	
	public void update(Level level) {
	    if(this.health <= 0) Log.shutDown(0);

        //Checks if the button A is pressed and if the player is inGround. If on water it doesn't.
		if(Gamepad.checkGamepad(Gamepad.VK_A) && (this.inWater ? true : this.onGround)) {
		    //Sets ya depending on if the player is in water or not.
			this.ya = this.inWater ? jumpSpeed*0.55 : this.jumpSpeed;
			Sound.player_jump.play();
			this.onGround = false;
		}

		//Chcks if laser isn't on.
		if(!laser) {
			this.fireTime++;
			//Once the loading time is over, checks if btns are pressed and shoots a normal bullet.
			if(this.fireTime >= this.fireRate) {
				if(!((this.s_left || Gamepad.checkGamepad(Gamepad.VK_LT)) && (this.s_right || Gamepad.checkGamepad(Gamepad.VK_RT)))) {
					if(this.s_left || Gamepad.checkGamepad(Gamepad.VK_LT)) {shoot(-1); this.fireTime = 0;}
					if(this.s_right || Gamepad.checkGamepad(Gamepad.VK_RT)) {shoot(1); this.fireTime = 0;}
				}
			}
		} else {
		    //if laser is on...
			//Boolean that checks if any of the shoot keys is pressed.
            boolean lr = this.s_left|| this.s_right || Gamepad.checkGamepad(Gamepad.VK_LT) || Gamepad.checkGamepad(Gamepad.VK_RT);
			if(lr) {
				removeLaser();
				l_chargeTick++;
				if(Gamepad.checkGamepad(Gamepad.VK_RT) || this.s_right) l_dir = 1;
				if(Gamepad.checkGamepad(Gamepad.VK_LT) || this.s_left) l_dir = -1;;
			} else {
				if(l_chargeTick >= l_chargeTime) {
					l_chargeTick = 0;
					shoot(l_dir);
					l_isAlive = true;
				} else {
					l_chargeTick = 0;
				}
			}
		}

		//laser live mechanics.
		if(l_isAlive) {
			l_aliveTick++;
			if(l_aliveTick >= l_aliveTime) {
				removeLaser();
			}
		}
						
		this.xa = 0;

		//if there is any controller connected, assign the x val of the left joystick to the x velocitiy of the player.
		if(Game.hasJoystick) {
			Vector2d joy = Gamepad.getLJoyAxis();
			this.xa = joy.x * this.speed;
		}

		//Player movement controlled by keyboard.
		if(left) this.xa = -speed;
		if(right) this.xa = speed;

		//the untouchable mechanics.
		if(!isTouchable) {
			this.tickTouch++;
			if(this.tickTouch >= this.tickTouchTime) {
				this.isTouchable = true;
				this.tickTouch = 0;
			}
		}

		//Updates the current animation.
		this.currentAnim.update();

		//Knockback mechanics.
		if(this.kx != 0)this.xa = this.kx;
		if(this.ky != 0)this.ya = this.ky;
		if(this.kx > 0) this.kx -= 0.5;
		if(this.kx < 0) this.kx += 0.5;
		if(this.ky < 0) this.ky++;

		//Stores the player's x position berfore moving him.
		double px = this.x;

		//moves the player
		this.move();
		super.update();

		//Booleans to set the current animation.
		if(!onGround) {
			if(this.ya > 0) {
				this.isFalling = true;
				this.isJumping = false;
			}
			if(this.ya < 0) {
				this.isFalling = false;
				this.isJumping = true;
			}
		} else {
			this.isFalling = false;
			this.isJumping = false;
			if(this.kill_combo > 0) {
				this.kill_combo = 0;
			}
		}

		//to know if the player has moved or not.
		if(px != this.x) {
			this.currentAnim = this.run;
		} else {
			this.currentAnim = this.idle;
		}

		//Stores player's current x position and player' before moving position into integers.
		int xx = (int)this.x;
		int ppx = (int)px;
		//Animation stuff... (Boooorrrinnngggg!!!)
		if(isFalling) {
			this.currentSpr = Resources.i.spr_player_falling;
			if(xx != ppx) this.currentSpr = Resources.i.spr_player_falling_side;
		}

		if(isJumping) {
			this.currentSpr = Resources.i.spr_player_jumping;
			if(xx != ppx) this.currentSpr = Resources.i.spr_player_jumping_side;
		}

		//Checks if the player is outside the room to go to the next/previous room.
		if(this.x > 16*20) level.nextRoom();
        else if(this.x < -16) level.prevRoom();
	}

	public void render(Screen screen) {
		boolean flip = false;
		if(this.xa < 0) flip = true;

		//only renders killcombo text if it's bigger than 1.
		if(kill_combo > 1) {
			String s = kill_combo + "";
			screen.renderTextOnLevel("", 0, 0, 8, false);
			screen.renderTextOnLevel(s, this.x + (8 - screen.calcTextWidth(s)/2), this.y, 8, true);
		}

		//Rendering stuff.. (ez & boring..)
		if(this.tickTouch % 4 == 0) {
			if(isJumping || isFalling) {
				if(flip) this.currentSpr.draw(this.x, this.y, 1, screen);
				else this.currentSpr.draw(this.x, this.y, screen);
			}
			if(isJumping || isFalling) {} else {
                if (isTouchable) {
                    if (flip) this.currentAnim.draw((int) x, (int) y, screen, 1);
                    else this.currentAnim.draw((int) x, (int) y, screen);
                } else {
                    if (flip) this.currentAnim.draw((int) x, (int) y, screen, 1);
                    else this.currentAnim.draw((int) x, (int) y, screen);
                }
            }
		}

		//Render items owned!
        for(int i = 0; i < this.items.size(); i++) {
		    EntityItem item = this.items.get(i);
            item.render(10 + (i * 20), 210, screen);
            item.render(10 + (i * 20), 300, screen);
            item.render(10 + (i * 20), 210, screen);
        }
	}
	
	public void onEntityColide(Entity other) {
		if(isTouchable) {
			if(other.type == Entity.EntityType.ENEMY) {
				if(this.ya > 0) {
					other.kill();
					this.ya = this.jumpSpeed;
					Sound.enemy_hurt.play();
					this.kill_combo++;
					return;
				}
				
				this.ky = -5;
				if(other.getX() > this.x) {
					this.kx = -5;
				}
	
				if(other.getX() < this.x) {
					this.kx = 5;
				}
				isTouchable = false;
				Sound.player_hurt.play();
				this.health--;
			}
		}
	}
	
	public void onTileColide() {
	}
	
	public void shoot(int dir) {
	    //if u have the laser, creates and summons a laser, and plays the player_shoot sound.
		if(laser) {
			e_laser = new EntityLaser(this, dir);
			level.summon(e_laser, this.x, this.y);
			Sound.player_shoot.play();
			return;
		}

		//if u dont have the laser, summons an EntitySlimeBall an plays player_shoot sound.
		level.summon(new EntitySlimeBall(this, dir, this.spectralBalls), this.x + (8 * dir), this.y + 8);
		//this.kx = 3 * dir * -1;
		Sound.player_shoot.play();
	}
			
	public void onKeyDown(int key) {
		if(key == Game.KEY_MOVE_LEFT) left = true; 
		if(key == Game.KEY_MOVE_RIGHT) right = true; 
		
		if(key == Game.KEY_SHOOT_LEFT) s_left = true;
		if(key == Game.KEY_SHOOT_RIGHT) s_right = true;
		
		if((this.inWater ? true : this.onGround)) {
			if(key == Game.KEY_JUMP) {this.ya = (this.inWater ? this.jumpSpeed*0.55 : this.jumpSpeed); Sound.player_jump.play();}
		}
		
		if(key == KeyEvent.VK_P) {
			level.summon(new EntityItemSpectralBall(), this.x, this.y);
		}
	}

	public void onKeyUp(int key) {
		if(key == Game.KEY_MOVE_LEFT) left = false;
		if(key == Game.KEY_MOVE_RIGHT) right = false;

		if(key == Game.KEY_SHOOT_LEFT) s_left = false;
		if(key == Game.KEY_SHOOT_RIGHT) s_right = false;
	}

	private void removeLaser() {
		if(l_isAlive) {
			l_aliveTick = 0;
			e_laser.kill();
			e_laser = null;
			l_isAlive = false;
		}
	}

	public boolean addItem(EntityItem item) {
	    for(EntityItem it : this.items) {
	        if(it.getClass() == item.getClass()) return false;
        }

        this.items.add(item);
	    return true;
    }

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMaxHealth(int health) {
		this.maxHealth = health;
	}

	public void setInWater(boolean a) {
		this.inWater = a;
	}

	public boolean inWater() {
		return this.inWater;
	}

	public void setSpectralBalls(boolean b) {
		this.spectralBalls = b;
	}

	public void setJumpSpeed(double js) {
		this.jumpSpeed = js;
	}

	public void setFireRate(int fr) {
		this.fireRate = fr;
	}

	public void setSpeed(double js) {
		this.speed = js;
	}

	public int getDamage() {return this.damage;}

	public void addSpeed(double js) {
		this.speed += js;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void enableLaser() {
		this.laser = true;
	}

	public void disableLaser() {
		this.laser = false;
	}

	public Rectangle tileB() {
		return new Rectangle(2, 5, 12, 10);
	}
	
	
	private void setAnimation(Animation a) {
		this.currentAnim.stop();
		this.currentAnim.restart();
		this.currentAnim = a;
	}

	private void setAnimationAndPlay(Animation a) {
		this.currentAnim.stop();
		this.currentAnim.restart();
		this.currentAnim = a;
		this.currentAnim.play();
	}
}
