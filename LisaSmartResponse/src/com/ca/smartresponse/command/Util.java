package com.ca.smartresponse.command;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	public static String currentDate(String format) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
}
