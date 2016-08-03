package it.escsolution.escwebgis.tributiNew.logic;

import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.docfa.bean.Docfa;

import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;
import it.escsolution.escwebgis.tributiNew.bean.*;
import it.webred.utils.GenericTuples;
import it.webred.utils.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Date;

import org.apache.log4j.Logger;

public class TributiOggettiTARSUNewLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public TributiOggettiTARSUNewLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
		
	public final static String TARSU = "TARSU";
	
	public final static String TARSU_DOCFA_COLLEGATI = "TARSU_DOCFA_COLLEGATI@TributiOggettiTARSUNewLogic";
	
	public final static String SOLO_ATT = "SOLO_ATT@TributiOggettiTARSUNewLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private static final DecimalFormat DF_CAT = new DecimalFormat();
	static {
		DF_CAT.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DF_CAT.setDecimalFormatSymbols(dfs);
		DF_CAT.setMaximumFractionDigits(2);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
		"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA from (" +
		"select distinct SIT_T_TAR_OGGETTO.ID, " + 
		"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
		"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
		"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
		"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
		"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
		"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA where 1 = ? and  SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+)";
	
	private final static String SQL_SELECT_LISTA_DA_VIA = "select * from (" +
	"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA from (" +
	"select distinct SIT_T_TAR_OGGETTO.ID, " + 
	"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
	"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
	"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
	"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
	"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
	"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
	"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
	"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA " +
	"where SIT_T_TAR_VIA.id=? AND SIT_T_TAR_VIA.id_ext=SIT_T_TAR_OGGETTO.id_ext_via))";
	
	private final static String SQL_SELECT_LISTA_DA_CIV = "select * from (" +
	"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA from (" +
	"select distinct SIT_T_TAR_OGGETTO.ID, " + 
	"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
	"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
	"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
	"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
	"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
	"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
	"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
	"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA " +
	"where SIT_T_TAR_VIA.id=? AND SIT_T_TAR_OGGETTO.NUM_CIV=? AND SIT_T_TAR_VIA.id_ext=SIT_T_TAR_OGGETTO.id_ext_via))";
	
	private final static String SQL_SELECT_LISTA_DA_OGG = "select * from (" +
	"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA from (" +
	"select distinct SIT_T_TAR_OGGETTO.ID, " + 
	"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
	"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
	"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
	"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
	"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
	"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
	"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
	"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
	"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA where SIT_T_TAR_OGGETTO.ID = ? and  SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+) ))";
	
	private final static String SQL_SELECT_LISTA_SOGG = "select * from (" +
		"select ROWNUM as N, ID, FOGLIO, NUMERO, SUB, DES_CLS_RSU, SUP_TOT, DAT_INI, DAT_FIN, PROVENIENZA, COD_FISC, COG_DENOM, NOME, PART_IVA from (" +
		"select distinct SIT_T_TAR_OGGETTO.ID, " + 
		"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
		"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
		"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
		"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
		"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
		"v.COD_FISC, v.COG_DENOM, v.NOME, v.PART_IVA " +
		"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA, V_T_TAR_SOGG_ALL v where 1 = ? " +
		"and SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+) " +
		"and SIT_T_TAR_OGGETTO.ID_EXT = v.ID_EXT_OGG_RSU ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from (" +
		"select distinct SIT_T_TAR_OGGETTO.ID, " + 
		"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
		"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
		"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
		"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
		"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA " +
		"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA where 1 = ? and  SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+)";
	
	private final static String SQL_SELECT_COUNT_LISTA_SOGG = "select count(*) as conteggio from (" +
		"select distinct SIT_T_TAR_OGGETTO.ID, " + 
		"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO, " +
		"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
		"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB, " +
		"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
		"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
		"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI, " +
		"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN, " +
		"NVL(SIT_T_TAR_OGGETTO.PROVENIENZA,'-') AS PROVENIENZA, " +
		"v.COD_FISC, v.COG_DENOM, v.NOME, v.PART_IVA " +
		"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA, V_T_TAR_SOGG_ALL v where 1 = ? " +
		"and SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+) " +
		"and SIT_T_TAR_OGGETTO.ID_EXT = v.ID_EXT_OGG_RSU ";
	
	
	
	boolean soloAtt = false;
	
	public Hashtable mCaricareListaOggettiTARSUCiv(String oggettoSel, OggettiTARSUNewFinder finder) throws Exception {
		
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
			java.sql.ResultSet rs = null;
			
			

				this.initialize();				
				
				StringTokenizer st = new StringTokenizer(oggettoSel, "|");
				String id = st.nextToken();
				String civ = st.nextToken();
				
				this.setString(1,id);
				this.setString(2,civ);
								

			    sql = SQL_SELECT_LISTA_DA_CIV;
				
				prepareStatement(sql);
				
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next()){
					OggettiTARSUNew tarsu = new OggettiTARSUNew();
					String codEnte = "";
					ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
					while (rsEnte.next()) {
						codEnte = rsEnte.getString("codent");
					}
					tarsu.setCodEnte(codEnte);
					tarsu.setChiave(rs.getString("ID"));
					tarsu.setFoglio(rs.getString("FOGLIO"));
					tarsu.setNumero(rs.getString("NUMERO"));
					tarsu.setSub(rs.getString("SUB"));
					tarsu.setDesClsRsu(rs.getString("DES_CLS_RSU"));
					tarsu.setSupTot(DF.format(rs.getDouble("SUP_TOT")));
					tarsu.setDatIni(rs.getObject("DAT_INI") == null ? "-" : SDF.format(rs.getDate("DAT_INI")));
					tarsu.setDatFin(rs.getObject("DAT_FIN") == null ? "ATTUALE" : 
							(SDF.format(rs.getDate("DAT_FIN")).equals("31/12/9999") ? "ATTUALE" : SDF.format(rs.getDate("DAT_FIN"))));
					tarsu.setProvenienza(rs.getString("PROVENIENZA"));
					setEvidenziaAttuale(conn, tarsu);
					
					GenericTuples.T2<String,String> coord = null;
					try {
						coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getNumero(),5),tarsu.getCodEnte());
					} catch (Exception e) {
					}
					
					if (coord!=null) {
							tarsu.setLatitudine(coord.firstObj);
							tarsu.setLongitudine(coord.secondObj);
						}
						
						vct.add(tarsu);
					}
			
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_TARSU",vct);
			
			finder.setTotaleRecordFiltrati(vct.size());
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
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
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}	
	
	public Hashtable mCaricareListaOggettiTARSU(Vector listaPar, OggettiTARSUNewFinder finder) throws Exception {
		
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
			java.sql.ResultSet rs = null;
			
			boolean hasParametriSoggetto = hasParametriSoggetto(listaPar);
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0)
					sql = hasParametriSoggetto ? SQL_SELECT_COUNT_LISTA_SOGG : SQL_SELECT_COUNT_LISTA;
				else
					sql = hasParametriSoggetto ? SQL_SELECT_LISTA_SOGG : SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					
				}
				else{
					sql = sql + " and SIT_T_TAR_OGGETTO.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by LPAD(FOGLIO, 5, '0'), LPAD(NUMERO, 5, '0'), LPAD(SUB, 4, '0'), DAT_FIN DESC)) where N > " + limInf + " and N <=" + limSup;
				else if (i == 0) sql = sql + ")";
				
				
				
				prepareStatement(sql);
				
				
				
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						OggettiTARSUNew tarsu = new OggettiTARSUNew();
						String codEnte = "";
						ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
						while (rsEnte.next()) {
							codEnte = rsEnte.getString("codent");
						}
						tarsu.setCodEnte(codEnte);
						tarsu.setChiave(rs.getString("ID"));
						tarsu.setFoglio(rs.getString("FOGLIO"));
						tarsu.setNumero(rs.getString("NUMERO"));
						tarsu.setSub(rs.getString("SUB"));
						tarsu.setDesClsRsu(rs.getString("DES_CLS_RSU"));
						tarsu.setSupTot(DF.format(rs.getDouble("SUP_TOT")));
						tarsu.setDatIni(rs.getObject("DAT_INI") == null ? "-" : SDF.format(rs.getDate("DAT_INI")));
						tarsu.setDatFin(rs.getObject("DAT_FIN") == null ? "ATTUALE" : 
							(SDF.format(rs.getDate("DAT_FIN")).equals("31/12/9999") ? "ATTUALE" : SDF.format(rs.getDate("DAT_FIN"))));
						tarsu.setProvenienza(rs.getString("PROVENIENZA"));
						setEvidenziaAttuale(conn, tarsu);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getNumero(),5),tarsu.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							tarsu.setLatitudine(coord.firstObj);
							tarsu.setLongitudine(coord.secondObj);
						}
						
						vct.add(tarsu);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_TARSU",vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
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
	
