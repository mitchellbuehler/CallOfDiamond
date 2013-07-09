package us.chrisix.cod;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CodListener implements Listener{

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(e.getPlayer().getItemInHand().getType() == Material.DIRT){
			for(Map m : CallOfDiamond.getMaps()){
				e.setCancelled(m.step(e.getPlayer(), e.getBlock().getX(), e.getBlock().getZ()));
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(e.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK && e.getEntity().getLastDamageCause().getEntityType() == EntityType.PLAYER){
			Player victim = e.getEntity();
			Player killer = e.getEntity().getKiller();
			for(Match match : CallOfDiamond.getMatches()){
				if(match.playerIsPlaying(victim) && match.playerIsPlaying(killer)){
					onPlayerKilled(new PlayerKilledEvent(match, match.getPlayer(victim.getName()), match.getPlayer(killer.getName())));
					break;
				}
			}
		}
	}
	
	public void onPlayerKilled(PlayerKilledEvent e){
		e.getVictim().died();
		e.getKiller().killed();
	}
	
}
