package it.webred.diogene.metadata;

import java.sql.*;
import java.util.*;
import it.webred.diogene.metadata.beans.*;

/**
 * Interfaccia che definisce dei metodi di utilità per la connessione al DB di origine dati e per il recupero di 
 * metadati da esso.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public interface MetadataFactory {
	/**
	 * Registrazione del driver appropriato (classe java) per il tipo del DB di origine.
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public void registerDriver() throws Exception;
	/**
	 * Recupero della connessione al DB di origine (oggetto Connection) per i parametri di connessione specificati.
	 * @param url La stringa di connessione
	 * @param username L'username
	 * @param password La password
	 * @param registerDriver Flag che indica se devo o meno effettuare la registrazione del driver java appropriato 
	 * per il tipo del DB di origine.
	 * @return Un oggetto Connection (connessione java al DB di origine)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public Connection getConnection(String url, String username, String password, boolean registerDriver) throws Exception;

	/**
	 * Recupero della connessione al DB di origine (oggetto Connection) per i parametri di connessione specificati.
	 * @param host Il nome dell'host
	 * @param port La porta
	 * @param dbName Il nome del database
	 * @param username L'username
	 * @param password La password
	 * @param registerDriver Flag che indica se devo o meno effettuare la registrazione del driver java appropriato 
	 * per il tipo del DB di origine.
	 * @return Un oggetto Connection (connessione java al DB di origine)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public Connection getConnection(String host, String port, String dbName, String username, String password, boolean registerDriver) throws Exception;
	/**
	 * Restituisce una lista di oggetti di classe Schema che rappresentano gli schemi del DB di origine, la connessione 
	 * al quale è passata a parametro.
	 * @param conn Un oggetto Connection (connessione java al DB di origine)
	 * @return una List di oggetti di classe Schema (schemi del DB di origine)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public List<Schema> getSchemas(Connection conn) throws Exception;
	/** 
	 * Restituisce una lista di oggetti di classe Table che rappresentano le tabelle del DB di origine (la connessione 
	 * al quale è passata a parametro) per catalog, nome schema (e nome tabella e types) passati a parametro.
	 * @param conn Un oggetto Connection (connessione java al DB di origine)
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param types I types
	 * @return una List di oggetti di classe Table (tabelle del DB di origine per i parametri passati)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public List<Table> getTables(Connection conn,
							String catalog,
							String schemaPattern,
							String tableNamePattern,
							String[] types) throws Exception;
	/**
	 * Restituisce una lista di oggetti di classe Column che rappresentano le colonne del DB di origine (la connessione 
	 * al quale è passata a parametro) per catalog, nome schema, nome tabella (e nome colonna) passati a parametro.
	 * @param conn Un oggetto Connection (connessione java al DB di origine)
	 * @param catalog Il nome del catalog
	 * @param schemaPattern Il nome dello schema
	 * @param tableNamePattern Il nome della tabella
	 * @param columnNamePattern Il nome della colonna
	 * @return una List di oggetti di classe Column (colonne del DB di origine per i parametri passati)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public List<Column> getColumns(Connection conn,
							String catalog,
							String schemaPattern,
							String tableNamePattern,
							String columnNamePattern) throws Exception;
	/**
	 * Chiude la connessione al DB di origine passata a parametro.
	 * @param conn Un oggetto Connection (connessione java al DB di origine)
	 * @throws Exception Se si verifica un'eccezione SQL durante l'esecuzione del metodo.
	 */
	public void closeConnection(Connection conn) throws Exception;
}
