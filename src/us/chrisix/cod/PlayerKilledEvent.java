package us.chrisix.cod;

public class PlayerKilledEvent {

	private MatchPlayer victim, killer;
	private Match match;
	
	public PlayerKilledEvent(Match match, MatchPlayer victim, MatchPlayer killer){
		this.victim = victim;
		this.killer = killer;
		this.match = match;
	}
	
	public MatchPlayer getVictim(){
		return victim;
	}
	
	public MatchPlayer getKiller(){
		return killer;
	}
	
	public Match getMatch(){
		return match;
	}
	
}
