package com.dsstudio.hibernate.model;

import java.sql.Timestamp;

public class Agent {
	private int id;
	private String name;
	private String nameEng;
	private int	isUsed;
	private int hours;
	private int status;
	private Timestamp dateFinished;
	
	
	private AgentConfig agentConfig;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEng() {
		return nameEng;
	}
	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}
	public int getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getDateFinished() {
		return dateFinished;
	}
	public void setDateFinished(Timestamp dateFinished) {
		this.dateFinished = dateFinished;
	}
	public AgentConfig getAgentConfig() {
		return agentConfig;
	}
	public void setAgentConfig(AgentConfig agentConfig) {
		this.agentConfig = agentConfig;
	}
	
	
}
