package control;

import java.net.URL;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class editAppointmentController extends NewAppointmentController implements Initializable {


	
	@FXML
	Label cancelAppointmentLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		editNewAppointment = true;
		
	}
	//as
	public void initAppointment(Appointment app){
		
		this.appointmentToEdit = app;
		
	}
	
	public void loadAppointment(){
		
		super.descriptionField.setText(appointmentToEdit.getDescription());
		if(appointmentToEdit.getPlace()==null){
			super.placeField.setPromptText(appointmentToEdit.getRoom());
			super.reservationButton.setSelected(true);
			super.roomTable.getSelectionModel().select(appointmentToEdit.getRoom());
		}
		else{
			super.placeField.setText(appointmentToEdit.getPlace());
		}
	    super.startField.setText(appointmentToEdit.getStart().toString());
	    super.endField.setText(appointmentToEdit.getFrom().toString());
		ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
		if(!(appointmentToEdit.getAlarm()==0)){
		super.alarmButton.setSelected(true);
		super.alarmField.setText(Integer.toString(appointmentToEdit.getAlarm()));
		}
		super.generateRoomList();
		super.generateGroupsList();
		super.generateEmployersList();
		for(String employer1 : list) {
			super.addEmployers(employer1);
		}
		super.dateCalenderfix();
		super.datePicker.setValue(appointmentToEdit.getDate());
		cancelAppointmentLabel.setText(""+appointmentToEdit.isCancel());
		
	}
	

	
	public void cancelApp(ActionEvent event){
		
		if(appointmentToEdit.isCancel()==true){
			appointmentToEdit.setCancel(false);
			cancelAppointmentLabel.setText(""+appointmentToEdit.isCancel());
		}
		else{
			appointmentToEdit.setCancel(true);
			cancelAppointmentLabel.setText(""+appointmentToEdit.isCancel());
		}
		
	}
	

}