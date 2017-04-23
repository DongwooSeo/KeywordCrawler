package com.dsstudio.app;

import com.dsstudio.crawler.BasicKeywordCrawler;
import com.dsstudio.crawler.CrawlConfig;
import com.dsstudio.crawler.CrawlController;
import com.dsstudio.crawler.UpdateKeywordCrawler;

public class AppMain {
	public static void main(String[] args){
		//Setting for each controller - crawler.
		CrawlConfig config = new CrawlConfig();
		
		CrawlController controller = new CrawlController(config);
		
		//Starting RealtimeKeywordCrawler by passing a class with two threads.
		//controller.startNonBlocking(RealtimeKeywordCrawler.class, 2);
		controller.startNonBlocking(BasicKeywordCrawler.class, 6);
		//controller.startNonBlocking(UpdateKeywordCrawler.class, 6);
	}
}
