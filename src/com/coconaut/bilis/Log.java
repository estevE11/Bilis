package com.coconaut.bilis;

import org.json.simple.JSONObject;

import com.coconaut.bilis.Game.Version;
import com.coconaut.bilis.sound.Sound;

public class Log {
	private static String fulllog = "";
	
	public static void saveFullLog() {
		FileUtil.writeFile(fulllog, "game/log.txt");
		Log.info("Log saved...");
	}
	
	@SuppressWarnings("unchecked")
	public static void saveSettings() {
		JSONObject settings = new JSONObject();
		settings.put("key:jump", Game.KEY_JUMP);
		settings.put("key:move_left", Game.KEY_MOVE_LEFT);
		settings.put("key:move_right", Game.KEY_MOVE_RIGHT);
		settings.put("key:shoot_left", Game.KEY_SHOOT_LEFT);
		settings.put("key:shoot", Game.KEY_MOVE_RIGHT);
		settings.put("key:start", Game.KEY_START);
		
		settings.put("sound:volume", Sound.volume);
		settings.put("screen:fullscreen", (Game.isFullscreen ? "1" : "0"));
		
		FileUtil.writeFile(settings.toJSONString(), "game/settings.json");
	}
	
	public static void shutDown(int ext) {
		Log.info("Average FPS: " + Game.calculateAvgFrames());
		Log.info("Closing.. (" + ext + ")");
		saveFullLog();
		saveSettings();
		System.exit(ext);
	}
	
	public static void info(String log) {
		String s = "[INFO] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void warn(String log) {
		String s = "[WARN] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void error(String log) {
		String s = "[ERROR] - " + log;
		System.err.println(s);
		fulllog += s + "\n";
	}

	public static void debug(String log) {
		if(Game.bootVersion != Version.DEBUG) return;
		String s = "[DEBUG] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void info(int log) {
		String s = "[INFO] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void warn(int log) {
		String s = "[WARN] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void error(int log) {
		String s = "[ERROR] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void debug(double log) {
		if(Game.bootVersion != Version.DEBUG) return;
		String s = "[DEBUG] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void info(double log) {
		String s = "[INFO] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void warn(double log) {
		String s = "[WARN] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void error(double log) {
		String s = "[ERROR] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void debug(int log) {
		if(Game.bootVersion != Version.DEBUG) return;
		String s = "[DEBUG] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}
	
	public static void info(boolean log) {
		String s = "[INFO] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void warn(boolean log) {
		String s = "[WARN] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void error(boolean log) {
		String s = "[ERROR] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}

	public static void debug(boolean log) {
		if(Game.bootVersion != Version.DEBUG) return;
		String s = "[DEBUG] - " + log;
		System.out.println(s);
		fulllog += s + "\n";
	}
}
