package it.webred.diogene.db;

import java.util.HashMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSession
{
	/** Read the configuration, will share across threads**/
	private static HashMap<String, SessionFactory> sessionFactoryMap;
	public static final String  DEFAULT_SESSION_FACTORY_CONF_FILE = "hibernate.cfg.xml";
	/** the per thread session **/
	private static final ThreadLocal<HashMap<String,Session>> currentSessionMap = new ThreadLocal<HashMap<String,Session>>();
	/** The constants for describing the ownerships **/
	private static final Owner trueOwner = new Owner(true);
	private static final Owner fakeOwner = new Owner(false);

	/**
	 * get the hibernate session and set it on the thread local. Returns trueOwner if 
	 * it actually opens a session
	 */
	@SuppressWarnings("unchecked")
	public static Object createSession(String...whichFactoryConfFile ) throws Exception
	{
		HashMap<String,Session> sessionMap= currentSessionMap.get();
		if(sessionMap==null){
			sessionMap=new HashMap<String,Session>();
		}
		Session session=null;
		String theFactoryConfFile=DEFAULT_SESSION_FACTORY_CONF_FILE;
		if(whichFactoryConfFile.length==1){
			theFactoryConfFile=whichFactoryConfFile[0];
		}
		session= (Session) sessionMap.get(theFactoryConfFile);
		
		//System.out.println(session);
		if (session == null)
		{
			//System.out.println("No Session Found - Create and give the identity");
			session = getSessionFactory(theFactoryConfFile).openSession();
			sessionMap.put(theFactoryConfFile,session);
			currentSessionMap.set(sessionMap);
			return trueOwner;
		}
		return fakeOwner;
	}
	/**
	 * The method for closing a session. The close  and flush 
	 * will be executed only if the session is actually created
	 * by this owner.  
	 */
	@SuppressWarnings("unchecked")
	public static void closeSession(Object ownership, String...whichFactoryConfFile) 
	{
		if (ownership!=null && ((Owner) ownership).identity)
		{
			//System.out.println("Identity is accepted. Now closing the session");
			HashMap<String,Session> sessionMap = currentSessionMap.get();

			Session session=null;
			String theFactoryConfFile=DEFAULT_SESSION_FACTORY_CONF_FILE;
			if(whichFactoryConfFile.length==1){
				theFactoryConfFile=whichFactoryConfFile[0];
			}
			session= (Session) sessionMap.get(theFactoryConfFile);
			session.flush();
			session.close();
			sessionMap.remove(theFactoryConfFile);
			
			currentSessionMap.set(sessionMap);
		}
		else
		{
			//System.out.println("Identity is rejected. Ignoring the request");
		}
	}
	/**
	 * returns the current session
	 */
	public static Session getSession(String...whichFactoryConfFile) throws HibernateException
	{
		HashMap<String,Session> sessionMap = currentSessionMap.get();
		Session session=null;
		String theFactoryConfFile=DEFAULT_SESSION_FACTORY_CONF_FILE;
		if(whichFactoryConfFile.length==1){
			theFactoryConfFile=whichFactoryConfFile[0];
		}
		session= (Session) sessionMap.get(theFactoryConfFile);
		
		return session;
	}
	/** 
	 * Creating a session factory , if not already loaded
	 */
	private static SessionFactory getSessionFactory(String...whichFactoryConfFile) throws HibernateException
	{
		try
		{
			if(sessionFactoryMap==null){
				sessionFactoryMap=new HashMap<String,SessionFactory>();
			}
			String theFactoryConfFile = DEFAULT_SESSION_FACTORY_CONF_FILE;
			if(whichFactoryConfFile.length==1){
				theFactoryConfFile=whichFactoryConfFile[0];
			}

			
			SessionFactory sessionFactory=sessionFactoryMap.get(theFactoryConfFile);
			
			if (sessionFactory == null)
			{
				sessionFactory = new Configuration().configure(theFactoryConfFile).buildSessionFactory();
				sessionFactoryMap.put(theFactoryConfFile,sessionFactory);
			}
			return sessionFactory;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new HibernateException("Error getting Factory");
		}
	}

	/**
	 * Internal class , for handling the identity. Hidden for the 
	 * developers
	 */
	private static class Owner
	{
		public Owner(boolean identity)
		{
			this.identity = identity;
		}

		boolean identity = false;
	}
}