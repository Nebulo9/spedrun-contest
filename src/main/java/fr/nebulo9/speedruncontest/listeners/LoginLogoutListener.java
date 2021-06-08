package fr.nebulo9.speedruncontest.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.nebulo9.speedruncontest.scoreboards.TimerScoreboard;
import fr.nebulo9.speedruncontest.util.Messages;
import org.bukkit.ChatColor;

public class LoginLogoutListener implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(Messages.TITLE.getMessage() + ChatColor.AQUA + "Type " + ChatColor.GOLD + "/runner" + ChatColor.AQUA + " to be a runner." + ChatColor.RESET);
		TimerScoreboard.updateScoreboard(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		TimerScoreboard.removeScore(event.getPlayer());
	}
}
