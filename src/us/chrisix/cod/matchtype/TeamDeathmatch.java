package us.chrisix.cod.matchtype;

import us.chrisix.cod.Match;

public class TeamDeathmatch extends MatchType{

	public TeamDeathmatch(Match match) {
		super(match);
	}

	@Override
	public boolean useTeams() {
		return true;
	}

	@Override
	public String getName() {
		return "Team Deathmatch";
	}
	
	
	
}
