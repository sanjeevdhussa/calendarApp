package com.cagataygurturk.example.model;

public class ProviderServiceType {
	public static final String PHYSIO = "physio";
	public static final String CHIRO = "chiro";
	public static final String GENERAL = "gen";
	public static final int DEFAULT_DURATION = 60;
	
	private String serviceType;
	private int serviceDuration;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public int getServiceDuration() {
		return serviceDuration;
	}
	public void setServiceDuration(int serviceDuration) {
		this.serviceDuration = serviceDuration;
	}
	
}
