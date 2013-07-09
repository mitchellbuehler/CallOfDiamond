package us.chrisix.cod;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Spawn {

	private float x, y, z, pitch, yaw;
	private SpawnType type;
	
	public Spawn(SpawnType type, float x, float y, float z, float yaw, float pitch){
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.type = type;
	}
	
	public Spawn(String code) {
		String args[] = code.split(" ");
		x = Float.parseFloat(args[0]);
		y = Float.parseFloat(args[1]);
		z = Float.parseFloat(args[2]);
		yaw = Float.parseFloat(args[3]);
		pitch = Float.parseFloat(args[4]);
		type = SpawnType.getSpawnType(Integer.parseInt(args[5]));
	}

	public SpawnType getType(){
		return type;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	public void spawnPlayer(Map m, Player p){
		p.teleport(new Location(m.getWorld(), x, y, z, yaw, pitch));
	}
	
	@Override
	public String toString(){
		return x + " " + y + " " + z + " " + yaw + " " + pitch + " " + type.getId();
	}

	public Location getLocation(World world) {
		return new Location(world, x, y, z);
	}
	
}
