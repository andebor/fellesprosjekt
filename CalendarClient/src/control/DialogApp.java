package control;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DialogApp extends Application {

	private Stage primaryStage;
	public static String message;
	
//	public DialogApp(String message) {
//		this.message = message;
//	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		showAlert();
	}
	
	public void showAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		//alert.initOwner(null);
		alert.setTitle("Varsel");
		alert.setHeaderText(null);
		alert.setContentText(DialogApp.message);
		alert.showAndWait();
		try {
			stop();
		} catch (Exception e) {
			System.out.println("stop() failed in DialogApp!");
		}
	}
	
	public static void main(String[] args) {
		//DialogApp.message = args[0];
		launch(DialogApp.class, args);
	}

}
