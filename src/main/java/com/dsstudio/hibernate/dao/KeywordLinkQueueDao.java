package com.dsstudio.hibernate.dao;

import java.util.List;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public interface KeywordLinkQueueDao {
	KeywordLinkQueue findByLink(String link);
	KeywordLinkQueue fetchFirstRow();
	void save(KeywordLinkQueue entity);
	void saveAll(List<KeywordLinkQueue> keywordLinkQueues);
	void saveIfNotExist(String link, int agentId, int parentId);
	void update(KeywordLinkQueue keywordLinkQueue);
	
}
