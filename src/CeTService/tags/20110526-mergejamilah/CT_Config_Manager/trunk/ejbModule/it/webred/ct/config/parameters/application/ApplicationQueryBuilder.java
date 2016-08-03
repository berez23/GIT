package it.webred.ct.config.parameters.application;

import it.webred.ct.config.parameters.AbstractQueryBuilder;

public class ApplicationQueryBuilder extends AbstractQueryBuilder{
	
	private final String SQL_LISTA_APPLICATION = 
		"SELECT a.name, a.name, " +
		 "CASE WHEN SUM (CASE WHEN (SELECT COUNT (kv.key_conf) " +
		  "FROM am_key_value kv, am_key_value_ext kve " +
		 "WHERE kv.overw_type = 1 " +
		 "AND kve.key_conf(+) = kv.key_conf " +
		 "AND kve.fk_am_instance(+) = i.name " +
		 "AND kv.MUST_BE_SET = 'Y' " +
		 "AND kve.VALUE_CONF is null) = 0 " +
		 "THEN 0 " +
		 "ELSE 1 " +
		 "END) = 0 " +
		 "THEN 'N' " +
		 "ELSE 'S' " +
		 "END as required_found " +
		 "FROM am_instance i, am_application a " +
		 "WHERE i.FK_AM_APPLICATION = a.NAME " +
		 "GROUP BY a.name " +
		 "ORDER BY a.name";
	
	private final String SQL_LISTA_INSTANCE = 
		"SELECT i.name, i.name, " +
		 "CASE WHEN (SELECT COUNT (kv.key_conf) " +
		  "FROM am_key_value kv, am_key_value_ext kve " +
		 "WHERE kv.overw_type = 1 " +
		 "AND kve.key_conf(+) = kv.key_conf " +
		 "AND kve.fk_am_instance(+) = i.name " +
		 "AND kv.MUST_BE_SET = 'Y' " +
		 "AND kve.VALUE_CONF is null) = 0 " +
		 "THEN 'N' " +
		 "ELSE 'S' " +
		 "END as required_found " +
		 "FROM am_instance i " +
		 "WHERE i.FK_AM_APPLICATION = '@APPLICATION' " +
		 "ORDER BY i.name";
	
	public ApplicationQueryBuilder() {
	}
	
	public String createQueryListaApplication() {
		
		String sql = SQL_LISTA_APPLICATION;	
		return sql;
		
	}
	
	public String createQueryListaInstance(String application) {
		
		String sql = SQL_LISTA_INSTANCE;	
		sql = sql.replace("@APPLICATION", application);
		return sql;
		
	}
	
}
