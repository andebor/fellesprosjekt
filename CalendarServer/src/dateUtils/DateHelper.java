package dateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateHelper {
	
	public static String getMySQLDateTime() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		String time = timeFormat.format(new Date());
		return time;
	}
	
	public static String getDayInNorwegian(int dayNo) {
		//TODO: Flytt denne metoden et egnet sted
		switch (dayNo) {
		case 0: return "mandag";
		case 1: return "tirsdag";
		case 2: return "onsdag";
		case 3: return "torsdag";
		case 4: return "fredag";
		case 5: return "lørdag";
		case 6: return "søndag";
		default:
			System.out.println("Invalid day number.");
			return null;
		}
	}
	
	public static String getDayInNorwegian(String dayName) {
		//TODO: Flytt denne metoden et egnet sted
		switch (dayName) {
		case "Mon": return "mandag";
		case "Tue": return "tirsdag";
		case "Wed": return "onsdag";
		case "Thu": return "torsdag";
		case "Fri": return "fredag";
		case "Sat": return "lørdag";
		case "Sun": return "søndag";
		default:
			System.out.println("Invalid day number.");
			return null;
		}
	}
	
	public static String getMonthInNorwegian(int month) {
		//TODO: Flytt denne metoden et egnet sted
		switch (month) {
		case 0: return "januar";
		case 1: return "februar";
		case 2: return "mars";
		case 3: return "april";
		case 4: return "mai";
		case 5: return "juni";
		case 6: return "juli";
		case 7: return "august";
		case 8: return "september";
		case 9: return "oktober";
		case 10: return "november";
		case 11: return "desember";
		default:
			System.out.println("Invalid month number. Must be 0-11");
			return null;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateHelper dh = new DateHelper();
		System.out.println(dh.getMySQLDateTime());
	}

}
