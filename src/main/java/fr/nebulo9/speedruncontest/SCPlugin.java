package fr.nebulo9.speedruncontest;

import fr.nebulo9.speedruncontest.commands.StopCMD;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nebulo9.speedruncontest.commands.RunnerCMD;
import fr.nebulo9.speedruncontest.commands.RunnersCMD;
import fr.nebulo9.speedruncontest.commands.StartCMD;
import fr.nebulo9.speedruncontest.listeners.DropListeners;
import fr.nebulo9.speedruncontest.listeners.GameListener;
import fr.nebulo9.speedruncontest.listeners.LoginLogoutListener;
import fr.nebulo9.speedruncontest.managers.GameManager;
import fr.nebulo9.speedruncontest.managers.WorldManager;
import fr.nebulo9.speedruncontest.util.Config;
import fr.nebulo9.speedruncontest.util.Messages;

public class SCPlugin extends JavaPlugin{
	
	private static Config config;
	private static GameManager GAME_MANAGER;
	private static WorldManager WORLD_MANAGER;
	
	@Override
	public void onEnable(){
		loadConfig();
		config = new Config();
		setConfig();

		WORLD_MANAGER = new WorldManager(this);
		GAME_MANAGER = new GameManager(this);
		Bukkit.getScheduler().runTask(this, () -> {
			WORLD_MANAGER.setup();

			if(!WORLD_MANAGER.testLobbyExistence()) {
				getLogger().severe("Could not lobby. Ending process...");
				getServer().shutdown();
			}
			GAME_MANAGER.setup();
		});

		getCommand("runner").setExecutor(new RunnerCMD(GAME_MANAGER));
		getCommand("runners").setExecutor(new RunnersCMD(GAME_MANAGER));
		getCommand("start").setExecutor(new StartCMD(GAME_MANAGER));
		getCommand("stop").setExecutor(new StopCMD(GAME_MANAGER));
		
		this.getServer().getPluginManager().registerEvents(new DropListeners(this), this);
		this.getServer().getPluginManager().registerEvents(new GameListener(GAME_MANAGER), this);
		this.getServer().getPluginManager().registerEvents(new LoginLogoutListener(GAME_MANAGER), this);
		
		this.getLogger().info(Messages.PLUGIN_ENABLED.getMessage());
	}

	@Override
	public void onDisable() {
		this.getLogger().info(Messages.PLUGIN_DISABLED.getMessage());
	}
	
	private void loadConfig() {
		this.saveConfig();
		this.getConfig().addDefault("world-name", "world");
		this.getConfig().addDefault("melted-ores-drop", true);
		this.getConfig().addDefault("cooked-meats", true);
		this.getConfig().addDefault("remove-piglin-brute", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	private void setConfig() {
		config.addValue("world-name", getConfig().getString("world-name"));
		config.addValue("world-name-nether", getConfig().getString("world-name") + "_nether");
		config.addValue("world-name-end", getConfig().getString("world-name") + "_the_end");
		
		config.addValue("melted-ores-drop", getConfig().getBoolean("melted-ores-drop"));
		config.addValue("cooked-meats", getConfig().getBoolean("cooked-meats"));
		config.addValue("remove-piglin-brute", getConfig().getBoolean("remove-piglin-brute"));
	}

	public Config getConfiguration() {
		return config;
	}
}
