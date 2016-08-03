package it.webred.diogene.querybuilder.db;

import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


import it.webred.diogene.client.UserEntitiesProxy;
import it.webred.diogene.client.UserEntityBeanSer;
import it.webred.diogene.db.DcAttributes;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DcExpression;
import it.webred.diogene.db.model.DcSchemaEntity;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.UserEntityExportException;
import it.webred.diogene.querybuilder.beans.DcEntityBean;
import it.webred.diogene.querybuilder.beans.DvUserEntityBean;
import it.webred.diogene.querybuilder.control.ApplicationListener;
import it.webred.diogene.querybuilder.control.JoinSpec;
import it.webred.diogene.querybuilder.control.ObjectEditor;
import it.webred.diogene.querybuilder.control.UserEntityBean;
import it.webred.diogene.querybuilder.control.UserEntityColumnBean;
import it.webred.diogene.querybuilder.control.UserEntityColumnBeanAppendedValue;
import it.webred.diogene.querybuilder.db.EntitiesDBManager.SqlStatementAttribute;
import it.webred.diogene.sqldiagram.Condition;
import it.webred.diogene.sqldiagram.OperatorFactory;
import it.webred.diogene.sqldiagram.SqlGenerator;
import it.webred.diogene.sqldiagram.ValueExpression;
import it.webred.diogene.sqldiagram.EnumsRepository.DBType;
import it.webred.diogene.sqldiagram.EnumsRepository.ValueType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import static it.webred.diogene.querybuilder.Constants.ALIAS;
import static it.webred.diogene.querybuilder.Constants.COLUMN;
import static it.webred.diogene.querybuilder.Constants.TABLE;
import static it.webred.utils.StringUtils.isEmpty;

/**
 * Classe che contiene i metodi utilizzati per l'esportazione in un'altra base dati di una User Entity già esistente.
* @author Filippo Mazzini
* @version $Revision: 1.4 $ $Date: 2008/01/24 12:13:57 $
*/
public class UserEntityExporter {
	
	private static final Logger log = Logger.getLogger(UserEntityExporter.class.getName());

	/**
	 *	Oggetto Configuration di Hibernate.
	 */
	Configuration c;
	/**
	 *	Oggetto SessionFactory di Hibernate.
	 */
	SessionFactory sf;
	
	/**
	 * Costruttore di default.
	 */
	public UserEntityExporter() {
		super();
		c = new Configuration().configure("hibernate.cfg.xml");
		sf = c.buildSessionFactory();
	}
	
