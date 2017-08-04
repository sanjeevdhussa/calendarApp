package com.cagataygurturk.example.model;

import java.util.List;

public class ServiceProvider {
	private String provideId;
	private String providerName;
	private String calendarId;
	private int startHours;	
	private int startMin;
	private int endHours;
	private int endMin;
	private List<ProviderServiceType> serviceTypes;
	private String timeZone;
	private String locale;
	
	
	public String getProvideId() {
		return provideId;
	}
	public void setProvideId(String provideId) {
		this.provideId = provideId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public int getStartHours() {
		return startHours;
	}
	public void setStartHours(int startHours) {
		this.startHours = startHours;
	}
	public int getStartMin() {
		return startMin;
	}
	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}
	public int getEndHours() {
		return endHours;
	}
	public void setEndHours(int endHours) {
		this.endHours = endHours;
	}
	public int getEndMin() {
		return endMin;
	}
	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}
	public List<ProviderServiceType> getServiceTypes() {
		return serviceTypes;
	}
	public void setServiceTypes(List<ProviderServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
	public String getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}
	public int getServiceDuration(String serviceType){
		if(serviceType != null){
			for(ProviderServiceType serviceTypeLocal :serviceTypes){
				if(serviceType.equalsIgnoreCase(serviceTypeLocal.getServiceType())){
					return serviceTypeLocal.getServiceDuration();
				}
			}
		}
		return ProviderServiceType.DEFAULT_DURATION;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	
}
