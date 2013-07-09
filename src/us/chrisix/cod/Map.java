package us.chrisix.cod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Map {

	private int x1, y1, x2, y2, id;
	private String name, owner;
	private List<Spawn> spawns;
	private int step = -1;
	private World world;
	
	public Map(World world, String name, String owner, int id){
		this.world = world;
		this.id = id;
		this.name = name;
		this.owner = owner;
		step = 0;
		spawns = new CopyOnWriteArrayList<Spawn>();
	}
	
	public Map(int id, String name, String owner, String world, int x1, int y1, int x2, int y2, List<String> spawnList){
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.world = Bukkit.getWorld(world);
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		spawns = new CopyOnWriteArrayList<Spawn>();
		for(String s : spawnList){
			spawns.add(new Spawn(s));
		}
	}
	
	public World getWorld(){
		return world;
	}

	public int getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean step(Player p, int x, int y){
		if(p.getName().equalsIgnoreCase(owner)){
			if(step != -1){
				if(step == 0){
					x1 = x;
					y1 = y;
					step = 1;
					p.sendMessage(CallOfDiamond.header + "Place the second point of your map by breaking a block with a dirt block.");
					return true;
				}
				else if(step == 1){
					x2 = x;
					y2 = y;
					int x3 = x1;
					int y3 = y1;
					x1 = Math.min(x3, x2);
					y1 = Math.min(y3, y2);
					x2 = Math.max(x3, x2);
					y2 = Math.max(y3, y2);
					step = -1;
					p.sendMessage(CallOfDiamond.header + "Your map is now ready to have spawn points.");
					save();
					return true;
				}
			}
		}
		return false;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public void save(){
		File f = new File(CallOfDiamond.dir + File.separator + "maps" + File.separator + id + ".cod_map");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		config.set("id", id);
		config.set("name", name);
		config.set("owner", owner);
		config.set("world", world.getName());
		config.set("x1", x1);
		config.set("y1", y1);
		config.set("x2", x2);
		config.set("y2", y2);
		List<String> spawnList = new ArrayList<String>();
		for(Spawn s : spawns){
			spawnList.add(s.toString());
		} 
		config.set("spawns", spawnList);
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile(){
		return new File(CallOfDiamond.dir + File.separator + "maps" + File.separator + id + ".cod_map");
	}
	
	public List<Spawn> getPlayerSpawns(){
		List<Spawn> spawns = new ArrayList<Spawn>();
		for(Spawn s : this.spawns){
			if(s.getType() == SpawnType.Player){
				spawns.add(s);
			}
		}
		return spawns;
	}
	
	public List<Spawn> getBlueTeamSpawns(){
		List<Spawn> spawns = new ArrayList<Spawn>();
		for(Spawn s : this.spawns){
			if(s.getType() == SpawnType.BlueTeam){
				spawns.add(s);
			}
		}
		return spawns;
	}
	
	public List<Spawn> getRedTeamSpawns(){
		List<Spawn> spawns = new ArrayList<Spawn>();
		for(Spawn s : this.spawns){
			if(s.getType() == SpawnType.RedTeam){
				spawns.add(s);
			}
		}
		return spawns;
	}
	
	public List<Spawn> getZombieSpawns(){
		List<Spawn> spawns = new ArrayList<Spawn>();
		for(Spawn s : this.spawns){
			if(s.getType() == SpawnType.Zombie){
				spawns.add(s);
			}
		}
		return spawns;
	}
	
	public void spawnPlayer(Player p){
		List<Spawn> spawns = getPlayerSpawns();
		int i = CallOfDiamond.getRandom().nextInt(spawns.size());
		spawns.get(i).spawnPlayer(this, p);
	}
	
	public void spawnZombie(){
		List<Spawn> spawns = getZombieSpawns();
		int i = CallOfDiamond.getRandom().nextInt(spawns.size());
		world.spawnEntity(spawns.get(i).getLocation(world), EntityType.ZOMBIE);
	}
	
	public void spawnBlueTeam(Player p){
		List<Spawn> spawns = getBlueTeamSpawns();
		int i = CallOfDiamond.getRandom().nextInt(spawns.size());
		spawns.get(i).spawnPlayer(this, p);
	}
	
	public void spawnRedTeam(Player p){
		List<Spawn> spawns = getRedTeamSpawns();
		int i = CallOfDiamond.getRandom().nextInt(spawns.size());
		spawns.get(i).spawnPlayer(this, p);
	}

	public boolean contains(float x, float y){
		if((x > x1 && x < x2) && (y > y1 && y < y2)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean addSpawn(Spawn spawn) {
		if(contains(spawn.getX(), spawn.getZ())){
			spawns.add(spawn);
			return true;
		}
		else{
			return false;
		}
	}
	
}
