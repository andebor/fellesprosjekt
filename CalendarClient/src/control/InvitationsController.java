package control;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import model.Appointment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

public class InvitationsController extends AppointmentOverviewController {



    
	
	@Override
    public void initAppointmetTable() throws IOException{
    	
    	// Using generateExampleAppointment for testing. Need a new method for retrieving appointment-list from database
    	//appointmentList = generateExampleAppointment();
    	
    	appointmentList.clear();
    	
    	
    	
    	String str = Client.getAppointmentExclusive();
    	
    	System.out.println("hentet avtaler");
    	System.out.println(str); //THIS GENERATES A LOT OF SPAM IN CONSOLE
    	
    	
    	String[] appStrings = str.split(Pattern.quote("$%"));
    	
    	/*
    	for(int i = 0; i < appStrings.length; i++) {
    		System.out.println(appStrings[i] + "\n");
    	}
    	*/
    	
    	
    	for(int i = 0; i < appStrings.length; i++) {
    		if(appStrings[i].length() > 3) { //dirtyfix
    			addAppointment(appStrings[i]);    			
    		}
    	}
    	
    	
    	
    	
    	appointmentTable.setItems(appointmentList);
    	
    	if(mainApp.appointmentToSelect != null) {
    		selectInvitation(mainApp.appointmentToSelect);    		
    	}
    	
    }
	
	public void selectInvitation(Appointment appointment) {
    	
    	
    	for(int i = 0; i < appointmentList.size(); i++) {
    		if(appointment.getID().equals(appointmentList.get(i).getID())) {
    			index = i;
    			break;
    		}
    	}
    	
    	Platform.runLater(new Runnable()
    	{
    	    @Override
    	    public void run()
    	    {
    	        appointmentTable.requestFocus();
    	        
    	        appointmentTable.getSelectionModel().select(index);
    	        appointmentTable.getFocusModel().focus(index);
    	    }
    	});
    	
    	mainApp.appointmentToSelect = null;
	}
	

	/**
	 * Updates an employee's attendance status.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @param selection Selects a valid status. 1 = "Venter", 2 = "Deltar", 3 = "Avkreftet", 4 = "Avbud". Other values are ignored. Note that "Venter" is the default value in the DB if nothing has been set.
	 * @return True if status was changed successfully, false otherwise.
	 * @throws IOException 
	 */
	
	public void acceptAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(Client.changeStatus(appointment.getID(), "2").equals("true")){
			initAppointmetTable();
		};
		
	}
	
	public void declineAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(Client.changeStatus(appointment.getID(), "3").equals("true")){
			initAppointmetTable();
		};
		
	}
	
	public void avbudAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(Client.changeStatus(appointment.getID(), "4").equals("true")){
			initAppointmetTable();
		};
	}
	
	public void hideAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		Client.hideAppointment(appointment);
		if(Client.hideAppointment(appointment).equals("true")){
			initAppointmetTable();
		};
	}
	
	
	
}
