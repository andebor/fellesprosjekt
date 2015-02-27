package control;

import java.net.URL;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
//asda

public class editAppointmentController extends NewAppointmentController implements Initializable {

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		editNewAppointment = true;
		
	}
	
	public void initAppointment(Appointment app){
		
		this.appointmentToEdit = app;
		
	}
	
	public void loadAppointment(){
		
		super.descriptionField.setText(appointmentToEdit.getDescription());
		if(appointmentToEdit.getPlace()==null){
			super.placeField.setText(appointmentToEdit.getRoom());
		}
		else{
			super.placeField.setText(appointmentToEdit.getPlace());
		}
		super.datePicker.setValue(appointmentToEdit.getDate());
	    super.startField.setText(appointmentToEdit.getStart().toString());
	    super.endField.setText(appointmentToEdit.getFrom().toString());
		ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
		super.addedTable.setItems(list);
		if(!(appointmentToEdit.getAlarm()==0)){
		super.alarmField.setText(Integer.toString(appointmentToEdit.getAlarm()));
		}
		super.generateRoomList();
		super.generateGroupsList();
	//	ObservableList<String> newEmployerList = FXCollections.observableArrayList(appointmentToEdit.getUsers();
	//	for(String employer1 : super.employerList) {
	//		if(!list.contains(employer1)){
	//			newEmployerList.add(employer1);
	//		}
	//	}
	//	super.employersList = new newEmployerList;
	//	super.employers.setItems(super.employersList);
	}
	
	public Appointment testAppointment(){//	ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
	

		
		Appointment test = new Appointment();
//		test.createExampleAppointment();
		return test;
		
	}
	
	public void cancelApp(){
		
		cancelAppointment = true;
		// NOTIFY
		
	}
	
	

}
