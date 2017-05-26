package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageFetcher {
    public PageFetcher() {
    }

    public Document fetchDocument(String link, Agent agent) {
        AgentConfig agentConfig = agent.getAgentConfig();
        Connection connection = Jsoup.connect(link).timeout(agentConfig.getTimeout() * 1000)
                .userAgent(agentConfig.getUserAgent());
        try {
            return connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}