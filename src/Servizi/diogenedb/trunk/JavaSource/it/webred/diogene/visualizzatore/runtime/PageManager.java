package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.HibernateSession;
import it.webred.diogene.db.model.DcAttribute;
import it.webred.diogene.db.model.DcColumn;
import it.webred.diogene.db.model.DvClasse;
import it.webred.diogene.db.model.DvFormatClasse;
import it.webred.utils.GenericTuples;
import it.webred.utils.HashMapUtils;
import it.webred.utils.GenericTuples.T2;
import it.webred.utils.GenericTuples.T3;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;


/**
 * Classe che si occupa di reperire i files di configurazione delle pagine del visualizzatore
 * La classe alla richiesta di un utente si occupa di reperre la configurazione e poi generare gli oggetti appositi per quell'utente: FilterPage, DetailPage e ListPage.
 * 
 * Gli oggetti vengono messi in cache e ripresi successivamente dalla cache.
 * 
 * Nel caso che nel filtro venga applicato un filtro per utente (presenza di ? negli eventuali sql del filtro)
 * le pagine vengono messe in cache divise per utente.
 * 
 * @author marcoric
 * @version $Revision: 1.2 $ $Date: 2007/11/23 15:01:36 $
 */
public class PageManager
{

	private static Log	log	= LogFactory.getFactory().getInstance(PageManager.class);

	
	/**
	 *  è la cache delle pagine per ogni classe, contiene al suo interno terne di oggetti
	 */
	private static Map<Long, T3<FilterPage, ListPage, DetailPage>> cacheClassi = new HashMap<Long, T3<FilterPage, ListPage, DetailPage>>();
	/**
	 * è la cache delle classi relativamente ad ogni utente (sono le classi i cui filtri dipendono dall'utente)
	 * ogni riga contiene una hashmap che al suo interno ha come chiave il nome utente e sull'oggetto la terna di classi
	*/
	private static   Map<Long, Map<String, T3<FilterPage, ListPage, DetailPage>>> cacheClassiUtente =  new HashMap<Long, Map<String, T3<FilterPage, ListPage, DetailPage>>>();
	

