/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.catasto.bean.Qualita;
import it.escsolution.escwebgis.catasto.bean.TerreniFinder;
import it.escsolution.escwebgis.catasto.bean.Terreno;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.pregeo.bean.PregeoInfo;
import it.escsolution.escwebgis.pregeo.logic.PregeoLogic;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoDerivazioneDTO;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.utils.GenericTuples;
import it.webred.utils.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoTerreniLogic extends EscLogic{
	private String appoggioDataSource;
	public static final String LISTA_TERRENI ="LISTA_TERRENI";
	public final static String LISTA_TERR_DERIVATI = "LISTA_TERR_DERIVATI@CatastoTerreniLogic";
	public final static String LISTA_TERR_GENERATORI = "LISTA_TERR_GENERATORI@CatastoTerreniLogic";
	public final static String LISTA_TERR_STORICO = "LISTA_TERR_STORICO@CatastoTerreniLogic";
	public final static String SOLO_ATT = "SOLO_ATT@CatastoTerreniLogic";
	private static final String ANNOTAZIONE_AUTO = "CREATO AUTOMATICAMENTE IN FASE DI IMPORTAZIONE UIU";
	private SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private String DATA_MIN = "01/01/0001";
	private String DATA_MAX = "31/12/9999";

	private final String CONDIZIONE_SITICOMU = "(c.COD_NAZIONALE = ?) ";
	//private final String CONDIZIONE_SITICOMU = "(c.COD_NAZIONALE = ? OR c.CODI_FISC_LUNA)";
	
	public CatastoTerreniLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

/*	private final static String SQL_SELECT_LISTA= "select * from (" +
			"select rownum as N,SUPERFICIE,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO,PARTITA," +
			"COMUNE,QUALITA,CLASSE,REDDITO_DOMINICALE,REDDITO_AGRARIO,PK_PARTICELLA,FK_COMUNE,DATA_FINE_VAL,DATA_INIZIO_VAL " +
			"from (select ROWNUM as N," +
			"sititrkc.AREA_PART AS SUPERFICIE," +
			"decode(sititrkc.CLASSE_TERRENO,null,'-',sititrkc.CLASSE_TERRENO) AS CLASSE," +
			"decode(sititrkc.PARTITA,null,'-',sititrkc.PARTITA) AS PARTITA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
			"sititrkc.PARTICELLA as PARTICELLA," +
			"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
			"sititrkc.COD_NAZIONALE  AS FK_COMUNE," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE," +
			"sititrkc.REDDITO_DOMINICALE," +
			"sititrkc.REDDITO_AGRARIO," +
			"decode(sititrkc.QUAL_CAT,null,'-',sititrkc.QUAL_CAT) AS QUALITA," +
			"to_char(sititrkc.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_VAL,  " +
			"to_char(sititrkc.DATA_AGGI, 'dd/mm/yyyy')as DATA_INIZIO_VAL,  " +
			"sititrkc.TRKC_ID AS PK_PARTICELLA," +
			"sititrkc.annotazioni  " +
			"from sititrkc, siticomu " +
			"WHERE siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
			//"and  sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"AND NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			"AND 1 = ? ";*/
	
	private final static String SQL_SELECT_LISTA= "select * from (" +
			"select rownum as N, SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO, " +
			"COMUNE,FK_COMUNE " +
			"from (select DISTINCT " +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
			"sititrkc.PARTICELLA as PARTICELLA," +
			"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
			"sititrkc.COD_NAZIONALE  AS FK_COMUNE," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE " +
			"from sititrkc, siticomu " +
			"WHERE siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
			//"and  sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"AND NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			"AND 1 = ? ";

	/*private final static String SQL_SELECT_COUNT_LISTA=""+
				"select count(*) as conteggio  from sititrkc, siticomu "  +
				"WHERE siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
				"AND NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
				"AND 1 = ? ";
				//"and sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " ;
*/
	
	private final static String SQL_SELECT_COUNT_LISTA=""+
			"select count(*) as conteggio  from " +
			"(select rownum as N, SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO, " +
			"COMUNE,FK_COMUNE " +
			"from (select DISTINCT " +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
			"sititrkc.PARTICELLA as PARTICELLA," +
			"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
			"sititrkc.COD_NAZIONALE  AS FK_COMUNE," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE " +
			"from sititrkc, siticomu " +
			"WHERE siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
			//"and  sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
			"AND NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			"AND 1 = ? " ;
	
	private final static String SQL_SELECT_COUNT_ALL ="" +
				"select count(*) as conteggio  from sititrkc , siticomu "  +
				"WHERE siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
				"AND NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
				"AND 1 = ? " + 
				"and sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " ;
				//"and sititrkc.COD_NAZIONALE = 'F205'" ;

	private final static String SQL_SELECT_TERRENI_PER_GAUSS = "select '' AS ARE," +
			"'' AS CENTIARE," +
			"decode(siticata.CLASSE_TERRENO,null,'-',siticata.CLASSE_TERRENO) AS CLASSE," +
			"decode(siticata.PARTITA,null,'-',siticata.PARTITA) AS PARTITA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(siticata.FOGLIO,null,'-',siticata.FOGLIO) AS FOGLIO," +
			"siticata.PARTICELLA as NUMERO," +
			"decode(siticata.SUB,null,'-',siticata.SUB) as SUBALTERNO," +
			"'' AS ETTARI," +
			"cat_particelle_gauss.FK_COMUNE," +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE," +
			"decode(siticata.SUPE,null,'-',siticata.SUPE) AS SUPERFICIE," +
			"siticata.REDDITO_DOMINICALE," +
			"siticata.REDDITO_AGRARIO," +
			"decode(siticata.QUAL_CAT,null,'-',siticata.QUAL_CAT) AS QUALITA," +
			"cat_particelle_gauss.PK_PARTICELLE AS PK_PARTICELLA " +
			"from siticata, siticomu , CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss " +
			"WHERE siticomu.COD_NAZIONALE = cat_particelle_gauss.FK_COMUNE" +
			" and  siticomu.COD_NAZIONALE=siticata.COD_NAZIONALE" +
			" and cat_particelle_gauss.FOGLIO=siticata.FOGLIO" +
			" and cat_particelle_gauss.PARTICELLA=siticata.PARTICELLA" +
			" and nvl(upper(siticata.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			" AND cat_particelle_gauss.PK_PARTICELLE = ?";

	boolean soloAtt = false;
	
	public Hashtable mCaricareDettaglioTerreno(String oggettoSel, String pathDatiDiogene) throws Exception{
				// carico la lista dei terreni e la metto in un hashtable
				Hashtable ht = new Hashtable();

				// faccio la connessione al db
				Connection conn = null;
				try {
					Object[] listParam = new Object[6];
					if (oggettoSel.indexOf("|")>0)
						listParam=  oggettoSel.split("\\|");
					else
						listParam[0] = oggettoSel;

					this.setDatasource(JNDI_CATCOSPOLETO);
					conn = this.getConnection();
					
					//controllo per eventuali chiamate al dettaglio (es. da mappa) con annotazioni = "CREATO AUTOMATICAMENTE IN FASE DI IMPORTAZIONE UIU"
					String newOggettoSel = verificaAnnotazioni(conn, listParam);
					if (newOggettoSel != null) {
						return mCaricareDettaglioTerreno(newOggettoSel, pathDatiDiogene);
					}

					this.initialize();
					
					String sql = "SELECT DISTINCT " +
							"decode(c.ID_SEZC,null,'-',c.ID_SEZC) AS SEZIONE," +
							"decode(st.FOGLIO,null,'-',st.FOGLIO) AS FOGLIO," +
							"st.PARTICELLA as NUMERO," +
							"decode(st.SUB,null,'-',st.SUB) as SUBALTERNO," +
							"st.COD_NAZIONALE AS FK_COMUNE," +
							"decode(c.NOME,null,'-',c.NOME) AS COMUNE " +
							"from sititrkc st, siticomu c "+
							"WHERE "+this.CONDIZIONE_SITICOMU+
							"AND st.COD_NAZIONALE = c.COD_NAZIONALE " +
							"AND st.foglio=? and st.particella=? and NVL(st.sub,' ')=NVL(?,' ') ";
							
					
					/*
					 * String sql = 
					"select decode(sititrkc.CLASSE_TERRENO,null,'-',sititrkc.CLASSE_TERRENO) AS CLASSE," +
					"decode(sititrkc.PARTITA,null,'-',sititrkc.PARTITA) AS PARTITA," +
					"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
					"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
					"sititrkc.PARTICELLA as NUMERO," +
					"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
					"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
					"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE," +
					"decode(sititrkc.AREA_PART,null,'-',sititrkc.AREA_PART) AS SUPERFICIE," +
					"sititrkc.REDDITO_DOMINICALE," +
					"sititrkc.REDDITO_AGRARIO," +
					"decode(sititrkc.QUAL_CAT,null,'-',sititrkc.QUAL_CAT) AS QUALITA," +
					"sititrkc.TRKC_ID AS PK_PARTICELLA, " +
					"nvl(to_char(sititrkc.DATA_FINE,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
					"nvl(to_char(sititrkc.DATA_AGGI,'dd/mm/yyyy'),'-') AS DATA_INIZIO, " +
					"decode(siticods_qual.DESC_QUAL,null,'-',siticods_qual.DESC_QUAL) AS DESCR_QUALITA, " +
					"sititrkc.annotazioni, sititrkc.IDE_MUTA_INI, sititrkc.IDE_MUTA_FINE "+
					"from sititrkc, siticomu,siticods_qual " +
					"WHERE sititrkc.TRKC_ID = ? " +
					"AND siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
					//"and sititrkc.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
					"and sititrkc.QUAL_CAT = siticods_qual.CODI_QUAL(+) ";


					int indice = 1;
					//this.setString(indice,particella);
					this.setString(indice,(String)listParam.get(0));
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					Terreno ter = new Terreno();
					String foglio = "";
					String numero = "";
					if (rs.next()){
						ter.setSezione(rs.getString("SEZIONE"));
						foglio = rs.getString("FOGLIO");
						ter.setFoglio(foglio);
						numero = rs.getString("NUMERO");
						ter.setNumero(numero);
						ter.setSubalterno(rs.getString("SUBALTERNO"));
						ter.setPartita(rs.getString("PARTITA"));
						ter.setComune(rs.getString("COMUNE"));
						ter.setSuperficie(rs.getString("SUPERFICIE"));
						ter.setQualita(rs.getString("QUALITA"));
						ter.setClasse(rs.getString("CLASSE"));
						ter.setRDominicale(tornaValoreRS(rs,"REDDITO_DOMINICALE","EURO"));
						ter.setRAgrario(tornaValoreRS(rs,"REDDITO_AGRARIO","EURO"));
						ter.setDescrQualita(rs.getString("DESCR_QUALITA"));
						ter.setDataFine(rs.getString("DATA_FINE"));
						ter.setDataInizio(rs.getString("DATA_INIZIO"));
						ter.setCodente(rs.getString("FK_COMUNE"));
						ter.setAnnotazione(rs.getString("ANNOTAZIONI"));
						ter.setIdeMutaIni(rs.getBigDecimal("IDE_MUTA_INI"));
						ter.setIdeMutaFine(rs.getBigDecimal("IDE_MUTA_FINE"));
						ter.setStato(ter.getDataFine());*/
					
						
						String codEnte = ((String)listParam[0]);
						String foglio = ((String)listParam[1]);
						String numero = ((String)listParam[2]);
						String sub = ((String)listParam[3]);
						
						int index = 0;
						this.setString(++index,codEnte);
						//this.setString(++index,codEnte);
						this.setString(++index,foglio);
						this.setString(++index,numero);
						this.setString(++index,sub);
						
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						Terreno ter = new Terreno();
						if (rs.next()){
							
							ter.setSezione(rs.getString("SEZIONE"));
							ter.setFoglio(rs.getString("FOGLIO"));
							ter.setNumero(rs.getString("NUMERO"));
							ter.setSubalterno(rs.getString("SUBALTERNO"));
							ter.setComune(rs.getString("COMUNE"));
							ter.setCodente(rs.getString("FK_COMUNE"));
								
							GenericTuples.T2<String,String> coord = null;
							try {
								coord = getLatitudeLongitude(ter.getFoglio(), ter.getNumero(), ter.getCodente());
							} catch (Exception e) {
							}
							if (coord!=null) {
								ter.setLatitudine(coord.firstObj);
								ter.setLongitudine(coord.secondObj);
							}
							
					}
					
					
					//Caricamento Dati Pregeo
					String pathPregeo = pathDatiDiogene + File.separatorChar + this.getDirPregeoEnte();
					PregeoLogic pLogic = new PregeoLogic(super.getEnvUtente());
					ht.put("PREGEO", pLogic.getListaPregeoFabbricato(ter.getFoglio(),ter.getNumero(),pathPregeo,false));
					
					List<Sititrkc> lstStoricoTerreni =  this.getListaStoricoTerreni(ter, this.getEnvUtente().getEnte());
				
					String dtFin = ter.getDataFine();
					String dtIni = ter.getDataInizio();
					
					//setto ide_muta_ini  del terreno di cui faccio il dettaglio come l'ide_muta_ini del terreno ultimo della lista, oppure del penultimo etc.., se non valorizzato (primo in ordine di tempo)
					//setto ide_muta_fine del terreno di cui faccio il dettaglio come l'ide_muta_fine del terreno primo della lista, oppure del secondo etc..,   se non valorizzato (ultimo in ordine di tempo)
					
					BigDecimal ideMutaFine=new BigDecimal(0);
					BigDecimal ideMutaIni=new BigDecimal(0);
	 				if (lstStoricoTerreni!= null && lstStoricoTerreni.size()>0){
						
						for (int i= 0; i< lstStoricoTerreni.size(); i++ ){
							Sititrkc terreno = lstStoricoTerreni.get(i);
							if (terreno.getIdeMutaFine()!= null && !terreno.getIdeMutaFine().equals(BigDecimal.ZERO)){
								ideMutaFine= terreno.getIdeMutaFine();
								break;
							}
						}
						for (int i= lstStoricoTerreni.size() -1; i>-1; i-- ){
							Sititrkc terreno = lstStoricoTerreni.get(i);
							if (terreno.getIdeMutaIni()!= null && !terreno.getIdeMutaIni().equals(BigDecimal.ZERO)){
								ideMutaIni= terreno.getIdeMutaIni();
								break;
							}
						}
						
						ter.setIdeMutaIni(ideMutaIni);
						ter.setIdeMutaFine(ideMutaFine);
						
						//setto data fine validità come data fine del primo della lista
						//setto data inziio validità come data inizio dell'ultimo
						 dtFin = lstStoricoTerreni.get(0).getId().getDataFine() !=null ? SDF.format(lstStoricoTerreni.get(0).getId().getDataFine()) : this.DATA_MAX;
						 dtIni = lstStoricoTerreni.get(lstStoricoTerreni.size()-1).getDataAggi() !=null ? 
								SDF.format(lstStoricoTerreni.get(lstStoricoTerreni.size()-1).getDataAggi()) : this.DATA_MIN;
								
						ter.setDataFine(dtFin);
						ter.setDataInizio(dtIni);
					}
						
	 				ter.setParticella(codEnte+"|"+foglio+"|"+numero+"|"+sub+"|"+ter.getDataFine()+"|"+ter.getDataInizio());
	 				ht.put("TERRENO",ter);
	 				
					ht.put(this.LISTA_TERR_STORICO, lstStoricoTerreni);
					
					// cerco i terreni derivati 
					if (dtFin!= null && (!dtFin.equals(this.DATA_MAX) && !dtFin.equalsIgnoreCase("ATTUALE")))
						ht.put(this.LISTA_TERR_DERIVATI, this.getTerreniDerivati(ter, this.getEnvUtente().getEnte()));
					
					// cerco i terreni da cui deriva quella in oggetto 
					if (dtIni!= null && !dtIni.equals(this.DATA_MIN))
						ht.put(this.LISTA_TERR_GENERATORI,this.getTerreniGeneratori(ter, this.getEnvUtente().getEnte()));
					
					/*INIZIO AUDIT*/
					try {
						Object[] arguments = new Object[1];
						arguments[0] = oggettoSel;
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
					DbUtils.close(conn);
				}
		}
	
	private String verificaAnnotazioni(Connection conn, Object[] listParam) {
		String retVal = null;
		if (listParam == null || listParam.length < 4) {
			return retVal;
		}
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT ST.ANNOTAZIONI "
					+ "FROM SITITRKC ST, SITICOMU C "
					+ "WHERE C.COD_NAZIONALE = ? "
					+ "AND ST.COD_NAZIONALE = C.COD_NAZIONALE "
					+ "AND ST.FOGLIO = ? "
					+ "AND ST.PARTICELLA = ? "
					+ "AND NVL(ST.SUB, ' ') = NVL (?, ' ')";
			
			st = conn.prepareStatement(sql);
			
			String codEnte = ((String)listParam[0]);
			String foglio = ((String)listParam[1]);
			String numero = ((String)listParam[2]);
			String sub = ((String)listParam[3]);			
			int index = 0;
			st.setString(++index, codEnte);
			st.setString(++index, foglio);
			st.setString(++index, numero);
			st.setString(++index, sub);
			
			rs = st.executeQuery();
			String ann = null;
			while (rs.next()) {
				ann = rs.getObject("ANNOTAZIONI") == null ? null : rs.getString("ANNOTAZIONI");
			}
			if (!ANNOTAZIONE_AUTO.equals(ann)) {
				return retVal;
			}
			
			rs.close();
			st.close();
			
			sql = "SELECT DISTINCT DECODE (C.ID_SEZC, NULL, '-', C.ID_SEZC) AS SEZIONE, "
					+ "DECODE (ST.FOGLIO, NULL, '-', ST.FOGLIO) AS FOGLIO, "
					+ "ST.PARTICELLA AS NUMERO, "
					+ "DECODE (ST.SUB, NULL, ' ', ST.SUB) AS SUBALTERNO, "
					+ "ST.COD_NAZIONALE AS FK_COMUNE, "
					+ "DECODE (C.NOME, NULL, '-', C.NOME) AS COMUNE "
					+ "FROM SITITRKC ST, SITICOMU C "
					+ "WHERE ST.COD_NAZIONALE = C.COD_NAZIONALE "
					+ "AND ST.FOGLIO = ? "
					+ "AND ST.PARTICELLA = ? "
					+ "AND NVL(ST.SUB, ' ') = NVL(?, ' ') "
					+ "AND NVL(ANNOTAZIONI, ' ') <> ? "
					+ "AND EXISTS ("
					+ "SELECT 1 FROM SITITRKC ST1 "
					+ "WHERE ST1.FOGLIO = ST.FOGLIO "
					+ "AND ST1.PARTICELLA = ST.PARTICELLA "
					+ "AND NVL(ST1.SUB, ' ') = NVL(ST.SUB, ' ') "
					+ "AND NVL(ST1.ANNOTAZIONI, ' ') = ?) "
					+ "AND EXISTS ("
					+ "SELECT 1 FROM CAT_PARTICELLE_GAUSS CPG "
					+ "WHERE CPG.FK_COMUNE <> ST.COD_NAZIONALE "
					+ "AND CPG.FOGLIO = ST.FOGLIO "
					+ "AND CPG.PARTICELLA = ST.PARTICELLA "
					+ "AND NVL (CPG.SUB, ' ') = NVL (ST.SUB, ' ') "
					+ "AND EXISTS ("
					+ "SELECT 1 FROM CAT_PARTICELLE_GAUSS CPG1 "
					+ "WHERE CPG1.FK_COMUNE = ST.COD_NAZIONALE "
					+ "AND CPG1.FOGLIO = ST.FOGLIO "
					+ "AND CPG1.PARTICELLA = ST.PARTICELLA "
					+ "AND NVL (CPG1.SUB, ' ') = NVL (ST.SUB, ' ') "
					+ "AND SDO_GEOM.RELATE(CPG1.GEOMETRY, 'ANYINTERACT', CPG.GEOMETRY, 0.0005) = 'TRUE'))";
			
			st = conn.prepareStatement(sql);
			index = 0;
			
			st.setString(++index, foglio);
			st.setString(++index, numero);
			st.setString(++index, sub);
			st.setString(++index, ANNOTAZIONE_AUTO);
			st.setString(++index, ANNOTAZIONE_AUTO);
			
			String codEnteNew = null;
			String foglioNew = null;
			String numeroNew = null;
			String subNew = null;
			
			rs = st.executeQuery();
			while (rs.next()) {
				codEnteNew = rs.getString("FK_COMUNE");
				foglioNew = rs.getString("FOGLIO");
				numeroNew = rs.getString("NUMERO");
				subNew = rs.getString("SUBALTERNO");
			}
			
			if (codEnteNew == null || foglioNew == null || numeroNew == null || subNew == null) {
				return retVal;
			} else {
				listParam[0] = codEnteNew;
				listParam[1] = foglioNew;
				listParam[2] = numeroNew;
				listParam[3] = subNew;
				
				for (Object obj : listParam) {
					if (retVal == null) {
						retVal = (String)obj;
					} else {
						retVal += ("|" + (String)obj);
					}
				}				
			}
		} catch (Exception e) {
			log.debug("Errore in verifica annotazioni", e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (st != null && !st.isClosed()) {
					st.close();
				}
			} catch(Exception e1) {
				log.debug(e1);
			}			
		}		
		return retVal;
	}

	public Hashtable mCaricareDatiFormRicerca() throws NamingException, CatastoServiceException{
			// carico la lista dei terreni e le metto in un hashtable
			Hashtable ht = new Hashtable();

			CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

			ht.put("LISTA_QUALITA",mCaricareQualita(catastoService));
			ht.put("LISTA_CLASSE",mCaricareClasse(catastoService));

			return ht;
		}




	private Vector<Qualita> mCaricareQualita(CatastoService catastoService) throws CatastoServiceException{
		
		Vector<Qualita> vct = new Vector<Qualita>();
		vct.add(new Qualita("","Tutte"));
		
		EnvUtente eu = this.getEnvUtente();

		try{	
			CeTBaseObject cet = new CeTBaseObject();
			cet.setEnteId(eu.getEnte());
			cet.setUserId(eu.getUtente().getUsername());
			
			List<KeyValueDTO> lista = catastoService.getListaQualitaTerreni(cet);
			
			for(KeyValueDTO kv : lista)
				vct.add(new Qualita(kv.getKey(),kv.getValue()));

		}catch(Exception e){
			log.error("mCaricareQualita",e);
		}
		
	return vct;
	}
	
	private Vector<Qualita> mCaricareClasse(CatastoService catastoService) throws CatastoServiceException{
		Vector<Qualita> vct = new Vector<Qualita>();
	
		String sql="select codi_qual,desc_qual as descrizione from SITICODS_QUAL";
	
		//Statement stmt = conn.createStatement();
		//ResultSet rs = stmt.executeQuery(sql);
		vct.add(new Qualita("","Tutte"));
		/*
		while (rs.next()){
			new Qualita(rs.getString("cid_qual"),rs.getString("desc_qual"));
		}*/
	
	
		return vct;
	}
		

	private List<Sititrkc> getListaStoricoTerreni(Terreno terr,  String codEnte) throws Exception{
		
		List<Sititrkc> listaStorico = new ArrayList<Sititrkc>();
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setCodEnte(terr.getCodente());
		rc.setSezione(terr.getSezione());
		rc.setFoglio(terr.getFoglio());
		rc.setParticella(terr.getNumero());
		rc.setSub(terr.getSubalterno());
		
		listaStorico = catastoService.getListaStoricoTerreniByFPS(rc);
		
		
		return listaStorico;
	}


	private List<TerrenoDerivazioneDTO> getTerreniDerivati(Terreno terr,  String codEnte) throws Exception{
		
		List<TerrenoDerivazioneDTO> listaTerrDerivati = new ArrayList<TerrenoDerivazioneDTO>();
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setIdeMuta(terr.getIdeMutaFine());
		rc.setFoglio(terr.getFoglio());
		rc.setParticella(terr.getNumero());
		rc.setSub(terr.getSubalterno());
		
		 listaTerrDerivati = catastoService.getTerreniDerivati(rc);
		
		
		return listaTerrDerivati;
	}
			
	private List<TerrenoDerivazioneDTO> getTerreniGeneratori(Terreno terr,  String codEnte) throws Exception{
		
		List<TerrenoDerivazioneDTO> listaTerrGen = new ArrayList<TerrenoDerivazioneDTO>();
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setIdeMuta(terr.getIdeMutaIni());
		rc.setFoglio(terr.getFoglio());
		rc.setParticella(terr.getNumero());
		rc.setSub(terr.getSubalterno());
		
		listaTerrGen = catastoService.getTerreniGeneratori(rc);
			
				
		return listaTerrGen;
	}

	public Hashtable mCaricareListaTerreniPerTitolarita(String titolarita) throws Exception{
		// String foglio,String numero,String subalterno,String partita,String comune,String ettari,String are, String centiare,String superficie, String qualita,String classe,String Rdominicale,String Ragrario)
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String sql = "";
		String conteggio = "";
		long conteggione = 0;

		// faccio la connessione al db

		java.sql.ResultSet rs = null;
		java.sql.ResultSet rs2 = null;
		Connection conn = this.getConnection();
		try {

			// il primo ciclo faccio la count
			this.setDatasource(JNDI_CATCOSPOLETO);
			sql="" +
			"select distinct " +
			//"sititrkc.AREA_PART as AREA, " +
			//"decode(sititrkc.CLASSE_TERRENO,null,'-',sititrkc.CLASSE_TERRENO) AS CLASSE," +
			//"decode(sititrkc.PARTITA,null,'-',sititrkc.PARTITA) AS PARTITA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
			"decode(sititrkc.PARTICELLA,null,'-',sititrkc.PARTICELLA) as NUMERO," +
			"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
			"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
			//"decode(sititrkc.AREA_PART,null,'-',sititrkc.AREA_PART) AS SUPERFICIE," +
			//"sititrkc.REDDITO_DOMINICALE," +
			//"sititrkc.REDDITO_AGRARIO," +
			//"decode(sititrkc.QUAL_CAT,null,'-',sititrkc.QUAL_CAT) AS QUALITA," +
			"to_char(sititrkc.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_VAL,  " +
			"sititrkc.DATA_FINE as DATA_FINE_VAL_DATE, " +
			"to_char(sititrkc.DATA_AGGI, 'dd/mm/yyyy')as DATA_INIZIO_VAL,  " +
			"to_char(cons_cons_tab.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_POS, " +
			"cons_cons_tab.DATA_FINE as DATA_FINE_POS_DATE, " +
			"sititrkc.TRKC_ID AS PK_PARTICELLA, " +
			"cons_deco_tab.description AS TITOLO, " +
			"cons_cons_tab.PERC_POSS, sititrkc.annotazioni " +
			"from sititrkc, cons_cons_tab, siticomu, cons_deco_tab " +
			"where cons_cons_tab.PK_CUAA = ? " +
			"and sititrkc.COD_NAZIONALE = cons_cons_tab.COD_NAZIONALE " +
			"and siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
			"and sititrkc.FOGLIO = cons_cons_tab.FOGLIO " +
			"and sititrkc.PARTICELLA = cons_cons_tab.PARTICELLA " +
			"and sititrkc.SUB = cons_cons_tab.SUB " +
			//"and ( cons_cons_tab.DATA_INIZIO between sititrkc.DATA_AGGI and sititrkc.DATA_FINE " +
			//"or cons_cons_tab.DATA_FINE between sititrkc.DATA_AGGI and sititrkc.DATA_FINE ) " +
			"and cons_cons_tab.DATA_INIZIO < sititrkc.DATA_FINE " +
			"and  cons_cons_tab.DATA_FINE > sititrkc.DATA_AGGI " +
			"and nvl(upper(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_cons_tab.tipo_documento " +
			"order by TO_NUMBER(foglio),numero, DATA_FINE_VAL_DATE DESC,  DATA_FINE_POS_date DESC";

			
			log.debug("mCaricareListaTerreniPerTitolarita-sql1["+sql+"]");
			log.debug("param["+titolarita+"]");
			
			
			int indice = 1;
			this.initialize();
			this.setString(indice,titolarita);
			indice ++;

//				il primo passaggio esegue la select count


			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			HashMap temp = new HashMap();
			while (rs.next()){
				Terreno ter = new Terreno();
				ter.setSezione(rs.getString("SEZIONE"));
				ter.setFoglio(rs.getString("FOGLIO"));
				ter.setNumero(rs.getString("NUMERO"));
				ter.setSubalterno(rs.getString("SUBALTERNO"));
				/*ter.setPartita(rs.getString("PARTITA"));
				ter.setSuperficie(rs.getString("SUPERFICIE"));
				ter.setQualita(rs.getString("QUALITA"));
				ter.setClasse(rs.getString("CLASSE"));
				ter.setRDominicale(tornaValoreRS(rs,"REDDITO_DOMINICALE","EURO"));
				ter.setRAgrario(tornaValoreRS(rs,"REDDITO_AGRARIO","EURO"));*/
				ter.setDataFine(rs.getString("DATA_FINE_VAL"));
				ter.setDataInizio(rs.getString("DATA_INIZIO_VAL"));
				ter.setDataFinePos(rs.getString("DATA_FINE_POS"));
				
				String chiave = rs.getString("FK_COMUNE")+"|"+ter.getFoglio()+"|"+ter.getNumero()+"|"+ter.getSubalterno();
				chiave += "|"+ ter.getDataFine() +"|"+ter.getDataInizio();
				
				ter.setParticella(chiave);
				
				//ter.setParticella(rs.getString("PK_PARTICELLA")+"|"+rs.getString("DATA_INIZIO_VAL")+"|"+rs.getString("DATA_FINE_VAL"));
				String titolo = rs.getString("TITOLO");
				ter.setTitolo(titolo!=null ? titolo : "PROPRIETARIO");
				
				ter.setPercPoss(rs.getBigDecimal("PERC_POSS"));
				ter.setAnnotazione(rs.getString("ANNOTAZIONI"));
				
				String key =    ter.getSezione() +"|"+
								ter.getFoglio() +"|"+
								ter.getNumero() +"|"+
								ter.getSubalterno() +"|"+
								ter.getDataFinePos() +"|"+
								ter.getTitolo();
				
				if(!temp.containsKey(key)){
					vct.add(ter);
					temp.put(key, ter);
				}
			}

			//recupero altre titolarità  cons_ufre_tab
			String sql2="" +
			"select distinct " +
			//"sititrkc.AREA_PART as AREA, " +
			//"decode(sititrkc.CLASSE_TERRENO,null,'-',sititrkc.CLASSE_TERRENO) AS CLASSE," +
			//"decode(sititrkc.PARTITA,null,'-',sititrkc.PARTITA) AS PARTITA," +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
			"decode(sititrkc.FOGLIO,null,'-',sititrkc.FOGLIO) AS FOGLIO," +
			"decode(sititrkc.PARTICELLA,null,'-',sititrkc.PARTICELLA) as NUMERO," +
			"decode(sititrkc.SUB,null,'-',sititrkc.SUB) as SUBALTERNO," +
			"sititrkc.COD_NAZIONALE AS FK_COMUNE," +
			//"decode(sititrkc.AREA_PART,null,'-',sititrkc.AREA_PART) AS SUPERFICIE," +
			//"sititrkc.REDDITO_DOMINICALE," +
			//"sititrkc.REDDITO_AGRARIO," +
			//"decode(sititrkc.QUAL_CAT,null,'-',sititrkc.QUAL_CAT) AS QUALITA," +
			"to_char(sititrkc.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_VAL,  " +
			"sititrkc.DATA_FINE as DATA_FINE_VAL_DATE, " +
			"to_char(sititrkc.DATA_AGGI, 'dd/mm/yyyy')as DATA_INIZIO_VAL,  " +
			"to_char(cons_ufre_tab.DATA_FINE, 'dd/mm/yyyy')as DATA_FINE_POS, " +
			"cons_ufre_tab.DATA_FINE as DATA_FINE_POS_DATE, " +
			"sititrkc.TRKC_ID AS PK_PARTICELLA, " +
			"cons_deco_tab.description AS TITOLO, " +
			"cons_ufre_tab.PERC_POSS " +
			"from sititrkc, cons_ufre_tab, siticomu, cons_deco_tab " +
			"where cons_ufre_tab.PK_CUAA = ? " +
			"and sititrkc.COD_NAZIONALE = cons_ufre_tab.COD_NAZIONALE " +
			"and siticomu.COD_NAZIONALE = sititrkc.COD_NAZIONALE " +
			"and sititrkc.FOGLIO = cons_ufre_tab.FOGLIO " +
			"and sititrkc.PARTICELLA = cons_ufre_tab.PARTICELLA " +
			"and sititrkc.SUB = cons_ufre_tab.SUB " +
			//"and ( cons_ufre_tab.DATA_INIZIO between sititrkc.DATA_AGGI and sititrkc.DATA_FINE " +
			//"or cons_ufre_tab.DATA_FINE between sititrkc.DATA_AGGI and sititrkc.DATA_FINE ) " +
			"and cons_ufre_tab.DATA_INIZIO < sititrkc.DATA_FINE " +
			"and  cons_ufre_tab.DATA_FINE > sititrkc.DATA_AGGI " +
			"and nvl(upper(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
			"AND cons_deco_tab.fieldname ='TIPO_DOCUMENTO' AND cons_deco_tab.tablename='CONS_ATTI_TAB' AND cons_deco_tab.code = cons_ufre_tab.tipo_documento " +
			"order by TO_NUMBER(foglio),numero, DATA_FINE_VAL_DATE DESC, DATA_FINE_POS_date DESC";
			
			log.debug("mCaricareListaTerreniPerTitolarita-sql2["+sql2+"]");
			log.debug("param["+titolarita+"]");
			
			this.initialize();
			this.setString(1,titolarita);

			prepareStatement(sql2);
			rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs2.next()){
				Terreno ter = new Terreno();
				ter.setSezione(rs2.getString("SEZIONE"));
				ter.setFoglio(rs2.getString("FOGLIO"));
				ter.setNumero(rs2.getString("NUMERO"));
				ter.setSubalterno(rs2.getString("SUBALTERNO"));
				/*ter.setPartita(rs2.getString("PARTITA"));
				ter.setSuperficie(rs2.getString("SUPERFICIE"));
				ter.setQualita(rs2.getString("QUALITA"));
				ter.setClasse(rs2.getString("CLASSE"));
				ter.setRDominicale(tornaValoreRS(rs2,"REDDITO_DOMINICALE","EURO"));
				ter.setRAgrario(tornaValoreRS(rs2,"REDDITO_AGRARIO","EURO"));*/
				ter.setDataFine(rs2.getString("DATA_FINE_VAL"));
				ter.setDataInizio(rs2.getString("DATA_INIZIO_VAL"));
				ter.setDataFinePos(rs2.getString("DATA_FINE_POS"));
				
				
				String chiave = rs2.getString("FK_COMUNE")+"|"+ter.getFoglio()+"|"+ter.getNumero()+"|"+ter.getSubalterno();
				chiave += "|"+ ter.getDataFine() +"|"+ter.getDataInizio();
				
				ter.setParticella(chiave);
				
				//ter.setParticella(rs2.getString("PK_PARTICELLA")+"|"+rs2.getString("DATA_INIZIO_VAL")+"|"+rs2.getString("DATA_FINE_VAL"));
				//ter.setParticella(rs2.getString("PK_PARTICELLA"));
				String titolo = rs2.getString("TITOLO");
				
				ter.setTitolo(titolo!=null ? titolo : "ALTRO");
				
				ter.setPercPoss(rs2.getBigDecimal("PERC_POSS"));
				
				String key =    ter.getSezione() +"|"+
								ter.getFoglio() +"|"+
								ter.getNumero() +"|"+
								ter.getSubalterno() +"|"+
								ter.getDataFinePos() +"|"+
								ter.getTitolo();

				if(!temp.containsKey(key)){
					vct.add(ter);
					temp.put(key, ter);
				}
			}

			conn.close();
			ht.put("LISTA_TERRENI",vct);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = titolarita;
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
		} finally {
			DbUtils.close(rs);
			DbUtils.close(rs2);
			DbUtils.close(conn);
		}
		
	}

	public Hashtable mCaricareListaTerreni2(Vector listaPar, TerreniFinder finder) throws Exception{
			// String foglio,String numero,String subalterno,String partita,String comune,String ettari,String are, String centiare,String superficie, String qualita,String classe,String Rdominicale,String Ragrario)
			// carico la lista dei comuni e le metto in un hashtable
			Hashtable ht = new Hashtable();
			Vector vct = new Vector();

			String conteggio = "";
			long conteggione = 0;

			// faccio la connessione al db

			try {
				this.setDatasource(JNDI_CATCOSPOLETO);
				Connection conn = this.getConnection();
				int indice = 1;
				java.sql.ResultSet rs = null;

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

					// il primo passaggio esegue la select count
					if (finder.getKeyStr().equals("")){
						sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					}
					else{
						sql = sql + " and sititrkc.TRKC_ID in (" +finder.getKeyStr() + ")" ;
					}

					long limInf, limSup;
					limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
					limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				
					if(i==0) 
						sql += "))";
					else 
						sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

					prepareStatement(sql);
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					if (i ==1) {
						while (rs.next()){
							Terreno ter = new Terreno();
							
							ter.setSezione(rs.getString("SEZIONE"));
							ter.setFoglio(rs.getString("FOGLIO"));
							ter.setNumero(rs.getString("PARTICELLA"));
							ter.setSubalterno(rs.getString("SUBALTERNO"));
							//ter.setPartita(rs.getString("PARTITA"));
							ter.setComune(rs.getString("COMUNE"));
						  /*ter.setSuperficie(rs.getString("SUPERFICIE"));
							ter.setQualita(rs.getString("QUALITA"));
							ter.setClasse(rs.getString("CLASSE"));
							ter.setRDominicale(tornaValoreRS(rs,"REDDITO_DOMINICALE","EURO"));
							ter.setRAgrario(tornaValoreRS(rs,"REDDITO_AGRARIO","EURO"));*/
							//ter.setParticella(rs.getString("PK_PARTICELLA")+"|"+rs.getString("DATA_INIZIO_VAL")+"|"+rs.getString("DATA_FINE_VAL"));
							ter.setCodente(rs.getString("FK_COMUNE"));
							/*ter.setDataFine(rs.getString("DATA_FINE_VAL"));
							ter.setDataInizio(rs.getString("DATA_INIZIO_VAL"));*/
							
							this.setDateValiditaTerreno(ter);
							
							String key = rs.getString("FK_COMUNE")+"|"+rs.getString("FOGLIO")+"|"+rs.getString("PARTICELLA")+"|"+rs.getString("SUBALTERNO");
							key += "|"+ ter.getDataFine() +"|"+ter.getDataInizio();
							
							ter.setParticella(key);
							
							GenericTuples.T2<String,String> coord = null;
							try {
								coord = getLatitudeLongitude(ter.getFoglio(), ter.getNumero(), ter.getCodente());
							} catch (Exception e) {
							}
							if (coord!=null) {
								ter.setLatitudine(coord.firstObj);
								ter.setLongitudine(coord.secondObj);
							}
							
							if (ter.getDataFine() != null && ter.getDataFine().equals(this.DATA_MAX))
								ter.setEvidenza(true);

							vct.add(ter);
						}
					}
				    else{
						if (rs.next()){
							conteggio = rs.getString("conteggio");
						}
					}
				}
				conn.close();
				ht.put("LISTA_TERRENI",vct);
				finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
				// pagine totali
				finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
				finder.setTotaleRecord(conteggione);
				finder.setRighePerPagina(RIGHE_PER_PAGINA);
				
				ht.put(SOLO_ATT, new Boolean(soloAtt));
				
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
		}

	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception{
		String sql = "";
		Vector listaParNew= new Vector();
		soloAtt = false;
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if ("SOLO_ATT".equalsIgnoreCase(el.getAttributeName())) {
				soloAtt = "Y".equalsIgnoreCase(el.getValore());
			} else {
				listaParNew.add(el);				
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParNew);
		
		if (soloAtt) {
			sql = sql + " and DATA_FINE = to_date('31/12/9999','dd/MM/yyyy') ";
		}
		
		return sql;
	}

	public Hashtable mCaricareDettaglioTerrenoGauss(String particella) throws Exception{
					// carico la lista dei terreni e la metto in un hashtable
					Hashtable ht = new Hashtable();

					// faccio la connessione al db
					try {
						this.setDatasource(JNDI_SITISPOLETO); 
						Connection conn = this.getConnection();

						this.initialize();
						String sql = SQL_SELECT_TERRENI_PER_GAUSS;

						int indice = 1;
						this.setString(indice,particella);
						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						Terreno ter = new Terreno();
						if (rs.next()){
							ter.setSezione(rs.getString("SEZIONE"));
							ter.setFoglio(rs.getString("FOGLIO"));
							ter.setNumero(rs.getString("NUMERO"));
							ter.setSubalterno(rs.getString("SUBALTERNO"));
							ter.setPartita(rs.getString("PARTITA"));
							ter.setComune(rs.getString("COMUNE"));
							ter.setEttari(rs.getString("ETTARI"));
							ter.setAre(rs.getString("ARE"));
							ter.setCentiare(rs.getString("CENTIARE"));
							ter.setSuperficie(rs.getString("SUPERFICIE"));
							ter.setQualita(rs.getString("QUALITA"));
							ter.setClasse(rs.getString("CLASSE"));
							ter.setRDominicale(tornaValoreRS(rs,"REDDITO_DOMINICALE","EURO"));
							ter.setRAgrario(tornaValoreRS(rs,"REDDITO_AGRARIO","EURO"));
							

						}
						ht.put("TERRENO",ter);
						conn.close();
						
						/*INIZIO AUDIT*/
						try {
							Object[] arguments = new Object[1];
							arguments[0] = particella;
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
			}
	
	private void setDateValiditaTerreno(Terreno terr) throws Exception{
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setCodEnte(terr.getCodente());
		rc.setFoglio(terr.getFoglio());
		rc.setParticella(terr.getNumero());
		rc.setSub(terr.getSubalterno());
		
		Date[] minmax = catastoService.getMinMaxDateValTerreno(rc);
		
		terr.setDataInizio(minmax[0]!=null ? SDF.format(minmax[0]) : "01/01/0001");
		terr.setDataFine(minmax[1]!=null ? SDF.format(minmax[1]) : "31/12/9999");
		terr.setStato(terr.getDataFine());
		
	}

}

