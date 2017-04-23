package com.dsstudio.parser.stock;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.dsstudio.helper.DataCommon;
import com.dsstudio.helper.ParseCommon;
import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.ParserDao;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.StockDao;
import com.dsstudio.hibernate.dao.StockDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.hibernate.model.Stock;

public class NaverStockParser implements StockParser {

	private StockDao stockDao = new StockDaoImpl();

	public void parseStock(Document document, int stockKeywordId, Agent agent, List<Parser> parsers) {
		AgentConfig agentConfig = agent.getAgentConfig();
		String content = document.toString();
		try {
			String stockName = ParseCommon.getTarget(content, DataCommon.getValueBy("StockNameStarted", parsers),
					DataCommon.getValueBy("StockNameEnded", parsers));
			String stockCode = ParseCommon.getTarget(content, DataCommon.getValueBy("StockCodeStarted", parsers),
					DataCommon.getValueBy("StockCodeEnded", parsers));
			String url = String.format(DataCommon.getValueBy("StockUrlFormat", parsers), stockCode);

			Document stockInfo = Jsoup.connect(url).timeout(agentConfig.getTimeout() * 1000)
					.userAgent(agentConfig.getUserAgent()).get();

			int stockPrice = Integer.parseInt(
					ParseCommon.getTarget(stockInfo.toString(), DataCommon.getValueBy("StockPriceStarted", parsers),
							DataCommon.getValueBy("StockPriceEnded", parsers)));
			int stockPricePrev = Integer.parseInt(
					ParseCommon.getTarget(stockInfo.toString(), DataCommon.getValueBy("StockPricePrevStarted", parsers),
							DataCommon.getValueBy("StockPricePrevEnded", parsers)));
			int stockPriceMax = Integer.parseInt(
					ParseCommon.getTarget(stockInfo.toString(), DataCommon.getValueBy("StockPriceMaxStarted", parsers),
							DataCommon.getValueBy("StockPriceMaxEnded", parsers)));
			int stockPriceMin = Integer.parseInt(
					ParseCommon.getTarget(stockInfo.toString(), DataCommon.getValueBy("StockPriceMinStarted", parsers),
							DataCommon.getValueBy("StockPriceMinEnded", parsers)));
			int stockPriceFluct = Integer.parseInt(ParseCommon.getTarget(stockInfo.toString(),
					DataCommon.getValueBy("StockPriceFluctStarted", parsers),
					DataCommon.getValueBy("StockPriceFluctEnded", parsers)));
			double stockPriceFluctRate = Double.parseDouble(ParseCommon.getTarget(stockInfo.toString(),
					DataCommon.getValueBy("StockPriceFluctRateStarted", parsers),
					DataCommon.getValueBy("StockPriceFluctRateEnded", parsers)));

			String chartDailyUrl = String.format(DataCommon.getValueBy("StockChartDailyFormat", parsers), stockCode);
			String chartWeeklyUrl = String.format(DataCommon.getValueBy("StockChartWeeklyFormat", parsers), stockCode);
			String chartMonthlyUrl = String.format(DataCommon.getValueBy("StockChartMonthlyFormat", parsers),
					stockCode);

			System.out.println(stockName + " | " + stockCode + " | " + stockPrice + " | " + stockPricePrev + " | "
					+ stockPriceMax + " | " + stockPriceMin + " | " + stockPriceFluct + " | " + stockPriceFluctRate);
			saveStock(stockKeywordId, stockName, stockCode, stockPrice, stockPricePrev, stockPriceMax, stockPriceMin, stockPriceFluct,
					stockPriceFluctRate, chartDailyUrl, chartWeeklyUrl, chartMonthlyUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e){
			//e.printStackTrace();
			//System.out.println("거래정지 종목.");
		} 

	}

	private void saveStock(int keywordId, String name, String code, int price, int pricePrev, int priceMax, int priceMin,
			int priceFluct, double priceFluctRate, String chartDailyUrl, String chartWeeklyUrl,
			String chartMonthlyUrl) {
		stockDao.upsertStock(keywordId, name, code, price, pricePrev, priceMax, priceMin, priceFluct, priceFluctRate, chartDailyUrl, chartWeeklyUrl, chartMonthlyUrl);
	}

}
