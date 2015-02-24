package control;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class newAppointmentController implements Initializable {
	
	
	@FXML
	TextField objective;
	
	@FXML 
	TextField room;
	
	@FXML
	TextField roomNumber;
	
	@FXML
	TextField date;
	
	@FXML
	TextField setTimeFromHours;
	
	@FXML
	TextField setTimeFromMinutes;
	
	@FXML
	TextField setTimeToHours;
	
	@FXML
	TextField setTimeToMinutes;
	
	@FXML
	TextField repetitionFrequency;
	
	@FXML
	TextField endDate;
	
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

	

	
	public void acceptAppointment(ActionEvent event) throws Exception  {
		
		errorLabel.setText("");
		
		appointment.setFormal(objective.getText());

		
		String romString = room.getText();
		if (romString.isEmpty()){
			errorLabel.setText("Trenger navn på bygning");
			return;
		}
		String romStringNumber = roomNumber.getText();
	    try { 
	        Integer.parseInt(romStringNumber); 
	    } catch(NumberFormatException e) { 
	    	errorLabel.setText("Romnummer må være et tall");
	    	return;
	    }
	    appointment.setRom(romString + " " + romStringNumber);
		
		
		
		String dateString = date.getText();
		if(dateString.trim().length() == 0){
			errorLabel.setText("Trenger dato!");
			return;
		}
		try {
			LocalDate myDate = LocalDate.parse(dateString);
			if (myDate.isAfter(LocalDate.now())){
			appointment.setDato(myDate);
			}
			else {
				errorLabel.setText("Ugydlig dato!");
				return;
			}
		} catch (Exception e) {
			errorLabel.setText("Dato må være på formatet yyyy-mm-dd");
			date.clear();
			return;
		}
		
		String setTimeFromHoursString = setTimeFromHours.getText();
		String setTimeFromMinString = setTimeFromMinutes.getText();
	    try { 
	        Integer.parseInt(setTimeFromHoursString); 
	        Integer.parseInt(setTimeFromMinString); 
	   
	    } catch(NumberFormatException e) { 
	    	errorLabel.setText("Fra-tidspunkt må bestå av tall");
	    	return;
	    }
	    int hours = Integer.parseInt(setTimeFromHoursString); 
	    int minutes = Integer.parseInt(setTimeFromMinString); 
		if(hours>24 || hours<0){
			errorLabel.setText("Fra-tidspunkt: timer må være på formatet 0-24");
			setTimeFromHours.clear();
			return;
		}
		if(minutes>60 || minutes<0){
			errorLabel.setText("Fra-tidspunkt: minutter må v�ære på formatet 0:60");
			setTimeFromMinutes.clear();
			return;
		}
		LocalTime from = LocalTime.of(hours, minutes);
		appointment.setFra(from);
		
		String setTimeToHoursString = setTimeToHours.getText();
		String setTimeToMinString = setTimeToMinutes.getText();
	    try { 
	    	Integer.parseInt(setTimeToHoursString); 
	        Integer.parseInt(setTimeToMinString); 
	   
	    } catch(NumberFormatException e) { 
	    	errorLabel.setText("Til-tidspunkt må bestå av tall");
	    	return;
	    }
	    int hours2 = Integer.parseInt(setTimeToHoursString); 
	    int minutes2 = Integer.parseInt(setTimeToMinString); 
		if(hours2>24 || hours2<0){
			errorLabel.setText("Til-tidspunkt: timer må være på formatet 0-24");
			setTimeToHours.clear();
			return;
		}
		if(minutes2>60 || minutes2<0){
			errorLabel.setText("Til-tidspunkt: minutter må være på formatet 0:60");
			setTimeToMinutes.clear();
			return;
		}
		LocalTime to = LocalTime.of(hours2, minutes2);
		if(to.isBefore(from)){
			errorLabel.setText("Møter over midnatt støttes ikke");
			setTimeToMinutes.clear();
			setTimeToHours.clear();
			return;
		}
		appointment.setTil(to);
		
		String frequencyString = repetitionFrequency.getText();
	    try { 
	        Integer.parseInt(frequencyString);  
	   
	    } catch(NumberFormatException e) { 
	    	errorLabel.setText("Repetisjonsfrekvens må være et tall");
	    	repetitionFrequency.clear();
	    	return;
	    }
	    int frequencyInt = Integer.parseInt(frequencyString);
	    appointment.setRepetisjon(frequencyInt);
	    
	    String endDateString = endDate.getText();
		try {
			LocalDate myEndDate = LocalDate.parse(endDateString);
			if(appointment.getRepetisjon()<=0) {
				errorLabel.setText("Ingen repetisjon!");
				endDate.clear();
				return;
			}
			if (myEndDate.isAfter(appointment.getDato())){
			appointment.setSlutt(myEndDate);
			}
			else {
				errorLabel.setText("Ugydlig sluttdato!");
				return;
			}
		} catch (Exception e) {
			errorLabel.setText("Dato må være på formatet yyyy-mm-dd");
			endDate.clear();
		}
		errorLabel.setText("Suksess!");

		
	}
	
	public void cleanAppointment(ActionEvent event) {
		
		errorLabel.setText("Skjema nullstilt");
		
		roomNumber.clear();
		objective.clear();
		room.clear();
		date.clear();
		setTimeFromHours.clear();
		setTimeFromMinutes.clear();
		setTimeToHours.clear();
		setTimeToMinutes.clear();
		repetitionFrequency.clear();
		endDate.clear();
		
		
	}
	
}
	