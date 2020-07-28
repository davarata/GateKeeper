package tee.ancient.gatekeeper;

import java.time.Duration;
import java.util.List;

public class User {

	private String username;
	private Duration limit;
	private List<Activity> activities;

	public User(String username) {
		this.username = username;
	}
	
	public Activity getActivity(String activityName) {
		return activities.stream().filter(a -> a.getName().equals(activityName)).findFirst().orElse(null);
	}
	
	public String getUsername() {
		return username;
	}
	
	public Duration getLimit() {
		return limit;
	}
	
	public void setLimit(Duration limit) {
		this.limit = limit;
	}
	
	public List<Activity> getActivities() {
		return activities;
	}
	
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
}
