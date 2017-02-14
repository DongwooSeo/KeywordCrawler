package com.dsstudio.hibernate.dao;

import java.util.List;

import com.dsstudio.hibernate.model.RealtimeKeyword;

public interface RealtimeKeywordDao {
	RealtimeKeyword findById(int id);
	RealtimeKeyword findByName(String name);
	RealtimeKeyword findByNameAndAgentId(String name, int agentId);
	void save(RealtimeKeyword realtimeKeyword);
	void saveAll(List<RealtimeKeyword> realtimeKeywords);
	void saveOrUpdate(RealtimeKeyword realtimeKeyword);
	void update(RealtimeKeyword realtimeKeyword);
	void deleteAllByAgentId(int agentId);
	
}
