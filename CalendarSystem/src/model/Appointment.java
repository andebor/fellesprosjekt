package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// asd
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointment {
	//as
	private boolean cancel;
	private StringProperty beskrivelse;
	private StringProperty dato;
	private String room;
	private int roomAmount;
	private List<String> users;
	private int alarm = 0;
	private String identificationKey;
	private StringProperty formalProperty = new SimpleStringProperty();
	private StringProperty romProperty = new SimpleStringProperty();
	
	public Appointment() {
		
	}
	
	public Appointment(String beskrivelse, String dato) {
		this.beskrivelse = new SimpleStringProperty(beskrivelse);
		this.dato = new SimpleStringProperty(dato);
	}
	
	public String getBeskrivelse() {
		return beskrivelse.get();
	}
	
	public StringProperty beskrivelseProperty() {
		return beskrivelse;
	}
	
	public StringProperty descriptionProperty() {
		return formalProperty;
	}
	
	public StringProperty dateProperty(){
		return new SimpleStringProperty(getDate().toString());
	}
	
	public String getDato() {
		return dato.get();
	}
	
	public StringProperty datoProperty() {
		return dato;
	}
	
	
	private Property<LocalDate> datoProperty = new ObjectPropertyBase<LocalDate>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "dato";
		}
	};
	private Property<LocalTime> fraProperty = new ObjectPropertyBase<LocalTime>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "fra";
		}
	};
	private Property<LocalTime> tilProperty = new ObjectPropertyBase<LocalTime>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "til";
		}
	};
	private Property<LocalDate> sluttProperty = new ObjectPropertyBase<LocalDate>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "slutt";
		}
	};

	public String getDescription() {
		return formalProperty.getValue();
	}

	public void setDescription(String formal) {
		formalProperty.setValue(formal);
	}

	public StringProperty formalProperty() {
		return formalProperty;
	}

	public String getPlace() {
		return romProperty.getValue();
	}

	public void setPlace(String rom) {
		romProperty.setValue(rom);
	}

	public StringProperty romProperty() {
		return romProperty;
	}

	public LocalDate getDate() {
		return datoProperty.getValue();
	}

	public void setDate(LocalDate dato) {
		datoProperty.setValue(dato);
	}

	public Property<LocalDate> DatoProperty() {
		return datoProperty;
	}

	public LocalTime getStart() {
		return fraProperty.getValue();
	}

	public void setStart(LocalTime fra) {
		fraProperty.setValue(fra);
	}

	public Property<LocalTime> fraProperty() {
		return fraProperty;
	}

	public LocalTime getFrom() {
		return tilProperty.getValue();
	}

	public void setFrom(LocalTime til) {
		tilProperty.setValue(til);
	}

	public Property<LocalTime> tilProperty() {
		return tilProperty;
	}

	public LocalDate getEnd() {
		return sluttProperty.getValue();
	}

	public void setEnd(LocalDate slutt) {
		sluttProperty.setValue(slutt);
	}

	public Property<LocalDate> sluttProperty() {
		return sluttProperty;
	}

	public String getIdentificationKey() {
		return identificationKey;
	}

	public void setIdentificationKey(String identificationKey) {
		this.identificationKey = identificationKey;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getRoomAmount() {
		return roomAmount;
	}

	public void setRoomAmount(int roomAmount) {
		this.roomAmount = roomAmount;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	


}
