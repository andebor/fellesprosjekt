package control;

import java.io.IOException;
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
	
	public void loadAppointment() throws IOException{
		
		super.descriptionField.setText(appointmentToEdit.getDescription());
	    super.startHourField.setValue(Integer.toString(appointmentToEdit.getStart().getHour()));
	    super.startMinuteField.setValue(Integer.toString(appointmentToEdit.getStart().getMinute()));
	    super.endHourField.setValue(Integer.toString(appointmentToEdit.getFrom().getHour()));
	    super.endMinuteField.setValue(Integer.toString(appointmentToEdit.getFrom().getMinute()));
		ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
		if(!(appointmentToEdit.getAlarm()==0)){
		super.alarmButton.setSelected(true);
		super.alarmField.setText(Integer.toString(appointmentToEdit.getAlarm()));
		}
		super.generateRoomList();
		super.generateGroupsList();
		super.generateEmployersList();
		for(String employer1 : list) {
			if(!employer1.equals("Venter")){
				try {String[] emp = employer1.split(" ");
				super.addEmployers(emp[1] + " " + emp[2] + " " + emp[0]);
				}
				catch(Exception e){
				//LOL random bug fix
				}
			} //
		}
		if(appointmentToEdit.getRoom()!=null){
			super.placeField.setPromptText(appointmentToEdit.getRoom());
			super.roomAmountField.setText(Integer.toString(appointmentToEdit.getRoomAmount()));
			super.reservationButton.setSelected(true);
			super.roomTable.getSelectionModel().select(appointmentToEdit.getRoom());
		}
		else{
			super.placeField.setText(appointmentToEdit.getPlace());
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