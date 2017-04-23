package com.dsstudio.hibernate.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Stock {
	private int id;
	private int keywordId;
	private String name;
	private int code;
	private int pricePrev;
	private int price;
	private int priceMax;
	private int priceMin;
	private int fluct;
	private Double fluctRate;
	private String chartDaily;
	private String chartWeekly;
	private String chartMonthly;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getPricePrev() {
		return pricePrev;
	}
	public void setPricePrev(int pricePrev) {
		this.pricePrev = pricePrev;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(int priceMax) {
		this.priceMax = priceMax;
	}
	public int getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(int priceMin) {
		this.priceMin = priceMin;
	}
	public int getFluct() {
		return fluct;
	}
	public void setFluct(int fluct) {
		this.fluct = fluct;
	}
	public Double getFluctRate() {
		return fluctRate;
	}
	public void setFluctRate(Double fluctRate) {
		this.fluctRate = fluctRate;
	}
	public String getChartDaily() {
		return chartDaily;
	}
	public void setChartDaily(String chartDaily) {
		this.chartDaily = chartDaily;
	}
	public String getChartWeekly() {
		return chartWeekly;
	}
	public void setChartWeekly(String chartWeekly) {
		this.chartWeekly = chartWeekly;
	}
	public String getChartMonthly() {
		return chartMonthly;
	}
	public void setChartMonthly(String chartMonthly) {
		this.chartMonthly = chartMonthly;
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
