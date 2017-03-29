package com.dsstudio.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dsstudio.hibernate.util.HibernateUtil;


public abstract class AbstractDao <PK extends Serializable, T>{
	private final Class<T> persistentClass;
	private SessionFactory sessionFactory;
	
	public AbstractDao(){
		this.persistentClass = (Class<T>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	protected Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public T getByKey(PK key){
		Transaction tx = getSession().beginTransaction();
		T obj = (T)getSession().get(persistentClass, key);
		tx.commit();
		return obj;
	}
	
	public void persist(T entity){
		Transaction tx = getSession().beginTransaction();
		getSession().persist(entity);
		try{
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}
	}
	
	public void update(T entity){
		Transaction tx = getSession().beginTransaction();
		getSession().update(entity);
		tx.commit();
	}
	
	public void saveOrUpdate(T entity){
		Transaction tx = getSession().beginTransaction();
		getSession().saveOrUpdate(entity);
		tx.commit();
	}
	
	public void delete(T entity){
		Transaction tx = getSession().beginTransaction();
		getSession().delete(entity);
		tx.commit();
	}
	
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(persistentClass);
	}

}
