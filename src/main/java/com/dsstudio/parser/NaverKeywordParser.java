package com.dsstudio.parser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dsstudio.helper.DataCommon;
import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.ParserDao;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RealtimeKeywordDao;
import com.dsstudio.hibernate.dao.RealtimeKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.hibernate.model.RealtimeKeyword;

public class NaverKeywordParser extends AgentKeywordParser {
	private Object lock = new Object();
	
	public NaverKeywordParser(){
		super.agentId = 1;
		super.agentName = "네이버";
	}
	
	public void parseKeyword() {
		// TODO Auto-generated method stub
			Agent agent = agentDao.findById(super.getAgentId());
			AgentConfig agentConfig = agent.getAgentConfig();
			
			List<Parser> parsers = parseDao.findByAgentId(agent.getId());
			List<RealtimeKeyword> realtimeKeywords = new ArrayList<RealtimeKeyword>();
			realtimeKeywordDao.deleteAllByAgentId(agent.getId());
			
			System.out.println(Thread.currentThread().getName() + " | " + super.agentName + " " + "파싱중....");
			
			try {
				Connection connection = Jsoup.connect(agentConfig.getUrl()).userAgent(agentConfig.getUserAgent());
				Document document = connection.get();
				Elements rtKeywordList = document.select(DataCommon.getValueBy("RtKeywordList", parsers));
				
				int rank = 1;
				for(Element rtKeywordElem : rtKeywordList){
					String name = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordTitle", parsers)).text();
					String step = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordStep", parsers)).text();
					String link = rtKeywordElem.select("a").first().attr("href");
					
					RealtimeKeyword realtimeKeyword = new RealtimeKeyword();
					realtimeKeyword.setAgentId(super.getAgentId());
					realtimeKeyword.setName(name);
					realtimeKeyword.setLink(link);
					realtimeKeyword.setRank(rank);
					if(step.isEmpty()){
						realtimeKeyword.setStep(0);
					}else{
						realtimeKeyword.setStep(Integer.parseInt(step));
					}
					realtimeKeyword.setCreatedTime(new Timestamp(new Date().getTime()));
					realtimeKeyword.setUpdatedTime(new Timestamp(new Date().getTime()));
					realtimeKeywords.add(realtimeKeyword);
					//realtimeKeywordDao.save(realtimeKeyword);
					saveKeywordLinkQueue(link);			
					rank++;
				}
				realtimeKeywordDao.saveAll(realtimeKeywords);		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> links = new ArrayList<String>();
			System.out.println(Thread.currentThread().getName()+" | " + super.agentName + " " + "파싱 성공!!");
	}

}
