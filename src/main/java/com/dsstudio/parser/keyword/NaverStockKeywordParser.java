package com.dsstudio.parser.keyword;

import java.io.IOException;
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
import com.dsstudio.hibernate.dao.KeywordMainDao;
import com.dsstudio.hibernate.dao.KeywordMainDaoImpl;
import com.dsstudio.hibernate.dao.ParserDaoImpl;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDao;
import com.dsstudio.hibernate.dao.RelatedKeywordLinkDaoImpl;
import com.dsstudio.hibernate.dao.StockKeywordDao;
import com.dsstudio.hibernate.dao.StockKeywordDaoImpl;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.parser.stock.StockParser;

public class NaverStockKeywordParser implements KeywordParser {
    private static final int STOCK_KEYWORD = 1;
    private static final int STOCK_RELATED_KEYWORD = 2;
    private static final int NOT_A_STOCK_KEYWORD = 0;
    private Agent agent;
    private List<Parser> parsers;
    private KeywordLinkQueueDao keywordLinkQueueDao;
    private RelatedKeywordLinkDao relatedKeywordLinkDao;
    private StockKeywordDao stockKeywordDao;
    private KeywordMainDao keywordMainDao;
    private StockParser stockParser;

    public NaverStockKeywordParser(int agentId, StockParser stockParser) {
        // TODO Auto-generated constructor stub
        this.agent = new AgentDaoImpl().findById(agentId);
        this.parsers = new ParserDaoImpl().findByAgentId(agentId);
        this.stockParser = stockParser;
        this.keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
        this.relatedKeywordLinkDao = new RelatedKeywordLinkDaoImpl();
        this.stockKeywordDao = new StockKeywordDaoImpl();
        this.keywordMainDao = new KeywordMainDaoImpl();
    }

    public void parse(KeywordLinkQueue keywordLinkQueue) {
        // TODO Auto-generated method stub
        AgentConfig agentConfig = agent.getAgentConfig();

        try {
            System.out.println(agent.getName() + " 키워드 파싱 시작!!");

            Document document = getDocumentBy(keywordLinkQueue, agentConfig);

            String pageHtml = document.toString();
            String keywordName = getKeywordNameFrom(document);

            int stockKeywordType = getStockKeywordTypeBy(pageHtml, keywordName);
            if (stockKeywordType == NOT_A_STOCK_KEYWORD) {
                return;
            }

            int stockKeywordId = generateStockKeyword(keywordName, keywordLinkQueue.getLink(), agent.getId(), stockKeywordType);
            if (stockKeywordType == STOCK_KEYWORD)
                stockParser.parseStock(document, stockKeywordId, agent, parsers);

            saveRelatedKeywordLink(keywordLinkQueue, stockKeywordId);
            parseRelatedKeywords(agentConfig, stockKeywordId, document);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(agent.getName() + " 키워드 파싱 완료!!");
    }

    private void saveRelatedKeywordLink(KeywordLinkQueue keywordLinkQueue, int stockKeywordId) {
        relatedKeywordLinkDao.upsertRelatedKeywordLink(keywordLinkQueue.getParentId(), stockKeywordId, 0);
    }

    private void parseRelatedKeywords(AgentConfig agentConfig, int stockKeywordId, Document document) {
        String relKeywordList = DataCommon.getValueBy("RKeywordList", parsers);
        String relKeywordItems = DataCommon.getValueBy("RKeywordListElem", parsers);

        Elements elements = document.select(relKeywordList).select(relKeywordItems);

        for (Element elem : elements) {
            String link = agentConfig.getSearchQuery() + elem.attr("href");
            saveIntoKeywordLinkQueue(link, agent.getId(), stockKeywordId);
        }
    }

    private String getKeywordNameFrom(Document document) {
        return document.select(DataCommon.getValueBy("KeywordField", parsers))
                .attr(DataCommon.getValueBy("KeywordFieldAttr", parsers)).trim().replaceAll("주가", "")
                .replaceAll("주식", "");
    }

    private Document getDocumentBy(KeywordLinkQueue keywordLinkQueue, AgentConfig agentConfig) throws IOException {
        Connection connection = Jsoup.connect(keywordLinkQueue.getLink()).timeout(agentConfig.getTimeout() * 1000)
                .userAgent(agentConfig.getUserAgent());
        return connection.get();
    }

    private int generateStockKeyword(String keywordName, String link, int agentId, int typeId) {
        int keywordMainId = saveIntoKeywordMain(keywordName);
        if (keywordMainId == 0) {
            return 0;
        }
        int stockKeywordId = saveIntoStockKeyword(keywordName, link, agentId, typeId, keywordMainId);
        System.out.println(keywordMainId + " : " + stockKeywordId + " : " + keywordName + " : " + typeId);
        return stockKeywordId;
    }

    private int saveIntoKeywordMain(String keywordName) {
        return keywordMainDao.upsertKeywordMain(keywordName);
    }

    private int saveIntoStockKeyword(String keywordName, String link, int agentId, int typeId, int keywordMainId) {
        return stockKeywordDao.upsertKeyword(keywordName, link, keywordMainId, agentId, typeId);
    }

    private void saveIntoKeywordLinkQueue(String link, int agentId, int parentId) {
        keywordLinkQueueDao.saveIfNotExist(link, agentId, parentId);
    }

    private int getStockKeywordTypeBy(String pageHtml, String keywordName) {
        boolean isStockKeyword = pageHtml.contains(DataCommon.getValueBy("StockKeywordCheck", parsers));
        if (!isStockKeyword) {
            boolean isStockRelatedKeyword = DataCommon.containValue(DataCommon.getValueBy("StockKeywordTypeCheck", parsers), keywordName);
            if (isStockRelatedKeyword)
                return STOCK_RELATED_KEYWORD;
            else
                return NOT_A_STOCK_KEYWORD;
        } else {
            return STOCK_KEYWORD;
        }
    }
}
