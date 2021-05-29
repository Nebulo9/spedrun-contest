package fr.nebulo9.speedruncontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("test")) {
			sender.sendMessage(((Player)sender).getLocation().getWorld().getEnvironment().name());
		} else {
			return false;
		}
		return true;
	}

}
