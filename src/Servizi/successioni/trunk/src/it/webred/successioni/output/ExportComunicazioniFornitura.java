package it.webred.successioni.output;

import it.webred.mui.MuiException;
import it.webred.mui.consolidation.DapManager;
import it.webred.mui.inputdb.MuiFornituraDB2TXTTransformer;
import it.webred.successioni.model.Comunicazione;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import net.skillbill.logging.Logger;

public class ExportComunicazioniFornitura extends HttpServlet {

	static final String A_CAPO = "\n";
	static final String SEPARATORE = ";";
	static final String NON_VALORIZZATO = "";
	static final String DA_DEFINIRE = "";
	static final String SEPARATORE_NON_VALORIZZATO = ";";

	/**
	 * @todo Cosa si deve mettere nei campi "Valore impostato da ricerca a catasto"?
	 */
	static final String DA_RICERCA_A_CATASTO = "";
	static final String DA_RICERCA_A_CATASTO_NUM = "0";
	static final DateFormat dateformatOutput = new SimpleDateFormat("ddMMyyyy");
	static final DateFormat dateformatDB = new SimpleDateFormat("yyyy-MM-dd");
	static final DateFormat dateformatExport = new SimpleDateFormat("yyyyMMdd");

	private static final String SQL_GET_DATI_CONIUGATO = "SELECT cd.iid_soggetto, cd.ID_NOTA, dtit.SC_FLAG_TIPO_TITOL_NOTA, dtit.SF_FLAG_TIPO_TITOL_NOTA, cd.CODICE_FISCALE,"
			+ " NVL(cc.COGNOME, ds.COGNOME) COGNOME, NVL(cc.NOME, ds.NOME) NOME, NVL(TO_CHAR(cc.DATA_NASCITA, 'YYYYMMDD'), TO_CHAR(TO_DATE(ds.DATA_NASCITA, 'DDMMYYYY'), 'YYYYMMDD')) DATA_NASCITA, DECODE(NVL(cc.SESSO, ds.SESSO),'1','M','2','F') SESSO, NVL(cc.COMUNE_NASCITA, ds.LUOGO_NASCITA) COMUNE_NASCITA, cc.PROVINCIA_NASCITA,"
			+ " NVL(cc.INDIRIZZO, dis.INDIRIZZO) INDIRIZZO_RESIDENZA, NVL(cc.COMUNE, dis.COMUNE) COMUNE, NVL(cc.PROVINCIA, dis.PROVINCIA) PROVINCIA, dnota.ANNO_NOTA, DECODE(dtit.TIPO_SOGGETTO, 'P', 'F', dtit.TIPO_SOGGETTO) TIPO_SOGGETTO, DECODE(dtit.TIPOLOGIA_IMMOBILE, 'F', '3', 'T', '1') TIPOLOGIA_IMMOBILE, NVL(co.INDIRIZZO, NVL(dfinfo.C_INDIRIZZO, dfinfo.T_INDIRIZZO)) INDIRIZZO,"
			+ " TO_NUMBER(NVL(co.FOGLIO, dfiden.FOGLIO)) FOGLIO, NVL(co.PARTICELLA, dfiden.NUMERO) PARTICELLA, TO_NUMBER(NVL(co.SUBALTERNO, dfiden.SUBALTERNO)) SUBALTERNO, NVL(co.NUMERO_PROTOCOLLO, dfiden.NUMERO_PROTOCOLLO) NUMERO_PROTOCOLLO, dfiden.ANNO ANNO,"
			+ " NVL(co.CATEGORIA, dfinfo.CATEGORIA) CATEGORIA, NVL(co.CLASSE, dfinfo.CLASSE) CLASSE, NVL(co.PERCENTUALE_POSSESSO, dtit.QUOTA) PERCENTUALE_POSSESSO, dtit.SC_QUOTA_NUMERATORE, dtit.SC_QUOTA_DENOMINATORE, dtit.SF_QUOTA_NUMERATORE, dtit.SF_QUOTA_DENOMINATORE,"
			+ " dtit.SF_CODICE_DIRITTO, dtit.SC_CODICE_DIRITTO, DECODE(co.CODICE_VARIAZIONE, 'C1', 'C', 'A1', 'A', 'V') CODICE_VARIAZIONE, TO_CHAR(dnota.DATA_VALIDITA_ATTO_DATE, 'YYYYMMDD') DATA_VALIDITA_ATTO_DATE, dfinfo.FLAG_GRAFFATO FLAG_PERTINENZA, dfinfo.T_CIVICO1 NR_CIVICO, dfinfo.CODICE_VIA, dfinfo.RENDITA_EURO REDDITO_EURO,"
			+ " DECODE(cd.REGDAP_NO_DTRES, 'Y', '001', DECODE(cd.REGDAP_DTRES_OLTRE90, 'Y', '003', DECODE(cd.REGDAP_PERCPOSSTOT_ERR, 'Y', '005', DECODE(cd.REGDAP_SGTPOSS_PIUIMM_SI, 'Y', '011', DECODE(cd.REGDAP_SGTPOSS_PIUIMM, 'Y', '007', DECODE(cd.REGDAP_NO_DTCOMPFAM, 'Y', '017', '0')))))) CODICE_DAP,"
			+ " TO_CHAR(co.DECORRENZA, 'YYYYMMDD') DECORRENZA, NVL(co.CONTITOLARI_ABIT_PRINCIPALE, '1') CONTITOLARI_ABIT_PRINCIPALE, DECODE(co.FLAG_ABIT_PRINCIPALE, 'Y', 'S', 'N', 'N', '') FLAG_ABIT_PRINCIPALE, co.MEMBRI_NUCLEO_FAMILIARE, dnota.NUMERO_NOTA_TRAS, dtit.SC_CODICE_DIRITTO, dtit.SF_CODICE_DIRITTO, NVL(dnota.TIPO_NOTA, '') TIPO_NOTA" + " FROM SUC_CONS_DAP cd, SUC_DUP_SOGGETTI ds, SUC_DUP_INDIRIZZI_SOG dis, SUC_DUP_NOTA_TRAS dnota,"
			+ " SUC_DUP_TITOLARITA dtit, SUC_DUP_FABBRICATI_INFO dfinfo, SUC_DUP_FABBRICATI_IDENTIFICA dfiden, SUC_DUP_TERRENI_INFO dti, SUC_CONS_COMUNICAZIONE cc, SUC_CONS_OGGETTO co" + " WHERE cd.iid_fornitura = ?" + " AND ds.iid = cd.iid_soggetto" + " AND dtit.iid = cd.iid_titolarita" + " AND dnota.iid = cd.iid_nota" + " AND ds.iid = dis.iid_soggetto (+)" + " AND dtit.iid_nota = dti.iid_nota (+)"
			+ " AND dtit.id_immobile = dti.id_immobile (+)" + " AND cd.iid_fornitura = cc.iid_fornitura (+)" + " AND cd.iid_nota = cc.iid_nota (+)" + " AND cd.iid_soggetto = cc.iid_soggetto (+)" + " AND dtit.iid_fornitura = co.iid_fornitura (+)" + " AND dtit.iid = co.iid_titolarita (+)" + " AND dtit.iid_fabbricatiinfo = dfinfo.iid (+)" + " AND dfinfo.iid  = dfiden.iid_fabbricatiinfo (+)"
			+ " AND EXISTS " 
			+ "	(SELECT 1 FROM MVW_SUC_STO_STACIV WHERE CODICE_FISCALE = CC.CODICE_FISCALE "
			+ "	AND PER_STATO_CIVILE = 'C' "
			+ "	) "
			+ " ORDER BY TO_NUMBER(cd.id_nota), cd.iid_soggetto";

