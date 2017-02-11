package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.url.WebURL;

public class WebCrawler implements Runnable{
	protected static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);
	
	protected int myId;
	
	protected CrawlController myController;
	
	private Thread myThread;
	private Frontier frontier;
	
	private boolean isWaitingForNewURLs;
	
	public void init(int id, CrawlController crawlController)
		throws InstantiationException, IllegalAccessException{
		this.myId = id;
		this.myController = crawlController;
		this.frontier = crawlController.getFrontier();
		this.isWaitingForNewURLs = false;
	}
	
	public int getMyId(){
		return myId;
	}
	
	public CrawlController getMyController(){
		return myController;
	}
	
	public void onStart(){
		
	}
	
	public void onBeforeExit(){
		
	}
	public void run() {
		// TODO Auto-generated method stub
		onStart();
		while(true){
			List<WebURL> assignedURLs = new ArrayList<WebURL>(50);
			isWaitingForNewURLs = true;
			frontier.getNextURLs(50, assignedURLs);
			isWaitingForNewURLs = false;
			if(assignedURLs = false){
				if(frontier.isFinished()){
					return;
				}
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e){
					logger.error("Error occurred", e);
				}
			}else{
				for(WebURL curURL : assignedURLs){
					if(myController.isShuttingDown()){
						logger.info("Exiting because of controller shutdown.");
						return;
					}
					
					if(curURL != null){
						curURL = handleUrlBeforeProcess(curURL);
						processPage(curURL);
						frontier.setProcesssed(curURL);
					}
				}
			}
		}
	}
	
	public Thread getThread(){
		return myThread;
	}
	
	public void setThread(Thread myThread){
		this.myThread = myThread;
	}
	
	public boolean isNotWaitingForNewURLs(){
		return !isWaitingForNewURLs;
	}
	
	

}
