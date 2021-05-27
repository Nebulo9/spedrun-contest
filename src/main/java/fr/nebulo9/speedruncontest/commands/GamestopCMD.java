package fr.nebulo9.speedruncontest.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.constants.Messages;

public class GamestopCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("gamestop")) {
			if(SCPlugin.isGAME_STARTED()) {
				sender.sendMessage(ChatColor.AQUA + Messages.GAME_STOPPING.getMessage());
				SCPlugin.setGAME_STARTED(false);
				for(UUID uuid : SCPlugin.getRunners()) {
					SCPlugin.removeRunner(uuid);
				}
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.setGameMode(GameMode.ADVENTURE);
					p.setInvulnerable(true);
					p.setCollidable(false);
					p.setFoodLevel(20);
					p.setSaturation(1e5f);
				}
			} else {
				sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STOPPED.getMessage());
			}
		} else {
			return false;
		}
		return true;
	}

}
