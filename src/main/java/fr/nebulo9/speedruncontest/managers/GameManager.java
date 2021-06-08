package fr.nebulo9.speedruncontest.managers;

import java.util.HashSet;
import java.util.Set;

import fr.nebulo9.speedruncontest.scoreboards.TimerScoreboard;
import fr.nebulo9.speedruncontest.util.Messages;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class GameManager {
	private static SCPlugin PLUGIN;
	
	private final Set<Player> runners = new HashSet<>();
	private final Set<Player> spectators = new HashSet<>();
	
	private static BukkitTask TIMER;
	private static BukkitTask PIGLIN_REMOVE;
	private static BukkitTask SCOREBOARD;
	private Team runnersTeam;
	private boolean dragonKilled;
	private Status status;

	private Location spawn;
	
	public GameManager(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	public void setup() {
		Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
		
		runnersTeam = sc.getTeam("runners");
		if(runnersTeam == null) {
			runnersTeam = sc.registerNewTeam("runners");
			runnersTeam.setColor(ChatColor.GREEN);
		}
		
		for(String entry : runnersTeam.getEntries()) {
			runnersTeam.removeEntry(entry);
		}

		spawn = Bukkit.getWorld(PLUGIN.getConfiguration().getStringValue("world-name")).getSpawnLocation();
		
		status = Status.INITIALIZED;
	}
	
	public void start() {
		if(runners.isEmpty()){
			PLUGIN.getLogger().severe(Messages.RUNNERS_LIST_EMPTY.getMessage());
			return;
		}
		status = Status.STARTING;
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(!runnersTeam.getEntries().contains(player.getName())) {
				addSpectator(player);
			}
			player.teleport(spawn);
		});
		
		runners.forEach(player -> {
			player.setGameMode(GameMode.SURVIVAL);
			player.setFoodLevel(20);
			player.setHealth(20);
		});

		spawn.getWorld().setTime(0);
		
		if(PLUGIN.getConfiguration().getBooleanValue("remove-piglin-brute")) {
			PIGLIN_REMOVE = new BukkitRunnable() {
				@Override
				public void run() {
					PLUGIN.getServer().getOnlinePlayers().forEach(player -> {
						player.getWorld().getLivingEntities().forEach(livingEntity -> {
							if(livingEntity.getType() == EntityType.PIGLIN_BRUTE) {
								livingEntity.damage(1500);
							}
						});
					});
				}
				
			}.runTaskTimer(PLUGIN, 0, 1L);
		}
		
		SCOREBOARD = new BukkitRunnable() {
			@Override
			public void run() {
				PLUGIN.getServer().getOnlinePlayers().forEach(player -> {
					TimerScoreboard.updateScoreboard(player);
				});
			}
		}.runTaskTimer(PLUGIN,0l,20L);
		
		TIMER = new TimerTask(this).runTaskTimer(PLUGIN, 0L, 20L);

		dragonKilled = false;

		this.status = Status.STARTED;
	}

	public void stop(Player winner) {
		status = Status.FINISHED;

		PIGLIN_REMOVE.cancel();
		TIMER.cancel();
		SCOREBOARD.cancel();

		PLUGIN.getServer().getOnlinePlayers().forEach(player -> {
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(spawn);
			player.sendTitle(ChatColor.BLUE + winner.getName() + ChatColor.GOLD + " won the race !", ChatColor.GREEN + "Race time: " + ChatColor.AQUA + TimerTask.getTime(), 10, 4*20, 20);
		});

		new BukkitRunnable() {
			@Override
			public void run() {
				PLUGIN.getServer().shutdown();
			}
		}.runTaskLater(PLUGIN,30*20);
	}
	
	public Team getRunnersTeam() {
		return runnersTeam;
	}

	public void setRunnersTeam(Team runnersTeam) {
		this.runnersTeam = runnersTeam;
	}

	public boolean isDragonKilled() {
		return dragonKilled;
	}

	public void setDragonKilled(boolean dragonKilled) {
		this.dragonKilled = dragonKilled;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<Player> getRunners() {
		return runners;
	}

	public void addSpectator(Player p){
		spectators.add(p);
		p.setGameMode(GameMode.SPECTATOR);
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,999999,255,false,false));
	}

	public void addRunner(Player p) {
		runnersTeam.addEntry(p.getName());
		runners.add(p);
		p.teleport(spawn);
	}
	
	public void removeRunner(Player p) {
		runnersTeam.removeEntry(p.getName());
		runners.remove(p);
	}

	public static BukkitTask getTIMER() {
		return TIMER;
	}

	public enum Status {
		INITIALIZED,
		STARTING,
		STARTED,
		FINISHED,
	}
}
