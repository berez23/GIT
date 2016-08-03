package it.webred.mui.inputdb;

import it.webred.mui.MuiException;
import it.webred.mui.consolidation.DapManager;
import it.webred.mui.http.MuiApplication;
import it.webred.successioni.model.ResultSetSuccessioni;
import it.webred.successioni.model.SuccessioniA;
import it.webred.successioni.model.SuccessioniB;
import it.webred.successioni.model.SuccessioniC;
import it.webred.successioni.model.SuccessioniD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.ServletException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Date;

import net.skillbill.logging.Logger;

public class MuiFornituraDB2TXTTransformer {

	static final String INITPARM_CONFIGFILE = "properties.filename";
	static final String A_CAPO = "\n";
	static final String SEPARATORE = "|";
	static final String SEPARATORE_EXPORT = ",";
	static final String NON_VALORIZZATO = "";
	static final String ID_SOGGETTO_CATASTALE_DEFAULT = "0";
	static final String SEPARATORE_NON_VALORIZZATO = "|";
	static final String SEPARATORE_NON_VALORIZZATO_EXPORT = ",";

	/**
	 * @todo Cosa si deve mettere nei campi "Valore impostato da ricerca a catasto"?
	 */
	static final String DA_RICERCA_A_CATASTO = "";
	static final String DA_RICERCA_A_CATASTO_NUM = "0";
	static final String DA_SPECIFICHE_MUI_DAP = "";
	static final DateFormat dateformatOutput = new SimpleDateFormat("ddMMyyyy");
	static final DateFormat dateformatDB = new SimpleDateFormat("yyyy-MM-dd");
	static final DateFormat dateformatExport = new SimpleDateFormat("yyyyMMdd");
	static int ggMaxEstrazione = 30;

	// Valori fissi del file di testo da generare
	static final String VF_COD_AMM = MuiApplication.belfiore;

	static final String VF_DATA_FITTIZIA = "10041973";

	private static final String SQL_GET_FORNITURA_ESISTENTE = "SELECT * FROM suc_dup_fornitura a WHERE data_iniziale = ? AND data_finale = ?";

	private static final String SQL_GET_DATI_SUCC = "SELECT distinct a.pk_id_succa, a.ufficio, a.anno, a.volume, a.numero, a.sottonumero, a.comune, a.progressivo, a.tipo_dichiarazione, a.data_apertura, a.cf_defunto, a.cognome_defunto, a.nome_defunto, a.sesso a_sesso,"
			+ " a.citta_nascita a_citta_nascita, a.prov_nascita a_prov_nascita, a.data_nascita a_data_nascita, a.citta_residenza a_citta_residenza, a.prov_residenza a_prov_residenza, a.indirizzo_residenza a_indirizzo_residenza, a.fornitura,"
			+ // Dati tipo record 2
			" b.pk_id_succb, b.comune, b.progressivo b_progressivo, b.progressivo_erede b_progressivo_erede, b.cf_erede, b.denominazione, b.sesso b_sesso, b.citta_nascita b_citta_nascita," + " b.prov_nascita b_prov_nascita, b.data_nascita b_data_nascita, b.citta_residenza b_citta_residenza, b.prov_residenza b_prov_residenza, b.indirizzo_residenza b_indirizzo_residenza, "
			+ " c.comune c_comune, c.progressivo c_progressivo, c.progressivo_immobile c_progressivo_immobile, TO_NUMBER(c.numeratore_quota_def) numeratore_quota_def, TO_NUMBER(c.denominatore_quota_def) denominatore_quota_def, c.diritto diritto, c.catasto, c.tipo_dati, c.foglio, c.particella1,"
			+ " c.particella2, c.subalterno1, c.subalterno2, c.denuncia1, c.anno_denuncia, c.natura, c.superficie_ettari, TO_NUMBER(c.superficie_mq) superficie_mq, TO_NUMBER(c.vani) vani, c.indirizzo_immobile," 
			+ " d.progressivo d_progressivo, d.progressivo_immobile d_progressivo_immobile, d.progressivo_erede d_progressivo_erede, TO_NUMBER(d.numeratore_quota) d_numeratore_quota, TO_NUMBER(d.denominatore_quota) d_denominatore_quota, d.agevolazione_1_casa d_agevolazione_1_casa, d.tipo_record d_tipo_record"
			+ " FROM mi_successioni_a a, mi_successioni_b b, mi_successioni_c c, mi_successioni_d d" 
			+ " WHERE a.ufficio = b.ufficio" 
			+ " AND a.anno = b.anno" 
			+ " AND a.volume = b.volume" 
			+ " AND a.numero = b.numero"
			+ " AND a.sottonumero = b.sottonumero" 
			+ " AND a.fornitura = b.fornitura" 
			+ " AND a.ufficio = c.ufficio" 
			+ " AND a.anno = c.anno" 
			+ " AND a.volume = c.volume" 
			+ " AND a.numero = c.numero" 
			+ " AND a.sottonumero = c.sottonumero" 
			+ " AND a.fornitura = c.fornitura" 
			+ " AND a.data_apertura >= ?" 
			+ " AND a.data_apertura <= ?"
			// MARCORIC 16-10-2008 agginta and sottostante ci meteva anche eredi che erroneamente erano in nota 
			// e non avevano eredità 
			+ " AND B.PROGRESSIVO_EREDE = D.PROGRESSIVO_EREDE"
			// MARCORIC: 19-3-2009 : MAIL ZAZANI 17-3 
			// DUPLICAZIONI SU SUCCESSIONI DI TIPO TESTAMENTARIE 
			// MANCAA UNA CONDIZIONE E PER QUESTO ASSEGNAVA A TUTTI GLI EREDI UNA QUOTA DI OGNI IMMOBILE
			// ANCHE QANDO AD OGNI EREDE VENIVA DATO UN SOLO IMMOBILE
			+ " AND c.PROGRESSIVO_IMMOBILE = d.PROGRESSIVO_IMMOBILE"
			+ " AND d.ufficio = a.ufficio" 
			+ " AND d.anno = a.anno " 
			+ " AND d.volume = a.volume"
			+ " AND d.numero = a.numero" 
			+ " AND d.sottonumero = a.sottonumero" 
			+ " AND d.comune = a.comune"
			+ " AND d.fornitura = a.fornitura"
			// " AND a.pk_id_succa = 35743" + // CONDIZIONE SOLO PER TEST
			//+ " AND a.cf_defunto = 'BZZBTS12M25F205J'"
			// FILIPPO MAZZINI 23-04-2013: PER PROBLEMA DATI DUPLICATI NELLE TABELLE MI_SUCCESSIONI_*
			/*+ " and a.pk_id_succa = (select max(pk_id_succa)"
			+ " FROM mi_successioni_a aa"
			+ " WHERE a.ufficio = aa.ufficio"
			+ " AND a.anno = aa.anno"
			+ " AND a.volume = aa.volume"
			+ " AND a.numero = aa.numero"
			+ " AND a.sottonumero = aa.sottonumero"
			+ " AND a.comune = aa.comune"
			+ " AND a.progressivo = aa.progressivo)"*/
			// FINE FILIPPO MAZZINI 23-04-2013
			// MODIFICATO FILIPPO MAZZINI 22-05-2013
			+ " AND a.pk_id_succa = (select max(pk_id_succa)"
			+ " FROM mi_successioni_a aa, mi_successioni_b bb, mi_successioni_c cc, mi_successioni_d dd"
			+ " WHERE aa.ufficio = bb.ufficio"
			+ " AND aa.anno = bb.anno"
			+ " AND aa.volume = bb.volume"
			+ " AND aa.numero = bb.numero"
			+ " AND aa.sottonumero = bb.sottonumero"
			+ " AND aa.fornitura = bb.fornitura"
			+ " AND aa.ufficio = cc.ufficio"
			+ " AND aa.anno = cc.anno"
			+ " AND aa.volume = cc.volume"
			+ " AND aa.numero = cc.numero"
			+ " AND aa.sottonumero = cc.sottonumero"
			+ " AND aa.fornitura = cc.fornitura"
			+ " AND bb.PROGRESSIVO_EREDE = dd.PROGRESSIVO_EREDE"
			+ " AND cc.PROGRESSIVO_IMMOBILE = dd.PROGRESSIVO_IMMOBILE"
			+ " AND dd.ufficio = aa.ufficio"
			+ " AND dd.anno = aa.anno"
			+ " AND dd.volume = aa.volume"
			+ " AND dd.numero = aa.numero"
			+ " AND dd.sottonumero = aa.sottonumero"
			+ " AND dd.comune = aa.comune"
			+ " AND dd.fornitura = aa.fornitura"
			+ " AND a.cf_defunto = aa.cf_defunto"
			+ " AND a.tipo_dichiarazione = aa.tipo_dichiarazione"
			+ " AND a.data_apertura = aa.data_apertura"
			+ " AND b.cf_erede = bb.cf_erede"
			+ " AND NVL(c.foglio, '-') = NVL(cc.foglio, '-')"
			+ " AND NVL(c.particella1, '-') = NVL(cc.particella1, '-')"
			+ " AND NVL(c.particella2, '-') = NVL(cc.particella2, '-')"
			+ " AND NVL(c.subalterno1, 0) = NVL(cc.subalterno1, 0)"
			+ " AND NVL(c.subalterno2, '-') = NVL(cc.subalterno2, '-')"
			+ " AND TO_NUMBER(c.numeratore_quota_def) = TO_NUMBER(cc.numeratore_quota_def)"
			+ " AND TO_NUMBER(c.denominatore_quota_def) = TO_NUMBER(cc.denominatore_quota_def)"
			+ " AND TO_NUMBER(d.numeratore_quota) = TO_NUMBER(dd.numeratore_quota)"
			+ " AND TO_NUMBER(d.denominatore_quota) = TO_NUMBER(dd.denominatore_quota)"
			+ " AND c.diritto = cc.diritto)"
			//FINE MODIFICATO FILIPPO MAZZINI 22-05-2013
			+  " ORDER BY a.pk_id_succa, b.pk_id_succb, a.ufficio, a.anno, a.volume, a.numero, a.sottonumero, a.comune, c.progressivo, a.fornitura, d.progressivo, d.progressivo_immobile, d.progressivo_erede";

