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

public class DaumKeywordParser extends CommonKeywordParser {
	private static DaumKeywordParser daumKeywordParser=null;
	
	private ParserDao parseDao = new ParserDaoImpl();
	private AgentDao agentDao = new AgentDaoImpl();
	
	private RealtimeKeywordDao realtimeKeywordDao = new RealtimeKeywordDaoImpl();
	
	private Object lock = new Object();
	
	private DaumKeywordParser(){
		super.agentId=2;
	}
	
	public static synchronized DaumKeywordParser getInstance(){
		if(daumKeywordParser==null)
			daumKeywordParser = new DaumKeywordParser();
		return daumKeywordParser;
	}
	public void parseKeyword() {
		synchronized(lock){
			// TODO Auto-generated method stub
			Agent agent = agentDao.findById(super.getAgentId());
			if(super.isCrawl(agent)){
				List<Parser> parsers = parseDao.findByAgentId(agent.getId());
				AgentConfig agentConfig = agent.getAgentConfig();

				realtimeKeywordDao.deleteAllByAgentId(agent.getId());
				
				try {
					Connection connection = Jsoup.connect(agentConfig.getUrl()).userAgent(agentConfig.getUserAgent());
					Document document = connection.get();
					Elements rtKeywordList = document.select(DataCommon.getValueBy("RtKeywordList", parsers));
					
					int rank = 1;
					for(Element rtKeywordElem : rtKeywordList){
						Element elem = rtKeywordElem.select("a").first();
						String name = elem.text();
						String step = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordStep", parsers)).first().ownText();
						
						RealtimeKeyword realtimeKeyword = new RealtimeKeyword();
						realtimeKeyword.setAgentId(super.getAgentId());
						realtimeKeyword.setName(name);
						realtimeKeyword.setLink(elem.attr("href"));
						realtimeKeyword.setRank(rank);
						
						if(step.isEmpty()){
							realtimeKeyword.setStep(0);
						}else{
							realtimeKeyword.setStep(Integer.parseInt(step));
						}
						realtimeKeyword.setCreatedTime(new Timestamp(new Date().getTime()));
						realtimeKeyword.setUpdatedTime(new Timestamp(new Date().getTime()));
						realtimeKeywordDao.persist(realtimeKeyword);
						
						
						rank++;
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> links = new ArrayList<String>();
				agent.setDateFinished(new Timestamp(new Date().getTime()));
				agentDao.update(agent);
				
				}
		}
	}


}
