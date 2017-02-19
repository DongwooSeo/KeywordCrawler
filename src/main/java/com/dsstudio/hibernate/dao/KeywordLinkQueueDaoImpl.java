package com.dsstudio.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.KeywordLinkQueue;


public class KeywordLinkQueueDaoImpl extends AbstractDao<Integer, KeywordLinkQueue>implements KeywordLinkQueueDao {

	public KeywordLinkQueue findByLink(String link) {
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("link",link));
		KeywordLinkQueue keywordLinkQueue = (KeywordLinkQueue)crit.uniqueResult();
		// TODO Auto-generated method stub
		tx.commit();
		
		return keywordLinkQueue;
	}
	
	public void save(KeywordLinkQueue entity) {
		// TODO Auto-generated method stub
		super.persist(entity);
	}

	public void saveAll(List<KeywordLinkQueue> keywordLinkQueues) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		
		for(int i=0; i<keywordLinkQueues.size(); i++){
			KeywordLinkQueue keywordLinkQueue = keywordLinkQueues.get(i);
			session.save(keywordLinkQueue);
			if(i%keywordLinkQueues.size()-1==0){
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
	public KeywordLinkQueue fetchFirstRow() {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Query query = getSession().createSQLQuery("select * from KeywordLinkQueue a where Status = 1 or (BookingDate + INTERVAL 10 MINUTE < now() and status = 2) order by Id limit 1").addEntity(KeywordLinkQueue.class);
		List<KeywordLinkQueue> result = query.list();
		KeywordLinkQueue keywordLinkQueue = null;
		
		if(!result.isEmpty()){
			keywordLinkQueue = result.get(0);
		}
		
		tx.commit();
		return keywordLinkQueue;
	}
	
}
