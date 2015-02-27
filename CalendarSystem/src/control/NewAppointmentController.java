package control;


import java.util.List;
import java.net.URL;	
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class NewAppointmentController implements Initializable {
	
	
	@FXML
	TextField description;
	
	@FXML 
	TextField place;
	
	@FXML
	DatePicker date;
	
	@FXML
	TextField start;	
	
	@FXML
	TextField end;
	
	@FXML
	Label errorLabel;
	
	@FXML
	Button accept;
	
	@FXML
	Button decline;
	
	@FXML
	ListView<String> employers; //String need to be changes to userss
	
	@FXML
	ListView<String> groups; //String need to be changes to users
	
	@FXML
	ListView<String> added;
	
	@FXML
	Button addEmployers;
	
	@FXML
	Button addGroups;
	
	@FXML
	Button removeEmployers;
	
	@FXML
	ToggleButton alarmButton;
	
	@FXML
	ToggleButton reservation;
	
	@FXML
	TextField alarm;
	
	@FXML
	ListView<String> room; // String needs to be changed to room object
	
	@FXML
	TextField roomAmount;
	

	private Stage dialogStage;

	private ObservableList<String> addedList = FXCollections.observableArrayList(); // currently list over added employers
	protected ObservableList<String> employersList = FXCollections.observableArrayList(); //currently list over employers
	protected ObservableList<String> groupList = FXCollections.observableArrayList(); // // currently over groups
	protected Appointment appointmentToEdit; // appointment to be edited
	protected boolean editNewAppointment = false; // true if appointment is to be edited
	protected boolean cancelAppointment = false; // set true to cancel appointment (on edit appointment)
	

	
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialize tableviews 
		generateEmployersList();
		generateGroupsList();
		generateRoomList();
		
	}	
	
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	
	
	public boolean placeValidation(String placeString) {
		// Validate "sted" input
		if (placeString.isEmpty()){
			place.setPromptText("Trenger navn på bygning");
			return false;
			}	
		return true;
		
	}
	
	public boolean descriptionValidation(String descriptionString) {
		// Valide "beskrivelse" input
		if(descriptionString.isEmpty()){
			description.setPromptText("Trenger beskrivelse");
			return false;
		}
		return true;	
		
	}
	
	public boolean dateValidation(LocalDate dateCheck) {
		// Validate dato input
		if(dateCheck == null){
			date.setPromptText("Trenger dato");
			return false;
		}
		
		if (dateCheck.isAfter(LocalDate.now())){
			return true;
		}
		else {
			date.setValue(null);
			date.setPromptText("Ugydlig dato!");
			return false;
		}

	}
	
	public boolean startTimeValidation(String starts){
		// Validate "Fra-tid"
		
		  try { 
				LocalTime startTime = LocalTime.of(Integer.parseInt(starts.substring(0,2)), Integer.parseInt(starts.substring(2,4)));
		    } catch(Exception e) { 
		    	start.setText("Ugyldig input");
		    	return false;
		    }
		  return true;
		  
				
	}
	
	public boolean endTimeValidation(String ends){
		// validate "til-tid"
		  try { 
				LocalTime endTime = LocalTime.of(Integer.parseInt(ends.substring(0,2)), Integer.parseInt(ends.substring(2,4)));
		    } catch(Exception e) { 
		    	end.setText("Ugyldig input");
		    	return false;
		    }

		  // Check if start time is before end time
		LocalTime startTime = LocalTime.of(Integer.parseInt(start.getText().substring(0,2)), Integer.parseInt(start.getText().substring(2,4)));
		LocalTime endTime = LocalTime.of(Integer.parseInt(ends.substring(0,2)), Integer.parseInt(ends.substring(2,4)));
				
        
		if(endTime.isBefore(startTime)){
			end.setText("start < slutt");
			return false;
		}
		return true;
        
	}
	
	public void generateEmployersList() {
		
		//Get list from database
		//String need to be changes to users
		// Using a example list to test functionality
		ObservableList<String> list = FXCollections.observableArrayList(
				"Ansatt 1",
				"Ansatt 2",
				"Ansatt 3",
				"Ansatt 4",
				"Ansatt 5",
				"Ansatt 6",
				"Ansatt 7");
		employersList = list;
		employers.setItems(list);									
	}
	
	public void generateGroupsList() {
		
		//Get list from database
		//String need to be changes to users
		// Using a example list to test functionality
		ObservableList<String> list = FXCollections.observableArrayList(
				"Group 1",
				"Group 2",
				"Group 3");
		groupList = list;
		groups.setItems(groupList);	
		
	}
	
	public void generateRoomList() {
		
		//Get list from database
		//String need to be changes to users
		// Using a example list to test functionality

		ObservableList<String> list = FXCollections.observableArrayList(
				"Grouproom 1",
				"Grouproom 2",
				"Grouproom 3");
		room.setItems(list);	
		
	}
	
	
	
	public void addEmployers(String user){
		// Move employers from "ansatte" table view to "deltaker" table view
		if(addedList.contains(user) || employersList.isEmpty()){
			return;
		}
		addedList.add(user);
		employersList.remove(user);
		employers.setItems(employersList);
		added.setItems(addedList);

		
	}
	
	public void addGroup(String group){
		// Move group(employers) from "group" table view to "deltaker" table view
		if(addedList.contains(group)){
			return;
		}
		addedList.add(group);
//		groupList.remove(group);
//		groups.setItems(groupList);
		added.setItems(addedList);
		
	}
	
	public void remove(String user){
		// Remove employers from "deltakere" table view back to "ansatte" table view
		if(!addedList.contains(user) || addedList.isEmpty()){
			return;
		}
		addedList.remove(user);
		employersList.add(user);
		employers.setItems(employersList);
		added.setItems(addedList);
		
	}
	
	public void addButtonAction(ActionEvent event)  {
		
		addEmployers(employers.getSelectionModel().getSelectedItem());
		
		
	}
	
	public void addGroupButtonAction(ActionEvent event) {
		
		addGroup(groups.getSelectionModel().getSelectedItem());
		
	}
	
	public void removeButtonAction(ActionEvent event) {
		
		remove(added.getSelectionModel().getSelectedItem());
		
	}
	
	public boolean alarmValidation(String alarmString){
		
		try { 
	        Integer.parseInt(alarmString); 
	   
	    } catch(NumberFormatException e) { 
	    	alarm.setPromptText("Ugyldig input");
	    	return false;
	    }
	    int alarmInt = Integer.parseInt(alarmString); 
		if(alarmInt<0){
			alarm.setPromptText("Ugydlig input");
			return false;
		}
		
		return true;
		
	}
	
	public boolean roomValidation(){
		// Check if reservation button is pressed
		if(room.getSelectionModel().isEmpty()){
			reservation.setText("Reserver m�terom: Velg ledig m�terom");
			return false;
		}
		return true;
		
	}
	


	public void acceptAppointment(ActionEvent event) throws Exception  {
		
		
		//Possible refactor: Remove inputs from validation methods.
		// Errorlabel for debugging
		errorLabel.setText("");
		
		// Need to pass 6 validations checks for new appointment to be created
		int validationCheck = 0;
		
		if(descriptionValidation(description.getText())){
			validationCheck++;
		}
		
		if(dateValidation(date.getValue())){
			validationCheck++;
		}
		
		if(startTimeValidation(start.getText())){
			validationCheck++;
		}

		if(endTimeValidation(end.getText())){
			validationCheck++;
		}
		
		if(reservation.isSelected()){
			place.setText(null);
			place.setPromptText("M�TEROM");
			if(roomValidation()){
				validationCheck++;
			}
		}
		if(!reservation.isSelected()){
			if(placeValidation(place.getText())){
				validationCheck++;
			}
		}
		if(!addedList.isEmpty()){
			validationCheck++;
		}
		
		if(alarmButton.isSelected()){
			if(!alarmValidation(alarm.getText())){
				validationCheck--;
			}
		}
		
		if(validationCheck==6)	{ 
			
			// Validation passed. Creating new appointment
			
			Appointment appointment = new Appointment();
			
			// If edit appointment is selected, the appointment selected to be edited will be updated instead of creating a new one. 
			if(editNewAppointment){
				appointment = appointmentToEdit;
			}


			
			appointment.setDescription(description.getText());
			appointment.setDate(date.getValue());
			appointment.setDate(date.getValue());
			appointment.setStart(LocalTime.of(Integer.parseInt(start.getText().substring(0,1)), Integer.parseInt(start.getText().substring(2,3))));
			appointment.setFrom(LocalTime.of(Integer.parseInt(end.getText().substring(0,1)), Integer.parseInt(end.getText().substring(2,3))));
			if(reservation.isSelected()){
				if(!roomAmount.contains(null) && roomAmountValidation()){
					appointment.setRoomAmount(Integer.parseInt(roomAmount.getText()));
				}			
				appointment.setRoom(room.getSelectionModel().getSelectedItem());
			}
			if(!reservation.isSelected()){
			appointment.setPlace(place.getText());
			}
			appointment.setAlarm(Integer.parseInt(alarm.getText()));
			appointment.setUsers(added.getItems());
			
			// Transfer generated appointment object to database
			if(!editNewAppointment){
			errorLabel.setText("Ny avtale lagt inn!");
			}
			else {
				errorLabel.setText("Avtale er endret!");
				//Notify change to users
			}
	
		}
		else {
		errorLabel.setText(" "+validationCheck+ " of  6 checks");
		}

	}
	
	public boolean roomAmountValidation(){
		
		if(Integer.parseInt(roomAmount.getText())<=0){
			roomAmount.setPromptText("Ugylddig tall");
			return false;
		}
		return true;
		
	}
	
	public void cleanAppointment(ActionEvent event) {
		
		// Clean form. Not complete
		errorLabel.setText("Skjema nullstilt");
		
	}
	
}
