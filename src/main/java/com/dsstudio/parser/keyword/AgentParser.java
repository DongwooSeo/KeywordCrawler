package com.dsstudio.parser.keyword;

import java.util.ArrayList;
import java.util.List;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public abstract class AgentParser {
	
	protected List<KeywordParser> parsers;
	protected int agentId;
	protected String agentName;
	
	public AgentParser() {
		parsers = new ArrayList<KeywordParser>();
	}

	public int getAgentId() {
		return agentId;
	}

	protected void addParser(KeywordParser parser){
		this.parsers.add(parser);
	}
	
	public void parse(KeywordLinkQueue keywordLinkQueue){
		for(KeywordParser parser : parsers){
			parser.parse(keywordLinkQueue);
		}
	}
}