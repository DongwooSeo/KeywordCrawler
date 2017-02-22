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

import com.dsstudio.hibernate.model.KeywordLinkQueue;
import com.dsstudio.hibernate.model.KeywordMain;

public class KeywordMainDaoImpl extends AbstractDao<Integer, KeywordMain> implements KeywordMainDao{

	public KeywordMain findByName(String name) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name", name));
		KeywordMain keywordMain = (KeywordMain) crit.uniqueResult();
		tx.commit();
		return keywordMain;
	}

	public int save(KeywordMain keywordMain) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		int id = (Integer) getSession().save(keywordMain);
		tx.commit();
		return id;
	}
	
	public void update(KeywordMain keywordMain){
		super.update(keywordMain);
	}

	public int upsertKeywordMain(String keywordName){
		// TODO Auto-generated method stub
		int keywordMainId = 0;
		
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		
		/*
		Criteria crit = session.createCriteria(KeywordMain.class);
		crit.add(Restrictions.eq("name", keywordName));
		crit.setLockMode
		*/
		Query query = session.createQuery("from KeywordMain b where name=:keywordName");
		query.setParameter("keywordName", keywordName);
		query.setLockMode("b",LockMode.PESSIMISTIC_WRITE);
		query.setMaxResults(1);
		
		List<KeywordMain> result = query.list();
		KeywordMain entityKeywordMain = null;

		if (!result.isEmpty()) {
			entityKeywordMain = result.get(0);
		}
		
		if(entityKeywordMain==null){
			KeywordMain keywordMain = new KeywordMain();
			keywordMain.setName(keywordName);
			keywordMain.setDateCreated(new Timestamp(new Date().getTime()));
			keywordMain.setDateUpdated(new Timestamp(new Date().getTime()));
			if (!keywordMain.getName().isEmpty()){
				System.out.println(keywordMain.getName());
				keywordMainId = (Integer) session.save(keywordMain);
			}
				
		}else{
			entityKeywordMain.setDateUpdated(new Timestamp(new Date().getTime()));
			keywordMainId = entityKeywordMain.getId();
			session.update(entityKeywordMain);
		}
		try{
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}
		
		return keywordMainId;
	}
}
