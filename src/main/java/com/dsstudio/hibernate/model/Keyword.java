package com.dsstudio.hibernate.model;

import java.sql.Timestamp;

public class Keyword {
	private int id;
	private int keywordMainId;
	private int agentId;
	private String name;
	private String link;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKeywordMainId() {
		return keywordMainId;
	}
	public void setKeywordMainId(int keywordMainId) {
		this.keywordMainId = keywordMainId;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	
}
