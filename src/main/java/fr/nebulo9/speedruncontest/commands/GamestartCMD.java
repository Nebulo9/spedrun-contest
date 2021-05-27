package fr.nebulo9.speedruncontest.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.constants.Messages;

public class GamestartCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("gamestart")) {
			if(!(SCPlugin.isGAME_STARTED())) {
				if(!(SCPlugin.getRunners().isEmpty())) {
					sender.sendMessage(ChatColor.AQUA + Messages.GAME_STARTING.getMessage());
					SCPlugin.setGAME_STARTED(true);
					for(Player p : Bukkit.getOnlinePlayers()) {
						if(SCPlugin.getRunners().contains(p.getUniqueId())) {
							p.setGameMode(GameMode.SURVIVAL);
							p.setInvulnerable(false);
							p.setCollidable(true);
							p.setSaturation(1f);
						} else {
							p.setGameMode(GameMode.SPECTATOR);
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + Messages.RUNNERS_LIST_EMPTY.getMessage());
				}
			} else {
				sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STARTED.getMessage());
			}
		} else {
			return false;
		}
		return true;
	}

}
