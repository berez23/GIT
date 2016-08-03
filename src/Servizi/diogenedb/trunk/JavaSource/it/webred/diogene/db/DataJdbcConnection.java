package it.webred.diogene.db;




import it.webred.diogene.db.model.DcMetadataConnection;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.xml.parsers.*;
import org.w3c.dom.*;


public class DataJdbcConnection
{
	private static Hashtable<Object,Object> connectionPools = new Hashtable<Object,Object>();
	private static DcMetadataConnection defaultMdc = null;
	/*
	 * Aggiunta Filippo 21.06.06
	 * Le connessioni hanno includeSynonyms = "true", questo permette la lettura dei metadati delle colonne dei sinonimi
	 * ma blocca la lettura dei metadati delle colonne di altre tabelle.
	 * Nel caso in cui getColumns() di AbstractMetadataFactory mi restituisca una lista vuota, da là leggo questa
	 * HashMap e tento una connessione temporanea con includeSynonyms = "false" (e le altre proprietà uguali a quelle
	 * della connessione scelta in partenza).
	 */
	public static HashMap<String, Properties> savedProps = new HashMap<String, Properties>();



	public static Session getHibernateSession(String database) {
		try {			
			SessionFactory sessF = ((ConnectionBean) connectionPools.get(database)).sessF;
			return sessF.openSession();
		} catch (Exception e1)
		{
			throw new RuntimeException(e1);
		}
	}
	
	static
	{
		Object owner = null;
		try
		{

			//connectionPools = new Hashtable();
			//HashSet listDB = new HashSet();
			
			boolean defaultConn = false;
			owner = HibernateSession.createSession();
			Session session = HibernateSession.getSession();
			Query q = session.createQuery("from DC_METADATA_CONNECTION in class DcMetadataConnection");
			
			List conns = q.list();
			Document doc = null;
			for (int i = 0; i < conns.size(); i++)
			{
				DcMetadataConnection mdc = (DcMetadataConnection) conns.get(i);

				// todo qui valorizzo tutte le variabili prendendo i dati dalla lista
				String driverClass = mdc.getDriverClass();
				String connString = mdc.getConnString();
				String connectionName = mdc.getName();
				String userName = mdc.getUserName();
				String password = mdc.getPassword();

					ByteArrayInputStream is = new ByteArrayInputStream(mdc.getHibernateMapping());
					 // Create a builder factory
	               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	               factory.setValidating(false);
	   
	               // Create the builder and parse the file
	               doc = factory.newDocumentBuilder().parse(is);

					Configuration cfg =  new Configuration().configure(doc); 
					SessionFactory sessF = cfg.buildSessionFactory();
				
				
				if (connectionName.equals("DEFAULT")) {
					defaultConn = true;
					defaultMdc = mdc;
				}
				
				try
				{
					Class.forName(driverClass);
				}
				catch (Exception e)
				{
					throw e;
				}
				GenericObjectPool connectionPool;
				connectionPool = new GenericObjectPool(null);
				connectionPool.setMaxActive(8);
				connectionPool.setMaxIdle(-1);
				connectionPool.setTestOnBorrow(true);
				connectionPool.setTestOnReturn(true);
				connectionPool.setMaxWait(5000);

				connectionPool.setTestWhileIdle(true);
				connectionPool.setTimeBetweenEvictionRunsMillis(1000);
				connectionPool.setNumTestsPerEvictionRun(4);
				connectionPool.setMinEvictableIdleTimeMillis(1000 * 3);

				connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
				@SuppressWarnings("unused")
				ConnectionFactory connectionFactory = null;
				
				/////////////////////////////////////////////////////////////////////////
				//modificato Filippo 23.05.06 per consentire la lettura delle colonne dei sinonimi (Oracle)
				//verificare...
				/*if (userName != null)
					connectionFactory = new DriverManagerConnectionFactory(connString, userName, password);
				else
					connectionFactory = new DriverManagerConnectionFactory(connString, null);*/
				
				//sostituito con:
				
				try {
					Properties props = new Properties();
					if (userName != null) {
						props.put("user", userName);
						props.put("password", password);
					}
					props.put("includeSynonyms", "true");
					connectionFactory = new DriverManagerConnectionFactory(connString, props);
					// aggiunto Filippo 21.06.06
					Properties props1 = (Properties)props.clone();
					props1.put("includeSynonyms", "false");
					savedProps.put(connectionName, props1);
					// fine aggiunto Filippo 21.06.06
				}catch (Exception e) {
					//in caso di eccezione effettua le connessioni come da codice sostituito (v. sopra)
					//verificare anche qui...
					if (userName != null)
						connectionFactory = new DriverManagerConnectionFactory(connString, userName, password);
					else
						connectionFactory = new DriverManagerConnectionFactory(connString, null);
				}
				//fine modificato Filippo 23.05.06 
				//////////////////////////////////////////////////////////////////////////
				
				@SuppressWarnings("unused") PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
				@SuppressWarnings("unused") PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
				//System.out.println("CONNECTION NAME = "+connectionName);

				ConnectionBean connBean = new ConnectionBean(connectionName,sessF,connectionPool);
				new PoolingDriver().registerPool(connectionName, connBean.connectionPool);
				
				connectionPools.put(connectionName,connBean);

			}
			if (!defaultConn)
				throw new RuntimeException("Nessuna connessione di DEFAULT definita!");
				

		}
		catch (Exception e1)
		{
			throw new RuntimeException(e1);
		}
		finally
		{
			HibernateSession.closeSession(owner);
		}

	}

	public static Connection getConnectionn() throws Exception
	{
		return getConnectionn("DEFAULT");
	}

		/**
	 * restituisce una connessione sul database richiesto da parametro
	 * @param  database
	 * @return Connection
	 * @throws Exception
	 * @exception
	 */
	public static Connection getConnectionn(String database) throws Exception
	{
		Connection conn = null;
		try
		{
			if (database!=null) {
				GenericObjectPool gop = ((ConnectionBean) connectionPools.get(database)).connectionPool;
				gop.evict();
				//System.out.println("GET CONNECTION"+database);
				//conn = (Connection) gop.borrowObject();
				conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + database);
			} else {
				conn= getConnectionn();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("Pooler - Impossibile ottenere una connessione al database " + database);
		}
		return conn;
	}

	public static DcMetadataConnection getDefaultMdc() {
		return defaultMdc;
	}

	public static void setDefaultMdc(DcMetadataConnection defaultMdc) {
		DataJdbcConnection.defaultMdc = defaultMdc;
	}

}