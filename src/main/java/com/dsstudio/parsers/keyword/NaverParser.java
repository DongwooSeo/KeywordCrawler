package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import org.jsoup.nodes.Document;

import java.util.List;

public class NaverParser implements AgentParser{

	public static final double STOCK_KEYWORD = 1;

	private final Agent agent;
	private List<KeywordParser> keywordParsers;
	private PageFetcher pageFetcher;


	public NaverParser(List<KeywordParser> parsers, PageFetcher pageFetcher) {
		this.agent = new AgentDaoImpl().findById(getAgentId());
		this.keywordParsers = parsers;
		this.pageFetcher = pageFetcher;
	}

	public void executeParsing(KeywordLinkQueue keywordLinkQueue) {
		for(KeywordParser keywordParser : keywordParsers){
			Document document = pageFetcher.fetchDocument(keywordLinkQueue.getLink(), agent);
			String keywordName = keywordParser.parseKeyword(document);
			int keywordType = keywordParser.checkKeywordTypeBy(document, keywordName);
			int keywordId = keywordParser.generateKeyword(keywordName, keywordLinkQueue.getLink(), agent.getId(), keywordType);

			//			if (keywordType == STOCK_KEYWORD)
//			int keywordId = keywordGenerator.stockKeywordGenerator(keywordName, keywordLinkQueue.getLink(), agent.getId(), keywordType);
//			if (keywordType == STOCK_KEYWORD)
//				stockParser.parseStock(document, stockKeywordId, agent, null);

//			keywordParser.parseRelatedKeywords(keywordLinkQueue.getParentId(),agent.getAgentConfig(),stockKeywordId,document);
		}
	}

	public int getAgentId() {
		return 1;
	}
}