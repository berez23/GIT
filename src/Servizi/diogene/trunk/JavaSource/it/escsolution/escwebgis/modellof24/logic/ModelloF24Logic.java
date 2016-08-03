/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.modellof24.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.modellof24.bean.ModelloF24;
import it.escsolution.escwebgis.modellof24.bean.ModelloF24Finder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ModelloF24Logic extends EscLogic
{
	private String appoggioDataSource;

	public ModelloF24Logic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_MODELLIF24 = "LISTA_MODELLIF24@ModelloF24Logic";
	public final static String LISTA_DETTAGLIO_MODELLOF24 = "LISTA_DETTAGLIO_MODELLOF24@ModelloF24Logic";
	public final static String LISTA_DETTAGLIO_G2 = "LISTA_DETTAGLIO_G2@ModelloF24Logic";
	public final static String MODELLOF24 = "MODELLOF24@ModelloF24Logic";
	private final static String SQL_SELECT_LISTA =
		"SELECT N, G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
		" G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
		" G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
		" G1_PROGRESSIVO_RIGA, G1_CD_FISCALE_CONTRIBUENTE," +
		" G1_COGNOME_DENOMINAZIONE, G1_NOME, CHIAVE" +
		" FROM(" +
		" SELECT ROWNUM N,nvl(to_char(G1_FORNITURA_DATA,'dd/mm/yyyy'),'-') AS G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
		" nvl(to_char(G1_RIPARTIZIONE_DATA,'dd/mm/yyyy'),'-') AS G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
		" nvl(to_char(G1_BONIFICO_DATA,'dd/mm/yyyy'),'-') AS G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
		" G1_PROGRESSIVO_RIGA, G1_CD_FISCALE_CONTRIBUENTE," +
		" TRIM(G1_COGNOME_DENOMINAZIONE) AS G1_COGNOME_DENOMINAZIONE, TRIM(G1_NOME) AS G1_NOME," +
		" MI_F24_G1.G1_FORNITURA_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_DELEGA" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_RIGA AS CHIAVE" +
		" FROM MI_F24_G1 WHERE 1=? ";
	private final static String SQL_SELECT_COUNT_LISTA =
		"SELECT COUNT(*) conteggio " +
		" FROM(" +
		" SELECT N, G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
		" G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
		" G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
		" G1_PROGRESSIVO_RIGA, G1_CD_FISCALE_CONTRIBUENTE," +
		" G1_COGNOME_DENOMINAZIONE, G1_NOME" +
		" FROM ( SELECT ROWNUM N, G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
		" G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
		" G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
		" G1_PROGRESSIVO_RIGA, G1_CD_FISCALE_CONTRIBUENTE," +
		" G1_COGNOME_DENOMINAZIONE, G1_NOME" +
		" FROM MI_F24_G1 WHERE 1=? ";

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaModelliF24(Vector listaPar, ModelloF24Finder finder)
		throws Exception
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
			java.sql.ResultSet rs;
			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					if(i == 0 )
						sql = sql + "AND (G1_FORNITURA_PROGRESSIVO" +
				" || '|' || TO_CHAR (G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || G1_PROGRESSIVO_DELEGA" +
				" || '|' || G1_PROGRESSIVO_RIGA) in (" + finder.getKeyStr() + ")";
					if (i == 1)
						sql = sql + "AND (TO_CHAR(G1_FORNITURA_PROGRESSIVO)" +
				" || '|' || TO_CHAR (G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || G1_PROGRESSIVO_DELEGA" +
				" || '|' || G1_PROGRESSIVO_RIGA) in (" + finder.getKeyStr() + ")";


				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1 && finder.getKeyStr().equals(""))
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				else if(i == 0 )
					sql = sql + "))";
				else
					sql = sql + ")";


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						ModelloF24 f24 = new ModelloF24();
						f24.setChiave(rs.getString("CHIAVE"));
						f24.setFornituraData(rs.getString("G1_FORNITURA_DATA"));
						f24.setFornituraProgressivo(rs.getString("G1_FORNITURA_PROGRESSIVO"));
						f24.setRipartizioneData(rs.getString("G1_RIPARTIZIONE_DATA"));
						f24.setRipartizioneProgressivo(rs.getString("G1_RIPARTIZIONE_PROGRESSIVO"));
						f24.setBonificoData(rs.getString("G1_BONIFICO_DATA"));
						f24.setProgressivoDelega(rs.getString("G1_PROGRESSIVO_DELEGA"));
						f24.setProgressivoRiga(rs.getString("G1_PROGRESSIVO_RIGA"));
						f24.setCdFiscaleContribuente(rs.getString("G1_CD_FISCALE_CONTRIBUENTE"));
						f24.setCognomeDenominazione(rs.getString("G1_COGNOME_DENOMINAZIONE"));
						f24.setNome(rs.getString("G1_NOME"));
						vct.add(f24);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}

			ht.put(LISTA_MODELLIF24, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
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

	public Hashtable mCaricareDettaglioModelloF24(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			this.initialize();
			// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
			String sqlDettaglio =
				"SELECT  MI_F24_G1.G1_FORNITURA_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_DELEGA" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_RIGA AS CHIAVE," +
				" MI_F24_G1.G1_CD_ENTE_RENDICONTO, MI_F24_G1.G1_TP_ENTE," +
				" MI_F24_G1.G1_CAB, MI_F24_G1.G1_CD_FISCALE_CONTRIBUENTE," +
				" TO_CHAR (MI_F24_G1.G1_DATA_RISCOSSIONE, 'dd/mm/yyyy') AS G1_DATA_RISCOSSIONE, MI_F24_G1.G1_CD_TRIBUTO," +
				" MI_F24_G1.G1_FLAG_ERRORE_CD_TRIBUTO," +
				" MI_F24_G1.G1_RATEIZZAZIONE, MI_F24_G1.G1_ANNO_RIFERIMENTO," +
				" MI_F24_G1.G1_FLAG_ERRORE_ANNO, MI_F24_G1.G1_IMPORTO_DEBITO," +
				" MI_F24_G1.G1_IMPORTO_CREDITO, MI_F24_G1.G1_ICI_RAVVEDIMENTO," +
				" MI_F24_G1.G1_ICI_IMMOBILI_VARIATI, MI_F24_G1.G1_ICI_ACCONTO," +
				" MI_F24_G1.G1_ICI_SALDO, MI_F24_G1.G1_ICI_N_FABBRICATI," +
				" MI_F24_G1.G1_FLAG_ERRORE_DATI_ICI," +
				" MI_F24_G1.G1_IMPORTO_DETRAZIONE_AB_PR," +
				" MI_F24_G1.G1_IMPOSTA_TIPO, nvl(to_char(G1_FORNITURA_DATA,'dd/mm/yyyy'),'-') AS G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
				" nvl(to_char(G1_RIPARTIZIONE_DATA,'dd/mm/yyyy'),'-') AS G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
				" nvl(to_char(G1_BONIFICO_DATA,'dd/mm/yyyy'),'-') AS G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
				" G1_PROGRESSIVO_RIGA, G1_FLAG_ERRORE_CD_FISCALE,G1_CD_ENTE_COMUNALE" +
				" FROM MI_F24_G1 " +
				" WHERE MI_F24_G1.G1_FORNITURA_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_DELEGA" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_RIGA = ?";

			int indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sqlDettaglio);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ModelloF24 f24 = new ModelloF24();
			if (rs.next())
			{
				f24.setChiave(rs.getString("CHIAVE"));
				f24.setFornituraData(rs.getString("G1_FORNITURA_DATA"));
				f24.setFornituraProgressivo(rs.getString("G1_FORNITURA_PROGRESSIVO"));
				f24.setRipartizioneData(rs.getString("G1_RIPARTIZIONE_DATA"));
				f24.setRipartizioneProgressivo(rs.getString("G1_RIPARTIZIONE_PROGRESSIVO"));
				f24.setBonificoData(rs.getString("G1_BONIFICO_DATA"));
				f24.setProgressivoDelega(rs.getString("G1_PROGRESSIVO_DELEGA"));
				f24.setProgressivoRiga(rs.getString("G1_PROGRESSIVO_RIGA"));
				f24.setCdEnteRendiconto(rs.getString("G1_CD_ENTE_RENDICONTO"));
				f24.setTpEnte(rs.getString("G1_TP_ENTE"));
				f24.setCab(rs.getString("G1_CAB"));
				f24.setCdFiscaleContribuente(rs.getString("G1_CD_FISCALE_CONTRIBUENTE"));
				f24.setFlagErroreCdFiscale(rs.getString("G1_FLAG_ERRORE_CD_FISCALE"));
				f24.setDataRiscossione(rs.getString("G1_DATA_RISCOSSIONE"));
				f24.setCdTributo(rs.getString("G1_CD_TRIBUTO"));
				f24.setFlagErroreCdTributo(rs.getString("G1_FLAG_ERRORE_CD_TRIBUTO"));
				f24.setRateizzazione(rs.getString("G1_RATEIZZAZIONE"));
				f24.setAnnoRiferimento(rs.getString("G1_ANNO_RIFERIMENTO"));
				f24.setFlagErroreAnno(rs.getString("G1_FLAG_ERRORE_ANNO"));
				f24.setImportoDebito(rs.getString("G1_IMPORTO_DEBITO"));
				f24.setImportoCredito(rs.getString("G1_IMPORTO_CREDITO"));
				f24.setIciRavvedimento(rs.getString("G1_ICI_RAVVEDIMENTO"));
				f24.setIciImmobiliVariati(rs.getString("G1_ICI_IMMOBILI_VARIATI"));
				f24.setIciAcconto(rs.getString("G1_ICI_ACCONTO"));
				f24.setIciSaldo(rs.getString("G1_ICI_SALDO"));
				f24.setIciNFabbricati(rs.getString("G1_ICI_N_FABBRICATI"));
				f24.setFlagErroreDatiIci(rs.getString("G1_FLAG_ERRORE_DATI_ICI"));
				f24.setImportoDetrazioneAbPr(rs.getString("G1_IMPORTO_DETRAZIONE_AB_PR"));
				f24.setImpostaTipo(rs.getString("G1_IMPOSTA_TIPO"));
				f24.setCdEnteComunale(rs.getString("G1_CD_ENTE_COMUNALE"));
				//f24.setAccreditoCdEnteComunale(rs.getString("G2_CD_ENTE_COMUNALE"));
				//f24.setAccreditoImporto(rs.getString("G2_IMPORTO_ACCREDITO"));
				//f24.setAccreditoTipoImposta(rs.getString("G2_IMPOSTA_TIPO"));
			}


			//verifico G2
			String sql =
				"SELECT CHIAVE, G1_CD_ENTE_RENDICONTO, G1_TP_ENTE, G1_CAB," +
				" G1_CD_FISCALE_CONTRIBUENTE, G1_DATA_RISCOSSIONE, G1_CD_TRIBUTO," +
				" G1_FLAG_ERRORE_CD_TRIBUTO, G1_RATEIZZAZIONE, G1_ANNO_RIFERIMENTO," +
				" G1_FLAG_ERRORE_ANNO, G1_IMPORTO_DEBITO, G1_IMPORTO_CREDITO," +
				" G1_ICI_RAVVEDIMENTO, G1_ICI_IMMOBILI_VARIATI, G1_ICI_ACCONTO," +
				" G1_ICI_SALDO, G1_ICI_N_FABBRICATI, G1_FLAG_ERRORE_DATI_ICI," +
				" G1_IMPORTO_DETRAZIONE_AB_PR, G1_IMPOSTA_TIPO, G2_IMPORTO_ACCREDITO," +
				" G2_IMPOSTA_TIPO, G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
				" G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
				" G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
				" G1_PROGRESSIVO_RIGA, G1_FLAG_ERRORE_CD_FISCALE, G2_CD_ENTE_COMUNALE,G1_CD_ENTE_COMUNALE"+
				" FROM (SELECT  MI_F24_G1.G1_FORNITURA_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_FORNITURA_DATA,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_RIPARTIZIONE_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_RIPARTIZIONE_PROGRESSIVO" +
				" || '|' || TO_CHAR (MI_F24_G1.G1_BONIFICO_DATA, 'dd/mm/yyyy')" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_DELEGA" +
				" || '|' || MI_F24_G1.G1_PROGRESSIVO_RIGA AS CHIAVE," +
				" MI_F24_G1.G1_CD_ENTE_RENDICONTO, MI_F24_G1.G1_TP_ENTE," +
				" MI_F24_G1.G1_CAB, MI_F24_G1.G1_CD_FISCALE_CONTRIBUENTE," +
				" TO_CHAR (MI_F24_G1.G1_DATA_RISCOSSIONE, 'dd/mm/yyyy') AS G1_DATA_RISCOSSIONE, MI_F24_G1.G1_CD_TRIBUTO," +
				" MI_F24_G1.G1_FLAG_ERRORE_CD_TRIBUTO," +
				" MI_F24_G1.G1_RATEIZZAZIONE, MI_F24_G1.G1_ANNO_RIFERIMENTO," +
				" MI_F24_G1.G1_FLAG_ERRORE_ANNO, MI_F24_G1.G1_IMPORTO_DEBITO," +
				" MI_F24_G1.G1_IMPORTO_CREDITO, MI_F24_G1.G1_ICI_RAVVEDIMENTO," +
				" MI_F24_G1.G1_ICI_IMMOBILI_VARIATI, MI_F24_G1.G1_ICI_ACCONTO," +
				" MI_F24_G1.G1_ICI_SALDO, MI_F24_G1.G1_ICI_N_FABBRICATI," +
				" MI_F24_G1.G1_FLAG_ERRORE_DATI_ICI," +
				" MI_F24_G1.G1_IMPORTO_DETRAZIONE_AB_PR," +
				" MI_F24_G1.G1_IMPOSTA_TIPO, MI_F24_G2.G2_IMPORTO_ACCREDITO," +
				" MI_F24_G2.G2_IMPOSTA_TIPO,nvl(to_char(G1_FORNITURA_DATA,'dd/mm/yyyy'),'-') AS G1_FORNITURA_DATA, G1_FORNITURA_PROGRESSIVO," +
				" nvl(to_char(G1_RIPARTIZIONE_DATA,'dd/mm/yyyy'),'-') AS G1_RIPARTIZIONE_DATA, G1_RIPARTIZIONE_PROGRESSIVO," +
				" nvl(to_char(G1_BONIFICO_DATA,'dd/mm/yyyy'),'-') AS G1_BONIFICO_DATA, G1_PROGRESSIVO_DELEGA," +
				" G1_PROGRESSIVO_RIGA, G1_FLAG_ERRORE_CD_FISCALE,G2_CD_ENTE_COMUNALE,G1_CD_ENTE_COMUNALE" +
				" FROM MI_F24_G1, MI_F24_G2" +
				" WHERE MI_F24_G1.G1_FORNITURA_DATA = MI_F24_G2.G2_FORNITURA_DATA" +
				" AND MI_F24_G1.G1_FORNITURA_PROGRESSIVO = MI_F24_G2.G2_FORNITURA_PROGRESSIVO" +
				" AND MI_F24_G1.G1_RIPARTIZIONE_DATA = MI_F24_G2.G2_RIPARTIZIONE_DATA" +
				" AND MI_F24_G1.G1_RIPARTIZIONE_PROGRESSIVO = MI_F24_G2.G2_RIPARTIZIONE_PROGRESSIVO" +
				" AND MI_F24_G1.G1_BONIFICO_DATA = MI_F24_G2.G2_BONIFICO_DATA)" +
				" WHERE CHIAVE = ?";

			this.initialize();
			this.setString(indice, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs1 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs1.next())
			{
				f24.setAccreditoCdEnteComunale(rs1.getString("G2_CD_ENTE_COMUNALE"));
				f24.setAccreditoImporto(rs1.getString("G2_IMPORTO_ACCREDITO"));
				f24.setAccreditoTipoImposta(rs1.getString("G2_IMPOSTA_TIPO"));
				ArrayList listag2 = new ArrayList();
				BigDecimal totaleg2 = new BigDecimal(0);

				String appog2 = rs1.getString("g2_importo_accredito");
				Pattern p2 = Pattern.compile("\\d+([,.]\\d+)?");
				Matcher m2 = p2.matcher(appog2);
				if(m2.find())
				{
					appog2= m2.group(0).replaceFirst(",",".");
					totaleg2 = totaleg2.add(new BigDecimal(appog2));
				}
				listag2.add(f24);
				String totales2 = totaleg2.toString();
				totales2 = totales2.replace('.',',');
				ModelloF24 f24L = new ModelloF24();
				f24L.setAccreditoTipoImposta(" ");
				f24L.setFornituraData(" ");
				f24L.setFornituraProgressivo(" ");
				f24L.setRipartizioneData(" ");
				f24L.setRipartizioneProgressivo(" ");
				f24L.setBonificoData(" ");
				f24L.setAccreditoCdEnteComunale("TOTALE:");
				f24L.setAccreditoImporto(totales2);
				listag2.add(f24L);
				ht.put(LISTA_DETTAGLIO_G2, listag2);

			}


			sql =
				"SELECT chiave,g5_importo_accredito, g5_cd_riferimento_operazione," +
				" g5_accredito_data, g5_ripartizione_data_originari,g5_ripartizione_progressivo_o," +
				" g5_bonifico_data_originari, g5_imposta_tipo, G5_CD_ENTE_COMUNALE" +
				" FROM (SELECT  mi_f24_g1.g1_fornitura_progressivo" +
				" || '|' || TO_CHAR (mi_f24_g1.g1_fornitura_data,'dd/mm/yyyy')" +
				" || '|' || TO_CHAR (mi_f24_g1.g1_ripartizione_data, 'dd/mm/yyyy')" +
				" || '|' || mi_f24_g1.g1_ripartizione_progressivo" +
				" || '|' || TO_CHAR (mi_f24_g1.g1_bonifico_data, 'dd/mm/yyyy')" +
				" || '|' || mi_f24_g1.g1_progressivo_delega" +
				" || '|' || mi_f24_g1.g1_progressivo_riga AS chiave," +
				" mi_f24_g5.g5_importo_accredito,mi_f24_g5.g5_cd_riferimento_operazione," +
				" nvl(to_char(mi_f24_g5.g5_accredito_data,'dd/mm/yyyy'),'-') AS g5_accredito_data, nvl(to_char(mi_f24_g5.g5_ripartizione_data_originari,'dd/mm/yyyy'),'-') AS g5_ripartizione_data_originari," +
				" mi_f24_g5.g5_ripartizione_progressivo_o, nvl(to_char(mi_f24_g5.g5_bonifico_data_originari,'dd/mm/yyyy'),'-') AS g5_bonifico_data_originari," +
				" mi_f24_g5.g5_imposta_tipo,G5_CD_ENTE_COMUNALE" +
				" FROM mi_f24_g1, mi_f24_g5 " +
				" WHERE mi_f24_g1.g1_fornitura_data = mi_f24_g5.g5_fornitura_data" +
				" AND mi_f24_g1.g1_fornitura_progressivo = mi_f24_g5.g5_fornitura_progressivo)" +
				" WHERE CHIAVE =?";
			this.initialize();
			//this.setString(1, f24.getChiave());
			this.setString(1,chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList listamodelli = new ArrayList();
			if (rs.next())
			{
				BigDecimal totale = new BigDecimal(0);
				do
				{
					ModelloF24 modello = new ModelloF24();
					modello.setChiave(rs.getString("CHIAVE"));
					modello.setIdentificazioneTipoImposta(f24.getIdentificazioneTipoImposta());
					modello.setFornituraData(f24.getFornituraData());
					modello.setFornituraProgressivo(f24.getFornituraProgressivo());
					modello.setCdEnteComunale(f24.getCdEnteComunale());
					modello.setIdentificazioneImportoAccredito(rs.getString("g5_importo_accredito"));
					modello.setIdentificazioneCro(rs.getString("g5_cd_riferimento_operazione"));
					modello.setIdentificazioneDataAccreditamento(rs.getString("g5_accredito_data"));
					modello.setIdentificazioneDataRipOrig(rs.getString("g5_ripartizione_data_originari"));
					modello.setIdentificazioneProgRipOrig(rs.getString("g5_ripartizione_progressivo_o"));
					modello.setIdentificazioneDataBonificoOriginario(rs.getString("g5_bonifico_data_originari"));
					modello.setIdentificazioneTipoImposta(rs.getString("g5_imposta_tipo"));
					modello.setIdentificazioneCdEnteComunale(rs.getString("G5_CD_ENTE_COMUNALE"));
					String appo = rs.getString("g5_importo_accredito");
					Pattern p = Pattern.compile("\\d+([,.]\\d+)?");
					Matcher m = p.matcher(appo);
					if(m.find())
					{
						appo = m.group(0).replaceFirst(",",".");
						totale = totale.add(new BigDecimal(appo));
					}
					listamodelli.add(modello);
				} while (rs.next());
				///Calcolo totali
				String totales = totale.toString();
				totales = totales.replace('.',',');
				ModelloF24 f24L = new ModelloF24();
				f24L.setIdentificazioneTipoImposta(" ");
				f24L.setFornituraData(" ");
				f24L.setFornituraProgressivo(" ");
				f24L.setCdEnteComunale(" ");
				f24L.setChiave(" ");
				f24L.setIdentificazioneImportoAccredito(totales);
				f24L.setIdentificazioneCro(" ");
				f24L.setIdentificazioneDataAccreditamento(" ");
				f24L.setIdentificazioneDataRipOrig(" ");
				f24L.setIdentificazioneProgRipOrig(" ");
				f24L.setIdentificazioneCdEnteComunale(" ");
				f24L.setIdentificazioneDataBonificoOriginario("TOTALE:");
				f24L.setIdentificazioneTipoImposta(" ");
				listamodelli.add(f24L);

			}
			ht.put(LISTA_DETTAGLIO_MODELLOF24, listamodelli);
			ht.put(MODELLOF24, f24);
			
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
		catch (Exception e)
		{
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

}
