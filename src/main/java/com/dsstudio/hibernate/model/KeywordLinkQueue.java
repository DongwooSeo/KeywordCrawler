package com.dsstudio.hibernate.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class KeywordLinkQueue implements Serializable {
	

	private int id;
	private String link;
	private String batchId;
	private int status;
	private Timestamp dateCreated;
	private String booking;
	private Timestamp bookingDate;
	private int agentId;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getBooking() {
		return booking;
	}
	public void setBooking(String booking) {
		this.booking = booking;
	}
	public Timestamp getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Timestamp bookingDate) {
		this.bookingDate = bookingDate;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	
	
}
