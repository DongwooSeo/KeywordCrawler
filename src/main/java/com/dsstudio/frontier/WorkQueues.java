package com.dsstudio.frontier;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.dsstudio.hibernate.util.HibernateUtil;
import com.dsstudio.url.WebURL;

public class WorkQueues<T> {
	
	private final Class<T> persistentClass;

	
	private List<T> urlsDBs;
	
	private Session session;
	
	private String tableName;
	
	protected final Object mutex = new Object();
	
	
	@SuppressWarnings("unchecked")
	public WorkQueues(){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];	
	
	}

	protected Transaction beginTransaction(){
		return session.beginTransaction();
		
	}
	protected static void commit(Transaction tx){
		if(tx != null)
			tx.commit();
	}
	
	public List<T> get(int max){
		synchronized(mutex){
			//List<WebURL> results = new ArrayList<WebURL>(max);
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(getPersistentClass());
			List<T> results = crit.list();
			commit(tx);
			
			return results;
		}
	}
	
	public void put(T object){
		synchronized(mutex){
			Transaction tx = session.beginTransaction();
			session.save(object);
			commit(tx);
		}
	}
	
	public void delete(T object){
		synchronized(mutex){
			Transaction tx = session.beginTransaction();
			session.delete(object);
			commit(tx);
		}
	}
	
	public void update(T object){
		synchronized(mutex){
			Transaction tx = session.beginTransaction();
			session.update(object);
			commit(tx);
		}
	}
	
	public int getLength(){
		return urlsDBs.size();
	}

	public Class<T> getPersistentClass(){
		return persistentClass;
	}
}
