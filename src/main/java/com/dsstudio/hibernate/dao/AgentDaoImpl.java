package com.dsstudio.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.dsstudio.hibernate.model.Agent;

public class AgentDaoImpl extends AbstractDao<Integer, Agent> implements AgentDao {

	public Agent findById(int id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}

	public Agent findByName(String name) {
		// TODO Auto-generated method stub
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name", name));
		return (Agent) crit.uniqueResult();
	}	
	
    @Override
    public void update(Agent entity) {
    	// TODO Auto-generated method stub
    	super.update(entity);
    }
	
	public static void main(String[] args){
		/*
		AgentDao dao = new AgentDaoImpl();
		
		Agent agent = dao.findByIndex(1);
		System.out.println(String.format(agent.getAgentConfig().getSearchQuery(), "test"));
		
		
		Timestamp timestamp = new Timestamp(new Date().getTime());
		agent.setDateFinished(timestamp);
		dao.update(agent);
		*/
		/*
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		c1.add(Calendar.HOUR, -1*3);
		Timestamp timestamp = new Timestamp(c1.getTime().getTime());
		System.out.println(timestamp.after(null));*/
		
	
	}
}
