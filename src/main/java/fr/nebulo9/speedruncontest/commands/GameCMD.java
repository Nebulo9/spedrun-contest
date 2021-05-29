package fr.nebulo9.speedruncontest.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.constants.Messages;
import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class GameCMD implements CommandExecutor {

	private static SCPlugin PLUGIN;
	
	public GameCMD(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("game")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start")) {
					if(!(SCPlugin.isGAME_STARTED() && TimerTask.getStatus() <= 0)) {
						if(!(SCPlugin.getRunners().isEmpty())) {
							sender.sendMessage(ChatColor.AQUA + Messages.GAME_STARTING.getMessage() + ChatColor.RESET);
							TimerTask.setStatus(1);
							for(Player p : Bukkit.getOnlinePlayers()) {
								if(SCPlugin.getRunners().contains(p.getUniqueId())) {
									p.setGameMode(GameMode.SURVIVAL);
									p.setInvulnerable(false);
									p.setCollidable(true);
									p.setSaturation(1f);
								} else {
									p.setGameMode(GameMode.SPECTATOR);
								}
							}
						} else {
							sender.sendMessage(ChatColor.RED + Messages.RUNNERS_LIST_EMPTY.getMessage() + ChatColor.RESET);
						}
					} else {
						sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STARTED.getMessage());
					}
				} else if(args[0].equalsIgnoreCase("pause")) {
					if(SCPlugin.isGAME_STARTED()) {
						if(TimerTask.getStatus() == 1) {
							TimerTask.setStatus(0);
							sender.sendMessage(ChatColor.AQUA + Messages.GAME_PAUSED.getMessage() + ChatColor.RESET);
						} else {
							sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_PAUSED.getMessage() + ChatColor.RESET);
						}
					} else {
						sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STOPPED.getMessage() + ChatColor.RESET);
					}
				} else if(args[0].equalsIgnoreCase("stop")) {
					if(SCPlugin.isGAME_STARTED() && TimerTask.getStatus() >= 0){
						sender.sendMessage(ChatColor.AQUA + Messages.GAME_STOPPING.getMessage() + ChatColor.RESET);
						TimerTask.setStatus(-1);
						for(UUID uuid : SCPlugin.getRunners()) {
							SCPlugin.removeRunner(uuid);
						}
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.setGameMode(GameMode.SPECTATOR);
						}
					} else {
						sender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STOPPED.getMessage() + ChatColor.RESET);
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
