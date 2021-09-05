package com.coconaut.bilis.console;

import com.coconaut.bilis.Game;
import com.coconaut.bilis.Log;
import com.coconaut.bilis.entity.Entity;
import com.coconaut.bilis.entity.mob.Player;
import com.coconaut.bilis.level.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Console {
	private boolean isOpen = false;
	
	private final int lnCnt = 10;
	private final int lnW = 35;
	private final int w = (lnCnt+1) * lnW;
	private final double speed = 20;

	private final Color c_font = new Color(255, 255, 255);
	private final Color c_adove = new Color(0, 0, 255);
	private final Color c_bot = new Color(255, 0, 0);
	
	private double y = -w;
	private String input = "";
	private String[] content = new String[lnCnt];

	private int pointer_pos = 0;
	private int historyIdx = -1;
	private int tickTime = 0;
	
	private Player player;
	
	private LinkedList<ConsoleCommand> commands = new LinkedList<ConsoleCommand>();
	private LinkedList<String> inputHistory = new LinkedList<String>();
	
	public Console() {
		for(int i = 0; i < content.length; i++) {
			content[i] = "";
		}
		log("Bilis " + Game.version + " console.");
		log("Type /help to see the aviable commands.");
		createCommands();
	}
	
	public void createCommands() {
		ConsoleCommand help = new ConsoleCommand("help") {
			public void action(Console console, String[] args) {
				console.log("Commands: /help /print /close /exit /add /subs /summon /tp /move /health");
				console.log("/maxhealth");
			}
		};
		commands.add(help);

		ConsoleCommand print = new ConsoleCommand("print") {
			public void action(Console console, String[] args) {
				if(args.length != 1) {
					console.log("ERROR: /print needs 1 args, not " + args.length);
					return;
				}else
					
				if(args[0].equals(new String("playerpos"))) {
					console.log(console.getPlayer().getX() + ", " + console.getPlayer().getY());
					return;
				} else
					
				if(args[0].equals("entity_ammount")) {
					console.log(""+console.getPlayer().getLevel().getEntities().size());
					return;
				}
				
				
				console.log(args[0]);
			}
		};
		commands.add(print);

		ConsoleCommand exit = new ConsoleCommand("exit") {
			public void action(Console console, String[] args) {
				Log.shutDown(1);
			}
		};
		commands.add(exit);

		ConsoleCommand close = new ConsoleCommand("close") {
			public void action(Console console, String[] args) {
				console.toggle();
				console.log("Closing console...");
			}
		};
		commands.add(close);

		ConsoleCommand health = new ConsoleCommand("health") {
			public void action(Console console, String[] args) {
				if(args.length != 1) {
					console.log("ERROR: /add needs 1 args, not " + args.length);
					return;
				}
				
				int h = Integer.parseInt(args[0]);

				console.getPlayer().setHealth(h);
				console.log("Player health set to " + h);
			}
		};
		commands.add(health);

		ConsoleCommand maxhealth = new ConsoleCommand("maxhealth") {
			public void action(Console console, String[] args) {
				if(args.length != 1) {
					console.log("ERROR: /add needs 1 args, not " + args.length);
					return;
				}
				
				int h = Integer.parseInt(args[0]);

				console.getPlayer().setMaxHealth(h);
				console.log("Player max health set to " + h);
			}
		};
		commands.add(maxhealth);

		ConsoleCommand tp = new ConsoleCommand("tp") {
			public void action(Console console, String[] args) {
				if(args.length != 2) {
					console.log("ERROR: /add needs 2 args, not " + args.length);
					return;
				}
				
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);

				console.getPlayer().setPosition(x, y);
				console.log("Player to " + x + ", " + y);
			}
		};
		commands.add(tp);

		ConsoleCommand move = new ConsoleCommand("move") {
			public void action(Console console, String[] args) {
				if(args.length != 2) {
					console.log("ERROR: /add needs 2 args, not " + args.length);
					return;
				}
				
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);

				console.getPlayer()._move(x, y);
				console.log("Player moved to " + x + ", " + y);
			}
		};
		commands.add(move);

		ConsoleCommand add = new ConsoleCommand("add") {
			public void action(Console console, String[] args) {
				if(args.length != 2) {
					console.log("ERROR: /add needs 2 args, not " + args.length);
					return;
				}
				
				int num1 = Integer.parseInt(args[0]);
				int num2 = Integer.parseInt(args[1]);
				int res = num1 + num2;
				
				console.log(num1 + " + " + num2 + " = " + res);
			}
		};
		commands.add(add);

		ConsoleCommand subs = new ConsoleCommand("subs") {
			public void action(Console console, String[] args) {
				if(args.length != 2) {
					console.log("ERROR: /subs needs 2 args, not " + args.length);
					return;
				}
				
				int num1 = Integer.parseInt(args[0]);
				int num2 = Integer.parseInt(args[1]);
				int res = num1 - num2;
				
				console.log(num1 + " - " + num2 + " = " + res);
			}
		};
		commands.add(subs);

		ConsoleCommand summon = new ConsoleCommand("summon") {
			public void action(Console console, String[] args) {
				if(args.length != 3) {
					console.log("ERROR: /summon needs 3 args, not " + args.length);
					return;
				}
				
				int pos_x = Integer.parseInt(args[1]);
				int pos_y = Integer.parseInt(args[2]);
				
				Entity e = Entity.createEntityByName(args[0]);
				console.getPlayer().getLevel().summon(e, pos_x, pos_y);
				
				console.log(args[0] + " summoned at " + pos_x + ", " + pos_y);
			}
		};
		commands.add(summon);
		
		ConsoleCommand levelSummary = new ConsoleCommand("ls") {
			public void action(Console console, String args[]) {				
				Level level = console.getPlayer().getLevel();
				console.log(". : Level : .");
				console.log("Name: " + level.getName());
				console.log("Tile grid size: " + level.getWidth() + "x" + level.getHeight());
				console.log("Pixel size: " + level.getPixelWidth() + "x" + level.getPixelHeight());
				console.log("Entity ammount: "+console.getPlayer().getLevel().getEntities().size());
			}
		};
		commands.add(levelSummary);
		
		ConsoleCommand getTile = new ConsoleCommand("getTile") {
			public void action(Console console, String args[]) {
				if(args.length != 2) return;
				
				int tx = Integer.parseInt(args[0]);
				int ty = Integer.parseInt(args[1]);
				
				String tile = (""+console.getPlayer().getLevel().getTile(tx, ty)).split("@")[0];
				console.log(tile);
			}
		};
		commands.add(getTile);

		ConsoleCommand isaacFound = new ConsoleCommand("isaac") {
			public void action(Console console, String args[]) {
				if(args.length != 9) {console.log("/isaac needs 9 arg, not " + args.length); return;}

				String ruthere = "";
				for(String s : args) {
					ruthere += s + " ";
				}
				if(ruthere.equals("... --- .-. .-. -.-- / -- --- -- ")) {
					console.log("... --- / .--. -.-- .-. .- -- .. -.. ... / .-- . .-. . / -... .. .-.. - /");
					console.log("-... -.-- / .... ..- -- .- -. ... --..-- / .-. .. --. .... - ..--..");
				}
			}
		};
		commands.add(isaacFound);
	}
	
	public void update() {
		if(isOpen) {
			if(y < 0) {
				y+=speed;
			}
			if(y > 0) y = 0;
			tickTime++;
		} else {
			if(y > -w) {
				y-=speed;
			}
		}
	}
	
	public void render(Graphics g) {
		if(!(this.y < 0 - this.w)) {	
			g.setColor(c_adove);
			g.fillRect(0, (int) y, Game.WIDTH*Game.SCALE, w-lnW);
			g.setColor(c_bot);
			g.fillRect(0, (int) y + w-lnW, Game.WIDTH*Game.SCALE, lnW);
			
			g.setColor(c_font);
			g.setFont(new Font("Arial", Font.PLAIN, 26));
	
			for(int i = 0; i < content.length; i++) {
				String str = content[i];
				g.drawString(str, 5, (int)y + (i+1)*lnW - 9);
			}
			
			g.drawString(input, 5, (int)y + w - 9);
			/*if(tickTime % 60 > 30) */g.fillRect(Game.getStringWidth(input.substring(0, pointer_pos), g) + 3, (int)y + w - 33, 2, 30);
		}
	}
	
	public void log(String str) {
		content[0] = "";
		for(int i = 1; i < lnCnt; i++) {
			content[i-1] = content[i];
		}
		content[lnCnt-1] = str;
	}
	
	public void onKeyDown(int key, char keyStr) {
		if(isOpen) {
			if(key == KeyEvent.VK_UP) {
				if(historyIdx == -1) historyIdx = inputHistory.size() > 0 ? inputHistory.size() : -1;
				if(historyIdx == -1) return;
				historyIdx--;
				if(historyIdx < 0) historyIdx = 0;
				input = inputHistory.get(historyIdx);
			}

			if(key == KeyEvent.VK_DOWN) {
				if(historyIdx == -1) return;
				historyIdx++;
				if(historyIdx >= inputHistory.size()) historyIdx = -1;
				if(historyIdx == -1) input = ""; else
				input = inputHistory.get(historyIdx);
			}
			
			if(key == KeyEvent.VK_ENTER) {
				historyIdx = -1;
				execute();
				return;
			} else
			if(key == KeyEvent.VK_LEFT) {
				if(pointer_pos == 0) return;
				pointer_pos--;
			} else
			if(key == KeyEvent.VK_RIGHT) {
				if(pointer_pos == input.length()) return;
				pointer_pos++;
			}
			switch(keyStr) {
			case 8:
			    if (input != null && input.length() > 0) {
					String fstpart = (input.length() == 0 || pointer_pos == 0 ? "" : input.substring(0, pointer_pos-1));
					String scpart = (input.length() == 0 || pointer_pos == input.length() ? "" : input.substring(pointer_pos, input.length()));
					input = fstpart + scpart;
					if(pointer_pos == 0) return;
					pointer_pos--;
			    	return;
			    }
			    break;
			case 65535:
				return;
			case 127:
				return;
			}

			String fstpart = (input.length() == 0 || pointer_pos == 0 ? "" : input.substring(0, pointer_pos));
			String scpart = (input.length() == 0 || pointer_pos == input.length() ? "" : input.substring(pointer_pos, input.length()));
			input = fstpart + keyStr + scpart;
			pointer_pos++;
		}
	}
	
	public void execute() {
		log(input);
		String[] cm = input.split(" ");
		inputHistory.add(input);
		if(!input.startsWith("/")) {
			log("ERROR: You have to write ' / ' at the begining.");
			input = "";
			pointer_pos = 0;
			return;
		}
		boolean worked = false;
		for(ConsoleCommand cc : commands) {
			if(cc.keyword.equals(cm[0].substring(1))) {
				if(cm.length > 1) {
					String[] args = new String[cm.length-1];
					for(int i = 0; i < args.length; i++) {
						args[i] = cm[i+1];
					}
					cc.action(this, args);
				} else if (cm.length == 1) {
					cc.action(this, null);
				}
				worked = true;
			}
		}
		if(!worked) log(input.split(" ")[0] + ": Command not known.");
		input = "";
		pointer_pos = 0;
	}
	
	public void onKeyUp(int key) {
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isOpened() {
		return isOpen;
	}
	
	public void toggle() {
		this.isOpen = !this.isOpen;
	}
}
