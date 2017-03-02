package com.dsstudio.hibernate.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
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

	public void update(Keyword keyword) {
		super.update(keyword);
	}

	public Keyword findByKwdMainIdAndAgentId(int keywordMainId, int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("keywordMainId", keywordMainId));
		crit.add(Restrictions.eq("agentId", agentId));
		Keyword keyword = (Keyword) crit.uniqueResult();
		tx.commit();
		return keyword;
	}

	public Keyword findByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("agentId", agentId));
		Keyword keyword = (Keyword) crit.uniqueResult();
		tx.commit();
		return keyword;
	}

	public int upsertKeyword(String keywordName, String link, int keywordMainId, int agentId) {
		// TODO Auto-generated method stub
		int keywordId = 0;

		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(Keyword.class);
			crit.add(Restrictions.eq("keywordMainId", keywordMainId));
			crit.add(Restrictions.eq("agentId", agentId));

			Keyword entityKeyword = (Keyword) crit.setMaxResults(1).uniqueResult();

			if (entityKeyword == null) {
				entityKeyword = new Keyword();
				entityKeyword.setKeywordMainId(keywordMainId);
				entityKeyword.setAgentId(agentId);
				entityKeyword.setName(keywordName);
				entityKeyword.setLink(link);
				entityKeyword.setDateCreated(new Timestamp(new Date().getTime()));
				entityKeyword.setDateUpdated(new Timestamp(new Date().getTime()));
				if (!entityKeyword.getName().isEmpty()) {
					// System.out.println(keyword.getName());
					keywordId = (Integer) session.save(entityKeyword);
				}
			} else {
				entityKeyword.setDateUpdated(new Timestamp(new Date().getTime()));
				session.update(entityKeyword);
				keywordId = entityKeyword.getId();
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Duplicate keywordId at = " + keywordId + " / rollback()");
		}

		return keywordId;
	}

}
