package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Notification {

	private StringProperty beskrivelse;
	private StringProperty formalProperty = new SimpleStringProperty();
	
	public Notification() {
		
	}
	
	public Notification(String beskrivelse) {
		this.beskrivelse = new SimpleStringProperty(beskrivelse);
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
	
	public String getDescription() {
		return formalProperty.getValue();
	}
	
	public void setDescription(String formal) {
		formalProperty.setValue(formal);
	}
	
}