public Hashtable mCaricareListaOggettiTARSUFromVia(String idVia, OggettiTARSUNewFinder finder) throws Exception {
		
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			
					sql = SQL_SELECT_LISTA_DA_VIA;
				indice = 1;
				this.initialize();
				this.setString(indice,idVia);
				
				prepareStatement(sql);
				
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
					while (rs.next()){
						OggettiTARSUNew tarsu = new OggettiTARSUNew();
						String codEnte = "";
						ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
						while (rsEnte.next()) {
							codEnte = rsEnte.getString("codent");
						}
						tarsu.setCodEnte(codEnte);
						tarsu.setChiave(rs.getString("ID"));
						tarsu.setFoglio(rs.getString("FOGLIO"));
						tarsu.setNumero(rs.getString("NUMERO"));
						tarsu.setSub(rs.getString("SUB"));
						tarsu.setDesClsRsu(rs.getString("DES_CLS_RSU"));
						tarsu.setSupTot(DF.format(rs.getDouble("SUP_TOT")));
						tarsu.setDatIni(rs.getObject("DAT_INI") == null ? "-" : SDF.format(rs.getDate("DAT_INI")));
						tarsu.setDatFin(rs.getObject("DAT_FIN") == null ? "ATTUALE" : 
							(SDF.format(rs.getDate("DAT_FIN")).equals("31/12/9999") ? "ATTUALE" : SDF.format(rs.getDate("DAT_FIN"))));
						tarsu.setProvenienza(rs.getString("PROVENIENZA"));
						setEvidenziaAttuale(conn, tarsu);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getNumero(),5),tarsu.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							tarsu.setLatitudine(coord.firstObj);
							tarsu.setLongitudine(coord.secondObj);
						}
						
						vct.add(tarsu);
					}
				
			
			ht.put(SOLO_ATT, new Boolean(soloAtt));
			
			ht.put("LISTA_TARSU",vct);
			finder.setTotaleRecordFiltrati(vct.size());
			// pagine totali
			finder.setPagineTotali(1);
			finder.setTotaleRecord(vct.size());
			finder.setRighePerPagina(vct.size());

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = idVia;
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

