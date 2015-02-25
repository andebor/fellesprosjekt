package control;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {

	private StringProperty formalProperty = new SimpleStringProperty();
	private StringProperty romProperty = new SimpleStringProperty();
	private IntegerProperty repetisjonProperty = new SimpleIntegerProperty();
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

}
