package it.webred.diogene.correlazione.model;

import it.webred.diogene.correlazione.beans.Tema;
import it.webred.diogene.db.model.DvTema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di gestione temi.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/07/25 09:35:08 $
 */
public class TemiModel {

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
	public TemiModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Restituisce l'elenco dei temi presenti nel DB, sotto forma di ArrayList.
	 * @return Una ArrayList di oggetti di classe Tema, contenente l'elenco dei temi presenti nel DB.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<Tema> getTemi() throws Exception {
		ArrayList<Tema> retVal = new ArrayList<Tema>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DV_TEMA in class DvTema order by NAME");
			List dvtemi = q.list();
			Iterator it = dvtemi.iterator();
			Tema tema;
			while (it.hasNext()) {
				DvTema element = (DvTema)it.next();
				tema = new Tema();
				tema.setId(element.getId());
				tema.setName(element.getName());
				retVal.add(tema);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return retVal;
	}
	
	/**
	 * Effettua l'inserimento o la modifica di un tema nel DB.
	 * @param tema Oggetto di classe Tema contenente i dati da inserire (se l'id è null) o modificare nel DB.
	 * @return Un oggetto String contenente l'eventuale messaggio di errore ("" se non ci sono errori)
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public String salvaOAggiornaTema(Tema tema) throws Exception {
		Session session = null;
		Transaction t = null;
		if (tema.getName().equals("")) {
			return "Nome tema non inserito (presenti solo caratteri spazio).";
		}
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DV_TEMA in class DvTema where DV_TEMA.name=:name");
			DvTema param = new DvTema();
			param.setName(tema.getName());
			q.setProperties(param);
			List dvtemi = q.list();
			Iterator it = dvtemi.iterator();
			if (it.hasNext()) {
				DvTema element = (DvTema)it.next();
				if (tema.getId() == null || tema.getId().longValue() != element.getId().longValue()) {
					return "Nome tema già presente.";
				}				
			}
			t = session.beginTransaction();
			DvTema dvTema = new DvTema();
			if (tema.getId() != null) {
				dvTema = (DvTema)session.load(DvTema.class, tema.getId());
			}			
			dvTema.setName(tema.getName());
			session.saveOrUpdate(dvTema);
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.commit();
		} catch (Exception e) {
			//rollback transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return "";
	}
	
	/**
	 * Effettua la cancellazione di un tema (identificato dall'id parametro) dal DB.
	 * @param id L'identificativo del tema da cancellare.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void cancellaTema(Long id) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			session.delete(session.load(DvTema.class, id));
			//commit transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.commit();
		} catch (Exception e) {
			//rollback transazione
			if (t != null && !t.wasCommitted() && !t.wasRolledBack())
				t.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}
	
}
