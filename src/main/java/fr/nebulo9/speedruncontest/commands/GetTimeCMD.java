package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class GetTimeCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("gettime")) {
			sender.sendMessage(TimerTask.getTime());
		} else {
			return false;
		}
		return true;
	}

}
