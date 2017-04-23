package com.dsstudio.parser.keyword;

import java.util.List;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class Parser implements Runnable{

	private List<AgentParser> agentParsers;
	private AgentDao agentDao = new AgentDaoImpl();
	private static KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();

	public Parser(List<AgentParser> agentParsers){
		this.agentParsers = agentParsers;
	}
	
	public static synchronized KeywordLinkQueue crawlableLinkQueue() {
		KeywordLinkQueue keywordLinkQueue = keywordLinkQueueDao.fetchFirstRow();
		return keywordLinkQueue;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		AgentParser agentParser = null;
		Agent agent = null;
		while (true) {
			System.out.println(Thread.currentThread() + " 작동중");
			KeywordLinkQueue keywordLinkQueue = crawlableLinkQueue();
			if (keywordLinkQueue != null) {
				for (AgentParser _agentParser : agentParsers) {
					if (keywordLinkQueue.getAgentId() == _agentParser.getAgentId())
						agentParser = _agentParser;
				}
				if (agentParser != null) {
					agent = agentDao.findById(agentParser.getAgentId());
					if (agent.getIsUsed() == 1) {
						System.out.println("키워드 크롤링 시작!");
						agentParser.parse(keywordLinkQueue);
						keywordLinkQueue.setStatus(3);
						keywordLinkQueueDao.update(keywordLinkQueue);
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
