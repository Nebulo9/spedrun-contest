package fr.nebulo9.speedruncontest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.nebulo9.speedruncontest.constants.Messages;
import fr.nebulo9.speedruncontest.tasks.TimerTask;
import fr.nebulo9.speedruncontest.commands.GamestartCMD;
import fr.nebulo9.speedruncontest.commands.GamestopCMD;
import fr.nebulo9.speedruncontest.commands.GetTimeCMD;
import fr.nebulo9.speedruncontest.commands.RunnerCMD;
import fr.nebulo9.speedruncontest.commands.RunnersCMD;
import fr.nebulo9.speedruncontest.commands.StopTimeCMD;

/**
 * Main Plugin class.
 * @author Nebulo9
 */
public class SCPlugin extends JavaPlugin implements Listener{

	private static Set<UUID> RUNNERS = new HashSet<UUID>();
	private static Set<UUID> SPECTATORS;
	private static boolean GAME_STARTED = false;
	private static BukkitTask TIMER_TASK;
	
	@Override
	public void onEnable(){
		getCommand("runner").setExecutor(new RunnerCMD(this));
		getCommand("runners").setExecutor(new RunnersCMD(this));
		getCommand("gamestart").setExecutor(new GamestartCMD());
		getCommand("gamestop").setExecutor(new GamestopCMD());
		getCommand("gettime").setExecutor(new GetTimeCMD());
		getCommand("stoptime").setExecutor(new StopTimeCMD());
		
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			setScoreboard(p);
		}
		
		TIMER_TASK = new TimerTask(this).runTaskTimer(this, 0L, 20L);
		
		getServer().getPluginManager().registerEvents(this, this);
		
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_ENABLED.getMessage());
	}
	
	@Override
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_DISABLED.getMessage());
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event){
		if(event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			event.allow();
			setScoreboard(event.getPlayer());
		}
	}
	
	/**
	 * Changing the drop of Gold Ores and Iron Ores to their ingots.
	 * @param e the {@link org.bukkit.event.block.BlockBreakEvent Event} provided by a player.
	 */
	@EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if(block.getType() == Material.GOLD_ORE){
            e.setDropItems(false);
            e.setExpToDrop(1);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
        } else if(block.getType() == Material.IRON_ORE) {
        	e.setDropItems(false);
            e.setExpToDrop(1);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
        }
    }
	
	public void setScoreboard(Player p) {
		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		Scoreboard sb = sbm.getNewScoreboard();
		Objective o = sb.registerNewObjective("test","dummy", ChatColor.BOLD.toString() + "Test");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score sc1 = o.getScore(ChatColor.GREEN + "Ceci est");
		Score sc2 = o.getScore(ChatColor.RED + "un test.");
		sc1.setScore(2);
		sc2.setScore(1);
		
		p.setScoreboard(sb);
	}

	/**
	 * Returns the {@link java.util.Set Set} of the runners' {@link java.util.UUID UUID}.
	 * @return a {@link java.util.Set Set} of {@link java.util.UUID UUID}s.
	 */
	public static Set<UUID> getRunners() {
		return RUNNERS;
	}
	
	/**
	 * Adds the {@link java.util.UUID UUID} of a player to the runners.
	 * @param uuid the {@link java.util.UUID UUID} of the player.
	 */
	public static void addRunner(UUID uuid) {
		String name = Bukkit.getPlayer(uuid).getName();
		Bukkit.getPlayer(uuid).setDisplayName(ChatColor.GREEN + name + ChatColor.RESET);
		Bukkit.getPlayer(uuid).setPlayerListName(Bukkit.getOfflinePlayer(uuid).getPlayer().getDisplayName());
		RUNNERS.add(uuid);
	}
	
	/**
	 * Removes the {@link java.util.UUID UUID} of a player to the runners.
	 * @param uuid the {@link java.util.UUID UUID} of the player.
	 */
	public static void removeRunner(UUID uuid) {
		String name = Bukkit.getPlayer(uuid).getName();
		Bukkit.getPlayer(uuid).setDisplayName(ChatColor.RESET + name + ChatColor.RESET);
		Bukkit.getPlayer(uuid).setPlayerListName(Bukkit.getOfflinePlayer(uuid).getPlayer().getDisplayName());
		RUNNERS.remove(uuid);
	}

	public static Set<UUID> getSpectators() {
		return SPECTATORS;
	}
	
	public static void addSpectator(UUID uuid) {
		SPECTATORS.add(uuid);
	}
	
	public static void removeSpectator(UUID uuid) {
		SPECTATORS.remove(uuid);
	}

	/**
	 * Returns the boolean status of the game.
	 * @return a boolean.
	 */
	public static boolean isGAME_STARTED() {
		return GAME_STARTED;
	}

	/**
	 * Sets the status of the game.
	 * @param gameStatus the boolean status to set.
	 */
	public static void setGAME_STARTED(boolean gameStatus) {
		GAME_STARTED = gameStatus;
	}

	public static BukkitTask getTimerTask() {
		return TIMER_TASK;
	}
	
	
}
