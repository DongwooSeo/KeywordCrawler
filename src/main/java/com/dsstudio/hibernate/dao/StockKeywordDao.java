package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.StockKeyword;

public interface StockKeywordDao {
	StockKeyword fetchFirstRow();
	void update(StockKeyword entity);
	int upsertKeyword(String keywordName, String link, int keywordMainId, int agentId, int typeId);
	boolean checkIfExist(String keywordName);
}
