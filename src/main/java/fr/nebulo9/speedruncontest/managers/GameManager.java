package fr.nebulo9.speedruncontest.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
	private Team runnersTeam;
	private boolean portalPassed;
	private Status status;
	
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
		
		status = Status.INITIALIZED;
	}
	
	public void start() {
		status = Status.STARTING;
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(!runnersTeam.getEntries().contains(player.getName())) {
				spectators.add(player);
			}
		});
		
		spectators.forEach(player -> {
			player.setGameMode(GameMode.SPECTATOR);
		});
		
		runners.forEach(player -> {
			player.setGameMode(GameMode.SURVIVAL);
		});
		
		if(PLUGIN.getConfiguration().getBooleanValue("remove-piglin-brute")) {
			new BukkitRunnable() {

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
		
		
		
		TIMER = new TimerTask(this).runTaskTimer(PLUGIN, 0L, 20L);
	}
	
	public Team getRunnersTeam() {
		return runnersTeam;
	}

	public void setRunnersTeam(Team runnersTeam) {
		this.runnersTeam = runnersTeam;
	}

	public boolean isPortalPassed() {
		return portalPassed;
	}

	public void setPortalPassed(boolean portalPassed) {
		this.portalPassed = portalPassed;
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
	
	public void addRunner(Player p) {
		runnersTeam.addEntry(p.getName());
		runners.add(p);
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
