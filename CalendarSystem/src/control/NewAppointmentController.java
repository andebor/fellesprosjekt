package control;


import java.util.List;
import java.net.URL;	
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class NewAppointmentController implements Initializable {
	
	MainApp mainApp;
	
	 public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
		 }
	 
	@FXML
	TextField descriptionField;
	
	@FXML 
	TextField placeField, startField, endField, alarmField, roomAmountField;
	
	@FXML
	DatePicker datePicker;
	
	@FXML
	Label errorLabel;
	
	@FXML
	Button acceptButton, declineButton, addEmployersButton, addGroupsButton, removeEmployers;
	
	@FXML
	ToggleButton alarmButton, reservationButton;
	
	@FXML
	ListView<String> employersTable; //String need to be changes to userss
	
	@FXML
	ListView<String> groupsTable; //String need to be changes to users
	
	@FXML
	ListView<String> addedTable;
	
	@FXML
	ListView<String> roomTable; // String needs to be changed to room object
	

	private Stage dialogStage;

	protected ObservableList<String> addedList = FXCollections.observableArrayList(); // currently list over added employers
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
		dateCalenderfix();
		
		
		
	}	
	
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void dateCalenderfix(){
		// gj�r datoer f�r dagens dato utilgjengelige
		datePicker.setValue(LocalDate.now());

        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
            }
        };

        StringConverter converter = new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null)
                	return dateFormatter.format(date);
                else
                	return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    LocalDate date = LocalDate.parse(string, dateFormatter);

                    if (date.isBefore(LocalDate.now())) {
                        return datePicker.getValue();
                    }
                    else
                    	return date;
                }
                else
                	return null;
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setConverter(converter);
        datePicker.setPromptText("dd/MM/yyyy");
    }

	
	
	public boolean placeValidation(String placeString) {
		// Validate "sted" input
		if (placeString.isEmpty()){
			placeField.setPromptText("Trenger navn på bygning");
			return false;
			}	
		return true;
		
	}
	
	public boolean descriptionValidation(String descriptionString) {
		// Valide "beskrivelse" input
		if(descriptionString.isEmpty()){
			descriptionField.setPromptText("Trenger beskrivelse");
			return false;
		}
		return true;	
		
	}
	
	public boolean dateValidation(LocalDate dateCheck) {
		// Validate dato input
		if(dateCheck == null){
			datePicker.setPromptText("Trenger dato");
			return false;
		}
		
		if (dateCheck.isAfter(LocalDate.now())){
			return true;
		}
		else {
			datePicker.setValue(null);
			datePicker.setPromptText("Ugydlig dato!");
			return false;
		}

	}
	
	public boolean startTimeValidation(String starts){
		// Validate "Fra-tid"
		
		  try { 
				LocalTime startTime = LocalTime.of(Integer.parseInt(starts.substring(0,2)), Integer.parseInt(starts.substring(2,4)));
		    } catch(Exception e) { 
		    	startField.setText("");
		    	startField.setPromptText("Ugyldig input");
		    	return false;
		    }
		  return true;
		  
				
	}
	
	public boolean endTimeValidation(String ends){
		// validate "til-tid"
		  try { 
				LocalTime endTime = LocalTime.of(Integer.parseInt(ends.substring(0,2)), Integer.parseInt(ends.substring(2,4)));
		    } catch(Exception e) { 
		    	endField.setText("");
		    	endField.setPromptText("Ugyldig input");
		    	return false;
		    }

		  // Check if start time is before end time
		LocalTime startTime = LocalTime.of(Integer.parseInt(startField.getText().substring(0,2)), Integer.parseInt(startField.getText().substring(2,4)));
		LocalTime endTime = LocalTime.of(Integer.parseInt(ends.substring(0,2)), Integer.parseInt(ends.substring(2,4)));
				
        
		if(endTime.isBefore(startTime)){
			endField.setText("start < slutt");
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
		employersTable.setItems(list);									
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
		groupsTable.setItems(groupList);	
		
	}
	
	public void generateRoomList() {
		
		//Get list from database
		//String need to be changes to users
		// Using a example list to test functionality

		ObservableList<String> list = FXCollections.observableArrayList(
				"Grouproom 1",
				"Grouproom 2",
				"Grouproom 3");
		roomTable.setItems(list);	
		
	}
	
	
	
	public void addEmployers(String user){
		// Move employers from "ansatte" table view to "deltaker" table view
		if(addedList.contains(user) || employersList.isEmpty()){
			return;
		}
		addedList.add(user);
		employersList.remove(user);
		employersTable.setItems(employersList);
		addedTable.setItems(addedList);

		
	}
	
	public void addGroup(String group){
		// Move group(employers) from "group" table view to "deltaker" table view
		if(addedList.contains(group)){
			return;
		}
		addedList.add(group);
//		groupList.remove(group);
//		groups.setItems(groupList);
		addedTable.setItems(addedList);
		
	}
	
	public void remove(String user){
		// Remove employers from "deltakere" table view back to "ansatte" table view
		if(!addedList.contains(user) || addedList.isEmpty()){
			return;
		}
		addedList.remove(user);
		employersList.add(user);
		employersTable.setItems(employersList);
		addedTable.setItems(addedList);
		
	}
	
	public void addButtonAction(ActionEvent event)  {
		
		addEmployers(employersTable.getSelectionModel().getSelectedItem());
		
		
	}
	
	public void addGroupButtonAction(ActionEvent event) {
		
		addGroup(groupsTable.getSelectionModel().getSelectedItem());
		
	}
	
	public void removeButtonAction(ActionEvent event) {
		
		remove(addedTable.getSelectionModel().getSelectedItem());
		
	}
	
	public boolean alarmValidation(String alarmString){
		
		try { 
	        Integer.parseInt(alarmString); 
	   
	    } catch(Exception e) { 
	    	alarmField.setText("");
	    	alarmField.setPromptText("Ugyldig input");
	    	return false;
	    }
	    int alarmInt = Integer.parseInt(alarmString); 
		if(alarmInt<0){
			alarmField.setText("");
			alarmField.setPromptText("Ugydlig input");
			return false;
		}
		
		return true;
		
	}
	
	public boolean roomValidation(){
		// Check if reservation button is pressed
		if(roomTable.getSelectionModel().isEmpty()){
			reservationButton.setText("Reserver m�terom: Velg ledig m�terom");
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
		
		if(descriptionValidation(descriptionField.getText())){
			validationCheck++;
		}
		
		if(dateValidation(datePicker.getValue())){
			validationCheck++;
		}
		
		if(startTimeValidation(startField.getText())){
			validationCheck++;
		}

		if(endTimeValidation(endField.getText())){
			validationCheck++;
		}
		
		if(reservationButton.isSelected()){
			placeField.setText(null);
			placeField.setPromptText("M�TEROM");
			if(roomValidation()){
				validationCheck++;
			}
		}
		if(!reservationButton.isSelected()){
			if(placeValidation(placeField.getText())){
				validationCheck++;
			}
		}
		if(!addedList.isEmpty()){
			validationCheck++;
		}
		

		
		if(alarmButton.isSelected()){
			if(!alarmValidation(alarmField.getText())){
				validationCheck--;
			}
		}
		
		if(reservationButton.isSelected()){
			if(!roomAmountValidation()){
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


			
			appointment.setDescription(descriptionField.getText());
			appointment.setDate(datePicker.getValue());
			appointment.setDate(datePicker.getValue());
			appointment.setStart(LocalTime.of(Integer.parseInt(startField.getText().substring(0,2)), Integer.parseInt(startField.getText().substring(2,4))));
			appointment.setFrom(LocalTime.of(Integer.parseInt(endField.getText().substring(0,2)), Integer.parseInt(endField.getText().substring(2,4))));
			if(reservationButton.isSelected()){
				appointment.setRoomAmount(Integer.parseInt(roomAmountField.getText()));		
				appointment.setRoom(roomTable.getSelectionModel().getSelectedItem());
				appointment.setPlace(null);
			}
			if(!reservationButton.isSelected()){
			appointment.setPlace(placeField.getText());
			appointment.setRoom(null);
			}
			if(alarmButton.isSelected()){
			appointment.setAlarm(Integer.parseInt(alarmField.getText()));
			}
			appointment.setUsers(addedTable.getItems());
			
			// Transfer generated appointment object to database
			if(!editNewAppointment){
				errorLabel.setText("Ny avtale lagt inn!");
				AppointmentOverviewController.getAppointmentList().add(appointment);
				dialogStage.close();
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
		
		try {
			if(roomAmountField.getText().isEmpty()){
				roomAmountField.setText("Trenger tall");
				return true;
			}
			if (Integer.parseInt(roomAmountField.getText())<=0){
				roomAmountField.setText("");
				roomAmountField.setPromptText("Ugyldig tall");
				return false;
			}
		}
		catch(Exception e){
			roomAmountField.setText("");
			roomAmountField.setPromptText("Ugyldig tall");
		return false;
		}
		return true;
	}
	

	
	public void cleanAppointment(ActionEvent event) {
		
		// Clean form. Not complete
//		dialogStage.close();
		mainApp.showAppointmentOverview();
		
	}
	
}
