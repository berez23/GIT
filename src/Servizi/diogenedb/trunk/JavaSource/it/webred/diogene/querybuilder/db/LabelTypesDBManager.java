package it.webred.diogene.querybuilder.db;

import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcTipoe;
import it.webred.diogene.db.model.DcTipoeColumn;
import it.webred.diogene.db.model.DvTema;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.beans.DcColumnWithLabelTypeBean;
import it.webred.diogene.querybuilder.db.EntitiesDBManager.SqlStatementAttribute;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Classe che contiene metodi di accesso ai dati utilizzati dalla pagina di associazione colonne - tipi etichetta e definizione 
 * manuale dell'SQL.
 * @author Filippo Mazzini
 * @version $Revision: 1.4 $ $Date: 2007/11/23 13:42:16 $
 */
public class LabelTypesDBManager {

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
	public LabelTypesDBManager() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	/**
	 * Restituisce, sotto forma di ArrayList di oggetti di classe DcColumnWithLabelTypeBean, l'elenco delle colonne 
	 * della DcEntity il cui identificativo è passato a parametro.
	 * @param userEntityId Identificativo della DcEntity per la quale si ricerca l'elenco delle colonne
	 * @return Una ArrayList di oggetti di classe DcColumnWithLabelTypeBean, contenente l'elenco delle colonne per la DcEntity 
	 * il cui identificativo è passato a parametro.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<DcColumnWithLabelTypeBean> getColumns(Long userEntityId) throws Exception {
		ArrayList<DcColumnWithLabelTypeBean> retVal = new ArrayList<DcColumnWithLabelTypeBean>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcEntity=:dcEntity order by LONG_DESC");
			DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, userEntityId);
			DcColumn param = new DcColumn();
			param.setDcEntity(dcEntity);
			q.setProperties(param);
			List dcColumns = q.list();
			Iterator it = dcColumns.iterator();
			while (it.hasNext()) {
				DcColumn dcColumn = (DcColumn)it.next();
				DcColumnWithLabelTypeBean dcColumnWithLabelTypeBean = new DcColumnWithLabelTypeBean();
				dcColumnWithLabelTypeBean.setId(dcColumn.getId());
				dcColumnWithLabelTypeBean.setDescription(dcColumn.getLongDesc());
				Set dcTipoeColumns = dcColumn.getDcTipoeColumns();
				Iterator itTipoeColumns = dcTipoeColumns.iterator();
				Long labelType = new Long(-1);
				while (itTipoeColumns.hasNext()) {
					DcTipoeColumn dcTipoeColumn = (DcTipoeColumn)itTipoeColumns.next();
					labelType = dcTipoeColumn.getDcTipoe().getId();
				}
				dcColumnWithLabelTypeBean.setLabelType(labelType);
				retVal.add(dcColumnWithLabelTypeBean);
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
	 * Restituisce l'elenco dei tipi etichetta presenti nel DB, sotto forma di ArrayList di SelectItem (da utilizzarsi per la 
	 * visualizzazione in ComboBox).
	 * @return Una ArrayList di oggetti di classe SelectItem, contenente l'elenco dei tipi etichetta presenti nel DB.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public ArrayList<SelectItem> getLabelTypes() throws Exception {
		ArrayList<SelectItem> retVal = new ArrayList<SelectItem>();
		Session session = null;
		try {
			session = sf.openSession();
			Query q = session.createQuery("from DC_TIPOE in class DcTipoe order by NAME");
			List dctipie = q.list();
			Iterator it = dctipie.iterator();
			while (it.hasNext()) {
				DcTipoe element = (DcTipoe)it.next();
				DvTema dvTema = element.getDvTema();
				String text = element.getName();
				text += dvTema == null ? "" : " (tema " + dvTema.getName() + ")";
				retVal.add(new SelectItem(element.getId(), text));
			}
			//item vuoto
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
	 * Effettua il salvataggio nel DB dei dati delle associazioni colonne - tipi etichette.
	 * Inoltre salva il valore del campo SQL_STATEMENT_BK definito per l'entità.
	 * @param userEntityId L'identificativo dell'entità selezionata.
	 * @param sqlStatementBk Il valore definito per il campo SQL_STATEMENT_BK dell'entità.
	 * @param columns ArrayList di oggetti di classe DcColumnWithLabelTypeBean, contenente i dati per inserimenti, modifiche e/o 
	 * cancellazioni nel DB delle associazioni colonne - tipi etichette.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public void salva(Long userEntityId, String sqlStatementBk, ArrayList<DcColumnWithLabelTypeBean> columns) throws Exception {
		Session session = null;
		Transaction t = null;
		try {
			session = sf.openSession();
			t = session.beginTransaction();
			//salvataggio dati associazioni colonne - tipi etichette
			for (DcColumnWithLabelTypeBean column : columns) {
				DcTipoeColumn dcTipoeColumn = null;				
				Query q = session.createQuery("from DC_TIPOE_COLUMN in class DcTipoeColumn where DC_TIPOE_COLUMN.dcColumn=:dcColumn");				
				DcTipoeColumn param = new DcTipoeColumn();
				DcColumn dcColumn = (DcColumn)session.load(DcColumn.class, column.getId());
				param.setDcColumn(dcColumn);
				q.setProperties(param);
				List dcTipoeColumns = q.list();
				Iterator it = dcTipoeColumns.iterator();
				while (it.hasNext()) {
					dcTipoeColumn = (DcTipoeColumn)it.next();
				}				
				if (column.getLabelType() == null && dcTipoeColumn != null) {
					//cancellazione
					session.delete(dcTipoeColumn);
				} else if (column.getLabelType() != null) {
					DcTipoe dcTipoe = (DcTipoe)session.load(DcTipoe.class, column.getLabelType());
					if (dcTipoeColumn == null) {
						//inserimento
						dcTipoeColumn = new DcTipoeColumn();
						dcTipoeColumn.setDcColumn(dcColumn);
						
					}
					//inserimento o modifica
					dcTipoeColumn.setDcTipoe(dcTipoe);
					session.saveOrUpdate(dcTipoeColumn);
				}
			}
			//salvataggio valore per sqlStatementBk
			if (sqlStatementBk != null && !sqlStatementBk.equals("")) {
				String xml = "<sql_statement attribute='" + SqlStatementAttribute.EXPLICIT.name() + "'><![CDATA[";
				xml += sqlStatementBk;
				xml += "]]></sql_statement>";
				DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, userEntityId);
				DvUserEntity dvUserEntity = null;
				Iterator it = dcEntity.getDvUserEntities().iterator();
				while (it.hasNext()) {
					dvUserEntity = (DvUserEntity)it.next();
				}
				dvUserEntity.setSqlStatementBk(xml.getBytes(UTF8_XML_ENCODING));
				session.saveOrUpdate(dvUserEntity);
			}
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
	
	/**
	 * Restituisce un oggetto di classe String, che contiene il valore del campo SQL_STATEMENT per l'entità selezionata.
	 * @param userEntityId L'identificativo dell'entità selezionata.
	 * @return Un oggetto di classe String che contiene il valore del campo SQL_STATEMENT per l'entità selezionata.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public String getSqlStatement(Long userEntityId) throws Exception {
		String retVal = "";
		Session session = null;
		try {
			session = sf.openSession();
			DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, userEntityId);
			Iterator it = dcEntity.getDvUserEntities().iterator();
			while (it.hasNext()) {
				DvUserEntity dvUserEntity = (DvUserEntity)it.next();
				String xmlSqlStatement = new String(dvUserEntity.getSqlStatement(), UTF8_XML_ENCODING);
				SAXBuilder builder = new SAXBuilder();
				List content = builder.build(new StringReader(xmlSqlStatement)).getContent();
				Iterator it1 = content.iterator();
				while (it1.hasNext()) {
					Element element = (Element) it1.next();
					retVal = element.getText();
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
	 * Restituisce un oggetto di classe String, che contiene il valore del campo SQL_STATEMENT_BK per l'entità selezionata.
	 * @param userEntityId L'identificativo dell'entità selezionata.
	 * @return Un oggetto di classe String che contiene il valore del campo SQL_STATEMENT_BK per l'entità selezionata.
	 * @throws Exception Se si verifica una qualche eccezione Hibernate durante l'esecuzione del metodo.
	 */
	public String getSqlStatementBk(Long userEntityId) throws Exception {
		String retVal = "";
		Session session = null;
		try {
			session = sf.openSession();
			DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, userEntityId);
			Iterator it = dcEntity.getDvUserEntities().iterator();
			while (it.hasNext()) {
				DvUserEntity dvUserEntity = (DvUserEntity)it.next();
				String xmlSqlStatementBk = new String(dvUserEntity.getSqlStatementBk(), UTF8_XML_ENCODING);
				SAXBuilder builder = new SAXBuilder();
				List content = builder.build(new StringReader(xmlSqlStatementBk)).getContent();
				Iterator it1 = content.iterator();
				while (it1.hasNext()) {
					Element element = (Element) it1.next();
					retVal = element.getText();
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
	
}
