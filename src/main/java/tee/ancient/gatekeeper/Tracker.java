package tee.ancient.gatekeeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Tracker {

	private User user;
	private Activity activity;
	private File trackingFile;
	private boolean calculateOtherUsage = true;
	private Duration usage = Duration.ZERO;
	
	private static final String START_ENTRY = "started";
	private static final String STOP_ENTRY = "stopped";
	
	public Tracker(User user, String activityName) {
		this.user = user;
		
		if (activityName == null) {
			activity = user.getActivities().get(0);
		} else {
			activity = user.getActivity(activityName);
		}

		trackingFile = new File(System.getenv("HOME") + "/." + Constants.CONFIG_DIRECTORY + "/" + user.getUsername() +
				"." + activity.getName());
		Logger.log(this,  "trackingFile='" + trackingFile.getAbsolutePath() + "'");
	}
	
	public void calculateUsage() {
		if (!trackingFile.isFile()) {
			activity.setUsage(Duration.ZERO);
			return;
		}
		
		LocalDate today = LocalDate.now();
		LocalDate lastActivity = null;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(trackingFile))) {
			LocalTime startTime = null;
			LocalTime stopTime = null;
			
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				if (line.startsWith(START_ENTRY)) {
					startTime = LocalTime.parse(line.split(" ")[1]);
				} else if (line.startsWith(STOP_ENTRY)) {
					stopTime = LocalTime.parse(line.split(" ")[1]);
					
					if (today.equals(lastActivity)) {
						activity.increaseUsage(Duration.between(startTime, stopTime));
					}

					startTime = null;
					stopTime = null;
				} else if (!line.trim().equals("")) {
					lastActivity = LocalDate.parse(line);
					startTime = null;
					stopTime = null;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if (!today.equals(lastActivity)) {
			activity.setUsage(Duration.ZERO);
			write(today.toString());
		}
		
		usage = activity.getUsage();
		if (calculateOtherUsage) {
			calculateAllUsage();
		}
	}
	
	public void logStartTime(LocalTime startTime) {
		write(START_ENTRY + " " + startTime.toString());
	}
	
	public void logStopTime(LocalTime stopTime) {
		write(STOP_ENTRY + " " + stopTime.toString());
	}
	
	public void doNotCalculateAllUsage() {
		calculateOtherUsage = false;
	}

	public Duration getUsage() {
		return usage;
	}
	
	private void calculateAllUsage() {
		for (Activity otherActivity : user.getActivities()) {
			if (otherActivity != activity) {
				Tracker tracker = new Tracker(user, otherActivity.getName());
				tracker.doNotCalculateAllUsage();
				tracker.calculateUsage();
				usage = usage.plus(otherActivity.getUsage());
			}
		}
	}

	private void write(String value) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(trackingFile, true))) {
			writer.write(value + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
