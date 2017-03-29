package com.dsstudio.parser.keyword;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public abstract class CommonKeywordParser {
	protected abstract int getAgentId();
	protected abstract int saveKeywordLinkQueue(String link, int agentId, int parentId);
	protected abstract void parseRealtimeKeyword();
	protected abstract void parseKeyword(KeywordLinkQueue keywordLinkQueue);
}
