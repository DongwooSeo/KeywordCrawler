package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsstudio.parser.CommonKeywordParser;
import com.dsstudio.parser.DaumKeywordParser;
import com.dsstudio.parser.KeywordParser;
import com.dsstudio.parser.NaverKeywordParser;

public class RealtimeKeywordCrawler extends KeywordCrawler{
	static final Logger logger = LoggerFactory.getLogger(RealtimeKeywordCrawler.class);
	
	private List<CommonKeywordParser> commonKeywordParsers = new ArrayList<CommonKeywordParser>();
	
	//NaverKeywordParser, DaumKeywordParser 파서 추가 !
	public RealtimeKeywordCrawler(){
		commonKeywordParsers.add(new NaverKeywordParser());
		commonKeywordParsers.add(new DaumKeywordParser());
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		//파싱 시작!
		new Thread(new KeywordParser(commonKeywordParsers)).start();
	}

}
