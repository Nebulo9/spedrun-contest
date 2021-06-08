package fr.nebulo9.speedruncontest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.util.Messages;


public class RunnerCMD implements CommandExecutor {
	
	private static GameManager GAME_MANAGER;

	public RunnerCMD(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("runner")) {
			if(sender instanceof Player) {
				if(args.length == 0) {
					Player p = (Player)sender;
					if(GAME_MANAGER.getStatus() != GameManager.Status.INITIALIZED) {
						p.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STARTED.getMessage());
					} else {
						if(GAME_MANAGER.getRunners().contains(p)) {
							GAME_MANAGER.removeRunner(p);
							p.sendMessage(ChatColor.AQUA + Messages.PLAYER_NO_MORE_RUNNER.getMessage());
						} else {
							GAME_MANAGER.addRunner(p);
							p.sendMessage(ChatColor.AQUA + Messages.PLAYER_IS_NOW_RUNNER.getMessage());
						}
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
