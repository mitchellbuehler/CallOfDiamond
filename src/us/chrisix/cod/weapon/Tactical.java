package us.chrisix.cod.weapon;

import org.bukkit.inventory.ItemStack;

public enum Tactical {

	NOTHING, HEALING, NIGHT_VISION;
	
	public int getId(){
		if(this == HEALING){
			return 1;
		}
		else if(this == NIGHT_VISION){
			return 2;
		}
		else{
			return 0;
		}
	}
	
	public static Tactical getTactical(int id){
		for(Tactical t : Tactical.values()){
			if(t.getId() == id){
				return t;
			}
		}
		return null;
	}
	
	public ItemStack getItemStack() {
		if(this == HEALING){
			return new ItemStack(373, 1, (short)8197);
		}
		else if(this == NIGHT_VISION){
			return new ItemStack(373, 1, (short)8262);
		}
		else{
			return null;
		}
	}
	
}
