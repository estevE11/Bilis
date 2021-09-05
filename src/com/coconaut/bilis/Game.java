package com.coconaut.bilis;

import com.coconaut.bilis.console.Console;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.gfx.Camera;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.input.KeyboardInput;
import com.coconaut.bilis.postprocessing.EffectTest;
import com.coconaut.bilis.postprocessing.EffectTurnBlue;
import com.coconaut.bilis.postprocessing.PostProcessor;
import com.coconaut.bilis.sound.Sound;
import com.coconaut.bilis.state.GameState;
import com.coconaut.bilis.state.InitState;
import com.coconaut.bilis.state.State;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.json.simple.JSONObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

public class Game extends Canvas implements Runnable {	
	private static final long serialVersionUID = 1L;
	
	public static enum Version {
		RELEASE("release"), DEBUG("debug"), RELEASE_LINUX("release_linux"), DEBUG_LINUX("debug_linux");
		
		public String name = "Unknown boot version";
		Version(String name) {
			this.name = name;
		}
	};

	private static Random r = new Random();
	
	public static int WIDTH = 160*2, HEIGHT = 120*2, SCALE = 3;
	private final int tickRate = 60;
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static double swidth        = screenSize.getWidth();
	private static double sheight       = screenSize.getHeight();
	
	public static final String version             = "v0.0.10";
	public static final Version bootVersion        = Version.DEBUG;
	public static final boolean resizeable         = false;
	public static final int onClose                = JFrame.DO_NOTHING_ON_CLOSE;
	public static final Component locationRelative = null;
	public static boolean isFullscreen;
	public JFrame frame;
	
	public static int KEY_JUMP        = KeyEvent.VK_SPACE;
	public static int KEY_MOVE_LEFT   = KeyEvent.VK_A;
	public static int KEY_MOVE_RIGHT  = KeyEvent.VK_D;
	public static int KEY_SHOOT_LEFT  = KeyEvent.VK_LEFT;
	public static int KEY_SHOOT_RIGHT = KeyEvent.VK_RIGHT;
	public static int KEY_START       = KeyEvent.VK_ESCAPE;

	public static int level_world = 1;
	public static int level_level = 1;

	public static boolean hasJoystick = false;
	public static Controller gamepad;
	
	private BufferedImage canvas;
	public static int fps = 0, tps = 0, ffps = 0, ftps = 0;
	private static LinkedList<Integer> frames = new LinkedList<Integer>();

	private static LinkedList<Notification> notifications = new LinkedList<Notification>();
	
	private GameState gameState;
	
	private Thread thread;
	public static Camera camera;
	private KeyboardInput kb;
	private Screen screen;
	private Console console;
	private PostProcessor postProcessor;

	public static Lenguage lenguage = new Lenguage("resources/lenguage/en.leng");
		
	private State currentState;
	
	public void start() {
		Resources.i.load();
		Entity.addMappings();
		try {
			Controllers.create();
			if(!Controllers.isCreated()) Log.error("Error loading controllers pluged in!"); 
		} catch (LWJGLException e) {
			Log.error(e.getMessage());
		}
		Controllers.poll();
		
		this.thread = new Thread(this, "Bilis Main Thread");
		this.thread.start();
	}
	
	public void init() {
		
		this.canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
						
		this.currentState = new InitState(this);
		this.console = new Console();
		this.gameState = new GameState(this);
		this.kb = new KeyboardInput(console);
		this.postProcessor = new PostProcessor();
        this.postProcessor.add(new EffectTurnBlue());

		this.addKeyListener(this.kb);
		this.kb.setState(this.currentState);
		Game.camera = new Camera();
		this.screen = new Screen(Game.camera);
		
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		for(Controller c : ca) {
			if(c.getType() == Controller.Type.GAMEPAD) { //Searching a controller
				gamepad = c;
				hasJoystick = true;
			}
		}
		
		if(hasJoystick) {
			Log.info("Game pad connected (" + gamepad.getName() + ")");
			Gamepad.prepareController(gamepad.getName());
		}
		
		JSONObject s = JSONUtil.load("game/settings.json");
		long i = (Long) s.get("key:jump");
		Game.KEY_JUMP = (int) i;
		i = (Long) s.get("key:move_left");
		Game.KEY_MOVE_LEFT = (int) i;
		i = (Long) s.get("key:move_right");
		Game.KEY_MOVE_RIGHT = (int) i;
		i = (Long) s.get("key:shoot_left");
		Game.KEY_SHOOT_LEFT = (int) i;
		i = (Long) s.get("key:shoot_left");
		Game.KEY_SHOOT_LEFT = (int) i;
		i = (Long) s.get("key:start");
		Game.KEY_START = (int) i;
		
		double v = (Double) s.get("sound:volume");
		Sound.volume = (float)v;
		i = Integer.parseInt((String) s.get("screen:fullscreen"));
		int f = (int) i;
		Game.isFullscreen = f == 0 ? false : true;
		this.setFullscreen(Game.isFullscreen);
	}
 	
