package us.chrisix.cod;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import us.chrisix.cod.weapon.Lethal;
import us.chrisix.cod.weapon.Tactical;
import us.chrisix.cod.weapon.Weapon;
import us.chrisix.cod.weapon.WeaponClass;

public class CallOfDiamond extends JavaPlugin{

	public static String dir = "plugins" + File.separator + "Call of Diamond";
	private static List<Map> maps;
	private static List<Match> matches;
	private WeaponClass defWc;
	public static String header = "[" + ChatColor.AQUA + "Call of Diamond" + ChatColor.WHITE + "] ";
	public static String error = ChatColor.RED + "[Call of Diamond]";
	private static Random rand;

	public void onEnable(){
		setupIO();
		loadMaps();
		matches = new CopyOnWriteArrayList<Match>();
		defWc = new WeaponClass(-1, "Default", Weapon.STONE_SWORD, Weapon.WOOD_SWORD, Lethal.DAMAGE1, Tactical.HEALING);
		rand = new Random();
		Bukkit.getPluginManager().registerEvents(new CodListener(), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("cod")){
				if(args.length > 0){
					executeMapCommands(p, args);
					executeMatchCommands(p, args);
					executeClassCommands(p, args);
				}
				else{

				}
			}
		}
		return true;
	}

	private void executeMapCommands(Player p, String args[]){
		if(args[0].equalsIgnoreCase("map")){
			if(args.length > 4){
				Map m = getMap(args[1]);
				if(m == null){
					p.sendMessage(error + "Map " + args[1] + "does not exist.");
				}
				else{
					if(args[2].equalsIgnoreCase("set")){
						if(args[3].equalsIgnoreCase("spawn")){
							SpawnType type = SpawnType.getSpawnType(Integer.parseInt(args[4]));
							if(m.addSpawn(new Spawn(type, (float)p.getLocation().getX(), (float)p.getLocation().getY(), (float)p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch()))){
								m.save();
								p.sendMessage(header + "Spawn point was created.");
							}
							else{
								p.sendMessage(error + "Spawn was not in the map boundries");
							}
						}
					}
				}
			}
			else if(args.length > 3){
				Map m = getMap(args[1]);
				if(m == null){
					p.sendMessage(error + "Map " + args[1] + "does not exist.");
				}
				else{
					if(args[2].equalsIgnoreCase("spawn")){
						if(args[3].equalsIgnoreCase("blue")){
							m.spawnBlueTeam(p);
						}
						else if(args[3].equalsIgnoreCase("red")){
							m.spawnRedTeam(p);
						}
						else if(args[3].equalsIgnoreCase("zombie")){
							m.spawnZombie();
						}
					}
				}
			}
			else if(args.length > 2){
				if(args[1].equalsIgnoreCase("create")){
					maps.add(createMap(p, args[2]));
					p.sendMessage(header + "Place the first point of your map by breaking a block with a dirt block.");
				}
				else if(args[1].equalsIgnoreCase("remove")){
					Map m = getMap(args[2]);
					if(m != null){
						maps.remove(m);
						p.sendMessage(header + "The map " + (args[2]) + " has been removed!");
						m.getFile().delete();
					}
					else{
						p.sendMessage(error + "A map does not have an id of " + args[2] + ".");
					}
				}
				else{
					Map m = getMap(args[1]);
					if(m == null){
						p.sendMessage(error + "Map " + args[1] + "does not exist.");
					}
					else{
						if(args[2].equalsIgnoreCase("spawn")){
							m.spawnPlayer(p);
						}
					}
				}
			}
			else if(args.length > 1){
				if(args[1].equalsIgnoreCase("list")){
					p.sendMessage(header + "Maps");
					for(Map m : maps){
						p.sendMessage(m.getName() + ": " + m.getId());
					}
				}
			}
		}
	}

	private void executeMatchCommands(Player p, String args[]){
		if(args[0].equalsIgnoreCase("match")){
			if(args.length > 4){
				if(args[1].equalsIgnoreCase("create")){
					Map m = getMap(args[2]);
					if(m == null){
						p.sendMessage(error + "Map " + args[1] + "does not exist.");
					}
					else{
						int type = Integer.parseInt(args[3]);
						if(Match.isMatchType(type)){
							Match match = new Match(m, type, p.getName());
							match.join(p, defWc);
							matches.add(match);
							p.sendMessage(header + "You successfully created a match.");
						}
					}
				}
			}
			else if(args.length > 3){
				if(args[1].equalsIgnoreCase("join")){
					Match m = getMatch(Integer.parseInt(args[2]));
					if(m.join(p, defWc)){
						p.sendMessage(header + "You have successfully joined the match.");
					}
					else{
						p.sendMessage(error + "Their are not enough slots for you to join.");
					}
				}
			}
			else if(args.length > 1){
				if(args[1].equalsIgnoreCase("list")){
					p.sendMessage(header + "Matches");
					for(int i = 0; i < matches.size(); i++){
						Match m = matches.get(i);
						String inlobby = "In-Lobby";
						if(!m.inLobby()){
							inlobby = "Playing";
						}
						p.sendMessage("Match " + i + " on " + m.getMap().getName());
						p.sendMessage("    " + inlobby + "(" + m.getAmountOfPlayers() + "/" + m.getMaxPlayers() + ")");
						p.sendMessage("    " + m.getType().getName());
					}
				}
				else if(args[1].equalsIgnoreCase("start")){
					for(Match m : matches){
						if(m.getOwner().equalsIgnoreCase(p.getName())){
							m.start();
						}
					}
				}
				else if(args[1].equalsIgnoreCase("leave")){
					for(Match m : matches){
						m.leave(p);
					}
				}
			}
		}
	}
	private void executeClassCommands(Player p, String args[]){
		if(args[0].equalsIgnoreCase("class")){
			if(args.length > 2){
				if(args[2].equalsIgnoreCase("create")){
					WeaponClass wc = WeaponClass.empty(p.getName(), args[2]);
					p.sendMessage(header + "Empty weapon class with ID " + wc.getId() + " has been created.");
				}
				if(args[2].equalsIgnoreCase("delete")){
					WeaponClass wc = WeaponClass.load(p.getName(), Integer.parseInt(args[2]));
					p.sendMessage(header + "Weapon Class with the id " + wc.getId() + " has been removed.");
					wc.getFile(p.getName()).delete();
				}else{
					p.sendMessage(error + "There is no Weapon Class with the given id " + args[2]);
				}
			}
			if(args.length > 3){
				if(args[2].equalsIgnoreCase("set")){
					String play = p.getName();
					WeaponClass wc = WeaponClass.load(play, Integer.parseInt(args[1]));
					if(args[3].equalsIgnoreCase("primary")){
						if(args.length == 4){
							p.sendMessage(header + "Primary Choices:");
							p.sendMessage(header + "1: Wood Sword");
							p.sendMessage(header + "2: Stone Sword");
							p.sendMessage(header + "3: Iron Sword");
							p.sendMessage(header + "4: Diamond Sword");
						} else {
							Weapon w = Weapon.getWeapon(Integer.parseInt(args[3]));
							wc.setPrimary(w);
							wc.save(p.getName());
							p.sendMessage(header + "Primary weapon successfully set to class " + args[1] + ".");
						}
					}
					else if(args[3].equalsIgnoreCase("secondary")){
						if(args.length == 4){
							p.sendMessage(header + "Primary Choices:");
							p.sendMessage(header + "1: Wood Sword");
							p.sendMessage(header + "2: Stone Sword");
							p.sendMessage(header + "3: Iron Sword");
							p.sendMessage(header + "4: Diamond Sword");
						}else{
							Weapon w = Weapon.getWeapon(Integer.parseInt(args[3]));
							wc.setSecondary(w);
							wc.save(p.getName());
							p.sendMessage(header + "Secondary weapon successfully set to class " + args[1] + ".");
						}
					}
					else if(args[3].equalsIgnoreCase("lethal")){
						if(args.length == 4){
							p.sendMessage(header + "Lethal Choices:");
							p.sendMessage(header + "1: Damage 1");
							p.sendMessage(header + "2: Damage 2");
							p.sendMessage(header + "3: Poison");
						}else{
							Lethal l = Lethal.getLethal(Integer.parseInt(args[3]));
							wc.setLethal(l);
							wc.save(p.getName());
							p.sendMessage(header + "Lethal item successfully set to class " + args[1] + ".");
						}
					}
					else if(args[3].equalsIgnoreCase("tactical")){
						if(args.length == 4){
							p.sendMessage(header + "Tactical Choices:");
							p.sendMessage(header + "1: Healing");
							p.sendMessage(header + "2: Night Vision");
						}else{
							Tactical t = Tactical.getTactical(Integer.parseInt(args[3]));
							wc.setTactical(t);
							wc.save(p.getName());
							p.sendMessage(header + "Tactical item successfully set to class " + args[1] + ".");
						}
					}
					else if(args[3].equalsIgnoreCase("help")){
						p.sendMessage(header + "Options are:");
						p.sendMessage(header + "primary - Set primary Weapon");
						p.sendMessage(header + "secondary - Set secondary Weapon");
						p.sendMessage(header + "lethal - Set lethal Item");
						p.sendMessage(header + "tactical - Set tactical Item");
					}
					else{
						p.sendMessage(error + "That is an invalid option.");
						p.sendMessage(error + "Options are:");
						p.sendMessage(header + "primary - Set primary Weapon");
						p.sendMessage(header + "secondary - Set secondary Weapon");
						p.sendMessage(header + "lethal - Set lethal Item");
						p.sendMessage(header + "tactical - Set tactical Item");
					}
				}
			}
		}
	}
	public Map getMap(String arg){
		try{
			int id = Integer.parseInt(arg);
			for(Map m : maps){
				if(m.getId() == id){
					return m;
				}
			}
			return null;
		}
		catch(NumberFormatException e){
			for(Map m : maps){
				if(m.getName().equalsIgnoreCase(arg)){
					return m;
				}
			}
			return null;
		}
	}

	public Match getMatch(int id){
		return matches.get(id);
	}

	private void setupIO(){
		File f = new File(dir);
		if(!f.exists()){
			f.mkdir();
		}
		f = new File(dir + File.separator + "maps");
		if(!f.exists()){
			f.mkdir();
		}
		f = new File(dir + File.separator + "classes");
		if(!f.exists()){
			f.mkdir();
		}
	}

	private void loadMaps(){
		List<Map> maps = new CopyOnWriteArrayList<Map>();
		File root = new File(dir + File.separator + "maps");
		for(File f : root.listFiles()){
			if(f.getAbsolutePath().endsWith(".cod_map")){
				FileConfiguration config = YamlConfiguration.loadConfiguration(f);
				int id = config.getInt("id");
				String name = config.getString("name");
				String owner = config.getString("owner");
				String world = config.getString("world");
				int x1 = config.getInt("x1");
				int y1 = config.getInt("y1");
				int x2 = config.getInt("x2");
				int y2 = config.getInt("y2");
				List<String> spawnList = config.getStringList("spawns");
				Map m = new Map(id, name, owner, world, x1, y1, x2, y2, spawnList);
				maps.add(m);
			}
		}
		CallOfDiamond.maps = maps;
	}

	private Map createMap(Player p, String name){
		int id = -1;
		boolean run = true;
		while(run){
			id += 1;
			run = false;
			for(Map m : maps){
				if(m.getId() == id){
					run = true;
					break;
				}
			}
		}
		return new Map(p.getWorld(), name, p.getName(), id);
	}

	public static List<Map> getMaps() {
		return maps;
	}

	public static List<Match> getMatches(){
		return matches;
	}

	public static Random getRandom(){
		return rand;
	}

}
