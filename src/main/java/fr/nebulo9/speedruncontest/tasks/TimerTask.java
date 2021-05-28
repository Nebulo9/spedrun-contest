package fr.nebulo9.speedruncontest.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import fr.nebulo9.speedruncontest.SCPlugin;

public class TimerTask extends BukkitRunnable {
	private final SCPlugin plugin;
	
	private static Integer SECONDS = 0;
	private static Integer MINUTES = 0;
	private static Integer HOURS = 0;
	
	private static String TIME;
	private String SECONDS_STRING;
	private String MINUTES_STRING;
	private String HOURS_STRING;
	
	private static int STATUS = 1;
	
	public TimerTask(SCPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		if(STATUS <= -1) {
			this.cancel();
		} else if(STATUS == 0) {
			
		} else {
			SECONDS++;
			if(SECONDS == 60){
				MINUTES++;
				SECONDS = 0;
			}
			if(MINUTES == 60){
				HOURS++;
				MINUTES = 0;
			}
			if(HOURS < 10) {
				HOURS_STRING = "0" + HOURS;
			} else {
				HOURS_STRING = HOURS.toString();
			}
			if(MINUTES < 10) {
				MINUTES_STRING = "0" + MINUTES;
			} else {
				MINUTES_STRING = MINUTES.toString();
			}
			if(SECONDS < 10) {
				SECONDS_STRING = "0" + SECONDS;
			} else {
				SECONDS_STRING = SECONDS.toString();
			}
			TIME = HOURS_STRING + "h " + MINUTES_STRING + "m " + SECONDS_STRING + "s";
		}

	}

	public static int getStatus() {
		return STATUS;
	}

	public static void setStatus(int status) {
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
