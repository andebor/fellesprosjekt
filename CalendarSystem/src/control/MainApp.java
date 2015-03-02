package control;

import java.io.IOException;

import model.Appointment;
import control.NewAppointmentController;
import control.AppointmentOverviewController;
import control.MainApp;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane loginView;
	private BorderPane rootNav;
	
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    
	public MainApp() {
	}

	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //this.primaryStage.setTitle("AvtaleApp");

//        initRootNav();

        showLogin();
	}
	
    public void initRootNav() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_rootNav.fxml"));
            rootNav = (BorderPane) loader.load();

            //Close login stage
            primaryStage.close();
            //Create new stage for main window
            Scene scene = new Scene(rootNav);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Kalender");
            primaryStage.setScene(scene);
            //Show the scene containing the root layout.
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loginSuccess() {
    	System.out.println("testing root init..");
    	initRootNav();
    	System.out.println("Navigation loaded, loading calendar...");
    	showCalendar();
    }

    
    public void showLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_login.fxml"));
            loginView = (BorderPane) loader.load();
        	
            //Create login stage
            Scene scene = new Scene(loginView);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Kalender");
            primaryStage.setScene(scene);
            primaryStage.show();

            
            
            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setMainApp(this);
            

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCalendar() {
    	try {
    		//load main calendar view
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_calendar.fxml"));
            AnchorPane calendarView = (AnchorPane) loader.load();
            
            rootNav.setCenter(calendarView);
            
            // Give the controller access to the main app.
            CalendarController controller = loader.getController();
            controller.setMainApp(this);
            
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
			// Choose between edit appoint and new appointment
			if(appointment==null){
				loader.setLocation(MainApp.class.getResource("/view/GUI_newAppointment.fxml"));
			}
			else {
				loader.setLocation(MainApp.class.getResource("/view/GUI_editnewAppointment.fxml"));
			}
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			//dialogStage.setTitle("Endre avtale");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			if(appointment==null){
			NewAppointmentController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			}
			else { 
				editAppointmentController controller = loader.getController();
				controller.initAppointment(appointment);
				controller.loadAppointment();
				controller.setDialogStage(dialogStage);
			}
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
    
    public ObservableList<Appointment> getAppointmentList() {
    	return appointmentList;
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}