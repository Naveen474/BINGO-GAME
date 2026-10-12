package org.game.DAO;

import org.game.entity.GameSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GameSessionDAO {
	
	//only for maintaining the game session like  IN_PROGRESS or FINISHED
	//refer UserDAO.java for the code work
	public void saveGameSession(GameSession gsession) {
		Transaction tx = null;
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.persist(gsession);
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
