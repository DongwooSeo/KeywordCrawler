package com.dsstudio.parser;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.dao.ParserDao;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RealtimeKeywordDao;
import com.dsstudio.hibernate.dao.RealtimeKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class AgentKeywordParser extends CommonKeywordParser {
	protected int agentId;
	protected String agentName;
	protected AgentDao agentDao = new AgentDaoImpl();
	protected ParserDao parseDao = new ParserDaoImpl();
	protected RealtimeKeywordDao realtimeKeywordDao = new RealtimeKeywordDaoImpl();
	
	protected void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	
	protected int getAgentId() {
		return agentId;
	}
	
	protected int saveKeywordLinkQueue(String link){
		KeywordLinkQueue entity = keywordLinkQueueDao.findByLink(link);
		if(entity==null){
			Timestamp now = new Timestamp(new Date().getTime());
			KeywordLinkQueue keywordLinkQueue = new KeywordLinkQueue();
			keywordLinkQueue.setLink(link);
			keywordLinkQueue.setStatus(1);
			keywordLinkQueue.setDateCreated(now);
			keywordLinkQueue.setBooking(UUID.randomUUID()+"");
			keywordLinkQueue.setBookingDate(now);
			keywordLinkQueue.setAgentIndex(this.agentId);
			keywordLinkQueueDao.save(keywordLinkQueue);
			return 1;
		}
		
		return 0;
	}
	protected boolean isCrawl(Agent agent){
		boolean isCrawl = false;
		
		if(agent!=null){
			if(agent.getDateFinished()==null){
				isCrawl = true;
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MINUTE, -1 * agent.getMinutes());
				if(new Timestamp(cal.getTime().getTime()).after(agent.getDateFinished())){
					isCrawl = true;
				}
			}
		}
		return isCrawl;
	}

	@Override
	protected void parseKeyword() {
		// TODO Auto-generated method stub
	}

	
}
