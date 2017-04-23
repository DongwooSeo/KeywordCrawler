package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.parser.keyword.AgentParser;
import com.dsstudio.parser.keyword.NaverParser;
import com.dsstudio.parser.keyword.Parser;

public class BasicKeywordCrawler extends KeywordCrawler {
	static final Logger logger = LoggerFactory.getLogger(BasicKeywordCrawler.class);

	private List<AgentParser> agentParsers = new ArrayList<AgentParser>();

	public BasicKeywordCrawler() {
		agentParsers.add(new NaverParser());
	}

	public void run() {
		// TODO Auto-generated method stub
		// 파싱 시작!
		new Thread(new Parser(agentParsers)).start();
	}
}
