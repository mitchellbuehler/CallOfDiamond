package us.chrisix.cod;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.chrisix.cod.matchtype.Team;
import us.chrisix.cod.weapon.WeaponClass;

public class MatchPlayer {

	private Player p;
	private WeaponClass wc;
	private int kills = 0;
	private int deaths = 0;
	private int killstreak = 0;
	private int deathstreak;
	private ItemStack[] dump;
	private GameMode mode;
	private Location last;
	private Team t;
	
	public MatchPlayer(Player p, WeaponClass wc){
		this.p = p;
		this.wc = wc;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public WeaponClass getWeaponClass(){
		return wc;
	}
	
	public void reset(){
		kills = 0;
		killstreak = 0;
		deaths = 0;
		t = null;
	}
	
	public void killed(){
		kills += 1;
		killstreak += 1;
	}
	
	public void died(){
		deaths += 1;
		killstreak = 0;
	}
	
	public void start(Match m){
		dump = wc.dumpInventory(p);
		mode = p.getGameMode();
		last = p.getLocation();
		wc.equipClass(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.setHealth(p.getMaxHealth());
		p.sendMessage(ChatColor.AQUA + "== " + m.getType().getName() + " ==");
	}
	
	public void leave(){
		p.getInventory().setContents(dump);
		p.setGameMode(mode);
		p.teleport(last);
	}

	public void setTeam(Team t) {
		this.t = t;
	}
	
	public int getKillstreak(){
		return killstreak;
	}
	
	public int getDeaths(){
		return deaths;
	}
	
	public int getKills(){
		return kills;
	}
	
	public int getDeathstreak(){
		return deathstreak;
	}
	
	public Team getTeam(){
		return t;
	}
	
}
