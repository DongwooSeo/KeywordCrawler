package com.dsstudio.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


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
}
