package tee.ancient.gatekeeper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Loader {

	private static List<User> users;
	private static List<Activity> activities;
	
	public static List<User> getUsers() {
		return users;
	}
	
	public static List<Activity> getActivities() {
		return activities;
	}
	
	public static void load() {
		users = loadUsers();
		activities = loadActivities();
		
		for (User user : users) {
			user.setActivities(new LinkedList<>());
			for (Activity activity : activities) {
				Activity newActivity = new Activity(activity);
				newActivity.setLimit(user.getLimit());
				user.getActivities().add(newActivity);
				
			}
		}
	}

	private static List<User> loadUsers() {
		List<String> fileNames = null;
		try {
			fileNames = Files.list(Path.of(System.getenv("HOME") + "/." + Constants.CONFIG_DIRECTORY)).
					map(p -> p.toString()).
					collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<User> users = fileNames.stream().
				filter(s -> s.endsWith(".user")).
				map(s -> new User(s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".user")))).
				collect(Collectors.toList());
		for (User user : users) {
			Properties properties = new Properties();
			try {
				properties.load(new FileReader(new File(System.getenv("HOME") + "/." + Constants.CONFIG_DIRECTORY + "/" +
						user.getUsername() + ".user")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			String limit = properties.getProperty("limit");
			if (!limit.equals("-1")) {
				user.setLimit(Duration.parse(limit));
			}
		}
		
		return users;
	}
	
	private static List<Activity> loadActivities() {
		List<String> fileNames = null;
		try {
			fileNames = Files.list(Path.of(System.getenv("HOME") + "/." + Constants.CONFIG_DIRECTORY)).
					map(p -> p.toString()).
					collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<Activity> activities = fileNames.stream().
				filter(s -> s.endsWith(".activity")).
				map(s -> new Activity(s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".activity")))).
				collect(Collectors.toList());

		for (Activity activity : activities) {
			Properties properties = new Properties();
			try {
				properties.load(new FileReader(new File(System.getenv("HOME") + "/." + Constants.CONFIG_DIRECTORY + "/" +
						activity.getName() + ".activity")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			activity.setDisplayName(properties.getProperty("displayName"));
			activity.setSearchString(properties.getProperty("searchString"));
		}
		
		return activities;
	}
	
}
