package fr.nebulo9.speedruncontest.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import fr.nebulo9.speedruncontest.managers.GameManager;

public class TimerTask extends BukkitRunnable {
	
	private static GameManager GAME_MANAGER;
	
	private static Integer SECONDS = 0;
	private static Integer MINUTES = 0;
	private static Integer HOURS = 0;
	
	private static String TIME = "00h 00m 00s";
	private static String SECONDS_STRING;
	private static String MINUTES_STRING;
	private static String HOURS_STRING;
	
	public TimerTask(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}
	
	@Override
	public void run() {
		if(GAME_MANAGER.getStatus() == GameManager.Status.FINISHED) {
			this.cancel();
		} else if(GAME_MANAGER.getStatus() == GameManager.Status.STARTED){
			SECONDS++;
			if(SECONDS == 60){
				MINUTES++;
				SECONDS = 0;
			}
			if(MINUTES == 60){
				HOURS++;
				MINUTES = 0;
			}
			HOURS_STRING = formatTime(HOURS);
			MINUTES_STRING = formatTime(MINUTES);
			SECONDS_STRING = formatTime(SECONDS);
			TIME = HOURS_STRING + "h " + MINUTES_STRING + "m " + SECONDS_STRING + "s";
		}
	}
	
	private String formatTime(Integer time) {
		if(time < 10) {
			return "0" + time.toString();
		} else {
			return time.toString();
		}
	}

	public static String getTime() {
		return TIME;
	}

}
