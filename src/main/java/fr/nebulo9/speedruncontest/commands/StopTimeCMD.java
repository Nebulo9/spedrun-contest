package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class StopTimeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("stoptime")) {
			if(!TimerTask.isFinished()) {
				TimerTask.setFinished(true);
			}
		} else {
			return false;
		}
		return true;
	}

}
