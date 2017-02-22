package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.Keyword;

public interface KeywordDao {
	Keyword findByKwdMainIdAndAgentId(int keywordMainId, int agentId);
	Keyword findByNameAndAgentId(String name, int agentId);
	int save(Keyword keyword);
	int upsertKeyword(String keywordName, String link, int keywordMainId, int agentId);
	void update(Keyword keyword);
}
