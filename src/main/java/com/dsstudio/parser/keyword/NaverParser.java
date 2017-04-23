package com.dsstudio.parser.keyword;

import com.dsstudio.parser.stock.NaverStockParser;

public class NaverParser extends AgentParser{

	public NaverParser() {
		// TODO Auto-generated constructor stub
		super.agentId = 1;
		super.agentName = "네이버";
		addParser(new NaverStockKeywordParser(agentId, new NaverStockParser()));
	}
}