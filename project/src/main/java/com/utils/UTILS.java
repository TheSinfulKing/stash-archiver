package com.utils;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UTILS {
    public static String getFormattedTime(long timeInMillis) {
		String ret = "";
		if(timeInMillis < 1000) {
			return timeInMillis + " ms";
		}
		else {
			long timeInSeconds = timeInMillis / 1000;
			timeInMillis = timeInMillis % 1000;
			if(timeInSeconds > 60) {
				long timeInMinutes = timeInSeconds / 60;
				timeInSeconds = timeInSeconds % 60;
				if(timeInMinutes > 60) {
					long timeInHours = timeInMinutes / 60;
					timeInMinutes = timeInMinutes % 60;
					ret += timeInHours + " hr ";
				}
				ret += timeInMinutes + " m ";
			}
			ret += timeInSeconds + " s ";
			// ret += timeInMillis + "ms";
			return ret;
		}
	}

    public static LocalDate ConvertToDate(String dateString) {
		if(dateString == null || dateString.isBlank()) {
			return null;
		}
		if(dateString.length() < 10) {
			System.out.println("ERROR: This string is incorrect for YYYY-MM-DD date format! - " + dateString);
			return null;
		}
		// "YYYY-MM-DD"
		int year = Integer.parseInt(dateString.substring(0,4));
		int month = Integer.parseInt(dateString.substring(5,7));
		int day = Integer.parseInt(dateString.substring(8));
		LocalDate date = LocalDate.of(year, month, day);
		return date;
	}

    public static LocalDateTime ConvertToDateTime(String dateTimeString) {
		if(dateTimeString == null || dateTimeString.isBlank()) {
			return null;
		}
		if(dateTimeString.length() < 24) {
			System.out.println("ERROR: This string is incorrect for YYYY-MM-DD date format! - " + dateTimeString);
			return null;
		}
		//           1         2
		// 0123456789012345678901234
		// 2021-10-23T11:59:41-07:00
		// YYYY-MM-DDTHH:MM:SS-01:00
		// The -01:00 after SS is the timezone
		int year = Integer.parseInt(dateTimeString.substring(0,4));
		int month = Integer.parseInt(dateTimeString.substring(5,7));
		int day = Integer.parseInt(dateTimeString.substring(8, 10));

		int hour = Integer.parseInt(dateTimeString.substring(11, 13));
		int minute = Integer.parseInt(dateTimeString.substring(14, 16));
		int second = Integer.parseInt(dateTimeString.substring(17, 19));

		LocalDate date = LocalDate.of(year, month, day);
		LocalTime time = LocalTime.of(hour, minute, second);
		LocalDateTime datetime = LocalDateTime.of(date, time);
		return datetime;
	}

	public static void PRINTCRITICALERROR(String message) {
		// Critical errors terminate the program
		System.err.println("[CRITICAL]: " + message);
		System.exit(-1);
	}

	public static String GETEXTENSION(String filename) {
		int index = filename.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		String ext = filename.substring(index + 1);
		return ext;
	}

	public static String ESCAPEPATH(String path) {
		String currentDir = System.getProperty("user.dir");
		if(path.contains("..\\")) {
			File f = new File(currentDir);
			f = f.getParentFile();
			path = path.replace("..", f.getAbsolutePath());
		}
		else if(path.contains(".\\")) {
			path = path.replace(".", currentDir);
		}
		return path;
	}
}
