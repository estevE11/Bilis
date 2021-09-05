package com.coconaut.bilis;

public class Gamepad {
	public static int VK_JOYLX = 0;
	public static int VK_JOYLY = 1;
	public static int VK_X = 8;
	public static int VK_Y = 9;
	public static int VK_A = 6;
	public static int VK_B = 7;
	public static int VK_RB = 11;	
	public static int VK_LB = 10;
	private static int VK_T = 4;
	public static int VK_RT = 20;
	public static int VK_LT = 30;
	private static int VK_CROSSPAD = 16;
	public static int VK_UP = 40;
	public static int VK_DOWN = 50;
	public static int VK_LEFT = 60;
	public static int VK_RIGHT = 70;
	public static int VK_START = 13;

	public static String NAME_XBOX = "Xbox One For Windows";
	public static String NAME_PS4 = "Wireless Controller";

	public static void prepareController(String name) {
		/*if(name.equals(NAME_XBOX)) {*/prepareXbox();/* return;};*/
		//if(name.equals(NAME_PS4)) {preparePS4(); return;};
		//Log.warn("Controller not recognized! Using it as Xbox controller. It might not work.");
	}

	public static void prepareXbox() {
		Log.debug("Controller recognized: Xbox");
		VK_JOYLX = 0;
		VK_JOYLY = 1;
		VK_X = 7;
		VK_Y = 8;
		VK_A = 5;
		VK_B = 6;
		VK_RB = 10;
		VK_LB = 9;
		VK_T = 4;
		VK_RT = 20;
		VK_LT = 30;
		VK_CROSSPAD = 15;
		VK_UP = 40;
		VK_DOWN = 50;
		VK_LEFT = 60;
		VK_RIGHT = 70;
		VK_START = 12;
	}

	public static void preparePS4() {
		Log.debug("Controller recognized: PS4");
		VK_JOYLX = 2;
		VK_JOYLY = 3;
		VK_X = 5;
		VK_Y = 8;
		VK_A = 6;
		VK_B = 7;
		VK_RB = 10;
		VK_LB =  9;
		VK_T = 4;
		VK_RT = 20;
		VK_LT = 30;
		VK_CROSSPAD = 4;
		VK_UP = 40;
		VK_DOWN = 50;
		VK_LEFT = 60;
		VK_RIGHT = 70;
		VK_START = 14;
	}

	public static boolean checkGamepad(int it) {
		if(Game.hasJoystick) {
			if(it == VK_UP) {
				double pollData = Game.gamepad.getComponents()[VK_CROSSPAD].getPollData();			
				if(pollData == 0.25) {
					return true;
				}
				return false;
			}
			if(it == VK_DOWN) {
				double pollData = Game.gamepad.getComponents()[VK_CROSSPAD].getPollData();			
				if(pollData == 0.75) {
					return true;
				}
				return false;
			}
			if(it == VK_LEFT) {
				double pollData = Game.gamepad.getComponents()[VK_CROSSPAD].getPollData();			
				if(pollData == 1) {
					return true;
				}
				return false;
			}
			if(it == VK_RIGHT) {
				double pollData = Game.gamepad.getComponents()[VK_CROSSPAD].getPollData();			
				if(pollData == 0.5) {
					return true;
				}
				return false;
			}
			
			if(it == VK_LT) {
				if(Game.gamepad.getName().equals(NAME_PS4)) {
					double pollData = Game.gamepad.getComponents()[11].getPollData();
					if(pollData == 1) {
						return true;
					}
					return false;
				}
				double pollData = Game.gamepad.getComponents()[VK_T].getPollData();			
				if(pollData >= 0.5) {
					return true;
				}
				return false;
			}

			if(it == VK_RT) {
				if(Game.gamepad.getName().equals(NAME_PS4)) {
					double pollData = Game.gamepad.getComponents()[12].getPollData();
					if(pollData == 1) {
						return true;
					}
					return false;
				}
				double pollData = Game.gamepad.getComponents()[VK_T].getPollData();
				if(pollData <= -0.5) {
					return true;
				}
				return false;
			}

			
			double pollData = Game.gamepad.getComponents()[it].getPollData();			
			if(pollData == 1.0) {
				return true;
			}
		}
		return false;
	}

	public static Vector2d getLJoyAxis() {
		if(Game.hasJoystick) {
			double y = Game.gamepad.getComponents()[VK_JOYLX].getPollData();
			double x = Game.gamepad.getComponents()[VK_JOYLY].getPollData();
			return new Vector2d(x, y);
		}
		return null;
	}
}
