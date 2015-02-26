package control;

import java.io.IOException;

import model.Appointment;
import control.NewAppointmentController;
import control.AppointmentOverviewController;
import control.MainApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootNav;

	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //this.primaryStage.setTitle("AvtaleApp");

        initRootNav();

        showAppointmentOverview();
	}
	
    public void initRootNav() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_rootNav.fxml"));
            rootNav = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootNav);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

		

    public void showAppointmentOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_appointmentOverview.fxml"));
            AnchorPane appointmentOverview = (AnchorPane) loader.load();
        	
            
            
            // Set person overview into the center of root layout.
            rootNav.setCenter(appointmentOverview);
            
            
            // Give the controller access to the main app.
            AppointmentOverviewController controller = loader.getController();
            controller.setMainApp(this);
            

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	public boolean showNewAppointment(Appointment appointment) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/GUI_newAppointment.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			//dialogStage.setTitle("Endre avtale");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			NewAppointmentController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			//controller.setPerson(person);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			//return controller.isOkClicked();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
    
    public Stage getPrimaryStage() {
    	return primaryStage;
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
