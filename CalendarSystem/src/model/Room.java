package model;

public class Room {
	
	private boolean reservation;
	private String name;
	
	public boolean isReservation() {
		return reservation;
	}
	public void setReservation(boolean reservation) {
		this.reservation = reservation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