	public static void main(java.lang.String[] args) {
		//PageManager pm = new PageManager();
		try {
			//PageManager.getFilterPage(24,"pippo");
			PageManager.getListPage(5,"pippo");
			//PageManager.getDetailPage(5,"pippo","30052");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param idClasse
	 * @param username
	 * @return Tupla di 3 elementi T2.  Il boolean 
	 * @throws Exception
	 */
	private static T3<T2<FilterPage, Boolean>, T2<ListPage, Boolean>, T2<DetailPage, Boolean>> readXMLs(long idClasse, String username) throws Exception {
		Object owner = null;
		log.debug("readXMLs - sto leggendo gli xml di configurazione delle pagine dal db");
		try {
			owner = HibernateSession.createSession();
			Session s = HibernateSession.getSession();
			Query q = s.createQuery("from DV_FORMAT_CLASSE in class DvFormatClasse where DV_FORMAT_CLASSE.dvClasse=:dvClasse");
			DvClasse dvc = new DvClasse(new Long(idClasse));
			q.setParameter("dvClasse",dvc);
			DvFormatClasse dvFormatClasse = (DvFormatClasse) q.uniqueResult();
			
			
			
			ByteArrayInputStream isFilter = new ByteArrayInputStream(dvFormatClasse.getFilter());
			ByteArrayInputStream isList = new ByteArrayInputStream(dvFormatClasse.getList());
			ByteArrayInputStream isDetail = new ByteArrayInputStream(dvFormatClasse.getDetail());
			
			T2<FilterPage, Boolean> fp = generateFilterPage(isFilter, username , null);
			T2<ListPage, Boolean> lp = generateListPage(isList, username , null);
			
			try {
				ByteArrayInputStream sqlS = new ByteArrayInputStream(dvFormatClasse.getDvUserEntity().getSqlStatementBk());
				SAXReader reader = new SAXReader();
		      	Document docSql = reader.read(sqlS);
		      	Node node = docSql.selectSingleNode("//sql_statement");
		      	String sql = node.getText();
		      	lp.firstObj.setSql(sql);
			} catch (Exception e) {
				log.error("Impossibile reperire sql di userentity");
				throw new Exception(e);
			} 
			
			T2<DetailPage, Boolean> dp = generateDetailPage(isDetail, username , null);
			
			
			fp.firstObj.setIdDvClasse(dvc.getId());
			lp.firstObj.setIdDvClasse(idClasse);
			dp.firstObj.setIdDvClasse(idClasse);
			return GenericTuples.t3(fp,lp,dp);
		} finally {
			HibernateSession.closeSession(owner);
		}
		
	}
	


	private static it.webred.utils.GenericTuples.T2<ListPage, Boolean>  generateListPage(ByteArrayInputStream isList, String username, String database) throws Exception {
		
		log.debug("Generazione della ListPage");
		SAXReader reader = new SAXReader();
      	Document docList = reader.read(isList);

      	ListPage p = new ListPage();
      	
 
		
      	Node node = docList.selectSingleNode("//view");
      	p.setTitle(node.valueOf("@title"));

      	List colList = docList.selectNodes("//view/columns/column");
      	LinkedHashSet<ListElement> campiLista = new LinkedHashSet<ListElement>();
      	
        for (Iterator iter = colList.iterator(); iter.hasNext(); ) {
            Node colNode = (Node) iter.next();
         

    		try {
                ListElement le = new ListElement();
                Long idColumn = null;
                if (colNode.valueOf("@dccolumn")!="")
                	idColumn = new Long(colNode.valueOf("@dccolumn"));
                if (idColumn!=null) {
            		Object owner = null;
            		try {
		                owner = HibernateSession.createSession();
		    			Session s = HibernateSession.getSession();
			    		DcColumn dcColumn = getDcColumn(idColumn,s);
		                le.setId(idColumn);
		                le.setAlias(dcColumn.getDcExpression().getAlias());
		                le.setJavaType(dcColumn.getDcExpression().getColType());
		                le.setDescription(dcColumn.getLongDesc());
            		} finally {
            			HibernateSession.closeSession(owner);
            		}
                }  
	           
                
                
                
                String script = colNode.valueOf("@script");
                // nel caso la colonna sia uno script
                if (!script.equals("")) {
                	le.setLinkFunction(script);
                	le.setLinkImgUrl(colNode.valueOf("@imageurl"));
                	le.setLinkText(colNode.valueOf("@testurl"));
                	le.setDescription(colNode.valueOf("@header"));
    				// lista dei parametri dello script
    				List params = docList.selectNodes("//view/columns/column[@header='"+ le.getDescription() +"']/param");
    		        int nparams = params.size();
    		        String[] parameterAlias = new String[nparams];
    		        int i = 0;
    		        for (Iterator iterParam = params.iterator(); iterParam.hasNext(); ) {
    		            Node paramNode = (Node) iterParam.next();
    		                Long idColumnParam = new Long(paramNode.valueOf("@dccolumn"));
    		                if (idColumnParam!=null) {
    		            		Object owner = null;
    		            		try {
    			                owner = HibernateSession.createSession();
    			    			Session s = HibernateSession.getSession();
    				    		DcColumn dcColumn = getDcColumn(idColumnParam,s);
    				    		parameterAlias[i] =  dcColumn.getDcExpression().getAlias();
    				    		i = i + 1;
    		            		} finally {
    		            			HibernateSession.closeSession(owner);
    		            		}
    		                }  
    		        }
    		        // setto i parametri dello script sull'oggetto list element
    		        le.setLinkParameterAlias(parameterAlias);

                }

                campiLista.add(le);
                
   		} catch (Exception e) {
    			log.error("Errore nella generazione della ListPage",e);
    			throw new Exception(e);
    		}
        } // fine for
        
        p.setListElement(campiLista);

        

		// setto le chiavi e l'sql della ListPage
        //String[] keys = keyList.toArray(new String[keyList.size()]);
        

		//p.setIdDcColumnKey(keys);
		
		
        
        
		
		return it.webred.utils.GenericTuples.t2(p,new Boolean(false));
	}
	


	
	private static it.webred.utils.GenericTuples.T2<DetailPage, Boolean>  generateDetailPage(ByteArrayInputStream isDetail, String username, String database) throws Exception {

		log.debug("Generazione della detail page " + username + " database=" + database);
		SAXReader reader = new SAXReader();
		
		Document docDetail = reader.read(isDetail);

      	DetailPage p = new DetailPage();
      	Node node = docDetail.selectSingleNode("//view");
      	p.setTitle(node.valueOf("@title"));

      	LinkedHashSet<DetailTableElement> dtes =  new LinkedHashSet<DetailTableElement>();
      	List tables = docDetail.selectNodes("//view/table");
        for (Iterator iter = tables.iterator(); iter.hasNext(); ) {
            Node table = (Node) iter.next();
            DetailTableElement dte = new DetailTableElement();
            dte.setTitle(table.valueOf("@title"));

            LinkedHashSet<LinkedHashSet<DetailElement>>	tablerows = new LinkedHashSet<LinkedHashSet<DetailElement>>();	
            
            List rows = table.selectNodes("row");
            for (Iterator iterrow = rows.iterator(); iterrow.hasNext(); ) {
                Node row = (Node) iterrow.next();
                
                LinkedHashSet<DetailElement> des = new LinkedHashSet<DetailElement>();
                List columns = row.selectNodes("column");
                for (Iterator itercolumn = columns.iterator(); itercolumn.hasNext(); ) {
                    Node colNode = (Node) itercolumn.next();

                    Object owner = null;
    	            Long idColumn = null;
    	            if (!colNode.valueOf("@dccolumn").equals(""))
    	            		idColumn = new Long(colNode.valueOf("@dccolumn"));
    	    		try {
    		    		DetailElement elem = new DetailElement();
    	    			if (idColumn!=null) {
	    	    			owner = HibernateSession.createSession();
	    	    			Session s = HibernateSession.getSession();
	    		    		DcColumn dcColumn = getDcColumn(idColumn,s);
	    	    			elem.setDescription(dcColumn.getLongDesc());
	    	    			elem.setJavaType(dcColumn.getDcExpression().getColType());
	    	    			elem.setAlias(dcColumn.getDcExpression().getAlias());
    	    			} else {
    	    				
	    					org.jdom.Document document = new SAXBuilder().build(new StringReader(colNode.asXML()));
    	//    					org.jdom.Document document = new SAXBuilder().build(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><![CDATA[SELECT PIPPO FROM PLUTO]]></root>"));
    	    			       Element element = document.getRootElement();
    	    			       elem.setHtmlDATA(element.getText());
    	    			}
        	    		des.add(elem);
        	    		
    	    				
    	    		} catch (Exception e) {
    	    			log.error(e);
    	    		} finally {
    	    			if (owner!=null)
    	    				HibernateSession.closeSession(owner);
    	    		}
                }
                
                if (des.size()<3) {
                	int aggiungiColonnePerRiempireFinoATre = 3-des.size();
                	for (int i=1;i<=aggiungiColonnePerRiempireFinoATre;i++) {
                    	des.add(null);
                	}
                }
                tablerows.add(des);
                
            }
            dte.setPageElement(tablerows);
            dtes.add(dte);
            
        }

        p.setPageElement(dtes);

      	
      	

		
		
		return it.webred.utils.GenericTuples.t2(p,new Boolean(false));
	}
	
	
	private static DcColumn getDcColumn(Long idColumn, Session s) throws Exception {
		try {
			Query q = s.createQuery("from DC_COLUMN in class DcColumn where DC_COLUMN.id=:id");
			q.setParameter("id",idColumn);
			DcColumn dcColumn = (DcColumn) q.uniqueResult();
			return dcColumn;
		} catch (Exception e) {
			log.error("Errore nella lettura DI DcColumn con id=" + idColumn);
			throw e;
		}
			
	}

	private static it.webred.utils.GenericTuples.T2<FilterPage, Boolean>  generateFilterPage(ByteArrayInputStream isFilter, String username, String database) throws Exception {
			log.debug("Genera filterpage. username=" + username + " database=" + database);
			SAXReader reader = new SAXReader();
	      	Document docFilter = reader.read(isFilter);

	      	FilterPage p = new FilterPage();
	      	Node node = docFilter.selectSingleNode("//view");
	      	p.setTitle(node.valueOf("@title"));
	      	
	      	List colList = docFilter.selectNodes("//view/columns/column");
	      	
	      	// indica se in una eventuale query presente nell'xml c'è il punto interrogativo 
			boolean paramUtente=false;
	        LinkedHashSet<FilterElement> campiFiltro = new LinkedHashSet<FilterElement>();

	        for (Iterator iter = colList.iterator(); iter.hasNext(); ) {
	            Node colNode = (Node) iter.next();
	         
	            Long idColumn = new Long(colNode.valueOf("@dccolumn"));
	            
	    		Object owner = null;
	    		try {
	    			owner = HibernateSession.createSession();
	    			Session s = HibernateSession.getSession();
		    		DcColumn dcColumn = getDcColumn(idColumn,s);

		    		FilterElement elem = new FilterElement();
	    			elem.setDescription(dcColumn.getLongDesc());
	    			elem.setJavaType(dcColumn.getDcExpression().getColType());
	    			elem.setAlias(dcColumn.getDcExpression().getAlias());
	    				
	    				// lista degli operatori della colonna selezionata
	    				List opList = docFilter.selectNodes("//view/columns/column[@dccolumn="+ idColumn +"]/operators/values/value");
	    		        LinkedHashMap<String, String> operators = new LinkedHashMap<String, String>();
	    		        for (Iterator iterOp = opList.iterator(); iterOp.hasNext(); ) {
	    		            Node opNode = (Node) iterOp.next();
	    		            String idOperator = opNode.valueOf("@id");
	    		            String descriptionOperator = opNode.valueOf("@description");
	    		            operators.put(idOperator,descriptionOperator);
	    		        }
	    		        elem.setOperators(operators);
	    		        
	    		        //eventuale lista dei valori
	    		        LinkedHashMap<String, String> listvalues = new LinkedHashMap<String, String>();
	    				Node requiredListValue = docFilter.selectSingleNode("//view/columns/column[@dccolumn="+ idColumn +"]/listvalues");
	    				if (requiredListValue!=null) {
		    				String required = requiredListValue.valueOf("@required");
		    				if (required!=null && required.equalsIgnoreCase("false"))
		    					listvalues.put("","Tutti");
		    				
		    				List valList = docFilter.selectNodes("//view/columns/column[@dccolumn="+ idColumn +"]/listvalues/values/value");
		    		        for (Iterator iterVal = valList.iterator(); iterVal.hasNext(); ) {
		    		            Node opNode = (Node) iterVal.next();
		    		            String idValue = opNode.valueOf("@id");
		    		            String descriptionValue = opNode.valueOf("@description");
		    		            listvalues.put(idValue,descriptionValue);
		    		        }

		    				Node sql = docFilter.selectSingleNode("//view/columns/column[@dccolumn="+ idColumn +"]/listvalues/sql");
		    				if (sql!=null) {
		    					String   sqlString = sql.getText();
		    					Connection conn = null;
		    					PreparedStatement ps = null;
		    					try {
		    						conn = DataJdbcConnection.getConnectionn(database);
		    						ps = conn.prepareStatement(sqlString);
		    						try {
			    						ps.setString(1,username);
			    						paramUtente = true;
		    						} catch (Exception e) 
		    						{
		    							log.warn("Atteso nela query il punto interrogativo ma non trovato "+ sqlString);
		    							// se sono andato in eccezione significa che 
		    							// non era inserito nella query il punto interrogativo, non fa nulla
		    							// proseguo
		    						}
		    						ResultSet rs = null;
		    						try {
			    						rs = ps.executeQuery();
			    						while (rs.next()) {
			    							listvalues.put(rs.getObject(1).toString(),rs.getObject(2).toString());
			    						}
		    						}
			    					catch (Exception e) {
			    						log.error(e.getMessage());
			    						e.printStackTrace();
			    					} 
			    					finally {
			    						if (rs!=null)
			    							rs.close();
			    					}
		    					} catch (Exception e) {
		    						log.error(e.getMessage());
		    						throw new Exception(e);
		    					} finally {
		    						if (conn!=null)
		    							conn.close();
		    						if (ps!=null)
		    							ps.close();
		    					}
		    				}
		    		        
		    		        
		    		        elem.setListValues(listvalues.size()==0?null:listvalues);
	    				}
	    	    		campiFiltro.add(elem);

	    		} catch (Exception e) {
	    			log.error("Errore in generate filter page " + e.getMessage());
	    		} finally {
	    			HibernateSession.closeSession(owner);
	    		}
	    		p.setFilterElement(campiFiltro);
	        }
    		return it.webred.utils.GenericTuples.t2(p,new Boolean(paramUtente));
	}
	
	public static FilterPage getFilterPage(long idClasse, String userName) throws Exception {
		T3<FilterPage, ListPage, DetailPage> d = getPages(idClasse, userName);
		return d.firstObj;
	}
	public static ListPage getListPage(long idClasse, String userName) throws Exception {
		T3<FilterPage, ListPage, DetailPage> d = getPages(idClasse, userName);
		

		List<String> keyList = new ArrayList<String>();
		
      	Object owner = null;
		try {
            owner = HibernateSession.createSession();
			Session s = HibernateSession.getSession();
			
			String sql = "from DvFormatClasse as dvfc"+
			" where dvfc.dvClasse.id = :id";

			Query q = s.createQuery(sql);
			q.setParameter("id",idClasse);

			DvFormatClasse dvfc = (DvFormatClasse) q.uniqueResult();
			
			Set colonne = dvfc.getDvUserEntity().getDcEntity().getDcColumns();
			
        	HashMap<String,String> hmkey = new HashMap<String,String>();

        	Iterator it = colonne.iterator();
			while (it.hasNext()) {
				DcColumn dcColumn = (DcColumn) it.next();
				Set attributi = dcColumn.getDcAttributes();
                if (dcColumn.getDcAttributes().size()!=0) {
                	Iterator itatt = dcColumn.getDcAttributes().iterator();
                	while (itatt.hasNext()) {
                		DcAttribute dca = (DcAttribute) itatt.next();
                		if (dca.getAttributeSpec().equals("KEY")) {
                			hmkey.put(dcColumn.getDcExpression().getAlias(),dca.getAttributeVal());
                		}
                		if (dca.getAttributeSpec().equals("DT_INIZIO_VAL")) {
                    		d.secondObj.setAliasInizioValidita(dcColumn.getDcExpression().getAlias());
                		}
                		if (dca.getAttributeSpec().equals("DT_FINE_VAL")) {
                    		d.secondObj.setAliasFineValidita(dcColumn.getDcExpression().getAlias());
                		}
                		if (dca.getAttributeSpec().equals("EXTERNAL_KEY")) {
                    		d.secondObj.setAliasExtKey(dcColumn.getDcExpression().getAlias());
                		}
                	}
                	
                }
			}

        	// ordino le chiavi per valore dove il valore è la posizione 
        	LinkedHashMap chiaviordinate =  HashMapUtils.sortHashMapByValues(hmkey,true,true);
        	
            Set set = chiaviordinate.entrySet();
            Iterator chiaviordinateit = set.iterator();
            while (chiaviordinateit.hasNext())
            {
                Map.Entry e =  (Map.Entry)chiaviordinateit.next();
                keyList.add((String)e.getKey());   // sulla chiave della Hashmap ordinata c'è l'alias
            }
			
			
		} finally {
			HibernateSession.closeSession(owner);
		}

		
		
		// setto le chiavi e l'sql della ListPage
        String[] keys = keyList.toArray(new String[keyList.size()]);
		d.secondObj.setIdDcColumnKey(keys);
		
		
		return d.secondObj;
	}
	
	
	/**
	 * @param idClasse
	 * @param userName
	 * @param entityKey - chiave (anche con divisore @ dela quale si richiede il dettaglio)
	 * @return
	 * @throws Exception
	 */
	public static DetailPage getDetailPage(long idClasse, String userName, String entityKey) throws Exception {
		T3<FilterPage, ListPage, DetailPage> d = getPages(idClasse, userName);
		try {
			PageLink pl = new PageLink(entityKey,idClasse);
			d.thirdObj.setPageLink(pl);
			
		} catch (Exception e) {
			log.error("Impossibile reperire link per dettaglio classe="+idClasse);
			throw new Exception(e);
		} 

		
		return d.thirdObj;
	}	

	private  static T3<FilterPage, ListPage, DetailPage> getPages(long idClasse, String userName) throws Exception
	{
			log.debug("GetPages - si sta reperendo le pagine di configurazione o da database oppure da chache");
			T3<FilterPage, ListPage, DetailPage> d = null;
			try {
				
				d = (T3<FilterPage, ListPage, DetailPage>) cacheClassi.get(idClasse);
				if (d==null) {
					//	vado sull'altra cache
					Map<String, T3<FilterPage, ListPage, DetailPage>>  mu = cacheClassiUtente.get(idClasse);
					if (mu!=null)
						d = mu.get(userName);
				}
				if (d==null) {
					// leggo dal db e faccio la cache
					T3<T2<FilterPage, Boolean>, T2<ListPage, Boolean>, T2<DetailPage, Boolean>> pages = readXMLs(idClasse,userName);
					cachePages(idClasse,userName, pages);
					// ricorsivamente la vado a prendere dalla hashmap
					return getPages(idClasse,userName);
				}
				return d;

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		
	}

	private static void cachePages(long idClasse, String username, T3<T2<FilterPage, Boolean>, T2<ListPage, Boolean>, T2<DetailPage, Boolean>> pages) {

		log.debug("Cache delle pagine di configurazione username="+ username);
		T3<FilterPage, ListPage, DetailPage> pagine = GenericTuples.t3(pages.firstObj.firstObj,pages.secondObj.firstObj,pages.thirdObj.firstObj);
		// è una classe la cui configurazione non è legata ad un utente
		if (!pages.firstObj.secondObj.booleanValue()) {
			cacheClassi.put(idClasse,pagine);
		} else {
			Map<String, T3<FilterPage, ListPage, DetailPage>> pagineClassePerUtente = (Map<String, T3<FilterPage, ListPage, DetailPage>>) cacheClassiUtente.get(idClasse);
			
			if (pagineClassePerUtente==null) {
				pagineClassePerUtente = new HashMap<String, T3<FilterPage, ListPage, DetailPage>>();
				cacheClassiUtente.put(idClasse,pagineClassePerUtente);
			}
			pagineClassePerUtente.put(username,pagine);
		}
		
	}


	public static void svuotaCache(long idClasse)
	{
		cacheClassi.remove(idClasse) ; 
		cacheClassiUtente.remove(idClasse) ;
	}





}
