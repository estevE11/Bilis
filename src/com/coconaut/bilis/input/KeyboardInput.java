package com.coconaut.bilis.input;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.coconaut.bilis.Log;
import com.coconaut.bilis.console.Console;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.state.GameState;
import com.coconaut.bilis.state.State;

public class KeyboardInput implements KeyListener {
	
	private State state;
	private Console console;

	public KeyboardInput(Console console) {
		this.console = console;
	}
	
	public void setState(State player) {
		this.state = player;
	}
	
	public void keyPressed(KeyEvent arg0) {
		if(!console.isOpened()) state.onKeyDown(arg0.getKeyCode());
		else console.onKeyDown(arg0.getKeyCode(), arg0.getKeyChar());
	}
	
	public void keyReleased(KeyEvent arg0) {
		if(!console.isOpened()) state.onKeyUp(arg0.getKeyCode());
		else console.onKeyUp(arg0.getKeyCode());

		if(arg0.getKeyCode() == KeyEvent.VK_F1) {
			console.toggle();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

}