	private static final String SQL_GET_DATI_CELIBE = "SELECT cd.iid_soggetto, cd.ID_NOTA, dtit.SC_FLAG_TIPO_TITOL_NOTA, dtit.SF_FLAG_TIPO_TITOL_NOTA, cd.CODICE_FISCALE,"
		+ " NVL(cc.COGNOME, ds.COGNOME) COGNOME, NVL(cc.NOME, ds.NOME) NOME, NVL(TO_CHAR(cc.DATA_NASCITA, 'YYYYMMDD'), TO_CHAR(TO_DATE(ds.DATA_NASCITA, 'DDMMYYYY'), 'YYYYMMDD')) DATA_NASCITA, DECODE(NVL(cc.SESSO, ds.SESSO),'1','M','2','F') SESSO, NVL(cc.COMUNE_NASCITA, ds.LUOGO_NASCITA) COMUNE_NASCITA, cc.PROVINCIA_NASCITA,"
		+ " NVL(cc.INDIRIZZO, dis.INDIRIZZO) INDIRIZZO_RESIDENZA, NVL(cc.COMUNE, dis.COMUNE) COMUNE, NVL(cc.PROVINCIA, dis.PROVINCIA) PROVINCIA, dnota.ANNO_NOTA, DECODE(dtit.TIPO_SOGGETTO, 'P', 'F', dtit.TIPO_SOGGETTO) TIPO_SOGGETTO, DECODE(dtit.TIPOLOGIA_IMMOBILE, 'F', '3', 'T', '1') TIPOLOGIA_IMMOBILE, NVL(co.INDIRIZZO, NVL(dfinfo.C_INDIRIZZO, dfinfo.T_INDIRIZZO)) INDIRIZZO,"
		+ " TO_NUMBER(NVL(co.FOGLIO, dfiden.FOGLIO)) FOGLIO, NVL(co.PARTICELLA, dfiden.NUMERO) PARTICELLA, TO_NUMBER(NVL(co.SUBALTERNO, dfiden.SUBALTERNO)) SUBALTERNO, NVL(co.NUMERO_PROTOCOLLO, dfiden.NUMERO_PROTOCOLLO) NUMERO_PROTOCOLLO, dfiden.ANNO ANNO,"
		+ " NVL(co.CATEGORIA, dfinfo.CATEGORIA) CATEGORIA, NVL(co.CLASSE, dfinfo.CLASSE) CLASSE, NVL(co.PERCENTUALE_POSSESSO, dtit.QUOTA) PERCENTUALE_POSSESSO, dtit.SC_QUOTA_NUMERATORE, dtit.SC_QUOTA_DENOMINATORE, dtit.SF_QUOTA_NUMERATORE, dtit.SF_QUOTA_DENOMINATORE,"
		+ " dtit.SF_CODICE_DIRITTO, dtit.SC_CODICE_DIRITTO, DECODE(co.CODICE_VARIAZIONE, 'C1', 'C', 'A1', 'A', 'V') CODICE_VARIAZIONE, TO_CHAR(dnota.DATA_VALIDITA_ATTO_DATE, 'YYYYMMDD') DATA_VALIDITA_ATTO_DATE, dfinfo.FLAG_GRAFFATO FLAG_PERTINENZA, dfinfo.T_CIVICO1 NR_CIVICO, dfinfo.CODICE_VIA, dfinfo.RENDITA_EURO REDDITO_EURO,"
		+ " DECODE(cd.REGDAP_NO_DTRES, 'Y', '001', DECODE(cd.REGDAP_DTRES_OLTRE90, 'Y', '003', DECODE(cd.REGDAP_PERCPOSSTOT_ERR, 'Y', '005', DECODE(cd.REGDAP_SGTPOSS_PIUIMM_SI, 'Y', '011', DECODE(cd.REGDAP_SGTPOSS_PIUIMM, 'Y', '007', DECODE(cd.REGDAP_NO_DTCOMPFAM, 'Y', '017', '0')))))) CODICE_DAP,"
		+ " TO_CHAR(co.DECORRENZA, 'YYYYMMDD') DECORRENZA, NVL(co.CONTITOLARI_ABIT_PRINCIPALE, '1') CONTITOLARI_ABIT_PRINCIPALE, DECODE(co.FLAG_ABIT_PRINCIPALE, 'Y', 'S', 'N', 'N', '') FLAG_ABIT_PRINCIPALE, co.MEMBRI_NUCLEO_FAMILIARE, dnota.NUMERO_NOTA_TRAS , dtit.SC_CODICE_DIRITTO, dtit.SF_CODICE_DIRITTO, NVL(dnota.TIPO_NOTA, '') TIPO_NOTA" + " FROM SUC_CONS_DAP cd, SUC_DUP_SOGGETTI ds, SUC_DUP_INDIRIZZI_SOG dis, SUC_DUP_NOTA_TRAS dnota,"
		+ " SUC_DUP_TITOLARITA dtit, SUC_DUP_FABBRICATI_INFO dfinfo, SUC_DUP_FABBRICATI_IDENTIFICA dfiden, SUC_DUP_TERRENI_INFO dti, SUC_CONS_COMUNICAZIONE cc, SUC_CONS_OGGETTO co" + " WHERE cd.iid_fornitura = ?" + " AND ds.iid = cd.iid_soggetto" + " AND dtit.iid = cd.iid_titolarita" + " AND dnota.iid = cd.iid_nota" + " AND ds.iid = dis.iid_soggetto (+)" + " AND dtit.iid_nota = dti.iid_nota (+)"
		+ " AND dtit.id_immobile = dti.id_immobile (+)" + " AND cd.iid_fornitura = cc.iid_fornitura (+)" + " AND cd.iid_nota = cc.iid_nota (+)" + " AND cd.iid_soggetto = cc.iid_soggetto (+)" + " AND dtit.iid_fornitura = co.iid_fornitura (+)" + " AND dtit.iid = co.iid_titolarita (+)" + " AND dtit.iid_fabbricatiinfo = dfinfo.iid (+)" + " AND dfinfo.iid  = dfiden.iid_fabbricatiinfo (+)"
		+ " AND NOT EXISTS " 
		+ "	(SELECT 1 FROM MVW_SUC_STO_STACIV WHERE CODICE_FISCALE = CC.CODICE_FISCALE "
		+ "	AND PER_STATO_CIVILE = 'C' "
		+ "	) "
		+ " ORDER BY TO_NUMBER(cd.id_nota), cd.iid_soggetto";
	
