package fr.nebulo9.speedruncontest.scoreboards;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.nebulo9.speedruncontest.tasks.TimerTask;

public class TimerScoreboard {
	
	private static HashMap<UUID,TimerScoreboard> PLAYERS = new HashMap<>();
	
	public static boolean hasScore(Player p) {
		return PLAYERS.containsKey(p.getUniqueId());
	}
	
	public static TimerScoreboard createScore(Player p) {
		return new TimerScoreboard(p);
	}
	
	public static TimerScoreboard getByPlayer(Player p) {
		return PLAYERS.get(p.getUniqueId());
	}
	
	public static TimerScoreboard removeScore(Player p) {
		return PLAYERS.remove(p.getUniqueId());
	}
	
	private ScoreboardManager manager;
	private Scoreboard scoreboard;
	private Objective sidebar;
	
	private TimerScoreboard(Player p) {
		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		sidebar = scoreboard.registerNewObjective("title", "dummy",ChatColor.DARK_GREEN	+ ChatColor.BOLD.toString() + "SPEEDRUN CONTEST");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 1; i < 16; i++) {
			Team team = scoreboard.registerNewTeam("slot_" + i);
			team.addEntry(ChatColor.values()[i].toString());
		}
		setSlot(2,ChatColor.GOLD + "Timer :");
		p.setScoreboard(scoreboard);
		PLAYERS.put(p.getUniqueId(), this);
	}
	
	public void setSlot(int slot,String text) {
		Team team = scoreboard.getTeam("slot_" + slot);
		String entry = ChatColor.values()[slot].toString();
		if(!scoreboard.getEntries().contains(entry)) {
			sidebar.getScore(entry).setScore(slot);
		}
		text = ChatColor.translateAlternateColorCodes('&', text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
	}
	
	private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }
}
