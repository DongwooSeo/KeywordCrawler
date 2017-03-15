package com.dsstudio.hibernate.model;

public class RelatedKeywordLink {
	private int id;
	private int keywordId;
	private int relatedId;
	private int correl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(int keywordId) {
		this.keywordId = keywordId;
	}
	public int getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(int relatedId) {
		this.relatedId = relatedId;
	}
	public int getCorrel() {
		return correl;
	}
	public void setCorrel(int correl) {
		this.correl = correl;
	}
}
