package it.webred.diogene.db;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.hibernate.SessionFactory;

public class ConnectionBean {
	protected String connectionName;
	protected SessionFactory sessF;
	protected GenericObjectPool connectionPool;
	
	public ConnectionBean(String connectionName,SessionFactory sessf,GenericObjectPool connectionPool) {
		this.connectionName = connectionName;
		this.sessF = sessf;
		this.connectionPool = connectionPool;
	}
	
}
