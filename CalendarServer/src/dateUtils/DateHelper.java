package dateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
	
	public static String getMySQLDateTime() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		String time = timeFormat.format(new Date());
		return time;
	}
	
	public static String getMySQLDatetime(Date date) {
		//TODO: Fill out if necessary
		return null;
	}
	
	public static long getDateLong(String mysqlDT) {
		String [] date = mysqlDT.split("-");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2].substring(0, 2));
		String time = date[2].substring(3, 3+5);
		int hour = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3, 3+2));
		TimeZone timezone = TimeZone.getDefault();
		Calendar calendar = new GregorianCalendar(timezone);
		calendar.set(year, month - 1, day, hour, minutes);
		return calendar.getTimeInMillis();
	}
	
	public static String getMySQLDateTime(String mysqlDT, int offset) {
		//Fungerer ikke som den skal
		TimeZone timezone = TimeZone.getDefault();
		Calendar calendar = new GregorianCalendar(timezone);
		long oldTime = DateHelper.getDateLong(mysqlDT);
		long newTime = oldTime + offset*60*1000;
		calendar.setTimeInMillis(newTime);
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		String time = timeFormat.format(calendar.getTime());
		return time;
	}
	
	public static String getNorwegianFormat(String mysqlDT) {
		TimeZone timezone = TimeZone.getDefault();
		Calendar calendar = new GregorianCalendar(timezone);
		String [] date = mysqlDT.split("-");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2].substring(0, 2));
		String time = date[2].substring(3, 3+5);
		int hour = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3, 3+2));
		calendar.set(year, month - 1, day, hour, minutes);
		String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
		String weekday = DateHelper.getDayInNorwegian(dayName);
		String monthName = DateHelper.getMonthInNorwegian(month - 1);
		return weekday + " " + day + ". " + monthName + " klokken " + hour + ":" + minutes;
	}
	
	public static String getDayInNorwegian(int dayNo) {
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
		//System.out.println(dh.getMySQLDateTime());
		System.out.println(dh.getMySQLDateTime("2015-09-11 13:45:00", 0));
	}

}
