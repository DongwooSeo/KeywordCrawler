package com.dsstudio.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeywordCrawler implements Runnable {

	protected static final Logger logger = LoggerFactory.getLogger(KeywordCrawler.class);

	protected int id;

	protected CrawlController crawlController;

	private Thread thread;
	private boolean isWaitingForNewURLs;

	public void init(int id, CrawlController crawlController) throws InstantiationException, IllegalAccessException {
		this.id = id;
		this.crawlController = crawlController;
		this.isWaitingForNewURLs = false;
	}

	public int getId() {
		return id;
	}

	public CrawlController getCrawlController() {
		return crawlController;
	}

	public void onStart() {

	}

	public void onBeforeExit() {

	}

	public void run() {
		// TODO Auto-generated method stub
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public boolean isWaitingForNewURLs() {
		return isWaitingForNewURLs;
	}

}
