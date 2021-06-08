package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.util.Messages;
import net.md_5.bungee.api.ChatColor;

public class StartCMD implements CommandExecutor {

	private static GameManager GAME_MANAGER;
	
	public StartCMD(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("start")){
			if(GAME_MANAGER.getStatus() != GameManager.Status.INITIALIZED) {
				sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STARTED.getMessage() + ChatColor.RESET);
			} else {
				GAME_MANAGER.start();
			}
		} else {
			return false;
		}
		return true;
	}
}
