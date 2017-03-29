package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.parser.keyword.CommonKeywordParser;
import com.dsstudio.parser.keyword.DaumKeywordParser;
import com.dsstudio.parser.keyword.KeywordParser;
import com.dsstudio.parser.keyword.NaverKeywordParser;

public class BasicKeywordCrawler extends KeywordCrawler {
	static final Logger logger = LoggerFactory.getLogger(BasicKeywordCrawler.class);
	
	private List<CommonKeywordParser> commonKeywordParsers = new ArrayList<CommonKeywordParser>();
	
	//NaverKeywordParser, DaumKeywordParser 파서 추가 !
	public BasicKeywordCrawler(){
		commonKeywordParsers.add(new NaverKeywordParser());
		commonKeywordParsers.add(new DaumKeywordParser());
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		//파싱 시작!
		new Thread(new KeywordParser(commonKeywordParsers)).start();
	}
}
