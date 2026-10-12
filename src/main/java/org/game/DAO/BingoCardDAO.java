package org.game.DAO;

import org.game.entity.BingoCard;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BingoCardDAO {
	
	//it's for the BingoCard.java
	//saves 5*5 matrix and matrix that are the converted into string 
	//refer UserDAO.java for the code understandingf
	public void saveBingoCard(BingoCard card) {
		Transaction tx = null;
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.persist(card);
			tx.commit();
		}catch(Exception e) {
			if(tx!=null && tx.isActive()) {
				try {
					tx.rollback();
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		}finally {
			if(session != null && session.isOpen()) {
				session.close();
			}
		}
	}
}
