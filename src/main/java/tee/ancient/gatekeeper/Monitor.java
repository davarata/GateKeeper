package tee.ancient.gatekeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public class Monitor {

	private Tracker tracker;
	private Activity activity;
	private static String searchString;
	
	private static final int FIND_ATTEMPTS = 60;
	private static final int KILL_TIMEOUT = 10;
	
	public static void main(String[] args) {
		Loader.load();
		
		String data[] = new String[0];
		try {
			data = Files.readString(Paths.get("/tmp/.gatekeeper.details")).trim().split(" ");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		final String userName = data[0];
		User user = Loader.getUsers().stream().filter(u -> u.getUsername().equals(userName)).findFirst().orElse(null);
		
		if ((args.length > 0) && "video".equals(args[0])) {
			searchString = "smplayer " + args[1];
		}
		new Monitor().run(user, data[1]);
	}
	
	public void run(User user, String activityName) {
		tracker = new Tracker(user, activityName);
		activity = user.getActivity(activityName);
		
		
		if (searchString != null) {
			activity.setSearchString(searchString);
		}
		Logger.log(this,  "searchString='" + activity.getSearchString() + "'");
		
		start();
	}
	
	public void start() {
		LocalTime startTime = null;
		LocalTime stopTime = null;

		boolean ignoreRemaining = false;
		long remaining = 0;
		if (activity.getLimit() == null) {
			ignoreRemaining = true;
		} else {
			remaining = activity.getLimit().minus(activity.getUsage()).getSeconds();
		}
		
		long pid = -1;
		for (int attempt = 0; attempt < FIND_ATTEMPTS; attempt++) {
			try {
				pid = getPid();
				if (pid != -1) {
					startTime = LocalTime.now();
					tracker.logStartTime(startTime);
					break;
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// do nothing.
			}
		}
		
		if (pid == -1) {
			Logger.log(this,  "Could not find a PID");
			return;
		}
		
		while (true) {
			try {
				stopTime = LocalTime.now();

				pid = getPid();
				if (pid == -1) {
					tracker.logStopTime(stopTime);
					break;
				}
				
				if (!ignoreRemaining && (Duration.between(startTime,  stopTime).getSeconds() > remaining)) {
					stopActivity();
				}
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// do nothing.
			}
		}
	}
	
	/*
	 * This method needs to change to look for all possible entries with the same command and kill the others first.
	 * 
	 */
	public long getPid() {
		BufferedReader output = null;
		try {
			output = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("ps afx").getInputStream()));

			String data = output.lines().filter(p -> p.endsWith(activity.getSearchString())).findFirst().orElse(null);
			if (data != null) {
				return Long.valueOf(data.strip().split(" ")[0].strip());
			}
		} catch (IOException | NumberFormatException e) {
			throw new RuntimeException(e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return -1;
	}
	
	public void stopActivity() {
		// Ask the process (process) nicely to stop (two times), waiting a total of KILL_TIMEOUT seconds for both
		// requests. This allows the program to save any data it would normally save when a person terminates it.
		for (int attempt = 0; attempt < 2; attempt++) {
			try {
				Runtime.getRuntime().exec("kill -15 " + getPid());
				Thread.sleep(KILL_TIMEOUT * 500); // same as KILL_TIMEOUT * 1000 (milliseconds) / 2 (attempts)
				
				if (getPid() == -1) {
					return;
				}
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			} catch (InterruptedException ie) {
				// do nothing
			}
		}
		
		// If the process did not respond to being asked nicely, kill it.
		try {
			Runtime.getRuntime().exec("kill -9 " + getPid());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}

}
