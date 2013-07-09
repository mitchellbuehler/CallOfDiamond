package us.chrisix.cod;

public enum SpawnType {

	Player, Zombie, BlueTeam, RedTeam;
	
	public int getId(){
		if(this == Zombie){
			return 1;
		}
		else if(this == BlueTeam){
			return 2;
		}
		else if(this == RedTeam){
			return 3;
		}
		else{
			return 0;
		}
	}
	
	public static SpawnType getSpawnType(int id){
		for(SpawnType type : SpawnType.values()){
			if(id == type.getId()){
				return type;
			}
		}
		return null;
	}
	
}
