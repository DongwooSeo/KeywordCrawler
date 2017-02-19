package com.dsstudio.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.RealtimeKeyword;

public class RealtimeKeywordDaoImpl extends AbstractDao<Integer, RealtimeKeyword> implements RealtimeKeywordDao{

	public RealtimeKeyword findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public RealtimeKeyword findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RealtimeKeyword findByNameAndAgentId(String name, int agentId){
		Transaction tx = getSession().beginTransaction();
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name",name));
		crit.add(Restrictions.eq("agentId", agentId));
		RealtimeKeyword realtimeKeyword = (RealtimeKeyword)crit.uniqueResult();
		tx.commit();
		return realtimeKeyword;
	}
	
	public void save(RealtimeKeyword entity) {
		// TODO Auto-generated method stub
		super.persist(entity);
	}
	
	public void saveAll(List<RealtimeKeyword> realtimeKeywords){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		
		for(int i=0; i<realtimeKeywords.size(); i++){
			RealtimeKeyword realtimeKeyword = realtimeKeywords.get(i);
			session.save(realtimeKeyword);
			if(i%realtimeKeywords.size()-1==0){
				session.flush();
				session.clear();
			}		
		}
		tx.commit();
	}
	@Override
    public void update(RealtimeKeyword entity) {
    	// TODO Auto-generated method stub
    	super.update(entity);
    }
	
	public void deleteAllByAgentId(int agentId) {
		// TODO Auto-generated method stub
		Transaction tx = getSession().beginTransaction();
		getSession().createQuery("delete from RealtimeKeyword where AgentId = (:agentId)").setString("agentId", agentId+"").executeUpdate();
		tx.commit();
		
	}
	
	@Override
	public void saveOrUpdate(RealtimeKeyword entity){
		super.update(entity);
	}

}
