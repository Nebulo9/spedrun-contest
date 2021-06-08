package fr.nebulo9.speedruncontest.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.tasks.TimerTask;
import fr.nebulo9.speedruncontest.util.Messages;

public class TimerCMD implements CommandExecutor {

	private static GameManager GAME_MANAGER;
	
	public TimerCMD(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("timer")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("get")) {
					sender.sendMessage(TimerTask.getTime());
				} else if(args[0].equalsIgnoreCase("pause")) {
					if(TimerTask.getStatus() == TimerTask.Status.STARTED) {
						TimerTask.setStatus(TimerTask.Status.PAUSED);
						sender.sendMessage(ChatColor.GOLD + Messages.TIMER_PAUSED.getMessage() + ChatColor.RESET);
					} else {
						sender.sendMessage(ChatColor.RED + Messages.TIMER_PAUSED_OR_STOPPED.getMessage() + ChatColor.RESET);
					}
				} else if(args[0].equalsIgnoreCase("stop")) {
					if(TimerTask.getStatus() == TimerTask.Status.PAUSED) {
						TimerTask.setStatus(TimerTask.Status.STOPPED);
						sender.sendMessage(ChatColor.RED + Messages.TIMER_STOPPED.getMessage() + ChatColor.RESET);
					} else {
						sender.sendMessage(ChatColor.RED + Messages.TIMER_PAUSED_OR_STOPPED.getMessage() + ChatColor.RESET);
					}
				} else if(args[0].equalsIgnoreCase("resume")) {
					if(TimerTask.getStatus() == TimerTask.Status.PAUSED) {
						TimerTask.setStatus(TimerTask.Status.STARTED);
						sender.sendMessage(ChatColor.GOLD + Messages.TIMER_RESUMED.getMessage() + ChatColor.RESET);
					} else {
						sender.sendMessage(ChatColor.RED + Messages.TIMER_PAUSED_OR_STOPPED.getMessage() + ChatColor.RESET);
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

}
