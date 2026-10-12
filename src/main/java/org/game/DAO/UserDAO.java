package org.game.DAO;

import org.game.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;


//DAO is like a intermediary b/w application's core logic & database
//DAO is more over like a Encapsulation
public class UserDAO {
	
	//boiler plate code is written here to minimize the work of inserting user into database
	
	public void saveUser(User user) {
		Transaction tx = null; //both transaction and session declared null because  incase error 
		Session session = null; //happened we can simply roll back and close the session to prevent data leakage
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.persist(user); //persist -> insert data of uuser
			tx.commit(); //making permanent change & cannot roll back
		}catch(Exception e) {
			e.printStackTrace();
			if(tx!=null && tx.isActive()) {
				try {
					tx.rollback();
				}catch (Exception ex) {
					ex.printStackTrace(); // print the exception which is occured
				}
			}
			
		}finally {
			if(session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
