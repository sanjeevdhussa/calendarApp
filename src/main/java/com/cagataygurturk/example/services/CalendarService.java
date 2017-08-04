package com.cagataygurturk.example.services;

import java.util.List;

import com.cagataygurturk.example.model.ApptBookingEvent;
import com.cagataygurturk.example.model.ServiceProvider;

public interface CalendarService {

	public List<ApptBookingEvent> findAvailableTimeSlots(ServiceProvider provider, String startDate, String endDate, String serviceType);
}
