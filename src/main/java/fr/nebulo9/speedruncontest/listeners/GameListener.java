package fr.nebulo9.speedruncontest.listeners;

import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import fr.nebulo9.speedruncontest.managers.GameManager;

public class GameListener implements Listener{
	
	private static GameManager GAME_MANAGER;
	
	public GameListener(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}
	
	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if(GAME_MANAGER.getStatus() == GameManager.Status.STARTED) {
			if(GAME_MANAGER.getRunners().contains(event.getPlayer())) {
				if(event.getFrom().getEnvironment() == Environment.THE_END) {
					
				}
			}
		}
	}
}
