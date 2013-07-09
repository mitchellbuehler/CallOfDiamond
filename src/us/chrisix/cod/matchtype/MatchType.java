package us.chrisix.cod.matchtype;

import us.chrisix.cod.Match;
import us.chrisix.cod.MatchPlayer;

public abstract class MatchType {

	private int maxKills = 60, maxPlayers = 12, lives = 0;
	private long time = 300000;
	private boolean allowCustomClasses = true;
	private Match match;
	
	public MatchType(Match match){
		this.match = match;
	}
	
	public void set(String setting, String value){
		if(setting.equalsIgnoreCase("max-kills")){
			maxKills = Integer.parseInt(value);
		}
		else if(setting.equalsIgnoreCase("max-players")){
			maxPlayers = Integer.parseInt(value);
		}
		else if(setting.equalsIgnoreCase("time")){
			time = Long.parseLong(value);
		}
		else if(setting.equalsIgnoreCase("lives")){
			lives = Integer.parseInt(value);
		}
		else if(setting.equalsIgnoreCase("allow-custom-classes")){
			allowCustomClasses = Boolean.parseBoolean(value);
		}
		onSet(setting, value);
	}
	
	public int getMaxKills(){
		return maxKills;
	}
	
	public int getMaxPlayers(){
		return maxPlayers;
	}
	
	public int getLives(){
		return lives;
	}
	
	public long getTime(){
		return time;
	}
	
	public boolean allowCustomClasses(){
		return allowCustomClasses;
	}
	
	public Match getMatch(){
		return match;
	}
		
	public void onSet(String setting, String value){ }
	public void onPlayerKilled(MatchPlayer killer, MatchPlayer victim){ }
	public abstract boolean useTeams();
	public abstract String getName();

}