	private static final String SQL_GET_DATI_CONIUGATO_AFTER2006 = "SELECT cd.iid_soggetto, cd.ID_NOTA, dtit.SC_FLAG_TIPO_TITOL_NOTA, dtit.SF_FLAG_TIPO_TITOL_NOTA, cd.CODICE_FISCALE,"
		+ " NVL(cc.COGNOME, ds.COGNOME) COGNOME, NVL(cc.NOME, ds.NOME) NOME, NVL(TO_CHAR(cc.DATA_NASCITA, 'YYYYMMDD'), TO_CHAR(TO_DATE(ds.DATA_NASCITA, 'DDMMYYYY'), 'YYYYMMDD')) DATA_NASCITA, DECODE(NVL(cc.SESSO, ds.SESSO),'1','M','2','F') SESSO, NVL(cc.COMUNE_NASCITA, ds.LUOGO_NASCITA) COMUNE_NASCITA, cc.PROVINCIA_NASCITA,"
		+ " NVL(cc.INDIRIZZO, dis.INDIRIZZO) INDIRIZZO_RESIDENZA, NVL(cc.COMUNE, dis.COMUNE) COMUNE, NVL(cc.PROVINCIA, dis.PROVINCIA) PROVINCIA, dnota.ANNO_NOTA, DECODE(dtit.TIPO_SOGGETTO, 'P', 'F', dtit.TIPO_SOGGETTO) TIPO_SOGGETTO, DECODE(dtit.TIPOLOGIA_IMMOBILE, 'F', '3', 'T', '1') TIPOLOGIA_IMMOBILE, NVL(co.INDIRIZZO, NVL(dfinfo.C_INDIRIZZO, dfinfo.T_INDIRIZZO)) INDIRIZZO,"
		+ " TO_NUMBER(NVL(co.FOGLIO, dfiden.FOGLIO)) FOGLIO, NVL(co.PARTICELLA, dfiden.NUMERO) PARTICELLA, TO_NUMBER(NVL(co.SUBALTERNO, dfiden.SUBALTERNO)) SUBALTERNO, NVL(co.NUMERO_PROTOCOLLO, dfiden.NUMERO_PROTOCOLLO) NUMERO_PROTOCOLLO, dfiden.ANNO ANNO,"
		+ " NVL(co.CATEGORIA, dfinfo.CATEGORIA) CATEGORIA, NVL(co.CLASSE, dfinfo.CLASSE) CLASSE, NVL(co.PERCENTUALE_POSSESSO, dtit.QUOTA) PERCENTUALE_POSSESSO, dtit.SC_QUOTA_NUMERATORE, dtit.SC_QUOTA_DENOMINATORE, dtit.SF_QUOTA_NUMERATORE, dtit.SF_QUOTA_DENOMINATORE,"
		+ " dtit.SF_CODICE_DIRITTO, dtit.SC_CODICE_DIRITTO, DECODE(co.CODICE_VARIAZIONE, 'C1', 'C', 'A1', 'A', 'V') CODICE_VARIAZIONE, TO_CHAR(dnota.DATA_VALIDITA_ATTO_DATE, 'YYYYMMDD') DATA_VALIDITA_ATTO_DATE, dfinfo.FLAG_GRAFFATO FLAG_PERTINENZA, dfinfo.T_CIVICO1 NR_CIVICO, dfinfo.CODICE_VIA, dfinfo.RENDITA_EURO REDDITO_EURO,"
		+ " DECODE(cd.REGDAP_NO_DTRES, 'Y', '001', DECODE(cd.REGDAP_DTRES_OLTRE90, 'Y', '003', DECODE(cd.REGDAP_PERCPOSSTOT_ERR, 'Y', '005', DECODE(cd.REGDAP_SGTPOSS_PIUIMM_SI, 'Y', '011', DECODE(cd.REGDAP_SGTPOSS_PIUIMM, 'Y', '007', DECODE(cd.REGDAP_NO_DTCOMPFAM, 'Y', '017', '0')))))) CODICE_DAP,"
		+ " TO_CHAR(co.DECORRENZA, 'YYYYMMDD') DECORRENZA, NVL(co.CONTITOLARI_ABIT_PRINCIPALE, '1') CONTITOLARI_ABIT_PRINCIPALE, DECODE(co.FLAG_ABIT_PRINCIPALE, 'Y', 'S', 'N', 'N', '') FLAG_ABIT_PRINCIPALE, co.MEMBRI_NUCLEO_FAMILIARE, dnota.NUMERO_NOTA_TRAS, dtit.SC_CODICE_DIRITTO, dtit.SF_CODICE_DIRITTO, NVL(dnota.TIPO_NOTA, '') TIPO_NOTA" + " FROM SUC_CONS_DAP cd, SUC_DUP_SOGGETTI ds, SUC_DUP_INDIRIZZI_SOG dis, SUC_DUP_NOTA_TRAS dnota,"
		+ " SUC_DUP_TITOLARITA dtit, SUC_DUP_FABBRICATI_INFO dfinfo, SUC_DUP_FABBRICATI_IDENTIFICA dfiden, SUC_DUP_TERRENI_INFO dti, SUC_CONS_COMUNICAZIONE cc, SUC_CONS_OGGETTO co" + " WHERE cd.iid_fornitura = ?" + " AND ds.iid = cd.iid_soggetto" + " AND dtit.iid = cd.iid_titolarita" + " AND dnota.iid = cd.iid_nota" + " AND ds.iid = dis.iid_soggetto (+)" + " AND dtit.iid_nota = dti.iid_nota (+)"
		+ " AND dtit.id_immobile = dti.id_immobile (+)" + " AND cd.iid_fornitura = cc.iid_fornitura (+)" + " AND cd.iid_nota = cc.iid_nota (+)" + " AND cd.iid_soggetto = cc.iid_soggetto (+)" + " AND dtit.iid_fornitura = co.iid_fornitura (+)" + " AND dtit.iid = co.iid_titolarita (+)" + " AND dtit.iid_fabbricatiinfo = dfinfo.iid (+)" + " AND dfinfo.iid  = dfiden.iid_fabbricatiinfo (+)"
		+ " AND EXISTS"
		+ "	(SELECT 1 FROM SIT_D_PERSONA c WHERE c.ID_EXT IN (SELECT u.ID_EXT_D_PERSONA2 FROM SIT_D_PERSONA p, SIT_D_UNIONE u"
		+ "	WHERE p.id_ext = u.id_ext AND p.CODFISC = cc.CODICE_FISCALE AND U.DT_FINE_VAL IS NULL)"
		+ "	AND (DATA_MOR IS NULL OR DATA_MOR > (SELECT MAX(NVL(DATA_MOR,to_date('01011000','ddmmyyyy'))) FROM SIT_D_PERSONA WHERE CODFISC = cc.CODICE_FISCALE AND DT_FINE_VAL IS NULL))"
		+ "	) "
		+ " ORDER BY TO_NUMBER(cd.id_nota), cd.iid_soggetto";

