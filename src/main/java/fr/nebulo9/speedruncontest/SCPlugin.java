package fr.nebulo9.speedruncontest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.nebulo9.speedruncontest.commands.GameCMD;
import fr.nebulo9.speedruncontest.commands.RunnerCMD;
import fr.nebulo9.speedruncontest.commands.RunnersCMD;
import fr.nebulo9.speedruncontest.commands.TimerCMD;
import fr.nebulo9.speedruncontest.constants.Messages;
import fr.nebulo9.speedruncontest.scoreboards.TimerScoreboard;
import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class SCPlugin extends JavaPlugin implements Listener{

	private static Set<UUID> RUNNERS = new HashSet<UUID>();
	private static Set<UUID> SPECTATORS = new HashSet<UUID>();
	private static boolean GAME_STARTED = false;
	private static BukkitTask TIMER_TASK;
	private static BukkitTask SCOREBOARD_TASK;
	private static BukkitTask KILL_BRUTES;
	private static Location SPAWN;
	private Player winner;
	
	private String worldName;
	private String worldNameNether;
	private String worldNameEnd;
	private boolean meltedOresDrop;
	
	@Override
	public void onEnable(){
		loadConfig();
		worldName = getConfig().getString("world-name");
		worldNameNether = worldName + "_nether";
		worldNameEnd = worldName + "_the_end";
		meltedOresDrop = getConfig().getBoolean("melted-ores-drop");
		
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
		
		KILL_BRUTES = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getWorld().equals(Bukkit.getWorld(worldNameNether))) {
						for(LivingEntity e : p.getWorld().getLivingEntities()) {
							if(e.getType() == EntityType.PIGLIN_BRUTE) {
								e.damage(1500);
							}
						}
					}
					if(p.getWorld().equals(Bukkit.getWorld(worldName))) {
						for(LivingEntity e : p.getWorld().getLivingEntities()) {
							if(e.getType() == EntityType.PHANTOM) {
								e.damage(1500);
							}
						}
					}
				}
			}
		}.runTaskTimer(this, 0, 1);
		
		getServer().getPluginManager().registerEvents(this, this);
		
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_ENABLED.getMessage());
		SPAWN = new Location(Bukkit.getWorld(worldName),0,Bukkit.getWorld(worldName).getHighestBlockYAt(0, 0)+1,0);
		Bukkit.getWorld(worldName).setSpawnLocation(SPAWN);
	}


	@Override
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(Messages.PLUGIN_DISABLED.getMessage());
		TIMER_TASK.cancel();
		SCOREBOARD_TASK.cancel();
		KILL_BRUTES.cancel();
	}
	
	private void loadConfig() {
		this.saveConfig();
		this.getConfig().addDefault("world-name", "world");
		this.getConfig().addDefault("melted-ores-drop", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldChangeEvent(PlayerChangedWorldEvent event) {
		if(GAME_STARTED) {
			if(RUNNERS.contains(event.getPlayer().getUniqueId())) {
				if(event.getFrom().equals(Bukkit.getWorld(worldNameEnd))) {
					winner = event.getPlayer();
					victory();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerJoinEvent event){
		if(event.getPlayer().getWorld().equals(Bukkit.getWorld(worldName)) || event.getPlayer().getWorld().equals(Bukkit.getWorld(worldNameNether)) || event.getPlayer().getWorld().equals(Bukkit.getWorld(worldNameEnd))) {
			TimerScoreboard test = TimerScoreboard.createScore(event.getPlayer());
		}
		if(!RUNNERS.contains(event.getPlayer().getUniqueId())) {
			addSpectator(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(TimerScoreboard.hasScore(event.getPlayer())) {
			TimerScoreboard.removeScore(event.getPlayer());
		}
	}
	
	public void updateScoreboard(Player p) {
		if(p.getWorld().equals(Bukkit.getWorld(worldName)) || p.getWorld().equals(Bukkit.getWorld(worldNameNether)) || p.getWorld().equals(Bukkit.getWorld(worldNameEnd))) {
			if(TimerScoreboard.hasScore(p)) {
				TimerScoreboard score = TimerScoreboard.getByPlayer(p);
				score.setSlot(1, ChatColor.AQUA + TimerTask.getTime());
			} else {
				TimerScoreboard test = TimerScoreboard.createScore(p);
			}
		}
		
	}

	@EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.getPlayer().getWorld().equals(Bukkit.getWorld(worldName)) || e.getPlayer().getWorld().equals(Bukkit.getWorld(worldNameNether)) || e.getPlayer().getWorld().equals(Bukkit.getWorld(worldNameEnd))) {
        	if(meltedOresDrop) {
            	if(e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
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
            }
        }
    }

	public void victory() {
		TimerTask.setStatus(-1);
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getWorld().equals(Bukkit.getWorld(worldName)) || p.getWorld().equals(Bukkit.getWorld(worldNameNether)) || p.getWorld().equals(Bukkit.getWorld(worldNameEnd))) {
				if(RUNNERS.contains(p.getUniqueId())) {
					removeRunner(p.getUniqueId());
				}
				addSpectator(p.getUniqueId());
				p.teleport(SPAWN);
				p.sendTitle(ChatColor.BLUE + winner.getName() + ChatColor.GOLD + " won the race !", ChatColor.GREEN + "Race time: " + ChatColor.AQUA + TimerTask.getTime(), 10, 4*20, 20);
			}
		}
	}
	
	public static Set<UUID> getRunners() {
		return RUNNERS;
	}
	
	public static void addRunner(UUID uuid) {
		String name = Bukkit.getPlayer(uuid).getName();
		Bukkit.getPlayer(uuid).setDisplayName(ChatColor.GREEN + name + ChatColor.RESET);
		Bukkit.getPlayer(uuid).setPlayerListName(Bukkit.getOfflinePlayer(uuid).getPlayer().getDisplayName());
		Bukkit.getPlayer(uuid).setInvulnerable(true);
		Bukkit.getPlayer(uuid).setCollidable(false);
		Bukkit.getPlayer(uuid).setSaturation(10000f);
		RUNNERS.add(uuid);
	}
	
	public static void removeRunner(UUID uuid) {
		String name = Bukkit.getPlayer(uuid).getName();
		Bukkit.getPlayer(uuid).setDisplayName(ChatColor.RESET + name + ChatColor.RESET);
		Bukkit.getPlayer(uuid).setPlayerListName(Bukkit.getOfflinePlayer(uuid).getPlayer().getDisplayName());
		RUNNERS.remove(uuid);
	}
	
	public static boolean isGAME_STARTED() {
		return GAME_STARTED;
	}
	
	public static void setGAME_STARTED(boolean gameStatus) {
		GAME_STARTED = gameStatus;
	}

	public String getWorldName() {
		return worldName;
	}


	public static BukkitTask getTimerTask() {
		return TIMER_TASK;
	}
	
	public static Location getSPAWN() {
		return SPAWN;
	}

	public static void addSpectator(UUID uuid) {
		SPECTATORS.add(uuid);
		Bukkit.getPlayer(uuid).setGameMode(GameMode.SPECTATOR);
	}
	
	public static void removeSpectator(UUID uuid) {
		SPECTATORS.remove(uuid);
		Bukkit.getPlayer(uuid).setGameMode(GameMode.ADVENTURE);
	}


	public String getWorldNameNether() {
		return worldNameNether;
	}


	public String getWorldNameEnd() {
		return worldNameEnd;
	}
	
}
