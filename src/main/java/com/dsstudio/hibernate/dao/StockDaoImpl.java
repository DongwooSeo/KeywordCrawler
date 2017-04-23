package com.dsstudio.hibernate.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.Stock;

public class StockDaoImpl extends AbstractDao<Integer, Stock> implements StockDao{


	public void upsertStock(int keywordId, String name, String code, int price, int pricePrev, int priceMax,
			int priceMin, int priceFluct, double priceFluctRate, String chartDailyUrl, String chartWeeklyUrl,
			String chartMonthlyUrl) {
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(Stock.class);
			crit.add(Restrictions.eq("keywordId", keywordId));
			Stock entityStock = (Stock) crit.setMaxResults(1).uniqueResult();
			if (entityStock == null) {
				Stock stock = new Stock();
				stock.setKeywordId(keywordId);
				stock.setName(name);
				try{
					stock.setCode(Integer.parseInt(code));
				}catch(NumberFormatException e){
					//e.printStackTrace();
					System.out.println("StockCode에 문자포함("+stock.getName()+")");
				}
				stock.setPrice(price);
				stock.setPricePrev(pricePrev);
				stock.setPriceMax(priceMax);
				stock.setPriceMin(priceMin);
				stock.setFluct(priceFluct);
				stock.setFluctRate(priceFluctRate);
				stock.setChartDaily(chartDailyUrl);
				stock.setChartWeekly(chartWeeklyUrl);
				stock.setChartMonthly(chartMonthlyUrl);
				stock.setDateCreated(new Timestamp(new Date().getTime()));
				stock.setDateUpdated(new Timestamp(new Date().getTime()));
				session.save(stock);
			}else{
				entityStock.setPrice(price);
				entityStock.setPricePrev(pricePrev);
				entityStock.setPriceMax(priceMax);
				entityStock.setPriceMin(priceMin);
				entityStock.setFluct(priceFluct);
				entityStock.setFluctRate(priceFluctRate);
				entityStock.setChartDaily(chartDailyUrl);
				entityStock.setChartWeekly(chartWeeklyUrl);
				entityStock.setChartMonthly(chartMonthlyUrl);
				entityStock.setDateUpdated(new Timestamp(new Date().getTime()));
				session.save(entityStock);
			}
			// TODO Auto-generated method stub
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("rollback!!");
		}
		// TODO Auto-generated method stub
	}
}
