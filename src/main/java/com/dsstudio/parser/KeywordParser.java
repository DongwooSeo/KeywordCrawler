package com.dsstudio.parser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.model.Agent;

public class KeywordParser implements Runnable{
	private List <CommonKeywordParser> keywordParsers;
	
	public KeywordParser(List<CommonKeywordParser> keywordParsers){
		this.keywordParsers = keywordParsers;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			//Naver, Daum parser의 parseKeyword() 메서드를 호출!
			//-> 네이버, 다음의 실시간 검색어 수집 시작!.
			for(CommonKeywordParser keywordParser : keywordParsers){
				keywordParser.parseKeyword();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/*
	public static void main(String[] args){
		List<ICommonKeywordParser> commonKeywordParsers = new ArrayList<ICommonKeywordParser>();
		commonKeywordParsers.add(new NaverKeywordParser());
		commonKeywordParsers.add(new DaumKeywordParser());
	
		for(int i=0; i<commonKeywordParsers.size(); i++){
			new Thread(new KeywordParser(commonKeywordParsers),"ParserThread-"+i).start();
		}
		
	}*/
}
