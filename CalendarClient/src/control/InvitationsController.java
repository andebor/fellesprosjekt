package control;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

public class InvitationsController extends AppointmentOverviewController {


	@Override
    public void addAppointment(String str) {
    	Appointment appointment = new Appointment();
    	

    	String[] z = str.split(Pattern.quote("%$"));
    	
    	//System.out.println("LENGDE: " + z.length);
    	
    	appointment.setDescription(z[0]);
    	
    	
    	
    	String[] startDate = z[1].split(" ");
    	
    	String dato = startDate[0];
    	String[] datoList = dato.split("-");
    	
    	String[] endDate = z[2].split(" ");
    	
    	String startTid = startDate[1];
    	String[] startTidList = startTid.split(":");
    	
    	String endTid = endDate[1];
    	String[] endTidList = startTid.split(":");
    	
    	
    	String[] deltagere = z[7].split("@/@");
    	
    	ObservableList<String> usersList = FXCollections.observableArrayList();
    	
    	for(int i = 0; i < deltagere.length; i++) {
    		usersList.add(deltagere[i]);
    	}
    	
    	
    	appointment.setDate(LocalDate.of(Integer.parseInt(datoList[0]), Integer.parseInt(datoList[1]), Integer.parseInt(datoList[2])));
    	appointment.setStart(LocalTime.of(Integer.parseInt(startTidList[0]), Integer.parseInt(startTidList[1])));
    	appointment.setFrom(LocalTime.of(11,30));
    	appointment.setUsers(usersList);
    	appointment.setRoomAmount(2);
    	appointment.setID(z[6]);
    	appointment.setOwner(z[5]);
    	
    	if(z[4].equals("null")){
    		appointment.setPlace(z[3]);
    		appointment.setRoom("null");
    	}
    	else {
    		appointment.setRoom(z[4]);
    		appointment.setPlace("null");
    	}
    	try {
			if(Client.isInvitedEmployee(appointment.getID()).equals("true") && 
					!Client.checkAppointmentOwnership(appointment.getID()).equals("true")){				
			appointmentList.add(appointment);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
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
			super.initAppointmetTable();
		};
		
	}
	
	public void declineAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(Client.changeStatus(appointment.getID(), "3").equals("true")){
			super.initAppointmetTable();
		};
		
	}
	
	public void avbudAppointment(ActionEvent event) throws IOException{
		
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(Client.changeStatus(appointment.getID(), "4").equals("true")){
			super.initAppointmetTable();
		};
	}
	
	
	
}
