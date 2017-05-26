package com.dsstudio.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsstudio.parsers.keyword.*;
import com.dsstudio.parsers.stock.NaverStockParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicKeywordCrawler extends KeywordCrawler {
    static final Logger logger = LoggerFactory.getLogger(BasicKeywordCrawler.class);

    private List<AgentParser> agentParsers = new ArrayList<AgentParser>();
    public BasicKeywordCrawler() {
        agentParsers.add(
                new NaverParser(
                        Arrays.<KeywordParser>asList(new NaverStockKeywordParser(1, new NaverStockParser(), new KeywordGenerator())),
                        new PageFetcher()
                )
        );
    }

    public void run() {
        // TODO Auto-generated method stub
        // 파싱 시작!
        new Thread(new ParserSelector(agentParsers)).start();
    }
}
