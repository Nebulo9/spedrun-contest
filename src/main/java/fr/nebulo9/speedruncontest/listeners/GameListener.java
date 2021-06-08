package fr.nebulo9.speedruncontest.listeners;

import fr.nebulo9.speedruncontest.managers.GameManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class GameListener implements Listener{
	
	private static GameManager GAME_MANAGER;
	
	public GameListener(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.ENDER_DRAGON){
			if(event.getEntity().getKiller() != null) {
				GAME_MANAGER.stop(event.getEntity().getKiller());
			}
		}
	}
}
