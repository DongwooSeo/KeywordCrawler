package com.dsstudio.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.Keyword;

public class KeywordDaoImpl extends AbstractDao<Integer, Keyword> implements KeywordDao {

	public int save(Keyword keyword) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		int id = (Integer) getSession().save(keyword);
		tx.commit();
		return id;
	}
	
	public void update(Keyword keyword){
		super.update(keyword);
	}
	public Keyword findByKwdMainIdAndAgentId(int keywordMainId, int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("keywordMainId",keywordMainId));
		crit.add(Restrictions.eq("agentId",agentId));
		Keyword keyword = (Keyword)crit.uniqueResult();
		tx.commit();
		return keyword;
	}

	public Keyword findByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name",name));
		crit.add(Restrictions.eq("agentId",agentId));
		Keyword keyword = (Keyword)crit.uniqueResult();
		tx.commit();
		return keyword;
	}


}
