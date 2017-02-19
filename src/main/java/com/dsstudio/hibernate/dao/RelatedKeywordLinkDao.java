package com.dsstudio.hibernate.dao;

import java.util.List;

import com.dsstudio.hibernate.model.RelatedKeywordLink;

public interface RelatedKeywordLinkDao {
	RelatedKeywordLink findByKeywordId(int keywordId);
	void save(RelatedKeywordLink relatedKeywordLink);
	void saveAll(List<RelatedKeywordLink> relatedKeywordLinks);
}
