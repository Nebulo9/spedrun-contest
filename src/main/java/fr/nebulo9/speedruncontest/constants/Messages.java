package fr.nebulo9.speedruncontest.constants;

import org.bukkit.ChatColor;
/**
 * An Enum to stores the constant messages that can be easily used in the entire plugin.
 * @author Nebulo9
 */
public enum Messages {
	
	PLUGIN_ENABLED("SpeedrunContest enabled."),
	PLUGIN_DISABLED("SpeedrunContests disabled"),
	
	CONSOLE_EXECUTOR_ERROR("This command must be performed by a player."),
	
	PLAYER_ALREADY_RUNNER("You are already a runner."),
	
	PLAYER_IS_NOW_RUNNER("You are now a runner."),
	PLAYER_NO_MORE_RUNNER("You are no more a runner."),
	
	RUNNERS_LIST_TITLE(ChatColor.GOLD + "-----[" + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "SPEEDRUNNERS" + ChatColor.GOLD + "]-----" + ChatColor.RESET + "\n"),
	
	RUNNERS_LIST_EMPTY("There are no runners."),
	GAME_STARTING("The game is starting."),
	GAME_STOPPING("The game has been stopped."),
	GAME_ALREADY_STARTED("A game is currently running."),
	GAME_ALREADY_STOPPED("There is no game running.")
	
	;
	
	private String message;
	
	Messages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
