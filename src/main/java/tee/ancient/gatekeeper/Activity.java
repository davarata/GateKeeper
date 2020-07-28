package tee.ancient.gatekeeper;

import java.time.Duration;
import java.util.Objects;

public class Activity {

	private String name;
	private String displayName;
	private String searchString;
	private Duration limit;
	private Duration usage;

	/**
	 * Creates an activity object with usage set to zero ({@link java.time.Duration#ZERO Duration.ZERO}).
	 *
	 * @param name
	 *        the name of the activity. It cannot be {@code null} or an empty string.
	 *
	 * @param displayName
	 *        the display name of the activity (shown in the user). It cannot be {@code null} or an empty string.
	 *
	 * @param searchString
	 *        the value that will be used when trying to search for the Linux process that this activity represents.
	 *        It cannot be {@code null} or an empty string.
	 *
	 * @param limit
	 *        the time limit of the activity. It cannot be negative or zero. If set to {@code null}, it means that the
	 *        activity does not have a time limit. 
	 * 
	 * @throws NullpointerException
	 *         if {@code name} is {@code null}, {@code displayName} is {@code null} or {@code searchString} is
	 *         {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *         if {@code name} is empty, {@code displayName} is empty, {@code searchString} is empty, or {@code limit}
	 *         is negative or zero.
	 */
	public Activity(String name, String displayName, String searchString, Duration limit) {
		this(name, displayName, searchString, limit, Duration.ZERO);
	}

	/**
	 * Creates an activity object.
	 *
	 * @param name
	 *        the name of the activity. It cannot be {@code null} or an empty string.
	 *
	 * @param displayName
	 *        the display name of the activity (shown in the user). It cannot be {@code null} or an empty string.
	 *
	 * @param searchString
	 *        the value that will be used when trying to search for the Linux process that this activity represents.
	 *        It cannot be {@code null} or an empty string.
	 *
	 * @param limit
	 *        the time limit of the activity. It cannot be negative or zero. If set to {@code null}, it means that the
	 *        activity does not have a time limit. 
	 * 
	 * @param usage
	 * 		  the time already spent on in this activity. It cannot be negative. If set to {@code null}, it
	 *        defaults to zero ({@link java.time.Duration#ZERO Duration.ZERO}).
	 * 
	 * @throws NullpointerException
	 *         if {@code name} is {@code null}, {@code displayName} is {@code null} or {@code searchString} is
	 *         {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *         if {@code name} is empty, {@code displayName} is empty, {@code searchString} is empty, {@code limit} is
	 *         negative or zero, or {@code usage} is negative.
	 */
	public Activity(String name, String displayName, String searchString, Duration limit, Duration usage) {
		Objects.requireNonNull(name, Constants.NAME_PARAM_NULL);
		if (name.trim().equals("")) {
			throw new IllegalArgumentException(Constants.NAME_PARAM_EMTPY);
		}
		this.name = name;

		Objects.requireNonNull(displayName, Constants.DISPLAYNAME_PARAM_NULL);
		if (displayName.trim().equals("")) {
			throw new IllegalArgumentException(Constants.DISPLAYNAME_PARAM_EMTPY);
		}
		this.displayName = displayName;
		
		Objects.requireNonNull(searchString, Constants.SEARCHSTRING_PARAM_NULL);
		if (searchString.trim().equals("")) {
			throw new IllegalArgumentException(Constants.SEARCHSTRING_PARAM_EMTPY);
		}
		this.searchString = searchString;
		
		if (limit != null) {
			if (limit.isZero())  {
				throw new IllegalArgumentException(Constants.LIMIT_PARAM_ZERO);
			} else if (limit.isNegative()) {
				throw new IllegalArgumentException(Constants.LIMIT_PARAM_NEGATIVE);
			}

			this.limit = limit;
		}

		if (usage == null) {
			this.usage = Duration.ZERO;
		} else {
			if (usage.isNegative()) {
				throw new IllegalArgumentException(Constants.USAGE_PARAM_NEGATIVE);
			}
			this.usage = usage;
		}
	}

	/**
	 * Creates a copy of an existing activity object.
	 * 
	 * @param activity
	 *        the activity to copy. It cannot be {@code null}.
	 * 
	 * @throws NullPointerException if {@code activity} is {@code null}. 
	 */
	public Activity(Activity activity) {
		Objects.requireNonNull(activity, Constants.ACTIVITY_PARAM_NULL);
		
		name = activity.name;
		displayName = activity.displayName;
		searchString = activity.searchString;
		limit = activity.limit;
		usage = activity.usage;
	}

	/**
	 * Increases the time spent on this activity.
	 * 
	 * @param amount the amount of time spent on this activity. It cannot be {@code null}.
	 * 
	 * @throws NullPointerException if {@code amount} is {@code null}
	 */
	public void increaseUsage(Duration amount) {
		Objects.requireNonNull(amount, Constants.AMOUNT_PARAM_NULL);
		if (amount.isNegative()) {
			throw new IllegalArgumentException(Constants.AMOUNT_PARAM_NEGATIVE);
		}
		
		usage = usage.plus(amount);
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getSearchString() {
		return searchString;
	}

	public Duration getLimit() {
		return limit;
	}

	public Duration getUsage() {
		return usage;
	}

	public void setUsage(Duration usage) {
		this.usage = usage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = prime + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((searchString == null) ? 0 : searchString.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Activity)) {
			return false;
		}

		Activity other = (Activity) object;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		// If the names match then displayName, searchString and limit may also match,
		// so check usage first.
		if (usage == null) {
			if (other.usage != null) {
				return false;
			}
		} else if (!usage.equals(other.usage)) {
			return false;
		}

		if (displayName == null) {
			if (other.displayName != null) {
				return false;
			}
		} else if (!displayName.equals(other.displayName)) {
			return false;
		}

		if (searchString == null) {
			if (other.searchString != null) {
				return false;
			}
		} else if (!searchString.equals(other.searchString)) {
			return false;
		}

		if (limit == null) {
			if (other.limit != null) {
				return false;
			}
		} else if (!limit.equals(other.limit)) {
			return false;
		}

		return true;
	}

}
