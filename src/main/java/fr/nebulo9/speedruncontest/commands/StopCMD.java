package fr.nebulo9.speedruncontest.commands;

import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopCMD implements CommandExecutor {

    private static GameManager GAME_MANAGER;

    public StopCMD(GameManager gameManager){
        GAME_MANAGER = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("stop")){
            if(GAME_MANAGER.getStatus() != GameManager.Status.STARTED){
                commandSender.sendMessage(ChatColor.RED + Messages.GAME_ALREADY_STOPPED.getMessage() + ChatColor.RESET);
            } else {
                GAME_MANAGER.stop(null);
            }
        } else {
            return false;
        }
        return true;
    }
}
