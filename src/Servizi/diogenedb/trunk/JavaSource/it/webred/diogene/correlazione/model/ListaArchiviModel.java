package it.webred.diogene.correlazione.model;

import it.webred.diogene.correlazione.backingbeans.ListaTipiEtichettaBB;
import it.webred.diogene.correlazione.beans.TipoEtichetta;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcTipoe;
import it.webred.diogene.db.model.DcTipoeColumn;
import it.webred.diogene.db.model.DvTema;
import it.webred.diogene.metadata.beans.Table;
import it.webred.diogene.querybuilder.RightsManager;
import it.webred.permessi.GestionePermessi;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di visualizzazione della lista degli archivi consultabili 
 * per uno o più tipi etichetta selezionati (fase di visualizzazione dei dati da archivi).
 * @author Filippo Mazzini
 * @version $Revision: 1.4.2.1 $ $Date: 2012/01/26 16:59:39 $
 */
public class ListaArchiviModel {

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
	public ListaArchiviModel() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}	
	
	/**
	 * Restituisce l'elenco degli archivi consultabili per i tipi etichetta passati a parametro sotto forma di ArrayList di 
	 * oggetti di classe TipoEtichetta.
	 * @param selTipiEtichetta ArrayList di oggetti di classe TipoEtichetta, contenente i tipi etichetta per cui si ricercano 
	 * gli archivi consultabili.
	 * @return Una ArrayList di oggetti di classe Table, contenente l'elenco degli archivi consultabili per i tipi etichetta 
	 * passati a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<Table> getArchivi(ArrayList<TipoEtichetta> selTipiEtichetta) throws Exception {
		ArrayList<Table> retVal = new ArrayList<Table>();
		Session session = null;
		//per controllo permessi
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Principal principal = externalContext.getUserPrincipal();
		String context = externalContext.getRequestContextPath().substring(1);		
		try {
			session = sf.openSession();
			//leggo tutte le entità presenti nel DB
			Query q = session.createQuery("select DC_ENTITY from DC_ENTITY in class DcEntity, DV_USER_ENTITY in class DvUserEntity where DC_ENTITY.id = DV_USER_ENTITY.dcEntity order by DC_ENTITY.longDesc");
			List dcents = q.list();
			Iterator it = dcents.iterator();
			Table table;
			boolean aggiungi = false;
			while (it.hasNext()) {
				DcEntity element = (DcEntity) it.next();
				table = new Table();
				table.setId(element.getId());
				table.setName(element.getName());
				table.setLongDesc(element.getLongDesc());
				table.setOwner(element.getOwner());
				table.setUpdated(false);
				table.setDtMod(element.getDtMod());
				Set dcColumns = element.getDcColumns();
				aggiungi = true;
				for (TipoEtichetta selTipoEtichetta : selTipiEtichetta) {
					if (!aggiungi)
						break;
					aggiungi = false;
					DcTipoe dctipoe = (DcTipoe)session.load(DcTipoe.class, selTipoEtichetta.getId());
					Set tipoeColumns = dctipoe.getDcTipoeColumns();
					Iterator dcIt = dcColumns.iterator();
					while (!aggiungi && dcIt.hasNext()) {
						DcColumn dcCol = (DcColumn)dcIt.next();
						Iterator tipoeIt = tipoeColumns.iterator();
						while (!aggiungi && tipoeIt.hasNext()) {
							DcTipoeColumn tipoeCol = (DcTipoeColumn)tipoeIt.next();
							if (dcCol.getId().longValue() == tipoeCol.getDcColumn().getId().longValue()) {								
								aggiungi = true;
							}									
						}
					}
				}
				if (aggiungi) {
					//controllo permessi
					/* TODO: MODIFICARE GESTIONE EPRMESSI
					if (RightsManager.isRightGranted(principal, table.getName(), context, GestionePermessi.READ_ENTITY)) {
						retVal.add(table);
					}
					*/ 
					// MESSA QUESTA RIGA SOTTO AL POSTO DELLA IF SOPRA
					retVal.add(table);
				}
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
	 * Per i tipi etichetta passati a parametro, se in almeno uno degli archivi passati a parametro la colonna 
	 * corrispondente al tipo etichetta è di un tipo speciale (implementato al momento solo per i campi data), 
	 * nell'oggetto TipoEtichetta viene impostato il campo "formato" (per la visualizzazione nella pagina).
	 * @param tipiEtichetta I tipi etichetta da verificare.
	 * @param archivi Gli archivi in cui effettuare la verifica.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void setFormatoEtichette(ArrayList<TipoEtichetta> tipiEtichetta, ArrayList<Table> archivi) throws Exception {
		Session session = null;	
		try {
			session = sf.openSession();
			for (TipoEtichetta selTipoEtichetta : tipiEtichetta) {
				boolean isDate = false;
				for (Table selArchivio : archivi) {
					DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, selArchivio.getId());
					Set dcColumns = dcEntity.getDcColumns();
					DcTipoe dctipoe = (DcTipoe)session.load(DcTipoe.class, selTipoEtichetta.getId());
					Set tipoeColumns = dctipoe.getDcTipoeColumns();
					Iterator dcIt = dcColumns.iterator();
					while (dcIt.hasNext()) {
						DcColumn dcCol = (DcColumn)dcIt.next();
						Iterator tipoeIt = tipoeColumns.iterator();
						while (tipoeIt.hasNext()) {
							DcTipoeColumn tipoeCol = (DcTipoeColumn)tipoeIt.next();
							if (dcCol.getId().longValue() == tipoeCol.getDcColumn().getId().longValue()) {								
								String colType = dcCol.getDcExpression().getColType();
								Object obj = null;
								try {
									obj = Class.forName(colType).newInstance();
								}catch (java.lang.InstantiationException ie) {
									//si verifica con java.sql.Date e con java.math.BigDecimal
									if (colType.equals("java.math.BigDecimal")) {
										//ci sarebbe una nuova eccezione...
										obj = new BigDecimal(0);
									}else{
										obj = Class.forName(colType).getSuperclass().newInstance();
									}
								}
								if (obj instanceof Date) {
									isDate = true;
								}
							}									
						}
					}
				}
				selTipoEtichetta.setFormato(isDate ? ListaTipiEtichettaBB.FORMATO_DATA_VIS : null);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}
	
	/**
	 * Restituisce l'elenco dei tipi etichetta associati all'archivio selezionato (sotto forma di ArrayList di oggetti TipoEtichetta), 
	 * per l'effettuazione della query alternativa.
	 * @param idArchivioAltraQuery Identificativo dell'archivio selezionato per effettuare la query alternativa.
	 * @return Una ArrayList di oggetti TipoEtichetta, contenente l'elenco dei tipi etichetta associati all'archivio il cui 
	 * identificativo è passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<TipoEtichetta> getTipiEtichettaAltraQuery(Long idArchivioAltraQuery) throws Exception {
		ArrayList<TipoEtichetta> retVal = new ArrayList<TipoEtichetta>();
		if (idArchivioAltraQuery == null) {
			return retVal;
		}
		Session session = null;
		try {
			session = sf.openSession();
			DcEntity entity = (DcEntity)session.load(DcEntity.class, idArchivioAltraQuery);
			Set dcColumns = entity.getDcColumns();
			Iterator itColumns = dcColumns.iterator();
			while (itColumns.hasNext()) {
				DcColumn dcColumn = (DcColumn)itColumns.next();
				Set dcTipoeColumns = dcColumn.getDcTipoeColumns();
				Iterator itTipoeColumns = dcTipoeColumns.iterator();
				TipoEtichetta tipoEtichetta;
				while (itTipoeColumns.hasNext()) {
					DcTipoeColumn dcTipoeColumn = (DcTipoeColumn)itTipoeColumns.next();
					DcTipoe dcTipoe = dcTipoeColumn.getDcTipoe();
					tipoEtichetta = new TipoEtichetta();
					tipoEtichetta.setId(dcTipoe.getId());
					tipoEtichetta.setName(dcTipoe.getName());
					DvTema dvTema = dcTipoe.getDvTema();
					tipoEtichetta.setFkDvTema(dvTema == null ? null : dvTema.getId());
					tipoEtichetta.setNameTema(dvTema == null ? null : dvTema.getName());
					retVal.add(tipoEtichetta);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		//ordinamento alfabetico per nome tipo etichetta
		Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                String name1 = ((TipoEtichetta)o1).getName();
                String name2 = ((TipoEtichetta)o2).getName();
                return name1.compareTo(name2);
            }
        };
		Collections.sort((List)retVal, comparator);
		
		return retVal;
	}
	
}
