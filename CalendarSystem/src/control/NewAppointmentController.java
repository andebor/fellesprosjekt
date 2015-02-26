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
	
	@FXML
	ListView<String> employers; //String need to be changes to users
	
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

	private ObservableList<String> addedList = FXCollections.observableArrayList();
	private ObservableList<String> employersList = FXCollections.observableArrayList();
	private ObservableList<String> groupList = FXCollections.observableArrayList();
	protected Appointment appointmentToEdit;
	protected boolean editNewAppointment = false;
	protected boolean cancelAppointment = false;
	

	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		generateEmployersList();
		generateGroupsList();
		generateRoomList();
		
	}	
	
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	
	
	public boolean placeValidation(String placeString) {
		
		if (placeString.isEmpty()){
			place.setPromptText("Trenger navn på bygning");
			return false;
			}	
		return true;
		
	}
	
	public boolean descriptionValidation(String descriptionString) {
		
		if(descriptionString.isEmpty()){
			description.setPromptText("Trenger beskrivelse");
			return false;
		}
		return true;	
		
	}
	
	public boolean dateValidation(LocalDate dateCheck) {
			
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
	
	public boolean startTimeValidation(String startHour, String startMin){
		
		  try { 
		        Integer.parseInt(startHour); 
		        Integer.parseInt(startMin); 
		   
		    } catch(NumberFormatException e) { 
		    	startHours.setPromptText("Ugyldig input");
		    	startMinutes.setPromptText("Ugyldig input");
		    	return false;
		    }
		    int hours = Integer.parseInt(startHour); 
		    int minutes = Integer.parseInt(startMin); 
			if(hours>24 || hours<0){
				startHours.setPromptText("Ugydlig input");
				return false;
			}
			if(minutes>60 || minutes<0){
				startMinutes.setText("Ugydlig input");
				return false;
			}
				
			return true;
				
	}
	
	
	
	public boolean endTimeValidation(String endHour, String endMin){
				
		
		try { 
		        Integer.parseInt(endHour); 
		        Integer.parseInt(endMin); 
		   
		    } catch(NumberFormatException e) { 
		    	endHours.setPromptText("Ugyldig input");
		    	endMinutes.setPromptText("Ugyldig input");
		    	return false;
		    }
		    int hours = Integer.parseInt(endHour); 
		    int minutes = Integer.parseInt(endMin); 
			if(hours>24 || hours<0){
				endHours.setPromptText("Ugydlig input");
				return false;
			}
			if(minutes>60 || minutes<0){
				endMinutes.setText("Ugydlig input");
				return false;
			}
				
        LocalTime endTime = LocalTime.of(hours, minutes);
        LocalTime fromTime = LocalTime.of(Integer.parseInt(startHours.getText()), Integer.parseInt(startMinutes.getText()));
		
		if(endTime.isBefore(fromTime)){
			endHours.setText("start < slutt");
			endMinutes.setText("start < slutt");
			return false;
		}
		return true;
        
	}
	
	public void generateEmployersList() {
		
		//Get list from database
		//String need to be changes to users
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
		ObservableList<String> list = FXCollections.observableArrayList(
				"Grouproom 1",
				"Grouproom 2",
				"Grouproom 3");
		room.setItems(list);	
		
	}
	
	
	
	public void addEmployers(String user){
		
		if(addedList.contains(user)){
			return;
		}
		addedList.add(user);
		employersList.remove(user);
		employers.setItems(employersList);
		added.setItems(addedList);

		
	}
	
	public void addGroup(String group){
		
		if(addedList.contains(group)){
			return;
		}
		addedList.add(group);
//		groupList.remove(group);
//		groups.setItems(groupList);
		added.setItems(addedList);
		
	}
	
	public void remove(String user){
		
		if(!addedList.contains(user)){
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
		
		if(room.getSelectionModel().isEmpty()){
			reservation.setText("Reserver m�terom: Velg ledig m�terom");
			return false;
		}
		return true;
		
	}
	


	public void acceptAppointment(ActionEvent event) throws Exception  {
		
		
		//Possible refactor: Remove inputs from validation methods.
		
		errorLabel.setText("");
		
		
		int validationCheck = 0;
		
		if(descriptionValidation(description.getText())){
			validationCheck++;
		}
		
		if(dateValidation(date.getValue())){
			validationCheck++;
		}
		
		if(startTimeValidation(startHours.getText(), startMinutes.getText())){
			validationCheck++;
		}

		if(endTimeValidation(endHours.getText(), endMinutes.getText())){
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
			
			Appointment appointment = appointmentToEdit;
			
			if(!editNewAppointment){
			appointment = new Appointment();
			}

			//Generate unique primary key / ID for appointment object
			// CODE
			// CODE
			
			appointment.setDescription(description.getText());
			appointment.setDate(date.getValue());
			appointment.setDate(date.getValue());
			appointment.setStart(LocalTime.of(Integer.parseInt(startHours.getText()), Integer.parseInt(startMinutes.getText())));
			appointment.setStart(LocalTime.of(Integer.parseInt(startHours.getText()), Integer.parseInt(startMinutes.getText())));
			appointment.setFrom(LocalTime.of(Integer.parseInt(endHours.getText()), Integer.parseInt(endMinutes.getText())));
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
		errorLabel.setText(" "+validationCheck+ " of 7 checks");
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
		
		errorLabel.setText("Skjema nullstilt");
		
	}
	
}