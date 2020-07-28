package tee.ancient.gatekeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;

public class ActivityTest {

	/* ** Activity(String name, String displayName, String searchString, Duration limit) ** */
    /* ************************************************************************************ */
	
	/* ** name ** */
	/* ********** */
	@Test
	public void constructorNameNotNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity(null, "displayName", "searchString", Duration.ZERO));
		assertThat(exception.getMessage(), equalTo(Constants.NAME_PARAM_NULL));
	}
	
	@Test
	public void constructorNameNotEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("", "displayName", "searchString", null));
		assertThat(exception.getMessage(),  equalTo(Constants.NAME_PARAM_EMTPY));
	}
	
	@Test
	public void constructorNameNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("\t \r\n", "displayName", "searchString", null));
		assertThat(exception.getMessage(), equalTo(Constants.NAME_PARAM_EMTPY));
	}
	
	/* ** displayName ** */
	/* ***************** */
	@Test
	public void constructorDisplayNameNotNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity("name", null, "searchString", null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_NULL));
	}
	
	@Test
	public void constructorDisplayNameEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "", "searchString", null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_EMTPY));
	}
	
	@Test
	public void constructorDisplayNameNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "\t \r\n", "searchString", null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_EMTPY));
	}
	
	/* ** searchString ** */
	/* ****************** */
	@Test
	public void constructorSearchStringNotNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity("name", "displayName", null, null));
		assertThat(exception.getMessage(),  equalTo(Constants.SEARCHSTRING_PARAM_NULL));
	}
	
	@Test
	public void constructorSearchStringNotEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "", null));
		assertThat(exception.getMessage(), equalTo(Constants.SEARCHSTRING_PARAM_EMTPY));
	}
	
	@Test
	public void constructorSearchStringNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "\t \r\n", null));
		assertThat(exception.getMessage(), equalTo(Constants.SEARCHSTRING_PARAM_EMTPY));
	}
	
	/* ** limit ** */
	/* *********** */
	@Test
	public void constructorLimitNotZero() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "searchString", Duration.ZERO));
		assertThat(exception.getMessage(), equalTo(Constants.LIMIT_PARAM_ZERO));
	}
	
	@Test
	public void constructorLimitNotNegative() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "searchString", Duration.ofHours(-1)));
		assertThat(exception.getMessage(), equalTo(Constants.LIMIT_PARAM_NEGATIVE));
	}
	
	/* ** check if usage is set to zero ** */
	@Test
	public void constructorSetsUsageToZero() {
		Activity activity = new Activity("name", "displayName", "searchString", null);
		assertThat(activity.getUsage(), equalTo(Duration.ZERO));
	}
	
	/* ** Activity(String name, String displayName, String searchString, Duration limit, Duration usage) ** */
	/* **************************************************************************************************** */

	/* ** name ** */
	/* ********** */
	@Test
	public void fullConstructorNameNotNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity(null, "displayName", "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.NAME_PARAM_NULL));
	}
	
	@Test
	public void fullConstructorNameNotEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("", "displayName", "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.NAME_PARAM_EMTPY));
	}
	
	@Test
	public void fullConstructorNameNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("\t \r\n", "displayName", "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.NAME_PARAM_EMTPY));
	}
	
	/* ** displayName ** */
	/* ***************** */
	@Test
	public void fullConstructorDisplayNameNotNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity("name", null, "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_NULL));
	}
	
	@Test
	public void fullConstructorDisplayNameNotEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "", "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_EMTPY));
	}
	
	@Test
	public void fullConstructorDisplayNameNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "\t \r\n", "searchString", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.DISPLAYNAME_PARAM_EMTPY));
	}
	
	/* ** searchString ** */
	/* ****************** */
	@Test
	public void fullConstructorSearchStringNull() {
		Exception exception = assertThrows(
				NullPointerException.class,
				() -> new Activity("name", "displayName", null, null, null));
		assertThat(exception.getMessage(), equalTo(Constants.SEARCHSTRING_PARAM_NULL));
	}
	
	@Test
	public void fullConstructorSearchStringNotEmpty() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.SEARCHSTRING_PARAM_EMTPY));
	}
	
	@Test
	public void fullConstructorSearchStringNotWhitespace() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "", null, null));
		assertThat(exception.getMessage(), equalTo(Constants.SEARCHSTRING_PARAM_EMTPY));
	}
	
	/* ** limit ** */
	/* *********** */
	@Test
	public void fullConstructorLimitNotZero() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "searchString", Duration.ZERO, null));
		assertThat(exception.getMessage(), equalTo(Constants.LIMIT_PARAM_ZERO));
	}
	
	@Test
	public void fullConstructorLimitNotNegative() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "searchString", Duration.ofHours(-1), null));
		assertThat(exception.getMessage(), equalTo(Constants.LIMIT_PARAM_NEGATIVE));
	}
	
	/* ** usage ** */
	/* *********** */
	@Test
	public void fullConstructorUsageNegative() {
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> new Activity("name", "displayName", "searchString", null, Duration.ofHours(-1)));
		assertThat(exception.getMessage(), equalTo(Constants.USAGE_PARAM_NEGATIVE));
	}
	
	/* ********************** boolean equals(Object object) ********************** */
	/* *************************************************************************** */
	
	@Test
	public void equals() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", null, null);
		assertThat(activity1,  equalTo(activity2));
	}

	@Test
	public void differentNamesNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("another name",  "displayName", "searchString", null, null);
		assertThat(activity1,  not(equalTo(activity2)));
	}
	
	@Test
	public void differentDisplayNamesNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "another displayName", "searchString", null, null);
		assertThat(activity1, not(equalTo(activity2)));
	}
	
	@Test
	public void differentSearchStringNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "another searchString", null, null);
		assertThat(activity1, not(equalTo(activity2)));
	}
	
	@Test
	public void differentLimitNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", Duration.ofHours(1), null);
		assertThat(activity1, not(equalTo(activity2)));
	}
	
	@Test
	public void differentUsageNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", null, Duration.ofHours(1));
		assertThat(activity1, not(equalTo(activity2)));
	}
	
	
	/* ******************************** int hashCode() ******************************** */
	/* ******************************************************************************** */
	
	@Test
	public void hashCodesEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", null, null);
		assertThat(activity1.hashCode(), equalTo(activity2.hashCode()));
	}

	@Test
	public void differentNameHashCodeNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("another name",  "displayName", "searchString", null, null);
		assertThat(activity1.hashCode(),  not(equalTo(activity2.hashCode())));
	}
	
	@Test
	public void differentDisplayNameHashCodeNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "another displayName", "searchString", null, null);
		assertThat(activity1.hashCode(),  not(equalTo(activity2.hashCode())));
	}
	
	@Test
	public void differentSearchStringHashCodeNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "another searchString", null, null);
		assertThat(activity1.hashCode(),  not(equalTo(activity2.hashCode())));
	}
	
	@Test
	public void differentLimitHashCodeNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", Duration.ofHours(1), null);
		assertThat(activity1.hashCode(),  not(equalTo(activity2.hashCode())));
	}
	
	@Test
	public void differentUsageHashCodeNotEqual() {
		Activity activity1 = new Activity("name",  "displayName", "searchString", null, null);
		Activity activity2 = new Activity("name",  "displayName", "searchString", null, Duration.ofHours(1));
		assertThat(activity1.hashCode(), not(equalTo(activity2.hashCode())));
	}
	
	
	/* ********************** Activity (Activity activity) ********************** */
	/* ************************************************************************** */
	
	@Test
	public void copyConstructorActivityNotNull() {
		Activity activity = null;
		Exception exception = assertThrows(NullPointerException.class, () -> new Activity(activity));
		assertThat(exception.getMessage(), equalTo(Constants.ACTIVITY_PARAM_NULL));
	}
	
	@Test
	public void copyConstructorEquals() {
		Activity activity1 =
				new Activity("name",  "displayName", "searchString", Duration.ofHours(2), Duration.ofHours(1));
		Activity activity2 = new Activity(activity1);
		assertThat(activity1, equalTo(activity2));
	}
	
	
	/* ********************** void increase(Duration amount) ********************** */
	/* **************************************************************************** */
	
	@Test
	public void increaseUsageAmountNull() {
		Activity activity = new Activity("name",  "displayName", "searchString", null);
		Exception exception = assertThrows(NullPointerException.class, () -> activity.increaseUsage(null));
		assertThat(exception.getMessage(), equalTo(Constants.AMOUNT_PARAM_NULL));
	}
	
	@Test
	public void increaseUsageAmountNegative() {
		Activity activity = new Activity("name",  "displayName", "searchString", null);
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> activity.increaseUsage(Duration.ofHours(-1)));
		assertThat(exception.getMessage(), equalTo(Constants.AMOUNT_PARAM_NEGATIVE));
	}
	
	@Test
	public void increaseUsageWithZeroSucceeds() {
		Activity activity = new Activity("name",  "displayName", "searchString", null, Duration.ZERO);
		activity.increaseUsage(Duration.ZERO);
		assertThat(activity.getUsage(), equalTo(Duration.ZERO));
	}
	
	@Test
	public void increaseUsageWithPositiveValueSucceeds() {
		Activity activity = new Activity("name",  "displayName", "searchString", null, Duration.ofHours(1));
		activity.increaseUsage(Duration.ofHours(1));
		assertThat(activity.getUsage(), equalTo(Duration.ofHours(2)));
	}
	
}
