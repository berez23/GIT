/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.esatri.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.esatri.bean.Contribuente;
import it.escsolution.escwebgis.esatri.bean.ContribuenteFinder;
import it.escsolution.escwebgis.esatri.bean.RiscossioneContabile;
import it.escsolution.escwebgis.esatri.bean.RiscossioneViolazione;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EsatriContribuentiLogic extends EscLogic{
	private String appoggioDataSource;
	public EsatriContribuentiLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String ESATRI_CONTRIBUENTI = "ESATRI_CONTRIBUENTI";
	public static final String LISTA = "LISTA_ESATRI_CONTRIBUENTI";
	public static final String LISTA_RIS_VIO = "LISTA_RISCOSSIONI_VIO";
	public static final String LISTA_RIS_CON = "LISTA_RISCOSSIONI_CON";
	public static final String ESATRICON = EsatriContribuentiLogic.class.getName() + "@ESATRICON";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM( " +
					"select rownum n,CODICE_FISCALE_CONTRIBUENTE," +
					"CODICE_FORNITURA,COD_ENTE,ANNO_RIFERIMENTO," +
					"SUBSTR(DATA_SCADENZA,1,2)||'/'||SUBSTR(DATA_SCADENZA,3,2)||'/'||SUBSTR(DATA_SCADENZA,5,4) DATA_SCADENZA," +
					"PROGR_INVIO,TIPO FROM(" +
					"select distinct CODICE_FISCALE_CONTRIBUENTE, " +
					"CODICE_FORNITURA," +
					"TO_NUMBER(SUBSTR(CODICE_FORNITURA,1,3)) COD_ENTE," +
					"SUBSTR(CODICE_FORNITURA,4,4) ANNO_RIFERIMENTO," +
					"SUBSTR(CODICE_FORNITURA,8,8) DATA_SCADENZA, " +
					"TO_NUMBER(SUBSTR(CODICE_FORNITURA,16)) PROGR_INVIO," +
					"'CONTABILE' AS TIPO " +
					"from MILANO.mi_esatri_ris_con " +
					"union all " +
					"select distinct CODICE_FISCALE_CONTRIBUENTE, " +
					"CODICE_FORNITURA, " +
					"TO_NUMBER(SUBSTR(CODICE_FORNITURA,1,3)) COD_ENTE," +
					"SUBSTR(CODICE_FORNITURA,4,4) ANNO_RIFERIMENTO," +
					"SUBSTR(CODICE_FORNITURA,8,8) DATA_SCADENZA," +
					"TO_NUMBER(SUBSTR(CODICE_FORNITURA,16)) PROGR_INVIO," +
					"'VIOLAZIONE' AS TIPO " +
					"from MILANO.mi_esatri_ris_vio) " +
					"WHERE 1=? " ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from " +
					"(select distinct CODICE_FISCALE_CONTRIBUENTE, " +
					"CODICE_FORNITURA, " +
					"'CONTABILE' AS TIPO " +
					"from MILANO.mi_esatri_ris_con " +
					"union " +
					"select distinct CODICE_FISCALE_CONTRIBUENTE, " +
					"CODICE_FORNITURA, " +
					"'VIOLAZIONE' AS TIPO " +
					"from MILANO.mi_esatri_ris_vio) WHERE 1=? " ;

	private final static String SQL_LISTA_RIS_CON_CONTRIBUENTE="SELECT PF.COGNOME ||' '||PF.NOME DENOMINAZ_PF," +
					"PF.COMUNE_DOMICILIO COMU_DOMI_PF, " +
					"PG.RAGIONE_SOCIALE DENOMINAZ_PG, " +
					"PG.COMUNE_DOMICILIO COMU_DOMI_PG, " +
					"CON.CODICE_CONCESSIONE, " +
					"CON.CODICE_ENTE, " +
					"CON.NUMERO_QUIETANZA, " +
					"CON.PROGRESSIVO_RECORD, " +
					"CON.TIPO_RECORD," +
					"CON.CODICE_FISCALE_CONTRIBUENTE," +
					"CON.PERIODO_RIF_VERSAMENTO," +
					"CON.NUMERO_RIFERIMENTO_QUIETANZA," +
					"CON.IMPORTO_VERSATO," +
					"CON.IMPORTO_TERRENI_AGRICOLI," +
					"CON.IMPORTO_AREE_FABBRICABILI," +
					"CON.IMPORTO_ALTRI_FABBRICATI," +
					"CON.IMPORTO_DETRAZIONE_ABIT_PRINC," +
					"CON.IMPORTO_ABITAZIONE_PRINCIPALE," +
					"CON.FLAG_QUADRATURA," +
					"CON.FLAG_REPERIBILITA," +
					"CON.TIPO_VERSAMENTO," +
					"SUBSTR(CON.DATA_VERSAMENTO_CONTRIBUENTE,7,2)||'/'||SUBSTR(CON.DATA_VERSAMENTO_CONTRIBUENTE,5,2)||'/'||SUBSTR(CON.DATA_VERSAMENTO_CONTRIBUENTE,1,4) DATA_VERSAMENTO," +
					"SUBSTR(CON.DATA_REGISTRAZIONE,7,2)||'/'||SUBSTR(CON.DATA_REGISTRAZIONE,5,2)||'/'||SUBSTR(CON.DATA_REGISTRAZIONE,1,4) DATA_REGISTRAZIONE," +
					"CON.FLAG_COMPETENZA," +
					"CON.COMUNE_IMMOBILI," +
					"CON.CAP_COMUNE_IMMOBILI," +
					"CON.NUMERO_FABBRICATI," +
					"CON.FLAG_ACCONTO_SALDO," +
					"CON.FLAG_IDENTIFICAZIONE," +
					"CON.PERIODO_RIF_VERSAMENTO," +
					"CON.CODICE_FORNITURA," +
					"CON.FK_RIVERSAMENTO " +
					"FROM MILANO.MI_ESATRI_RIS_CON CON,MILANO.MI_ESATRI_RIS_PF PF,MILANO.MI_ESATRI_RIS_PG PG " +
					"WHERE CON.CODICE_FORNITURA = ? " +
					"AND CON.CODICE_FISCALE_CONTRIBUENTE = ? " +
					"AND CON.CODICE_FORNITURA = PF.CODICE_FORNITURA (+) " +
					"AND CON.PROGRESSIVO_RECORD = PF.PROGRESSIVO_RECORD (+) " +
					"AND CON.CODICE_FORNITURA = PG.CODICE_FORNITURA (+) " +
					"AND CON.PROGRESSIVO_RECORD = PG.PROGRESSIVO_RECORD (+) " +
					"ORDER BY CON.CODICE_FORNITURA,CON.DATA_VERSAMENTO_CONTRIBUENTE ";

	private final static String SQL_LISTA_RIS_VIO_CONTRIBUENTE="SELECT PF.COGNOME ||' '||PF.NOME DENOMINAZ_PF," +
					"PF.COMUNE_DOMICILIO COMU_DOMI_PF," +
					"PG.RAGIONE_SOCIALE DENOMINAZ_PG," +
					"PG.COMUNE_DOMICILIO COMU_DOMI_PG," +
					"VIO.CODICE_CONCESSIONE," +
					"VIO.CODICE_ENTE," +
					"VIO.NUMERO_QUIETANZA," +
					"VIO.PROGRESSIVO_RECORD," +
					"VIO.TIPO_RECORD," +
					"VIO.CODICE_FISCALE_CONTRIBUENTE," +
					"SUBSTR(VIO.DATA_VERSAMENTO_CONTRIBUENTE,7,2)||'/'||SUBSTR(VIO.DATA_VERSAMENTO_CONTRIBUENTE,5,2)||'/'||SUBSTR(VIO.DATA_VERSAMENTO_CONTRIBUENTE,1,4) DATA_VERSAMENTO_CONTRIBUENTE," +
					"VIO.NUMERO_RIFERIMENTO_QUIETANZA," +
					"VIO.IMPORTO_VERSATO," +
					"VIO.IMPORTO_IMPOSTA," +
					"VIO.IMPORTO_SANZIONI_1," +
					"VIO.IMPORTO_SANZIONI_2," +
					"VIO.IMPORTO_INTERESSI," +
					"VIO.FLAG_QUADRATURA," +
					"VIO.FLAG_REPERIBILITA," +
					"VIO.TIPO_VERSAMENTO," +
					"SUBSTR(VIO.DATA_REGISTRAZIONE,7,2)||'/'||SUBSTR(VIO.DATA_REGISTRAZIONE,5,2)||'/'||SUBSTR(VIO.DATA_REGISTRAZIONE,1,4) DATA_REGISTRAZIONE," +
					"VIO.FLAG_COMPETENZA," +
					"VIO.COMUNE_IMMOBILI," +
					"VIO.CAP_COMUNE_IMMOBILI," +
					"VIO.FLAG_IDENTIFICAZIONE," +
					"VIO.ANNO_IMPOSTA_VIOLAZIONE," +
					"VIO.NUMERO_LIQUIDAZIONE," +
					"VIO.DATA_LIQUIDAZIONE," +
					"VIO.CODICE_FORNITURA," +
					"VIO.FK_RIVERSAMENTO " +
					"FROM MILANO.MI_ESATRI_RIS_VIO VIO,MILANO.MI_ESATRI_RIS_PF PF,MILANO.MI_ESATRI_RIS_PG PG " +
					"WHERE VIO.CODICE_FORNITURA = ? " +
					"AND VIO.CODICE_FISCALE_CONTRIBUENTE = ? " +
					"AND VIO.CODICE_FORNITURA = PF.CODICE_FORNITURA (+) " +
					"AND VIO.PROGRESSIVO_RECORD = PF.PROGRESSIVO_RECORD (+) " +
					"AND VIO.CODICE_FORNITURA = PG.CODICE_FORNITURA (+) " +
					"AND VIO.PROGRESSIVO_RECORD = PG.PROGRESSIVO_RECORD (+)";

	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			//recupero le chiavi di ricerca (codFornitura / CF o PI / tipoRis)
			ArrayList listParam = new ArrayList();
			while (chiave.indexOf('|') > 0) {
				listParam.add(chiave.substring(0,chiave.indexOf('|')));
				chiave = chiave.substring(chiave.indexOf('|')+1);
			}
			listParam.add(chiave);

			conn = this.getConnection();
			this.initialize();
			String sqlAna = getProperty("sql.SELECT_ANA");

			String sqlSiatelPF = "SELECT PF.COGNOME, " +
						 "PF.NOME, " +
						 "SUBSTR(PF.DATA_NASCITA,5,2)||'/'||SUBSTR(PF.DATA_NASCITA,3,2)||'/'||SUBSTR(PF.DATA_NASCITA,1,2) DATA_NASCITA, " +
						 "PF.INDIRIZZO " +
						 "FROM MILANO.MI_SIATEL_P_FIS PF " +
						 "WHERE PF.COD_FISC_PERSONA = ? ";

			String sqlSiatelPG = "SELECT PG.DENOMINAZIONE," +
						 "PG.IND_CIV_SEDE_LEGALE INDIRIZZO, " +
						 "PG.COMUNE_SEDE_LEGALE COMUNE, " +
						 "PG.CAP_SEDE_LEGALE CAP " +
						 "FROM MILANO.MI_SIATEL_P_GIU PG " +
						 "WHERE LPAD(PG.COD_FISC_INDIVIDUATO,11,'0') = LPAD(?,11,'0')";

			String sqlThebit = "select CODFISC," +
						 "PARTITAIVA," +
						 "COGNOME," +
						 "NOME," +
						 "LUOGONASCITA," +
						 "NASDATA," +
						 "DENOMINAZIONE," +
						 "INDIRIZZOESTERNO," +
						 "LUOGOESTERNO " +
						 "from sit_t_contribuenti " +
						 "where provenienza ='T' " +
						 "and (codfisc = ? or partitaiva= ? )";

			int indice = 1;

			Contribuente con = new Contribuente();
			ArrayList listaRisCon = new ArrayList();
			ArrayList listaRisVio = new ArrayList();

			String cfPI = (String)listParam.get(1);
			if (cfPI != null)
			{
				cfPI = cfPI.trim();
				this.setString(indice,cfPI);

				//controllo se è una PI o CF
				//e recupero dati contribuente
				if (cfPI.length() == 11)//PIva
				{
					con.setFlagTipoPers("PG");
					//cerco dati contribuente in siatel
					prepareStatement(sqlSiatelPG);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs.next()){
						con.setFlagFonteDati("SIATEL");
						con.setDenominazione(rs.getString("DENOMINAZIONE"));
						con.setIndirizzo(rs.getString("INDIRIZZO"));
						con.setComune(rs.getString("COMUNE"));
						con.setCap(rs.getString("CAP"));
						con.setCF_pI(cfPI);
					}
					else
					{
						//cerco in thebit
						this.initialize();
						this.setString(1,cfPI);
						this.setString(2,cfPI);
						prepareStatement(sqlThebit);
						rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						if (rs.next())
						{
							con.setFlagFonteDati("THEBIT");
							con.setDenominazione(rs.getString("DENOMINAZIONE"));
							con.setIndirizzo(rs.getString("INDIRIZZOESTERNO"));
							con.setComune(rs.getString("LUOGOESTERNO"));
							con.setCap("");
							con.setCF_pI(cfPI);
						}
					}
				}
				else
				{//CF
					con.setFlagTipoPers("PF");
					//cerco dati contribuente in anagrafe
					prepareStatement(sqlAna);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs.next()){
						con.setFlagFonteDati("ANAGRAFE");
						con.setCognome(rs.getString("COGNOME"));
						con.setNome(rs.getString("NOME"));
						con.setDataNascita(rs.getString("DATA_NASCITA"));
						con.setIndirizzo(rs.getString("INDIRIZZO"));
						con.setCF_pI(cfPI);
					}
					else
					{
						//cerco in siatel
						prepareStatement(sqlSiatelPF);
						rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						if (rs.next())
						{
							con.setFlagFonteDati("SIATEL");
							con.setCognome(rs.getString("COGNOME"));
							con.setNome(rs.getString("NOME"));
							con.setDataNascita(rs.getString("DATA_NASCITA"));
							con.setIndirizzo(rs.getString("INDIRIZZO"));
							con.setCF_pI(cfPI);
						}
						else
						{
							//cerco in thebit
							this.initialize();
							this.setString(1,cfPI);
							this.setString(2,cfPI);
							prepareStatement(sqlThebit);
							rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							if (rs.next())
							{
								con.setFlagFonteDati("THEBIT");
								con.setCognome(rs.getString("COGNOME"));
								con.setNome(rs.getString("NOME"));
								con.setDataNascita(rs.getString("NASDATA"));
								con.setIndirizzo(rs.getString("LUOGONASCITA"));
								con.setCF_pI(cfPI);
							}
						}
					}
				}

				//recupero info riscossioni contabili
				this.initialize();
				this.setString(1,(String)listParam.get(0));
				this.setString(2,cfPI);
				prepareStatement(SQL_LISTA_RIS_CON_CONTRIBUENTE);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next())
				{
					RiscossioneContabile rc = new RiscossioneContabile();
					rc.setCodConcessione(rs.getString("CODICE_CONCESSIONE"));
					rc.setCodEnte(rs.getString("CODICE_ENTE"));
					rc.setCap(rs.getString("CAP_COMUNE_IMMOBILI"));
					rc.setCF_pI(rs.getString("CODICE_FISCALE_CONTRIBUENTE"));
					rc.setDenominazionePF(rs.getString("DENOMINAZ_PF"));
					rc.setComuneDomicilioPF(rs.getString("COMU_DOMI_PF"));
					rc.setComuneDomicilioPG(rs.getString("COMU_DOMI_PG"));
					rc.setComuneImmobili(rs.getString("COMUNE_IMMOBILI"));
					rc.setDataRegistrazione(rs.getString("DATA_REGISTRAZIONE"));
					rc.setDataVersamento(rs.getString("DATA_VERSAMENTO"));
					rc.setDenominazionePG(rs.getString("DENOMINAZ_PG"));
					rc.setFkRiversamento(rs.getString("FK_RIVERSAMENTO"));
					rc.setFlagAccontoSaldo(rs.getString("FLAG_ACCONTO_SALDO"));
					rc.setFlagCompVersContr(rs.getString("FLAG_COMPETENZA"));
					rc.setFlagIdentificazione(rs.getString("FLAG_IDENTIFICAZIONE"));
					rc.setFlagQuadratura(rs.getString("FLAG_QUADRATURA"));
					String impAF = rs.getString("IMPORTO_ALTRI_FABBRICATI");
					if (impAF != null)
						impAF = round(impAF,2);
					rc.setImportoAltriFabbr(impAF);
					String impAreF = rs.getString("IMPORTO_AREE_FABBRICABILI");
					if (impAreF != null)
						impAreF = round(impAreF,2);
					rc.setImportoAreeFabbr(impAreF);
					String impDAP = rs.getString("IMPORTO_DETRAZIONE_ABIT_PRINC");
					if (impDAP != null)
						impDAP = round(impDAP,2);
					rc.setImportoDetrazione(impDAP);
					String impTA = rs.getString("IMPORTO_TERRENI_AGRICOLI");
					if (impTA != null)
						impTA = round(impTA,2);
					rc.setImportoTerreniAgricoli(impTA);
					String impV = rs.getString("IMPORTO_VERSATO");
					if (impV!= null)
						impV = round(impV,2);
					rc.setImportoVersato(impV);
					rc.setNumFabbr(rs.getString("NUMERO_FABBRICATI"));
					rc.setNumQuietanza(rs.getString("NUMERO_QUIETANZA"));
					String impAP = rs.getString("IMPORTO_ABITAZIONE_PRINCIPALE");
					if (impAP!= null)
						impAP = round(impAP,2);
					rc.setImportoAbitazPrincipale(impAP);
					rc.setPerioRifVersamento(rs.getString("PERIODO_RIF_VERSAMENTO"));
					rc.setProgRecord(rs.getString("PROGRESSIVO_RECORD"));
					rc.setRifQuietanza(rs.getString("NUMERO_RIFERIMENTO_QUIETANZA"));
					rc.setTipoRec(rs.getString("TIPO_RECORD"));
					rc.setTipoVers(rs.getString("TIPO_VERSAMENTO"));
					rc.setFlagReperibilita(rs.getString("FLAG_REPERIBILITA"));

					String codForn = rs.getString("CODICE_FORNITURA");
					rc.setCodFornitura(codForn);
					rc.setAnnoRiferimento(codForn.substring(3,7));
					rc.setDataScadenza(codForn.substring(7,9)+"/"+codForn.substring(9,11)+"/"+codForn.substring(11,15));
					rc.setProgressivoInvio(codForn.substring(15,17));


					listaRisCon.add(rc);

				}


				//recupero info riscossioni violazioni
				this.initialize();
				this.setString(1,(String)listParam.get(0));
				this.setString(2,cfPI);
				prepareStatement(SQL_LISTA_RIS_VIO_CONTRIBUENTE);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next())
				{
					RiscossioneViolazione rv = new RiscossioneViolazione();
					rv.setCodConcessione(rs.getString("CODICE_CONCESSIONE"));
					rv.setCodEnte(rs.getString("CODICE_ENTE"));
					rv.setFkRiversamento(rs.getString("FK_RIVERSAMENTO"));
					rv.setCap(rs.getString("CAP_COMUNE_IMMOBILI"));
					rv.setCF_pI(rs.getString("CODICE_FISCALE_CONTRIBUENTE"));
					rv.setComuneImmobili(rs.getString("COMUNE_IMMOBILI"));
					rv.setDataRegistrazione(rs.getString("DATA_REGISTRAZIONE"));
					rv.setFlagCompVersContr(rs.getString("FLAG_COMPETENZA"));
					rv.setFlagIdentificazione(rs.getString("FLAG_IDENTIFICAZIONE"));
					rv.setFlagQuadratura(rs.getString("FLAG_QUADRATURA"));
					String impV = rs.getString("IMPORTO_VERSATO");
					if (impV!= null)
						impV = round(impV,2);
					rv.setImportoVersato(impV);
					rv.setNumQuietanza(rs.getString("NUMERO_QUIETANZA"));
					rv.setProgRecord(rs.getString("PROGRESSIVO_RECORD"));
					rv.setRifQuietanza(rs.getString("NUMERO_RIFERIMENTO_QUIETANZA"));
					rv.setTipoRec(rs.getString("TIPO_RECORD"));
					rv.setTipoVers(rs.getString("TIPO_VERSAMENTO"));
					rv.setFlagReperib(rs.getString("FLAG_REPERIBILITA"));
					String impI = rs.getString("IMPORTO_IMPOSTA");
					if (impI!= null)
						impI = round(impI,2);
					rv.setImportoImposta(impI);
					String impInt = rs.getString("IMPORTO_INTERESSI");
					if (impInt!= null)
						impInt = round(impInt,2);
					rv.setImportoInteressi(impInt);
					String impS2 = rs.getString("IMPORTO_SANZIONI_2");
					if (impS2!= null)
						impS2 = round(impS2,2);
					rv.setImportoPenaPecuniaria(impS2);
					String impS1 = rs.getString("IMPORTO_SANZIONI_1");
					if (impS1!= null)
						impS1 = round(impS1,2);
					rv.setImportoSoprattassa(impS1);
					rv.setAnnoImpostaVio(rs.getString("ANNO_IMPOSTA_VIOLAZIONE"));
					rv.setNumProvvLiqui(rs.getString("NUMERO_LIQUIDAZIONE"));
					rv.setDataProvvLiqui(rs.getString("DATA_LIQUIDAZIONE"));
					rv.setDataVersamento(rs.getString("DATA_VERSAMENTO_CONTRIBUENTE"));
					rv.setDenominazionePF(rs.getString("DENOMINAZ_PF"));
					rv.setComuneDomicilioPF(rs.getString("COMU_DOMI_PF"));
					rv.setComuneDomicilioPG(rs.getString("COMU_DOMI_PG"));
					rv.setDenominazionePG(rs.getString("DENOMINAZ_PG"));

					String codForn = rs.getString("CODICE_FORNITURA");
					rv.setCodFornitura(codForn);
					rv.setAnnoRiferimento(codForn.substring(3,7));
					rv.setDataScadenza(codForn.substring(7,9)+"/"+codForn.substring(9,11)+"/"+codForn.substring(11,15));
					rv.setProgressivoInvio(codForn.substring(15,17));

					listaRisVio.add(rv);

				}
			}

			ht.put(EsatriContribuentiLogic.ESATRI_CONTRIBUENTI,con);
			ht.put(EsatriContribuentiLogic.LISTA_RIS_CON,listaRisCon);
			ht.put(EsatriContribuentiLogic.LISTA_RIS_VIO,listaRisVio);

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

	public Hashtable mCaricareListaContribuenti(Vector listaPar, ContribuenteFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//controllo provenienza chiamata
					String chiavi = finder.getKeyStr();
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + " order by CODICE_FISCALE_CONTRIBUENTE,CODICE_FORNITURA,TIPO ";
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Contribuente con = new Contribuente();
						con.setCF_pI(rs.getString("CODICE_FISCALE_CONTRIBUENTE"));
						con.setCodFornitura(rs.getString("CODICE_FORNITURA"));
						con.setFlagRivesamento(rs.getString("TIPO"));
						con.setCodEnte(rs.getString("COD_ENTE"));
						con.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
						con.setDataScadenza(rs.getString("DATA_SCADENZA"));
						con.setProgrInvio(rs.getString("PROGR_INVIO"));

						vct.add(con);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(EsatriContribuentiLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(EsatriContribuentiLogic.FINDER,finder);
			
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
		catch (Exception e)
		{
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