	public void importUserEntity(String serverURL, Long id, HashMap<String, String> entitiesNames)  throws Exception {
		Session session = null;
		try {
			session = sf.openSession();
			SqlGenerator sqlGen = new SqlGenerator(DBType.ORACLE);
			String WSPath = "/services/UserEntities";
			String nameWS = serverURL + WSPath;
			//decodifica da XML con XMLDecoder	
			UserEntityBeanSer uebSer = new UserEntitiesProxy(nameWS).getUserEntityBean(id);
/*			XMLDecoder dec = new XMLDecoder(bais);			
			dec.setExceptionListener( new ExceptionListener() {
	            public void exceptionThrown( Exception e ) {
	                e.printStackTrace();
	            }
	        });
			LinkedHashMap lhm = null;
			try {
				lhm = (LinkedHashMap)dec.readObject();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } finally {
		        dec.close();
		    }    		    
			String ueName = (String)((LinkedHashMap)lhm.get("dcEntity")).get("name");
			if (getUserEntityFromName(session, ueName) != null && false) {
				throw new UserEntityExportException("Nel DB di destinazione esiste già una User Entity con nome " + ueName + ": impossibile procedere");
			}
			if (isEmpty((String)lhm.get("explicitSql"))) {
				setUserEntityBeanIds(session, lhm, entitiesNames, sqlGen);
			}
*/
			//edbm.writeUserEntityToExport(ueb, session, sqlGen); //TODO metodo apposito
			writeUserEntityToDebug(uebSer, session,sqlGen);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}
	
	private void setUserEntityBeanIds(Session session, LinkedHashMap lhm, HashMap<String, String> entitiesNames, SqlGenerator sqlGen) throws UserEntityExportException {
		try {
			//entità in from
			LinkedHashMap fromList = (LinkedHashMap)lhm.get("fromMap");
			if (fromList != null) {
				Iterator it = fromList.keySet().iterator();
				while (it.hasNext()) {
					LinkedHashMap dvUeFromEntity = (LinkedHashMap)fromList.get((String)it.next());
					String dvUeFromEntityName = entitiesNames.get((String)dvUeFromEntity.get("name"));
					if (dvUeFromEntityName == null) {
						throw new UserEntityExportException("Impossibile impostare correttamente il nome di destinazione per l'entità in from con nome di origine " + (String)dvUeFromEntity.get("name"));
					}
					dvUeFromEntity.put("id", getDcEntityIdFromEntitySqlName(session, dvUeFromEntityName));
					dvUeFromEntity.put("name", dvUeFromEntityName);
				}
			}
			SAXBuilder builder = new SAXBuilder();
			//colonne in select
			LinkedHashMap selectList = (LinkedHashMap)lhm.get("selectList");
			if (selectList != null) {
				Iterator it = selectList.keySet().iterator();
				while (it.hasNext()) {
					LinkedHashMap dcColumn = (LinkedHashMap)selectList.get((String)it.next());
					LinkedHashMap dcExpression = (LinkedHashMap)dcColumn.get("dcExpression");
					String xml = new String((byte[])dcExpression.get("expression"));
					List content = builder.build(new StringReader(xml)).getContent();
					Iterator itContent = content.iterator();
					while (itContent.hasNext()) {
						Element element = (Element) itContent.next();
						setOperand(session, element, entitiesNames);
						StringWriter result = new StringWriter();
						XMLOutputter out = new XMLOutputter();
						out.setFormat(Format.getPrettyFormat());
						out.output(element, result);
						ValueExpression newValExp = ValueExpression.createFromXml(result.toString(), sqlGen);
						dcExpression.put("expression", ValueExpression.getOperandXmlString(newValExp).getBytes(UTF8_XML_ENCODING));
					}
				}
			}
			//condizioni di filtro
			/*
			
			if (ueb.getCondition() != null) {
				Condition condition = ueb.getCondition();
				String xml = condition.getStringXml();
				List content = builder.build(new StringReader(xml)).getContent();
				Iterator it = content.iterator();
				while (it.hasNext()) {
					Element element = (Element) it.next();
					setCondition(session, element);
					StringWriter result = new StringWriter();
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getPrettyFormat());
					out.output(element, result);
					ueb.setCondition(Condition.createFromXml(result.toString(),	sqlGen));
				}
			} //TODO verificare
			//relazioni
			if (ueb.getJoins() != null) {
				for (JoinSpec item : ueb.getJoins()) {
					Condition condition = item.getJoinCondition();
					String xml = condition.getStringXml();
					List content = builder.build(new StringReader(xml)).getContent();
					Iterator it = content.iterator();
					while (it.hasNext()) {
						Element element = (Element) it.next();
						setCondition(session, element);
						StringWriter result = new StringWriter();
						XMLOutputter out = new XMLOutputter();
						out.setFormat(Format.getPrettyFormat());
						out.output(element, result);
						item.setJoinCondition(Condition.createFromXml(result.toString(), sqlGen));
					}
				}
			}*/ //TODO verificare
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserEntityExportException(e.getMessage());
		}		
	}
	
	private void setCondition(Session session, Element condition, HashMap<String, String> entitiesNames) throws Exception {
		List content1 = condition.getChildren();
		Iterator it1 = content1.iterator();
		Element sql = null;
		while (it1.hasNext()) {
			Element element1 = (Element) it1.next();
			if (element1.getName().equals("sql")) {
				sql = element1;
			} else if (element1.getName().equals("operator")) {
				List content2 = element1.getChildren();
				Iterator it2 = content2.iterator();
				while (it2.hasNext()) {
					Element element2 = (Element) it2.next();
					if (element2.getName().equals("operand")) {
						setOperand(session, element2, entitiesNames);
					}
				}
			}
		}
		sql.setText(getSql(sql.getText(), entitiesNames));
	}
	
	private String getSql(String sqlText, HashMap<String, String> entitiesNames) {
		String retVal = sqlText;	
		Iterator it = entitiesNames.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			String value = entitiesNames.get(key);
			if (value != null) {
				retVal = retVal.replaceAll(key + ".", value + ".");
			}
		}
		return retVal;
	}
	
