package com.dsstudio.parser.keyword;

import java.util.List;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class ParserSelector implements Runnable {

    private static KeywordLinkQueueDao keywordLinkQueueDao;
    private List<AgentParser> agentParsers;
    private AgentDao agentDao;

    public ParserSelector(List<AgentParser> agentParsers) {
        this.agentParsers = agentParsers;
        this.agentDao = new AgentDaoImpl();
        this.keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
    }

    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            System.out.println(Thread.currentThread() + " 작동중");

            KeywordLinkQueue keywordLinkQueue = getCrawlableLinkQueue();

            if (keywordLinkQueue==null)
                continue;

            AgentParser agentParser = getAgentParserFrom(keywordLinkQueue);

            if (getAgentFrom(agentParser).getIsUsed() == 1) {
                parseAgentPageFrom(agentParser, keywordLinkQueue);
                setStatusFinishedTo(keywordLinkQueue);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Agent getAgentFrom(AgentParser agentParser) {
        return agentDao.findById(agentParser.getAgentId());
    }

    private AgentParser getAgentParserFrom(KeywordLinkQueue keywordLinkQueue) {
        AgentParser _agentParser = null;
        for (AgentParser agentParser : agentParsers) {
            if (keywordLinkQueue.getAgentId() == agentParser.getAgentId()) {
                _agentParser = agentParser;
            }
        }
        return _agentParser;
    }

    private static synchronized KeywordLinkQueue getCrawlableLinkQueue() {
        KeywordLinkQueue keywordLinkQueue = keywordLinkQueueDao.fetchFirstRow();
        return keywordLinkQueue;
    }
    private void parseAgentPageFrom(AgentParser agentParser, KeywordLinkQueue keywordLinkQueue) {
        agentParser.parse(keywordLinkQueue);
    }

    private void setStatusFinishedTo(KeywordLinkQueue keywordLinkQueue) {
        keywordLinkQueue.setStatus(3);
        keywordLinkQueueDao.update(keywordLinkQueue);
    }
}
