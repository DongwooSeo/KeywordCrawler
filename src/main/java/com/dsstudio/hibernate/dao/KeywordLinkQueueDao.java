package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public interface KeywordLinkQueueDao {
	KeywordLinkQueue findByLink(String link);
	void save(KeywordLinkQueue entity);
}
