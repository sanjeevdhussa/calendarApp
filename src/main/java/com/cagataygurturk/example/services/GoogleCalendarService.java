package com.cagataygurturk.example.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.cagataygurturk.example.model.ApptBookingEvent;
import com.cagataygurturk.example.model.CalendarLocale;
import com.cagataygurturk.example.model.ServiceProvider;
import com.cagataygurturk.example.util.DateUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar.Freebusy;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

@Service
public class GoogleCalendarService implements CalendarService{
	
	private static final String AWS_HOME = "/tmp";
	/**
	   * Be sure to specify the name of your application. If the application name is {@code null} or
	   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	   */
	  private static final String APPLICATION_NAME = "";

	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(AWS_HOME);

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;
	  
	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	  private static com.google.api.services.calendar.Calendar client;

	  static final java.util.List<Calendar> addedCalendarsUsingBatch = Lists.newArrayList();
	  
	  
	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize() throws Exception {
	    // load client secrets
		  Resource resource = new ClassPathResource("client_secrets.json");
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(resource.getInputStream()));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
	          + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
	        .build();
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().build()).authorize("user");
	  }
	  
	@Override
	public List<ApptBookingEvent> findAvailableTimeSlots(ServiceProvider provider, 
			String startDt, String endDt, String serviceType) {
		TimeZone timeZone = TimeZone.getTimeZone(provider.getTimeZone());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", getLocale(provider));
		df.setTimeZone(timeZone);
		Date startDate = null;;
		if(startDt != null){
			try {
				startDate = df.parse(startDt + " 00:00:00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			startDate = new Date();
		}
		Date endDate = null;;
		if(endDt != null){
			try {
				endDate = df.parse(endDt + " 00:00:00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			endDate = startDate;
		}
		
		try {
		      // initialize the transport
		      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		      
		      copyFile(AWS_HOME, "StoredCredential");

		      // initialize the data store factory
		      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

		      // authorization
		      Credential credential = authorize();

		      // set up global Calendar instance
		      client = new com.google.api.services.calendar.Calendar.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

		      // run commands
//		      showCalendars();
//		      addCalendarsUsingBatch();
//		      Calendar calendar = addCalendar();
//		      updateCalendar(calendar);
//		      addEvent(calendar);
//		      showEvents(calendarId);
//		      deleteCalendarsUsingBatch();
//		      deleteCalendar(calendar);
		      List<ApptBookingEvent> bookedCalEntries = getBookedEvents(provider, startDate, endDate);
		    		  //getBusyTimes(provider.getCalendarId(),startDate,endDate);
		      List<ApptBookingEvent> availableBooking = new ArrayList<ApptBookingEvent>();
		      if(endDate == null){
		    	  endDate = startDate;
		      }
		      while(startDate.before(DateUtil.nextDay(endDate))){
			      //start time of the start date
			      Calendar calStartTimeOfTheDay = Calendar.getInstance(timeZone);
			      calStartTimeOfTheDay.setTime(startDate);
			      calStartTimeOfTheDay.set(Calendar.HOUR_OF_DAY, provider.getStartHours());
			      calStartTimeOfTheDay.set(Calendar.MINUTE, provider.getStartMin());
			      
			      //end time of the start date
			      Calendar calEndTimeOfTheDay = Calendar.getInstance(timeZone);
				  calEndTimeOfTheDay.setTime(startDate);
			      calEndTimeOfTheDay.set(Calendar.HOUR_OF_DAY, provider.getEndHours());
			      calEndTimeOfTheDay.set(Calendar.MINUTE, provider.getEndMin());
			      
			      
			      System.out.println("Start = " +df.format(calStartTimeOfTheDay.getTime()));
			      System.out.println("End = " +df.format(calEndTimeOfTheDay.getTime()));
			      
			      while(calStartTimeOfTheDay.getTime().getTime() < calEndTimeOfTheDay.getTime().getTime()){
				      boolean slotBooked = false;
				      for(ApptBookingEvent bookedCalSlot : bookedCalEntries){
				    	  Date bookedCalSlotDate = df.parse(bookedCalSlot.getStartTime());
				    	  if (calStartTimeOfTheDay.getTime().getTime() == bookedCalSlotDate.getTime()){
				    		  slotBooked = true;
				    		  calStartTimeOfTheDay.add(Calendar.MINUTE, provider.getServiceDuration(serviceType));
				    		  System.out.println("bookedCalSlot ="+bookedCalSlot);
				    		  break;
				    	  }
				      }
				      if(!slotBooked){
				    	  ApptBookingEvent availableSlot = new ApptBookingEvent();
				    	  availableSlot.setStartTime(df.format(calStartTimeOfTheDay.getTime()));
				    	  calStartTimeOfTheDay.add(Calendar.MINUTE, provider.getServiceDuration(serviceType));
				    	  availableSlot.setEndTime(df.format(calStartTimeOfTheDay.getTime()));
				    	  availableBooking.add(availableSlot);
				    	  //System.out.println(availableSlot);
				      }
			      }
			      startDate = DateUtil.nextDay(startDate);
		      }
		      //System.out.println(availableBooking);
		      return availableBooking;
		      
		      
		    } catch (IOException e) {
		      System.err.println(e.getMessage());
		    } catch (Throwable t) {
		      t.printStackTrace();
		    }
		
		return null;
	}
	
//	private void showCalendars() throws IOException {
//	    View.header("Show Calendars");
//	    CalendarList feed = client.calendarList().list().execute();
//	    View.display(feed);
//	  }
//	
//	private  void showEvents(String calendarId) throws IOException {
//	    View.header("Show Events");
//	    Events feed = client.events().list(calendarId).execute();
//	    View.display(feed);
//	  }
	
	public List<ApptBookingEvent> getBookedEvents(ServiceProvider provider, Date startDate, Date endDate){
		TimeZone timeZone = TimeZone.getTimeZone(provider.getTimeZone());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", getLocale(provider));
		df.setTimeZone(timeZone);
		List<ApptBookingEvent> bookedCalEntries = new ArrayList<ApptBookingEvent>();
		try {
			DateTime sdt = new DateTime(startDate);
			if(endDate == null){
				endDate = startDate;
			}
			//set end date with 23 hrs 59 minutes and 59 seconds to cover full day
			Calendar c = Calendar.getInstance(timeZone);
			c.setTime(endDate);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			endDate = c.getTime();
			
			DateTime edt = new DateTime(endDate);
			Events calEvents = client.events().list(provider.getCalendarId()).setTimeMax(edt).setTimeMin(sdt).execute();
			if (calEvents.getItems() != null) {
			      for (Event event : calEvents.getItems()) {
			    	  ApptBookingEvent apptBookingEvent = new ApptBookingEvent();
			    	  if (event.getStart() != null) {
			    		  DateTime timeStart = event.getStart().getDateTime();
			    		  Date dateStart = new Date(timeStart.getValue());
					      //System.out.println("Start Time: " + df.format(dateStart1) );
					      apptBookingEvent.setStartTime(df.format(dateStart) );
					    }
					    if (event.getEnd() != null) {
					    	DateTime timeEnd = event.getEnd().getDateTime();
				    		  Date dateEnd = new Date(timeEnd.getValue());
				    		  apptBookingEvent.setEndTime(df.format(dateEnd));
						      //System.out.println("End Time: " + df.format(dateStart2));
					    }
					    bookedCalEntries.add(apptBookingEvent);
			      }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookedCalEntries;
			
	}
	

	private void copyFile(String dest, String filename) 
                                  throws IOException {

		Path inputPath = Paths.get(this.getClass().getClassLoader().
		                  getResource(filename).getPath());
			Path path = Paths.get(dest + File.separator + filename);
			try {
				Files.copy(inputPath, path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * Get busy times from the Calendar API.
	 * 
	 * @param attendees
	 *            Attendees to retrieve busy times for.
	 * @param startDate
	 *            Start date to retrieve busy times from.
	 * @param timeSpan
	 *            Number of days to retrieve busy times for.
	 * @return Busy times for the selected attendees.
	 * @throws IOException
	 */
	public List<ApptBookingEvent> getBusyTimes(ServiceProvider provider, Date startTime, Date endTime) throws IOException {
		TimeZone timeZone = TimeZone.getTimeZone(provider.getTimeZone());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", getLocale(provider));
		df.setTimeZone(timeZone);
			if(endTime == null){
				endTime = startTime;
			}
			//set end date with 23 hrs 59 minutes and 59 seconds to cover full day
			Calendar c = Calendar.getInstance(timeZone);
			c.setTime(endTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			endTime = c.getTime();
			List<TimePeriod> result = new ArrayList<TimePeriod>();
			List<FreeBusyRequestItem> requestItems = new ArrayList<FreeBusyRequestItem>();
			
			 System.out.println( "Time Zone "+ TimeZone.getDefault().getDisplayName() ); 
		      
			 // create time zone object 
//		      TimeZone tzone = TimeZone.getTimeZone("Canada/Eastern");
		         
		      // set time zone to default
//		      TimeZone.setDefault(tzone);

		      // checking default time zone
//		      System.out.println( "Time Zone "+ TimeZone.getDefault().getDisplayName() );
			      
			      
			FreeBusyRequest request = new FreeBusyRequest();
					request.setTimeMin(getDateTime(startTime));
					request.setTimeMax(getDateTime(endTime));
					//request.setTimeZone(timeZone);
					requestItems.add(new FreeBusyRequestItem().setId(provider.getCalendarId()));
					request.setItems(requestItems);
			FreeBusyResponse busyTimes;
			try {
				Freebusy.Query query = client.freebusy().query(request);
				// Use partial GET to only retrieve needed fields.
				query.setFields("calendars");
				busyTimes = query.execute();
				for (Map.Entry<String, FreeBusyCalendar> busyCalendar : busyTimes
									.getCalendars().entrySet()) {
								result = busyCalendar.getValue().getBusy();
				}
			} catch (IOException e) {
				System.out.println("Exception occured while retrieving busy times: "+ e.toString());
				if (e instanceof HttpResponseException) {
					throw e;
				}
			}
			return convertToTimeSlot(result, df);
		}
		
	


	private List<ApptBookingEvent> convertToTimeSlot(List<TimePeriod> timePeriods, DateFormat df) {
		List<ApptBookingEvent> timeSlots = new ArrayList<ApptBookingEvent>();
		for (TimePeriod tp : timePeriods) {
			Date start = new Date(tp.getStart().getValue());
			Date end = new Date(tp.getEnd().getValue());
			ApptBookingEvent timeSlot = new ApptBookingEvent();
			timeSlot.setStartTime(df.format(start));
			timeSlot.setEndTime(df.format(end));
						timeSlots.add(timeSlot);
		}
		System.out.println("busy slots :"+timeSlots);
		return timeSlots;
	}
	
	private DateTime getDateTime(Date date) {
		java.util.Calendar cal = new GregorianCalendar();
				cal.setTime(date);
		return new DateTime(cal.getTime().getTime(), 0);
	}
	
	private Locale getLocale(ServiceProvider provider){
		if(CalendarLocale.canada.name().equals(provider.getLocale())){
			return Locale.CANADA;
		}else if(CalendarLocale.us.name().equals(provider.getLocale())){
			return Locale.US;
		}
		return Locale.CANADA;
	}

}