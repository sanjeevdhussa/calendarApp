package com.cagataygurturk.example.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cagataygurturk.example.model.ApptBookingEvent;
import com.cagataygurturk.example.model.ServiceProvider;

@Component
public class Service {

    /**
     * Autowiring another Spring Bean
     */
    @Autowired
    AnotherService anotherService;
    
    @Autowired
	GoogleCalendarService gogleCalendarService;
	
	@Autowired
	ServiceProvidersService serviceProviderService;
	
	public static final String CALANDER_TYPE_GOOGLE = "google";
//	TimeZone timeZone = TimeZone.getTimeZone("Canada/Eastern");
//	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
	
//	@PostConstruct
//	public void init(){
//		df.setTimeZone(timeZone);
//	}

    public List<ApptBookingEvent> getBookings(String startDate,String endDate, String serviceType, String providerId) {
    	
//    	String startDate = "05/01/2017";
//    	String endDate = null;
//    	String serviceType="physio";
//    	String providerId="1";
    			
    	ServiceProvider provider = serviceProviderService.getProviderById(providerId);
//		Date startDt = null;;
//		if(startDate != null){
//			try {
//				startDt = df.parse(startDate);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else{
//			startDt = new Date();
//		}
//		
//		Date endDt = null;;
//		if(endDate != null){
//			try {
//				endDt = df.parse(endDate);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else{
//			endDt = startDt;
//		}
		return gogleCalendarService.findAvailableTimeSlots(provider, startDate, endDate, serviceType);
		
		
        //return anotherService.getText(text);
    }
}
