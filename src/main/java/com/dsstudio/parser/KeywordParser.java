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
			//NaverKeywordParser, DaumKeywordParser의 parserKeyword 메서드를 호출합니다.
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
}
