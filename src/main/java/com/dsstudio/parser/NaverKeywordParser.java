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
	private static NaverKeywordParser naverKeywordParser = null;
	private Object lock = new Object();
	
	private NaverKeywordParser(){
		super.agentId = 1;
		super.agentName = "네이버";
	}
	
	public static synchronized NaverKeywordParser getInstance(){
		if(naverKeywordParser==null)
			naverKeywordParser = new NaverKeywordParser();
		
		return naverKeywordParser;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dsstudio.parser.CommonKeywordParser#parseKeyword()
	 * 각각 Thread의 호출로 인한 parseKeyword 의 중복 호출을 막기 위해 lock 구현.
	 */
	public void parseKeyword() {
		// TODO Auto-generated method stub
		synchronized(lock){
			
			Agent agent = agentDao.findById(super.getAgentId());
			/*
			 * isCrawl(agent) --> Multi-threads의 호출로 부터  parseKeyword의 중복 호출을 막기위하여
			 * db의 dateFinished 필드를 참조하여 실행 여부를 판단합니다.
			 */
			if(super.isCrawl(agent)){
				System.out.println(Thread.currentThread().getName()+" | " + super.agentName + " " + "파싱중....");
				List<Parser> parsers = parseDao.findByAgentId(agent.getId());
				AgentConfig agentConfig = agent.getAgentConfig();
				
				realtimeKeywordDao.deleteAllByAgentId(agent.getId());
				
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
						realtimeKeywordDao.save(realtimeKeyword);
						saveKeywordLinkQueue(link);
						
						
						rank++;
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> links = new ArrayList<String>();
				agent.setDateFinished(new Timestamp(new Date().getTime()));
				agentDao.update(agent);
				System.out.println(Thread.currentThread().getName()+" | " + super.agentName + " " + "파싱 성공!!");
			}
			
		}
		
	}

}
