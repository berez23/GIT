package it.webred.diogene.metadata.impl;

import java.sql.*;

/**
 * Estensione di AbstractMetadataFactory per la connessione a DB MySql.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class MySqlMetadataFactory extends AbstractMetadataFactory {

	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#registerDriver()
	 */
	public void registerDriver() throws Exception {
		//todo
		//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	}
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.metadata.MetadataFactory#getConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public Connection getConnection(String host,
				String port,
				String dbName,
				String username,
				String password,
				boolean registerDriver) throws Exception {
		//per il momento non è utilizzato, si fa la connessione con la stringa "secca"
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
		return getConnection(url, username, password, registerDriver);
	}
	
}
