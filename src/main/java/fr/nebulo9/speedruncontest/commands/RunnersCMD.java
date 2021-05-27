package fr.nebulo9.speedruncontest.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.constants.Messages;
import net.md_5.bungee.api.ChatColor;

public class RunnersCMD implements CommandExecutor {

	@SuppressWarnings("unused")
	private static SCPlugin PLUGIN;
	
	public RunnersCMD(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("runners")) {
			if(args.length == 0) {
				String title = Messages.RUNNERS_LIST_TITLE.getMessage();
				String message = new String();
				if(SCPlugin.getRunners().isEmpty()) {
					message = ChatColor.GOLD + Messages.RUNNERS_LIST_EMPTY.getMessage();
				} else {
					int i = 0;
					for(UUID uuid : SCPlugin.getRunners()) {
						i++;
						message += ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName();
						if(i+1 > SCPlugin.getRunners().size()) {
							message += ChatColor.GOLD + ".";
						} else {
							message += ChatColor.GOLD + ", ";
						}
					}
				}
				sender.sendMessage(title + message);
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

}
