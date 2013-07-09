package us.chrisix.cod.weapon;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.chrisix.cod.CallOfDiamond;

public class WeaponClass {

	private int id;
	private String name;
	private Weapon primary, secondary;
	private Lethal lethal;
	private Tactical tactical;
	
	public WeaponClass(int id, String name, Weapon primary, Weapon secondary, Lethal lethal, Tactical tactical){
		this.id = id;
		this.primary = primary;
		this.secondary = secondary;
		this.lethal = lethal;
		this.tactical = tactical;
	}
	
	public Weapon getPrimary(){
		return primary;
	}
	public void setPrimary(Weapon w){
		primary = w;
	}
	public Weapon getSecondary(){
		return secondary;
	}
	
	public void setSecondary(Weapon w){
		secondary = w;
	}
	
	public Lethal getLethal(){
		return lethal;
	}
	
	public void setLethal(Lethal l){
		lethal = l;
	}
	
	public Tactical getTactical(){
		return tactical;
	}
	
	public void setTactical(Tactical t){
		tactical = t;
	}
	
	public ItemStack[] dumpInventory(Player p){
		ItemStack[] content = p.getInventory().getContents();
		p.getInventory().clear();
		return content;
	}
	
	public void equipClass(Player p){
		p.getInventory().setItem(0, new ItemStack(primary.getMaterial()));
		p.getInventory().setItem(1, new ItemStack(secondary.getMaterial()));
		p.getInventory().setItem(2, lethal.getItemStack());
		p.getInventory().setItem(3, tactical.getItemStack()); 
	}
	
	public int getId(){
		return id;
	}
	
	public void save(String player){
		File f = new File(CallOfDiamond.dir + File.separator +  "classes" + File.separator + player + "_" + id + ".wc");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		config.set("name", name);
		config.set("primary", primary.getId());
		config.set("secondary", secondary.getId());
		config.set("lethal", lethal.getId());
		config.set("tactical", tactical.getId());
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile(String player){
		return new File(CallOfDiamond.dir + File.separator + "classes" + File.separator + player + "_" + id + ".wc");
	}
	
	public static WeaponClass load(String player, int id){
		File f = new File(CallOfDiamond.dir + File.separator +  "classes" + File.separator + player + "_" + id + ".wc");
		if(f.exists()){
			FileConfiguration config = YamlConfiguration.loadConfiguration(f);
			return new WeaponClass(id, config.getString("name"), Weapon.getWeapon(config.getInt("primary")), Weapon.getWeapon(config.getInt("secondary")), Lethal.getLethal(config.getInt("lethal")), Tactical.getTactical(config.getInt("tactical")));
		}
		return null;
	}
	
	public static WeaponClass empty(String player, String name){
		int i = 0;
		while(true){
			if(!new File(CallOfDiamond.dir + File.separator +  "classes" + File.separator + player + "_" + i + ".wc").exists()){
				return new WeaponClass(i, name, null, null, null, null);
			}
		}
	}
}
