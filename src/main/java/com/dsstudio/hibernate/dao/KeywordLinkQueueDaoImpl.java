package com.dsstudio.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.KeywordLinkQueue;

public class KeywordLinkQueueDaoImpl extends AbstractDao<Integer, KeywordLinkQueue>implements KeywordLinkQueueDao {

	public KeywordLinkQueue findByLink(String link) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("link",link));
		KeywordLinkQueue keywordLinkQueue = (KeywordLinkQueue)crit.uniqueResult();
		tx.commit();
		return keywordLinkQueue;
	}
	
	public void save(KeywordLinkQueue entity) {
		// TODO Auto-generated method stub
		super.persist(entity);
	}
	
}
