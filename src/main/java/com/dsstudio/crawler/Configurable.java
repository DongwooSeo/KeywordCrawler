package com.dsstudio.crawler;

public abstract class Configurable {
	protected CrawlConfig config;
	
	protected Configurable(CrawlConfig config){
		this.config = config;
	}
	
	public CrawlConfig getConfig(){
		return config;
	}
}
