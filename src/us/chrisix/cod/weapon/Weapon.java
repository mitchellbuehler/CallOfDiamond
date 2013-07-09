package us.chrisix.cod.weapon;

import org.bukkit.Material;

public enum Weapon {

	NOTHING, WOOD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, BOW;
	
	public int getId(){
		if(this == WOOD_SWORD){
			return 1;
		}
		else if(this == STONE_SWORD){
			return 2;
		}
		else if(this == IRON_SWORD){
			return 3;
		}
		else if(this == DIAMOND_SWORD){
			return 4;
		}
		else if(this == BOW){
			return 5;
		}
		else{
			return 0;
		}
	}
	
	public Material getMaterial(){
		if(this == WOOD_SWORD){
			return Material.WOOD_SWORD;
		}
		else if(this == STONE_SWORD){
			return Material.STONE_SWORD;
		}
		else if(this == IRON_SWORD){
			return Material.IRON_SWORD;
		}
		else if(this == DIAMOND_SWORD){
			return Material.DIAMOND_SWORD;
		}
		else if(this == BOW){
			return Material.BOW;
		}
		else{
			return null;
		}
	}
	
	public static Weapon getWeapon(int id){
		for(Weapon w : Weapon.values()){
			if(w.getId() == id){
				return w;
			}
		}
		return null;
	}
	
}