	private static final String SQL_GET_DATI_CELIBE_AFTER2006 = "SELECT cd.iid_soggetto, cd.ID_NOTA, dtit.SC_FLAG_TIPO_TITOL_NOTA, dtit.SF_FLAG_TIPO_TITOL_NOTA, cd.CODICE_FISCALE,"
		+ " NVL(cc.COGNOME, ds.COGNOME) COGNOME, NVL(cc.NOME, ds.NOME) NOME, NVL(TO_CHAR(cc.DATA_NASCITA, 'YYYYMMDD'), TO_CHAR(TO_DATE(ds.DATA_NASCITA, 'DDMMYYYY'), 'YYYYMMDD')) DATA_NASCITA, DECODE(NVL(cc.SESSO, ds.SESSO),'1','M','2','F') SESSO, NVL(cc.COMUNE_NASCITA, ds.LUOGO_NASCITA) COMUNE_NASCITA, cc.PROVINCIA_NASCITA,"
		+ " NVL(cc.INDIRIZZO, dis.INDIRIZZO) INDIRIZZO_RESIDENZA, NVL(cc.COMUNE, dis.COMUNE) COMUNE, NVL(cc.PROVINCIA, dis.PROVINCIA) PROVINCIA, dnota.ANNO_NOTA, DECODE(dtit.TIPO_SOGGETTO, 'P', 'F', dtit.TIPO_SOGGETTO) TIPO_SOGGETTO, DECODE(dtit.TIPOLOGIA_IMMOBILE, 'F', '3', 'T', '1') TIPOLOGIA_IMMOBILE, NVL(co.INDIRIZZO, NVL(dfinfo.C_INDIRIZZO, dfinfo.T_INDIRIZZO)) INDIRIZZO,"
		+ " TO_NUMBER(NVL(co.FOGLIO, dfiden.FOGLIO)) FOGLIO, NVL(co.PARTICELLA, dfiden.NUMERO) PARTICELLA, TO_NUMBER(NVL(co.SUBALTERNO, dfiden.SUBALTERNO)) SUBALTERNO, NVL(co.NUMERO_PROTOCOLLO, dfiden.NUMERO_PROTOCOLLO) NUMERO_PROTOCOLLO, dfiden.ANNO ANNO,"
		+ " NVL(co.CATEGORIA, dfinfo.CATEGORIA) CATEGORIA, NVL(co.CLASSE, dfinfo.CLASSE) CLASSE, NVL(co.PERCENTUALE_POSSESSO, dtit.QUOTA) PERCENTUALE_POSSESSO, dtit.SC_QUOTA_NUMERATORE, dtit.SC_QUOTA_DENOMINATORE, dtit.SF_QUOTA_NUMERATORE, dtit.SF_QUOTA_DENOMINATORE,"
		+ " dtit.SF_CODICE_DIRITTO, dtit.SC_CODICE_DIRITTO, DECODE(co.CODICE_VARIAZIONE, 'C1', 'C', 'A1', 'A', 'V') CODICE_VARIAZIONE, TO_CHAR(dnota.DATA_VALIDITA_ATTO_DATE, 'YYYYMMDD') DATA_VALIDITA_ATTO_DATE, dfinfo.FLAG_GRAFFATO FLAG_PERTINENZA, dfinfo.T_CIVICO1 NR_CIVICO, dfinfo.CODICE_VIA, dfinfo.RENDITA_EURO REDDITO_EURO,"
		+ " DECODE(cd.REGDAP_NO_DTRES, 'Y', '001', DECODE(cd.REGDAP_DTRES_OLTRE90, 'Y', '003', DECODE(cd.REGDAP_PERCPOSSTOT_ERR, 'Y', '005', DECODE(cd.REGDAP_SGTPOSS_PIUIMM_SI, 'Y', '011', DECODE(cd.REGDAP_SGTPOSS_PIUIMM, 'Y', '007', DECODE(cd.REGDAP_NO_DTCOMPFAM, 'Y', '017', '0')))))) CODICE_DAP,"
		+ " TO_CHAR(co.DECORRENZA, 'YYYYMMDD') DECORRENZA, NVL(co.CONTITOLARI_ABIT_PRINCIPALE, '1') CONTITOLARI_ABIT_PRINCIPALE, DECODE(co.FLAG_ABIT_PRINCIPALE, 'Y', 'S', 'N', 'N', '') FLAG_ABIT_PRINCIPALE, co.MEMBRI_NUCLEO_FAMILIARE, dnota.NUMERO_NOTA_TRAS , dtit.SC_CODICE_DIRITTO, dtit.SF_CODICE_DIRITTO, NVL(dnota.TIPO_NOTA, '') TIPO_NOTA" + " FROM SUC_CONS_DAP cd, SUC_DUP_SOGGETTI ds, SUC_DUP_INDIRIZZI_SOG dis, SUC_DUP_NOTA_TRAS dnota,"
		+ " SUC_DUP_TITOLARITA dtit, SUC_DUP_FABBRICATI_INFO dfinfo, SUC_DUP_FABBRICATI_IDENTIFICA dfiden, SUC_DUP_TERRENI_INFO dti, SUC_CONS_COMUNICAZIONE cc, SUC_CONS_OGGETTO co" + " WHERE cd.iid_fornitura = ?" + " AND ds.iid = cd.iid_soggetto" + " AND dtit.iid = cd.iid_titolarita" + " AND dnota.iid = cd.iid_nota" + " AND ds.iid = dis.iid_soggetto (+)" + " AND dtit.iid_nota = dti.iid_nota (+)"
		+ " AND dtit.id_immobile = dti.id_immobile (+)" + " AND cd.iid_fornitura = cc.iid_fornitura (+)" + " AND cd.iid_nota = cc.iid_nota (+)" + " AND cd.iid_soggetto = cc.iid_soggetto (+)" + " AND dtit.iid_fornitura = co.iid_fornitura (+)" + " AND dtit.iid = co.iid_titolarita (+)" + " AND dtit.iid_fabbricatiinfo = dfinfo.iid (+)" + " AND dfinfo.iid  = dfiden.iid_fabbricatiinfo (+)"
		+ " AND NOT EXISTS"
		+ "	(SELECT 1 FROM SIT_D_PERSONA c WHERE c.ID_EXT IN (SELECT u.ID_EXT_D_PERSONA2 FROM SIT_D_PERSONA p, SIT_D_UNIONE u"
		+ "	WHERE p.id_ext = u.id_ext AND p.CODFISC = cc.CODICE_FISCALE AND U.DT_FINE_VAL IS NULL)"
		+ "	AND (DATA_MOR IS NULL OR DATA_MOR > (SELECT MAX(NVL(DATA_MOR,to_date('01011000','ddmmyyyy'))) FROM SIT_D_PERSONA WHERE CODFISC = cc.CODICE_FISCALE AND DT_FINE_VAL IS NULL))"
		+ "	) "
		+ " ORDER BY TO_NUMBER(cd.id_nota), cd.iid_soggetto";

