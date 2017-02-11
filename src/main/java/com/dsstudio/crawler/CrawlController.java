package com.dsstudio.crawler;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CrawlController extends Configurable{
	static final Logger logger = LoggerFactory.getLogger(CrawlController.class);
	
	protected boolean finished;
	protected boolean shuttingDown;
	
	//protected Frontier frontier;
	protected final Object waitingLock = new Object();
	
	
	public CrawlController(CrawlConfig config){
		super(config);
		finished = false;
		shuttingDown = false;
		//frontier = new Frontier(config);
	}
	
	public interface KeywordCrawlerFactory<T extends KeywordCrawler>{
		T newInstance() throws Exception;
	}
	
	private static class DefaultKeywordCrawlerFactory<T extends KeywordCrawler> implements KeywordCrawlerFactory<T>{
		final Class<T> clazz;
		
		public DefaultKeywordCrawlerFactory(Class<T> clazz) {
			// TODO Auto-generated constructor stub
			this.clazz = clazz;
		}
		public T newInstance() throws Exception {
			// TODO Auto-generated method stub
			return clazz.newInstance();
		}	
	}
	
	public <T extends KeywordCrawler> void startNonBlocking(Class<T> clazz, int numberOfCrawlers){
		start(new DefaultKeywordCrawlerFactory<T>(clazz), numberOfCrawlers, false);
	}
	
	
	protected<T extends KeywordCrawler>void start(final KeywordCrawlerFactory<T> crawlerFactory, final int numberOfCrawlers, boolean isBlocking){
		try{
			finished = false;
			final List<Thread> threads = new ArrayList<Thread>();
			final List<T> crawlers = new ArrayList<T>();
			
			for(int i=1; i<=numberOfCrawlers; i++){
				T crawler = crawlerFactory.newInstance();
				Thread thread = new Thread(crawler, "Crawler "+i);
				crawler.setThread(thread);
				crawler.init(i, this);
				thread.start();
				crawlers.add(crawler);
				threads.add(thread);
				logger.info("Crawler {} started", i);	
				
			}
			
			final CrawlController controller = this;
			final CrawlConfig config = this.getConfig();
			
			
			if(isBlocking){
				waitUntilFinish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void waitUntilFinish() {
		// TODO Auto-generated method stub
		while(!finished){
			synchronized(waitingLock){
				if(finished){
					return;
				}
				try{
					waitingLock.wait();
				}catch(InterruptedException e){
					logger.error("Error occurred", e);
				}
			}
		}
	}
	
	protected static void sleep(int seconds){
		try{
			Thread.sleep(seconds * 1000);
		}catch(InterruptedException ignored){
			//Do nothing
		}
	}
}
