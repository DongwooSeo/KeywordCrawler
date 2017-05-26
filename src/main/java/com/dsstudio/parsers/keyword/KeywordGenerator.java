package com.dsstudio.parsers.keyword;

import com.dsstudio.hibernate.dao.KeywordMainDao;
import com.dsstudio.hibernate.dao.KeywordMainDaoImpl;
import com.dsstudio.hibernate.dao.StockKeywordDao;
import com.dsstudio.hibernate.dao.StockKeywordDaoImpl;

/**
 * Created by DongwooSeo on 2017-05-23.
 */
public class KeywordGenerator {
    private KeywordMainDao keywordMainDao;

    public KeywordGenerator() {
        keywordMainDao = new KeywordMainDaoImpl();
    }

    public int stockKeywordGenerator(String keywordName, String link, int agentId, int typeId) {
        StockKeywordDao stockKeywordDao = new StockKeywordDaoImpl();
        int keywordMainId = keywordMainDao.saveKeywordMain(keywordName);
        if (keywordMainId == 0) {
            return 0;
        }
        int stockKeywordId = stockKeywordDao.saveKeyword(keywordName, link, keywordMainId, agentId, typeId);
        return stockKeywordId;
    }

    public int normalKeywordGenerator(String keywordName, String link, int agentId){
        return 0;
    }
}
