/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.escsolution.escwebgis.tributi.bean.Contribuente;
import it.escsolution.escwebgis.tributi.bean.ContribuentiFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TributiContribuentiLogic extends EscLogic{
	private String appoggioDataSource;
			public TributiContribuentiLogic(EnvUtente eu) {
						super(eu);
						appoggioDataSource=eu.getDataSource();
					}
		private final static String SQL_SELECT_LISTA= "select * from (" +
		" select ROWNUM as N,cognome,nome,COD_CONTRIBUENTE,DENOMINAZIONE,INDIRIZZO,CODICE_FISCALE,PARTITA_IVA, provenienza from("+
		" select ROWNUM as N,"+
		"decode(tri_contribuenti.UK_CODI_CONTRIBUENTE,null,'-',tri_contribuenti.UK_CODI_CONTRIBUENTE) AS COD_CONTRIBUENTE," +
		"decode(tri_contribuenti.COGNOME,null,'-',tri_contribuenti.COGNOME) AS COGNOME," +
		"decode(tri_contribuenti.NOME,null,'-',tri_contribuenti.NOME) AS NOME," +
		"decode(tri_contribuenti.DENOMINAZIONE,null,'-',tri_contribuenti.DENOMINAZIONE) AS DENOMINAZIONE," +
		"decode(tri_contribuenti.DESCR_INDIRIZZO,null,'-',tri_contribuenti.DESCR_INDIRIZZO) AS INDIRIZZO," +
		"decode(tri_contribuenti.CODICE_FISCALE,null,'-',tri_contribuenti.CODICE_FISCALE) AS CODICE_FISCALE," +
		"decode(tri_contribuenti.PARTITA_IVA,null,'-',tri_contribuenti.PARTITA_IVA) AS PARTITA_IVA," +
		"decode(tri_contribuenti.provenienza,null,'-',tri_contribuenti.provenienza) AS provenienza"+
		" from tri_contribuenti WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from tri_contribuenti WHERE 1=?" ;

		private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from tri_contribuenti WHERE 1=?" ;

		private final static String SQL_SELECT_CONTRIBUENTI_OGGETTO_ICI = "" +
		" select UK_CODI_CONTRIBUENTE " +
		" from tri_contribuenti "+
		" WHERE  to_char(tri_contribuenti.UK_CODI_CONTRIBUENTE) = ?";

		private final static String SQL_SELECT_CONTRIBUENTI_OGGETTO_TARSU = ""+
		"select decode(tri_contribuenti.UK_CODI_CONTRIBUENTE,null,'-',tri_contribuenti.UK_CODI_CONTRIBUENTE) AS UK_CODI_CONTRIBUENTE, " +
		"tri_contribuenti.PROVENIENZA, " +
		"decode(tri_contribuenti.COGNOME,null,'-',tri_contribuenti.COGNOME) AS COGNOME, " +
		"decode(tri_contribuenti.NOME,null,'-',tri_contribuenti.NOME) AS NOME, " +
		"decode(tri_contribuenti.SESSO,null,'-',tri_contribuenti.SESSO) AS SESSO, " +
		//"decode(tri_contribuenti.DATA_NASCITA,null,'-',tri_contribuenti.DATA_NASCITA) AS DATA_NASCITA, " +
		//"nvl(to_char(tri_contribuenti.DATA_NASCITA,'dd/mm/yyyy'),'-') AS DATA_NASCITA," +
		"nvl(tri_contribuenti.DATA_NASCITA,'-') AS DATA_NASCITA," +
		"decode(tri_contribuenti.DESCR_COMUNE_NASCITA,null,'-',tri_contribuenti.DESCR_COMUNE_NASCITA) AS COMUNE_NASCITA, " +
		"decode(tri_contribuenti.DENOMINAZIONE,null,'-',tri_contribuenti.DENOMINAZIONE) AS DENOMINAZIONE,  " +
		//"decode(tri_contribuenti.PIANO,null,'-',tri_contribuenti.PIANO) AS PIANO,  " +
		//"decode(tri_contribuenti.SCALA,null,'-',tri_contribuenti.SCALA) AS SCALA,  " +
		"decode(tri_contribuenti.INTERNO,null,'-',tri_contribuenti.INTERNO) AS INTERNO, " +
		"decode(tri_contribuenti.DESCR_INDIRIZZO,null,'-',tri_contribuenti.DESCR_INDIRIZZO) AS INDIRIZZO, " +
		"decode(tri_contribuenti.COMUNE_RESIDENZA,null,'-',tri_contribuenti.COMUNE_RESIDENZA) AS COMUNE_RESIDENZA, " +
		"decode(tri_contribuenti.CODICE_FISCALE,null,'-',tri_contribuenti.CODICE_FISCALE) AS COD_FISCALE,  " +
		"decode(tri_contribuenti.PARTITA_IVA,null,'-',tri_contribuenti.PARTITA_IVA) AS PARTITA_IVA  " +
		"from tri_contribuenti, tri_oggetti_tarsu WHERE  " +
		//"tri_contribuenti.FK_ANAGRAFE = tri_oggetti_tarsu.FK_ANAGRAFE  " +
		//"tri_contribuenti.UK_CODI_CONTRIBUENTE = tri_oggetti_tarsu.FK_CONTRIBUENTE " +
		"tri_contribuenti.CODICE_CONTRIBUENTE = tri_oggetti_tarsu.FK_CONTRIBUENTE " +
		"and tri_contribuenti.PROVENIENZA = tri_oggetti_tarsu.PROVENIENZA " +
		"and tri_contribuenti.FK_COMUNI = tri_oggetti_tarsu.FK_COMUNI  " +
		"and tri_oggetti_tarsu.PK_SEQU_OGGETTI_TARSU = ? ";
		
		private final static String SQL_SELECT_CONTRIBUENTE_INDIRIZZO_ANAGRAFE = "SELECT trim(nvl(SIT_D_VIA.VIASEDIME,'')) VIASEDIME, " + 
		"trim(nvl(SIT_D_VIA.DESCRIZIONE,'')) VIADESCRIZIONE, " +
		"TO_NUMBER (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"numero\"]/@valore' ) )  || " +
		"DECODE(NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"barrato\"]/@valore' ), ''), '', '', '/' || NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"barrato\"]/@valore' ), ''))  || " +
		"DECODE(NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' ), ''), '', '', '/' || NVL (EXTRACTVALUE (VALUE (tab_civicocomposto), '/civicocomposto/att[@tipo=\"subbarrato\"]/@valore' ), ''))  AS CIVICO, " +
		"ENTE_S.DESCRIZIONE " +
		"FROM sit_d_persona, sit_comune, SIT_D_PERS_FAM, SIT_STATO, SIT_D_PERSONA_CIVICO , SIT_D_CIVICO, SIT_D_VIA, " +
		"sit_ente, " +
		"TABLE (XMLSEQUENCE (EXTRACT (nvl(civico_composto,'<civicocomposto/>'), 'civicocomposto'))) tab_civicocomposto, " +
		"SIT_COMUNE COM_MOR, SIT_COMUNE COM_IMM , SIT_COMUNE COM_EMI " +
		"WHERE sit_comune.id_ext (+) = sit_d_persona.ID_EXT_COMUNE_NASCITA " +
		"and sit_comune.DT_FINE_VAL is null " +
		"AND SIT_D_PERSONA.ID_EXT = SIT_D_PERS_FAM.ID_EXT_D_PERSONA (+) " +
		"AND SIT_D_PERS_FAM.DT_FINE_VAL IS NULL and sit_STATO.id_ext = sit_d_persona.ID_EXT_STATO " +
		"and sit_STATO.DT_FINE_VAL is null " +
		"AND SIT_D_PERSONA.ID_EXT = SIT_D_PERSONA_CIVICO.ID_EXT_D_PERSONA (+) " +
		"AND SIT_D_PERSONA_CIVICO.DT_FINE_VAL IS NULL AND SIT_D_PERSONA_CIVICO.ID_EXT_D_CIVICO = SIT_D_CIVICO.ID_EXT (+) " +
		"AND SIT_D_CIVICO.DT_FINE_VAL IS NULL AND SIT_D_CIVICO.ID_EXT_D_VIA = SIT_D_VIA.ID_EXT (+) " +
		"AND SIT_D_VIA.DT_FINE_VAL IS NULL AND COM_MOR.ID_EXT (+)  = SIT_D_PERSONA.ID_EXT_COMUNE_MOR " + 
		"AND COM_MOR.DT_FINE_VAL IS NULL AND COM_EMI.ID_EXT (+)  = SIT_D_PERSONA.ID_EXT_COMUNE_EMI " +
		"AND COM_EMI.DT_FINE_VAL IS NULL AND COM_IMM.ID_EXT (+) = SIT_D_PERSONA.ID_EXT_COMUNE_IMM " +
		"AND COM_IMM.DT_FINE_VAL IS NULL " +
		"AND sit_d_persona.CODFISC = ?";
		
		private final static String SQL_SELECT_CONTRIBUENTE_ESISTE_TAB_SIATEL = "select * from tabs where table_name = 'MI_SIATEL_P_FIS'";
		private final static String SQL_SELECT_CONTRIBUENTE_ESISTE_SYN_TAB_SIATEL = "select * from syn where synonym_name = 'MI_SIATEL_P_FIS'";
		private final static String SQL_SELECT_CONTRIBUENTE_INDIRIZZO_SIATEL = "select * from MI_SIATEL_P_FIS where COD_FISC_PERSONA = ?";	


	public Hashtable mCaricareDettaglioContribuente(String codCont) throws Exception{
					// carico la lista dei terreni e la metto in un hashtable
					Hashtable ht = new Hashtable();
					Connection conn = null;

					// faccio la connessione al db
					try {
						conn = this.getConnection();

						this.initialize();
						String sql = "select decode(tri_contribuenti.UK_CODI_CONTRIBUENTE,null,'-',tri_contribuenti.UK_CODI_CONTRIBUENTE) AS COD_CONTRIBUENTE," +
						"decode(tri_contribuenti.COGNOME,null,'-',tri_contribuenti.COGNOME) AS COGNOME," +
						"decode(tri_contribuenti.NOME,null,'-',tri_contribuenti.NOME) AS NOME," +
						"decode(tri_contribuenti.SESSO,null,'-',tri_contribuenti.SESSO) AS SESSO," +
						"nvl(tri_contribuenti.data_nascita, '-') AS DATA_NASCITA," +
						"decode(tri_contribuenti.DESCR_COMUNE_NASCITA,null,'-',tri_contribuenti.DESCR_COMUNE_NASCITA) AS COMUNE_NASCITA," +
						"decode(tri_contribuenti.DENOMINAZIONE,null,'-',tri_contribuenti.DENOMINAZIONE) AS DENOMINAZIONE, " +
						"/*decode(tri_contribuenti.PIANO,null,'-',tri_contribuenti.PIANO)*/ '' AS PIANO, " +
						"/*decode(tri_contribuenti.SCALA,null,'-',tri_contribuenti.SCALA)*/ '' AS SCALA, " +
						"decode(tri_contribuenti.INTERNO,null,'-',tri_contribuenti.INTERNO) AS INTERNO, " +
						"decode(tri_contribuenti.DESCR_INDIRIZZO,null,'-',tri_contribuenti.DESCR_INDIRIZZO) AS INDIRIZZO, " +
						"decode(tri_contribuenti.COMUNE_RESIDENZA,null,'-',tri_contribuenti.COMUNE_RESIDENZA) AS COMUNE_RESIDENZA, " +
						"decode(tri_contribuenti.CODICE_FISCALE,null,'-',tri_contribuenti.CODICE_FISCALE) AS COD_FISCALE, " +
						"decode(tri_contribuenti.PARTITA_IVA,null,'-',tri_contribuenti.PARTITA_IVA) AS PARTITA_IVA, " +
						"decode(tri_contribuenti.PROVENIENZA,null,'-',tri_contribuenti.PROVENIENZA) AS PROVENIENZA "+
						"from tri_contribuenti WHERE UK_CODI_CONTRIBUENTE=? ";

						int indice = 1;
						this.setString(indice,codCont);
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						Contribuente cont = new Contribuente();
						if (rs.next()){
							cont.setCodContribuente(rs.getString("COD_CONTRIBUENTE"));
							cont.setCognome(rs.getString("COGNOME"));
							cont.setNome(rs.getString("NOME"));
							cont.setSesso(rs.getString("SESSO"));
							cont.setDataNascita(rs.getString("DATA_NASCITA"));
							cont.setDesComuneNascita(rs.getString("COMUNE_NASCITA"));
							cont.setDenominazione(rs.getString("DENOMINAZIONE"));
						//	cont.setPiano(rs.getString("PIANO"));
							//cont.setScala(rs.getString("SCALA"));
							cont.setInterno(rs.getString("INTERNO"));
							cont.setIndirizzo(rs.getString("INDIRIZZO"));
							cont.setResidenza(rs.getString("COMUNE_RESIDENZA"));							
							cont.setCodFiscale(rs.getString("COD_FISCALE"));
							cont.setPartitaIVA(rs.getString("PARTITA_IVA"));
							cont.setProvenienza(rs.getString("PROVENIENZA"));
							ArrayList<String> alAnagrafe = getIndirizzoAnagrafe(conn, cont.getCodFiscale());
							cont.setAnagrafe(alAnagrafe.get(0).trim());
							cont.setComune(alAnagrafe.get(1).trim());
							ArrayList<String> alSiatel = getIndirizzoSiatel(conn, cont.getCodFiscale());
							cont.setSiatel(alSiatel.get(0).trim());
							cont.setComuneSiatel(alSiatel.get(1).trim());
						}
						ht.put("CONTRIBUENTE",cont);
						
						/*INIZIO AUDIT*/
						try {
							Object[] arguments = new Object[1];
							arguments[0] = codCont;
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
	
	private ArrayList<String> getIndirizzoAnagrafe(Connection conn, String codFiscale) throws Exception {
		ArrayList<String> alAnagrafe = new ArrayList<String>();
		alAnagrafe.add(""); //default
		alAnagrafe.add(""); //default
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_INDIRIZZO_ANAGRAFE);
			pstmt.setString(1, codFiscale.trim());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				alAnagrafe = new ArrayList<String>();
				String viaSedime = rs.getObject("VIASEDIME") == null ? "" : rs.getString("VIASEDIME").trim();
				String viaDescrizione = rs.getObject("VIADESCRIZIONE") == null ? "" : rs.getString("VIADESCRIZIONE").trim();
				String civico = rs.getObject("CIVICO") == null ? "" : rs.getString("CIVICO").trim();
				String anagrafe = viaSedime;
				if (!viaDescrizione.equals("")) {
					if (!anagrafe.equals("")) {
						anagrafe += " ";
					}
					anagrafe += viaDescrizione;
				}
				if (!civico.equals("")) {
					if (!anagrafe.equals("")) {
						anagrafe += ", ";
					}
					anagrafe += civico;
				}				
				alAnagrafe.add(anagrafe);
				alAnagrafe.add(rs.getObject("DESCRIZIONE") == null ? "" : rs.getString("DESCRIZIONE").trim());
			}
			return alAnagrafe;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	private ArrayList<String> getIndirizzoSiatel(Connection conn, String codFiscale) throws Exception {
		ArrayList<String> alSiatel = new ArrayList<String>();
		alSiatel.add(""); //default
		alSiatel.add(""); //default
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//verifico prima se esiste la tabella, o il sinonimo, MI_SIATEL_P_FIS
			pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_ESISTE_TAB_SIATEL);
			boolean tabSiatel = pstmt.executeQuery().next();
			if (!tabSiatel) {
				pstmt.cancel();
				pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_ESISTE_SYN_TAB_SIATEL);
				tabSiatel = pstmt.executeQuery().next();
			}
			pstmt.cancel();
			if (tabSiatel) {
				pstmt = conn.prepareStatement(SQL_SELECT_CONTRIBUENTE_INDIRIZZO_SIATEL);
				pstmt.setString(1, codFiscale.trim());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					alSiatel = new ArrayList<String>();
					alSiatel.add(rs.getObject("INDIRIZZO") == null ? "" : rs.getString("INDIRIZZO").trim());
					alSiatel.add(rs.getObject("COMUNE_RESIDENZA") == null ? "" : rs.getString("COMUNE_RESIDENZA").trim());
				}
			}
			return alSiatel;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}

	public Hashtable mCaricareDettaglioContribuenteDaNinc(String codice) throws Exception{

		Connection conn = null;
		try {
			conn = this.getConnection();
			//recupero ninc e provenienza dal parametro passato
			String ninc = codice.substring(0,codice.indexOf('|'));
			String prov = codice.substring(codice.indexOf('|')+1);


			this.initialize();
		/*	String sql = "select decode(tri_contribuenti.UK_CODI_CONTRIBUENTE,null,'-',tri_contribuenti.UK_CODI_CONTRIBUENTE) AS UK_CONTRIBUENTE " +
			"from tri_contribuenti WHERE to_char(CODICE_CONTRIBUENTE)=? " +
			"and tri_contribuenti.PROVENIENZA = ?";
        */
			String sql="SELECT DECODE (tri_contribuenti.uk_codi_contribuente,NULL, '-',tri_contribuenti.uk_codi_contribuente) AS uk_contribuente," +
					   "NVL(tri_contribuenti.PROVENIENZA,' ') AS PROVENIENZA," +
					   "NVL(tri_contribuenti.COGNOME,' ') AS COGNOME, " +
					   "NVL(tri_contribuenti.NOME,' ') AS NOME, " +
					   "NVL(tri_contribuenti.DENOMINAZIONE,' ') AS DENOMINAZIONE, " +
					   "NVL(tri_contribuenti.CODICE_FISCALE,' ') AS CODICE_FISCALE, " +
					   "NVL(tri_contribuenti.PARTITA_IVA,' ') AS PARTITA_IVA " +
					   "FROM tri_contribuenti " +
					   "WHERE TO_CHAR (codice_contribuente) = ? " +
					   "AND tri_contribuenti.provenienza = ? ";
			int indice = 1;
			this.setString(indice,ninc);
			indice ++;
			this.setString(indice,prov);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			String uk_contribuente="";

			if (rs.next()){
				uk_contribuente = rs.getString("UK_CONTRIBUENTE");

			}
			return mCaricareDettaglioContribuente(uk_contribuente);

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

public Hashtable mDettaglioContribuenteDaNinc(String codice) throws Exception{

		Connection conn = null;
		try {
			conn = this.getConnection();
			//recupero ninc e provenienza dal parametro passato
			String ninc = codice.substring(0,codice.indexOf('|'));
			String prov = codice.substring(codice.indexOf('|')+1);


			this.initialize();
			String sql = "select decode(tri_contribuenti.UK_CODI_CONTRIBUENTE,null,'-',tri_contribuenti.UK_CODI_CONTRIBUENTE) AS UK_CONTRIBUENTE " +
			"from tri_contribuenti WHERE to_char(CODICE_CONTRIBUENTE)=? " +
			"and tri_contribuenti.PROVENIENZA = ?";

		/*	String sql="SELECT DECODE (tri_contribuenti.uk_codi_contribuente,NULL, '-',tri_contribuenti.uk_codi_contribuente) AS uk_contribuente," +
					   "NVL(tri_contribuenti.PROVENIENZA,' ') AS PROVENIENZA," +
					   "NVL(tri_contribuenti.COGNOME,' ') AS COGNOME, " +
					   "NVL(tri_contribuenti.NOME,' ') AS NOME, " +
					   "NVL(tri_contribuenti.DENOMINAZIONE,' ') AS DENOMINAZIONE, " +
					   "NVL(tri_contribuenti.CODICE_FISCALE,' ') AS CODICE_FISCALE, " +
					   "NVL(tri_contribuenti.PARTITA_IVA,' ') AS PARTITA_IVA " +
					   "FROM tri_contribuenti " +
					   "WHERE TO_CHAR (codice_contribuente) = ? " +
					   "AND tri_contribuenti.provenienza = ? ";
		*/
			int indice = 1;
			this.setString(indice,ninc);
			indice ++;
			this.setString(indice,prov);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			String uk_contribuente="";

			if (rs.next()){
				uk_contribuente = rs.getString("UK_CONTRIBUENTE");

			}
			return mCaricareDettaglioContribuente(uk_contribuente);

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


	public Contribuente mCaricareListaContribuentiPerOggettoICI(String chiave) throws Exception{

				//Hashtable ht = new Hashtable();
				//Vector vct = new Vector();
				String sql = "";
				String conteggio = "";
				long conteggione = 0;
				Connection conn = null;

				// faccio la connessione al db
					try {
						conn = this.getConnection();
						sql = SQL_SELECT_CONTRIBUENTI_OGGETTO_ICI;

						int indice = 1;
						this.initialize();
						this.setString(indice,chiave);
						indice ++;

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						Contribuente cont = new Contribuente();
						if (rs.next()){
							cont.setCodContribuente(rs.getString("UK_CODI_CONTRIBUENTE"));
						}
						//ht.put("LISTA_CONTRIBUENTI",vct);
						return cont;
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



	/*public Contribuente mCaricareListaContribuentiPerOggettoTARSU(String chiave) throws Exception{
				return mCaricareListaContribuentiPerOggettoICI(chiave);
			}*/



	public Contribuente mCaricareListaContribuentiPerOggettoTARSU(String chiave) throws Exception{

	//Hashtable ht = new Hashtable();
				//Vector vct = new Vector();
				String sql = "";
				String conteggio = "";
				long conteggione = 0;
				Connection conn = null;

				// faccio la connessione al db
					try {
						conn = this.getConnection();
						sql = SQL_SELECT_CONTRIBUENTI_OGGETTO_TARSU;

						int indice = 1;
						this.initialize();
						this.setString(indice,chiave);
						indice ++;

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						Contribuente cont = new Contribuente();
						if (rs.next()){
							cont.setCodContribuente(rs.getString("UK_CODI_CONTRIBUENTE"));
							cont.setCodFiscale(rs.getString("COD_FISCALE"));
							cont.setPartitaIVA(rs.getString("PARTITA_IVA"));
							cont.setCognome(rs.getString("COGNOME"));
							cont.setNome(rs.getString("NOME"));
							cont.setDenominazione(rs.getString("DENOMINAZIONE"));
							cont.setProvenienza(rs.getString("PROVENIENZA"));
						}
						//ht.put("LISTA_CONTRIBUENTI",vct);
						return cont;
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

	public Hashtable mCaricareListaContribuenti(Vector listaPar, ContribuentiFinder finder) throws Exception{
		// carico la lista dei terreni e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			java.sql.ResultSet rs;
/* non conto			this.initialize();
			this.setInt(indice,1);
			indice ++;
			prepareStatement(sql);
			 rs = executeQuery(conn);
			if (rs.next()){
					conteggione = rs.getLong("conteggio");
			}
*/
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
					//controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					String appo ="";
					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
						String ente = controllo.substring(0,controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
						appo= "'"+controllo.substring(controllo.indexOf("|")+1);
						sql = sql + " and tri_contribuenti.CODENTE = '" +ente + "'" ;
						//////
						//String appo = finder.getKeyStr();
						String chiave = "";
						if (appo.indexOf('|')>0){
							chiave =appo.substring(0,appo.indexOf('|'));
							chiave = chiave + "'";
							String tipo =appo.substring(appo.indexOf('|')+1,appo.length()-1);
							sql = sql + " and tri_contribuenti.PROVENIENZA ='" +tipo+"' "  ;
						}else
							chiave = appo;

						if ("".equals(chiave))
							chiave = "'-1'";
						sql = sql + " and tri_contribuenti.CODICE_CONTRIBUENTE in (" + chiave + ")" ;
					}
					else
						sql = sql + " and tri_contribuenti.UK_CODI_CONTRIBUENTE in (" + controllo + ")" ;
					////////////
				}


				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + " order by tri_contribuenti.COGNOME,tri_contribuenti.NOME";
				if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql) ;
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Contribuente cont = new Contribuente();
						cont.setCodContribuente(rs.getString("COD_CONTRIBUENTE"));
						cont.setCognome(rs.getString("COGNOME"));
						cont.setNome(rs.getString("NOME"));
						cont.setDenominazione(rs.getString("DENOMINAZIONE"));
						cont.setIndirizzo(rs.getString("INDIRIZZO"));
						cont.setCodFiscale(rs.getString("CODICE_FISCALE"));
						cont.setPartitaIVA(rs.getString("PARTITA_IVA"));
						cont.setProvenienza(rs.getString("Provenienza"));

						vct.add(cont);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put("LISTA_CONTRIBUENTI",vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
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


}