	/**
	 * Constructor of the object.
	 */
	public MuiFornituraDB2TXTTransformer() {
		super();
	}

	public StringBuffer readDataSuccFromDB(String dataInizio, String dataFine, Writer output, Connection conn) throws ServletException, IOException, it.webred.successioni.exceptions.MuiException {

		StringBuffer sbOut = new StringBuffer();

		Hashtable nrRecordEsportati = new Hashtable();
		nrRecordEsportati.put("TOT", "0");
		nrRecordEsportati.put("TR6", "0");
		nrRecordEsportati.put("TR8", "0");

		try {

			String dataInizioDDMMYYYY = VF_DATA_FITTIZIA;
			if (dataInizio != null && dataInizio.trim().length() > 0) {				
				dataInizioDDMMYYYY = parseStringDateToDDMMYYYY(dataInizio);
			}
			
			String dataFineDDMMYYYY = VF_DATA_FITTIZIA;
			if (dataFine != null && dataFine.trim().length() > 0) {				
				dataFineDDMMYYYY = parseStringDateToDDMMYYYY(dataFine);
			}
			
			
			/* INIZIO Recupero eventuali caricamenti già fatti per il periodo selezionato */
						
			PreparedStatement _getStmtFornitura = conn.prepareStatement(SQL_GET_FORNITURA_ESISTENTE);
			// _getStmt = _conn.prepareStatement(SQL_TEST);
			_getStmtFornitura.clearParameters();
			_getStmtFornitura.setString(1, dataInizioDDMMYYYY);
			_getStmtFornitura.setString(2, dataFineDDMMYYYY);

			Logger.log().info(this.getClass().getName(), "Query eseguita RECUPERO FORNITURA ESISTENTE: " + SQL_GET_FORNITURA_ESISTENTE);
			Logger.log().info(this.getClass().getName(), "Param[1]: " + dataInizioDDMMYYYY);
			Logger.log().info(this.getClass().getName(), "Param[2]: " + dataFineDDMMYYYY);

			ResultSet rsFornitura = _getStmtFornitura.executeQuery();

			String iidFornituraEsistente = "";
			if (rsFornitura.next()) {
				iidFornituraEsistente = rsFornitura.getString("iid");
			}
			
			Logger.log().info(this.getClass().getName(), "Iid fornitura esistente: [" + iidFornituraEsistente + "]");
			
			/* FINE Recupero eventuali caricamenti già fatti per il periodo selezionato */

			Logger.log().info(this.getClass().getName(), "NrRecordTotali: " + nrRecordEsportati);

			// Logger.log().info(this.getClass().getName(), "SbOut: " + sbOut.toString());

			// checkInputDate(dataInizio, dataFine);

			// TIPO RECORD 1 - La prima riga dovrebbe essere OK
			appendConSepa(sbOut, "1");
			appendConSepa(sbOut, VF_COD_AMM);
			appendConSepa(sbOut, dataInizioDDMMYYYY);
			appendConSepa(sbOut, dataFineDDMMYYYY);
			appendConSepa(sbOut, "");
			appendConSepa(sbOut, parseDateToDDMMYYYY(new java.util.Date()));
			appendConAcapo(sbOut, "", nrRecordEsportati);

			try {

				PreparedStatement _getStmt = null;
				_getStmt = conn.prepareStatement(SQL_GET_DATI_SUCC);
				// _getStmt = _conn.prepareStatement(SQL_TEST);
				_getStmt.clearParameters();
				_getStmt.setString(1, dataInizio);
				_getStmt.setString(2, dataFine);
				Logger.log().info(this.getClass().getName(), "Query eseguita: " + SQL_GET_DATI_SUCC);
				Logger.log().info(this.getClass().getName(), "Param[1]: " + dataInizio);
				Logger.log().info(this.getClass().getName(), "Param[2]: " + dataFine);

				Statement st = conn.createStatement();
				st.execute("ALTER SESSION SET NLS_TERRITORY = 'ITALY'");
				
				ResultSet rs = _getStmt.executeQuery();

				int idNota = 0;
				int nrIdNota = 0;
				String idFornituraOld = "";
				SuccessioniA succA = null;
				SuccessioniB succB = null;
				SuccessioniC succC = null;
				SuccessioniD succD = null;
				ResultSetSuccessioni rss;
				while (rs.next()) {
					rss = make(rs);

					String pk_succaCurr = rss.getPk_id_succa();
					String pk_succbCurr = rss.getPk_id_succb();
					String pk_succcCurr = rss.getId_succc();
					String pk_succdCurr = rss.getId_succd();
					if ("BZZBTS12M25F205J".equals(rss.getA_cf_defunto()))
						pk_succbCurr=pk_succbCurr;
					
					boolean aToCreate = true;
					if (succA != null) {
						boolean bToCreate = true;
						boolean cToCreate = true;
						boolean dToCreate = true;
						String pk_succaOld = succA.getPk_id_succa();
						if (pk_succaOld.equalsIgnoreCase(pk_succaCurr)) {
							aToCreate = false;
							// NON RICREO A - VERIFICO SE DEVO CREARE B - VERIFICO SE
							// DEVO CREARE C - VERIFICO SE DEVO CREARE D
							Collection listaB = succA.getListSuccessioniB();
							for (Iterator iter = listaB.iterator(); iter.hasNext();) {
								SuccessioniB succBCurr = (SuccessioniB) iter.next();
								if (succBCurr.getPk_id_succb().equalsIgnoreCase(pk_succbCurr)) {
									bToCreate = false;
									break;
								}
							}
							Collection listaC = succA.getListSuccessioniC();
							for (Iterator iter = listaC.iterator(); iter.hasNext();) {
								SuccessioniC succCCurr = (SuccessioniC) iter.next();
								if (succCCurr.getId_succc().equalsIgnoreCase(pk_succcCurr)) {
									cToCreate = false;
									break;
								}
							}
							Collection listaD = succA.getListSuccessioniD();
							for (Iterator iter = listaD.iterator(); iter.hasNext();) {
								SuccessioniD succDCurr = (SuccessioniD) iter.next();
								if (succDCurr.getId_succd().equalsIgnoreCase(pk_succdCurr)) {
									dToCreate = false;
									break;
								}
							}
						} else {
							// Scrivo le informazioni legate al corrente oggetto SuccA
							// e poi ne creo uno nuovo
							// String idFornituraCurr = succA.getA_fornitura() + "-" +
							// succA.getA_progressivo();
							// In attesa di deinire meglio il concetto di fornitura
							// facciamo
							// in modo che la fornitura sia sempre una sola per
							// estrazione.
							String idFornituraCurr = "";
							if (idFornituraCurr.equalsIgnoreCase(idFornituraOld)) {
								++idNota;
							} else {
								nrIdNota = idNota;
								idNota = 1;
							}

							writeA(sbOut, succA, idNota, nrRecordEsportati);

							idFornituraOld = idFornituraCurr;

							aToCreate = true;
						}
						if (!aToCreate && bToCreate) {
							succB = makeB(rss);
							succA.addSuccessioniB(succB);
						}
						if (!aToCreate && cToCreate) {
							succC = makeC(rss);
							succA.addSuccessioniC(succC);
						}
						if (!aToCreate && dToCreate) {
							succD = makeD(rss);
							succA.addSuccessioniD(succD);
						}
					}
					if (aToCreate) {
						succA = makeA(rss);
						succB = makeB(rss);
						succC = makeC(rss);
						succD = makeD(rss);
						succA.addSuccessioniB(succB);
						succA.addSuccessioniC(succC);
						succA.addSuccessioniD(succD);
					}

				}

				if (succA != null) {
					writeA(sbOut, succA, ++idNota, nrRecordEsportati);
				}

				// TIPO RECORD 9 - Informazioni relative alla fornitura
				appendConSepa(sbOut, "9");
				appendConSepa(sbOut, "" + (Integer.parseInt((String) nrRecordEsportati.get("TOT")) - 1)); // Occorre fare -1 per non contare anche il TIPO RECORD 1
				appendConSepa(sbOut, "" + (nrIdNota + idNota));
				appendConSepa(sbOut, NON_VALORIZZATO, 3);
				appendConSepa(sbOut, "" + nrRecordEsportati.get("TR6"));
				appendConSepa(sbOut, "" + nrRecordEsportati.get("TR8"));
				appendConAcapo(sbOut, "", nrRecordEsportati);

			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(), e);
				throw new it.webred.successioni.exceptions.MuiException("Errore dalla procedura di lettura dei dati");
			}

			/**
			 * Se output è diverso da null allora riverso il contenuto di sbOut nel file associato al FileWriter output e ritorno NULL
			 */
			if (output != null) {
				if (sbOut.length() > 0) {
					output.write(sbOut.charAt(0));
					for (int pos = 1; pos < sbOut.length(); pos++) {
						if (!(sbOut.charAt(pos) == '.' && sbOut.charAt(pos - 1) == '\n')) {
							output.write(sbOut.charAt(pos));
						}
					}
				}

				output.flush();
				output.close();

				return null;

			} else {
				return sbOut;
			}

			// out.flush();
			// out.close();
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), e.getMessage());
			e.printStackTrace();
			throw new it.webred.successioni.exceptions.MuiException("Errore dalla procedura di lettura dei dati");
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *            if an error occure
	 */

	private void appendConSepa(StringBuffer sbOut, String value) {
		if (NON_VALORIZZATO.equalsIgnoreCase(value)) {
			sbOut.append(value);
			sbOut.append(SEPARATORE_NON_VALORIZZATO);
		} else {
			sbOut.append(value);
			sbOut.append(SEPARATORE);
		}
	}

	private void appendConSepa(StringBuffer sbOut, String value, int nrVolte) {
		for (int i = 0; i < nrVolte; i++) {
			appendConSepa(sbOut, value);
		}
	}

	private void appendConSepaExport(StringBuffer sbOut, String value) {
		if (NON_VALORIZZATO.equalsIgnoreCase(value)) {
			sbOut.append(value);
			sbOut.append(SEPARATORE_NON_VALORIZZATO_EXPORT);
		} else {
			// Per evitare confusione con il carattere utilizzato come separatore
			// vado a sostituire
			// la sua eventuale presenza all'interno della stringa
			if (value != null) {
				sbOut.append(value.replaceAll(SEPARATORE_EXPORT, " "));
			} else {
				sbOut.append("");
			}
			sbOut.append(SEPARATORE_EXPORT);
		}
	}

	/*
	 * private void appendConSepaExport(StringBuffer sbOut, String value, int nrVolte) { for (int i = 0; i < nrVolte; i++) { appendConSepaExport(sbOut, value); } }
	 */

	private void appendConAcapo(StringBuffer sbOut, String value, Hashtable nrRecordEsportati) {
		sbOut.append(value);
		sbOut.append(A_CAPO);

		if (nrRecordEsportati != null) {
			nrRecordEsportati.put("TOT", "" + (Integer.parseInt((String) nrRecordEsportati.get("TOT")) + 1));
		}
	}

	/**
	 * Dato un oggetto SuccessioniA scrive nel buffer tutte le informazioni contenute in tale oggetto, comprese quelle dei record SuccessioniB e SuccessioniC collegati
	 * 
	 * @param succA
	 * @param idNota
	 * @throws Exception
	 */
	private void writeA(StringBuffer sbOut, SuccessioniA succA, int idNota, Hashtable nrRecordEsportati) throws MuiException, Exception {

		Collection listaB = succA.getListSuccessioniB();
		Collection listaC = succA.getListSuccessioniC();
		Collection listaD = succA.getListSuccessioniD();

		// TIPO RECORD 2 - Dati della successione
		appendConSepa(sbOut, "" + idNota);
		appendConSepa(sbOut, "2");
		appendConSepa(sbOut, succA.getA_tipo_dichiarazione() != null ? succA.getA_tipo_dichiarazione() : "");
		appendConSepa(sbOut, allineaSx(3, succA.getA_ufficio()) + allineaSx(2, succA.getA_anno()) + allineaDxZero(5, succA.getA_volume()) + allineaDxZero(6, succA.getA_numero()) + allineaDxZero(3, succA.getA_sottonumero()));
		// appendConSepa(sbOut, allineaDxZero(6, succA.getA_numero()));

		/**
		 * @todo Valore di NUMERO NOTA da rivedere. Sul DB il campo prevede l'inserimento di un varchar da 6, mentre secondo le specifiche di Paolo dovrebbe essere di 19 (vedi linea di codice commentata)
		 */
		// appendConSepa(sbOut, "0");
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, (succA.getA_data_apertura() != null && succA.getA_data_apertura().length() >= 4) ? succA.getA_data_apertura().substring(0, 4) : "");
		appendConSepa(sbOut, (succA.getA_data_apertura() != null) ? parseStringDateToDDMMYYYY(succA.getA_data_apertura()) : "");
		appendConSepa(sbOut, NON_VALORIZZATO, 14);
		appendConAcapo(sbOut, "", nrRecordEsportati);

		// TIPO RECORD 3 - Informazioni generali su soggetti - Tipo [D]Defunto
		appendConSepa(sbOut, "" + idNota);
		appendConSepa(sbOut, "3");
		appendConSepa(sbOut, "1"); // Id Soggetto Nota
		appendConSepa(sbOut, ID_SOGGETTO_CATASTALE_DEFAULT);
		// appendConSepa(sbOut, "D"); Per ora mettiamo P persona fisica,
		// altrimenti l'estrazione DAP crea problemi
		appendConSepa(sbOut, "P");
		appendConSepa(sbOut, succA.getA_cognome_defunto());
		appendConSepa(sbOut, succA.getA_nome_defunto());
		appendConSepa(sbOut, "M".equalsIgnoreCase(succA.getA_sesso()) ? "1" : ("F".equalsIgnoreCase(succA.getA_sesso()) ? "2" : succA.getA_sesso()));
		appendConSepa(sbOut, (succA.getA_data_nascita() != null) ? parseStringDateToDDMMYYYY(succA.getA_data_nascita()) : "");
		// appendConSepa(sbOut, succA.getA_citta_nascita() + " (" +
		// succA.getA_prov_nascita() + ")");
		appendConSepa(sbOut, getCodBelfioreComuneNascita(succA));
		appendConSepa(sbOut, succA.getA_cf_defunto());
		appendConSepa(sbOut, NON_VALORIZZATO, 3);
		appendConAcapo(sbOut, "", nrRecordEsportati);

		// TIPO RECORD 5 - Indirizzi soggetti - Tipo [D]Defunto
		appendConSepa(sbOut, "" + idNota);
		appendConSepa(sbOut, "5");
		appendConSepa(sbOut, "1"); // Id Soggetto Nota
		/**
		 * @todo Come si fa a capire quando il tipo indirizzo è residenza? Per ora io considero che si tratti sempre di indirizzo di residenza. Quindi metto come valore 1
		 */
		appendConSepa(sbOut, "1");
		appendConSepa(sbOut, succA.getA_citta_residenza());
		appendConSepa(sbOut, succA.getA_prov_residenza());
		appendConSepa(sbOut, succA.getA_indirizzo_residenza());
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConAcapo(sbOut, "", nrRecordEsportati);

		int idSoggettoNota = 2; // idSoggetto = 1 è il defunto
		for (Iterator iter = listaB.iterator(); iter.hasNext();) {
			SuccessioniB succBCurr = (SuccessioniB) iter.next();
			// TIPO RECORD 3 - Informazioni generali su soggetti - Tipo [E]Erede
			appendConSepa(sbOut, "" + idNota);
			appendConSepa(sbOut, "3");
			appendConSepa(sbOut, "" + idSoggettoNota);
			appendConSepa(sbOut, ID_SOGGETTO_CATASTALE_DEFAULT);
			// appendConSepa(sbOut, "E"); Per ora mettiamo P persona fisica,
			// altrimenti l'estrazione DAP crea problemi
			appendConSepa(sbOut, "P");
			String bDenominazione = succBCurr.getB_denominazione();
			String bDenominazione1 = "";
			String bDenominazione2 = "";
			if (bDenominazione != null) {
				if (bDenominazione.length() > 25) {
					bDenominazione1 = bDenominazione.substring(0, 25);
					bDenominazione2 = bDenominazione.substring(25);
				} else {
					bDenominazione1 = bDenominazione;
					bDenominazione2 = "";
				}
			}
			appendConSepa(sbOut, !"S".equalsIgnoreCase(succBCurr.getB_sesso()) ? bDenominazione1.trim() : "");
			appendConSepa(sbOut, !"S".equalsIgnoreCase(succBCurr.getB_sesso()) ? bDenominazione2.trim() : "");
			appendConSepa(sbOut, "M".equalsIgnoreCase(succBCurr.getB_sesso()) ? "1" : ("F".equalsIgnoreCase(succBCurr.getB_sesso()) ? "2" : succBCurr.getB_sesso()));
			appendConSepa(sbOut, (succBCurr.getB_data_nascita() != null) ? parseStringDateToDDMMYYYY(succBCurr.getB_data_nascita()) : "");
			// appendConSepa(sbOut, succBCurr.getB_citta_nascita() + " (" +
			// succBCurr.getB_prov_nascita() + ")");
			appendConSepa(sbOut, getCodBelfioreComuneNascita(succBCurr));
			appendConSepa(sbOut, !"S".equalsIgnoreCase(succBCurr.getB_sesso()) ? succBCurr.getB_cf_erede() : "");
			appendConSepa(sbOut, "S".equalsIgnoreCase(succBCurr.getB_sesso()) ? bDenominazione : "");
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, "S".equalsIgnoreCase(succBCurr.getB_sesso()) ? succBCurr.getB_cf_erede() : "");
			appendConAcapo(sbOut, "", nrRecordEsportati);

			// TIPO RECORD 5 - Indirizzi soggetti - Tipo [E]Erede
			appendConSepa(sbOut, "" + idNota);
			appendConSepa(sbOut, "5");
			appendConSepa(sbOut, "" + idSoggettoNota); // Id Soggetto Nota
			/**
			 * @todo Come si fa a capire quando il tipo indirizzo è residenza? Per ora io considero che si tratti sempre di indirizzo di residenza. Quindi metto come valore 1
			 */
			appendConSepa(sbOut, "1");
			appendConSepa(sbOut, succBCurr.getB_citta_residenza());
			appendConSepa(sbOut, succBCurr.getB_prov_residenza());
			appendConSepa(sbOut, succBCurr.getB_indirizzo_residenza());
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConAcapo(sbOut, "", nrRecordEsportati);

			// Incrementiamo di uno il campo idSoggetto
			idSoggettoNota++;
		}

		int idImmobile = 1;
		for (Iterator iterC = listaC.iterator(); iterC.hasNext();) {
			SuccessioniC succCCurr = (SuccessioniC) iterC.next();

			// TIPO RECORD 4 - Informazioni generali sulla titolarità - Tipo
			// [D]Defunto
			appendConSepa(sbOut, "" + idNota);
			appendConSepa(sbOut, "4");
			appendConSepa(sbOut, "1"); // Mi riferisco al defunto ossia la soggetto
			// A
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, !"S".equalsIgnoreCase(succA.getA_sesso()) ? "P" : "G");
			appendConSepa(sbOut, "" + idImmobile);
			if ("U".equalsIgnoreCase(succCCurr.getC_catasto())) {
				appendConSepa(sbOut, "F");
			} else if ("T".equalsIgnoreCase(succCCurr.getC_catasto())) {
				appendConSepa(sbOut, "T");
			} else {
				throw new MuiException("Valore MI_SUCCESSIONI_C.CATASTO = " + succCCurr.getC_catasto() + ". Il valore deve essere \"T\" o \"U\"");
			}
			appendConSepa(sbOut, "C"); // Valorizzato solo per il defunto
			// marcoric 24-11-2008 : abbiamo trovato diritti non numerici "1S"
			Integer diritto = null;
			try {
				diritto = new Integer(succCCurr.getC_diritto());
			} catch (NumberFormatException e) {
			}
			if (diritto !=null)
				appendConSepa(sbOut,diritto.intValue() > 9 ? "9" : diritto.toString() );
			else
				appendConSepa(sbOut,succCCurr.getC_diritto());
				
			appendConSepa(sbOut, succCCurr.getC_numeratore_quota_def());
			appendConSepa(sbOut, succCCurr.getC_denominatore_quota_def());
			appendConSepa(sbOut, NON_VALORIZZATO, 2);
			// appendConSepa(sbOut, succCCurr.getC_diritto());
			appendConSepa(sbOut, NON_VALORIZZATO, 4); // Valorizzato solo per Eredi
			/*
			 * appendConSepa(sbOut, (Integer.parseInt(succCCurr.getC_diritto()) > 9) ? "9" : succCCurr.getC_diritto()); appendConSepa(sbOut, succCCurr.getC_numeratore_quota_def()); appendConSepa(sbOut, succCCurr.getC_denominatore_quota_def());
			 */
			appendConSepa(sbOut, NON_VALORIZZATO, 18);
			appendConAcapo(sbOut, "", nrRecordEsportati);

			int idSoggettoNotaTR4 = 2; // idSoggetto = 1 è il defunto
			for (Iterator iterB = listaB.iterator(); iterB.hasNext();) {
				
				SuccessioniB succBCurr = (SuccessioniB) iterB.next();
				SuccessioniD succDCurr = null;
				for (Iterator iterD = listaD.iterator(); iterD.hasNext();) {
					SuccessioniD succDToCheck = (SuccessioniD) iterD.next();
					if (succDToCheck.getD_progressivo_immobile() != null && succDToCheck.getD_progressivo_immobile().equalsIgnoreCase(succCCurr.getC_progressivo_immobile()) &&
						 succDToCheck.getD_progressivo_erede() != null && succDToCheck.getD_progressivo_erede().equalsIgnoreCase(succBCurr.getB_progressivo_erede())) {
						succDCurr = succDToCheck;
						break;
					}
					
				}
				
				if (succDCurr!=null) {
					// TIPO RECORD 4 - Informazioni generali sulla titolarità - Tipo
					// [E]Erede
					appendConSepa(sbOut, "" + idNota);
					appendConSepa(sbOut, "4");
					appendConSepa(sbOut, "" + idSoggettoNotaTR4); // Mi riferisco al
					// defunto ossia la
					// soggetto A
					appendConSepa(sbOut, NON_VALORIZZATO); 
					appendConSepa(sbOut, !"S".equalsIgnoreCase(succBCurr.getB_sesso()) ? "P" : "G");
					appendConSepa(sbOut, "" + idImmobile);
					if ("U".equalsIgnoreCase(succCCurr.getC_catasto())) {
						appendConSepa(sbOut, "F");
					} else if ("T".equalsIgnoreCase(succCCurr.getC_catasto())) {
						appendConSepa(sbOut, "T");
					} else {
						throw new MuiException("Valore MI_SUCCESSIONI_C.CATASTO = " + succCCurr.getC_catasto() + ". Il valore deve essere \"T\" o \"U\"");
					}
					appendConSepa(sbOut, NON_VALORIZZATO, 4); // Valorizzato solo per il
					// defunto
					/*
					 * appendConSepa(sbOut, (Integer.parseInt(succCCurr.getC_diritto()) > 9) ? "9" : succCCurr.getC_diritto()); appendConSepa(sbOut, succCCurr.getC_numeratore_quota_def()); appendConSepa(sbOut, succCCurr.getC_denominatore_quota_def());
					 */
					appendConSepa(sbOut, NON_VALORIZZATO, 2);
					// appendConSepa(sbOut, succCCurr.getC_diritto());
					appendConSepa(sbOut, "F"); // Valorizzato solo per Eredi
					// marcoric 24-11-2008 : abbiamo trovato diritti non numerici "1S"
					diritto = null;
					try {
						diritto = new Integer(succCCurr.getC_diritto());
					} catch (NumberFormatException e) {
					}
					if (diritto !=null)
						appendConSepa(sbOut,diritto.intValue() > 9 ? "9" : diritto.toString() );
					else
						appendConSepa(sbOut,succCCurr.getC_diritto());
	
					if (succDCurr != null) {
						Logger.log().info(this.getClass().getName(), "[D] - Num: " + succDCurr.getD_numeratore_quota() + " - Den: " + succDCurr.getD_denominatore_quota());
						String dNumeratoreQuotaCurr = succDCurr.getD_numeratore_quota();
						String dDenominatoreQuotaCurr = succDCurr.getD_denominatore_quota();
						String cNumeratoreQuotaCurr = succCCurr.getC_numeratore_quota_def();
						String cDenominatoreQuotaCurr = succCCurr.getC_denominatore_quota_def();
						if (cNumeratoreQuotaCurr == null || cNumeratoreQuotaCurr.trim().length() == 0 ||
								cDenominatoreQuotaCurr == null || cDenominatoreQuotaCurr.trim().length() == 0 ) {
							appendConSepa(sbOut, dNumeratoreQuotaCurr);
							appendConSepa(sbOut, dDenominatoreQuotaCurr);
						} else if (dNumeratoreQuotaCurr != null && dNumeratoreQuotaCurr.trim().length() > 0 
										&& dDenominatoreQuotaCurr != null && dDenominatoreQuotaCurr.trim().length() > 0) {
							try {
								// MARCORIC 3-11-2008 : LE QUOTE IN INPUT CON LE VIRGOLE DAVANO ERRORE
								appendConSepa(sbOut, "" + (Float.parseFloat(dNumeratoreQuotaCurr)*Float.parseFloat(cNumeratoreQuotaCurr)));
								appendConSepa(sbOut, "" + (Float.parseFloat(dDenominatoreQuotaCurr)*Float.parseFloat(cDenominatoreQuotaCurr)));
							} catch (NumberFormatException nfe) {
								Logger.log().info(this.getClass().getName(), "I seguenti valori devono essere tutti valori NUMERICI. MI_SUCCESSIONI_C.NUMERATORE_QUOTA_DEF=" + cNumeratoreQuotaCurr +
																							" - MI_SUCCESSIONI_C.DENOMINATORE_QUOTA_DEF=" + cDenominatoreQuotaCurr +
																							" - MI_SUCCESSIONI_D.NUMERATORE_QUOTA=" + dNumeratoreQuotaCurr +
																							" - MI_SUCCESSIONI_D.DENOMINATORE_QUOTA=" + dDenominatoreQuotaCurr + "[Pk_id_succa=" + succA.getPk_id_succa() + "]");
							}
						} else {
							appendConSepa(sbOut, dNumeratoreQuotaCurr);
							appendConSepa(sbOut, dDenominatoreQuotaCurr);						
						}
					} else {
						Logger.log().info(this.getClass().getName(), "[C] - Num: " + succCCurr.getC_numeratore_quota_def() + " - Den: " + succCCurr.getC_denominatore_quota_def());
						appendConSepa(sbOut, succCCurr.getC_numeratore_quota_def());
						appendConSepa(sbOut, succCCurr.getC_denominatore_quota_def());					
					}
					appendConSepa(sbOut, NON_VALORIZZATO, 18);
					appendConAcapo(sbOut, "", nrRecordEsportati);
	
				}
				// marcoric : modifica del 23/3 la precedente if if (succDCurr!=null)  non esisteva
				// risolve il problema dei testamentari
				// Incrementiamo di uno il campo idSoggetto
				idSoggettoNotaTR4++;
			}

			// TIPO RECORD 6
			appendConSepa(sbOut, "" + idNota);
			appendConSepa(sbOut, "6");
			appendConSepa(sbOut, "F");
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, "" + idImmobile);
			appendConSepa(sbOut, NON_VALORIZZATO, 2);
			String cNatura = succCCurr.getC_natura();
			appendConSepa(sbOut, cNatura);
			appendConSepa(sbOut, NON_VALORIZZATO);
			// Le specifiche della procedura da caricamento prevedono qui valori
			// del tipo:
			// "A01", "A02", "A03", "A04", "A05", "A06", "A07", "A08", "A09",
			// quindi sostituisco i corrispondenti
			// valori che mi arrivano senza lo 0 intermedio "A1, "A2" per
			// intenderci con quello "giusto"
			HashMap tipologieAbitazioni = new HashMap();
			tipologieAbitazioni.put("A1", "A01");
			tipologieAbitazioni.put("A2", "A02");
			tipologieAbitazioni.put("A3", "A03");
			tipologieAbitazioni.put("A4", "A04");
			tipologieAbitazioni.put("A5", "A05");
			tipologieAbitazioni.put("A6", "A06");
			tipologieAbitazioni.put("A7", "A07");
			tipologieAbitazioni.put("A8", "A08");
			tipologieAbitazioni.put("A9", "A09");
			appendConSepa(sbOut, tipologieAbitazioni.containsKey(cNatura) ? (String) tipologieAbitazioni.get(cNatura) : cNatura);
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, succCCurr.getC_vani());
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, succCCurr.getC_superficie_mq());
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, DA_RICERCA_A_CATASTO_NUM);
			appendConSepa(sbOut, NON_VALORIZZATO, 10);
			appendConSepa(sbOut, (succCCurr.getC_indirizzo_immobile() != null) ? succCCurr.getC_indirizzo_immobile() : "");
			appendConSepa(sbOut, NON_VALORIZZATO, 12);
			appendConSepa(sbOut, DA_RICERCA_A_CATASTO, 5);
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConAcapo(sbOut, "", nrRecordEsportati);
			nrRecordEsportati.put("TR6", "" + (Integer.parseInt((String) nrRecordEsportati.get("TR6")) + 1));

			// TIPO RECORD 7 - Dati identificativi dei fabbricati
			appendConSepa(sbOut, "" + idNota);
			appendConSepa(sbOut, "7");
			appendConSepa(sbOut, "" + idImmobile);
			appendConSepa(sbOut, NON_VALORIZZATO, 3);
			appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_foglio() : "");
			appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_particella1() : "");
			appendConSepa(sbOut, NON_VALORIZZATO);
			appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? (succCCurr.getC_particella2() != null && succCCurr.getC_particella2().length() > 0 ? succCCurr.getC_particella2() : succCCurr.getC_subalterno1()) : "");
			appendConSepa(sbOut, NON_VALORIZZATO, 2);
			appendConSepa(sbOut, "1".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_denuncia1() : "");
			appendConSepa(sbOut, "1".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_anno_denuncia() : "");
			appendConAcapo(sbOut, "", nrRecordEsportati);

			if ("T".equalsIgnoreCase(succCCurr.getC_catasto())) {
				// TIPO RECORD 8 - Informazioni relative ai terreni (Solo per record
				// con SUCCESSIONI_C.CATASTO = "T")
				appendConSepa(sbOut, "" + idNota);
				appendConSepa(sbOut, "8");
				appendConSepa(sbOut, NON_VALORIZZATO);
				appendConSepa(sbOut, "" + idImmobile);
				appendConSepa(sbOut, NON_VALORIZZATO, 3);
				appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_foglio() : "");
				appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? succCCurr.getC_particella1() : "");
				appendConSepa(sbOut, NON_VALORIZZATO);
				appendConSepa(sbOut, "0".equalsIgnoreCase(succCCurr.getC_tipo_dati()) ? (succCCurr.getC_particella2() != null && succCCurr.getC_particella2().length() > 0 ? succCCurr.getC_particella2() : succCCurr.getC_subalterno1()) : "");
				appendConSepa(sbOut, NON_VALORIZZATO);
				appendConSepa(sbOut, succCCurr.getC_natura());
				appendConSepa(sbOut, NON_VALORIZZATO, 2);
				appendConSepa(sbOut, succCCurr.getC_superficie_ettari());
				appendConSepa(sbOut, NON_VALORIZZATO, 3);
				appendConSepa(sbOut, DA_RICERCA_A_CATASTO_NUM);
				appendConSepa(sbOut, NON_VALORIZZATO, 2);
				appendConAcapo(sbOut, "", nrRecordEsportati);
				nrRecordEsportati.put("TR8", "" + (Integer.parseInt((String) nrRecordEsportati.get("TR8")) + 1));
			}

			// Incrementiamo di uno il campo idImmobile
			idImmobile++;
		}

	}
	
	
	/**
	 * Dato un oggetto SuccessioniA o SuccessioniB, restituisce il codice Belfiore del comune di nascita 
	 * del soggetto
	 * @param succ
	 * @return
	 */
	private String getCodBelfioreComuneNascita(Object succ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String codBelfiore = null;
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String sql = "SELECT CODI_FISC_LUNA FROM SITICOMU WHERE NOME = ?";				
			pstmt = conn.prepareStatement(sql);
			if (succ instanceof SuccessioniA) {
				SuccessioniA succA = (SuccessioniA)succ;
				if (succA.getA_citta_nascita() != null && !succA.getA_citta_nascita().trim().equals("")) {					
					pstmt.setString(1, succA.getA_citta_nascita().trim());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						if (rs.getObject("CODI_FISC_LUNA") != null && !rs.getString("CODI_FISC_LUNA").trim().equals("")) {
							codBelfiore = rs.getString("CODI_FISC_LUNA").trim();
						}
					}
				}
				//"prova del nove" con il codice fiscale
				if (codBelfiore == null || codBelfiore.equals("")) {
					if (succA.getA_cf_defunto() != null && succA.getA_cf_defunto().trim().length() == 16) {
						codBelfiore = succA.getA_cf_defunto().trim().substring(11, 15);
					}
				} else {
					if (succA.getA_cf_defunto() != null && succA.getA_cf_defunto().trim().length() == 16
						&& !codBelfiore.equals(succA.getA_cf_defunto().trim().substring(11, 15))) {
						codBelfiore = succA.getA_cf_defunto().trim().substring(11, 15);
					}
				}
			} else if (succ instanceof SuccessioniB) {
				SuccessioniB succB = (SuccessioniB)succ;
				if (succB.getB_citta_nascita() != null && !succB.getB_citta_nascita().trim().equals("")) {					
					pstmt.setString(1, succB.getB_citta_nascita().trim());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						if (rs.getObject("CODI_FISC_LUNA") != null && !rs.getString("CODI_FISC_LUNA").trim().equals("")) {
							codBelfiore = rs.getString("CODI_FISC_LUNA").trim();
						}
					}
				}
				//"prova del nove" con il codice fiscale
				if (codBelfiore == null || codBelfiore.equals("")) {
					if (succB.getB_cf_erede() != null && succB.getB_cf_erede().trim().length() == 16) {
						codBelfiore = succB.getB_cf_erede().trim().substring(11, 15);
					}
				} else {
					if (succB.getB_cf_erede() != null && succB.getB_cf_erede().trim().length() == 16
						&& !codBelfiore.equals(succB.getB_cf_erede().trim().substring(11, 15))) {
						codBelfiore = succB.getB_cf_erede().trim().substring(11, 15);
					}
				}
			}			
		} catch (Exception e) {
			Logger.log().info(this.getClass().getName(), "Errore nella lettura del codice del comune: " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				Logger.log().info(this.getClass().getName(), "Errore nella chiusura della connessione: " + e.getMessage());
			}			
		}
		if (codBelfiore == null || codBelfiore.equals("")) {
			//per default restituisce il valore fittizio che era inserito fisso in precedenza
			codBelfiore = MuiApplication.belfiore;
		}
		return codBelfiore;
	}

	/**
	 * Dato un oggetto SuccessioniA scrive nel buffer tutte le informazioni contenute in tale oggetto, comprese quelle dei record SuccessioniB e SuccessioniC collegati nel nuovo formato previsto per l'export
	 * 
	 * @param sbOut
	 * @param succA
	 * @param dataEsportazione
	 * @param idNota
	 * @param nrRecordEsportati
	 * @throws MuiException
	 * @throws Exception
	 *            private void writeAExport(StringBuffer sbOut, SuccessioniA succA, String dataEsportazione, int idNota, Hashtable nrRecordEsportati) throws MuiException, Exception {
	 * 
	 * Collection listaB = succA.getListSuccessioniB(); Collection listaC = succA.getListSuccessioniC();
	 * 
	 * for (Iterator iterC = listaC.iterator(); iterC.hasNext();) { SuccessioniC succCCurr = (SuccessioniC) iterC.next();
	 * 
	 * writeSingleRecordExport(sbOut, succA, null, succCCurr, dataEsportazione, idNota, nrRecordEsportati);
	 * 
	 * for (Iterator iterB = listaB.iterator(); iterB.hasNext();) { SuccessioniB succBCurr = (SuccessioniB) iterB.next(); writeSingleRecordExport(sbOut, succA, succBCurr, succCCurr, dataEsportazione, idNota, nrRecordEsportati); } } }
	 */

	/**
	 * Dato un oggetto SuccessioniA scrive nel buffer tutte le informazioni contenute in tale oggetto, comprese quelle dei record SuccessioniB e SuccessioniC collegati nel nuovo formato previsto per l'export
	 * 
	 * @param sbOut
	 * @param succA
	 * @param succB
	 * @param succC
	 * @param dataEsportazione
	 * @param idNota
	 * @param nrRecordEsportati
	 * @throws MuiException
	 * @throws Exception
	 *            private void writeSingleRecordExport(StringBuffer sbOut, SuccessioniA succA, SuccessioniB succB, SuccessioniC succC, String dataEsportazione, int idNota, Hashtable nrRecordEsportati) throws MuiException, Exception {
	 * 
	 * boolean isErede = succB != null;
	 * 
	 * appendConSepaExport(sbOut, "" + idNota); appendConSepaExport(sbOut, dataEsportazione); appendConSepaExport(sbOut, isErede ? succB.getB_cf_erede() : succA.getA_cf_defunto()); appendConSepaExport(sbOut, NON_VALORIZZATO, 2);
	 * 
	 * String bDenominazione1 = ""; String bDenominazione2 = ""; if (isErede) { String bDenominazione = succB.getB_denominazione(); if (bDenominazione != null) { if (bDenominazione.length() > 25) { bDenominazione1 = bDenominazione.substring(0, 25); bDenominazione2 = bDenominazione.substring(25); } else { bDenominazione1 = bDenominazione; bDenominazione2 = ""; } } }
	 * 
	 * appendConSepaExport(sbOut, isErede ? bDenominazione1.trim() : succA.getA_cognome_defunto()); appendConSepaExport(sbOut, isErede ? bDenominazione2.trim() : succA.getA_nome_defunto()); appendConSepaExport(sbOut, isErede ? parseStringDateToYYYYMMDD(succB.getB_data_nascita()) : parseStringDateToYYYYMMDD(succA.getA_data_nascita())); appendConSepaExport(sbOut, isErede ? succB.getB_sesso() :
	 * succA.getA_sesso()); appendConSepaExport(sbOut, isErede ? succB.getB_citta_nascita() : succA.getA_citta_nascita()); appendConSepaExport(sbOut, isErede ? succB.getB_prov_nascita() : succA.getA_prov_nascita()); appendConSepaExport(sbOut, isErede ? succB.getB_indirizzo_residenza() : succA.getA_indirizzo_residenza()); appendConSepaExport(sbOut, NON_VALORIZZATO); appendConSepaExport(sbOut,
	 * isErede ? succB.getB_citta_residenza() : succA.getA_citta_residenza()); appendConSepaExport(sbOut, isErede ? succB.getB_prov_residenza() : succA.getA_prov_residenza()); appendConSepaExport(sbOut, NON_VALORIZZATO, 18); appendConSepaExport(sbOut, succA.getA_data_apertura().substring(0, 4)); appendConSepaExport(sbOut, NON_VALORIZZATO, 5); appendConSepaExport(sbOut, isErede ?
	 * (!"S".equalsIgnoreCase(succB.getB_sesso()) ? "P" : "G") : (!"S".equalsIgnoreCase(succA.getA_sesso()) ? "P" : "G")); appendConSepaExport(sbOut, "U".equalsIgnoreCase(succC.getC_catasto()) ? "F" : "T"); appendConSepaExport(sbOut, succC.getC_indirizzo_immobile()); appendConSepaExport(sbOut, NON_VALORIZZATO, 2);
	 * 
	 * appendConSepaExport(sbOut, "0".equalsIgnoreCase(succC.getC_tipo_dati()) ? succC.getC_foglio() : ""); appendConSepaExport(sbOut, "0".equalsIgnoreCase(succC.getC_tipo_dati()) ? succC.getC_particella1() : ""); appendConSepaExport(sbOut, "0".equalsIgnoreCase(succC.getC_tipo_dati()) ? (succC.getC_particella2() != null && succC.getC_particella2().length() > 0 ? succC.getC_particella2() :
	 * succC.getC_subalterno1()) : ""); appendConSepaExport(sbOut, "1".equalsIgnoreCase(succC.getC_tipo_dati()) ? succC.getC_denuncia1() : ""); appendConSepaExport(sbOut, "1".equalsIgnoreCase(succC.getC_tipo_dati()) ? succC.getC_anno_denuncia() : ""); appendConSepaExport(sbOut, NON_VALORIZZATO, 7); appendConSepaExport(sbOut, DA_RICERCA_A_CATASTO);
	 * 
	 * NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); nf.setMaximumFractionDigits(2); nf.setGroupingUsed(false); nf.setMaximumFractionDigits(2); appendConSepaExport(sbOut, nf.format(Double.parseDouble(succC.getC_numeratore_quota_def()) / Double.parseDouble(succC.getC_denominatore_quota_def()))); appendConSepaExport(sbOut, NON_VALORIZZATO); appendConSepaExport(sbOut, isErede ?
	 * succC.getC_diritto() : ""); appendConSepaExport(sbOut, isErede ? "" : succC.getC_diritto()); appendConSepaExport(sbOut, NON_VALORIZZATO); appendConSepaExport(sbOut, isErede ? "V" : "C"); appendConSepaExport(sbOut, parseStringDateToYYYYMMDD(succA.getA_data_apertura())); appendConSepaExport(sbOut, NON_VALORIZZATO, 5); appendConSepaExport(sbOut, DA_RICERCA_A_CATASTO, 9);
	 * appendConSepaExport(sbOut, NON_VALORIZZATO); appendConSepaExport(sbOut, DA_RICERCA_A_CATASTO); appendConSepaExport(sbOut, NON_VALORIZZATO, 6); appendConSepaExport(sbOut, "1"); appendConSepaExport(sbOut, DA_RICERCA_A_CATASTO); appendConSepaExport(sbOut, "S"); appendConSepaExport(sbOut, DA_SPECIFICHE_MUI_DAP, 3); appendConSepaExport(sbOut, NON_VALORIZZATO); appendConSepaExport(sbOut,
	 * DA_SPECIFICHE_MUI_DAP); appendConSepaExport(sbOut, NON_VALORIZZATO, 9);
	 * 
	 * appendConAcapo(sbOut, allineaSx(3, succA.getA_ufficio()) + allineaSx(2, succA.getA_anno()) + allineaDxZero(5, succA.getA_volume()) + allineaDxZero(6, succA.getA_numero()) + allineaDxZero(3, succA.getA_sottonumero()), null); }
	 */

	/**
	 * Data una stringa data nel formato 'YYYY-MM-DD' ritorna una stringa nel formato 'DDMMYYYY'
	 * 
	 * @param dataToParse
	 * @return
	 * @throws Exception
	 */
	public static String parseStringDateToDDMMYYYY(String dataToParse) throws Exception {
		if (dataToParse != null && dataToParse.length() > 0) {
			if (dataToParse.length() == 10) {
				String anno = dataToParse.substring(0, 4);
				String mese = dataToParse.substring(5, 7);
				String giorno = dataToParse.substring(8, 10);
				return giorno + mese + anno;
			} else {
				throw new MuiException("Formato data sbagliato: " + dataToParse);
			}
		} else {
			return null;
		}
	}

	/**
	 * Data una stringa data nel formato 'YYYY-MM-DD' ritorna una stringa nel formato 'YYYYMMDD'
	 * 
	 * @param dataToParse
	 * @return
	 * @throws Exception
	 */
	public static String parseStringDateToYYYYMMDD(String dataToParse) throws Exception {
		if (dataToParse != null && dataToParse.length() > 0) {
			if (dataToParse.length() == 10) {
				String anno = dataToParse.substring(0, 4);
				String mese = dataToParse.substring(5, 7);
				String giorno = dataToParse.substring(8, 10);
				return anno + mese + giorno;
			} else {
				throw new MuiException("Formato data sbagliato: " + dataToParse);
			}
		} else {
			return null;
		}
	}

	/**
	 * Data una stringa data nel formato 'DDMMYYYY' ritorna una stringa nel formato 'YYYY-MM-DD'
	 * 
	 * @param dataToParse
	 * @return
	 * @throws Exception
	 */
	public static String parseStringDateFromDDMMYYYY(String dataToParse) throws Exception {
		if (dataToParse != null && dataToParse.length() > 0) {
			if (dataToParse.length() == 8) {
				String giorno = dataToParse.substring(0, 2);
				String mese = dataToParse.substring(2, 4);
				String anno = dataToParse.substring(4, 8);
				return anno + "-" + mese + "-" + giorno;
			} else {
				throw new MuiException("Formato data sbagliato: " + dataToParse);
			}
		} else {
			return null;
		}
	}

	/**
	 * METODO ATTUALMENTE NON UTILIZZATO Controlla che la correttezza delle date passate in input (devono essere nel formato YYYY-MM-DD)
	 * 
	 * @param dataInizio
	 * @param dataFine
	 * @throws Exception
	 */
	private void checkInputDate(String dataInizio, String dataFine) throws Exception {
		boolean dateCorrette = true;
		if (dataInizio != null && dataInizio.length() == 10 && dataFine != null && dataFine.length() == 10) {

			Date dInizio = dateformatDB.parse(dataInizio);
			Date dFine = dateformatDB.parse(dataFine);

			Calendar oggiCal = Calendar.getInstance();
			oggiCal.setTime(dInizio);
			oggiCal.add(Calendar.DAY_OF_MONTH, ggMaxEstrazione);
			Date dInizioPiuGiorniMax = oggiCal.getTime();

			if (dInizioPiuGiorniMax.before(dFine)) {
				Logger.log().info(this.getClass().getName(), "Data inizio: " + dInizio.toString());
				Logger.log().info(this.getClass().getName(), "Data inizio più giorni: " + dInizioPiuGiorniMax.toString());
				Logger.log().info(this.getClass().getName(), "Data fine: " + dFine.toString());
				throw new MuiException("Intervallo date data_inizio e data_fine troppo ampio. Numero massimo di giorni consentito: " + ggMaxEstrazione);
			}

		} else {
			dateCorrette = false;
		}
		if (!dateCorrette) {
			throw new MuiException("Date \"data_inizio\" e \"data_fine\" nulle o nel formato non corretto: " + dataInizio + " - " + dataFine + ". Formato corretto: YYYY-MM-DD");
		}
	}

	private ResultSetSuccessioni make(ResultSet rs) throws Exception {
		ResultSetSuccessioni rss = new ResultSetSuccessioni();
		try {
			rss.setPk_id_succa(rs.getString("pk_id_succa"));
			rss.setA_ufficio(rs.getString("ufficio"));
			rss.setA_anno(rs.getString("anno"));
			rss.setA_volume(rs.getString("volume"));
			rss.setA_numero(rs.getString("numero"));
			rss.setA_sottonumero(rs.getString("sottonumero"));
			rss.setA_comune(rs.getString("comune"));
			rss.setA_progressivo(rs.getString("progressivo"));
			rss.setA_tipo_dichiarazione(rs.getString("tipo_dichiarazione"));
			rss.setA_data_apertura(rs.getString("data_apertura"));
			rss.setA_cf_defunto(rs.getString("cf_defunto"));
			rss.setA_cognome_defunto(rs.getString("cognome_defunto"));
			rss.setA_nome_defunto(rs.getString("nome_defunto"));
			rss.setA_sesso(rs.getString("a_sesso"));
			rss.setA_citta_nascita(rs.getString("a_citta_nascita"));
			rss.setA_prov_nascita(rs.getString("a_prov_nascita"));
			rss.setA_data_nascita(rs.getString("a_data_nascita"));
			rss.setA_citta_residenza(rs.getString("a_citta_residenza"));
			rss.setA_prov_residenza(rs.getString("a_prov_residenza"));
			rss.setA_indirizzo_residenza(rs.getString("a_indirizzo_residenza"));
			rss.setA_fornitura(rs.getString("fornitura"));

			rss.setPk_id_succb(rs.getString("pk_id_succb"));
			rss.setB_ufficio(rs.getString("ufficio"));
			rss.setB_anno(rs.getString("anno"));
			rss.setB_volume(rs.getString("volume"));
			rss.setB_numero(rs.getString("numero"));
			rss.setB_sottonumero(rs.getString("sottonumero"));
			rss.setB_comune(rs.getString("comune"));
			rss.setB_progressivo(rs.getString("b_progressivo"));
			rss.setB_progressivo_erede(rs.getString("b_progressivo_erede"));
			rss.setB_cf_erede(rs.getString("cf_erede"));
			rss.setB_denominazione(rs.getString("denominazione"));
			rss.setB_sesso(rs.getString("b_sesso"));
			rss.setB_citta_nascita(rs.getString("b_citta_nascita"));
			rss.setB_prov_nascita(rs.getString("b_prov_nascita"));
			rss.setB_data_nascita(rs.getString("b_data_nascita"));
			rss.setB_citta_residenza(rs.getString("b_citta_residenza"));
			rss.setB_prov_residenza(rs.getString("b_prov_residenza"));
			rss.setB_indirizzo_residenza(rs.getString("b_indirizzo_residenza"));
			rss.setB_fornitura(rs.getString("fornitura"));

			rss.setC_ufficio(rs.getString("ufficio"));
			rss.setC_anno(rs.getString("anno"));
			rss.setC_volume(rs.getString("volume"));
			rss.setC_numero(rs.getString("numero"));
			rss.setC_sottonumero(rs.getString("sottonumero"));
			rss.setC_comune(rs.getString("comune"));
			rss.setC_progressivo(rs.getString("c_progressivo"));
			rss.setC_progressivo_immobile(rs.getString("c_progressivo_immobile"));
			rss.setC_fornitura(rs.getString("fornitura"));
			rss.setC_numeratore_quota_def(rs.getString("numeratore_quota_def"));
			rss.setC_denominatore_quota_def(rs.getString("denominatore_quota_def"));
			rss.setC_diritto(rs.getString("diritto"));
			rss.setC_catasto(rs.getString("catasto"));
			rss.setC_tipo_dati(rs.getString("tipo_dati"));
			rss.setC_foglio(rs.getString("foglio"));
			rss.setC_particella1(rs.getString("particella1"));
			rss.setC_particella2(rs.getString("particella2"));
			rss.setC_subalterno1(rs.getString("subalterno1"));
			rss.setC_subalterno2(rs.getString("subalterno2"));
			rss.setC_denuncia1(rs.getString("denuncia1"));
			rss.setC_anno_denuncia(rs.getString("anno_denuncia"));
			rss.setC_natura(rs.getString("natura"));
			rss.setC_superficie_ettari(rs.getString("superficie_ettari"));
			rss.setC_superficie_mq(rs.getString("superficie_mq"));
			rss.setC_vani(rs.getString("vani"));
			rss.setC_indirizzo_immobile(rs.getString("indirizzo_immobile"));

			rss.setD_ufficio(rs.getString("ufficio"));
			rss.setD_anno(rs.getString("anno"));
			rss.setD_volume(rs.getString("volume"));
			rss.setD_numero(rs.getString("numero"));
			rss.setD_sottonumero(rs.getString("sottonumero"));
			rss.setD_comune(rs.getString("comune"));
			rss.setD_progressivo(rs.getString("d_progressivo"));
			rss.setD_progressivo_immobile(rs.getString("d_progressivo_immobile"));
			rss.setD_progressivo_erede(rs.getString("d_progressivo_erede"));
			rss.setD_fornitura(rs.getString("fornitura"));
			rss.setD_numeratore_quota(rs.getString("d_numeratore_quota"));
			rss.setD_denominatore_quota(rs.getString("d_denominatore_quota"));
			rss.setD_agevolazione_1_casa(rs.getString("d_agevolazione_1_casa"));
			rss.setD_tipo_record(rs.getString("d_tipo_record"));

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del resultset - " + ex);
			throw ex;
		}
		return rss;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniA riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniA makeA(ResultSetSuccessioni rss) throws Exception {
		SuccessioniA succA = new SuccessioniA();
		try {
			succA.setPk_id_succa(rss.getPk_id_succa());
			succA.setA_ufficio(rss.getA_ufficio());
			succA.setA_anno(rss.getA_anno());
			succA.setA_volume(rss.getA_volume());
			succA.setA_numero(rss.getA_numero());
			succA.setA_sottonumero(rss.getA_sottonumero());
			succA.setA_comune(rss.getA_comune());
			succA.setA_progressivo(rss.getA_progressivo());
			succA.setA_tipo_dichiarazione(rss.getA_tipo_dichiarazione());
			succA.setA_data_apertura(rss.getA_data_apertura());
			succA.setA_cf_defunto(rss.getA_cf_defunto());
			succA.setA_cognome_defunto(rss.getA_cognome_defunto());
			succA.setA_nome_defunto(rss.getA_nome_defunto());
			succA.setA_sesso(rss.getA_sesso());
			succA.setA_citta_nascita(rss.getA_citta_nascita());
			succA.setA_prov_nascita(rss.getA_prov_nascita());
			succA.setA_data_nascita(rss.getA_data_nascita());
			succA.setA_citta_residenza(rss.getA_citta_residenza());
			succA.setA_prov_residenza(rss.getA_prov_residenza());
			succA.setA_indirizzo_residenza(rss.getA_indirizzo_residenza());
			succA.setA_fornitura(rss.getA_fornitura());
		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniA - " + ex);
			throw ex;
		}
		return succA;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniB riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniB makeB(ResultSetSuccessioni rss) throws Exception {
		SuccessioniB succB = new SuccessioniB();
		try {
			succB.setPk_id_succb(rss.getPk_id_succb());
			succB.setB_ufficio(rss.getB_ufficio());
			succB.setB_anno(rss.getB_anno());
			succB.setB_volume(rss.getB_volume());
			succB.setB_numero(rss.getB_numero());
			succB.setB_sottonumero(rss.getB_sottonumero());
			succB.setB_comune(rss.getB_comune());
			succB.setB_progressivo(rss.getB_progressivo());
			succB.setB_progressivo_erede(rss.getB_progressivo_erede());
			succB.setB_cf_erede(rss.getB_cf_erede());
			succB.setB_denominazione(rss.getB_denominazione());
			succB.setB_sesso(rss.getB_sesso());
			succB.setB_data_nascita(rss.getB_data_nascita());
			succB.setB_citta_nascita(rss.getB_citta_nascita());
			succB.setB_prov_nascita(rss.getB_prov_nascita());
			succB.setB_citta_residenza(rss.getB_citta_residenza());
			succB.setB_prov_residenza(rss.getB_prov_residenza());
			succB.setB_indirizzo_residenza(rss.getB_indirizzo_residenza());
			succB.setB_fornitura(rss.getB_fornitura());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniB - " + ex);
			throw ex;
		}
		return succB;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniC riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniC makeC(ResultSetSuccessioni rss) throws Exception {
		SuccessioniC succC = new SuccessioniC();
		try {
			succC.setC_ufficio(rss.getC_ufficio());
			succC.setC_anno(rss.getC_anno());
			succC.setC_volume(rss.getC_volume());
			succC.setC_numero(rss.getC_numero());
			succC.setC_sottonumero(rss.getC_sottonumero());
			succC.setC_comune(rss.getC_comune());
			succC.setC_progressivo(rss.getC_progressivo());
			succC.setC_progressivo_immobile(rss.getC_progressivo_immobile());
			succC.setC_fornitura(rss.getC_fornitura());
			succC.setC_numeratore_quota_def(rss.getC_numeratore_quota_def());
			succC.setC_denominatore_quota_def(rss.getC_denominatore_quota_def());
			succC.setC_diritto(rss.getC_diritto());
			succC.setC_catasto(rss.getC_catasto());
			succC.setC_tipo_dati(rss.getC_tipo_dati());
			succC.setC_foglio(rss.getC_foglio());
			succC.setC_particella1(rss.getC_particella1());
			succC.setC_particella2(rss.getC_particella2());
			succC.setC_subalterno1(rss.getC_subalterno1());
			succC.setC_subalterno2(rss.getC_subalterno2());
			succC.setC_denuncia1(rss.getC_denuncia1());
			succC.setC_anno_denuncia(rss.getC_anno_denuncia());
			succC.setC_natura(rss.getC_natura());
			succC.setC_superficie_ettari(rss.getC_superficie_ettari());
			succC.setC_superficie_mq(rss.getC_superficie_mq());
			succC.setC_vani(rss.getC_vani());
			succC.setC_indirizzo_immobile(rss.getC_indirizzo_immobile());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniC - " + ex);
			throw ex;
		}
		return succC;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniD riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniD makeD(ResultSetSuccessioni rss) throws Exception {
		SuccessioniD succD = new SuccessioniD();
		try {
			succD.setD_ufficio(rss.getC_ufficio());
			succD.setD_anno(rss.getC_anno());
			succD.setD_volume(rss.getC_volume());
			succD.setD_numero(rss.getC_numero());
			succD.setD_sottonumero(rss.getC_sottonumero());
			succD.setD_comune(rss.getC_comune());
			succD.setD_progressivo(rss.getC_progressivo());
			succD.setD_progressivo_immobile(rss.getD_progressivo_immobile());
			succD.setD_progressivo_erede(rss.getD_progressivo_erede());
			succD.setD_fornitura(rss.getC_fornitura());
			succD.setD_numeratore_quota(rss.getD_numeratore_quota());
			succD.setD_denominatore_quota(rss.getD_denominatore_quota());
			succD.setD_agevolazione_1_casa(rss.getD_agevolazione_1_casa());
			succD.setD_tipo_record(rss.getD_tipo_record());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniD - " + ex);
			throw ex;
		}
		return succD;
	}

	/**
	 * Dato un oggetto Date ritorna una stringa che la rappresenta nel formato DDMMYYYY
	 * 
	 * @param dataToParse
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToDDMMYYYY(Date dataToParse) throws Exception {
		if (dataToParse != null) {
			// return parseStringDateToDDMMYYYY(dateformatDB.format(dataToParse));
			return dateformatOutput.format(dataToParse);
		} else {
			return null;
		}
	}

	/**
	 * Dato un oggetto Date ritorna una stringa che la rappresenta nel formato YYYYMMDD
	 * 
	 * @param dataToParse
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToYYYYMMDD(Date dataToParse) throws Exception {
		if (dataToParse != null) {
			return dateformatExport.format(dataToParse);
		} else {
			return null;
		}
	}

	/**
	 * Questo metodo allinea a sinsitra la stringa passata, aggiugendo spazi " " alla fine, portando le dimensioni della stringa di output a quelle richieste, a meno che il parametro preserveSize non sia true, in tal caso infatti se la dimensione della stringa supera numSpace, la dimensione viene lasciata inalterata
	 * 
	 * @param numSpace
	 *           int
	 * @param input
	 *           String
	 * @param preserveSize
	 *           boolean
	 * @param defaultValue
	 *           String
	 * @return String
	 */
	public static String allineaSx(int numSpace, String input, boolean preserveSize, String defaultValue) {
		if (input == null) {
			return defaultValue;
		} else if (preserveSize && input.length() >= numSpace) {
			return input;
		} else {
			return allineaSx(numSpace, input);
		}
	}

	/**
	 * Questo metodo allinea a sinistra la stringa passata, aggiugendo spazi " " alla fine, portando le dimensioni della stringa di output a quelle richieste
	 * 
	 * @param numSpace
	 *           int
	 * @param input
	 *           String
	 * @return String
	 */
	public static String allineaSx(int numSpace, String input) {
		String output = "";
		input = (input != null) ? input : "";
		if (input.length() > numSpace) {
			return input.substring(0, numSpace);
		} else if (input.length() == numSpace) {
			return input;
		} else {
			output = input;
			for (int i = 0; i < numSpace - input.length(); i++) {
				output += " ";
			}
		}
		return output;
	}

	/**
	 * Questo metodo allinea a destra la stringa passata, aggiugendo 0 all'inizio, portando le dimensioni della stringa di output a quelle richieste, a meno che il parametro preserveSize non sia true, in tal caso infatti se la dimensione della stringa supera numSpace, la dimensione viene lasciatra inalterata
	 * 
	 * @param int
	 *           numSpace Dimensione per il padding
	 * @param String
	 *           input Stringa in input
	 * @param boolean
	 *           preserveSize
	 * @param String
	 *           defaultValue Valore di default ritornato in caso di input nullo
	 * @return String stringa formattata
	 */
	public static String allineaDxZero(int numSpace, String input, boolean preserveSize, String defaultValue) {

		if (input == null) {
			return defaultValue;
		} else if (preserveSize && input.length() >= numSpace) {
			return input;
		} else {
			return allineaDxZero(numSpace, input);
		}
	}

	/**
	 * Questo metodo allinea a destra la stringa passata, aggiugendo 0 all'inizio, portando le dimensioni della stringa di output a quelle richieste
	 * 
	 * @param numSpace
	 * @param input
	 * @return
	 */
	public static String allineaDxZero(int numSpace, String input) {
		String temp = "";
		input = (input != null) ? input : "";
		if (input.length() > numSpace) {
			return input.substring(0, numSpace);
		} else if (input.length() == numSpace) {
			return input;
		} else {
			for (int i = 0; i < numSpace - input.length(); i++) {
				temp += "0";
			}
			temp += input;
		}
		return temp;
	}

	/**
	 * Questo metodo allinea a sinistra la stringa passata,aggiugendo 0, portando le dimensioni della stringa di output a quelle richieste
	 * 
	 * @param int
	 *           lunghezza stringa voluta
	 * @param String
	 *           stringa in input
	 * @return String stringa formattata
	 */

	public static String allineaSxZero(int numSpace, String input) {

		String output = "";

		if (input.length() > numSpace) {
			return input.substring(0, numSpace);
		} else if (input.length() == numSpace) {
			return input;
		} else {
			output = input;
			for (int i = 0; i < numSpace - input.length(); i++) {
				output += "0";
			}
		}

		return output;
	}

}
