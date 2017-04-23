package com.dsstudio.hibernate.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.Keyword;
import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.StockKeyword;

public class StockKeywordDaoImpl extends AbstractDao<Integer, StockKeyword> implements StockKeywordDao {

	public StockKeyword fetchFirstRow() {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;
		
		StockKeyword stockKeyword = null;
		try {
			tx = session.beginTransaction();
			/*
			 * Query query = getSession() .createSQLQuery(
			 * "select * from KeywordLinkQueue a where Status = 1 or (BookingDate + INTERVAL 10 MINUTE < now() and status = 2) order by Id limit 1"
			 * ) .addEntity(KeywordLinkQueue.class);
			 */
			Query query = session.createQuery(
					"from StockKeyword a where Status = 1 or (DateUpdated < :time and status = 2) or status = 3 order by status, DateUpdated");
			query.setParameter("time", new Timestamp(new Date().getTime() - 10 * 60 * 1000));
			query.setLockMode("a", LockMode.PESSIMISTIC_WRITE);
			query.setMaxResults(1);

			List<StockKeyword> result = query.list();
			if (!result.isEmpty()) {
				stockKeyword = result.get(0);
			}
			if (stockKeyword != null) {
				stockKeyword.setStatus(2);
				stockKeyword.setDateUpdated(new Timestamp(new Date().getTime()));
				session.update(stockKeyword);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("rollback");
		}
		return stockKeyword;
	}
	public int upsertKeyword(String keywordName, String link, int keywordMainId, int agentId, int typeId) {
		int stockKeywordId = 0;

		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(StockKeyword.class);
			crit.add(Restrictions.eq("keywordMainId", keywordMainId));
			crit.add(Restrictions.eq("agentId", agentId));

			StockKeyword entityKeyword = (StockKeyword) crit.setMaxResults(1).uniqueResult();

			if (entityKeyword == null) {
				entityKeyword = new StockKeyword();
				entityKeyword.setKeywordMainId(keywordMainId);
				entityKeyword.setAgentId(agentId);
				entityKeyword.setName(keywordName);
				entityKeyword.setLink(link);
				entityKeyword.setDateCreated(new Timestamp(new Date().getTime()));
				entityKeyword.setDateUpdated(new Timestamp(new Date().getTime()));
				entityKeyword.setTypeId(typeId);
				entityKeyword.setStatus(1);
				if (!entityKeyword.getName().isEmpty()) {
					// System.out.println(keyword.getName());
					stockKeywordId = (Integer) session.save(entityKeyword);
				}
			} else {
				entityKeyword.setDateUpdated(new Timestamp(new Date().getTime()));
				session.update(entityKeyword);
				stockKeywordId = entityKeyword.getId();
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Duplicate keywordId at = " + stockKeywordId + " / rollback()");
		}

		return stockKeywordId;
	}
	@Override
	public void update(StockKeyword entity) {
		// TODO Auto-generated method stub
		super.update(entity);
	}
	public boolean checkIfExist(String keywordName) {
		// TODO Auto-generated method stub
		boolean isExist = false;
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(StockKeyword.class);
			crit.add(Restrictions.eqOrIsNull("name", keywordName));
			StockKeyword entityKeyword = (StockKeyword) crit.uniqueResult();
			if (entityKeyword != null)
				isExist = true;
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			// System.out.println("Duplicate keywordId at = " + stockKeywordId +
			// " / rollback()");
		}
		return isExist;
	}

}
