package tee.ancient.gatekeeper;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuardGUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		String activityName = null;
		
		Loader.load();
		
		for (Activity activity : Loader.getActivities()) {
			if (activity.getName().equals(getParameters().getRaw().get(0))) {
				activityName = activity.getDisplayName();
			}
		}
		
		UI ui = new UI(primaryStage, getParameters().getRaw().get(0));
		primaryStage.setScene(ui.buildUI(Loader.getUsers()));
		
		primaryStage.setTitle("Time manager: " + activityName);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
