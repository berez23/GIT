/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.logic;

import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TributiOggettiTARSULogic extends EscLogic{
	private String appoggioDataSource;
			public TributiOggettiTARSULogic(EnvUtente eu) {
						super(eu);
						appoggioDataSource=eu.getDataSource();
					}
		public final static String TARSU_DOCFA_COLLEGATI = "TARSU_DOCFA_COLLEGATI@TributiOggettiTARSULogic";
		private final static String SQL_SELECT_LISTA= "select * from (" +		
		"select ROWNUM as N,COMUNE,FK_COMUNI,PK_SEQU_OGGETTI_TARSU,FOGLIO,PARTICELLA,SUBALTERNO,SUPERFICIE,PROVENIENZA FROM(" +
		"select ROWnum as N,tri_oggetti_tarsu.PK_SEQU_OGGETTI_TARSU, tri_oggetti_tarsu.FK_COMUNI,"+
		"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE," +
		"decode(tri_oggetti_tarsu.FOGLIO_CATASTO,null,'-',tri_oggetti_tarsu.FOGLIO_CATASTO) AS FOGLIO," +
		"decode(tri_oggetti_tarsu.PARTICELLA_CATASTO,null,'-',tri_oggetti_tarsu.PARTICELLA_CATASTO) AS PARTICELLA," +
		"decode(tri_oggetti_tarsu.SUBALTERNO_CATASTO,null,'-',tri_oggetti_tarsu.SUBALTERNO_CATASTO) AS SUBALTERNO," +
		"decode(tri_oggetti_tarsu.SUPERFICIE_TOTALE,null,'-',tri_oggetti_tarsu.SUPERFICIE_TOTALE) AS SUPERFICIE, " +
		"decode(tri_oggetti_tarsu.PROVENIENZA,null,'-',tri_oggetti_tarsu.PROVENIENZA) AS PROVENIENZA " +
		"from tri_oggetti_tarsu, ewg_tab_comuni , sit_t_civici c, sit_t_vie v , sit_t_oggetti_tarsu ot where 1=? and tri_oggetti_tarsu.FK_COMUNI = ewg_tab_comuni.UK_BELFIORE "+
		"and c.PK_CIVICO (+) = ot.FK_T_CIVICI AND ot.PK_ID_TARSU (+) =TRI_OGGETTI_TARSU.PK_SEQU_OGGETTI_TARSU AND C.FK_VI_COD = V.PK_VIACOD ";
		

		private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  from tri_oggetti_tarsu, ewg_tab_comuni,sit_t_civici c, sit_t_vie v , sit_t_oggetti_tarsu ot where ewg_tab_comuni.UK_BELFIORE = tri_oggetti_tarsu.FK_COMUNI and c.PK_CIVICO (+) = ot.FK_T_CIVICI AND ot.PK_ID_TARSU (+) =TRI_OGGETTI_TARSU.PK_SEQU_OGGETTI_TARSU AND C.FK_VI_COD = V.PK_VIACOD and 1=?" ;
		private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from tri_oggetti_tarsu, ewg_tab_comuni,sit_t_civici c, sit_t_vie v , sit_t_oggetti_tarsu ot where ewg_tab_comuni.UK_BELFIORE = tri_oggetti_tarsu.FK_COMUNI and c.PK_CIVICO (+) = ot.FK_T_CIVICI AND ot.PK_ID_TARSU (+) =TRI_OGGETTI_TARSU.PK_SEQU_OGGETTI_TARSU AND C.FK_VI_COD = V.PK_VIACOD and 1=?" ;


		

		
	public Hashtable mCaricareDettaglioOggettiTARSU(String chiave, String pathDatiDiogene) throws Exception{
				// carico la lista dei comuni e le metto in un hashtable
				Hashtable ht = new Hashtable();
				Connection conn = null;
				// faccio la connessione al db
				try {
					conn = this.getConnection();
					this.initialize();
					String sql = "select tri_oggetti_tarsu.PK_SEQU_OGGETTI_TARSU, decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE, tri_oggetti_tarsu.FK_COMUNI, " +
						"decode(tri_oggetti_tarsu.FOGLIO_CATASTO,null,'-',tri_oggetti_tarsu.FOGLIO_CATASTO) AS FOGLIO," +
						"decode(tri_oggetti_tarsu.PARTICELLA_CATASTO,null,'-',tri_oggetti_tarsu.PARTICELLA_CATASTO) AS PARTICELLA, " +
						"decode(tri_oggetti_tarsu.SUBALTERNO_CATASTO,null,'-',tri_oggetti_tarsu.SUBALTERNO_CATASTO) AS SUBALTERNO," +
						"decode(tri_oggetti_tarsu.SUPERFICIE_TOTALE,null,'-',tri_oggetti_tarsu.SUPERFICIE_TOTALE) AS SUPERFICIE, " +
						"decode(tri_oggetti_tarsu.SUP_GARAGE,null,'-',tri_oggetti_tarsu.SUP_GARAGE) AS GARAGE, " +
						"decode(tri_oggetti_tarsu.SUP_FONDO,null,'-',tri_oggetti_tarsu.SUP_FONDO) AS FONDO, " +
						"decode(tri_oggetti_tarsu.SUP_SOFFITTA,null,'-',tri_oggetti_tarsu.SUP_SOFFITTA) AS SOFFITTA," +
						"decode(tri_oggetti_tarsu.SUP_COMMERCIALE,null,'-',tri_oggetti_tarsu.SUP_COMMERCIALE) AS COMMERCIALE ," +
						"decode(tri_oggetti_tarsu.SUP_ARTIGIANALE,null,'-',tri_oggetti_tarsu.SUP_ARTIGIANALE) AS ARTIGIANALE, " +
						"decode(tri_oggetti_tarsu.SUP_PRODUTTIVO,null,'-',tri_oggetti_tarsu.SUP_PRODUTTIVO) AS PRODUTTIVO, " +
						"decode(tri_oggetti_tarsu.SUP_SERVIZI,null,'-',tri_oggetti_tarsu.SUP_SERVIZI) AS SERVIZI, " +
						"decode(tri_oggetti_tarsu.SUP_ACCESSORI,null,'-',tri_oggetti_tarsu.SUP_ACCESSORI) AS ACCESSORI, " +
						"decode(tri_oggetti_tarsu.SUP_ABITAZIONE,null,'-',tri_oggetti_tarsu.SUP_ABITAZIONE) AS ABITAZIONE, " +
						"decode(tri_oggetti_tarsu.PROVENIENZA,null,'-',tri_oggetti_tarsu.PROVENIENZA) AS PROVENIENZA, " +
						"decode(tri_oggetti_tarsu.FK_CONTRIBUENTE,null,'-',tri_oggetti_tarsu.FK_CONTRIBUENTE) AS FK_CONTRIBUENTE, " +
						"decode(tri_oggetti_tarsu.sup_vani,null,'-',tri_oggetti_tarsu.sup_vani) AS sup_vani, " +
						"decode(tri_oggetti_tarsu.DESCR_INDIRIZZO,null,'-',tri_oggetti_tarsu.DESCR_INDIRIZZO) AS DESCR_INDIRIZZO " +
						"from tri_oggetti_tarsu, ewg_tab_comuni " +
						"where tri_oggetti_tarsu.PK_SEQU_OGGETTI_TARSU = ? and ewg_tab_comuni.UK_BELFIORE = tri_oggetti_tarsu.FK_COMUNI";


					int indice = 1;
					this.setString(indice,chiave);

					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					OggettiTARSU tarsu = new OggettiTARSU();

					if (rs.next()){
						tarsu.setChiave(rs.getString("PK_SEQU_OGGETTI_TARSU"));
						tarsu.setComune(rs.getString("COMUNE"));
						tarsu.setFoglio(rs.getString("FOGLIO"));
						tarsu.setParCatastali(rs.getString("PARTICELLA"));
						tarsu.setSubalterno(rs.getString("SUBALTERNO"));
						tarsu.setGarage(rs.getString("GARAGE"));
						tarsu.setFondo(rs.getString("FONDO"));
						tarsu.setSoffitta(rs.getString("SOFFITTA"));
						tarsu.setCommerciale(rs.getString("COMMERCIALE"));
						tarsu.setArtigianale(rs.getString("ARTIGIANALE"));
						tarsu.setProduttivo(rs.getString("PRODUTTIVO"));
						tarsu.setServizi(rs.getString("SERVIZI"));
						tarsu.setAccessori(rs.getString("ACCESSORI"));
						tarsu.setAbitazione(rs.getString("ABITAZIONE"));
						tarsu.setSupTotale(rs.getString("SUPERFICIE"));
						tarsu.setProvenienza(rs.getString("PROVENIENZA"));
						tarsu.setCodSoggetto(rs.getString("FK_CONTRIBUENTE"));
						tarsu.setIndirizzo(rs.getString("DESCR_INDIRIZZO"));
						tarsu.setSupVani(rs.getString("sup_vani"));
						tarsu.setCodEnte(rs.getString("FK_COMUNI"));
						
						

					}
					ht.put("TARSU",tarsu);


					if
					(
							(tarsu.getSupVani() != null && !tarsu.getSupVani().equals("-")) &&
							(tarsu.getFoglio() != null && !tarsu.getFoglio().equals("-")) &&
							(tarsu.getSupVani() != null && !tarsu.getSupVani().equals("-"))

					)
					{
						CatastoImmobiliLogic clogic = new CatastoImmobiliLogic(this.envUtente);	
						
						Immobile imm = new Immobile();
						imm.setFoglio(tarsu.getFoglio());
						imm.setNumero(tarsu.getParCatastali());
						imm.setUnimm(tarsu.getSubalterno());
						
						ArrayList dettaglioVani = clogic.getSuperficiComma340(imm);
						ht.put("TARSU_DETTAGLIO_VANI",dettaglioVani);
					}

					
					// docfa collegati
					if
					(
							(tarsu.getFoglio() != null && !tarsu.getFoglio().equals("") && !tarsu.getFoglio().equals("-")) &&
							(tarsu.getParCatastali() != null && !tarsu.getParCatastali().equals("") && !tarsu.getParCatastali().equals("-")) &&
							(tarsu.getSubalterno() != null && !tarsu.getSubalterno().equals("") && !tarsu.getSubalterno().equals("-"))

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
						this.setString(2,tarsu.getParCatastali());
						this.setString(3,tarsu.getSubalterno());
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
						ht.put(TARSU_DOCFA_COLLEGATI,docfaCollegati);
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

	public Hashtable mCaricareListaOggettiTARSUPerTitolarita(String titolarita) throws Exception{

					Hashtable ht = new Hashtable();
					Vector vct = new Vector();
					String sql = "";
					String conteggio = "";
					long conteggione = 0;
					Connection conn = null;
					// faccio la connessione al db
			try {
						conn = this.getConnection();
							 sql = "select tri_oggetti_tarsu.PK_SEQU_OGGETTI_TARSU, decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE, tri_oggetti_tarsu.FK_COMUNI, " +
							 		"decode(tri_oggetti_tarsu.DESCR_CATEGORIA,null,'-',tri_oggetti_tarsu.DESCR_CATEGORIA) AS DESCR_CATEGORIA," +
									"decode(ltrim(tri_oggetti_tarsu.FOGLIO_CATASTO),null,'-',tri_oggetti_tarsu.FOGLIO_CATASTO) AS FOGLIO," +
									"decode(ltrim(tri_oggetti_tarsu.FK_PAR_CATASTALI),null,'-',tri_oggetti_tarsu.FK_PAR_CATASTALI) AS PARTICELLA, " +
									"decode(ltrim(tri_oggetti_tarsu.PARTICELLA_CATASTO),null,'-',tri_oggetti_tarsu.PARTICELLA_CATASTO) AS PARTICELLA_CATASTO, " +
									"decode(ltrim(tri_oggetti_tarsu.SUBALTERNO_CATASTO),null,'-',tri_oggetti_tarsu.SUBALTERNO_CATASTO) AS SUBALTERNO," +
									"decode(tri_oggetti_tarsu.DESCR_INDIRIZZO,null,'-',tri_oggetti_tarsu.DESCR_INDIRIZZO) AS INDIRIZZO," +
									"decode(tri_oggetti_tarsu.SUPERFICIE_TOTALE,null,'-',tri_oggetti_tarsu.SUPERFICIE_TOTALE) AS SUPERFICIE, " +
									"decode(tri_oggetti_tarsu.SUP_GARAGE,null,'-',tri_oggetti_tarsu.SUP_GARAGE) AS GARAGE, " +
									"decode(tri_oggetti_tarsu.SUP_FONDO,null,'-',tri_oggetti_tarsu.SUP_FONDO) AS FONDO, " +
									"decode(tri_oggetti_tarsu.SUP_SOFFITTA,null,'-',tri_oggetti_tarsu.SUP_SOFFITTA) AS SOFFITTA," +
									"decode(tri_oggetti_tarsu.SUP_COMMERCIALE,null,'-',tri_oggetti_tarsu.SUP_COMMERCIALE) AS COMMERCIALE ," +
									"decode(tri_oggetti_tarsu.SUP_ARTIGIANALE,null,'-',tri_oggetti_tarsu.SUP_ARTIGIANALE) AS ARTIGIANALE, " +
									"decode(tri_oggetti_tarsu.SUP_PRODUTTIVO,null,'-',tri_oggetti_tarsu.SUP_PRODUTTIVO) AS PRODUTTIVO, " +
									"decode(tri_oggetti_tarsu.SUP_SERVIZI,null,'-',tri_oggetti_tarsu.SUP_SERVIZI) AS SERVIZI, " +
									"decode(tri_oggetti_tarsu.SUP_ACCESSORI,null,'-',tri_oggetti_tarsu.SUP_ACCESSORI) AS ACCESSORI, " +
									"decode(tri_oggetti_tarsu.SUP_ABITAZIONE,null,'-',tri_oggetti_tarsu.SUP_ABITAZIONE) AS ABITAZIONE, " +
									"decode(tri_oggetti_tarsu.FK_CONTRIBUENTE,null,'-',tri_oggetti_tarsu.FK_CONTRIBUENTE) AS CONTRIBUENTE, " +
									"decode(tri_oggetti_tarsu.PROVENIENZA,null,'-',tri_oggetti_tarsu.PROVENIENZA) AS PROVENIENZA " +
									"from tri_oggetti_tarsu, ewg_tab_comuni, tri_contribuenti " +
									"where (tri_oggetti_tarsu.FK_CONTRIBUENTE = tri_contribuenti.CODICE_CONTRIBUENTE) " +
									//"where (tri_oggetti_tarsu.FK_CONTRIBUENTE = tri_contribuenti.UK_CODI_CONTRIBUENTE) " +
									"and tri_oggetti_tarsu.PROVENIENZA = tri_contribuenti.PROVENIENZA " +
									"and (tri_contribuenti.UK_CODI_CONTRIBUENTE = ?) " +
									"and (ewg_tab_comuni.UK_BELFIORE = tri_oggetti_tarsu.FK_COMUNI) " +
									"and tri_contribuenti.CODENTE = tri_oggetti_tarsu.codente";

							this.initialize();
							this.setString(1,titolarita);


							prepareStatement(sql);
							java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							while (rs.next()){
								OggettiTARSU tarsu = new OggettiTARSU();
								tarsu.setChiave(rs.getString("PK_SEQU_OGGETTI_TARSU"));
								tarsu.setComune(rs.getString("COMUNE"));
								tarsu.setFoglio(rs.getString("FOGLIO"));
								tarsu.setParCatastali(rs.getString("PARTICELLA"));
								tarsu.setSubalterno(rs.getString("SUBALTERNO"));
								tarsu.setIndirizzo(rs.getString("INDIRIZZO"));
								tarsu.setGarage(rs.getString("GARAGE"));
								tarsu.setFondo(rs.getString("FONDO"));
								tarsu.setSoffitta(rs.getString("SOFFITTA"));
								tarsu.setCommerciale(rs.getString("COMMERCIALE"));
								tarsu.setArtigianale(rs.getString("ARTIGIANALE"));
								tarsu.setProduttivo(rs.getString("PRODUTTIVO"));
								tarsu.setServizi(rs.getString("SERVIZI"));
								tarsu.setAccessori(rs.getString("ACCESSORI"));
								tarsu.setAbitazione(rs.getString("ABITAZIONE"));
								tarsu.setSupTotale(rs.getString("SUPERFICIE"));
								tarsu.setContribuente(rs.getString("CONTRIBUENTE"));
								tarsu.setDescrCategoria(rs.getString("DESCR_CATEGORIA"));
								tarsu.setParticella(rs.getString("PARTICELLA_CATASTO"));
								tarsu.setProvenienza(rs.getString("PROVENIENZA"));
								vct.add(tarsu);
							}

						ht.put("LISTA_TARSU",vct);
						
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
					log.error(e.getMessage(),e);
					throw e;
				}
				finally
				{
					if (conn != null && !conn.isClosed())
						conn.close();
				}
		}





	public Hashtable mCaricareListaOggettiTARSU(Vector listaPar,OggettiTARSUFinder finder) throws Exception{

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
			java.sql.ResultSet rs = executeQuery(conn);
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

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//if (!finder.getKeyStr().equals("")){
					sql = sql + " and PK_SEQU_OGGETTI_TARSU in (" +finder.getKeyStr() + ")" ;
				}
				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + " order by ewg_tab_comuni.DESCRIZIONE,FOGLIO_CATASTO,PARTICELLA_CATASTO,SUBALTERNO_CATASTO";
				if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						OggettiTARSU tarsu = new OggettiTARSU();
						tarsu.setChiave(rs.getString("PK_SEQU_OGGETTI_TARSU"));
						tarsu.setComune(rs.getString("COMUNE"));
						tarsu.setFoglio(rs.getString("FOGLIO"));
						tarsu.setParCatastali(rs.getString("PARTICELLA"));
						tarsu.setSubalterno(rs.getString("SUBALTERNO"));
						tarsu.setSupTotale(rs.getString("SUPERFICIE"));
						tarsu.setProvenienza(rs.getString("PROVENIENZA"));
						tarsu.setCodEnte(rs.getString("FK_COMUNI"));

						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(tarsu.getFoglio(), Utils.fillUpZeroInFront(tarsu.getParCatastali(),5),tarsu.getCodEnte());
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
			ht.put("LISTA_TARSU",vct);
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