	public StringBuffer readDataFromDB(String idFornitura, String dataEsportazione, String coniugato, Connection conn, boolean isAfter2006) throws ServletException, IOException, it.webred.successioni.exceptions.MuiException {

		StringBuffer sbOut = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement _getStmt = null;
		java.sql.Statement st =null;
		
		try {

			boolean richiestiConiugati = "S".equalsIgnoreCase(coniugato);
			
			Hashtable idGrouping = new Hashtable();
			idGrouping.put("ID_NOTA_CURR", "0");
			idGrouping.put("ID_SOGG_CURR", "0");
			idGrouping.put("NR_SOGG_CURR", "0");

			st = conn.createStatement();
			st.execute("ALTER SESSION SET NLS_TERRITORY = 'ITALY'");
			
			
			if (richiestiConiugati) {
				_getStmt = conn.prepareStatement(isAfter2006 ? SQL_GET_DATI_CONIUGATO_AFTER2006 : SQL_GET_DATI_CONIUGATO);				
			} else {
				_getStmt = conn.prepareStatement(isAfter2006 ? SQL_GET_DATI_CELIBE_AFTER2006 : SQL_GET_DATI_CELIBE);
			}
			_getStmt.clearParameters();
			_getStmt.setString(1, idFornitura);
			Logger.log().info(this.getClass().getName(), "Query eseguita: " + (richiestiConiugati ? (isAfter2006 ? SQL_GET_DATI_CONIUGATO_AFTER2006 : SQL_GET_DATI_CONIUGATO) : (isAfter2006 ? SQL_GET_DATI_CELIBE_AFTER2006 : SQL_GET_DATI_CELIBE)));
			Logger.log().info(this.getClass().getName(), "Param[1]: " + idFornitura);

			rs = _getStmt.executeQuery();

			Comunicazione comunicazione;
			while (rs.next()) {   
				// MARCORIC 5-10-2008
				// ESPORTO SOLO I RECORD CON DIRITTO COME SOTTO:
				if (
						(rs.getString("SC_CODICE_DIRITTO") !=null && ( rs.getString("SC_CODICE_DIRITTO").equals("1") 
						|| rs.getString("SC_CODICE_DIRITTO").equals("3")
						|| rs.getString("SC_CODICE_DIRITTO").equals("5")
						|| rs.getString("SC_CODICE_DIRITTO").equals("6")
						|| rs.getString("SC_CODICE_DIRITTO").equals("7")
						|| rs.getString("SC_CODICE_DIRITTO").equals("8")
						)) 
				|| 
						(rs.getString("SF_CODICE_DIRITTO") !=null && ( rs.getString("SF_CODICE_DIRITTO").equals("1") 
						|| rs.getString("SF_CODICE_DIRITTO").equals("3")
						|| rs.getString("SF_CODICE_DIRITTO").equals("5")
						|| rs.getString("SF_CODICE_DIRITTO").equals("6")
						|| rs.getString("SF_CODICE_DIRITTO").equals("7")
						|| rs.getString("SF_CODICE_DIRITTO").equals("8")
						))		
				) {
					comunicazione = make(rs);
					writeSingleRecord(sbOut, comunicazione, dataEsportazione, idGrouping);
				}
			}

			return sbOut;

			// out.flush();
			// out.close();
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), e.getMessage());
			e.printStackTrace();
			throw new it.webred.successioni.exceptions.MuiException("Errore dalla procedura di lettura dei dati");
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if (st!=null)
					st.close();
				if (_getStmt!=null)
					_getStmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void appendConSepa(StringBuffer sbOut, String value) {
		if (NON_VALORIZZATO.equalsIgnoreCase(value)) {
			sbOut.append(value);
			sbOut.append(SEPARATORE_NON_VALORIZZATO);
		} else {
			// Per evitare confusione con il carattere utilizzato come separatore
			// vado a sostituire
			// la sua eventuale presenza all'interno della stringa
			if (value != null) {
				sbOut.append(value.replaceAll(SEPARATORE, " "));
			} else {
				sbOut.append("");
			}
			sbOut.append(SEPARATORE);
		}
	}

