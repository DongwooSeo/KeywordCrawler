package com.dsstudio.hibernate.dao;

import java.util.List;

import com.dsstudio.hibernate.model.Parser;

public interface ParserDao {
	List<Parser>findByAgentId(int agentId);

}
