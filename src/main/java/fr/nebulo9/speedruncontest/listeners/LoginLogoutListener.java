package fr.nebulo9.speedruncontest.listeners;

import fr.nebulo9.speedruncontest.managers.GameManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.nebulo9.speedruncontest.scoreboards.TimerScoreboard;
import fr.nebulo9.speedruncontest.util.Messages;
import org.bukkit.ChatColor;

public class LoginLogoutListener implements Listener{
	private static GameManager GAME_MANAGER;

	public LoginLogoutListener(GameManager gameManager){
		GAME_MANAGER = gameManager;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(GAME_MANAGER.getStatus() != GameManager.Status.STARTED){
			event.getPlayer().sendMessage(Messages.TITLE.getMessage() + ChatColor.AQUA + "Type " + ChatColor.GOLD + "/runner" + ChatColor.AQUA + " to be a runner." + ChatColor.RESET);
			event.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
		TimerScoreboard.updateScoreboard(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		TimerScoreboard.removeScore(event.getPlayer());
	}
}