public Hashtable mCaricareListaOggettiTARSUFromOggetto(String oggettoSel, OggettiTARSUNewFinder finder) throws Exception {
	
	Hashtable ht = new Hashtable();
	Vector vct = new Vector();
    sql = "";
	
	Connection conn = null;
	// faccio la connessione al db
	try {
		conn = this.getConnection();
		int indice = 1;
		java.sql.ResultSet rs = null;
		
		
				sql = SQL_SELECT_LISTA_DA_OGG;
			indice = 1;
			this.initialize();
			this.setString(indice,oggettoSel);
			
			prepareStatement(sql);
			
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
				while (rs.next()){
					OggettiTARSUNew tarsu = new OggettiTARSUNew();
					String codEnte = "";
					ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
					while (rsEnte.next()) {
						codEnte = rsEnte.getString("codent");
					}
					tarsu.setCodEnte(codEnte);
					tarsu.setChiave(rs.getString("ID"));
					tarsu.setFoglio(rs.getString("FOGLIO"));
					tarsu.setNumero(rs.getString("NUMERO"));
					tarsu.setSub(rs.getString("SUB"));
					tarsu.setDesClsRsu(rs.getString("DES_CLS_RSU"));
					tarsu.setSupTot(DF.format(rs.getDouble("SUP_TOT")));
					tarsu.setDatIni(rs.getObject("DAT_INI") == null ? "-" : SDF.format(rs.getDate("DAT_INI")));
					tarsu.setDatFin(rs.getObject("DAT_FIN") == null ? "ATTUALE" : 
						(SDF.format(rs.getDate("DAT_FIN")).equals("31/12/9999") ? "ATTUALE" : SDF.format(rs.getDate("DAT_FIN"))));
					tarsu.setProvenienza(rs.getString("PROVENIENZA"));
					setEvidenziaAttuale(conn, tarsu);
					
					GenericTuples.T2<String,String> coord = null;
					try {
						coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getNumero(),5),tarsu.getCodEnte());
					} catch (Exception e) {
					}
					if (coord!=null) {
						tarsu.setLatitudine(coord.firstObj);
						tarsu.setLongitudine(coord.secondObj);
					}
					
					vct.add(tarsu);
				}
			
		
		ht.put(SOLO_ATT, new Boolean(soloAtt));
		
		ht.put("LISTA_TARSU",vct);
		finder.setTotaleRecordFiltrati(vct.size());
		// pagine totali
		finder.setPagineTotali(1);
		finder.setTotaleRecord(vct.size());
		finder.setRighePerPagina(vct.size());

		ht.put("FINDER",finder);
		
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
	finally
	{
		if (conn != null && !conn.isClosed())
			conn.close();
	}

}
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception
	{
		String sql = "";
		Vector listaParOgg = new Vector();
		Vector listaParSogg = new Vector();
		soloAtt = false;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if ("SOLO_ATT".equalsIgnoreCase(el.getAttributeName())) {
				soloAtt = "Y".equalsIgnoreCase(el.getValore());
			} else {
				if (el.getAttributeName() != null && !el.getAttributeName().equals("")){
					if (isParametroOggetto(el)) {
						listaParOgg.add(el);
					} else {
						listaParSogg.add(el);
					}
				}
			}
		}		
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParOgg);
		indice = getCurrentParameterIndex();
		
		
		if (listaParSogg.size() > 0) {
			Vector listaParSoggQuery = new Vector();
			String[] titoliSogg = null;
			for (int i = 0; i < listaParSogg.size(); i++) {
				EscElementoFiltro el = (EscElementoFiltro)listaParSogg.get(i);
				String attName = el.getAttributeName();
				if ("TIT_SOGG".equalsIgnoreCase(attName)) {
					titoliSogg = el.getValori();
				} else {
					listaParSoggQuery.add(el);
				}
			}
			
			boolean cnt = false;
			boolean dic = false;
			boolean ult = false;
			if (titoliSogg == null || titoliSogg.length == 0) {
				//non si aggiungono clausole				
				//cnt = true;
				//dic = true;
				//ult = true;
			} else {
				for (String titoloSogg : titoliSogg) {
					if ("CNT".equalsIgnoreCase(titoloSogg)) {
						cnt = true;
					}
					if ("DIC".equalsIgnoreCase(titoloSogg)) {
						dic = true;
					}
					if ("ULT".equalsIgnoreCase(titoloSogg)) {
						ult = true;
					}
				}
			}
			String sqlAdd = "";
			boolean addOr = false;
			if (cnt) {
				if (addOr) {
					sqlAdd += " OR ";
				}
				
				sqlAdd += "v.TIPO_SOGGETTO = 'Contribuente'";
				
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			if (dic) {
				if (addOr) {
					sqlAdd += " OR ";
				}
				
				sqlAdd += "v.TIPO_SOGGETTO = 'Dichiarante'";
		
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			
			if (ult) {
				if (addOr) {
					sqlAdd += " OR ";
				}

				sqlAdd += "v.TIPO_SOGGETTO = 'Ulteriore Soggetto'";
				
				indice = getCurrentParameterIndex();
				addOr = true;
			}
			
			if (!"".equals(sqlAdd)) {
				sqlAdd = " and (" + sqlAdd + ")";
			}
			
			sqlAdd += super.elaboraFiltroMascheraRicercaParziale(indice, listaParSoggQuery);
			
			sql += sqlAdd;
		}
		
		if (soloAtt) {
			sql += " AND (SIT_T_TAR_OGGETTO.DAT_FIN IS NULL OR TO_CHAR(SIT_T_TAR_OGGETTO.DAT_FIN, 'dd/MM/yyyy') = '31/12/9999')";
		}
		
		return sql;
	}
	
	private boolean isParametroOggetto(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		//problema degli zeri iniziali
		if ("FOGLIO".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_TAR_OGGETTO.FOGLIO, 5, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 5, '0', true));
			}
		} else if ("NUMERO".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_TAR_OGGETTO.NUMERO, 5, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 5, '0', true));
			}
		} else if ("SUB".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_TAR_OGGETTO.SUB, 4, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 4, '0', true));
			}
		}else if ("NUM_CIV".equalsIgnoreCase(attName)) {
			if (!"".equals(el.getValore())) {
				el.setCampoFiltrato("LPAD(SIT_T_TAR_OGGETTO.NUM_CIV, 7, '0')");
				el.setValore(StringUtils.padding(el.getValore(), 7, '0', true));
			}
		}
		return  "DES_CLS_RSU".equalsIgnoreCase(attName) ||
				"FOGLIO".equalsIgnoreCase(attName) ||
				"NUMERO".equalsIgnoreCase(attName) ||
				"SUB".equalsIgnoreCase(attName) ||
				"VIA".equalsIgnoreCase(attName)||
				"NUM_CIV".equalsIgnoreCase(attName);
	}
	
	public List cercaClassiOggTARSU(String s) {
		List ris = new ArrayList();
		if(s == null)
			return ris;
		s = s.toUpperCase().trim();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = this.getConnection();
			String sql = "select distinct DES_CLS_RSU from SIT_T_TAR_OGGETTO where UPPER(DES_CLS_RSU) LIKE '%' || ? || '%'";
			this.initialize();
			this.setString(1, s);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				ris.add(rs.getString("DES_CLS_RSU"));
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}			
			if (conn != null)
				try
				{
					if (!conn.isClosed())
						conn.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}
			
		}
		return ris;		
	}
	
	public Hashtable mCaricareDettaglioOggettiTARSU(String chiave, String pathPlanimetrieComma340) throws Exception{
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			this.initialize();
			
			sql = "select SIT_T_TAR_OGGETTO.ID, SIT_T_TAR_OGGETTO.ID_EXT," +
			"NVL(SIT_T_TAR_OGGETTO.FOGLIO,'-') AS FOGLIO," +
			"NVL(SIT_T_TAR_OGGETTO.NUMERO,'-') AS NUMERO, " +
			"NVL(SIT_T_TAR_OGGETTO.SUB,'-') AS SUB," +
			"NVL(SIT_T_TAR_OGGETTO.DES_CLS_RSU,'-') AS DES_CLS_RSU, " +
			"SIT_T_TAR_OGGETTO.SUP_TOT AS SUP_TOT, " +
			"SIT_T_TAR_OGGETTO.DAT_INI AS DAT_INI," +
			"SIT_T_TAR_OGGETTO.DAT_FIN AS DAT_FIN," +
			"NVL(SIT_T_TAR_OGGETTO.DES_TIP_OGG,'-') AS DES_TIP_OGG, " +
			"DECODE(SIT_T_TAR_VIA.DESCRIZIONE, NULL, SIT_T_TAR_OGGETTO.DES_IND, (DECODE(SIT_T_TAR_VIA.PREFISSO, NULL, '', '', '', SIT_T_TAR_VIA.PREFISSO || ' ') || SIT_T_TAR_VIA.DESCRIZIONE)) AS DESC_IND, " +
			"SIT_T_TAR_OGGETTO.NUM_CIV, " +
			"SIT_T_TAR_OGGETTO.ESP_CIV, " +
			"NVL(SIT_T_TAR_OGGETTO.SCALA,'-') AS SCALA, " +
			"NVL(SIT_T_TAR_OGGETTO.PIANO,'-') AS PIANO, " +
			"NVL(SIT_T_TAR_OGGETTO.INTERNO,'-') AS INTERNO, " +
			"SIT_T_TAR_VIA.ID AS ID_VIA "+
			"from SIT_T_TAR_OGGETTO, SIT_T_TAR_VIA " +
			"where SIT_T_TAR_OGGETTO.ID = ? and SIT_T_TAR_OGGETTO.ID_EXT_VIA = SIT_T_TAR_VIA.ID_EXT(+) ";


			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			OggettiTARSUNew tarsu = new OggettiTARSUNew();
					
			if (rs.next()){
								
				tarsu.setChiave(rs.getString("ID"));
				tarsu.setIdExt(rs.getString("ID_EXT"));
				tarsu.setFoglio(rs.getString("FOGLIO"));
				tarsu.setNumero(rs.getString("NUMERO"));
				tarsu.setSub(rs.getString("SUB"));
				tarsu.setDesClsRsu(rs.getString("DES_CLS_RSU"));
				tarsu.setSupTot(DF.format(rs.getDouble("SUP_TOT")));
				
				Date dataIni = rs.getDate("DAT_INI");
				if(dataIni != null)
					tarsu.setDatIni(SDF.format(dataIni));
				else
					tarsu.setDatIni("-");
				
				Date dataFin = rs.getDate("DAT_FIN");
				if(dataFin != null)
					tarsu.setDatFin(SDF.format(dataFin));
				else
					tarsu.setDatFin("-");
				
				tarsu.setDesTipOgg(rs.getString("DES_TIP_OGG"));
				
				String desInd = rs.getObject("DESC_IND") == null ? null : rs.getString("DESC_IND");
				if (desInd == null) {
					desInd = "-";
				} else {
					String numCiv = rs.getObject("NUM_CIV") == null ? null : rs.getString("NUM_CIV");
					tarsu.setNumCiv(numCiv);
					if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
						try {
							numCiv = "" + Integer.parseInt(numCiv.trim());
						} catch (Exception e) {}
						String espCiv = rs.getObject("ESP_CIV") == null ? null : rs.getString("ESP_CIV");
						tarsu.setEspCiv(espCiv);
						if (espCiv != null && !espCiv.trim().equals("")) {
							numCiv += "/" + espCiv;
						}
						if (numCiv != null && !numCiv.trim().equals("") && !numCiv.trim().equals("0")) {
							desInd += " " + numCiv;
						}
					}						
				}				
				tarsu.setDesInd(desInd);

				tarsu.setScala(rs.getString("SCALA"));
				tarsu.setPiano(rs.getString("PIANO"));
				
				try {
					int inter = Integer.parseInt(rs.getString("INTERNO"));
					if(inter != 0) tarsu.setInterno(Integer.toString(inter));
					else tarsu.setInterno("-");
				} catch (Exception e) {
					tarsu.setInterno(rs.getObject("INTERNO") == null ? "-" : rs.getString("INTERNO"));
				}				

				tarsu.setIdVia(rs.getObject("ID_VIA") == null ? "-" : rs.getString("ID_VIA"));				
			}
			
			//Estrae il nome del comune
			String codEnte = "";
			String comune = "";
			this.initialize();
			ResultSet rsComune = conn.prepareStatement("select CODENT, DESCRIZIONE from SIT_ENTE").executeQuery();
			while (rsComune.next()) {
				codEnte = rsComune.getString("CODENT");
				comune = rsComune.getString("DESCRIZIONE");
				tarsu.setCodEnte(codEnte);
				tarsu.setComune(comune);
			}
			
			GenericTuples.T2<String,String> coord = null;
			try {
				coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getNumero(),5),tarsu.getCodEnte());
			} catch (Exception e) {
			}
			if (coord!=null) {
				tarsu.setLatitudine(coord.firstObj);
				tarsu.setLongitudine(coord.secondObj);
			}
			
			ht.put("TARSU",tarsu);


			if
			(
					/*(tarsu.getSupVani() != null && !tarsu.getSupVani().equals("-")) &&*/
					(tarsu.getFoglio() != null &&  !tarsu.getFoglio().trim().equals("") && !tarsu.getFoglio().equals("-")) 

			)
			{
				CatastoImmobiliLogic clogic = new CatastoImmobiliLogic(this.envUtente);	
				
				Immobile imm = new Immobile();
				imm.setFoglio(tarsu.getFoglio());
				imm.setNumero(tarsu.getNumero());
				imm.setUnimm(tarsu.getSub());
				
				ArrayList dettaglioVani = clogic.getSuperficiComma340(imm);
				tarsu.setSupCatasto(getSupCatasto(dettaglioVani));
				ht.put("TARSU_DETTAGLIO_VANI",dettaglioVani);
			}

			
			// docfa collegati
			if
			(
					(tarsu.getFoglio() != null && !tarsu.getFoglio().trim().equals("") && !tarsu.getFoglio().equals("-")) &&
					(tarsu.getNumero() != null && !tarsu.getNumero().trim().equals("") && !tarsu.getNumero().equals("-")) &&
					(tarsu.getSub() != null && !tarsu.getSub().trim().equals("") && !tarsu.getSub().equals("-")  && !tarsu.getSub().equals("0000"))

			)
			{
				//cerco dati planimetrici 
				sql = "SELECT distinct p.protocollo protocollo, "+
						" p.fornitura fornitura, "+
						" p.identificativo_immo identificativo_immo, "+
						"   u.foglio foglio, "+
						"   u.numero particella, "+
						"   u.subalterno subalterno "+
						" FROM docfa_uiu u, "+
						"   docfa_planimetrie p "+
						" WHERE u.protocollo_reg = p.protocollo "+
						"  AND u.fornitura = p.fornitura "+
						"  AND u.foglio = lpad(?,4,'0') "+
						"  AND u.numero = lpad(?,5,'0') "+
						"  AND u.subalterno = lpad(?,4,'0')";

				this.initialize();
				this.setString(1,tarsu.getFoglio());
				this.setString(2,tarsu.getNumero());
				this.setString(3,tarsu.getSub());
				prepareStatement(sql);
				java.sql.ResultSet rsDocfa = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				ArrayList docfaCollegati = new ArrayList();
				while (rsDocfa.next())
				{
					Docfa docfa = new Docfa();
					docfa.setProtocollo(tornaValoreRS(rsDocfa,"protocollo"));
					docfa.setFornitura(tornaValoreRS(rsDocfa,"fornitura","YYYY-MM-DD"));
					docfa.setIdentificativoImm(tornaValoreRS(rsDocfa,"identificativo_immo"));
					docfa.setFoglio(tornaValoreRS(rsDocfa,"foglio"));
					docfa.setParticella(tornaValoreRS(rsDocfa,"particella"));
					docfa.setSubalterno(tornaValoreRS(rsDocfa,"subalterno"));
					docfaCollegati.add(docfa);
				}
				ht.put( TARSU_DOCFA_COLLEGATI, docfaCollegati);
			}
			
			//Dettaglio Soggetti 
			
			sql = "select 'Contribuente' as titolo, " +
			"sit_t_tar_sogg.* " +
			"from sit_t_tar_sogg, sit_t_tar_contrib " +
			"where sit_t_tar_contrib.id_ext_ogg_rsu = ? "+
			"and sit_t_tar_sogg.id_ext = sit_t_tar_contrib.id_ext_sogg "+
			"union all "+
			"select 'Dichiarante' as titolo, sit_t_tar_sogg.* "+
			"from sit_t_tar_sogg, sit_t_tar_dich " +
			"where sit_t_tar_dich.id_ext_ogg_rsu = ? " +
			"and sit_t_tar_sogg.id_ext = sit_t_tar_dich.id_ext_sogg " +
			"union all " +
			"select * from( " +
			"select sit_t_tar_ogg_ultsogg.tit_sogg as titolo, sit_t_tar_sogg.* " +
			"from sit_t_tar_sogg, sit_t_tar_ult_sogg, sit_t_tar_ogg_ultsogg " +
			"where sit_t_tar_ult_sogg.id_ext_ogg_rsu = ? " +
			"and sit_t_tar_ogg_ultsogg.id_ext_tar_ult_sogg = sit_t_tar_ult_sogg.id_ext " +
			"and sit_t_tar_sogg.id_ext = sit_t_tar_ult_sogg.id_ext_sogg " +
			"order by titolo)";
			
			this.initialize();
			this.setString(1,tarsu.getIdExt());
			this.setString(2,tarsu.getIdExt());
			this.setString(3,tarsu.getIdExt());
			prepareStatement(sql);
			java.sql.ResultSet rsSogg = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList soggettiAssociati = new ArrayList();
			while (rsSogg.next())
			{
				SoggettiTributiNew sogg = new SoggettiTributiNew();
				sogg.setChiave("ID_EXT|" + rsSogg.getString("ID_EXT"));
				sogg.setIdOrig(rsSogg.getString("ID_ORIG"));
				sogg.setCogDenom(rsSogg.getString("COG_DENOM"));
				sogg.setNome(rsSogg.getString("NOME"));
				sogg.setCodFisc(rsSogg.getString("COD_FISC"));
				sogg.setPartIva(rsSogg.getString("PART_IVA"));
				sogg.setTitolo((rsSogg.getString("TITOLO")).toUpperCase());
				sogg.setProvenienza(rsSogg.getString("PROVENIENZA"));
				
				sogg.setId(rsSogg.getString("ID"));
				
				soggettiAssociati.add(sogg);
				
			}
			ht.put("SOGGETTI_ASSOCIATI_TARSU", soggettiAssociati);

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
	
	private boolean hasParametriSoggetto(Vector listaPar) {
		if (listaPar == null) {
			return false;
		}
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			String attName = el.getAttributeName();
			if ("TIT_SOGG".equalsIgnoreCase(attName)) {
				if (el.getValori() != null && el.getValori().length > 0) {
					return true;
				}
			} else {
				if (!"".equals(el.getValore())) {					
					if ("COD_FISC".equalsIgnoreCase(attName) ||
						"COG_DENOM".equalsIgnoreCase(attName) ||
						"NOME".equalsIgnoreCase(attName) ||
						"PART_IVA".equalsIgnoreCase(attName)) {
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	private void setEvidenziaAttuale(Connection conn, OggettiTARSUNew oggettoTARSU) throws Exception {
		oggettoTARSU.setEvidenzia(oggettoTARSU.getDatFin().equalsIgnoreCase("ATTUALE"));
	}
	
	private String getSupCatasto(ArrayList dettaglioVani) {
		if (dettaglioVani == null || dettaglioVani.size() == 0) {
			return "0";
		}
		double sup = 0;
		Iterator it = dettaglioVani.iterator();
		while (it.hasNext()) {
			OggettiTARSU vani = (OggettiTARSU)it.next();
			String ambiente = vani.getAmbiente();
			if (ambiente.equalsIgnoreCase("A") || ambiente.equalsIgnoreCase("B") || ambiente.equalsIgnoreCase("C")) {
				int mySup = 0;
				String supVani = vani.getSupVani();
				try {
					if (supVani != null && !supVani.equals("")) {
						mySup = Integer.parseInt(supVani);
					}
				} catch (Exception e) {}
				sup += mySup;
			}
		}
		sup *= 0.8;
		return DF_CAT.format(sup);
	}

}
