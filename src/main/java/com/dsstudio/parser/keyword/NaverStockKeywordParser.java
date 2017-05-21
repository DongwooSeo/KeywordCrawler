package com.dsstudio.parser.keyword;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dsstudio.helper.DataCommon;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.dao.KeywordMainDao;
import com.dsstudio.hibernate.dao.KeywordMainDaoImpl;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDao;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDaoImpl;
import com.dsstudio.hibernate.dao.StockKeywordDao;
import com.dsstudio.hibernate.dao.StockKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.parser.stock.StockParser;

public class NaverStockKeywordParser implements KeywordParsable {
	private Agent agent;
	private List<Parser> parsers;

	private KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
	private RelatedKeywordLinkDao relatedKeywordLinkDao = new RelatedKeywordLinkDaoImpl();
	private StockKeywordDao stockKeywordDao = new StockKeywordDaoImpl();
	private KeywordMainDao keywordMainDao = new KeywordMainDaoImpl();

	private StockParser stockParser;

	public NaverStockKeywordParser(int agentId, StockParser stockParser) {
		// TODO Auto-generated constructor stub
		this.agent = new AgentDaoImpl().findById(agentId);
		this.parsers = new ParserDaoImpl().findByAgentId(agentId);
		this.stockParser = stockParser;
	}

	public void parse(KeywordLinkQueue keywordLinkQueue) {
		// TODO Auto-generated method stub
		AgentConfig agentConfig = agent.getAgentConfig();
		int stockKeywordId;

		try {
			System.out.println(agent.getName() + " 키워드 파싱 시작!!");
			// System.out.println(keywordLinkQueue.getLink());
			Document document = getDocumentBy(keywordLinkQueue, agentConfig);

			// System.out.println(document.toString().("sItemCode : "));
			String keywordName = getKeywordNameFrom(document);

			if (document.toString().contains(DataCommon.getValueBy("StockKeywordCheck", parsers))) {
				stockKeywordId = generateStockKeyword(keywordName, keywordLinkQueue.getLink(), agent.getId(), 1);
				if (stockKeywordId != 0)
					stockParser.parseStock(document, stockKeywordId, agent, parsers);

			} else if (DataCommon.containValue(DataCommon.getValueBy("StockKeywordTypeCheck", parsers), keywordName)) {
				stockKeywordId = generateStockKeyword(keywordName, keywordLinkQueue.getLink(), agent.getId(), 2);
			} else {
				return;
			}
			relatedKeywordLinkDao.upsertRelatedKeywordLink(keywordLinkQueue.getParentId(), stockKeywordId, 0);
			Elements elements = document.select(DataCommon.getValueBy("RKeywordList", parsers))
					.select(DataCommon.getValueBy("RKeywordListElem", parsers));

			int correl = 1;
			for (Element elem : elements) {
				String title = elem.text().trim().replaceAll("주식", "").replaceAll("주가", "");
				String link = agentConfig.getSearchQuery() + elem.attr("href");
				saveKeywordLinkQueue(link, agent.getId(), stockKeywordId);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(agent.getName() + " 키워드 파싱 완료!!");
	}

	private String getKeywordNameFrom(Document document) {
		return document.select(DataCommon.getValueBy("KeywordField", parsers))
                        .attr(DataCommon.getValueBy("KeywordFieldAttr", parsers)).trim().replaceAll("주가", "")
                        .replaceAll("주식", "");
	}

	private Document getDocumentBy(KeywordLinkQueue keywordLinkQueue, AgentConfig agentConfig) throws IOException {
		Connection connection = Jsoup.connect(keywordLinkQueue.getLink()).timeout(agentConfig.getTimeout() * 1000)
                .userAgent(agentConfig.getUserAgent());
		return connection.get();
	}

	private int saveKeywordLinkQueue(String link, int agentId, int parentId) {
		keywordLinkQueueDao.saveIfNotExist(link, agentId, parentId);
		return 0;
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
}