	private void setOperand(Session session, Element operand, HashMap<String, String> entitiesNames) throws Exception {	
		List content1 = operand.getChildren();
		Iterator it1 = content1.iterator();
		while (it1.hasNext()) {
			Element element1 = (Element) it1.next();
			if (element1.getName().equals("column")) {
				Long entityId = null;
				String entityName = null;
				Long columnId = null;
				String uiKey = element1.getAttributeValue("ui_key");
				List content2 = element1.getChildren();
				Iterator it2 = content2.iterator();
				while (it2.hasNext()) {
					Element element2 = (Element) it2.next();
					if (element2.getName().equals("table")) {
						List content3 = element2.getChildren();
						Iterator it3 = content3.iterator();									
						Element pkId = null;
						Element description = null;
						while (it3.hasNext()) {
							Element element3 = (Element) it3.next();
							if (element3.getName().equals("pk_id")) {
								pkId = element3;
							}else if (element3.getName().equals("description")) {
								description = element3;
							}
							else if (element3.getName().equals("name")) {
								entityName = entitiesNames.get(element3.getText());
								if (description.getText().equals(element3.getText())) {
									//si presume che sia il default, quindi lo reimposto
									description.setText(entityName);
								}
								element3.setText(entityName);
								entityId = getDcEntityIdFromEntitySqlName(session, entityName);								
								break;
							}
						}
						pkId.setText("" + entityId.longValue());
					} else if (element2.getName().equals("field")) {
						List content3 = element2.getChildren();
						Iterator it3 = content3.iterator();
						while (it3.hasNext()) {
							Element element3 = (Element) it3.next();
							if (element3.getName().equals("name")) {
								columnId = getDcColumnIdFromEntitySqlAndColumnName(session, entityName, element3.getText());
								break;
							}
						}
					}
				}
				String newUiKey = TABLE + entityId.longValue();
				newUiKey += uiKey.substring(uiKey.indexOf(ALIAS), uiKey.indexOf(COLUMN));
				newUiKey += COLUMN + columnId.longValue();
				element1.getAttribute("ui_key").setValue(newUiKey);
			}
		}
	}
	
	private Long getDcEntityIdFromEntitySqlName(Session session, String entityName) throws UserEntityExportException {
		Long retVal = new Long(-1);
		try {
			Query q = session.createQuery("from DC_SCHEMA_ENTITY in class DcSchemaEntity where DC_SCHEMA_ENTITY.sqlName=:sqlName");
			DcSchemaEntity param = new DcSchemaEntity();
			param.setSqlName(entityName);
			q.setProperties(param);
			List list = q.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				retVal = ((DcSchemaEntity) it.next()).getDcEntity().getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserEntityExportException(e.getMessage());
		}		
		if (retVal.longValue() == -1)
			throw new UserEntityExportException("Non esiste nel DB di destinazione un'entità con nome " + entityName);
		return retVal;
	}
	
