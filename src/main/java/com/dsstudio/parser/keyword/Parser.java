package com.dsstudio.parser.keyword;

import java.util.List;

import com.dsstudio.hibernate.dao.AgentDao;
import com.dsstudio.hibernate.dao.AgentDaoImpl;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDao;
import com.dsstudio.hibernate.dao.KeywordLinkQueueDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class Parser implements Runnable {

    private List<AgentParser> agentParsers;
    private AgentParser agentParser = null;
    private AgentDao agentDao = new AgentDaoImpl();
    private KeywordLinkQueue keywordLinkQueue;
    private static KeywordLinkQueueDao keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();

    public Parser(List<AgentParser> agentParsers) {
        this.agentParsers = agentParsers;
    }

    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            System.out.println(Thread.currentThread() + " 작동중");

            keywordLinkQueue = getCrawlableLinkQueue();

            if (!checkParsingAvailability())
                continue;

            if (getAgentFromAgentParser().getIsUsed() == 1) {
                parseFromKeywordLinkQueue();
                parseFinishedFromKeywordLinkQueue();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private boolean checkParsingAvailability() {
        return keywordLinkQueue != null && getAgentParserMatchedFromKeywordLinkQueue();
    }

    private Agent getAgentFromAgentParser() {
        return agentDao.findById(agentParser.getAgentId());
    }

    private void parseFromKeywordLinkQueue() {
        agentParser.parse(keywordLinkQueue);
    }

    private boolean getAgentParserMatchedFromKeywordLinkQueue() {
        for (AgentParser agentParser : agentParsers) {
            if (keywordLinkQueue.getAgentId() == agentParser.getAgentId()) {
                this.agentParser = agentParser;
                return true;
            }
        }
        return false;
    }

    private void parseFinishedFromKeywordLinkQueue() {
        keywordLinkQueue.setStatus(3);
        keywordLinkQueueDao.update(keywordLinkQueue);
    }

    private static synchronized KeywordLinkQueue getCrawlableLinkQueue() {
        KeywordLinkQueue keywordLinkQueue = keywordLinkQueueDao.fetchFirstRow();
        return keywordLinkQueue;
    }
}
