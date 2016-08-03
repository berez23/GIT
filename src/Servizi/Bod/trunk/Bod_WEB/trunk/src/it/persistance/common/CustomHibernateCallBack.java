package it.persistance.common;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class CustomHibernateCallBack implements HibernateCallback {

	private String sqlQuery = "";

	public CustomHibernateCallBack(String query) {
		this.sqlQuery = query;
	}

	public Object doInHibernate(Session session) throws HibernateException, SQLException {
		List<Object> lst = new LinkedList<Object>();
		if (sqlQuery.toUpperCase().startsWith("SELECT")){
			Query query = session.createSQLQuery(sqlQuery);
			List events = query.list();
			for (Iterator it = events.iterator(); it.hasNext();) {
				Object obj = it.next();
				lst.add(obj);
			}
		}else{
			Query query = session.createSQLQuery(sqlQuery);
			Integer risp = query.executeUpdate();
			lst.add(risp);
		}
		return lst;
	}

}