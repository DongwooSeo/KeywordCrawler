package com.dsstudio.hibernate.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.KeywordMain;

public class KeywordMainDaoImpl extends AbstractDao<Integer, KeywordMain> implements KeywordMainDao {

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

	public void update(KeywordMain keywordMain) {
		super.update(keywordMain);
	}

	public int upsertKeywordMain(String keywordName) {
		// TODO Auto-generated method stub
		int keywordMainId = 0;

		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria crit = session.createCriteria(KeywordMain.class);
			crit.add(Restrictions.eq("name", keywordName));
			KeywordMain entityKeywordMain = (KeywordMain) crit.setMaxResults(1).uniqueResult();

			if (entityKeywordMain == null) {
				entityKeywordMain = new KeywordMain();
				entityKeywordMain.setName(keywordName);
				entityKeywordMain.setDateCreated(new Timestamp(new Date().getTime()));
				entityKeywordMain.setDateUpdated(new Timestamp(new Date().getTime()));
				if (!entityKeywordMain.getName().isEmpty()) {
					// System.out.println(keywordMain.getName());
					keywordMainId = (Integer) session.save(entityKeywordMain);
				}
			} else {
				entityKeywordMain.setDateUpdated(new Timestamp(new Date().getTime()));
				keywordMainId = entityKeywordMain.getId();
				session.update(entityKeywordMain);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("rollback!!");
		}

		return keywordMainId;
	}
}
