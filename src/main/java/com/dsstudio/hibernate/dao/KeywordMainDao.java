package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.KeywordMain;

public interface KeywordMainDao {
	KeywordMain findByName(String name);
	int save(KeywordMain keywordMain);
	void update(KeywordMain keywordMain);
}
