package com.dsstudio.hibernate.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class KeywordLinkQueueDaoImpl extends AbstractDao<Integer, KeywordLinkQueue> implements KeywordLinkQueueDao {

	public KeywordLinkQueue findByLink(String link) {
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("link", link));
		KeywordLinkQueue keywordLinkQueue = (KeywordLinkQueue) crit.setMaxResults(1).uniqueResult();
		// TODO Auto-generated method stub
		tx.commit();

		return keywordLinkQueue;
	}

	public void save(KeywordLinkQueue entity) {
		// TODO Auto-generated method stub
		super.persist(entity);
	}

	public void saveIfNotExist(String link, int agentId, int parentId) {

		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(KeywordLinkQueue.class);
			crit.add(Restrictions.eq("link", link));
			crit.add(Restrictions.eq("parentId", parentId));

			KeywordLinkQueue entityKeywordLinkQueue = (KeywordLinkQueue) crit.setMaxResults(1).uniqueResult();

			if (entityKeywordLinkQueue == null) {
				KeywordLinkQueue keywordLinkQueue = new KeywordLinkQueue();
				keywordLinkQueue.setLink(link);
				keywordLinkQueue.setStatus(1);
				keywordLinkQueue.setDateCreated(new Timestamp(new Date().getTime()));
				keywordLinkQueue.setAgentId(agentId);
				keywordLinkQueue.setParentId(parentId);
				session.save(keywordLinkQueue);
			}
			// TODO Auto-generated method stub
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("rollback!!");
		}

	}

	public void saveAll(List<KeywordLinkQueue> keywordLinkQueues) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = session.beginTransaction();

		for (int i = 0; i < keywordLinkQueues.size(); i++) {
			KeywordLinkQueue keywordLinkQueue = keywordLinkQueues.get(i);
			session.save(keywordLinkQueue);
			if (i % keywordLinkQueues.size() - 1 == 0) {
				session.flush();
				session.clear();
			}
		}
		tx.commit();

	}

	@Override
	public void update(KeywordLinkQueue entity) {
		// TODO Auto-generated method stub
		super.update(entity);
	}

	/*
	 * ( PESSIMISTIC_WRITE, Lock Mode를 사용하여 select for update시 테이블에 락을 생성합니다. //
	 * concurrency blocking)
	 * 
	 * @see com.dsstudio.hibernate.dao.KeywordLinkQueueDao#fetchFirstRow()
	 */
	public KeywordLinkQueue fetchFirstRow() {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;
		
		KeywordLinkQueue keywordLinkQueue = null;
		try {
			tx = session.beginTransaction();
			/*
			 * Query query = getSession() .createSQLQuery(
			 * "select * from KeywordLinkQueue a where Status = 1 or (BookingDate + INTERVAL 10 MINUTE < now() and status = 2) order by Id limit 1"
			 * ) .addEntity(KeywordLinkQueue.class);
			 */
			Query query = session.createQuery(
					"from KeywordLinkQueue a where Status = 1 or (BookingDate < :time1 and status = 2) or (BookingDate < :time2 and status = 3) order by status, Id");
			query.setParameter("time1", new Timestamp(new Date().getTime() - 10 * 60 * 1000));
			query.setParameter("time2", new Timestamp(new Date().getTime() - 1 * 24 * 60 * 60 * 1000));
			query.setLockMode("a", LockMode.PESSIMISTIC_WRITE);
			query.setMaxResults(1);

			List<KeywordLinkQueue> result = query.list();
			

			if (!result.isEmpty()) {
				keywordLinkQueue = result.get(0);
			}
			if (keywordLinkQueue != null) {
				keywordLinkQueue.setStatus(2);
				keywordLinkQueue.setBookingDate(new Timestamp(new Date().getTime()));
				session.update(keywordLinkQueue);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("rollback");
		}

		return keywordLinkQueue;
	}

}