	private void appendConSepa(StringBuffer sbOut, String value, int nrVolte) {
		for (int i = 0; i < nrVolte; i++) {
			appendConSepa(sbOut, value);
		}
	}

	private void appendConAcapo(StringBuffer sbOut, String value) {
		sbOut.append(value);
		sbOut.append(A_CAPO);
	}

	/**
	 * Data una stringa e un massimo numero di caratteri ritorna quella stringa se la sua lunghezza è minore o eguale
	 * a maxChar, in caso contrario ritorna gli ultimi maxChar caratteri
	 * @param value
	 * @param maxChar
	 * @return
	 */
	private String truncateLeft(String value, int maxChar) {
		String result = value;
		if (value != null && value.length() > maxChar) {
			int lunghezza = value.length();
			result = value.substring(lunghezza-maxChar, lunghezza);
		}
		return result;
	}

	private String truncateRight(String value, int maxChar) {
		String result = value;
		if (value != null && value.length() > maxChar) {
			result = value.substring(0, maxChar);
		}
		return result;
	}

	/**
	 * Dato un oggetto Comunicazione scrive nel buffer tutte le informazioni contenute in tale oggetto
	 * 
	 * @param sbOut
	 * @param comunicazione
	 * @throws MuiException
	 * @throws Exception
	 */
	private void writeSingleRecord(StringBuffer sbOut, Comunicazione comunicazione, String dataEsportazione, Hashtable idGrouping) throws MuiException, Exception {

		int idNotaCurr = Integer.parseInt((String) idGrouping.get("ID_NOTA_CURR"));
		int idSoggCurr = Integer.parseInt((String) idGrouping.get("ID_SOGG_CURR"));
		int nrSoggCurr = Integer.parseInt((String) idGrouping.get("NR_SOGG_CURR"));
		int idNotaNew = Integer.parseInt(comunicazione.getId_nota());
		int idSoggNew = Integer.parseInt(comunicazione.getIid_soggetto());

		int nrSoggNew = nrSoggCurr;
		if (idNotaCurr != idNotaNew) {
			// CAMBIO TUTTO
			idGrouping.put("ID_NOTA_CURR", "" + idNotaNew);
			idGrouping.put("ID_SOGG_CURR", "" + idSoggNew);
			nrSoggNew = nrSoggCurr + 1;
		} else if (idSoggCurr != idSoggNew) {
			// CAMBIO SOLO L'ID_SOGGETTO
			idGrouping.put("ID_SOGG_CURR", "" + idSoggNew);
			nrSoggNew = nrSoggCurr + 1;
		}
		idGrouping.put("NR_SOGG_CURR", "" + nrSoggNew);

		// appendConSepa(sbOut, "" + idNotaNew);
		appendConSepa(sbOut, "" + nrSoggNew);
		// appendConSepa(sbOut, dataEsportazione);
		appendConSepa(sbOut, comunicazione.getData_validita_atto_date());
		appendConSepa(sbOut, comunicazione.getCodice_fiscale());
		appendConSepa(sbOut, NON_VALORIZZATO, 2);
		appendConSepa(sbOut, comunicazione.getCognome());
		appendConSepa(sbOut, truncateRight(comunicazione.getNome(), 20));
		appendConSepa(sbOut, comunicazione.getData_nascita());
		appendConSepa(sbOut, comunicazione.getSesso());
		appendConSepa(sbOut, comunicazione.getComune_nascita());
		appendConSepa(sbOut, comunicazione.getProvincia_nascita());
		appendConSepa(sbOut, comunicazione.getIndirizzo_residenza());
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getComune());
		appendConSepa(sbOut, comunicazione.getProvincia());
		appendConSepa(sbOut, NON_VALORIZZATO, 19);
		// 12-1-2009 TOLTO, VISTO CHE L'ANNO CHE HO è L'ANNO DELLA 
		// DENUNCIA A CATASTO MENTRE IN EXPORT VOGLIONO L'ANNO DI RIFERIMENTO
		// appendConSepa(sbOut, comunicazione.getAnno());
		appendConSepa(sbOut, NON_VALORIZZATO, 5);
		appendConSepa(sbOut, comunicazione.getTipo_soggetto());
		appendConSepa(sbOut, comunicazione.getTipologia_immobile());
		appendConSepa(sbOut, comunicazione.getIndirizzo());
		appendConSepa(sbOut, NON_VALORIZZATO, 2);
		appendConSepa(sbOut, comunicazione.getFoglio());
		appendConSepa(sbOut, comunicazione.getParticella());
		appendConSepa(sbOut, comunicazione.getSubalterno());
		appendConSepa(sbOut, truncateLeft(comunicazione.getNumero_protocollo(), 6));
		appendConSepa(sbOut, comunicazione.getAnno());
		appendConSepa(sbOut, comunicazione.getCategoria());
		appendConSepa(sbOut, comunicazione.getClasse());
		appendConSepa(sbOut, NON_VALORIZZATO, 5);
		appendConSepa(sbOut, DA_RICERCA_A_CATASTO_NUM);

