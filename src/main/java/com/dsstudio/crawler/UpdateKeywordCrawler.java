package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.parser.stock.NaverStockParser;
import com.dsstudio.parser.stock.StockParser;
import com.dsstudio.updater.KeywordUpdatable;
import com.dsstudio.updater.Updater;
import com.dsstudio.updater.StockKeywordUpdater;

public class UpdateKeywordCrawler extends KeywordCrawler {
	static final Logger logger = LoggerFactory.getLogger(UpdateKeywordCrawler.class);
	private List<KeywordUpdatable> keywordUpdaters = new ArrayList<KeywordUpdatable>();
	private List<StockParser> stockParsers = new ArrayList<StockParser>();

	// NaverKeywordParser, DaumKeywordParser 파서 추가 !
	public UpdateKeywordCrawler() {
		stockParsers.add(new NaverStockParser());

		keywordUpdaters.add(new StockKeywordUpdater(stockParsers));
	}

	public void run() {
		// TODO Auto-generated method stub
		// 파싱 시작!
		new Thread(new Updater(keywordUpdaters)).start();
	}
}
