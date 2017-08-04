package com.cagataygurturk.example.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static Date nextDay(Date day){
		if(day != null){
			 Calendar c1 = Calendar.getInstance(); 
		     c1.setTime(day); 
		     c1.add(Calendar.DATE, 1);
		     return c1.getTime();
		}
		return null;
	}

}
