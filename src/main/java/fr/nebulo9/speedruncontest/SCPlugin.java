package fr.nebulo9.speedruncontest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Location;

import fr.nebulo9.speedruncontest.commands.GameCMD;
import fr.nebulo9.speedruncontest.commands.RunnerCMD;
import fr.nebulo9.speedruncontest.commands.RunnersCMD;
import fr.nebulo9.speedruncontest.commands.TestCMD;
import fr.nebulo9.speedruncontest.commands.TimerCMD;
import fr.nebulo9.speedruncontest.constants.Messages;
import fr.nebulo9.speedruncontest.scoreboards.TimerScoreboard;
import fr.nebulo9.speedruncontest.tasks.TimerTask;

/**
 * Main Plugin class.
 * @author Nebulo9
 */
public class SCPlugin extends JavaPlugin implements Listener{

	private static Set<UUID> RUNNERS = new HashSet<UUID>();
	private static Set<UUID> SPECTATORS;
	private static boolean GAME_STARTED = false;
	private static BukkitTask TIMER_TASK;
	private static BukkitTask SCOREBOARD_TASK;
	private static final Location SPAWN = new Location(Bukkit.getWorld("world"),0,Bukkit.getWorld("world").getHighestBlockYAt(0, 0),0);
	private Player winner;
	
	@Override
	public void onEnable(){
		
		getCommand("runner").setExecutor(new RunnerCMD(this));
		getCommand("runners").setExecutor(new RunnersCMD(this));
		getCommand("game").setExecutor(new GameCMD(this));
		getCommand("timer").setExecutor(new TimerCMD());
		
		
		
		TIMER_TASK = new TimerTask(this,0).runTaskTimer(this, 0L, 20L);
		SCOREBOARD_TASK = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					updateScoreboard(p);
				}
			}
		}.runTaskTimer(this, 0L, 20L);
		
		getServer().getPluginManager().registerEvents(this, this);
		
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_ENABLED.getMessage());
	}
	
	@Override
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_DISABLED.getMessage());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPortalEvent(PlayerChangedWorldEvent event) {
		if(GAME_STARTED) {
			if(event.getFrom().getEnvironment().equals(World.Environment.THE_END)) {
				winner = event.getPlayer();
				TimerTask.setStatus(-1);
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.setGameMode(GameMode.SPECTATOR);
					p.teleport(SPAWN);
					p.sendTitle(ChatColor.BLUE + winner.getName() + ChatColor.GOLD + " won the race !", ChatColor.GREEN + "Race time: " + ChatColor.AQUA + TimerTask.getTime(), 10, 4*20, 20);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerJoinEvent event){
		TimerScoreboard test = TimerScoreboard.createScore(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(TimerScoreboard.hasScore(event.getPlayer())) {
			TimerScoreboard.removeScore(event.getPlayer());
		}
	}
	
	public void updateScoreboard(Player p) {
		if(TimerScoreboard.hasScore(p)) {
			TimerScoreboard score = TimerScoreboard.getByPlayer(p);
			score.setSlot(1, ChatColor.AQUA + TimerTask.getTime());
		} else {
			TimerScoreboard test = TimerScoreboard.createScore(p);
		}
	}
	
	public static void setTIMER_TASK(BukkitTask timerTask) {
		TIMER_TASK = timerTask;
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
