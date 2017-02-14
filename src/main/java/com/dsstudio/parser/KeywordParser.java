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
	private static AgentDao agentDao = new AgentDaoImpl();
	
	public KeywordParser(List<CommonKeywordParser> keywordParsers){
		this.keywordParsers = keywordParsers;
	}
	/*
	 * 현재의 Agent 가 크롤링 가능한지 DB의 dateFinished 필드를 참조하여
	 * 가능 여부를 판단합니다.
	 */
	public static synchronized Agent crawlableAgent(){
		Agent agent = agentDao.fetchFirstRow();
		boolean isAgentCrawlable = false;
		
		if(agent!=null){
			if(agent.getDateFinished()==null){
				isAgentCrawlable = true;
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MINUTE, -1 * agent.getMinutes());
				if(new Timestamp(cal.getTime().getTime()).after(agent.getDateFinished())){
					isAgentCrawlable = true;
				}
			}
			if(isAgentCrawlable){
				//agent.getName();
				System.out.println(agent.getName());
				agent.setDateFinished(new Timestamp(new Date().getTime()));
				agentDao.update(agent);
				return agent;
			}
		}
		return null;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		CommonKeywordParser keywordParser = null;
		System.out.println(Thread.currentThread().getName()+" 크롤링 시작");
		
		while(true){
			System.out.println(Thread.currentThread()+" 작동중");
			Agent agent = crawlableAgent();
			
			if(agent!=null){
				System.out.println("시작!");
				
				for(CommonKeywordParser _keywordParser : keywordParsers){
					if(_keywordParser.getAgentId()==agent.getId()){
						keywordParser = _keywordParser;		
					}
				}
				if(keywordParser!=null)
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
