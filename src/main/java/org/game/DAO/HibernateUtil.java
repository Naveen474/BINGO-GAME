package org.game.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	//trying to implement singleton class
	//create only one session factory and object cannot access the build session factory
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
		}catch(Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	//return the session factory for the other class who want to use it
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
