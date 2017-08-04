package com.cagataygurturk.example.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApptBookingEvent {
	//@JsonFormat(pattern="MM/dd/yyyy HH:mm:ss", timezone="Canada/Eastern")
	private String startTime;
	//@JsonFormat(pattern="MM/dd/yyyy HH:mm:ss", timezone="Canada/Eastern")
	private String endTime;
	private String note;
	private String serviceType;
	private boolean isAntiMeridian;
	//private String providerId;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
//	public String getProviderId() {
//		return providerId;
//	}
//	public void setProviderId(String providerId) {
//		this.providerId = providerId;
//	}
	public boolean isAntiMeridian() {
		return isAntiMeridian;
	}
	public void setAntiMeridian(boolean isAntiMeridian) {
		this.isAntiMeridian = isAntiMeridian;
	}
	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return "ApptBookingEvent [startTime=" + startTime + ", endTime=" + endTime + ", note=" + note + ", serviceType="
				+ serviceType + ", isAntiMeridian=" + isAntiMeridian + "]";
	}

}
