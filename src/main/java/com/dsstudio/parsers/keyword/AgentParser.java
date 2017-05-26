package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public interface AgentParser {
	void executeParsing(KeywordLinkQueue keywordLinkQueue);
	int getAgentId();
}