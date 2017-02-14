package com.dsstudio.parser;

import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;

public abstract class CommonKeywordParser {
	protected KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
	protected abstract int getAgentId();
	protected abstract int saveKeywordLinkQueue(String link);
	protected abstract void parseKeyword();
}
