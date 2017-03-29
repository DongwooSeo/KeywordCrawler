package com.dsstudio.parser.keyword;

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
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.hibernate.model.RealtimeKeyword;

public class DaumKeywordParser extends AgentKeywordParser {
	// private static DaumKeywordParser daumKeywordParser = null;

	public DaumKeywordParser() {
		super.agentId = 2;
		super.agentName = "다음";
	}

	/*
	 * (다음 실시간 검색어를 파싱합니다.)
	 * @see com.dsstudio.parser.keyword.AgentKeywordParser#parseRealtimeKeyword()
	 */
	public void parseRealtimeKeyword() {

		// TODO Auto-generated method stub
		Agent agent = agentDao.findById(super.getAgentId());
		AgentConfig agentConfig = agent.getAgentConfig();

		List<Parser> parsers = parseDao.findByAgentId(agent.getId());
		List<RealtimeKeyword> realtimeKeywords = new ArrayList<RealtimeKeyword>();
		realtimeKeywordDao.deleteAllByAgentId(agent.getId());

		System.out.println(Thread.currentThread().getName() + " | " + super.agentName + " " + "실시간 키워드 파싱중....");

		try {
			Connection connection = Jsoup.connect(agentConfig.getUrl()).timeout(agentConfig.getTimeout()*1000).userAgent(agentConfig.getUserAgent());
			Document document = connection.get();
			Elements rtKeywordList = document.select(DataCommon.getValueBy("RtKeywordList", parsers));

			int rank = 1;
			for (Element rtKeywordElem : rtKeywordList) {
				Element elem = rtKeywordElem.select("a").first();

				String link = elem.attr("href");
				String name = elem.text();
				String step = rtKeywordElem.select(DataCommon.getValueBy("RtKeywordStep", parsers)).first().ownText();

				RealtimeKeyword realtimeKeyword = new RealtimeKeyword();
				realtimeKeyword.setAgentId(super.getAgentId());
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
				int parentId = 0;
				saveKeywordLinkQueue(link, agent.getId(), parentId);
				rank++;
			}
			realtimeKeywordDao.saveAll(realtimeKeywords);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " | " + super.agentName + " " + "실시간 키워드 파싱 성공!!");
	}

}
