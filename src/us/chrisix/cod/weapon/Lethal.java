package us.chrisix.cod.weapon;

import org.bukkit.inventory.ItemStack;

public enum Lethal {

	NOTHING, DAMAGE1, DAMAGE2, POSION;
	
	public int getId(){
		if(this == DAMAGE1){
			return 1;
		}
		else if(this == DAMAGE2){
			return 2;
		}
		else if(this == POSION){
			return 3;
		}
		else{
			return 0;
		}
	}
	
	public static Lethal getLethal(int id){
		for(Lethal l : Lethal.values()){
			if(l.getId() == id){
				return l;
			}
		}
		return null;
	}

	public ItemStack getItemStack() {
		if(this == DAMAGE1){
			return new ItemStack(373, 2, (short)16396);
		}
		else if(this == DAMAGE2){
			return new ItemStack(373, 2, (short)16428);
		}
		else if(this == POSION){
			return new ItemStack(373, 2, (short)16388);
		}
		else{
			return null;
		}
	}
	
}
