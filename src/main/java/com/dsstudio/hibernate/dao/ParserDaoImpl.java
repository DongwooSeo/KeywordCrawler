package com.dsstudio.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.Parser;

public class ParserDaoImpl extends AbstractDao<Integer, Parser> implements ParserDao{
	@SuppressWarnings("unchecked")
	public List<Parser> findByAgentId(int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("agentId", agentId));
		List<Parser> parsers = (List<Parser>)crit.list();
		tx.commit();
		return parsers;
	}
}
