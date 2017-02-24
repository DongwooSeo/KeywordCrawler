package com.dsstudio.app;

import com.dsstudio.crawler.BasicKeywordCrawler;
import com.dsstudio.crawler.CrawlConfig;
import com.dsstudio.crawler.CrawlController;
import com.dsstudio.crawler.RealtimeKeywordCrawler;

public class AppMain {
	public static void main(String[] args){
		//Setting for each controller - crawler.
		CrawlConfig config = new CrawlConfig();
		
		CrawlController controller = new CrawlController(config);
		
		//Starting RealtimeKeywordCrawler by passing a class with two threads.
		controller.startNonBlocking(RealtimeKeywordCrawler.class, 0);
		controller.startNonBlocking(BasicKeywordCrawler.class, 4);
	}
}
