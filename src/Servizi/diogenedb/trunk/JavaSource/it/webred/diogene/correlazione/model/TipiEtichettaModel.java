package it.webred.diogene.correlazione.model;

import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.db.model.DcTipoe;
import it.webred.diogene.db.model.DvTema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di gestione tipi etichetta.
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/08/03 14:42:04 $
 */
public class TipiEtichettaModel {

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
	public TipiEtichettaModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Restituisce l'elenco dei temi presenti nel DB, sotto forma di ArrayList di SelectItem (da utilizzarsi per la 
	 * visualizzazione in ComboBox).
	 * @return Una ArrayList di oggetti di classe SelectItem, contenente l'elenco dei temi presenti nel DB.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getTemi() throws Exception {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DV_TEMA in class DvTema order by NAME");
			List dvtemi = q.list();
			Iterator it = dvtemi.iterator();
			while (it.hasNext()) {
				DvTema element = (DvTema)it.next();
				retVal.add(new SelectItem(element.getId(), element.getName()));
			}
			//aggiungo l'item vuoto in posizione iniziale
			retVal.add(0, new SelectItem(new Long(-1), ""));
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
	 * Restituisce l'elenco dei tipi etichetta presenti nel DB, sotto forma di ArrayList.<p>
	 * Se idTema è diverso da null, viene restituito l'elenco dei tipi etichetta associati a quel tema.
	 * @param idTema L'identificativo del tema di cui si ricercano i tipi etichetta associati (se null, sono 
	 * restituiti tutti i tipi etichetta presenti nel DB).
	 * @return Una ArrayList di oggetti di classe TipoEtichetta, contenente l'elenco dei tipi etichetta presenti nel DB.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<TipoEtichetta> getTipiEtichetta(Long idTema) throws Exception {
		ArrayList<TipoEtichetta> retVal = new ArrayList<TipoEtichetta>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = null;
			if (idTema != null) {
				q = session.createQuery("from DC_TIPOE in class DcTipoe where DC_TIPOE.dvTema=:dvTema order by NAME");
				DcTipoe dctipoe = new DcTipoe();
				dctipoe.setDvTema((DvTema)(session.load(DvTema.class, idTema)));
				q.setProperties(dctipoe);
			}else{
				q = session.createQuery("from DC_TIPOE in class DcTipoe order by NAME");
			}
			List dctipie = q.list();
			Iterator it = dctipie.iterator();
			TipoEtichetta tipoEtichetta;
			while (it.hasNext()) {
				DcTipoe element = (DcTipoe)it.next();
				tipoEtichetta = new TipoEtichetta();
				tipoEtichetta.setId(element.getId());
				tipoEtichetta.setName(element.getName());
				DvTema dvTema = element.getDvTema();
				tipoEtichetta.setFkDvTema(dvTema == null ? null : dvTema.getId());
				tipoEtichetta.setNameTema(dvTema == null ? null : dvTema.getName());
				retVal.add(tipoEtichetta);
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
	 * Effettua l'inserimento o la modifica di un tipo etichetta nel DB.
	 * @param tipoEtichetta Oggetto di classe TipoEtichetta contenente i dati da inserire (se l'id è null) o modificare nel DB.
	 * @return Un oggetto String contenente l'eventuale messaggio di errore ("" se non ci sono errori)
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public String salvaOAggiornaTipoEtichetta(TipoEtichetta tipoEtichetta) throws Exception {
		Session session = null;
		Transaction t = null;
		if (tipoEtichetta.getName().equals("")) {
			return "Nome tipo etichetta non inserito (presenti solo caratteri spazio).";
		}
		try {
			session = sf.openSession();
			Query q = null;
			if (tipoEtichetta.getFkDvTema() == null) {
				q = session.createQuery("from DC_TIPOE in class DcTipoe where DC_TIPOE.name=:name and DC_TIPOE.dvTema is null");
			}else{
				q = session.createQuery("from DC_TIPOE in class DcTipoe where DC_TIPOE.name=:name and DC_TIPOE.dvTema=:dvTema");
			}
			DcTipoe param = new DcTipoe();
			param.setName(tipoEtichetta.getName());
			if (tipoEtichetta.getFkDvTema() != null) {
				param.setDvTema((DvTema)session.load(DvTema.class, tipoEtichetta.getFkDvTema()));
			}
			q.setProperties(param);
			List dctipie = q.list();
			Iterator it = dctipie.iterator();
			if (it.hasNext()) {
				DcTipoe element = (DcTipoe)it.next();
				if (tipoEtichetta.getId() == null || tipoEtichetta.getId().longValue() != element.getId().longValue()) {
					return "Tipo etichetta già presente.";
				}				
			}
			t = session.beginTransaction();
			DcTipoe dcTipoe = new DcTipoe();
			if (tipoEtichetta.getId() != null) {
				dcTipoe = (DcTipoe)session.load(DcTipoe.class, tipoEtichetta.getId());
			}			
			dcTipoe.setName(tipoEtichetta.getName());
			if (tipoEtichetta.getFkDvTema() != null) {
				dcTipoe.setDvTema((DvTema)session.load(DvTema.class, tipoEtichetta.getFkDvTema()));
			}
			session.saveOrUpdate(dcTipoe);
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
	 * Effettua la cancellazione di un tipo etichetta (identificato dall'id parametro) dal DB.
	 * @param id L'identificativo del tipo etichetta da cancellare.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void cancellaTipoEtichetta(Long id) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			session.delete(session.load(DcTipoe.class, id));
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
