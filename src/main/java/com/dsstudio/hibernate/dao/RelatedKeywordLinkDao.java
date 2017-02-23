package com.dsstudio.hibernate.dao;

import java.util.List;

import com.dsstudio.hibernate.model.RelatedKeywordLink;

public interface RelatedKeywordLinkDao {
	RelatedKeywordLink findByKeywordId(int keywordId);
	RelatedKeywordLink findByKeywordAndRelatedId(int keywordId, int relatedId);
	void save(RelatedKeywordLink relatedKeywordLink);
	void saveAll(List<RelatedKeywordLink> relatedKeywordLinks);
	void upsertRelatedKeywordLink(int keywordId, int relatedId);
}
