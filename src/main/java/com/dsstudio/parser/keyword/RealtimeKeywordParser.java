package com.dsstudio.parser.keyword;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.model.Agent;

/*
 * 실시간 검색어 수집기 입니다.
 * 각각의 Agent 안 parseRealtimeKeyword를 호출하여
 * 실시간 검색어를 수집합니다.
 */
public class RealtimeKeywordParser implements Runnable {
	private List<CommonKeywordParser> keywordParsers;
	private static AgentDao agentDao = new AgentDaoImpl();

	public RealtimeKeywordParser(List<CommonKeywordParser> keywordParsers) {
		this.keywordParsers = keywordParsers;
	}

	/*
	 * 현재의 Agent 가 크롤링 가능한지 DB의 dateFinished 필드를 참조하여 가능 여부를 판단합니다.
	 */
	public static synchronized Agent crawlableAgent() {
		Agent agent = agentDao.fetchFirstRow();

		if (agent != null) {

			// agent.getName();
			System.out.println(agent.getName());
			agent.setStatus(2);
			agent.setDateFinished(new Timestamp(new Date().getTime()));
			agentDao.update(agent);
			return agent;
		}
		return null;
	}

	public void run() {
		// TODO Auto-generated method stub
		CommonKeywordParser keywordParser = null;

		while (true) {
			System.out.println(Thread.currentThread() + " 작동중");
			Agent agent = crawlableAgent();

			if (agent != null) {
				System.out.println("실시간 키워드 크롤링 시작!");

				for (CommonKeywordParser _keywordParser : keywordParsers) {
					if (_keywordParser.getAgentId() == agent.getId()) {
						keywordParser = _keywordParser;
						System.out.println(keywordParser.getAgentId());
					}
				}
				if (keywordParser != null) {
					keywordParser.parseRealtimeKeyword();
					agent.setStatus(3);
					agentDao.update(agent);
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
