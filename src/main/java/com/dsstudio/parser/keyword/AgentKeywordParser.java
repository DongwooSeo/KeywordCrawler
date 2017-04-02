package com.dsstudio.parser.keyword;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dsstudio.helper.DataCommon;
import com.dsstudio.helper.ParseCommon;
import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordDao;
import com.dsstudio.hibernate.dao.KeywordDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.dao.KeywordMainDao;
import com.dsstudio.hibernate.dao.KeywordMainDaoImpl;
import com.dsstudio.hibernate.dao.ParserDao;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RealtimeKeywordDao;
import com.dsstudio.hibernate.dao.RealtimeKeywordDaoImpl;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDao;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDaoImpl;
import com.dsstudio.hibernate.dao.StockKeywordDao;
import com.dsstudio.hibernate.dao.StockKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.parser.stock.StockParser;

public class AgentKeywordParser extends CommonKeywordParser {
	protected int agentId;
	protected String agentName;

	protected AgentDao agentDao = new AgentDaoImpl();
	protected ParserDao parseDao = new ParserDaoImpl();
	protected KeywordDao keywordDao = new KeywordDaoImpl();
	protected StockKeywordDao stockKeywordDao = new StockKeywordDaoImpl();
	protected KeywordMainDao keywordMainDao = new KeywordMainDaoImpl();
	protected KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
	protected RelatedKeywordLinkDao relatedKeywordLinkDao = new RelatedKeywordLinkDaoImpl();
	protected RealtimeKeywordDao realtimeKeywordDao = new RealtimeKeywordDaoImpl();

	protected void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	protected int getAgentId() {
		return agentId;
	}

	/*
	 * (LinkQueue에 작업을 추가합니다.)
	 * 
	 * @see
	 * com.dsstudio.parser.keyword.CommonKeywordParser#saveKeywordLinkQueue(java
	 * .lang.String, int)
	 */
	protected int saveKeywordLinkQueue(String link, int agentId, int parentId) {
		keywordLinkQueueDao.saveIfNotExist(link, agentId, parentId);
		return 0;
	}

	@Override
	protected void parseRealtimeKeyword() {
		// TODO Auto-generated method stub
	}

	/*
	 * (네이버 및 다음 agent 가 공통적으로 사용하는 메서드로, 키워드 검색 후 나오는 페이지를 파싱합니다. +연관검색어 포함)
	 * (상세페이지의 DOM객체는 DB, Parser 테이블에 agent별로 각각 저장됩니다.)
	 * 
	 * @see
	 * com.dsstudio.parser.keyword.CommonKeywordParser#parseKeyword(com.dsstudio
	 * .hibernate.model.KeywordLinkQueue)
	 */
	@Override
	protected void parseKeyword(KeywordLinkQueue keywordLinkQueue) {
		// TODO Auto-generated method stub
		parseStockKeyword(keywordLinkQueue);

	}
	
	private void parseStockKeyword(KeywordLinkQueue keywordLinkQueue) {
		// TODO Auto-generated method stub
		Agent agent = agentDao.findById(this.agentId);
		AgentConfig agentConfig = agent.getAgentConfig();

		List<Parser> parsers = parseDao.findByAgentId(agent.getId());
		int stockKeywordId = 0;

		try {
			System.out.println(agent.getName() + " 키워드 파싱 시작!!");
			// System.out.println(keywordLinkQueue.getLink());
			Connection connection = Jsoup.connect(keywordLinkQueue.getLink()).timeout(agentConfig.getTimeout() * 1000)
					.userAgent(agentConfig.getUserAgent());
			Document document = connection.get();

			// System.out.println(document.toString().("sItemCode : "));
			String keywordName = document.select(DataCommon.getValueBy("KeywordField", parsers))
					.attr(DataCommon.getValueBy("KeywordFieldAttr", parsers)).trim().replaceAll("주가", "")
					.replaceAll("주식", "");

			// Keyword Validation, TypeCheck
			// If keyword is related to Stock.
			boolean stockKeywordCheck = document.toString()
					.contains(DataCommon.getValueBy("StockKeywordCheck", parsers));
			boolean stockKeywordTypeCheck = DataCommon
					.containValue(DataCommon.getValueBy("StockKeywordTypeCheck", parsers), keywordName);

			if (stockKeywordCheck || stockKeywordTypeCheck) {
				if (stockKeywordCheck) {
					// keywordId = generateStockKeyword
					stockKeywordId = generateStockKeyword(keywordName, keywordLinkQueue.getLink(), this.agentId, 1);
					if (stockKeywordId != 0)
						new StockParser(document, agent).parseStock(stockKeywordId);
					relatedKeywordLinkDao.upsertRelatedKeywordLink(keywordLinkQueue.getParentId(), stockKeywordId, 0);

				} else if (stockKeywordTypeCheck) {
					stockKeywordId = generateStockKeyword(keywordName, keywordLinkQueue.getLink(), this.agentId, 2);
					relatedKeywordLinkDao.upsertRelatedKeywordLink(keywordLinkQueue.getParentId(), stockKeywordId, 0);
				}
				Elements elements = document.select(DataCommon.getValueBy("RKeywordList", parsers))
						.select(DataCommon.getValueBy("RKeywordListElem", parsers));

				int correl = 1;
				for (Element elem : elements) {
					String title = elem.text().trim().replaceAll("주식", "").replaceAll("주가", "");
					String link = agentConfig.getSearchQuery() + elem.attr("href");
					saveKeywordLinkQueue(link, agentId, stockKeywordId);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(agent.getName() + " 키워드 파싱 완료!!");

	}
	/*
	 * 검색어가 현재 디비에 있는 경우 업데이트하며 없을 경우 키워드를 저장합니다. KeywordMain(indexing table),
	 * Keyword(Agent별 키워드), RelatedKeywordLink(연관 검색어 저장) 테이블에 각각 저장 및 업데이트 됩니다.
	 */
	private int generateKeyword(String keywordName, String link, int agentId) {
		// TODO Auto-generated method stub
		int keywordId = 0;
		int keywordMainId = keywordMainDao.upsertKeywordMain(keywordName);

		if (keywordMainId != 0) {
			keywordId = keywordDao.upsertKeyword(keywordName, link, keywordMainId, agentId);
			System.out.println(keywordMainId + " : " + keywordId + " : " + keywordName);
		}
		return keywordId;
	}

	private int generateStockKeyword(String keywordName, String link, int agentId, int typeId) {
		int stockKeywordId = 0;
		int keywordMainId = keywordMainDao.upsertKeywordMain(keywordName);

		if (keywordMainId != 0) {
			stockKeywordId = stockKeywordDao.upsertKeyword(keywordName, link, keywordMainId, agentId, typeId);
			System.out.println(keywordMainId + " : " + stockKeywordId + " : " + keywordName + " : " + typeId);
		}
		return stockKeywordId;
	}


	/*
	 * private int generateStockKeyword(String keywordName, String link, int
	 * agentId, int typeId){ int keywordId = 0; int keywordMainId =
	 * keywordMainDao.upsertKeywordMain(keywordName);
	 * 
	 * if (keywordMainId != 0){
	 * 
	 * } }
	 */
}
