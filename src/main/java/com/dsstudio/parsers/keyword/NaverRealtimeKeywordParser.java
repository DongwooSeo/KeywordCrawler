package com.dsstudio.parsers.keyword;

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
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RealtimeKeywordDao;
import com.dsstudio.hibernate.dao.RealtimeKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.hibernate.model.RealtimeKeyword;

public class NaverRealtimeKeywordParser implements KeywordParser {
	private Agent agent;
	private List<Parser> parsers;
	
	private KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
	private RealtimeKeywordDao realtimeKeywordDao = new RealtimeKeywordDaoImpl();
	
	public NaverRealtimeKeywordParser(int agentId) {
		// TODO Auto-generated constructor stub
		this.agent = new AgentDaoImpl().findById(agentId);
		this.parsers = new ParserDaoImpl().findByAgentId(agentId);
	}
	public void parse(KeywordLinkQueue keywordLinkQueue) {
		// TODO Auto-generated method stub
		AgentConfig agentConfig = agent.getAgentConfig();

		List<RealtimeKeyword> realtimeKeywords = new ArrayList<RealtimeKeyword>();
		realtimeKeywordDao.deleteAllByAgentId(agent.getId());

		System.out.println(Thread.currentThread().getName() + " | " + agent.getName() + " " + "실시간 키워드 파싱중....");

		try {
			Connection connection = Jsoup.connect(agentConfig.getUrl()).timeout(agentConfig.getTimeout()*1000).userAgent(agentConfig.getUserAgent());
			Document document = connection.get();
			Elements rtKeywordList = document.select(DataCommon.getValueBy("RtKeywordList", parsers));

			int rank = 1;
			for (Element rtKeywordElem : rtKeywordList) {
				String name = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordTitle", parsers)).text();
				String step = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordStep", parsers)).text();
				String link = rtKeywordElem.select("a").first().attr("href");

				RealtimeKeyword realtimeKeyword = new RealtimeKeyword();
				realtimeKeyword.setAgentId(agent.getId());
				realtimeKeyword.setName(name);
				realtimeKeyword.setLink(link);
				realtimeKeyword.setRank(rank);
				if (step.isEmpty()) {
					realtimeKeyword.setStep(0);
				} else {
					realtimeKeyword.setStep(Integer.parseInt(step));
				}
				realtimeKeyword.setCreatedTime(new Timestamp(new Date().getTime()));
				realtimeKeyword.setUpdatedTime(new Timestamp(new Date().getTime()));
				realtimeKeywords.add(realtimeKeyword);
				// realtimeKeywordDao.save(realtimeKeyword);
				int parentId = 0;
				saveKeywordLinkQueue(link, agent.getId(), parentId);
				rank++;
			}
			realtimeKeywordDao.saveAll(realtimeKeywords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " | " + agent.getName() + " " + "실시간 키워드 파싱 성공!!");
	}
	private int saveKeywordLinkQueue(String link, int agentId, int parentId) {
		keywordLinkQueueDao.saveIfNotExist(link, agentId, parentId);
		return 0;
	}

	public String parseKeyword(Document document) {
		return null;
	}

	public void parseRelatedKeywords(int parentId, AgentConfig agentConfig, int stockKeywordId, Document document) {

	}

	public int generateKeyword(String keywordName, String link, int agentId, int keywordType) {
		return 0;
	}

	public int generateKeyword(Document document, String link, int agentId, int keywordType) {
		return 0;
	}

	public int generateKeyword(Document document, int stockKeywordId, Agent agent, int keywordType) {
		return 0;
	}

	public void generateKeyword() {

	}

	public int checkKeywordTypeBy(Document document, String keywordName) {
		return 0;
	}
}
