package fr.nebulo9.speedruncontest.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.nebulo9.speedruncontest.SCPlugin;

public class DropListeners implements Listener{

	private static SCPlugin PLUGIN;
	
	public DropListeners(SCPlugin plugin) {
		PLUGIN = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(PLUGIN.getConfiguration().getBoolean("melted-ores-drop")) {
			if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				replace(event);
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) {
		if(PLUGIN.getConfiguration().getBoolean("cooked-meats")) {
			replace(event);
		}
	}
	
	public void replace(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() == Material.IRON_ORE) {
			event.setDropItems(false);
			event.setExpToDrop(1);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
		}
		if(block.getType() == Material.GOLD_ORE) {
			event.setDropItems(false);
			event.setExpToDrop(1);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
		}
	}
	
	public void replace(ItemSpawnEvent event) {
		switch(event.getEntity().getItemStack().getType()) {
		case BEEF:
			event.getEntity().getItemStack().setType(Material.COOKED_BEEF);
			break;
		case CHICKEN:
			event.getEntity().getItemStack().setType(Material.COOKED_CHICKEN);
			break;
		case MUTTON:
			event.getEntity().getItemStack().setType(Material.COOKED_MUTTON);
			break;
		case PORKCHOP:
			event.getEntity().getItemStack().setType(Material.COOKED_PORKCHOP);
			break;
		case RABBIT:
			event.getEntity().getItemStack().setType(Material.COOKED_RABBIT);
			break;
		default:
			break;
		}
	}
}
