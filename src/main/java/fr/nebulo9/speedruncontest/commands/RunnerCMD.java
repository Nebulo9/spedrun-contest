package fr.nebulo9.speedruncontest.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.constants.Messages;


public class RunnerCMD implements CommandExecutor {

	public static SCPlugin PLUGIN;
	
	public RunnerCMD(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("runner")) {
			if(sender instanceof Player) {
				if(args.length == 0) {
					Player p = (Player)sender;
					if(p.getWorld().equals(Bukkit.getWorld(PLUGIN.getWorldName())) || p.getWorld().equals(Bukkit.getWorld(PLUGIN.getWorldNameNether())) || p.getWorld().equals(Bukkit.getWorld(PLUGIN.getWorldNameEnd()))) {
						if(!SCPlugin.isGAME_STARTED()) {
							if(SCPlugin.getRunners().contains(p.getUniqueId())) {
								SCPlugin.removeRunner(p.getUniqueId());
								SCPlugin.addSpectator(p.getUniqueId());
								p.teleport(SCPlugin.getSPAWN());
								p.sendMessage(ChatColor.GOLD + Messages.PLAYER_NO_MORE_RUNNER.getMessage());
							} else {
								SCPlugin.addRunner(p.getUniqueId());
								SCPlugin.removeSpectator(p.getUniqueId());
								p.sendMessage(ChatColor.GOLD + Messages.PLAYER_IS_NOW_RUNNER.getMessage());
								p.teleport(SCPlugin.getSPAWN());
							}
						} else {
							p.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STARTED.getMessage());
						}
					} else {
						p.sendMessage(ChatColor.RED + Messages.WRONG_WORLD.getMessage());
					}
				} else {
					return false;
				}
			} else {
				sender.sendMessage(Messages.CONSOLE_EXECUTOR_ERROR.getMessage());
			}
		} else {
			return false;
		}
		return true;
	}
}
