package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

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
					if(SCPlugin.getRunners().contains(p.getUniqueId())) {
						SCPlugin.removeRunner(p.getUniqueId());
						p.sendMessage(ChatColor.GOLD + Messages.PLAYER_NO_MORE_RUNNER.getMessage());
					} else {
						SCPlugin.addRunner(p.getUniqueId());
						p.sendMessage(ChatColor.GOLD + Messages.PLAYER_IS_NOW_RUNNER.getMessage());
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
