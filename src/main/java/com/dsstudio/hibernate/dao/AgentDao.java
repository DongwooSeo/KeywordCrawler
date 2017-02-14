package com.dsstudio.hibernate.dao;

import com.dsstudio.hibernate.model.Agent;

public interface AgentDao {
	Agent findById(int id);
	Agent findByName(String name);
	Agent fetchFirstRow();
	void update(Agent agent);
}
