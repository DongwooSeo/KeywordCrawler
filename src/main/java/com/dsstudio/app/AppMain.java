package com.dsstudio.app;

import com.dsstudio.crawler.BasicKeywordCrawler;
import com.dsstudio.crawler.CrawlConfig;
import com.dsstudio.crawler.CrawlController;
import com.dsstudio.crawler.UpdateKeywordCrawler;

public class AppMain {
	public static void main(String[] args){
		CrawlConfig config = new CrawlConfig();

		CrawlController controller = new CrawlController(config);
		controller.startNonBlocking(BasicKeywordCrawler.class, 6);

	}
}
