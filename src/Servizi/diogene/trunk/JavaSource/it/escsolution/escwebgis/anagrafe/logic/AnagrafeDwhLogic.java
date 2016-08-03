/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.logic;


import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStorico;
import it.escsolution.escwebgis.anagrafe.bean.FamiliariStorico;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.utils.DateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeDwhLogic extends EscLogic{

	private String appoggioDataSource;
	public AnagrafeDwhLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
				SQL_SELECT_LISTA = getProperty("sql.SELECT_LISTA");
				SQL_SELECT_LISTA_ATT = getProperty("sql.SELECT_LISTA_ATT");
				SQL_SELECT_COUNT_LISTA = getProperty("sql.SELECT_COUNT_LISTA");
				SQL_SELECT_COUNT_LISTA_ATT = getProperty("sql.SELECT_COUNT_LISTA_ATT");
			}

	public static final String FINDER = "FINDER45";
	public static final String ANAGRAFEDWH = AnagrafeDwhLogic.class.getName() + "@ANAGRAFEDWH";
	public static final String ANAGRAFEDWH_POPUP = AnagrafeDwhLogic.class.getName() + "@ANAGRAFEDWH_POPUP";
	public static final String LISTA_ANAGRAFEDWH = "LISTA_ANAGRAFEDWH";
	public static final String LISTA_ANAGRAFE2DWH = "LISTA_ANAGRAFE2DWH";
	public static final String ANAGRAFE_INDIRIZZI = "ANAGRAFE_INDIRIZZI";
	public static final String ANAGRAFE_INDIRIZZI_POPUP = "ANAGRAFE_INDIRIZZI_POPUP";
	
	public static final String VIS_STORICO_FAM_KEY = "vis.storico.famiglie";
	public static final String VIS_STORICO_FAM_NO = "0";
	public static final String VIS_STORICO_FAM_ONLY = "1";
	public static final String VIS_STORICO_FAM_BOTH = "2";
	
	public static final String VIS_STORICO_DT_AGG = "VIS_STORICO_DT_AGG";
	public static final String VIS_STORICO_LISTA = "VIS_STORICO_LISTA";
	public static final String LBL_STORICO_PERIODO = "LBL_STORICO_PERIODO";

	//private final static int RIGHE_PER_PAGINA = it.webred.GlobalParameters.RIGHE_CONFIGURATE_PER_PAGINA;
	
	private static String SQL_SELECT_LISTA = null;
	private static String SQL_SELECT_LISTA_ATT = null;

	private static String SQL_SELECT_COUNT_LISTA= null;
	private static String SQL_SELECT_COUNT_LISTA_ATT= null;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from SIT_D_PERSONA WHERE 1=?" ;

	private final static String COD_COMMAND_STORICO_FAM = "CAR-FAM-S";	


	public Hashtable mCaricareListaAnagrafeCiv(String oggettoSel,AnagrafeDwhFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs ;
			
			
			this.initialize();			
			this.setString(1, oggettoSel);
			
			String sql = "SELECT * FROM (SELECT ROWNUM AS n, cognome, nome, cod_anagrafe, codice_fiscale, " +
                         "  data_nascita, codice_nazionale, id_orig, id_ext, ID, id_fam " +
                         " FROM (SELECT ROWNUM AS n, DECODE (sit_d_persona.id_orig, NULL, '-', " +
                         "  sit_d_persona.id_orig) AS cod_anagrafe, DECODE (sit_d_persona.cognome, " +
                         "     NULL, '-', sit_d_persona.cognome) AS cognome, DECODE (sit_d_persona.nome, " +
                         "     NULL, '-',  sit_d_persona.nome) AS nome, DECODE (sit_d_persona.codfisc, " +
                         "      NULL, '-', sit_d_persona.codfisc ) AS codice_fiscale,  sit_d_persona.id_orig AS id_orig, " +
                         " sit_d_persona.id_ext AS id_ext, sit_d_pers_fam.id_ext_d_famiglia AS id_fam, sit_d_persona.ID AS ID, " +
                         " NVL(TO_CHAR (sit_d_persona.data_nascita, 'dd/mm/yyyy'), '-') AS data_nascita, " +
                         " codent AS codice_nazionale FROM sit_d_persona LEFT JOIN sit_d_pers_fam " +
                         " ON sit_d_persona.id_ext = sit_d_pers_fam.id_ext_d_persona, "  +
                         " sit_ente " +
                         " WHERE 1 = 1 " +
                         " AND sit_d_persona.posiz_ana IN ('A', 'ISCRITTO NELL''A.P.R.') " +
                         " AND sit_d_persona.dt_fine_val IS NULL AND sit_d_pers_fam.dt_fine_val IS NULL " +
                         " AND sit_d_persona.id_ext in ( SELECT pciv.id_ext_d_persona from SIT_D_PERSONA_CIVICO pciv, SIT_D_CIVICO_V civ WHERE " +
                         " pciv.id_ext_d_civico=civ.id_ext and civ.id=?) ))";

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()){
				//campi della lista
				Anagrafe ana = new Anagrafe();
				ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
				ana.setId(rs.getString("ID"));
				ana.setIdExt(rs.getString("ID_EXT"));
				ana.setCognome(rs.getString("COGNOME"));
				ana.setNome(rs.getString("NOME"));
				ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
				ana.setDataNascita(rs.getString("DATA_NASCITA"));
				ana.setCodiceNazionale(getCodiceNazionale(conn, rs.getString("ID_EXT"), rs.getString("CODICE_NAZIONALE")));
				ana.setFamiglie(rs.getString("ID_FAM"));
				
				
				vct.add(ana);
			}

			ht.put(LISTA_ANAGRAFEDWH, vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put(FINDER, finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = oggettoSel;
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
		finally	{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
  }
	
	public Hashtable mCaricareListaAnagrafeVia(String oggettoSel,AnagrafeDwhFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs ;
			
			
			this.initialize();			
			this.setString(1, oggettoSel);
			
			String sql = "SELECT * FROM (SELECT ROWNUM AS n, cognome, nome, cod_anagrafe, codice_fiscale, " +
                         "  data_nascita, codice_nazionale, id_orig, id_ext, ID, id_fam " +
                         " FROM (SELECT ROWNUM AS n, DECODE (sit_d_persona.id_orig, NULL, '-', " +
                         "  sit_d_persona.id_orig) AS cod_anagrafe, DECODE (sit_d_persona.cognome, " +
                         "     NULL, '-', sit_d_persona.cognome) AS cognome, DECODE (sit_d_persona.nome, " +
                         "     NULL, '-',  sit_d_persona.nome) AS nome, DECODE (sit_d_persona.codfisc, " +
                         "      NULL, '-', sit_d_persona.codfisc ) AS codice_fiscale,  sit_d_persona.id_orig AS id_orig, " +
                         " sit_d_persona.id_ext AS id_ext, sit_d_pers_fam.id_ext_d_famiglia AS id_fam, sit_d_persona.ID AS ID, " +
                         " NVL(TO_CHAR (sit_d_persona.data_nascita, 'dd/mm/yyyy'), '-') AS data_nascita, " +
                         " codent AS codice_nazionale FROM sit_d_persona LEFT JOIN sit_d_pers_fam " +
                         " ON sit_d_persona.id_ext = sit_d_pers_fam.id_ext_d_persona, "  +
                         " sit_ente " +
                         " WHERE 1 = 1 " +
                         " AND sit_d_persona.posiz_ana IN ('A', 'ISCRITTO NELL''A.P.R.') " +
                         " AND sit_d_persona.dt_fine_val IS NULL AND sit_d_pers_fam.dt_fine_val IS NULL " +
                         " AND sit_d_persona.id_ext in ( SELECT pciv.id_ext_d_persona from SIT_D_PERSONA_CIVICO pciv, SIT_D_CIVICO_V civ, SIT_D_VIA via WHERE " +
                         "  civ.id_ext_d_via = via.id_ext and via.dt_fine_val is null " +
                         " and pciv.id_ext_d_civico=civ.id_ext and via.id=?) ))";

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next()){
				//campi della lista
				Anagrafe ana = new Anagrafe();
				ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
				ana.setId(rs.getString("ID"));
				ana.setIdExt(rs.getString("ID_EXT"));
				ana.setCognome(rs.getString("COGNOME"));
				ana.setNome(rs.getString("NOME"));
				ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
				ana.setDataNascita(rs.getString("DATA_NASCITA"));
				ana.setCodiceNazionale(getCodiceNazionale(conn, rs.getString("ID_EXT"), rs.getString("CODICE_NAZIONALE")));
				ana.setFamiglie(rs.getString("ID_FAM"));
				
				
				vct.add(ana);
			}

			ht.put(LISTA_ANAGRAFEDWH, vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put(FINDER, finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = oggettoSel;
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
		finally	{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
  }
	
	
	/*
	public String getIndirizzoFromPersona(String idPersona) throws Exception {
		String result = null;
		Connection conn = null;
		try {
			conn = this.getConnection();

			int indice = 1;
			java.sql.ResultSet rs ;
			String sql = "SELECT civ.id FROM SIT_D_CIVICO_V civ, SIT_D_PERSONA_CIVICO pciv WHERE " +
                         " pciv.ID_EXT_D_CIVICO=civ.id_ext AND pciv.id_ext_d_persona=?  AND pciv.dt_fine_val is null";
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		
	}
	*/
	
	public Hashtable mCaricareListaAnagrafe(Vector listaPar, AnagrafeDwhFinder finder) throws Exception{
					// carico la lista e la metto in un hashtable
					Hashtable ht = new Hashtable();
					Vector vct = new Vector();
					sql = "";
					String conteggio = "";
					long conteggione = 0;
					Connection conn = null;
					// faccio la connessione al db
							try {
								conn = this.getConnection();

								int indice = 1;
								java.sql.ResultSet rs ;
							
								String origine = "";
								if (listaPar != null && listaPar.size()>= 1){
									for (int index=0; index<listaPar.size(); index++){
										Object obj = listaPar.get(index);
										if ( obj != null && obj instanceof String &&  ((String)obj).equals("MAPPA") ){
											origine = "MAPPA";
											listaPar.remove(index);
											break;
										}
									}
								}
								/*
								 * l'ordinamento  diverso in base alla provenienza della chiamata:
								 * - chiamata dal civico della mappa, implica ordinamento 
								 * e raggruppamento per famiglia
								 * - altra provenienza invece ordinamento alfabetico dei nominativi
								 */
								String orderBy = "";
								for (int i=0;i<=1;i++){
									// il primo ciclo faccio la count
									if (i==0){
										if (origine != null && !origine.trim().equalsIgnoreCase("MAPPA"))
											sql = SQL_SELECT_COUNT_LISTA;
										else
											sql = SQL_SELECT_COUNT_LISTA_ATT;
									}else{
										if (origine != null && !origine.trim().equalsIgnoreCase("MAPPA")){
											sql = SQL_SELECT_LISTA;
											orderBy =  elaboraOrderByMascheraRicerca(listaPar);
											if (orderBy == null || orderBy.equals("")) {
												orderBy = " order by cognome, nome "; //questo per la lista
											}											
										}else{
											sql = SQL_SELECT_LISTA_ATT;
											orderBy = " order by id_fam, cognome, nome "; //questa per la lista richiesta cliccando sul civico
										}
									}
										indice = 1;
										this.initialize();
										this.setInt(indice,1);
										indice ++;

								//il primo passaggio esegue la select count
								//campi della search

								// il primo passaggio esegue la select count
								if (finder.getKeyStr().equals("")){
									sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
								}
								else{
									//controllo provenienza chiamata
									String controllo = finder.getKeyStr();
									if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
										String ente = controllo.substring(0,controllo.indexOf("|"));
										ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
										String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
										sql = sql + " and SIT_D_PERSONA.ID in (" +chiavi + ")" ;
										sql = sql + " and CODENT = '" +ente + "'" ;
									}else
										sql = sql + " and SIT_D_PERSONA.ID in (" +finder.getKeyStr() + ")" ;
								}

									long limInf, limSup;
									limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
									limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
									 
									sql += orderBy;
									
									if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
									else
										sql += "))";

									prepareStatement(sql);
									rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											Anagrafe ana = new Anagrafe();
											ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
											ana.setId(rs.getString("ID"));
											ana.setIdExt(rs.getString("ID_EXT"));
											ana.setCognome(rs.getString("COGNOME"));
											ana.setNome(rs.getString("NOME"));
											ana.setCodFiscale(rs.getString("CODICE_FISCALE"));
											ana.setDataNascita(rs.getString("DATA_NASCITA"));
											ana.setCodiceNazionale(getCodiceNazionale(conn, rs.getString("ID_EXT"), rs.getString("CODICE_NAZIONALE")));
											ana.setCivici(rs.getString("CIVICO"));
											ana.setResidenza(rs.getString("DESCRIZIONE_VIA"));
											if(origine != null && origine.trim().equalsIgnoreCase("MAPPA"))
												ana.setFamiglie(rs.getString("ID_FAM"));
											
											vct.add(ana);
										}
									}
									else{
										if (rs.next()){
											conteggio = rs.getString("conteggio");
										}
									}
								}
								ht.put(LISTA_ANAGRAFEDWH, vct);
								finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
								// pagine totali
								finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
								finder.setTotaleRecord(conteggione);
								finder.setRighePerPagina(RIGHE_PER_PAGINA);

								ht.put(FINDER, finder);
								
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
								// TODO Auto-generated catch block
								log.error(e.getMessage(),e);
								throw e;
							}
							finally
							{
								if (conn != null && !conn.isClosed())
									conn.close();
							}
}


	public Hashtable mCaricareDettaglioAnagrafe(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
						// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
						String sql = getProperty("sql.SELECT_DETTAGLIO");
						sql += " " + getProperty("sql.SELECT_DETTAGLIO_COND_ID");
						

						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						Anagrafe ana = new Anagrafe();

						if (rs.next()){
							ana.setCodAnagrafe(rs.getString("COD_ANAGRAFE"));
							ana.setId(rs.getString("ID"));
							ana.setIdExt(rs.getString("ID_EXT"));
							ana.setCognome(rs.getString("COGNOME"));
							ana.setNome(tornaValoreRS(rs,"NOME"));
							ana.setComuniNascita(tornaValoreRS(rs,"COMUNI_NASCITA"));
							ana.setDataNascita(tornaValoreRS(rs,"DATA_NASCITA"));
							ana.setSesso(tornaValoreRS(rs,"SESSO"));
							ana.setTipoSoggetto(tornaValoreRS(rs,"TIPO_SOGGETTO"));
							ana.setCodFiscale(tornaValoreRS(rs,"CODICE_FISCALE"));
							ana.setCittadinanze(tornaValoreRS(rs,"CITTADINANZA"));
							ana.setComuneImmigrazione(tornaValoreRS(rs,"COMUNE_IMMIGRAZIONE"));
							ana.setDataImmigrazione(tornaValoreRS(rs,"DATA_IMMIGRAZIONE"));
							ana.setComuneMorte(tornaValoreRS(rs,"COMUNE_MORTE"));
							ana.setDtMorte(tornaValoreRS(rs,"DATA_MORTE"));
							ana.setComuneEmigrazione(tornaValoreRS(rs,"COMUNE_EMIGRAZIONE"));
							ana.setDtEmigrazione(tornaValoreRS(rs,"DATA_EMIGRAZIONE"));
							ana.setPosizioneAnagrafica(tornaValoreRS(rs,"POSIZ_ANA"));
							ana.setStatoCivile(tornaValoreRS(rs,"DESC_STATO_CIVILE"));
							
							String sedime = rs.getString("VIASEDIME")==null?"":rs.getString("VIASEDIME") ;
							String descVia = rs.getString("VIADESCRIZIONE")==null?"":rs.getString("VIADESCRIZIONE");
							setResidenzaDaStorico(conn, ana);
							ana.setDescPrefissoVia(sedime + " " + descVia);

							ana.setFamiglie(rs.getString("FK_FAMIGLIE"));
							ana.setCodiceNazionale(getCodiceNazionale(conn, rs.getString("ID_EXT"), rs.getString("CODICE_NAZIONALE")));
							ana.setIdCiv(rs.getString("CIV_ID"));
							ana.setIdVia(rs.getString("ID_VIA"));
							ana.setIndirizzoEmi(rs.getString("INDIRIZZO_EMI")) ;
						}
						
						Hashtable htIndirizzi = mCaricareIndirizzi(ana.getCodAnagrafe());
						ht.putAll(htIndirizzi);
						
						ht.put(ANAGRAFEDWH, ana);
						
						setStoricoFamiglia(ht, ana.getIdExt(), null, null);
						
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
					if (conn != null)
						if (!conn.isClosed())
							conn.close();
				}
	}
	
	public void setStoricoFamiglia(Hashtable ht, String idExt, java.util.Date dtDa, java.util.Date dtA) throws Exception {
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setComune(getEnvUtente().getEnte());
			criteria.setKey(VIS_STORICO_FAM_KEY);
			ParameterService parameterService = (ParameterService)getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			AmKeyValueExt amKey = parameterService.getAmKeyValueExt(criteria);
			String value = amKey == null || amKey.getValueConf() == null ? VIS_STORICO_FAM_NO : amKey.getValueConf();
			ht.put(VIS_STORICO_FAM_KEY, value);
			if (value.equals(VIS_STORICO_FAM_ONLY) || value.equals(VIS_STORICO_FAM_BOTH)) {
				sql =  getProperty("sql.SELECT_DT_AGG_STORICO_FAM");
				this.initialize();
				int indice = 0;
				this.setString(++indice, getEnvUtente().getEnte());
				this.setString(++indice, COD_COMMAND_STORICO_FAM);
				prepareStatement(sql);
				ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				java.util.Date dtAgg = null;
				if (rs.next()) {
					dtAgg = rs.getDate("MAX_DATE_START");
				}
				if (dtAgg != null) {
					ht.put(VIS_STORICO_DT_AGG, dtAgg);
				}
				
				sql =  getProperty("sql.SELECT_STORICO_FAM");
				this.initialize();
				indice = 0;
				this.setString(++indice, idExt);
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				List<AnagrafeStorico> lstAnaSto = new ArrayList<AnagrafeStorico>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				while (rs.next()){
					AnagrafeStorico anaSto = new AnagrafeStorico();								
					anaSto.setCodFamiglia(rs.getString("ID_ORIG"));								
					java.util.Date dtIni = rs.getDate("DATA_INI_RIF");
					String dtIniStr = dtIni == null ? "-" : sdf.format(dtIni);
					if (dtIniStr.equals("01/01/1000")) dtIniStr = "-";
					anaSto.setInizioFamiglia(dtIniStr);								
					java.util.Date dtIniOrig = rs.getDate("DATA_INI_RIF_ORIG");
					String dtIniStrOrig = dtIniOrig == null ? "-" : sdf.format(dtIniOrig);
					if (dtIniStrOrig.equals("01/01/1000")) dtIniStrOrig = "-";
					anaSto.setInizioFamigliaOrig(dtIniStrOrig);
					java.util.Date dtFin = rs.getDate("DATA_FIN_RIF");
					String dtFinStr = dtFin == null ? "-" : sdf.format(dtFin);
					if (dtFinStr.equals("31/12/9999")) dtFinStr = "-";
					anaSto.setFineFamiglia(dtFinStr);								
					java.util.Date dtIniCiv = rs.getDate("DATA_INI_CIV");
					String dtIniCivStr = dtIniCiv == null ? "-" : sdf.format(dtIniCiv);
					if (dtIniCivStr.equals("01/01/1000")) dtIniCivStr = "-";
					anaSto.setInizioResidenzaElab(dtIniCivStr);								
					java.util.Date dtFinCiv = rs.getDate("DATA_FIN_CIV");
					String dtFinCivStr = dtFinCiv == null ? "-" : sdf.format(dtFinCiv);
					if (dtFinCivStr.equals("31/12/9999")) dtFinCivStr = "-";
					anaSto.setFineResidenzaElab(dtFinCivStr);								
					anaSto.setDescrVia(rs.getObject("DESCR_VIA") == null ? "-" : rs.getString("DESCR_VIA"));
					anaSto.setCivico(rs.getObject("NUM_CIV") == null ? "-" : rs.getString("NUM_CIV"));
					setStoricoPersone(conn, anaSto);
					lstAnaSto.add(anaSto);
				}
				
				if (dtDa == null)
					dtDa = sdf.parse("01/01/1000");
				if (dtA == null)
					dtA = sdf.parse("31/12/9999");
				
				List<AnagrafeStorico> lstAnaStoFilter = new ArrayList<AnagrafeStorico>();
				for (AnagrafeStorico anaSto : lstAnaSto) {
					java.util.Date dtIni = anaSto.getInizioFamiglia() == null || anaSto.getInizioFamiglia().equals("-") ?
											sdf.parse("01/01/1000") :
											sdf.parse(anaSto.getInizioFamiglia());
					java.util.Date dtFin = anaSto.getFineFamiglia() == null || anaSto.getFineFamiglia().equals("-") ?
											sdf.parse("31/12/9999") :
											sdf.parse(anaSto.getFineFamiglia());
					if (dtIni.getTime() <= dtA.getTime() && dtFin.getTime() >= dtDa.getTime()) {
						lstAnaStoFilter.add(anaSto);
					}				
				}
				lstAnaSto = lstAnaStoFilter;
				
				ht.put(VIS_STORICO_LISTA, lstAnaSto);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}		
	}
	
	private void setStoricoPersone(Connection conn, AnagrafeStorico anaSto) throws Exception {
		String sql = getProperty("sql.SELECT_STORICO_FAM_PERSONE");
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			int indice = 0;
			ps = conn.prepareStatement(sql);
			ps.setString(++indice, anaSto.getCodFamiglia());
			java.util.Date dtIni = anaSto.getInizioFamigliaOrig() == null || anaSto.getInizioFamigliaOrig().equals("-") ? sdf.parse("01/01/1000") : sdf.parse(anaSto.getInizioFamigliaOrig());
			ps.setDate(++indice, new java.sql.Date(dtIni.getTime()));
			java.util.Date dtFin = anaSto.getFineFamiglia() == null || anaSto.getFineFamiglia().equals("-") ? sdf.parse("31/12/9999") : sdf.parse(anaSto.getFineFamiglia());
			ps.setDate(++indice, new java.sql.Date(dtFin.getTime()));			
			ps.setString(++indice, anaSto.getCodFamiglia());
			ps.setDate(++indice, new java.sql.Date(dtIni.getTime()));
			ps.setDate(++indice, new java.sql.Date(dtFin.getTime()));			
			ps.setString(++indice, anaSto.getCodFamiglia());
			ps.setDate(++indice, new java.sql.Date(dtIni.getTime()));
			ps.setDate(++indice, new java.sql.Date(dtFin.getTime()));			
			rs = ps.executeQuery();
			ArrayList<FamiliariStorico> listFam = new ArrayList<FamiliariStorico>();
			while (rs.next()) {
				FamiliariStorico fs = new FamiliariStorico();
				fs.setDataInizioRif(anaSto.getInizioFamiglia() == null || anaSto.getInizioFamiglia().equals("-") ? sdf.parse("01/01/1000") : dtIni);
				fs.setDataFineRif(dtFin);
				fs.setId(rs.getString("ID"));
				fs.setCognome(rs.getString("COGNOME"));
				fs.setNome(rs.getString("NOME"));
				fs.setDtNascita(DateFormat.dateToString(rs.getDate("DATA_NASCITA"), "dd/MM/yyyy"));
				fs.setTipoParentela(rs.getString("RELAZ_PAR"));
				String dtIniFam = DateFormat.dateToString(rs.getDate("DATA_INI_RIF"), "dd/MM/yyyy");
				if (dtIniFam.equals("01/01/1000") || dtIniFam.equals("01/01/1001")) dtIniFam = "-";
				fs.setDtInizio(anaSto.getInizioFamiglia() == null || anaSto.getInizioFamiglia().equals("-") ? "-" : dtIniFam);
				String dtFinFam = DateFormat.dateToString(rs.getDate("DATA_FIN_RIF"), "dd/MM/yyyy");
				if (dtFinFam.equals("31/12/9999")) dtFinFam = "-";
				fs.setDtFine(dtFinFam);
				listFam.add(fs);
			}
			anaSto.setElencoFamiliari(listFam);
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}		
	}


	public Hashtable mCaricareIndirizzi(String chiave) throws Exception {
		
		// versione sql con codice civico in famiglia
		String sql_vecchia= "SELECT DISTINCT p.id_orig matricola, p.codfisc codice_fisc, p.cognome, p.nome," + 
        "p.sesso," + 
		 " SUBSTR (c.id_orig, 0, INSTR (c.id_orig, '#') - 1) cod_via," + 
		 " via.viasedime || ' ' || via.descrizione descr_via," + 
		 "   cxml.numero" + 
		 " || ' '" + 
		 " || cxml.barrato" + 
		 " || ' '" + 
		 " || cxml.subbarato num_civ," + 
		 " TO_CHAR (pc.dt_inizio_dato, 'dd/mm/yyyy') data_inizio, pc.dt_inizio_dato, " + 
		 " TO_CHAR (pc.dt_fine_dato, 'dd/mm/yyyy') data_fine, " + 
		 " PF.id_ext_d_famiglia fk_famiglia, F.id_orig cod_famiglia " + 
		 " FROM sit_d_pers_fam pf, sit_d_famiglia f, sit_d_persona_civico pc, sit_d_PERSONA P, sit_d_via via,  sit_d_civico c, " + 
		 " (SELECT id_ext, DT_FINE_VAL, " + 
		 " EXTRACT " + 
		 " (civico_composto, " + 
		 "  '/civicocomposto/att[@tipo=\"numero\"]/@valore' " + 
		 "  ).getstringval () numero, " + 
		 " EXTRACT " + 
		 "  (civico_composto, " +
		 "  '/civicocomposto/att[@tipo=\"barrato\"]/@valore' " + 
		 "  ).getstringval () barrato, " + 
		 "  EXTRACT " + 
		 "  (civico_composto, " + 
		 "      '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' " + 
		 "           ).getstringval () subbarato " + 
		 "  FROM sit_d_civico) cxml " + 
		 " WHERE pf.id_ext_d_famiglia = f.id_ext " + 
		 "  AND pf.iD_ORIG = ? " + 
		 " and PC.ID_EXT_D_PERSONA = PF.ID_EXT_D_PERSONA " + 
		 " AND PC.ID_EXT_D_CIVICO = F.ID_EXT_D_CIVICO " + 
         " AND NVL (f.DT_INIZIO_DATO , TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <" +
         " NVL (pc.DT_FINE_DATO ," +
         " TO_DATE ('31-12-9999', 'dd-mm-yyyy'))" +
         " AND NVL (f.DT_FINE_DATO , TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >=" +
         " NVL (pc.DT_INIZIO_DATO ," +
         " TO_DATE ('01-01-1001', 'dd-mm-yyyy'))" +
		 " AND P.ID_EXT = PF.ID_EXT_D_PERSONA " + 
		 " AND P.DT_FINE_VAL IS NULL " + 
		 " AND C.ID_EXT = PC.ID_EXT_D_CIVICO " + 
		 " AND C.ID_EXT_D_VIA = VIA.ID_EXT " + 
		 " AND C.DT_FINE_VAL IS NULL " + 
		 " AND VIA.DT_FINE_VAL IS NULL " + 
		 " AND c.id_ext = cxml.id_ext " + 
		 " AND CXML.DT_FINE_VAL IS NULL" + 
		 " ORDER BY pc.dt_inizio_dato DESC";
		
		//sostituita in data 06/04/2016 dall'attuale sql
		String sql_old = "SELECT DISTINCT p.id_orig matricola, p.codfisc codice_fisc, p.cognome, p.nome, p.sesso, " +
				"SUBSTR (c.id_orig, 0, INSTR (c.id_orig, '#') - 1) cod_via, " +
				"via.viasedime || ' ' || via.descrizione descr_via,  " +
				"   cxml.numero " +
				"|| ' ' " +
				"|| cxml.barrato " +
				"|| ' ' " +
				"|| cxml.subbarato num_civ, " +
				"TO_CHAR (pf.dt_inizio_dato, 'dd/mm/yyyy') data_inizio,  pf.dt_inizio_dato, " +
				"TO_CHAR (pf.dt_fine_dato, 'dd/mm/yyyy') data_fine, " +
				"PF.id_ext_d_famiglia fk_famiglia, F.id_orig cod_famiglia, F.ID_EXT_D_CIVICO " +
				"FROM sit_d_pers_fam pf, sit_d_famiglia f,  sit_d_PERSONA P, sit_d_via via,  sit_d_civico c, " +
				"(SELECT id_ext, DT_FINE_VAL, " +
				 " EXTRACT " + 
				 " (civico_composto, " + 
				 "  '/civicocomposto/att[@tipo=\"numero\"]/@valore' " + 
				 "  ).getstringval () numero, " + 
				 " EXTRACT " + 
				 "  (civico_composto, " +
				 "  '/civicocomposto/att[@tipo=\"barrato\"]/@valore' " + 
				 "  ).getstringval () barrato, " + 
				 "  EXTRACT " + 
				 "  (civico_composto, " + 
				 "      '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' " + 
				 "           ).getstringval () subbarato " + 
				 "  FROM sit_d_civico) cxml " + 
				" WHERE pf.id_ext_d_famiglia = f.id_ext " +
				" AND pf.iD_ORIG = ? " +
				" AND NVL (f.DT_INIZIO_DATO , TO_DATE ('01-01-1001', 'dd-mm-yyyy')) < NVL (pF.DT_FINE_DATO ,TO_DATE ('31-12-9999', 'dd-mm-yyyy')) " +
				" AND NVL (f.DT_FINE_DATO , TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >=  NVL (pF.DT_INIZIO_DATO ,TO_DATE ('01-01-1001', 'dd-mm-yyyy')) " +
				" AND P.ID_EXT = PF.ID_EXT_D_PERSONA " +
				" AND P.DT_FINE_VAL IS NULL " +
				" AND F.ID_EXT_D_CIVICO = C.ID_EXT  " +
				" AND C.ID_EXT_D_VIA = VIA.ID_EXT (+) " +
				" AND C.DT_FINE_VAL IS NULL " +
				" AND VIA.DT_FINE_VAL IS NULL " +
				" AND c.id_ext = cxml.id_ext (+) " +
				" AND CXML.DT_FINE_VAL IS NULL " +
				" ORDER BY pf.dt_inizio_dato DESC NULLS FIRST ";
		
		String sql = "SELECT tot.matricola, tot.codice_fisc, tot.cognome, tot.nome, tot.sesso, tot.cod_via, tot.descr_via, " +
					"LTRIM(RTRIM( " + 	
					" TRIM (LEADING 0 FROM EXTRACT (cxml.civico_composto, " +
					"'/civicocomposto/att[@tipo=\"numero\"]/@valore').getstringval ()) " +
					"|| ' ' " +
					"|| TRIM (LEADING 0 FROM EXTRACT (cxml.civico_composto, " +
					"'/civicocomposto/att[@tipo=\"barrato\"]/@valore').getstringval ()) " +
					"|| ' ' " +
					"|| TRIM (LEADING 0 FROM EXTRACT (cxml.civico_composto, " +
					"'/civicocomposto/att[@tipo=\"subbarrato\"]/@valore').getstringval ()) " +
					")) " +
					"num_civ, " +
					"tot.data_inizio, tot.dt_inizio_dato, tot.data_fine, tot.fk_famiglia, tot.cod_famiglia, tot.id_ext_d_civico, " +
					"tot.dt_inizio_civ, tot.dt_fine_civ " +
					"FROM ( " +
					"SELECT DISTINCT p.id_orig matricola, p.codfisc codice_fisc, p.cognome, p.nome, p.sesso, " +
					"SUBSTR (c.id_orig, 0, INSTR (c.id_orig, '#') - 1) cod_via, " +
					"via.viasedime || ' ' || via.descrizione descr_via,  " +
					"TO_CHAR (pf.dt_inizio_dato, 'dd/mm/yyyy') data_inizio,  pf.dt_inizio_dato, " +
					"TO_CHAR (pf.dt_fine_dato, 'dd/mm/yyyy') data_fine, " +
					"PF.id_ext_d_famiglia fk_famiglia, F.id_orig cod_famiglia, F.ID_EXT_D_CIVICO, c.id_ext, " +
					"PC.dt_inizio_dato as dt_inizio_civ, PC.dt_fine_dato as dt_fine_civ " +
					"FROM sit_d_pers_fam pf, sit_d_famiglia f,  sit_d_PERSONA P, sit_d_via via,  sit_d_civico c, sit_d_persona_civico pc " + 
					" WHERE pf.id_ext_d_famiglia = f.id_ext " +
					" AND pf.iD_ORIG = ? " +
					" AND NVL (f.DT_INIZIO_DATO , TO_DATE ('01-01-1001', 'dd-mm-yyyy')) < NVL (pF.DT_FINE_DATO ,TO_DATE ('31-12-9999', 'dd-mm-yyyy')) " +
					" AND NVL (f.DT_FINE_DATO , TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >=  NVL (pF.DT_INIZIO_DATO ,TO_DATE ('01-01-1001', 'dd-mm-yyyy')) " +
					" AND P.ID_EXT = PF.ID_EXT_D_PERSONA " +
					" AND C.ID_EXT = PC.ID_EXT_D_CIVICO " +
					" AND P.ID_EXT = PC.ID_EXT_D_PERSONA " +
					" AND P.DT_FINE_VAL IS NULL " +
					" AND F.ID_EXT_D_CIVICO = C.ID_EXT  " +
					" AND C.ID_EXT_D_VIA = VIA.ID_EXT (+) " +
					" AND C.DT_FINE_VAL IS NULL " +
					" AND VIA.DT_FINE_VAL IS NULL " +
					" ORDER BY pf.dt_inizio_dato DESC NULLS FIRST, pc.dt_inizio_dato DESC NULLS FIRST) tot, " +
					"sit_d_civico cxml " + 
					"WHERE tot.id_ext = cxml.id_ext(+) AND CXML.DT_FINE_VAL IS NULL";
 

		// versione sql senza codice divico in famiglia (n.b. si ha una duplicazione di info)
		String sql2 = "SELECT  distinct p.id_orig matricola," +
		"p.codfisc codice_fisc, " +
		"p.cognome, " +
		"p.nome, " +
		"p.sesso, " +
		"SUBSTR(C.ID_ORIG,0,instr(C.ID_ORIG, '#')-1) cod_via, " +
		"via.VIASEDIME ||' '||via.descrizione descr_via, " +
		"cxml.numero||' '||cxml.barrato||' '||cxml.subbarato NUM_CIV, " +
		"to_char(pc.dt_inizio_DATO, 'dd/mm/yyyy') DATA_INIZIO, " +
		"to_char(pc.dt_fine_DATO, 'dd/mm/yyyy') DATA_FINE, " +
		"f.ID_EXT_D_FAMIGLIA  fk_famiglia, " +
		"fam.ID_ORIG cod_famiglia " +
		"FROM sit_d_persona_civico pc, " +
		"sit_d_civico c, " +
		"sit_d_persona p, " +
		"sit_d_pers_fam f, " +
		"sit_d_famiglia fam, " +
		"sit_d_via via , " +
		"(SELECT ID_EXT, " +
		"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"numero\"]/@valore').getstringval () numero, " +
		"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"barrato\"]/@valore').getstringval () barrato," +
		"EXTRACT (civico_composto,'/civicocomposto/att[@tipo=\"subbarrato\"]/@valore').getstringval () subbarato " +
		"FROM sit_d_civico where dt_fine_val is null) cxml " +
		"WHERE pc.ID_EXT_d_civico = c.ID_EXT " +
		"AND p.ID_EXT = pc.ID_EXT_d_persona " +
		"AND p.FK_ENTE_SORGENTE = 1 " +
		"AND c.ID_EXT = cxml.ID_EXT " +
		"AND p.ID_EXT = f.ID_EXT_D_PERSONA " +
		"and f.ID_EXT_D_FAMIGLIA = fam.ID_EXT " +
		"and c.id_ext_d_via = via.id_ext " +
		"and via.dt_fine_val is null " +
		"and TO_CHAR(PC.DT_INIZIO_VAL,'dd/MM/yyyy') = TO_CHAR(f.DT_INIZIO_VAL,'dd/MM/yyyy') " +  // Condizione per evitare repliche spurie dovute ad errate associazioni civico/famiglia
		"AND p.ID_ORIG= ? " +
		"ORDER BY to_date(data_inizio,'dd/mm/yyyy') DESC" ;

		Hashtable ht = new Hashtable();
		ht = mCaricareIndirizziSwitch(chiave, sql);

		//25-06-2012 -> Si fa eseguire solo la prima query, che sfrutta il civico famiglia 
		/*		
		 * if (((Vector)ht.get("ANAGRAFE_INDIRIZZI")).isEmpty()) {
		 * 		ht = mCaricareIndirizziSwitch(chiave, sql2);
		 * 	}
		 * */
		
		return ht;
	
	}

	public Hashtable mCaricareIndirizziSwitch(String chiave, String sql) throws Exception{

		Hashtable ht = new Hashtable();
		Vector inds = new Vector();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
							
						String sqlFam = "SELECT DISTINCT p.id, p.cognome, p.nome, p.DATA_NASCITA, pf.RELAZ_PAR, pf.DT_INIZIO_DATO, pf.DT_FINE_DATO "+
							" FROM sit_d_pers_fam pf, sit_d_persona p "+
							" WHERE pf.id_ext_d_persona = p.id_ext "+
							" AND pf.id_ext_d_famiglia = ? "+
							" AND NVL (pf.DT_FINE_DATO, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >= NVL (?,    TO_DATE ('01-01-1001', 'dd-mm-yyyy')) " +     
							" AND NVL (pf.DT_INIZIO_DATO , TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <= NVL (?, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) " +
							" AND P.DT_FINE_VAL IS NULL  " +
							" AND NVL (p.data_MOR, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >= NVL (?,    TO_DATE ('01-01-1001', 'dd-mm-yyyy')) " +    
							" AND NVL (p.data_Nascita, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <= NVL (?, TO_DATE ('31-12-9999', 'dd-mm-yyyy'))  " +
							" AND NVL (p.dt_Fine_Dato, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >= NVL (?,    TO_DATE ('01-01-1001', 'dd-mm-yyyy'))  " +   
							" AND NVL (p.dt_Inizio_Dato, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <= NVL (?, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) " +
			        		" AND ( "+ 
			                "(NVL (p.data_Emi, TO_DATE ('31-12-9999', 'dd-mm-yyyy'))  > NVL (p.data_Imm, TO_DATE ('01-01-1001', 'dd-mm-yyyy'))   "+
			                "and (  "+
			                "    NVL (p.data_Emi, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >= NVL (?, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) "+
			                "    AND  "+
			                "    NVL (p.data_Imm, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <= NVL (NULL, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) "+
			                "    ) "+
			                ") or  "+
			                "( NVL (p.data_Emi, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <   NVL (p.data_Imm, TO_DATE ('31-12-9999', 'dd-mm-yyyy'))  "+
			                "and  "+
			                "    (  "+
			                "        NVL (p.data_Imm, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) <= NVL (?, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) "+
			                "    ) "+
			                ") "+
			                ")      "+ 
							" AND NVL (p.dt_Fine_Dato, TO_DATE ('31-12-9999', 'dd-mm-yyyy')) >= NVL (?,    TO_DATE ('01-01-1001', 'dd-mm-yyyy'))   " +  
							" AND NVL (p.dt_Inizio_Dato, TO_DATE ('01-01-1001', 'dd-mm-yyyy')) <= NVL (?, TO_DATE ('31-12-9999', 'dd-mm-yyyy'))  " +
							" AND (p.dt_Mod is null OR p.dt_Mod = (SELECT MAX(dt_Mod) FROM Sit_D_Persona WHERE codfisc = p.codfisc)) " +
							" AND pf.relaz_par NOT IN ('XE') " +
							"order by nvl(dt_inizio_dato, dt_fine_dato) , nvl(dt_fine_dato, dt_inizio_dato), cognome, nome, relaz_par ";
						
						
						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						log.debug("AnagrafeDwhLogic - STORICO FAMIGLIA - SQL["+sql+"]");
						log.debug("Chiave: [" + chiave + "]");
						ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

						while (rs.next()){
							AnagrafeStorico ana = new AnagrafeStorico();
							ana.setMatricola(rs.getString("MATRICOLA"));
							ana.setCognome(rs.getString("COGNOME"));
							ana.setNome(rs.getString("NOME"));
							ana.setInizioFamiglia(tornaValoreRS(rs,"DATA_INIZIO"));
							ana.setFineFamiglia(tornaValoreRS(rs,"DATA_FINE"));
							java.util.Date dtIniCiv = rs.getDate("DT_INIZIO_CIV");
							String dtIniCivStr = dtIniCiv == null ? "-" : sdf.format(dtIniCiv);
							if (dtIniCivStr.equals("01/01/1000")) dtIniCivStr = "-";
							ana.setInizioResidenza(dtIniCivStr);
							java.util.Date dtFinCiv = rs.getDate("DT_FINE_CIV");
							String dtFinCivStr = dtFinCiv == null ? "-" : sdf.format(dtFinCiv);
							if (dtFinCivStr.equals("31/12/9999")) dtFinCivStr = "-";
							ana.setFineResidenza(dtFinCivStr);					
							ana.setSesso(rs.getString("SESSO"));
							ana.setCivico(rs.getString("NUM_CIV"));
							ana.setCodFiscale(rs.getString("CODICE_FISC"));
							ana.setCodiceVia(rs.getString("COD_VIA"));
							ana.setDescrVia(rs.getString("DESCR_VIA"));
							ana.setCodFamiglia(rs.getString("COD_FAMIGLIA"));
							ana.setFkFamiglia(rs.getString("FK_FAMIGLIA"));

							//RECUPERO I COMPONENTI DELLA FAMIGLIA PER OGNI STATO DELLO STORICO
							ArrayList listfam = new ArrayList();
							
							Date dataInizioRif = DateFormat.stringToDate(ana.getInizioFamiglia(),"dd/MM/yyyy");
							Date dataFineRif = DateFormat.stringToDate(ana.getFineFamiglia(),"dd/MM/yyyy");
							
							log.debug("Param["+ana.getFkFamiglia()+"]");
							log.debug("Param["+dataInizioRif+"]");
							log.debug("Param["+dataFineRif+"]");
							
							log.debug("AnagrafeDwhLogic - RECUPERO I COMPONENTI DELLA FAMIGLIA PER OGNI STATO DELLO STORICO - SQL["+sqlFam+"]");
							
							PreparedStatement psfam = conn.prepareStatement(sqlFam);
							
							psfam.setString(1,ana.getFkFamiglia());
							psfam.setDate(2,dataInizioRif);
							psfam.setDate(3,dataFineRif);
							psfam.setDate(4,dataInizioRif);
							psfam.setDate(5,dataFineRif);
							psfam.setDate(6,dataInizioRif);
							psfam.setDate(7,dataFineRif);
							psfam.setDate(8,dataInizioRif);
							psfam.setDate(9,dataFineRif);
							psfam.setDate(10,dataInizioRif);
							psfam.setDate(11,dataFineRif);
							
							ResultSet rsfam = psfam.executeQuery();
							while (rsfam.next())
							{
								FamiliariStorico fs = new FamiliariStorico();
								
								fs.setDataInizioRif(dataInizioRif);
								fs.setDataFineRif(dataFineRif);
								
								fs.setId(rsfam.getString("ID"));
								fs.setCognome(rsfam.getString("COGNOME"));
								fs.setNome(rsfam.getString("NOME"));
								fs.setDtNascita(DateFormat.dateToString(rsfam.getDate("DATA_NASCITA"),"dd/MM/yyyy"));
								fs.setTipoParentela(rsfam.getString("RELAZ_PAR"));
								fs.setDtInizio(DateFormat.dateToString(rsfam.getDate("DT_INIZIO_DATO"),"dd/MM/yyyy"));
								fs.setDtFine(DateFormat.dateToString(rsfam.getDate("DT_FINE_DATO"),"dd/MM/yyyy"));
								listfam.add(fs);
							}
							rsfam.close();
							psfam.close();
							ana.setElencoFamiliari(listfam);

							inds.add(ana);
						}
						ht.put(ANAGRAFE_INDIRIZZI, inds);
						return ht;
					}
				catch (Exception e) {
					throw e;
				}
				finally
				{
					if (conn != null)
						if (!conn.isClosed())
							conn.close();
				}
		}
	
	/**
	 * METODO CHE RITORNA, DATO UN CODICE ANAGRAFICO IL CODICE_NAZIONALE GIUSTO 
	 * PRESO DALLE TABELLE DEL CATASTO.
	 * IL METODO E' STATO IMPLEMENTATO PER FAR FRONTE AI PROBLEMI DI COLLEGAMENTO 
	 * FRA ANAGRAFE E CATASTO NEI CATASTI COMUNALI MULTISEZIONE (ES. ORISTANO)
	 * @param conn
	 * @param pkAnagrafe
	 * @param codNaz
	 * @return
	 * @throws Exception
	 */
	private String getCodiceNazionale(Connection conn, String pkAnagrafe, String codNaz) throws Exception {
			String sql = "SELECT CV.ID_ORIG_VIA, CV.CIV_LIV1 , ente_s_v.CODENT" +
				     " FROM sit_d_persona_civico pc, " +
				     "     sit_d_civico_via_v CV, "+
				     "     ente_s_v "+
				     " WHERE pc.id_ext_d_persona = ?"+
				     " AND pc.id_ext_d_civico = CV.id_ext"+
				     " AND pc.dt_fine_val IS NULL"+
				     " AND CV.dt_fine_val IS NULL"+
				     " AND ente_s_v.id_sorgente = pC.fk_ente_sorgente";
			
			String sql2 = "select CIV.COD_NAZIONALE from SITICIVI civ " + 
						  " ,SITIDSTR str " +
						  " WHERE ? = STR.NUMERO " + 
						  "   AND ? = LPAD(CIV.CIVICO,5,'0') " + 
						  "   AND CIV.PKID_STRA = STR.PKID_STRA " +
						  "   and str.data_fine_val = to_date('99991231000000', 'YYYYMMDDHH24MISS') " +
						  "   and civ.data_fine_val = to_date('99991231000000', 'YYYYMMDDHH24MISS') ";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pkAnagrafe);
			log.debug("query1 - getCodiceNazionale: " + sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, rs.getString("ID_ORIG_VIA"));
				pstmt2.setString(2, rs.getString("CIV_LIV1"));
				log.debug("query2 - getCodiceNazionale: " + sql);
				ResultSet rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					codNaz = rs2.getString("COD_NAZIONALE");
				}
				if (rs2 != null)
					rs2.close();
				if (pstmt2 != null)
					pstmt2.close();
			}
		if (rs != null)
			rs.close();
		if (pstmt != null)
			pstmt.close();
		//per default restituisce il codice belfiore dell'applicativo, ricevuto come parametro
		return codNaz; 
	}
	
	@Override
	protected String elaboraOrderByMascheraRicerca(Vector listaPar) throws NumberFormatException, Exception	{
		String orderBy = super.elaboraOrderByMascheraRicerca(listaPar);
		if (orderBy != null) {
			//se l'ordinamento è per cognome, si aggiunge anche ordinamento per nome (e viceversa)
			if (orderBy.toUpperCase().indexOf("ORDER BY COGNOME") > -1) {
				if (orderBy.toUpperCase().indexOf(" DESC") > -1) {
					orderBy += ", NOME DESC ";
				} else {
					orderBy += ", NOME ";
				}
			} else if (orderBy.toUpperCase().indexOf("ORDER BY NOME") > -1) {
				if (orderBy.toUpperCase().indexOf(" DESC") > -1) {
					orderBy += ", COGNOME DESC ";
				} else {
					orderBy += ", COGNOME ";
				}
			}
		}			
		return orderBy;
	}
	
	protected void setResidenzaDaStorico(Connection conn, Anagrafe ana) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			String residenzaStr = "";
			String sql = getProperty("sql.STORICO_INDIRIZZI");
			ps = conn.prepareStatement(sql);
			ps.setString(1, ana.getId());
			rs = ps.executeQuery();
			ArrayList<String> datiRes = new ArrayList<String>();			
			String dtIniPrec = null;
			while (rs.next()) {
				String sedime = rs.getString("VIASEDIME") == null ? "" : rs.getString("VIASEDIME");
				String descVia = rs.getString("VIADESCRIZIONE") == null ? "" : (" " + rs.getString("VIADESCRIZIONE"));
				String civico = rs.getObject("CIVICO") == null ? "" : (", " + rs.getString("CIVICO"));
				String residenza = sedime + descVia + civico;
				String dtIni = rs.getObject("INIZIO_RES") == null ? "01/01/1000" : sdf.format(rs.getDate("INIZIO_RES"));
				String dtFin = rs.getObject("FINE_RES") == null ? (dtIniPrec == null ? "31/12/9999" : dtIniPrec) : sdf.format(rs.getDate("FINE_RES"));
				datiRes.add(residenza + "|" + dtIni + "|" + dtFin);
				dtIniPrec = dtIni;
			}
			
			java.util.Date dtIni = sdf.parse("01/01/1000");
			java.util.Date dtFin = sdf.parse("31/12/9999");
			HashMap idStorici = caricaIdStorici(ana.getId());
			String dateStr = (String)idStorici.get(ana.getId());
			if (dateStr != null) {
				String[] dateStrArr = dateStr.split("-");
				if (dateStrArr.length > 0 && dateStrArr[0] != null && !dateStrArr[0].trim().equals("")) {
					dtIni = sdf.parse(dateStrArr[0].trim());
				}
				if (dateStrArr.length > 1 && dateStrArr[1] != null && !dateStrArr[1].trim().equals("")) {
					dtFin = sdf.parse(dateStrArr[1].trim());
				}
			}			
			
			for (String datoRes : datiRes) {
				String[] datoResArr = null;
				java.util.Date dtIniRes = sdf.parse("01/01/1000");
				java.util.Date dtFinRes = sdf.parse("31/12/9999");
				if (datoRes != null) {
					datoResArr = datoRes.split("\\|");
					if (datoResArr.length > 1 && datoResArr[1] != null && !datoResArr[1].trim().equals("")) {
						dtIniRes = sdf.parse(datoResArr[1].trim());
					}
					if (datoResArr.length > 2 && datoResArr[2] != null && !datoResArr[2].trim().equals("")) {
						dtFinRes = sdf.parse(datoResArr[2].trim());
					}
				}
				boolean add = (dtIniRes.getTime() <= dtFin.getTime() && dtFinRes.getTime() >= dtIni.getTime()) ||
						(dtFinRes.getTime() >= dtIni.getTime() && dtIniRes.getTime() <= dtFin.getTime());				
				if (add && datoResArr != null && datoResArr.length > 0) {
					if (!residenzaStr.equals("")) {
						residenzaStr += "</span><br /><span class=\"TXTviewTextBox\">";
					}
					String res = datoResArr[0].trim();
					java.util.Date dtIniResView = dtIniRes.getTime() > dtIni.getTime() ? dtIniRes : null;
					java.util.Date dtFinResView = dtFinRes.getTime() < dtFin.getTime() ? dtFinRes : null;
					if (dtIniResView != null && dtFinResView != null) {
						res += "</span><span class=\"TXTmainLabel\">";
						res += " (dal " + sdf.format(dtIniResView) + " al " + sdf.format(dtFinResView) + ")";
					} else if (dtIniResView != null) {
						res += "</span><span class=\"TXTmainLabel\">";
						res += " (dal " + sdf.format(dtIniResView) + ")";
					} else if (dtFinResView != null) {
						res += "</span><span class=\"TXTmainLabel\">";
						res += " (fino al " + sdf.format(dtFinResView) + ")";
					}
					residenzaStr += res;
				}
			}

			if (residenzaStr.equals(""))
				residenzaStr = "-";
			ana.setResidenza(residenzaStr);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();
				if (ps != null && !ps.isClosed())
					ps.close();
			} catch (Exception e1) {
				throw e1;
			}
		}
	}


}