		String percentualePossesso = comunicazione.getPercentuale_possesso();
		/*
		 * if (percentualePossesso == null || percentualePossesso.trim().length() == 0) { if ("C".equalsIgnoreCase(comunicazione.getSc_flag_tipo_titol_nota()) && comunicazione.getSc_quota_numeratore() != null && comunicazione.getSc_quota_numeratore().trim().length() > 0 && comunicazione.getSc_quota_denominatore() != null && comunicazione.getSc_quota_denominatore().trim().length() > 0) { double
		 * numeratore = Double.parseDouble(comunicazione.getSc_quota_numeratore()); double denominatore = Double.parseDouble(comunicazione.getSc_quota_denominatore()); java.math.BigDecimal quota = java.math.BigDecimal.valueOf(numeratore / denominatore / 10d); appendConSepa(sbOut, quota.toString()); } else if ("F".equalsIgnoreCase(comunicazione.getSf_flag_tipo_titol_nota()) &&
		 * comunicazione.getSf_quota_numeratore() != null && comunicazione.getSf_quota_numeratore().trim().length() > 0 && comunicazione.getSf_quota_denominatore() != null && comunicazione.getSf_quota_denominatore().trim().length() > 0) { double numeratore = Double.parseDouble(comunicazione.getSf_quota_numeratore()); double denominatore = Double.parseDouble(comunicazione.getSf_quota_denominatore());
		 * java.math.BigDecimal quota = java.math.BigDecimal.valueOf(numeratore / denominatore / 10d); appendConSepa(sbOut, quota.toString()); } else { appendConSepa(sbOut, NON_VALORIZZATO); } } else { appendConSepa(sbOut, percentualePossesso); }
		 */

		Logger.log().info(this.getClass().getName(), "PercentualePossesso prima: " + percentualePossesso);
		if (percentualePossesso != null && percentualePossesso.startsWith(".")) {
			percentualePossesso = "0" + percentualePossesso;
		}
		String percentualePossessoInCentesimi = percentualePossesso;
		
