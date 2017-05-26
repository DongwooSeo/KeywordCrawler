package com.dsstudio.parsers.keyword;

import com.dsstudio.helper.DataCommon;
import com.dsstudio.hibernate.dao.*;
import com.dsstudio.hibernate.model.Agent;
import com.dsstudio.hibernate.model.AgentConfig;
import com.dsstudio.hibernate.model.Parser;
import com.dsstudio.parsers.stock.StockParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

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
    private KeywordGenerator keywordGenerator;

    public NaverStockKeywordParser(int agentId, StockParser stockParser, KeywordGenerator keywordGenerator) {
        // TODO Auto-generated constructor stub
        this.agent = new AgentDaoImpl().findById(agentId);
        this.parsers = new ParserDaoImpl().findByAgentId(agentId);
        this.keywordLinkQueueDao = new KeywordLinkQueueDaoImpl();
        this.relatedKeywordLinkDao = new RelatedKeywordLinkDaoImpl();
        this.stockKeywordDao = new StockKeywordDaoImpl();
        this.keywordMainDao = new KeywordMainDaoImpl();
        this.stockParser = stockParser;
        this.keywordGenerator = keywordGenerator;
    }

    public String parseKeyword(Document document){
        return document.select(DataCommon.getValueBy("KeywordField", parsers))
                .attr(DataCommon.getValueBy("KeywordFieldAttr", parsers)).trim().replaceAll("주가", "")
                .replaceAll("주식", "");
    }

    public void parseRelatedKeywords(int parentId, AgentConfig agentConfig, int stockKeywordId, Document document) {
        relatedKeywordLinkDao.saveRelatedKeyword(parentId, stockKeywordId, 0);
        String relKeywordList = DataCommon.getValueBy("RKeywordList", parsers);
        String relKeywordItems = DataCommon.getValueBy("RKeywordListElem", parsers);

        Elements elements = document.select(relKeywordList).select(relKeywordItems);

        for (Element elem : elements) {
            String link = agentConfig.getSearchQuery() + elem.attr("href");
            keywordLinkQueueDao.saveIfNotExist(link, agent.getId(), stockKeywordId);
        }
    }

    public int generateKeyword(String keywordName, String link, int agentId, int keywordType) {
        return keywordGenerator.stockKeywordGenerator(keywordName, link, agentId, keywordType);
    }


    public int checkKeywordTypeBy(Document document, String keywordName) {
        String pageHtml = document.toString();
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
