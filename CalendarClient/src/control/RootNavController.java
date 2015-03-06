package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class RootNavController {
	
	MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
	
    // MAIN NAVIGATION
    
    @FXML
	private void gotoCalendar() {
    	System.out.println("Opening calendarView..");
		mainApp.showCalendar();
	}
	
	@FXML
	private void gotoAppointments() {
		System.out.println("Opening appointmentView..");
		mainApp.showAppointmentOverview();
	}
	
	@FXML
	private void gotoInvitations() {
		System.out.println("Opening InvitationsView..");
		mainApp.showInvitations();
	}
	
	@FXML
	private void gotoNotifications() {
		System.out.println("Opening NotificationsView..");
	}
	
	// NOTIFICATIONS
	
	@FXML private MenuButton menubtn_notifications;
	
	// Add notification to notification list
	@FXML
	private void testAdd() {
		System.out.println("trykker");
		newNotification("appointment", "Fiskerimøte");
	}
	
	
	private boolean newNotification(String appointmentType, String appointmentName) {
		if (appointmentType.equals("appointment")) {
			MenuItem item = new MenuItem("Avtalen " + appointmentName + " er blitt endret");
			item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					gotoAppointments();	
				}
			});
			menubtn_notifications.getItems().add(item);
			if (menubtn_notifications.getItems().size() != 0) {
				menubtn_notifications.setVisible(true);
			} else {
				menubtn_notifications.setVisible(false);
			}
			System.out.println("Appointment change added.");
			return true;
		}else if (appointmentType.equals("invitation")) {
			MenuItem item = new MenuItem("Du er invitert til " + appointmentName);
			item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					gotoNotifications();	
				}
			});
			menubtn_notifications.getItems().add(item);
			if (menubtn_notifications.getItems().size() != 0) {
				menubtn_notifications.setVisible(true);
			} else {
				menubtn_notifications.setVisible(false);
			}
			System.out.println("Invitation added");
			return true;
			
		}else {
			if (menubtn_notifications.getItems().size() != 0) {
				menubtn_notifications.setVisible(true);
			} else {
				menubtn_notifications.setVisible(false);
			}
			System.out.println("hva skjer?");
			return false;
		}
		// Show notifications button only if new notifications
		//This will probably be done somewhere else in the future.
		
		
				
	}
	

}
