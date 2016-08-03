package it.webred.mui.http;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.cache.CacheRefresher;
import it.webred.mui.model.CodiceBelfiore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import net.skillbill.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;

public class CodiceBelfioreMap extends HashMap implements CacheRefresher,Serializable{

	public static final int CACHE_CYCLE_SKIPPED = 100;
	private int cycle_count =0;
	public CodiceBelfioreMap() {
		super(10000,0.9f);
		// TODO Auto-generated constructor stub
	}
	public Object get(Object key){
		synchronized (this) {
			Object res = super.get(key);
			return res != null? res:key;
		}
	}
	public Object doRefresh() {
		synchronized (this) {
			cycle_count++;
			if(isEmpty() ||CACHE_CYCLE_SKIPPED%cycle_count == 0 ){
		    	clear();
			    try{
					Session session;
					session = HibernateUtil.currentSession();
				    Query query = session.createQuery(
				        "select c from it.webred.mui.model.CodiceBelfiore as c");
					Logger.log().info(this.getClass().getName(),
							"executing query "+query);
				    for (Iterator it = query.iterate(); it.hasNext(); ) {
				    	CodiceBelfiore codiceBelfiore = (CodiceBelfiore) it.next();
				    	put(codiceBelfiore.getCodice(),codiceBelfiore.getDescrizione());
				    }
			    }
			    catch (Throwable e) {
					Logger.log().error(this.getClass().getName(),
							"error while updating fornitura list", e);
					throw new MuiException("error while updating fornitura list",e);
				}
			    finally{
				    HibernateUtil.closeSession();
			    }
			}
		}
	    return this;
	}
}
