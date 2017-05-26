package com.dsstudio.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.RelatedKeywordLink;

public class RelatedKeywordLinkDaoImpl extends AbstractDao<Integer, RelatedKeywordLink>
		implements RelatedKeywordLinkDao {

	public void save(RelatedKeywordLink relatedKeywordLink) {
		// TODO Auto-generated method stub
		super.persist(relatedKeywordLink);
	}

	public RelatedKeywordLink findByKeywordId(int keywordId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("keywordId", keywordId));
		RelatedKeywordLink relatedKeywordLink = (RelatedKeywordLink) crit.uniqueResult();
		tx.commit();
		return relatedKeywordLink;
	}

	public RelatedKeywordLink findByKeywordAndRelatedId(int keywordId, int relatedId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("keywordId", keywordId));
		crit.add(Restrictions.eq("relatedId", relatedId));
		RelatedKeywordLink relatedKeywordLink = (RelatedKeywordLink) crit.uniqueResult();
		tx.commit();
		return relatedKeywordLink;
	}

	public void saveAll(List<RelatedKeywordLink> relatedKeywordLinks) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = session.beginTransaction();

		for (int i = 0; i < relatedKeywordLinks.size(); i++) {
			RelatedKeywordLink keywordLinkQueue = relatedKeywordLinks.get(i);
			session.save(keywordLinkQueue);
			if (i % relatedKeywordLinks.size() - 1 == 0) {
				session.flush();
				session.clear();
			}
		}
		tx.commit();
	}

	public void saveRelatedKeyword(int keywordId, int relatedId, int correl) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria crit = session.createCriteria(RelatedKeywordLink.class);
			crit.add(Restrictions.eq("keywordId", keywordId));
			crit.add(Restrictions.eq("relatedId", relatedId));

			RelatedKeywordLink entityRelatedKeywordLink = (RelatedKeywordLink) crit.setMaxResults(1).uniqueResult();

			if (entityRelatedKeywordLink == null) {
				entityRelatedKeywordLink = new RelatedKeywordLink();
				entityRelatedKeywordLink.setKeywordId(keywordId);
				entityRelatedKeywordLink.setRelatedId(relatedId);
				entityRelatedKeywordLink.setCorrel(correl);
				session.save(entityRelatedKeywordLink);
			}else{
				entityRelatedKeywordLink.setCorrel(correl);
				session.update(entityRelatedKeywordLink);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}

	}

}
