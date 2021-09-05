package com.coconaut.bilis.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;

public class Sound {
	public static final Sound player_jump  = new Sound("resources/sfx/player_jump.wav"); 
	public static final Sound player_shoot = new Sound("resources/sfx/player_shoot.wav");
	public static final Sound menu_change  = new Sound("resources/sfx/menu_changed.wav");
	public static final Sound player_hurt  = new Sound("resources/sfx/player_hurt.wav");
	public static final Sound enemy_hurt   = new Sound("resources/sfx/enemy_hurt.wav");
	public static final Sound item_pickup  = new Sound("resources/sfx/pickup_item.wav");
	
	public static float volume;

	public Clip in;
	
	public Sound(String name) {
	    try
	    {
			Log.info("Attempting to load " + name);
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(name));
	        in = AudioSystem.getClip();
	        in.open(audioIn);
			Log.info(name + " successfuly loaded");
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	}
	
	public void play(){
		if(Game.bootVersion == Game.Version.RELEASE_LINUX || Game.bootVersion == Game.Version.DEBUG_LINUX) return;
		FloatControl cvolume = (FloatControl) in.getControl(FloatControl.Type.MASTER_GAIN);
		
		float v = 10 + (10 * volume / 20);
		cvolume.setValue((v*5)-100);    
		
	    
	    new Thread() {
	    	public void run() {
	    		if(in.isRunning()) in.stop();
	    		in.setFramePosition(0);
	    		in.start();
	    	}
	    }.start();
	}
	
	public static void volUp() {
		volume++;
		if(volume > 20) volume = 20;
	}

	public static void volDown() {
		volume--;
		if(volume < 0) volume = 0;
	}
}