	public void run() {
		Log.info("tickRate = " + tickRate);
		
		init();

		frame.requestFocus();
		this.requestFocus();

		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		long lastTimer1 = System.currentTimeMillis();

		while (true) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (unprocessed >= 1) {
				tps++;
				update();
				unprocessed -= 1;
			}

			fps++;
			render();

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				Log.info(tps + " ticks, " + fps + " fps");
				ffps = fps;
				ftps = tps;
				frames.add(fps);
				fps = 0;
				tps = 0;
			}
		}
	}
	
	public void update() {
		if(this.hasFocus()) {
			if(hasJoystick) {
				if(!gamepad.poll()) {
					hasJoystick = false;
				}
			}

			for(int i = 0; i < notifications.size(); i++) {
			    Notification n = notifications.get(i);
			    n.setPos(i);
			    n.update();
			    if(n.isDead()) this.notifications.remove(i);
            }
			
			//Updating the current state
			this.currentState.update();
			if(bootVersion == Version.DEBUG) this.console.update();
		}
	}
	
	public void render() {
		
		screen.update(canvas);
		screen.clear();

        for(int i = 0; i < notifications.size(); i++) {
            Notification n = notifications.get(i);
            n.render(screen);
        }

		this.currentState.render(screen);
//		if(Game.hasJoystick) {
//			for(int i = 0; i < Game.gamepad.getComponents().length; i++) {
//				double pollData = Game.gamepad.getComponents()[i].getPollData();
//				String name = Game.gamepad.getComponents()[i].getName();
//				screen.renderText(i + ". " + name + ", " + pollData, 1, 10+(10*i), 10, true);
//			}
//		}
		
		screen.end();
		this.canvas = this.postProcessor.execute(this.canvas);
		
		// EXTRA STUFF (do not touch this)
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics gg = bs.getDrawGraphics();
		if(isFullscreen) {
			int w = (int)(WIDTH * (sheight/HEIGHT));
			try {
				gg.fillRect(0, 0, (int)(swidth/2 - w/2), (int)sheight);
				gg.fillRect((int)(swidth/2 - w/2) + w, 0, (int)(swidth/2 - w/2), (int)sheight);
				gg.drawImage(this.canvas, (int)(swidth/2 - w/2), 0, w, (int)sheight, null);
			} catch(Exception e) {
				Log.error("Failed to render canvas! (" + e.getMessage() + ")");
			}
		} else {
			try {
				gg.drawImage(this.canvas, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
			} catch(Exception e) {
				Log.error("Failed to render canvas! (" + e.getMessage() + ")");
			}
		}
		
		//Rendering the console!!! Yay!"""
		if(bootVersion == Version.DEBUG || bootVersion == Version.DEBUG_LINUX) this.console.render(gg);
		
		try {
			gg.dispose();
			bs.show();
		} catch(Exception e) {
			Log.error("Failed to dispose Graphics! (" + e.getMessage() + ")");
		}
	}

	public static void addNotification() {
	    Notification n = new Notification();
	    n.init(notifications.size());
	    notifications.add(n);
    }
	
	public void setState(State state) {
		this.currentState = state;
		this.kb.setState(this.currentState);
		if(state instanceof GameState) {
			this.addMouseMotionListener((MouseMotionListener) state);
			this.addMouseListener((MouseListener) state);
		}
		Log.info("State=" + state);
	}
	
	public GameState getGameState() {
		return gameState;
	}

	public Console getConsole() {
		return console;
	}
	
	public static int calculateAvgFrames() {
		if(frames.size() == 0) return 0;
		int sum = 0;
		int avg = 0;
		
		for(int f : frames) {
			sum += f;
		}
		
		avg = sum/frames.size();
		
		return avg;
	}
	
	public static Camera getCamera() {
		return camera;
	}
	
	public void setFullscreen(boolean c) {
		isFullscreen = c;
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
		GraphicsDevice device = devices[0];
        
		frame.remove(this);
		frame.dispose();
		if(!frame.isDisplayable())frame.setUndecorated(isFullscreen);
		frame.setLocationRelativeTo(locationRelative);
		frame.add(this);
		if(isFullscreen) {
			device.setFullScreenWindow(this.frame);
			this.setSize(new Dimension((int)swidth, (int)sheight));
		} else {
			device.setFullScreenWindow(null);
			this.setSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		}
		frame.setVisible(true);
		Log.info("Fullscreen: " + isFullscreen);
	}
	
	public static void main(String args[]) {
		Log.info("Bilis " + version + " " + bootVersion.name + "\n");

		//		System.setProperty("java.library.path", "R:/git/Bilis/lib");
//		System.loadLibrary("jinput-dx8_64");
		
		Game game = new Game();
		game.frame = new JFrame("Bilis");

		if(isFullscreen) {
			game.frame.setPreferredSize(new Dimension((int)swidth, (int)sheight));
			game.frame.setMaximumSize(new Dimension((int)swidth, (int)sheight));
			game.frame.setMinimumSize(new Dimension((int)swidth, (int)sheight));
		} else {
			game.frame.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.frame.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.frame.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		}

		Image icon = Toolkit.getDefaultToolkit().getImage("resources/gfx/icon.png");
		
		game.frame.setDefaultCloseOperation(onClose);
		game.frame.setResizable(resizeable);
		game.frame.setIconImage(icon);
		game.setFullscreen(isFullscreen);
		game.frame.setLocationRelativeTo(locationRelative);
		game.frame.add(game);
		game.frame.setVisible(true);
		
		game.frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	Log.shutDown(1);
		    }
		});
				
		String os = System.getProperty("os.name");		
		Log.info("OS: " + os + " v" + System.getProperty("os.version"));
		Log.info("OS Arch: " + System.getProperty("os.arch"));
		Log.info("Java version: " +  System.getProperty("java.version"));
		Log.info("Java vendor: " +  System.getProperty("java.vendor"));
		if(os.equals("Windows 10")) {
			setProperty("sun.java2d.d3d", "true");
			setProperty("sun.java2d.ddforcevram", "true");
		} else {
			setProperty("sun.java2d.opengl", "true");
		}
				
		Log.info("Viewport: " + WIDTH*SCALE + "x" + HEIGHT*SCALE);
		Log.info("Render: " + WIDTH + "x" + HEIGHT);
		Log.info("Default close operation: " + onClose);
		Log.info("Resizeable: " + resizeable);
		Log.info("Location relative to: " + locationRelative);
		
		game.start();
	}
	
	public static int random(int min, int max, int isNeg) {
		int result = r.nextInt(max - min) + min;
		if(isNeg == 1)return -result;
		else if(isNeg == 0) return result;
		else if(isNeg == 2) {
			if(r.nextInt(100) > 50) return -result;
			else return result;
		}
		return result;
	}

	public static int randomDouble(int min, int max) {
		int result = r.nextInt(max - min) + min;
		return result;
	}
	
	public static int getStringWidth(String str, Graphics g) {
		return g.getFontMetrics().stringWidth(str);
	}
	
	public static String KeyCodeToString(int key) {
		String ks = KeyEvent.getKeyText(key);
		if(ks.equals("Izquierda")) ks = "Left";
		if(ks.equals("Derecha")) ks = "Right";
		if(ks.equals("Arriba")) ks = "Up";
		if(ks.equals("Abajo")) ks = "Down";
		if(ks.equals("Espacio")) ks = "Space";
		return ks;
	}
	
	public static void setProperty(String a, String b) {
		System.setProperty(a, b);
		Log.info(a + "=" + b);
	}

}
