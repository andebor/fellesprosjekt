package control;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class newAppointmentController implements Initializable {
	
	
	@FXML
	TextField description;
	
	@FXML 
	TextField place;
	
	@FXML
	TextField roomNumber;
	
	@FXML
	DatePicker date;
	
	@FXML
	TextField startHours;
	
	@FXML
	TextField startMinutes;
	
	@FXML
	TextField endHours;
	
	@FXML
	TextField endMinutes;
	
	
	@FXML
	Label errorLabel;
	
	@FXML
	Button accept;
	
	@FXML
	Button decline;
	
	final Appointment appointment = new Appointment(); 
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}	

	
	
	public boolean placeValidation(String placeString) {
		
		if (placeString.isEmpty()){
			errorLabel.setText("Trenger navn på bygning");
			return false;
			}	
		return true;
		
	}
	
	public boolean descriptionValidation(String descriptionString) {
		
		if(descriptionString.isEmpty()){
			errorLabel.setText("Trenger beskrivelse");
			return false;
		}
		return true;	
		
	}
	
	public boolean dateValidation(LocalDate dateCheck) {
			
		if(dateCheck == null){
			errorLabel.setText("Trenger dato");
			return false;
		}
		
		if (dateCheck.isAfter(LocalDate.now())){
			return true;
		}
		else {
			errorLabel.setText("Ugydlig dato!");
			return false;
		}

	}
	
	public boolean timeValidation(String startHour, String startMin){
		
		  try { 
		        Integer.parseInt(startHour); 
		        Integer.parseInt(startMin); 
		   
		    } catch(NumberFormatException e) { 
		    	errorLabel.setText("Fra-tidspunkt må bestå av tall");
		    	return false;
		    }
		    int hours = Integer.parseInt(startHour); 
		    int minutes = Integer.parseInt(startMin); 
			if(hours>24 || hours<0){
				errorLabel.setText("Fra-tidspunkt: timer må være på formatet 0-24");
				return false;
			}
			if(minutes>60 || minutes<0){
				errorLabel.setText("Fra-tidspunkt: minutter må v�ære på formatet 0:60");
				return false;
			}
				
			return true;
				
	}
	
	public boolean endTimeValidation(String endHour, String endMin){
		
    	int hour = Integer.parseInt(endHour); 
        int min = Integer.parseInt(endMin);
		
        LocalTime endTime = LocalTime.of(hour, min);
        LocalTime fromTime = LocalTime.of(Integer.parseInt(startHours.getText()), Integer.parseInt(startMinutes.getText()));
		
		if(endTime.isBefore(fromTime)){
			errorLabel.setText("Møter over midnatt støttes ikke");
			return false;
		}
		return true;
        
	}
	
	
	
	
	

	
	public void acceptAppointment(ActionEvent event) throws Exception  {
		
		errorLabel.setText("");
		
		if(!descriptionValidation(description.getText())){
			return;
		}
		appointment.setDescription(description.getText());
		
		if(!dateValidation(date.getValue())){
			return;
		}
		appointment.setDate(date.getValue());
		
		if(!timeValidation(startHours.getText(), startMinutes.getText())){
			return;
		}
		appointment.setStart(LocalTime.of(Integer.parseInt(startHours.getText()), Integer.parseInt(startMinutes.getText())));
		
		if(!timeValidation(endHours.getText(), endMinutes.getText()) && !endTimeValidation(endHours.getText(), endMinutes.getText())){
			return;
		}
		appointment.setFrom(LocalTime.of(Integer.parseInt(endHours.getText()), Integer.parseInt(endMinutes.getText())));
			
		if(!placeValidation(place.getText())){
			return;
		}
		appointment.setPlace(place.getText());

		

		errorLabel.setText("Suksess!");

		
	}
	
	public void cleanAppointment(ActionEvent event) {
		
		errorLabel.setText("Skjema nullstilt");
		

		
		
	}
	
}
	