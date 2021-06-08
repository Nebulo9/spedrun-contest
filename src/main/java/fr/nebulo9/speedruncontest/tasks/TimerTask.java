package fr.nebulo9.speedruncontest.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import fr.nebulo9.speedruncontest.managers.GameManager;

public class TimerTask extends BukkitRunnable {
	
	private static GameManager GAME_MANAGER;
	
	private static Integer SECONDS = 0;
	private static Integer MINUTES = 0;
	private static Integer HOURS = 0;
	
	private static String TIME = "00h 00m 00s";
	private String SECONDS_STRING;
	private String MINUTES_STRING;
	private String HOURS_STRING;
	
	private static Status STATUS;
	
	public TimerTask(GameManager gameManager) {
		GAME_MANAGER = gameManager;
	}
	
	public enum Status {
		STARTED,
		PAUSED,
		STOPPED,
	}
	
	@Override
	public void run() {
		if(STATUS == Status.STOPPED) {
			GAME_MANAGER.setStatus(GameManager.Status.FINISHED);
			this.cancel();
		} else if(STATUS == Status.PAUSED) {
			
		} else if(STATUS == Status.STARTED){
			GAME_MANAGER.setStatus(GameManager.Status.STARTED);
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

	public static Status getStatus() {
		return STATUS;
	}

	public static void setStatus(Status status) {
		STATUS = status;
	}

	public Integer getSeconds() {
		return SECONDS;
	}

	public Integer getMinutes() {
		return MINUTES;
	}

	public Integer getHours() {
		return HOURS;
	}

	public static String getTime() {
		return TIME;
	}

}
