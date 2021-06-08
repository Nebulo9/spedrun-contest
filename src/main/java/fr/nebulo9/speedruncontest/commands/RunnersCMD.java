package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.util.Messages;
import net.md_5.bungee.api.ChatColor;

public class RunnersCMD implements CommandExecutor {
	
	private static GameManager GAME_MANAGER;
	
	public RunnersCMD(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("runners")) {
			if(args.length == 0) {
				String title = Messages.TITLE.getMessage();
				String message = new String();
				if(GAME_MANAGER.getRunners().isEmpty()) {
					message = ChatColor.GOLD + Messages.RUNNERS_LIST_EMPTY.getMessage();
				} else {
					int i = 0;
					for(Player p : GAME_MANAGER.getRunners()) {
						i++;
						message += ChatColor.GREEN + p.getName();
						if(i+1 > GAME_MANAGER.getRunners().size()) {
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