		//ELIMINATA QUESTA PARTE VISTO CHE DAVA PROBLEMI SULLE QUOTE CONDOMINIALI
		//CHE SONO REALMENTE IN CENTESIMI !!!
		/*
		try {
			BigDecimal percentuale = new BigDecimal(percentualePossesso);
			// Purtroppo a questo punto in alcuni casi la percentuale mi arriva in centesimi e in altri casi come 
			// divisa per 100, quindi prima di moltiplicare per 100 devo verificare che la percentuale che ho non
			// risulti già divisa per 100...
			if (percentuale.compareTo(new BigDecimal(1)) <= 0) {
				percentualePossessoInCentesimi = (percentuale.multiply(new BigDecimal(100))).toString();				
			}
		} catch (NumberFormatException nfe) {
			Logger.log().error(this.getClass().getName(), "Errore nella formattazione della percentuale: " + percentualePossesso);
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), "Errore nel recupero della percentuale: " + percentualePossesso + e.getMessage());
			percentualePossessoInCentesimi = "0";
		}
		 */
		Logger.log().info(this.getClass().getName(), "PercentualePossesso inserita: " + percentualePossessoInCentesimi);
		appendConSepa(sbOut, percentualePossessoInCentesimi);
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getSf_codice_diritto());
		appendConSepa(sbOut, NON_VALORIZZATO); 
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getCodice_variazione());
		appendConSepa(sbOut, comunicazione.getData_validita_atto_date());
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getFlag_pertinenza());
		appendConSepa(sbOut, NON_VALORIZZATO, 2);
		appendConSepa(sbOut, DA_DEFINIRE);

		String nrCivicoCompleto = comunicazione.getNr_civico();
		boolean nrCivicoPresente = nrCivicoCompleto != null && nrCivicoCompleto.trim().length() > 0;
		String nrCivicoParteNumerica = nrCivicoPresente ? nrCivicoCompleto : "";
		String nrCivicoParteLetterale = "";
		if (nrCivicoPresente) {
			if (nrCivicoCompleto.indexOf("/") > -1) {
				String[] civicoEdEsponente = nrCivicoCompleto.split("/");
				nrCivicoParteNumerica = civicoEdEsponente[0];
				nrCivicoParteLetterale = civicoEdEsponente[1];
			} else if (nrCivicoCompleto.indexOf(" ") > -1) {
				String[] civicoEdEsponente = nrCivicoCompleto.split(" ");
				nrCivicoParteNumerica = civicoEdEsponente[0];
				nrCivicoParteLetterale = civicoEdEsponente[1];
			} else if (nrCivicoCompleto.indexOf(",") > -1) {
				String[] civicoEdEsponente = nrCivicoCompleto.split(",");
				nrCivicoParteNumerica = civicoEdEsponente[0];
				nrCivicoParteLetterale = civicoEdEsponente[1];
			} else if (nrCivicoCompleto.indexOf(";") > -1) {
				String[] civicoEdEsponente = nrCivicoCompleto.split(";");
				nrCivicoParteNumerica = civicoEdEsponente[0];
				nrCivicoParteLetterale = civicoEdEsponente[1];
			}

			// Se la parte numerica non è un numero allora lascio vuoto il campo Nr civico e metto tutto nel campo
			// esponente civico
			try {
				Integer.parseInt(nrCivicoParteNumerica);
			} catch (NumberFormatException nfe) {
				try {
					Collection<Civico> civ = GestioneStringheVie.restituisciCivico("AAAAAAAA " + nrCivicoCompleto);
					if (!civ.isEmpty()) {
						Civico c = ((Civico)civ.toArray()[0]);
						nrCivicoParteNumerica = c.getCivico();
					}
				} catch (Exception e) {
					nrCivicoParteNumerica = "";
					nrCivicoParteLetterale = "";
				}
			}
		}
		appendConSepa(sbOut, nrCivicoParteNumerica);
		appendConSepa(sbOut, nrCivicoParteLetterale);

		appendConSepa(sbOut, DA_RICERCA_A_CATASTO, 7);
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getCodice_via());
		appendConSepa(sbOut, NON_VALORIZZATO, 4);
		appendConSepa(sbOut, comunicazione.getSc_codice_diritto());
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, "1");
		appendConSepa(sbOut, comunicazione.getReddito_euro());

		// IF Added by MaX - 11/02/2008. Perché in alcuni casi mi trovavo la detrazione anche se la tipologia non
		// era di Tipo Abitativo
		if (DapManager.isTipologiaImmobileAbitativo(comunicazione.getCategoria())) {
			appendConSepa(sbOut, comunicazione.getFlag_abit_principale());			
		} else {
			appendConSepa(sbOut, NON_VALORIZZATO);
		}
		appendConSepa(sbOut, comunicazione.getDecorrenza());
		appendConSepa(sbOut, comunicazione.getContitolari_abit_principale());
		appendConSepa(sbOut, comunicazione.getCodice_dap());
		appendConSepa(sbOut, NON_VALORIZZATO);
		appendConSepa(sbOut, comunicazione.getMembri_nucleo_familiare());
		appendConSepa(sbOut, NON_VALORIZZATO, 9);
		appendConSepa(sbOut, comunicazione.getNumero_nota_tras());
		appendConAcapo(sbOut, comunicazione.getTipo_nota());
	}

	private Comunicazione make(ResultSet rs) throws Exception {
		Comunicazione comunicazione = new Comunicazione();
		try {
			comunicazione.setIid_soggetto(rs.getString("IID_SOGGETTO"));
			comunicazione.setId_nota(rs.getString("ID_NOTA"));
			comunicazione.setSc_flag_tipo_titol_nota(rs.getString("SC_FLAG_TIPO_TITOL_NOTA"));
			comunicazione.setSf_flag_tipo_titol_nota(rs.getString("SF_FLAG_TIPO_TITOL_NOTA"));
			comunicazione.setCodice_fiscale(rs.getString("CODICE_FISCALE"));
			comunicazione.setCognome(rs.getString("COGNOME"));
			comunicazione.setNome(rs.getString("NOME"));
			comunicazione.setData_nascita(rs.getString("DATA_NASCITA"));
			comunicazione.setSesso(rs.getString("SESSO"));
			comunicazione.setComune_nascita(rs.getString("COMUNE_NASCITA"));
			comunicazione.setProvincia_nascita(rs.getString("PROVINCIA_NASCITA"));
			comunicazione.setIndirizzo_residenza(rs.getString("INDIRIZZO_RESIDENZA"));
			comunicazione.setComune(rs.getString("COMUNE"));
			comunicazione.setProvincia(rs.getString("PROVINCIA"));
			comunicazione.setAnno_nota(rs.getString("ANNO_NOTA"));
			comunicazione.setTipo_soggetto(rs.getString("TIPO_SOGGETTO"));
			comunicazione.setTipologia_immobile(rs.getString("TIPOLOGIA_IMMOBILE"));
			comunicazione.setIndirizzo(rs.getString("INDIRIZZO"));
			comunicazione.setFoglio(rs.getString("FOGLIO"));
			comunicazione.setParticella(rs.getString("PARTICELLA"));
			comunicazione.setSubalterno(rs.getString("SUBALTERNO"));
			comunicazione.setNumero_protocollo(rs.getString("NUMERO_PROTOCOLLO"));
			comunicazione.setAnno(rs.getString("ANNO"));
			comunicazione.setCategoria(rs.getString("CATEGORIA"));
			comunicazione.setClasse(rs.getString("CLASSE"));
			
			comunicazione.setPercentuale_possesso(rs.getString("PERCENTUALE_POSSESSO"));

			comunicazione.setSc_quota_numeratore(rs.getString("SC_QUOTA_NUMERATORE"));
			comunicazione.setSc_quota_denominatore(rs.getString("SC_QUOTA_DENOMINATORE"));
			comunicazione.setSf_quota_numeratore(rs.getString("SF_QUOTA_NUMERATORE"));
			comunicazione.setSf_quota_denominatore(rs.getString("SF_QUOTA_DENOMINATORE"));
			comunicazione.setSf_codice_diritto(rs.getString("SF_CODICE_DIRITTO"));
			comunicazione.setSc_codice_diritto(rs.getString("SC_CODICE_DIRITTO"));
			comunicazione.setCodice_variazione(rs.getString("CODICE_VARIAZIONE"));
			comunicazione.setData_validita_atto_date(rs.getString("DATA_VALIDITA_ATTO_DATE"));
			comunicazione.setNr_civico(rs.getString("NR_CIVICO"));
			comunicazione.setCodice_via(rs.getString("CODICE_VIA"));
			comunicazione.setReddito_euro(rs.getString("REDDITO_EURO"));
			comunicazione.setCodice_dap(rs.getString("CODICE_DAP"));
			comunicazione.setDecorrenza(rs.getString("DECORRENZA"));
			comunicazione.setContitolari_abit_principale(rs.getString("CONTITOLARI_ABIT_PRINCIPALE"));
			comunicazione.setFlag_abit_principale(rs.getString("FLAG_ABIT_PRINCIPALE"));
			comunicazione.setMembri_nucleo_familiare(rs.getString("MEMBRI_NUCLEO_FAMILIARE"));
			comunicazione.setNumero_nota_tras(rs.getString("NUMERO_NOTA_TRAS"));
			comunicazione.setFlag_pertinenza(rs.getString("FLAG_PERTINENZA"));
			comunicazione.setTipo_nota(rs.getString("TIPO_NOTA"));

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del ResultSet Comunicazione - " + ex);
			throw ex;
		}
		return comunicazione;
	}

}
