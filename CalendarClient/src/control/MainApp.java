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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane loginView;
	private BorderPane rootNav;
	public static RootNavController rootController;
	public static Appointment appointmentToSelect;
	    
	public MainApp() {
	}

	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLogin();
     // legger til ikon
        this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

	}
	
    public void initRootNav() {
    	/*
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	MainApp.rootController.setNotificationBold();
	        	MainApp.rootController.removeNotificationBold();
	        }
	    });
	    */
    	
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_rootNav.fxml"));
            rootNav = (BorderPane) loader.load();

            //Close login stage
            primaryStage.close();
            //Create new stage for main window
            Scene scene = new Scene(rootNav);
            primaryStage.setResizable(true);
            primaryStage.setTitle("Kalender");
            primaryStage.setScene(scene);
            //Show the scene containing the root layout.
            primaryStage.show();
            
            // Give the controller access to the main app.
            rootController = loader.getController();
            rootController.setMainApp(this);
            

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loginSuccess() throws IOException {
    	System.out.println("testing root init..");
    	initRootNav();
    	System.out.println("Navigation loaded, loading calendar...");
    	showCalendar();
    	rootController.handleRefresh();
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
    
    public void showInvitations() {
    	try {
    		//load main calendar view
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_invitations.fxml"));
            AnchorPane invitationsView = (AnchorPane) loader.load();
            
            rootNav.setCenter(invitationsView);
            
            // Give the controller access to the main app.
            InvitationsController controller = loader.getController();
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

			// Set new/change appointment into the center of root layout
			rootNav.setCenter(page);

			// Set the person into the controller.
			if(appointment==null){
				NewAppointmentController controller = loader.getController();
				controller.setMainApp(this);
			}
			else { 
				editAppointmentController controller = loader.getController();
				controller.setMainApp(this);
				controller.initAppointment(appointment);
				controller.loadAppointment();
			}

			//return controller.isOkClicked();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;	
		}
	}
	
	public void showNotifications() {
    	try {
    		//load main calendar view
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_notifications.fxml"));
            AnchorPane notificationsView = (AnchorPane) loader.load();

            
            rootNav.setCenter(notificationsView);
            // Give the controller access to the main app.
            NotificationsController controller = loader.getController();
            controller.setMainApp(this);
            
    	} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void showUserManagement() {
		try {
    		//load main calendar view
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_userManagement.fxml"));
            AnchorPane userManagementView = (AnchorPane) loader.load();

            
            rootNav.setCenter(userManagementView);
            // Give the controller access to the main app.
            UserManagementController controller = loader.getController();
            controller.setMainApp(this);
            
    	} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    
    public Stage getPrimaryStage() {
    	return primaryStage;
    }
    
    public void showAddNewUser() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/GUI_newUser.fxml"));
			AnchorPane newUserView;
			newUserView = (AnchorPane) loader.load();
			
			// Create dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Legg til ny bruker");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(getPrimaryStage());
//    	dialogStage.initOwner(userManagementControllerApp.getPrimaryStage());
			
			// Create dialog scene
			Scene scene = new Scene(newUserView);
			dialogStage.setScene(scene);
			
			AddUserController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void logOut() {
    	primaryStage.close();
    	showLogin();
    }
    
    public void showGroupOverview() {
    	try {
    		//load main calendar view
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GUI_groupOverview.fxml"));
            AnchorPane groupManagementView = (AnchorPane) loader.load();

            
            rootNav.setCenter(groupManagementView);
            // Give the controller access to the main app.
            GroupManagementController controller = loader.getController();
            controller.setMainApp(this);
            
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
