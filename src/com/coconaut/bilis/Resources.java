package com.coconaut.bilis;

import com.coconaut.bilis.gfx.Sprite;
import com.coconaut.bilis.gfx.SpriteSheet;

public class Resources {
	public static Resources i = new Resources();
	
	public Sprite maintitle;
	public SpriteSheet contBtn;
	public Sprite logo_pat;
	public Sprite pausemenu;
	public Sprite pausemenu_back;
	public Sprite deathscreen;
	public Sprite trans;
	
	// . : SPRITES : .
	//Entities
	public Sprite spr_anim_player_idle;
	public Sprite spr_anim_player_run;

	public Sprite spr_player_jumping;
	public Sprite spr_player_jumping_side;
	public Sprite spr_player_falling;
	public Sprite spr_player_falling_side;
	
	public Sprite spr_slimeballs;
	public Sprite spr_laser;
	public Sprite spr_bonemerang;

	public Sprite spr_hotball;
	public Sprite spr_polt;
	public Sprite spr_chest;
	public Sprite spr_skull;
    public Sprite spr_edust;

	//Bosses
	public Sprite spr_boss_hotball;
	
	//Itemns
	public Sprite spr_item_spectral_ball;
	public Sprite spr_item_jump_boost;
	public Sprite spr_item_speed_boost;
	public Sprite spr_item_balls_up;
	public Sprite spr_item_power_rock;

	//Notifications
    public Sprite spr_notification;

	// . : TILES : .
	public Sprite spr_tile_rock;
    public Sprite spr_tile_barrier;

	public Sprite spr_fola;
	
	
	public void load() {
		Log.info("Loading sprites...");
		maintitle = new Sprite("mainmenu/maintitle.png");
		contBtn = new SpriteSheet(new Sprite("other/controllerBtns.png"));
		logo_pat = new Sprite("pat_logo.png");
		
		pausemenu = new Sprite("pausemenu/pausemenu.png");
		pausemenu_back = new Sprite("pausemenu/pausemenu_back.png");
		
		spr_anim_player_idle = new Sprite("player/player_idle.png");
		spr_anim_player_run = new Sprite("player/player_run.png");

		spr_player_jumping = new Sprite("player/player_jumping.png");
		spr_player_jumping_side = new Sprite("player/player_jumping_side.png");
		
		spr_player_falling = new Sprite("player/player_falling.png");
		spr_player_falling_side = new Sprite("player/player_falling_side.png");
		
		spr_laser = new Sprite("player/player_laser.png");
		spr_slimeballs = new Sprite("player/player_slimeball.png");
		spr_bonemerang = new Sprite("projectile/spr_bonemerang.png");

		spr_hotball = new Sprite("enemy/spr_hotball.png");
		spr_polt = new Sprite("enemy/spr_polt.png");
		spr_chest = new Sprite("entity/spr_chest.png");
		spr_skull = new Sprite("enemy/spr_skull.png");
        spr_edust = new Sprite("enemy/spr_edust.png");

		spr_boss_hotball = new Sprite("boss/spr_boss_hotball.png");
		
		spr_item_spectral_ball = new Sprite("items/spr_spectral_balls.png");
		spr_item_jump_boost = new Sprite("items/spr_jump_boost.png");
		spr_item_speed_boost = new Sprite("items/spr_speed_boost.png");
		spr_item_balls_up = new Sprite("items/spr_balls_up.png");
		spr_item_power_rock = new Sprite("items/spr_power_rock.png");

		spr_tile_rock = new Sprite("tiles/tile_rock.png");
		spr_tile_barrier = new Sprite("tiles/tile_barrier.png");

        spr_notification = new Sprite("notification/notification.png");

        trans = new Sprite("other/trans.png");
	}
}
