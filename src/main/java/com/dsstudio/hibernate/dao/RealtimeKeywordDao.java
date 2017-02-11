package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.RealtimeKeyword;

public interface RealtimeKeywordDao {
	RealtimeKeyword findById(int id);
	RealtimeKeyword findByName(String name);
	RealtimeKeyword findByNameAndAgentId(String name, int agentId);
	void persist(RealtimeKeyword realtimeKeyword);
	void saveOrUpdate(RealtimeKeyword realtimeKeyword);
	void update(RealtimeKeyword realtimeKeyword);
	void deleteAllByAgentId(int agentId);
	
}
