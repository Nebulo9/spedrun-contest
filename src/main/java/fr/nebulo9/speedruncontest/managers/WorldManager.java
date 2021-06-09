package fr.nebulo9.speedruncontest.managers;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;

import fr.nebulo9.speedruncontest.SCPlugin;
import fr.nebulo9.speedruncontest.util.CompressedFileActions;

public class WorldManager {
	
	private static final String LOBBY_URL = "https://www.dropbox.com/s/5niwsjwqzayjqxf/SRC_lobby.tar.gz?dl=1";
	
	private static SCPlugin PLUGIN;
	
	private World gameWorld;
	
	private World lobbyWorld;
	private Location lobbyLoc;
	
	public WorldManager(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	public void setup() {
		this.gameWorld = PLUGIN.getServer().getWorld(PLUGIN.getConfiguration().getString("world-name"));
		lobbyLoc = new Location(null,0.5d,60.5d,0.5d,0f,0f);
	}
	
	public boolean testLobbyExistence() {
		World tempWorld = PLUGIN.getServer().getWorld("SRC_Lobby");
		
		if(tempWorld == null) {
			PLUGIN.getLogger().info("\"SRC_Lobby\" world missing...");
			
			File tempWorldFolder = new File("SRC_Lobby");
			
			if(!tempWorldFolder.exists()) {
				PLUGIN.getLogger().info("\"SRC_Lobby\" folder missing...");
				
				File tempFile = new File("SRC_Lobby.tar.gz");
				
				if(!tempFile.exists()) {
					try {
						PLUGIN.getLogger().info("Downloading lobby...");
						CompressedFileActions.download(LOBBY_URL, tempFile);
						PLUGIN.getLogger().info("Lobby downloaded successfully !");
					} catch (IOException e) {
						PLUGIN.getLogger().log(Level.SEVERE,"Could not download lobby ! Ending process...",e);
						return false;
					}
				}
				
				try {
					PLUGIN.getLogger().info("Extracting lobby...");
					CompressedFileActions.decompressGzip(tempFile, tempWorldFolder);
					PLUGIN.getLogger().info("Lobby extracted successfully !");
				} catch (IOException e) {
					PLUGIN.getLogger().log(Level.SEVERE,"Could not extract lobby ! Ending process...",e);
					return false;
				}
			}
			
			World lobbyLoaded = PLUGIN.getServer().createWorld(WorldCreator.name("SRC_Lobby").environment(Environment.NORMAL));
			
			if(lobbyLoaded != null) {
				PLUGIN.getLogger().info("Lobby loaded successfully !");
			} else {
				PLUGIN.getLogger().severe("Could not load lobby.");
			}
			
			setLobbyWorld(lobbyLoaded);
			
			return lobbyLoaded != null;
		}
		PLUGIN.getLogger().info("Lobby already loaded.");
		
		return true;
	}
	
	public World createGameWorld() {
		String worldName = PLUGIN.getConfiguration().getString("world-name");
		String netherName = PLUGIN.getConfiguration().getString("world-name-nether");
		String endName = PLUGIN.getConfiguration().getString("world-name-end");
		
		World nether = Bukkit.getWorld(netherName);		
		if(nether != null) {
			Bukkit.unloadWorld(nether, false);
			nether.getWorldFolder().delete();
			
			new WorldCreator(netherName).environment(Environment.NETHER).createWorld();
		}
		
		World end = Bukkit.getWorld(endName);
		if(end != null) {
			Bukkit.unloadWorld(end, false);
			end.getWorldFolder().delete();
			
			new WorldCreator(endName).environment(Environment.NETHER).createWorld();
		}
		
		World world = Bukkit.getWorld(worldName);
		if(world != null) {
			Bukkit.unloadWorld(world, false);
			
			File folder = world.getWorldFolder();
			
			try {
				if(!folder.delete()) {
					throw new IOException();
				}
			} catch (IOException e) {
				e.printStackTrace();
				
				PLUGIN.getLogger().severe("Could not remove the \"" + PLUGIN.getConfiguration().getString("world-name") + "\" folder. Using a random name then");
				
				worldName += "_" + new Random().nextInt(100);
			}
		}
		
		WorldCreator creator = new WorldCreator(worldName);
		world = creator.createWorld();
		
		if(world != null) {
			world.getWorldFolder().deleteOnExit();
		}
		
		return world;
	}

	public void setLobbyWorld(World lobbyWorld) {
		this.lobbyWorld = lobbyWorld;
		this.lobbyLoc.setWorld(lobbyWorld);
		
	}
}
