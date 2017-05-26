package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.model.AgentConfig;
import org.jsoup.nodes.Document;

public interface KeywordParser {
	String parseKeyword(Document document);
	void parseRelatedKeywords(int parentId, AgentConfig agentConfig, int stockKeywordId, Document document);
	int generateKeyword(String keywordName, String link, int agentId, int keywordType);
	int checkKeywordTypeBy(Document document, String keywordName);
}
