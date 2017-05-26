package com.dsstudio.updater;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.ParserDao;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.StockKeywordDao;
import com.dsstudio.hibernate.dao.StockKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.hibernate.model.StockKeyword;
import com.dsstudio.parsers.stock.StockParser;

public class StockKeywordUpdater implements KeywordUpdatable {
	private static StockKeywordDao stockKeywordDao = new StockKeywordDaoImpl();
	private ParserDao parserDao = new ParserDaoImpl();
	private AgentDao agentDao = new AgentDaoImpl();
	private List<StockParser> stockParsers;

	public StockKeywordUpdater(List<StockParser> stockParsers) {
		// TODO Auto-generated constructor stub
		this.stockParsers = stockParsers;
	}

	private static synchronized StockKeyword updatableKeyword() {
		StockKeyword stockKeyword = stockKeywordDao.fetchFirstRow();
		return stockKeyword;
	}

	public void updateKeyword() {
		// TODO Auto-generated method stub
		StockKeyword stockKeyword = updatableKeyword();
		int agentId = stockKeyword.getAgentId();
		List<Parser> parsers = parserDao.findByAgentId(agentId);
		Agent agent = agentDao.findById(agentId);
		AgentConfig agentConfig = agent.getAgentConfig();

		if (stockKeyword != null) {
			try {
				Connection connection = Jsoup.connect(stockKeyword.getLink()).timeout(agentConfig.getTimeout() * 1000)
						.userAgent(agentConfig.getUserAgent());
				Document document = connection.get();
				for (StockParser stockParser : stockParsers) {
					stockParser.parseStock(document, stockKeyword.getId(), agent, parsers);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stockKeyword.setStatus(3);
		stockKeywordDao.update(stockKeyword);
	}
}
