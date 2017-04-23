package com.dsstudio.parser.keyword;

import java.util.ArrayList;
import java.util.List;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public abstract class AgentParser {
	
	protected List<KeywordParsable> parsers;
	protected int agentId;
	protected String agentName;
	
	public AgentParser() {
		parsers = new ArrayList<KeywordParsable>();
	}

	public int getAgentId() {
		return agentId;
	}

	protected void addParser(KeywordParsable parser){
		this.parsers.add(parser);
	}
	
	public void parse(KeywordLinkQueue keywordLinkQueue){
		for(KeywordParsable parser : parsers){
			parser.parse(keywordLinkQueue);
		}
	}
}