	private Long getDcColumnIdFromEntitySqlAndColumnName(Session session, String entityName, String columnName) throws UserEntityExportException {
		Long entityId = getDcEntityIdFromEntitySqlName(session, entityName);
		Long retVal = new Long(-1);
		try {
			Query q = session.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.dcEntity=:dcEntity");
			DcEntity dcEntity = (DcEntity)session.load(DcEntity.class, entityId);
			DcColumn param = new DcColumn();
			param.setDcEntity(dcEntity);
			q.setProperties(param);
			List list = q.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				DcColumn dcCol = (DcColumn)it.next();
				DcExpression dcExpr = dcCol.getDcExpression();
				String dcColName = getColumnNameFromExpression(dcExpr.getExpression());
				if (dcColName.equals(columnName)) {
					retVal = dcCol.getId();
				}
			}
			session.evict(dcEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserEntityExportException(e.getMessage());
		}
		if (retVal.longValue() == -1)
			throw new UserEntityExportException("Non esiste nel DB di destinazione una colonna con nome " + columnName + " dell'entità con nome " + entityName);
		return retVal;
	}
	
	private DvUserEntity getUserEntityFromName(Session session, String name) throws UserEntityExportException {
		DvUserEntity retVal = null;
		try {
			Query q = session.createQuery("from DC_ENTITY in class DcEntity where DC_ENTITY.name=:name");
			DcEntity param = new DcEntity();
			param.setName(name);
			q.setProperties(param);
			List list = q.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				DcEntity element = (DcEntity)it.next();
				Set dvues = element.getDvUserEntities();
				Iterator itues = dvues.iterator();
				if (itues.hasNext()) {
					retVal = (DvUserEntity)itues.next();
					break;
				}
			}
			session.evict(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserEntityExportException(e.getMessage());
		}
		return retVal;
	}
	
	/**
	 * Dato un array di byte (ricavato dalla lettura del valore EXPRESSION, campo di tipo XMLTYPE, nella tabella 
	 * DC_EXPRESSION), restituisce una String che rappresenta il nome della colonna corrispondente.
	 * @param expr L'array di byte ricavato dalla lettura del valore per il campo XMLTYPE
	 * @return Il nome della colonna
	 * @throws UserEntityExportException Se si verifica una qualche eccezione durante l'esecuzione del metodo.
	 */
	private String getColumnNameFromExpression(byte[] expr) throws UserEntityExportException {
		try {
			String xml = new String(expr, UTF8_XML_ENCODING);
			SAXBuilder builder = new SAXBuilder();
			List content = builder.build(new StringReader(xml)).getContent();
			Iterator it = content.iterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				List content1 = element.getChildren();
				Iterator it1 = content1.iterator();
				while (it1.hasNext()) {
					Element element1 = (Element) it1.next();
					if (element1.getName().equals("column")) {
						List content2 = element1.getChildren();
						Iterator it2 = content2.iterator();
						while (it2.hasNext()) {
							Element element2 = (Element) it2.next();
							if (element2.getName().equals("field")) {
								List content3 = element2.getChildren();
								Iterator it3 = content3.iterator();
								while (it3.hasNext()) {
									Element element3 = (Element) it3.next();
									if (element3.getName().equals("name"))
										return element3.getText();
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserEntityExportException(e.getMessage());
		}
		return null;		
	}
	

	
	
	private void  writeUserEntityToDebug(UserEntityBeanSer uebSer, Session session, SqlGenerator sqlGen) 
	throws Exception {
		EntitiesDBManager dbm = new EntitiesDBManager();

		log.debug("Cerco di Deserializzare la entity importata");
	    // Object deserialization
	    try {
	    	UserEntityBean object2;
	      ByteArrayInputStream bis = new ByteArrayInputStream(uebSer.getUserEntityBean());
	      ObjectInputStream ois = new ObjectInputStream(bis);
	      object2 = (UserEntityBean)ois.readObject();
	      ois.close();
	      log.debug("Deserializzata entity " + object2.getName());
	    }
	    catch(Exception e) {
	    	log.error("Errore nella deserializzazione della entity durante la procedura di importatazione", e);
	    	throw new Exception("Errore nella deserializzazione della entity durante la procedura di importatazione");
	    }

		try {

			

			
		}
		catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	
	

}
