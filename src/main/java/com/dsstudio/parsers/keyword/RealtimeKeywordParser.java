package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import org.jsoup.nodes.Document;

public class RealtimeKeywordParser implements KeywordParser {

	public void parse(KeywordLinkQueue keywordLinkQueue) {
		// TODO Auto-generated method stub
		
	}

	public String parseKeyword(Document document) {
		return null;
	}

	public void parseRelatedKeywords(int parentId, AgentConfig agentConfig, int stockKeywordId, Document document) {

	}

	public int generateKeyword(String keywordName, String link, int agentId, int keywordType) {
		return 0;
	}

	public int generateKeyword(Document document, String link, int agentId, int keywordType) {
		return 0;
	}

	public void generateKeyword() {

	}

	public int checkKeywordTypeBy(Document document, String keywordName) {
		return 0;
	}
}