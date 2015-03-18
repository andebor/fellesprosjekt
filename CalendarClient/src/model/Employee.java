package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
	
	private StringProperty username, firstname, lastname, fullName;
	private IntegerProperty empNo;
	
	public Employee() {
		
	}
	
	public Employee(String username) {
		this.setUsername(new SimpleStringProperty(username));
		
		// Dummy data
		this.setFirstname(new SimpleStringProperty("Anders"));
		this.setLastname(new SimpleStringProperty("Borud"));
		this.setEmpNo(new SimpleIntegerProperty(12345));
		
	}

	public StringProperty getUsername() {
		return username;
	}

	public void setUsername(StringProperty username) {
		this.username = username;
	}

	public StringProperty getFirstname() {
		return firstname;
	}

	public void setFirstname(StringProperty firstname) {
		this.firstname = firstname;
	}

	public StringProperty getLastname() {
		return lastname;
	}

	public void setLastname(StringProperty lastname) {
		this.lastname = lastname;
	}

	public IntegerProperty getEmpNo() {
		return empNo;
	}

	public void setEmpNo(IntegerProperty empNo) {
		this.empNo = empNo;
	}

	public StringProperty getFullName() {
		return fullName;
	}

	public void setFullName(StringProperty fullName) {
		this.fullName = fullName;
	}


	
	

}
