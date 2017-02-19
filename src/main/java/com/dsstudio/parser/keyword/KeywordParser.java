package com.dsstudio.parser.keyword;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;

/*
 * 검색어 상세페이지 수집기 입니다.
 * 각각 Agent의 parseKeyword를 호출하게 되며
 * Agent별 키워드의 상세페이지를 수집합니다.
 */
public class KeywordParser implements Runnable {

	private List<CommonKeywordParser> keywordParsers;
	private AgentDao agentDao = new AgentDaoImpl();
	private static KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();

	public KeywordParser(List<CommonKeywordParser> keywordParsers) {
		this.keywordParsers = keywordParsers;
	}

	/*
	 * 현재의 Link 가 크롤링 가능한지 DB의 BookingDate 필드를 참조하여 가능 여부를 판단합니다.
	 */
	public static synchronized KeywordLinkQueue crawlableLinkQueue() {
		KeywordLinkQueue keywordLinkQueue = keywordLinkQueueDao.fetchFirstRow();

		if (keywordLinkQueue != null) {
			// agent.getName();
			keywordLinkQueue.setStatus(2);
			keywordLinkQueue.setBooking(UUID.randomUUID() + "");
			keywordLinkQueue.setBookingDate(new Timestamp(new Date().getTime()));
			keywordLinkQueueDao.update(keywordLinkQueue);
			return keywordLinkQueue;
		}
		return null;
	}

	public void run() {
		// TODO Auto-generated method stub
		CommonKeywordParser keywordParser = null;
		Agent agent = null;
		while (true) {
			System.out.println(Thread.currentThread() + " 작동중");
			KeywordLinkQueue keywordLinkQueue = crawlableLinkQueue();
			if (keywordLinkQueue != null) {
				for (CommonKeywordParser _keywordParser : keywordParsers) {
					if (keywordLinkQueue.getAgentIndex() == _keywordParser.getAgentId())
						keywordParser = _keywordParser;
				}
				if (keywordParser != null) {
					agent = agentDao.findById(keywordParser.getAgentId());
					if (agent.getIsUsed() == 1) {
						System.out.println("키워드 크롤링 시작!");
						keywordParser.parseKeyword(keywordLinkQueue);
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
