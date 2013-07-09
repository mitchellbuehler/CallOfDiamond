package us.chrisix.cod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

import us.chrisix.cod.matchtype.MatchType;
import us.chrisix.cod.matchtype.Team;
import us.chrisix.cod.matchtype.TeamDeathmatch;
import us.chrisix.cod.weapon.WeaponClass;

public class Match {

	private MatchType type;
	private Map map;
	private List<MatchPlayer> players;
	private boolean inlobby = true;
	private String owner;
	
	public Match(Map map, int type, String owner)
	{
		this.owner = owner;
		players = new CopyOnWriteArrayList<MatchPlayer>();
		this.map = map;
		if(type == 0){
			this.type = new TeamDeathmatch(this);
		}
	}
	
	public boolean join(Player p, WeaponClass wc){
		if(players.size() + 1 <= type.getMaxPlayers()){
			players.add(new MatchPlayer(p, wc));
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean inLobby(){
		return inlobby;
	}
	
	public Map getMap(){
		return map;
	}
	
	public MatchType getType(){
		return type;
	}
	
	public static boolean isMatchType(int type){
		if(type == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getAmountOfPlayers(){
		return players.size();
	}
	
	public int getMaxPlayers(){
		return type.getMaxPlayers();
	}
	
	public String getOwner(){
		return owner;
	}

	public void start() {
		if(type.useTeams()){
			Team t = Team.BLUE;
			for(MatchPlayer p : players){
				p.setTeam(t);
				p.start(this);
				if(t == Team.BLUE){
					map.spawnBlueTeam(p.getPlayer());
					t = Team.RED;
				}
				else{
					map.spawnRedTeam(p.getPlayer());
					t = Team.BLUE;
				}
			}
		}
		inlobby = false;
	}

	public MatchPlayer getPlayer(String name) {
		for(MatchPlayer p : players){
			if(p.getPlayer().getName().equalsIgnoreCase(name)){
				return p;
			}
		}
		return null;
	}

	public void leave(Player p) {
		for(MatchPlayer mp : players){
			if(mp.getPlayer().getName().equalsIgnoreCase(p.getName())){
				mp.leave();
				players.remove(mp);
				break;
			}
		}
	}

	public boolean playerIsPlaying(Player p) {
		for(MatchPlayer mp : players){
			if(mp.getPlayer() == p){
				return true;
			}
		}
		return false;
	}
}
