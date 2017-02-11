package com.dsstudio.frontier;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.crawler.Configurable;
import com.dsstudio.crawler.CrawlConfig;
import com.dsstudio.url.WebURL;

public class Frontier extends Configurable {
	protected static final Logger logger = LoggerFactory.getLogger(Frontier.class);
	
	private static final String DATABASE_NAME = "PendingURLsDB";
	private static final int IN_PROCESS_RESCHEDULE_BATCH_SIZE = 100;
	protected WorkQueues workQueues;
	
	protected InProcessPageDB inProcessPages;
	
	protected final Object mutex = new Object();
	protected final Object waitingList = new Object();
	
	protected boolean isFinished = false;
	
	protected long scheduledPages;
	
	protected Counters counters;
	
	protected Frontier(CrawlConfig config) {
		super(config);
		this.counters = new Counters(config);
		workQueues = new WorkQueues<PendingURL>(config.isResumableCrawling());
		// TODO Auto-generated constructor stub
	}

	public void scheduleAll(List<WebURL> urls){
		int maxPagesToFetch = config.getMaxPagesToFetch();
		synchronized(mutex){
			int newScheduledPage = 0;
			for(WebURL url : urls){
				if((maxPagesToFetch > 0) && 
					((scheduledPages + newScheduledPage) >= maxPagesToFetch)){
					break;
				} 
				
				workQueues.put(url);
				newScheduledPage++;
			}
			
			if(newScheduledPage > 0){
				scheduledPages += newScheduledPage;
				counters.increment(Counters.ReservedCounterNames.SCHEDULED_PAGES, newScheduledPage);
			}
			synchronized(waitingList){
				waitingList.notifyAll();
			}
		}
	}
	public void schedule(WebURL url){
		int maxPagesToFetch = config.getMaxPagesToFetch();
		synchronized(mutex){
			if(maxPagesToFetch < 0 || scheduledPages < maxPagesToFetch){
				workQueues.put(url);
				scheduledPages++;
				counters.increment(Counters.ReservedCounterNames.SCHEDULED_PAGES);
			}
		}
	}
	public void getNextURLs(int max, List<WebURL> result){
		while(true){
			synchronized(mutex){
				if(isFinished){
					return;
				}
				
				List<WebURL> curResults = workQueues.get(max);
				workQueues.delete(curResults.size());
				if(inProcessPages != null){
					for(WebURL curPage : curResults){
						inProcessPages.put(curPage);
					}
				}
				result.addAll(curResults);
				
				if(result.size() > 0){
					return;
				}
			}
			
			synchronized(waitingList){
				waitingList.wait();
			}
			
			if(isFinished){
				return;
			}
		}
	}
	
	public void setProcessed(WebURL webURL){
		counters.increment(Counters.ReservedCounterName.PROCESSED_PAGES);
		if(inProcessPages != null){
			if(!inProcessPages.removeURL(webURL)){
				logger.warn("Could not remove: {} from list of processed pages.", webURL.getUrl());
			}
		}
	}
	
	public long getQueueLength(){
		return workQueues.getLength();
	}
	
	public long getNumberOfAssignedPages(){
		return inProcessPages.getLength();
	}
	
	public long getNumberOfProcessedPages(){
		return counters.getValue(Counters.ReservedCounterNames.PROCESSED_PAGES);
	}
	
	public long getNumberOfScheduledPages(){
		return counters.getValue(Counters.ReservedCounterNames.SCHEDULED_PAGES);
	}
	
	public boolean isFinished(){
		return isFinished;
	}
	
	public void close(){
		workQueues.close();
		counters.close();
		if(inProcessPages != null){
			inProcessPages.close();
		}
	}
	
	public void finish(){
		isFinished = true;
		synchronized(waitingList){
			waitingList.notifyAll();
		}
	}
}
