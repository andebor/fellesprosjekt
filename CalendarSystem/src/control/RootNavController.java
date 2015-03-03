package control;

import javafx.fxml.FXML;

public class RootNavController {
	
	MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
	
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
	public void gotoInvitations() {
		System.out.println("Opening InvitationsView..");
		mainApp.showInvitations();
	}

}
