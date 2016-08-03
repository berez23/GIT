package it.webred.mui.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	public static final SessionFactory sessionFactory;
	static {
		SessionFactory sessionFactoryTmp = null;
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			// sessionFactory = new Configuration().configure(new
			// java.io.File("/home/franci/proj/MUI-WEB/hibernate.ant-cfg.xml")).buildSessionFactory();
			sessionFactoryTmp = new Configuration()
					.configure("/hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			try {
				// Create the SessionFactory from hibernate.cfg.xml
				// sessionFactory = new Configuration().configure(new
				// java.io.File("/home/franci/proj/MUI-WEB/hibernate.ant-cfg.xml")).buildSessionFactory();
				sessionFactoryTmp = new Configuration()
						.configure("/hibernate.batch-cfg.xml").buildSessionFactory();
			} catch (Throwable ex1) {
				// Make sure you log the exception, as it might be swallowed
				System.err.println("Initial SessionFactory creation failed." + ex1);
				throw new ExceptionInInitializerError(ex1);
			}
		}
		sessionFactory = sessionFactoryTmp;
	}

	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		// Open a new Session, if this thread has none yet
		if (s == null) {
			s = sessionFactory.openSession(new MuiInterceptor());
			// Store it in the ThreadLocal variable
			session.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		if (s != null)
			s.close();
		session.set(null);
	}
}
