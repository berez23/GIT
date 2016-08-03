package it.webred.diogene.querybuilder.db;

import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;

import it.webred.diogene.client.UserEntitiesProxy;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DvUeFromEntity;
import it.webred.diogene.db.model.DvUserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class GetUserEntityDbManager {
	
	/**
	 *	Oggetto Configuration di Hibernate.
	 */
	Configuration c;
	/**
	 *	Oggetto SessionFactory di Hibernate.
	 */
	SessionFactory sf;

	/**
	*	Costruttore che inizializza gli oggetti Configuration e SessionFactory.
	*/
	public GetUserEntityDbManager() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Restituisce, sotto forma di ArrayList di oggetti di classe DvUserEntity, l'elenco delle User Entity già presenti nel DB.
	 * @param session La corrente sessione di Hibernate.
	 * @return Una ArrayList di oggetti di classe DvUserEntity, contenente l'elenco delle User Entity già presenti nel DB.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	private ArrayList<DvUserEntity> getCurrentEntities(Session session) throws Exception {
		ArrayList<DvUserEntity> retVal = new ArrayList<DvUserEntity>();
		try {
			Query q = session.createQuery("from DV_USER_ENTITY in class DvUserEntity");
			List list = q.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				retVal.add((DvUserEntity)it.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return retVal;
	}
	
	/**
	 * Restituisce, sotto forma di ArrayList di oggetti di classe DvUserEntity, l'elenco delle User Entity presenti nel DB del server 
	 * chiamato (tramite la chiamata al Web Service relativo).
	 * @param serverURL L'URL del server da cui si intende importare una User Entity.
	 * @return Una ArrayList di oggetti di classe DvUserEntity, contenente l'elenco delle User Entity presenti nel DB del server 
	 * chiamato.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private ArrayList<DvUserEntity> getServerEntities(String serverURL) throws Exception {
		ArrayList<DvUserEntity> retVal = new ArrayList<DvUserEntity>();
		//chiamata a web service		
		String WSPath = "/services/UserEntities";
		String nameWS = serverURL + WSPath;
		it.webred.diogene.client.UserEntitiesBean ueb = new UserEntitiesProxy(nameWS).getUserEntities();
		Object[] objs = ueb.getUserEntities();
		if (objs != null) {
			for (int i = 0; i < objs.length; i++) {
				HashMap dvueHm = (HashMap)objs[i];
				DvUserEntity dvue = new DvUserEntity();
				dvue.setId((Long)dvueHm.get("dvUserEntityId"));
				dvue.setSqlStatement(((String)dvueHm.get("sqlStatement")).getBytes(UTF8_XML_ENCODING));
				DcEntity dce = new DcEntity();
				dce.setId((Long)dvueHm.get("dcEntityId"));
				dce.setName((String)dvueHm.get("name"));
				dvue.setDcEntity(dce);
				HashMap entitiesNames = (HashMap)dvueHm.get("entitiesNames");
				Iterator it = entitiesNames.keySet().iterator();
				while (it.hasNext()) {
					String str = (String)entitiesNames.get((String)it.next());
					DvUeFromEntity dufe = new DvUeFromEntity();
					DcEntity dceDufe = new DcEntity();
					dceDufe.setName(str);
					dufe.setDcEntity(dceDufe);
					dvue.getDvUeFromEntities().add(dufe);
				}
				retVal.add(dvue);
			}
		}
		return retVal;
	}
	
	/**
	 * Esclude dall'elenco delle entità presenti nel server chiamato quelle che sono già presenti nel DB (il confronto avviene per nome 
	 * entità). Ordina inoltre le altre per nome entità.
	 * @param serverURL L'URL del server da cui si intende importare una User Entity.
	 * @return Una ArrayList di oggetti di classe DvUserEntity, contenente l'elenco delle entità effettivamente disponibili per 
	 * l'importazione, ordinato per nome entità.
	 * @throws Exception Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	public ArrayList<DvUserEntity> matchEntities(String serverURL) throws Exception {
		Session session = null;
		ArrayList<DvUserEntity> retVal = new ArrayList<DvUserEntity>();
		try {
			session = sf.openSession();
			ArrayList<DvUserEntity> serverEntities = getServerEntities(serverURL);
			ArrayList<DvUserEntity> currentEntities = getCurrentEntities(session);
			for (DvUserEntity serverDvue : serverEntities) {
				boolean presente = false;
				for (DvUserEntity currentDvue : currentEntities) {
					if (serverDvue.getDcEntity().getName().equals(currentDvue.getDcEntity().getName()))	{
						presente = false;
						break;
					}
				}
				if (!presente) {
					retVal.add(serverDvue);
				}
			}
			//ordinamento alfabetico per nome entità
			Comparator comparator = new Comparator()
	        {
	            public int compare(Object o1, Object o2)
	            {
	                String name1 = ((DvUserEntity)o1).getDcEntity().getName();
	                String name2 = ((DvUserEntity)o2).getDcEntity().getName();
	                return name1.compareTo(name2);
	            }
	        };
			Collections.sort((List)retVal, comparator);
			//aggiunta dell'item vuoto come primo elemento
			DvUserEntity dvue = new DvUserEntity();
			dvue.setId(new Long(-1));
			DcEntity dce = new DcEntity();
			dce.setId(new Long(-1));
			dce.setName("");
			dvue.setDcEntity(dce);
			retVal.add(0, dvue);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}		
		return retVal;
	}

}
