/*
 * Created on 12-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.eiv.JavaBeanGlobalVar;
import it.escsolution.escwebgis.catasto.bean.Gauss;
import it.escsolution.escwebgis.catasto.bean.GaussFinder;
import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.indagineCivico.bean.Titolare;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.utils.GenericTuples;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoGaussLogic extends EscLogic{
	
	private static final String ANNOTAZIONE_AUTO = "CREATO AUTOMATICAMENTE IN FASE DI IMPORTAZIONE UIU";
	
	public CatastoGaussLogic(EnvUtente eu) {
				super(eu);
			}

	private final static String SQL_SELECT_LISTA= "select * from (" +
		"select ROWNUM as N,COMUNE,SEZIONE,FOGLIO,PARTICELLA,LAYER,CHIAVE,FK_COMUNE,DATA_FINE_VAL FROM( "+
		"select ROWNUM as N," +
		"FK_COMUNE, " +
		"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE," +
		"decode(cat_particelle_gauss.FOGLIO,null,'-',cat_particelle_gauss.FOGLIO) AS FOGLIO," +
		"decode(cat_particelle_gauss.PARTICELLA,null,'-',cat_particelle_gauss.PARTICELLA) AS PARTICELLA,"+
		"decode(cat_particelle_gauss.LAYER,null,'-',cat_particelle_gauss.LAYER) AS LAYER,"+
		"decode(cat_particelle_gauss.PK_PARTICELLE,null,'-',cat_particelle_gauss.PK_PARTICELLE) AS CHIAVE,"+
		"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE," +
		"to_char(cat_particelle_gauss.DATA_FINE_VAL, 'dd/mm/yyyy')as DATA_FINE_VAL " +
		" from CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss, ewg_tab_comuni, siticomu "+
		"where cat_particelle_gauss.COMUNE = siticomu.COD_NAZIONALE " +
		"and siticomu.CODI_FISC_LUNA = ewg_tab_comuni.UK_BELFIORE " + 
		"and 1=?  ";

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss, ewg_tab_comuni, siticomu " +
		"where cat_particelle_gauss.COMUNE = siticomu.COD_NAZIONALE and siticomu.CODI_FISC_LUNA = ewg_tab_comuni.UK_BELFIORE and 1=?" ;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss, ewg_tab_comuni, siticomu " +
		"where cat_particelle_gauss.COMUNE = siticomu.COD_NAZIONALE and siticomu.CODI_FISC_LUNA = ewg_tab_comuni.UK_BELFIORE and 1=?";

	private final static  String SQL_TITOLARI="SELECT CSV.CUAA, CSV.RAGI_SOCI FROM CONS_CSUI_TAB CCT, CONS_SOGG_VIW CSV, SITICOMU C WHERE C.COD_NAZIONALE = ? AND CCT.COD_NAZIONALE = C.COD_NAZIONALE AND CCT.FOGLIO = ? AND CCT.PARTICELLA = LPAD (?, 5, '0') AND CCT.UNIMM= ? AND CCT.PK_CUAA=CSV.PK_CUAA AND DATA_FINE_VAL > DATA_INIZIO_VAL AND DATA_FINE >= SYSDATE UNION ALL SELECT CSV.CUAA, CSV.RAGI_SOCI FROM CONS_URUI_TAB CCT, CONS_SOGG_VIW CSV, SITICOMU C WHERE C.CODI_FISC_LUNA = ? AND CCT.COD_NAZIONALE = C.COD_NAZIONALE AND CCT.FOGLIO = ? AND CCT.PARTICELLA = LPAD (?, 5, '0') AND CCT.UNIMM= ? AND CCT.PK_CUAA=CSV.PK_CUAA AND DATA_FINE_VAL > DATA_INIZIO_VAL AND DATA_FINE >= SYSDATE";;



	public Hashtable mCaricareListaGauss(Vector listaPar, GaussFinder finder) throws Exception{
				// carico la lista dei civici e la metto in un hashtable
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
					/*
					sql = SQL_SELECT_COUNT_ALL;
					int indice = 1;
					this.initialize();
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
							sql = sql + " and cat_particelle_gauss.PK_PARTICELLE in (" +finder.getKeyStr() + ")" ;

						}


						long limInf, limSup;
						limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
						limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
						//sql = sql + " and cat_particelle_gauss.COMUNE='F205'";
						sql = sql + " order by COMUNE,FOGLIO,PARTICELLA";
						if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

						prepareStatement(sql) ;
						rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						if (i ==1) {
							while (rs.next()){
								//campi della lista
								Gauss gauss = new Gauss();
								gauss.setChiave(rs.getString("CHIAVE"));
								gauss.setSezione(rs.getString("SEZIONE"));
								gauss.setComune(rs.getString("COMUNE"));
								gauss.setFkComune(rs.getString("FK_COMUNE"));
								gauss.setFoglio(rs.getString("FOGLIO"));
								gauss.setParticella(rs.getString("PARTICELLA"));
								gauss.setLayer(rs.getString("LAYER"));
								gauss.setDataFine(rs.getString("DATA_FINE_VAL"));
								
								GenericTuples.T2<String,String> coord = null;
								try {
									coord = getLatitudeLongitude(gauss.getFoglio(), gauss.getParticella(), gauss.getFkComune());
								} catch (Exception e) {
								}
								if (coord!=null) {
									gauss.setLatitudine(coord.firstObj);
									gauss.setLongitudine(coord.secondObj);
								}
								
								vct.add(gauss);
							}
						}
						else{
							if (rs.next()){
								conteggio = rs.getString("conteggio");
							}
						}
					}

					ht.put("LISTA_GAUSS",vct);
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

	public JavaBeanGlobalVar mCaricareDatiGrafici(String chiave) throws Exception{

		Connection conn = null;
		try {
			conn = this.getConnection();
			this.initialize();
			String sql = "select Xcentroid, ycentroid, fwidth, fheight " +
			" from cat_particelle_gauss" +
			" where cat_particelle_gauss.PK_PARTICELLE = ?";


			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			JavaBeanGlobalVar beanGlobale = null;
			if (rs.next()){
				beanGlobale = new JavaBeanGlobalVar(this.getEnvUtente());
				beanGlobale.setXCentroid(rs.getDouble("Xcentroid"));
				beanGlobale.setYCentroid(rs.getDouble("Ycentroid"));
				beanGlobale.setFWidth(rs.getDouble("fwidth"));
				beanGlobale.setFHeight(rs.getDouble("fheight"));
			}
			return beanGlobale;

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



	public Hashtable mCaricareDettaglioGauss(String chiave, String layer) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			this.setDatasource(JNDI_CATCOSPOLETO);
			conn = this.getConnection();
			this.initialize();
			String sql = "select cat_particelle_gauss.PK_PARTICELLE AS PARTICELLA_TERRENO, " +
			"decode(siticomu.ID_SEZC,null,'-',siticomu.ID_SEZC) AS SEZIONE, " +
			"decode(cat_particelle_gauss.FOGLIO, null, '-', cat_particelle_gauss.FOGLIO) as FOGLIO, " +
			"decode(cat_particelle_gauss.PARTICELLA, null, '-' , cat_particelle_gauss.PARTICELLA) as PARTICELLA, " +
			"decode(cat_particelle_gauss.PK_PARTICELLE, null, '-' , cat_particelle_gauss.PK_PARTICELLE) as CHIAVE, " +
			"decode(cat_particelle_gauss.FK_PAR_CATASTALI, null, '-' , cat_particelle_gauss.FK_PAR_CATASTALI) as PAR_CATASTALI, " +
			"decode(cat_particelle_gauss.AREA, null, '-' , cat_particelle_gauss.AREA) as AREA, " +
			"decode(cat_particelle_gauss.PERIMETER, null, '-' , cat_particelle_gauss.PERIMETER) as PERIMETRO, " +
			"decode(cat_particelle_gauss.LAYER, null, '-' , cat_particelle_gauss.LAYER) as LAYER, " +
			"decode(cat_particelle_gauss.FK_COMUNE, null, '-' , cat_particelle_gauss.FK_COMUNE) as FK_COMUNE, " +
			"decode(cat_particelle_gauss.SUB, null, '-' , cat_particelle_gauss.SUB) as SUBALTERNO, " +
			"decode(siticomu.NOME,null,'-',siticomu.NOME) AS COMUNE, " +
			"nvl(to_char(cat_particelle_gauss.DATA_FINE_VAL, 'dd/mm/yyyy'),'31/12/9999') as DATA_FINE_VAL " +
			"from CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss, siticomu " +
			"where siticomu.COD_NAZIONALE=cat_particelle_gauss.COMUNE " +
			"and cat_particelle_gauss.PK_PARTICELLE = ?";
			
			if (layer!=null) {
				sql += " and cat_particelle_gauss.layer="+"'"+layer+"'";
			}

			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Gauss gauss = new Gauss();

			if (rs.next()){
				gauss.setChiave(rs.getString("CHIAVE"));
				gauss.setComune(rs.getString("COMUNE"));
				gauss.setSezione(rs.getString("SEZIONE"));
				gauss.setFoglio(rs.getString("FOGLIO"));
				gauss.setParticella(rs.getString("PARTICELLA"));
				gauss.setSubalterno(rs.getString("SUBALTERNO"));
				gauss.setArea(rs.getString("AREA"));
				gauss.setPerimetro(rs.getString("PERIMETRO"));
				gauss.setLayer(rs.getString("LAYER"));
				gauss.setParticellaCatasto(rs.getString("PAR_CATASTALI"));
				gauss.setParticellaTerreno(rs.getString("PARTICELLA_TERRENO"));
				gauss.setFkComune(rs.getString("FK_COMUNE"));
				gauss.setDataFine(rs.getString("DATA_FINE_VAL"));
			}

			ht.put("GAUSS",gauss);

			//verifico se è un terreno o un fabbricato
			if (gauss.getLayer().equals("PARTICELLE")){
				
/*				//recupero la chiave per la visualizzazione del dettaglio della particella-terreno
				String sqlTerr ="select sititrkc.TRKC_ID as CHIAVE,to_char(sititrkc.DATA_AGGI,'dd/mm/yyyy') as DATA_INIZIO,to_char(sititrkc.DATA_FINE,'dd/mm/yyyy') as DATA_FINE " +
				"from CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss,sititrkc,siticomu " +
				//"where  sititrkc.DATA_FINE = TO_DATE ('99991231', 'YYYYMMDD') " +
				//"where siticomu.CODI_FISC_LUNA = cat_particelle_gauss.FK_COMUNE " +
				//per problemi con catasto multisezione, sostituito da: (Filippo Mazzini 26.01.12)
				"where (siticomu.CODI_FISC_LUNA = cat_particelle_gauss.FK_COMUNE " +
				"or siticomu.COD_NAZIONALE = cat_particelle_gauss.FK_COMUNE) " +
				//fine sostituito Filippo Mazzini 26.01.12
				"and sititrkc.COD_NAZIONALE = siticomu.COD_NAZIONALE " +
				"and sititrkc.FOGLIO = cat_particelle_gauss.FOGLIO " +
				"and sititrkc.PARTICELLA = cat_particelle_gauss.PARTICELLA " +
				"and sititrkc.SUB = cat_particelle_gauss.SUB " +
				"and ( cat_particelle_gauss.DATA_FINE_VAL between sititrkc.DATA_AGGI and sititrkc.DATA_FINE " +
				"or cat_particelle_gauss.DATA_INIZIO_VAL between sititrkc.DATA_AGGI and sititrkc.DATA_FINE ) " +
				"and cat_particelle_gauss.LAYER = 'PARTICELLE' " +
				"and cat_particelle_gauss.FK_COMUNE = ? " +
				"and cat_particelle_gauss.FOGLIO = ? " +
				"and cat_particelle_gauss.PARTICELLA = ? " +
				"and cat_particelle_gauss.SUB = ? " +
				"and NVL(UPPER(sititrkc.ANNOTAZIONI), '-') <> '" + ANNOTAZIONE_AUTO + "' " +
				"and NVL(TO_CHAR(cat_particelle_gauss.DATA_FINE_VAL,'DD/MM/YYYY'),'31/12/9999') = ? " ;
				

				this.initialize();

				int ind = 1;
				this.setString(ind,gauss.getFkComune());
				this.setString(++ind,gauss.getFoglio());
				this.setString(++ind,gauss.getParticella());
				this.setString(++ind,gauss.getSubalterno());
				this.setString(++ind,gauss.getDataFine());

				prepareStatement(sqlTerr);
				java.sql.ResultSet rsTerr = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if(rsTerr.next()){
					gauss.setParticellaTerreno(rsTerr.getString("CHIAVE")+"|"+rsTerr.getString("DATA_INIZIO")+"|"+rsTerr.getString("DATA_FINE"));
					
				}else{
					gauss.setParticellaTerreno("");
				}*/
				
				try{
					String[] minmax = this.getDateValTerreno(gauss.getFoglio(), gauss.getParticella(), gauss.getSubalterno());
					gauss.setParticellaTerreno(gauss.getFkComune()+"|"+gauss.getFoglio()+"|"+gauss.getParticella()+"|"+gauss.getSubalterno()+"|"+minmax[1]+"|"+minmax[0]);
				}catch(Exception e){
					log.error("mCaricareDettaglioGauss - errore recuper date minmax particella ", e);
					gauss.setParticellaTerreno("");
				}
				
			}else if (gauss.getLayer().equals("FABBRICATI")){
				//recupero tutte le unimm collegate al fabbricato
				Vector listUnimm = new Vector();
				String sqlUnimm ="select distinct sitideco.DESCRIPTION,sitiuiu.COD_NAZIONALE,siticomu.ID_SEZC as SEZIONE,sitiuiu.FOGLIO," +
				"sitiuiu.PARTICELLA,sitiuiu.PARTICELLA,sitiuiu.SUB,sitiuiu.UNIMM,sitiuiu.CATEGORIA,to_char(sitiuiu.DATA_FINE_VAL,'dd/mm/yyyy')as DATA_FINE_VAL,to_char(sitiuiu.DATA_INIZIO_VAL,'dd/mm/yyyy')as DATA_INIZIO_VAL, " +
				"sitiuiu.CLASSE, sitiuiu.CONSISTENZA, sitiuiu.RENDITA, sitiuiu.SUP_CAT, @@@SUP_CAT_TARSU@@@ AS SUP_CAT_TARSU " +
				"from sitideco,CAT_PARTICELLE_GAUSS_2 cat_particelle_gauss,siticomu, sitiuiu @@@OUTER_JOIN@@@ " +
				//"where  sitiuiu.DATA_FINE_VAL = TO_DATE ('99991231', 'YYYYMMDD') " +
				//"where siticomu.CODI_FISC_LUNA = cat_particelle_gauss.FK_COMUNE " +
				//per problemi con catasto multisezione, sostituito da: (Filippo Mazzini 26.01.12)
				"where (siticomu.CODI_FISC_LUNA = cat_particelle_gauss.FK_COMUNE " +
				"or siticomu.COD_NAZIONALE = cat_particelle_gauss.FK_COMUNE) " +
				//fine sostituito Filippo Mazzini 26.01.12				
				"and sitiuiu.COD_NAZIONALE = siticomu.COD_NAZIONALE " +				
				"and sitiuiu.FOGLIO = cat_particelle_gauss.FOGLIO " +
				"and sitiuiu.PARTICELLA = cat_particelle_gauss.PARTICELLA " +
				"and sitiuiu.SUB = cat_particelle_gauss.SUB " +
				"and ( cat_particelle_gauss.DATA_FINE_VAL between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL " +
				"or cat_particelle_gauss.DATA_INIZIO_VAL between sitiuiu.DATA_INIZIO_VAL and sitiuiu.DATA_FINE_VAL ) " +
				"and sitiuiu.CATEGORIA = sitideco.CODE " +
				"and cat_particelle_gauss.LAYER = 'FABBRICATI' " +
				"and cat_particelle_gauss.FK_COMUNE = ? " +
				"and cat_particelle_gauss.FOGLIO = ? " +
				"and cat_particelle_gauss.PARTICELLA = ? " +
				"and cat_particelle_gauss.SUB = ? " +
				"and NVL(TO_CHAR(cat_particelle_gauss.DATA_FINE_VAL,'DD/MM/YYYY'),'31/12/9999') = ? " +
				"order by sitiuiu.COD_NAZIONALE,sitiuiu.FOGLIO,sitiuiu.PARTICELLA,sitiuiu.SUB,sitiuiu.UNIMM";

				this.initialize();

				int ind = 1;
				this.setString(ind,gauss.getFkComune());
				this.setString(++ind,gauss.getFoglio());
				this.setString(++ind,gauss.getParticella());
				this.setString(++ind,gauss.getSubalterno());
				this.setString(++ind,gauss.getDataFine());				
				
				boolean hasDatiMetrici = false;				
				PreparedStatement ctrlStm = null;
				ResultSet ctrlRs = null;
				try {
					String ctrlSql = "SELECT * FROM SITIEDI_UIU_EXT";
					ctrlStm = conn.prepareStatement(ctrlSql);
					ctrlRs = ctrlStm.executeQuery();
					ResultSetMetaData metadata = ctrlRs.getMetaData();
					int columnCount = metadata.getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						String columnName = metadata.getColumnName(i + 1); //in base 1
						if (columnName != null && columnName.equalsIgnoreCase("SUP_CAT_TARSU")) {
							hasDatiMetrici = true;
							break;
						}
					}
				} catch(Exception e) {
					log.error(e);
				}
				finally {
					try {
						if(ctrlRs != null)
							ctrlRs.close();
						if(ctrlStm != null)
							ctrlStm.close();						
					}
					catch(Exception e){}
				}

				sqlUnimm = sqlUnimm.replace("@@@SUP_CAT_TARSU@@@",  hasDatiMetrici ? "sitiedi_uiu_ext.SUP_CAT_TARSU" : "NULL");
				sqlUnimm = sqlUnimm.replace("@@@OUTER_JOIN@@@",  hasDatiMetrici ? 
																("left outer join sitiedi_uiu_ext " +
																"on sitiuiu.COD_NAZIONALE = sitiedi_uiu_ext.COD_NAZIONALE " +
																"and sitiuiu.FOGLIO = sitiedi_uiu_ext.FOGLIO " +
																"and sitiuiu.PARTICELLA = sitiedi_uiu_ext.PARTICELLA " +
																"and sitiuiu.SUB = sitiedi_uiu_ext.SUB " +
																"and sitiuiu.UNIMM = sitiedi_uiu_ext.UNIMM") : 
																"");
				
				prepareStatement(sqlUnimm);
				java.sql.ResultSet rsUnimm = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				while(rsUnimm.next()){
					Immobile unimm = new Immobile();
					//String key = rsUnimm.getString("COD_NAZIONALE")+"|"+rsUnimm.getString("FOGLIO")+"|"+rsUnimm.getString("PARTICELLA")+"|"+rsUnimm.getString("SUB")+"|"+rsUnimm.getString("UNIMM");
					String key = rsUnimm.getString("COD_NAZIONALE")+"|"+rsUnimm.getString("FOGLIO")+"|"+rsUnimm.getString("PARTICELLA")+"|"+rsUnimm.getString("SUB")+"|"+rsUnimm.getString("UNIMM")+"|"+rsUnimm.getString("DATA_FINE_VAL")+"|"+rsUnimm.getString("DATA_INIZIO_VAL");
					unimm.setChiave(key);
					unimm.setComune(rsUnimm.getString("COD_NAZIONALE"));
					unimm.setSezione(rsUnimm.getString("SEZIONE"));
					unimm.setFoglio(rsUnimm.getString("FOGLIO"));
					unimm.setParticella(rsUnimm.getString("PARTICELLA"));
					unimm.setNumero(rsUnimm.getString("PARTICELLA"));
					unimm.setUnimm(rsUnimm.getString("UNIMM"));
					unimm.setSubalterno(rsUnimm.getString("SUB"));
					unimm.setCategoria(rsUnimm.getString("DESCRIPTION"));
					unimm.setCodCategoria(rsUnimm.getString("CATEGORIA"));
					unimm.setDataInizioVal(rsUnimm.getString("DATA_INIZIO_VAL"));
					unimm.setDataFineVal(rsUnimm.getString("DATA_FINE_VAL"));
					unimm.setClasse(rsUnimm.getString("CLASSE"));
					unimm.setVani(formattaDecimale(rsUnimm.getBigDecimal("CONSISTENZA"), 2, false));
					unimm.setRendita(formattaDecimale(rsUnimm.getBigDecimal("RENDITA"), 3, false));
					unimm.setSuperficie(formattaDecimale(rsUnimm.getBigDecimal("SUP_CAT"), 2, false));
					unimm.getDatiMetrici().setSupCatTarsu(formattaDecimale(rsUnimm.getBigDecimal("SUP_CAT_TARSU"), 2, !hasDatiMetrici));
					
					String listaTitolari="";
					
					ArrayList<Titolare> listaTit = recuperaTitolari(conn, unimm.getFoglio(), unimm.getParticella(), (unimm.getUnimm()!= null ? unimm.getUnimm(): unimm.getSubalterno()), unimm.getComune());
					
					if (listaTit.size() > 0){
						for(int i=0; i<listaTit.size() ;i++) {
							Titolare tit= listaTit.get(i);
							if (i != 0){
								listaTitolari += "<br>&nbsp;";
								
							}
							if (tit.getDenom()!= null && !tit.getDenom().equals(""))
								listaTitolari += tit.getDenom();
							else
								listaTitolari="-";
					
					
						}
						unimm.setListaTitolari(listaTitolari);
						
					} else {
						unimm.setListaTitolari("-");
					}
					listUnimm.add(unimm);
				}

				ht.put("UNIMM",listUnimm);

				
				
				// lettura viario
				PreparedStatement stm2 =null;
				ResultSet rs2 =null;
				try
				{
					try {
						sql = "SELECT DISTINCT  t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
						"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
						"          WHERE c.cod_nazionale = ? " +
						"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
						"            AND ID.sez = c.sezione_censuaria " +
						"            AND ID.foglio = LPAD (?, 4, '0') " +
						"            AND ID.mappale = LPAD (?, 5, '0') " +
						"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
						"            AND ind.sezione = ID.sezione " +
						"            AND ind.id_imm = ID.id_imm " +
						"            AND ind.progressivo = ID.progressivo " +
						"            AND t.pk_id = ind.toponimo " ;
						stm2 = conn.prepareStatement(sql);
						stm2.setString(1, gauss.getFkComune());
						stm2.setString(2, gauss.getFoglio());
						stm2.setString(3, gauss.getParticella());
						rs2 =  stm2.executeQuery();
						while(rs2.next())
						{
							if(rs2.getString("indirizzo") != null)
								gauss.setIndCata(gauss.getIndCata() != null ? gauss.getIndCata()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
						}
					} catch(Exception exp2) {
						log.error(exp2.getMessage(),exp2);
					} finally {
						if(gauss.getIndCata() == null)
							gauss.setIndCata("non trovato");
					}					
					
					try {
						stm2.cancel();
						sql = "SELECT DISTINCT S.PREFISSO || ' ' || S.NOME || ' ' || c.civico as indirizzo " +
						"                  FROM sitiuiu u, siticivi_uiu iu, siticivi c, SITIDSTR S " +
						"                 WHERE iu.pkid_uiu = u.pkid_uiu " +
						"                   AND iu.pkid_civi = c.pkid_civi " +
						"                   and c.pkid_stra=s.pkid_stra " +
						"                   AND u.foglio = ? " +
						"                   AND u.particella = lpad(?,5,'0') " ; 
						stm2 = conn.prepareStatement(sql);
						stm2.setString(1, gauss.getFoglio());
						stm2.setString(2, gauss.getParticella());
						rs2 =  stm2.executeQuery();
						while(rs2.next())
						{
							if(rs2.getString("indirizzo") != null)
								gauss.setIndViarioRif(gauss.getIndViarioRif() != null ? gauss.getIndViarioRif()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
						}
					} catch(Exception exp2) {
						log.error(exp2.getMessage(),exp2);
					} finally {
						if(gauss.getIndViarioRif() == null)
							gauss.setIndViarioRif("non trovato");
					}
					
					try {
						stm2.cancel();
						sql = "SELECT DISTINCT DECODE(SIT_T_ICI_VIA.DESCRIZIONE, NULL, SIT_T_ICI_OGGETTO.DES_IND, (DECODE(SIT_T_ICI_VIA.PREFISSO, NULL, '', '', '', SIT_T_ICI_VIA.PREFISSO || ' ') || SIT_T_ICI_VIA.DESCRIZIONE)) " +
						"|| DECODE(SIT_T_ICI_OGGETTO.NUM_CIV, NULL, '', '','', ' ' || SIT_T_ICI_OGGETTO.NUM_CIV) " +
						"|| DECODE(SIT_T_ICI_OGGETTO.ESP_CIV, NULL, '', '','', '/' || SIT_T_ICI_OGGETTO.ESP_CIV) as indirizzo " +
						"FROM SIT_T_ICI_OGGETTO, SIT_T_ICI_VIA " +
						"WHERE SIT_T_ICI_OGGETTO.ID_EXT_VIA = SIT_T_ICI_VIA.ID_EXT(+) " +
						"AND LPAD (SIT_T_ICI_OGGETTO.FOGLIO, 5, '0') = LPAD (?, 5, '0') " +
						"AND LPAD (SIT_T_ICI_OGGETTO.NUMERO, 5, '0') = LPAD (?, 5, '0')";
						stm2 = conn.prepareStatement(sql);
						stm2.setString(1, gauss.getFoglio());
						stm2.setString(2, gauss.getParticella());
						rs2 =  stm2.executeQuery();
						while(rs2.next())
						{
							if(rs2.getString("indirizzo") != null)
								gauss.setIndIci(gauss.getIndIci() != null ? gauss.getIndIci()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
						}
						
					} catch(Exception exp2) {
						log.error(exp2.getMessage(),exp2);
					} finally {
						if(gauss.getIndIci() == null)
							gauss.setIndIci("non trovato");
					}
				}
				catch(Exception exp2)
				{
					log.error(exp2.getMessage(),exp2);
				}
				finally
				{
					try
					{
						if(stm2 != null)
							stm2.close();
						if(rs2 != null)
							rs2.close();
					}
					catch(Exception non){}
				}
				
			}
			
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
	
	
	private ArrayList<Titolare> recuperaTitolari(Connection conn, String foglio, String particella, String sub, String codNazionale) throws Exception {
		ResultSet rs =null;
		String sql=null;
		ArrayList<Titolare> listaTitolari = new ArrayList<Titolare>();
		boolean closeConn = false;
		try {
			if (conn == null) {
				conn=this.getConnection();
				closeConn = true;
			}	
			sql =SQL_TITOLARI;
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, codNazionale);
			pst.setString(2, foglio.trim());
			pst.setString(3, particella.trim());
			pst.setString(4, sub.trim());
			pst.setString(5, codNazionale);
			pst.setString(6, foglio.trim());
			pst.setString(7, particella.trim());
			pst.setString(8, sub.trim());
			rs= pst.executeQuery();
			while (rs.next()){
				Titolare tit = new Titolare();
				tit.setCodFisc(rs.getString("CUAA"));
				tit.setDenom(rs.getString("RAGI_SOCI"));
				listaTitolari.add(tit);
			}
			rs.close();
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally {
			if (rs != null)
				rs.close();
			if (closeConn) {
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		}
		return listaTitolari;
	}	
    
	public String[] getDateValTerreno(String foglio, String particella, String sub) throws Exception{
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
		RicercaOggettoCatDTO rc = new RicercaOggettoCatDTO();
		rc.setEnteId(enteId);
		rc.setUserId(userId);
		rc.setSessionId(sessionId);
		rc.setFoglio(foglio);
		rc.setParticella(particella);
		rc.setSub(sub);
		
		Date[] minmax = catastoService.getMinMaxDateValTerreno(rc);
		
		String[] sminmax = new String[2];
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
		sminmax[0] = minmax[0]!=null ? SDF.format(minmax[0]) : "01/01/0001";
		sminmax[1] = minmax[1]!=null ? SDF.format(minmax[1]) : "31/12/9999";
		
		return sminmax;
	}
	
	private String formattaDecimale(BigDecimal value, int numDec, boolean datoNonPres) throws Exception {
		if (value == null) {
			return datoNonPres ? "DATO NON PRESENTE" : "-";
		}
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		df.setMaximumFractionDigits(numDec);
		return df.format(value);
	}
}
