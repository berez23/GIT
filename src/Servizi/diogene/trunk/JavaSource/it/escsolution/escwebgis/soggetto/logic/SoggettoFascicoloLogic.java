/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.logic;


import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.bean.DataBase;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.net.URLDecoder;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoggettoFascicoloLogic extends EscLogic {

	public SoggettoFascicoloLogic(EnvUtente eu) {
					super(eu);
				}

	private final static String SQL_SELECT_LISTA = "" +
			"select * from (select ROWNUM as N,XMLCHIAVI,CODENT,COMUNE,COGNOME,FK_COMUNI,NOME,DENOMINAZIONE,COD_SOGGETTO,COD_FISC,PART_IVA " +
			"from ( select ROWNUM as N," +
			"XML_SOGGETTI_CHIAVI.XMLCHIAVI ," +
			"XML_SOGGETTI_CHIAVI.CODENT," +
			"decode(XML_SOGGETTI_CHIAVI.COGNOME, null, '-', XML_SOGGETTI_CHIAVI.COGNOME) as COGNOME," +
			"decode(XML_SOGGETTI_CHIAVI.NOME, null, '-',XML_SOGGETTI_CHIAVI.NOME) as NOME," +
			"decode(XML_SOGGETTI_CHIAVI.DENOMINAZIONE, null, '-', XML_SOGGETTI_CHIAVI.DENOMINAZIONE) as DENOMINAZIONE," +
			"decode(XML_SOGGETTI_CHIAVI.NASLUOGO ,null,'-',XML_SOGGETTI_CHIAVI.NASLUOGO) AS COMUNE," +
			"XML_SOGGETTI_CHIAVI.NASCODCOM AS FK_COMUNI," +
			"decode(XML_SOGGETTI_CHIAVI.COD_FISC , null, '-', XML_SOGGETTI_CHIAVI.COD_FISC) as COD_FISC," +
			"decode(XML_SOGGETTI_CHIAVI.PART_IVA , null, '-', XML_SOGGETTI_CHIAVI.PART_IVA) as PART_IVA," +
			"XML_SOGGETTI_CHIAVI.PK_IDINTERNOSGT as COD_SOGGETTO " +
			"from XML_SOGGETTI_CHIAVI " +
			"where 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from XML_SOGGETTI_CHIAVI WHERE 1=?" ;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from SIT_SGT WHERE 1=?" ;

	public final static String COST_PROCEDURA = "SOGGFASC";


	public Hashtable mCaricareListaSoggetto(Vector listaPar, SoggettoFascicoloFinder finder) throws Exception{

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "0";
		long conteggione = 0;
		java.sql.ResultSet rs;
		Connection conn = null;
		try {
			conn = this.getConnection();
			int indice = 1;

			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and XML_SOGGETTI_CHIAVI.PK_IDINTERNOSGT in (" +finder.getKeyStr() + ")" ;
				}

				if(i == 1){
					long limInf, limSup;
					limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
					sql = sql + "order by XML_SOGGETTI_CHIAVI.DENOMINAZIONE " ;
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						SoggettoFascicolo sog = new SoggettoFascicolo();
						sog.setcodSoggetto(rs.getString("COD_SOGGETTO"));
						sog.setComune(rs.getString("FK_COMUNI"));
						sog.setCognome(rs.getString("COGNOME"));
						sog.setNome(rs.getString("NOME"));
						sog.setDenominazione(rs.getString("DENOMINAZIONE"));
						sog.setCodiceFiscale(rs.getString("COD_FISC"));
						sog.setPartitaIva(rs.getString("PART_IVA"));
						sog.setComuneNascita(rs.getString("COMUNE"));
						sog.setXmlDb(rs.getClob("XMLCHIAVI"));
						sog.setCodEnte(rs.getString("CODENT"));

						vct.add(sog);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			//elenco DB presenti nell'applicazione
			List elencoDB = this.elencoDB(conn);
			ht.put("LISTA_DB",elencoDB);

			//recupero le presenze dei soggetti nei vari db
			Vector vctDB = trovaCollegamenti(vct,elencoDB);

//TEST MB
			for (int i = 0; i < vctDB.size(); i++) {
				SoggettoFascicolo sf_i=(SoggettoFascicolo)vctDB.get(i);
				List appo = this.elencoCrossDB(sf_i.getCodEnte(),sf_i.getPresenzeDB(),conn);
				sf_i.setPresenzeDB(appo);
				vctDB.set(i,sf_i);
			}
//FINE TEST MB

			ht.put("LISTA",vctDB);

			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}


	public Hashtable mCaricareDettaglioSoggetto(String chiave, List elencoDB) throws Exception{

		Connection conn = null;
			Hashtable ht = new Hashtable();
						// faccio la connessione al db
						try {
							conn = this.getConnection();
							this.initialize();
							String sql = "select XML_SOGGETTI_CHIAVI.XMLCHIAVI ," +
							"decode(XML_SOGGETTI_CHIAVI.COGNOME, null, '-', XML_SOGGETTI_CHIAVI.COGNOME) as COGNOME," +
							"decode(XML_SOGGETTI_CHIAVI.NOME, null, '-',XML_SOGGETTI_CHIAVI.NOME) as NOME," +
							"decode(XML_SOGGETTI_CHIAVI.DENOMINAZIONE, null, '-', XML_SOGGETTI_CHIAVI.DENOMINAZIONE) as DENOMINAZIONE," +
							"decode(XML_SOGGETTI_CHIAVI.NASLUOGO ,null,'-',XML_SOGGETTI_CHIAVI.NASLUOGO) AS COMUNE," +
							"XML_SOGGETTI_CHIAVI.NASCODCOM AS FK_COMUNI," +
							"decode(XML_SOGGETTI_CHIAVI.COD_FISC , null, '-', XML_SOGGETTI_CHIAVI.COD_FISC) as COD_FISC," +
							"decode(XML_SOGGETTI_CHIAVI.PART_IVA , null, '-', XML_SOGGETTI_CHIAVI.PART_IVA) as PART_IVA," +
							"XML_SOGGETTI_CHIAVI.PK_IDINTERNOSGT as COD_SOGGETTO, " +
							"XML_SOGGETTI_CHIAVI.CODENT " +
							"from XML_SOGGETTI_CHIAVI " +
							"where XML_SOGGETTI_CHIAVI.PK_IDINTERNOSGT = ?";

							this.setString(1,chiave);

							prepareStatement(sql);
							java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

							SoggettoFascicolo sog = new SoggettoFascicolo();

							if (rs.next()){
								sog.setcodSoggetto(rs.getString("COD_SOGGETTO"));
								sog.setComune(rs.getString("FK_COMUNI"));
								sog.setCognome(rs.getString("COGNOME"));
								sog.setNome(rs.getString("NOME"));
								sog.setDenominazione(rs.getString("DENOMINAZIONE"));
								sog.setCodiceFiscale(rs.getString("COD_FISC"));
								sog.setPartitaIva(rs.getString("PART_IVA"));
								sog.setComuneNascita(rs.getString("COMUNE"));
								sog.setXmlDb(rs.getClob("XMLCHIAVI"));
								sog.setCodEnte(rs.getString("CODENT"));

							}


							//recupero i db a cui è collegato il soggetto
							Vector appo = new Vector();
							appo.add(sog);
							appo = this.trovaCollegamenti(appo,elencoDB);
							sog = (SoggettoFascicolo)appo.get(0);

							//creo l'elenco dei collegamenti con i db legati al soggetto
							List elencoCrossDB = this.elencoCrossDB(sog.getCodEnte(),sog.getPresenzeDB(),conn);

							ht.put("ELENCO_CROSS",elencoCrossDB);
							ht.put("SOGGETTO",sog);

							/*INIZIO AUDIT*/
							try {
								Object[] arguments = new Object[1];
								arguments[0] = chiave;
								writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
							} catch (Throwable e) {				
								log.debug("ERRORE nella scrittura dell'audit", e);
							}
							/*FINE AUDIT*/
							
							return ht;
						}
					catch (Exception e) {
						log.error(e.getMessage(),e);
						throw e;
					}
					finally
					{
						if (conn != null && !conn.isClosed())
							conn.close();
					}

		}

	/**
	 * Il metodo recupera la presenza del soggetto nei vari Db dell'applicazione
	 * @param conn
	 * @param codSogg
	 * @return List contenente tutti i db e se il soggetto vi è presente
	 */
	private Vector trovaCollegamenti(Vector vectList,List elencoDB ){
		try{
			for (int y=0;y<vectList.size();y++){
				List elencoDB_y = new ArrayList();
				for (int t=0;t<elencoDB.size();t++){
					DataBase dby = new DataBase();
					dby.setIdDB(((DataBase)elencoDB.get(t)).getIdDB());
					dby.setDescrizione(((DataBase)elencoDB.get(t)).getDescrizione());
					dby.setElementi(((DataBase)elencoDB.get(t)).getElementi());
					dby.setUrlDB(((DataBase)elencoDB.get(t)).getUrlDB());
					dby.setNome(((DataBase)elencoDB.get(t)).getNome());
					dby.setCodProcedura(((DataBase)elencoDB.get(t)).getCodProcedura());
					elencoDB_y.add(t,dby);
				}
				//elencoDB_y = elencoDB;
				//recupero il Clob con l'xml
				SoggettoFascicolo s_y = (SoggettoFascicolo)vectList.get(y);
				Clob xmlDB = s_y.getXmlDb();
				//recupero il testo dell'xml
				StringBuffer clobBuffer = new StringBuffer();
				Reader reader = xmlDB.getCharacterStream();
				char[] buffer = new char[1024];
				int readCount;
				while ((readCount = reader.read(buffer))!= -1){
					clobBuffer.append(buffer,0,readCount);
				}

				//creo un dom.Document per "navigare" nell'xml
				ByteArrayInputStream bais = new ByteArrayInputStream(clobBuffer.toString().getBytes());
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = db.parse(bais);

				//sono in root, leggo i childs (1 solo!)
				NodeList nlist =doc.getChildNodes();

				//sono in SoggettiChiavi
				Element nodo = doc.getDocumentElement();
				NodeList nListSgt = nodo.getChildNodes();
				for (int i = 0;i<nListSgt.getLength();i++){
					//sono in sgt
					Node nodo_i = nListSgt.item(i);
					NodeList listC= nodo_i.getChildNodes();
					for (int t = 0;t<listC.getLength();t++){
						Node n_t = listC.item(t);
						//controllo se il figlio si sgt è db
						if (n_t.getNodeName().equals("db")){
							//sono in db
							NodeList nListDB = n_t.getChildNodes();
							//recupero i tag fk_chiave
							Vector presenza_db = new Vector();
							for (int k=0;k<nListDB.getLength();k++){
								Node nodo_key =nListDB.item(k);
								//controllo se c'è un valore
								if (nodo_key.getFirstChild()!= null){
									//aggiungo il valore al vettore
									presenza_db.add(nodo_key.getFirstChild().getNodeValue());
								}
							}

							//recupero l'id del tag db in esame (=codice DB)
							NamedNodeMap attrs = n_t.getAttributes();
							Node n_attr = attrs.getNamedItem("id");
							String valore = n_attr.getNodeValue();

							//aggiungo i valori trovati al bean dei db
							for (int z=0;z<elencoDB_y.size();z++){
								DataBase beanDB = (DataBase)elencoDB_y.get(z);
								if (beanDB.getIdDB().equals(valore)){
									beanDB.setElementi(presenza_db);
									elencoDB_y.set(z,beanDB);
									break;
								}
							}
						}
					}
				}
				s_y.setPresenzeDB(elencoDB_y);
				vectList.set(y,s_y);
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}

		return vectList;
	}

	/**
	 * Elenco ordinato per ID dei DB presenti nell'applicazione
	 * @param conn
	 * @return
	 */
	private List elencoDB(Connection conn){
		List listDB = new ArrayList();
		//String sql = "select * from sit_database_lookup order by id_db";
		String sql = "select dl.ID_DB,dl.DESCRIZIONE,dl.NOME,dl.COD_PROCEDURA,cl.NOMECLASSE,cl.URLCONSULTA from sit_database_lookup dl, ewg_classe cl	where dl.DESCRIZIONE = cl.NOMECLASSE (+) order by dl.ID_DB";

		try{

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()){
				DataBase db = new DataBase();
				db.setIdDB(rs.getString("id_db"));
				db.setDescrizione(rs.getString("descrizione"));
				db.setNome(rs.getString("nome"));
				db.setCodProcedura(rs.getString("cod_procedura"));
				db.setUrlDB(rs.getString("URLCONSULTA"));

				listDB.add(db);
			}


		}catch(Exception e){
			log.error(e.getMessage(),e);
		}

		return listDB;
	}

	private List elencoCrossDB(String codEnte,List listDB,Connection conn) throws Exception{
		List cross = new ArrayList();

		try{
			for (int k=0;k<listDB.size();k++){
				DataBase db_k = (DataBase)listDB.get(k);
				if (db_k.getElementi().size()>0){
					Vector elem = db_k.getElementi();
					String chiavi = "";
					for (int i=0;i<elem.size();i++){
						chiavi = chiavi + (String)elem.get(i) + ",";
					}
					chiavi = chiavi.substring(0,chiavi.length()-1);
					String url="";

					//imposto la url per richiamare il DB
					url =URLDecoder.decode(db_k.getUrlDB(),"US-ASCII")+COST_PROCEDURA+codEnte+"|"+chiavi;

					if (db_k.getCodProcedura()!= null)
						url += "|"+db_k.getCodProcedura();

					db_k.setUrlDB(url);
					cross.add(db_k);
				}// INIZIO TEST MB
				else{
					cross.add(db_k);
				}
				//FINE TEST MB

			}
		}catch(Exception e){
			throw e;
		}


		return cross;
	}

}
