package tee.ancient.gatekeeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UI {

	private Stage stage;
	private BorderPane content;
	private VBox quickHack;
	private List<Button> buttons = new LinkedList<>();
	private String activity;
	
	public UI(Stage stage, String activity) {
		this.stage = stage;
		this.activity = activity;
	}
	
	public Scene buildUI(List<User> users) {
		Label pleaseSelectText = new Label("Please select a family member...");
		pleaseSelectText.setFont(new Font(pleaseSelectText.getFont().getName(), 24));
		
		HBox pleaseSelectContent = new HBox();
		pleaseSelectContent.setAlignment(Pos.CENTER);
		pleaseSelectContent.setBorder(new Border(new BorderStroke(null,  null,  null,  new BorderWidths(5))));
		pleaseSelectContent.getChildren().add(pleaseSelectText);
		
		VBox usersContent = new VBox();
		usersContent.setAlignment(Pos.CENTER);
		for (User user : users) {
			usersContent.getChildren().add(createUserRow(user));
		}

		content = new BorderPane();
		content.setTop(pleaseSelectContent);
		content.setCenter(usersContent);
		
		return new Scene(content);
	}
	
	private Node createUserRow(User user) {
		Tracker tracker = new Tracker(user, null);
		tracker.calculateUsage();

		Label userLabel = new Label(user.getUsername());
		Label timeInfo = new Label();
		
		Button chooseButton = new Button("Choose");
		chooseButton.setOnMouseClicked(e -> showPassword(user));
		buttons.add(chooseButton);

		Duration remainingTime = calculateRemainingTime(user.getLimit(), tracker.getUsage());
		if (remainingTime == null) {
			timeInfo.setText("(no time limit)");
		} else if (remainingTime.getSeconds() <= 0) {
			timeInfo.setText("No time left.");
			chooseButton.setDisable(true);
		} else {
			timeInfo.setText(getTimeInfo(remainingTime));
		}
		
		BorderPane userRow = new BorderPane();
		userRow.setBorder(new Border(new BorderStroke(null,  null,  null,  new BorderWidths(5))));
		
		userRow.setLeft(userLabel);
		userRow.setCenter(timeInfo);
		userRow.setRight(chooseButton);
		
		return userRow;
	}

	private Duration calculateRemainingTime(Duration limit, Duration usage) {
		if (limit == null) {
			return null;
		}
		
		return limit.minus(usage);
	}
	
	private String getTimeInfo(Duration remainingTime) {
		StringBuilder stringBuilder = new StringBuilder();
		
		int hours = remainingTime.toHoursPart();
		if (hours > 0) {
			stringBuilder.append(hours).append(" " ).append(adjustForPlural(hours, "hour"));
		}

		int minutes = remainingTime.toMinutesPart();
//		// if a fraction of a minute (even just one second) is used, count it as another minute.
//		if (remainingTime.toSecondsPart() > 0) {
//			minutes++;
//		}
		if (minutes > 0) {
			if (hours > 0) {
				stringBuilder.append(" and ");
			}
			stringBuilder.append(minutes).append(" " ).append(adjustForPlural(minutes, "minute"));
		}

		stringBuilder.append(" remaining.");
		
		return stringBuilder.toString();
	}

	private String adjustForPlural(int amount, String unit) {
		if ((amount == 0) || (amount > 1)) {
			return unit + "s";
		}
		
		return unit;
	}

	private void showPassword(User user) {
		quickHack = new VBox();
		
		Label passwordText = new Label("Password:");
		PasswordField passwordInput = new PasswordField();
		Button startButton = new Button("Start");
		startButton.setOnMouseClicked(e -> handlePassword(user, passwordInput.getText()));
		HBox passwordContent = new HBox();
		passwordContent.getChildren().addAll(passwordText, passwordInput, startButton);
		passwordContent.setBorder(new Border(new BorderStroke(null,  null,  null,  new BorderWidths(5))));
		
		quickHack.getChildren().add(passwordContent);
		content.setBottom(quickHack);

		for (Button button : buttons) {
			button.setDisable(true);
		}
		
		stage.sizeToScene();
	}
	
	private void handlePassword(User user, String password) {
		if (!correctPassword(user, password)) {
			showIncorrectPassword();
		} else {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/tmp/.gatekeeper.details")))) {
				writer.write(user.getUsername() + " " + activity + "\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			System.exit(2);
		}
	}
	
	private void showIncorrectPassword() {
		Label incorrectPassword = new Label("Incorrect password");
		quickHack.getChildren().add(incorrectPassword);
		stage.sizeToScene();
	}
	
	private boolean correctPassword(User user, String password) {
		if (user.getUsername().equals("catharina") && password.equals("catIsKing")) {
			return true;
		}
		if (user.getUsername().equals("lucia") && password.equals("luciaExists")) {
			return true;
		}
		if (user.getUsername().equals("parent") && password.equals("parentForever")) {
			return true;
		}
		
		return false;
	}
}
