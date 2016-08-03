/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.tributi.bean.Contribuente;
import it.escsolution.escwebgis.tributi.bean.OggettiICI;
import it.escsolution.escwebgis.tributi.bean.OggettiICIFinder;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TributiOggettiICILogic extends EscLogic{
	private String appoggioDataSource;
			public TributiOggettiICILogic(EnvUtente eu) {
						super(eu);
						appoggioDataSource=eu.getDataSource();
					}
						
	public final static String ICI_DOCFA_COLLEGATI = "ICI_DOCFA_COLLEGATI@TributiOggettiTARSULogic";
	private final static String SQL_SELECT_LISTA = "select * from (" +
		"select ROWNUM as N,COMUNE,FK_COMUNI,FOGLIO,PARTICELLA,SUBALTERNO," +
		"PK_SEQU_OGGETTI_ICI,DEN_ANNO, DEN_NUMERO," +
		"IMMD," +
		"CATEGORIA,PROVENIENZA,TIP_DEN, PARTITA " +
		" from (" +
		"select distinct tri_oggetti_ici.IMMD," +
		"tri_oggetti_ici.PK_SEQU_OGGETTI_ICI, " +
		"tri_oggetti_ici.FK_COMUNI, " +
		"tri_oggetti_ici.DEN_ANNO, " +
		"tri_oggetti_ici.DEN_NUMERO, " +
		"decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE," +
		"decode(tri_oggetti_ici.FOGLIO_CATASTO,null,'-',tri_oggetti_ici.FOGLIO_CATASTO) AS FOGLIO, " +
		"decode(tri_oggetti_ici.PARTICELLA_CATASTO, null,'-',tri_oggetti_ici.PARTICELLA_CATASTO) AS PARTICELLA," +
		"decode(tri_oggetti_ici.SUBALTERNO_CATASTO,null,'-',tri_oggetti_ici.SUBALTERNO_CATASTO) AS SUBALTERNO," +
		"decode(tri_oggetti_ici.CATEGORIA_CATASTALE,null,'-',tri_oggetti_ici.CATEGORIA_CATASTALE) AS CATEGORIA, " +
		"decode(tri_oggetti_ici.PROVENIENZA,null,'-',tri_oggetti_ici.PROVENIENZA) AS PROVENIENZA, " +
		"tri_oggetti_ici.TIP_DEN, " +
		"decode(ltrim(rtrim(tri_oggetti_ici.PAR_CATASTALI)),null,'-','','-',tri_oggetti_ici.PAR_CATASTALI) AS PARTITA " +
		"from tri_oggetti_ici, ewg_tab_comuni " +
		"where TRI_OGGETTI_ICI.fk_comuni = ewg_tab_comuni.UK_BELFIORE " +
		"and 1=?";
		;
	
	private final static String SQL_SELECT_LISTA_SOGGETTO = "select tri_oggetti_ici.PK_SEQU_OGGETTI_ICI, " +
	"DECODE (tri_contribuenti.DENOMINAZIONE, NULL, tri_contribuenti.COGNOME || ' ' ||tri_contribuenti.NOME,tri_contribuenti.DENOMINAZIONE) AS NOMINATIVO," +
	"decode(tri_oggetti_ici.PROVENIENZA,null,'-',tri_oggetti_ici.PROVENIENZA) AS PROVENIENZA, " +
	"tri_oggetti_ici.DEN_ANNO, " +
	"tri_oggetti_ici.DEN_RIFERIMENTO, " +
	"tri_oggetti_ici.DEN_NUMERO, " +
	"tri_oggetti_ici.NUM_RIGA, " +
	"tri_oggetti_ici.DEN_MESI_POSSESSO, " +
	"tri_oggetti_ici.QUOTA_POSSESSO, " +
	"decode(tri_oggetti_ici.RENDITA_CATASTALE,null,'-',tri_oggetti_ici.RENDITA_CATASTALE) AS RENDITA, " +
	"tri_oggetti_ici.DIC_POSSEDUTO, " +	
	"tri_oggetti_ici.TIP_DEN " +
	"from tri_oggetti_ici, ewg_tab_comuni, tri_contribuenti where tri_contribuenti.CODICE_CONTRIBUENTE = tri_oggetti_ici.FK_CONTRIBUENTE " + 
	"and TRI_OGGETTI_ICI.fk_comuni = ewg_tab_comuni.UK_BELFIORE and 1=? " +
	"and (TRI_OGGETTI_ICI.DATA_INIZIO_POSSESSO IS NULL OR TO_DATE(TRI_OGGETTI_ICI.DATA_INIZIO_POSSESSO, 'dd/MM/yyyy') <= TO_DATE(?, 'dd/MM/yyyy')) " +
	"and (TRI_OGGETTI_ICI.DATA_FINE_POSSESSO IS NULL OR TO_DATE(TRI_OGGETTI_ICI.DATA_FINE_POSSESSO, 'dd/MM/yyyy') >= TO_DATE(?, 'dd/MM/yyyy')) " +
	"and (tri_contribuenti.codice_fiscale = ? OR tri_contribuenti.partita_iva = ?)";
	
	
	
	private final static String SQL_SELECT_LISTA_SOGGETTO_COUNT = "SELECT COUNT (*) AS conta " +
								"FROM tri_oggetti_ici, tri_contribuenti " +
								"WHERE tri_contribuenti.codice_contribuente = tri_oggetti_ici.fk_contribuente " +
								"AND 1 = ? " +
								"AND (tri_oggetti_ici.data_inizio_possesso IS NULL " +
								"OR TO_DATE (tri_oggetti_ici.data_inizio_possesso, 'dd/MM/yyyy') <= " +
								"TO_DATE (?, 'dd/MM/yyyy'))" +
								"AND (tri_oggetti_ici.data_fine_possesso IS NULL " +
								"OR TO_DATE (tri_oggetti_ici.data_fine_possesso, 'dd/MM/yyyy') >= " +
								"TO_DATE (?, 'dd/MM/yyyy'))" +
								"AND (tri_contribuenti.codice_fiscale = ? OR tri_contribuenti.partita_iva = ?)";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio " +
								"from tri_oggetti_ici, ewg_tab_comuni " +
								"where TRI_OGGETTI_ICI.fk_comuni = ewg_tab_comuni.UK_BELFIORE " +
								"and 1=?";
	
	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio from tri_oggetti_ici, ewg_tab_comuni where TRI_OGGETTI_ICI.fk_comuni = ewg_tab_comuni.UK_BELFIORE and 1=?";

	private final static String SQL_SELECT_EVIDENZIA_IN_LISTA = "SELECT 1 " +
								"FROM TRI_OGGETTI_ICI, EWG_TAB_COMUNI " +
								"WHERE TRI_OGGETTI_ICI.FK_COMUNI = EWG_TAB_COMUNI.UK_BELFIORE " +
								"AND TRI_OGGETTI_ICI.FK_COMUNI = ? AND TRI_OGGETTI_ICI.FOGLIO_CATASTO = ? " +
								"AND TRI_OGGETTI_ICI.PARTICELLA_CATASTO = ? AND TRI_OGGETTI_ICI.SUBALTERNO_CATASTO = ? " +
								"AND DEN_ANNO = (SELECT MAX(DEN_ANNO) FROM TRI_OGGETTI_ICI ICI1 " +
								"WHERE ICI1.FK_COMUNI = TRI_OGGETTI_ICI.FK_COMUNI " +
								"AND ICI1.FOGLIO_CATASTO = TRI_OGGETTI_ICI.FOGLIO_CATASTO " +
								"AND ICI1.PARTICELLA_CATASTO = TRI_OGGETTI_ICI.PARTICELLA_CATASTO " +
								"AND ICI1.SUBALTERNO_CATASTO = TRI_OGGETTI_ICI.SUBALTERNO_CATASTO) " +
								"AND DIC_POSSEDUTO = 'SI' " +
								"AND PK_SEQU_OGGETTI_ICI = ?";

	public Hashtable mCaricareDettaglioOggettiICI(String chiave) throws Exception{
			// carico la lista dei comuni e le metto in un hashtable
			Hashtable ht = new Hashtable();
			// faccio la connessione al db
			Connection conn = null;
			OggettiICI ici = new OggettiICI();
			try {

				if(chiave!=null && !chiave.equals(""))
				{

					conn = this.getConnection();
					this.initialize();
					String sql = "select  PK_SEQU_OGGETTI_ICI,decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE, tri_oggetti_ici.FK_COMUNI, " +
						"decode(tri_oggetti_ici.FK_CONTRIBUENTE,null,'-',tri_oggetti_ici.FK_CONTRIBUENTE) AS FK_CONTRIBUENTE," +
						"decode(tri_oggetti_ici.FOGLIO_CATASTO,null,'-',tri_oggetti_ici.FOGLIO_CATASTO) AS FOGLIO," +
						"decode(tri_oggetti_ici.PARTICELLA_CATASTO,null,'-',tri_oggetti_ici.PARTICELLA_CATASTO) AS PARTICELLA, " +
						"decode(tri_oggetti_ici.SUBALTERNO_CATASTO,null,'-',tri_oggetti_ici.SUBALTERNO_CATASTO) AS SUBALTERNO," +
						"decode(tri_oggetti_ici.FK_PAR_CATASTALI,null,'-',tri_oggetti_ici.FK_PAR_CATASTALI) AS PAR_CATASTALI," +
						"decode(tri_oggetti_ici.SUPERFICIE,null,'-',tri_oggetti_ici.SUPERFICIE) AS SUPERFICIE, " +
						"decode(tri_oggetti_ici.N_VANI,null,'-',tri_oggetti_ici.N_VANI) AS VANI, " +
						"decode(tri_oggetti_ici.CATEGORIA_CATASTALE,null,'-',tri_oggetti_ici.CATEGORIA_CATASTALE) AS CATEGORIA, " +
						"decode(tri_oggetti_ici.CLASSE,null,'-',tri_oggetti_ici.CLASSE) AS CLASSE," +
						"decode(tri_oggetti_ici.RENDITA_CATASTALE,null,'-',tri_oggetti_ici.RENDITA_CATASTALE) AS RENDITA ," +
						"decode(tri_oggetti_ici.PROVENIENZA,null,'-',tri_oggetti_ici.PROVENIENZA) AS PROVENIENZA, " +
						"decode(tri_oggetti_ici.DESCR_INDIRIZZO,null,'-',tri_oggetti_ici.DESCR_INDIRIZZO) AS DESCR_INDIRIZZO, " +
						"tri_oggetti_ici.DEN_NUMERO AS DEN_NUMERO ,"+
						"tri_oggetti_ici.DEN_NUMERO AS DEN_ANNO, "+
						//"decode(tri_oggetti_ici.DATA_INIZIO_POSSESSO,null,'-',tri_oggetti_ici.DATA_INIZIO_POSSESSO) AS DATA_INIZIO, " +
						//"nvl(to_char(tri_oggetti_ici.DATA_INIZIO_POSSESSO,'dd/mm/yyyy'),'-') AS DATA_INIZIO, " +
						//"decode(tri_oggetti_ici.DATA_FINE_POSSESSO,null,'-',tri_oggetti_ici.DATA_FINE_POSSESSO) AS DATA_FINE, " +
						//"nvl(to_char(tri_oggetti_ici.DATA_FINE_POSSESSO,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
						"DECODE (tri_contribuenti.PROVENIENZA ,NULL, '-',tri_contribuenti.PROVENIENZA) AS prov_contr," +
						"DECODE (tri_contribuenti.DENOMINAZIONE, NULL, tri_contribuenti.COGNOME || ' ' ||tri_contribuenti.NOME,tri_contribuenti.DENOMINAZIONE) AS nominativo," +
						"tri_contribuenti.CODICE_CONTRIBUENTE as COD_CONTRIBUENTE," +
						"tri_oggetti_ici.DEN_ANNO, " +
						"tri_oggetti_ici.DEN_RIFERIMENTO, " +
						"tri_oggetti_ici.DEN_NUMERO, " +
						"tri_oggetti_ici.NUM_RIGA, " +
						"tri_oggetti_ici.DEN_MESI_POSSESSO, " +
						"tri_oggetti_ici.QUOTA_POSSESSO, " +
						"tri_oggetti_ici.DIC_POSSEDUTO, " +
						"tri_oggetti_ici.TIP_DEN, " +
						"tri_oggetti_ici.IMMD, " +
						"decode(ltrim(rtrim(tri_oggetti_ici.par_catastali)),null,'-','','-',tri_oggetti_ici.par_catastali) AS PARTITA, " +
						"decode(tri_oggetti_ici.ABITAZIONE_PRINCIPALE,null,'0',tri_oggetti_ici.ABITAZIONE_PRINCIPALE) AS ABITAZIONEPRINCIPALE, " +
						"tri_contribuenti.CODICE_FISCALE, " +
						"tri_contribuenti.NOME, " +
						"tri_contribuenti.COGNOME, "+
						"tri_contribuenti.DATA_NASCITA "+
						/////
						"from tri_oggetti_ici, ewg_tab_comuni,tri_contribuenti " +
						"where PK_SEQU_OGGETTI_ICI = ? ";
						
					String relazioni = " and ewg_tab_comuni.UK_BELFIORE = tri_oggetti_ici.FK_COMUNI " +
					"and tri_contribuenti.CODICE_CONTRIBUENTE = tri_oggetti_ici.FK_CONTRIBUENTE " +
					"and tri_contribuenti.CODENTE = tri_oggetti_ici.CODENT " +
					"and tri_contribuenti.provenienza = tri_oggetti_ici.provenienza ";

					sql +=relazioni;


					//int indice = 1;
					//this.setString(indice,chiave);
					this.setString(1,chiave);

					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

					

					ArrayList contrList = new ArrayList();

					if (rs.next())
					{
						ici.setChiave(rs.getString("PK_SEQU_OGGETTI_ICI"));
						//ici.setCodContribuente(rs.getString("FK_CONTRIBUENTE"));
						ici.setParCatastali(rs.getString("PAR_CATASTALI"));
						ici.setComune(rs.getString("COMUNE"));
						ici.setFoglioCatasto(rs.getString("FOGLIO"));
						ici.setParticellaCatasto(rs.getString("PARTICELLA"));
						ici.setSubalterno(rs.getString("SUBALTERNO"));
						ici.setSuperficie(rs.getString("SUPERFICIE"));
						ici.setVani(rs.getString("VANI"));
						ici.setCategoria(rs.getString("CATEGORIA"));
						ici.setClasse(rs.getString("CLASSE"));
						ici.setRendita(rs.getString("RENDITA"));
						ici.setProvenienza(rs.getString("PROVENIENZA"));
						ici.setImmd(rs.getString("IMMD"));
						ici.setIndirizzo(rs.getString("DESCR_INDIRIZZO"));
						ici.setCodEnte(rs.getString("FK_COMUNI"));
						/*
						ici.setDenAnno(new Integer(rs.getInt("DEN_ANNO")));
						ici.setDenRiferimento(new Integer(rs.getInt("DEN_RIFERIMENTO")));
						ici.setDenNumero(new Integer(rs.getInt("DEN_NUMERO")));
						ici.setDenTipo(new Integer(rs.getInt("TIP_DEN")));
						ici.setNumRiga(new Integer(rs.getInt("NUM_RIGA")));
						ici.setDenMesiPossesso(new Integer(rs.getInt("DEN_MESI_POSSESSO")));
						ici.setQuotaPossesso(super.tornaValoreRS(rs, "QUOTA_POSSESSO"));
						ici.setDicPosseduto(super.tornaValoreRS(rs, "DIC_POSSEDUTO"));
						*/
						ici.setPartita(rs.getString("PARTITA"));
						String abitazionePrincipale= "NO";
						if (rs.getString("ABITAZIONEPRINCIPALE")!= null && rs.getString("ABITAZIONEPRINCIPALE").equals("1"))
							abitazionePrincipale="SI";
						ici.setAbitazionePrincipale(abitazionePrincipale);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(ici.getFoglioCatasto(), Utils.fillUpZeroInFront(ici.getParticellaCatasto(),5),ici.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							ici.setLatitudine(coord.firstObj);
							ici.setLongitudine(coord.secondObj);
						}
						
						
						if(ici.getFoglioCatasto() != null && !ici.getFoglioCatasto().trim().equals("")
								&& ici.getParticellaCatasto() != null && !ici.getParticellaCatasto().trim().equals("") )
						{
								this.initialize();

								String sqlCercoAltriSoggetti = "select  PK_SEQU_OGGETTI_ICI, " +
								"DECODE (tri_contribuenti.PROVENIENZA ,NULL, '-',tri_contribuenti.PROVENIENZA) AS prov_contr," +
								"DECODE (tri_contribuenti.DENOMINAZIONE, NULL, tri_contribuenti.COGNOME || ' ' ||tri_contribuenti.NOME,tri_contribuenti.DENOMINAZIONE) AS nominativo," +
								"tri_contribuenti.CODICE_CONTRIBUENTE as COD_CONTRIBUENTE," +
								"tri_oggetti_ici.DEN_ANNO, " +
								"tri_oggetti_ici.DEN_RIFERIMENTO, " +
								"tri_oggetti_ici.DEN_NUMERO, " +
								"tri_oggetti_ici.NUM_RIGA, " +
								"tri_oggetti_ici.DEN_MESI_POSSESSO, " +
								"tri_oggetti_ici.QUOTA_POSSESSO, " +
								"tri_oggetti_ici.DIC_POSSEDUTO, " +
								"tri_oggetti_ici.TIP_DEN, " +
								"TO_CHAR(tri_oggetti_ici.RENDITA_CATASTALE) RENDITA, " +
								"tri_contribuenti.CODICE_FISCALE, " +
								"tri_contribuenti.PARTITA_IVA, " +
								"tri_contribuenti.NOME, " +
								"tri_contribuenti.COGNOME, "+
								"tri_contribuenti.DATA_NASCITA "+
								"from tri_oggetti_ici, ewg_tab_comuni,tri_contribuenti " +
								"where nvl(foglio_catasto,'-') = ? and nvl(particella_catasto,'-') = ? and nvl(subalterno_catasto,'-') =  nvl(?,'-') "+
								relazioni + 
								" ORDER BY DEN_ANNO DESC , DEN_NUMERO, RENDITA_CATASTALE";
		
								this.setString(1,ici.getFoglioCatasto());
								this.setString(2,ici.getParticellaCatasto());
								this.setString(3,ici.getSubalterno());
		
								prepareStatement(sqlCercoAltriSoggetti);
								java.sql.ResultSet rsSogg = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
								
								while (rsSogg.next()) {
									Contribuente contr_I = new Contribuente();
									contr_I.setCodContribuente(rsSogg.getString("COD_CONTRIBUENTE"));
									contr_I.setDenAnno(new Integer(rsSogg.getInt("DEN_ANNO")));
									contr_I.setDenRiferimento(new Integer(rsSogg.getInt("DEN_RIFERIMENTO")));
									contr_I.setDenNumero(new Integer(rsSogg.getInt("DEN_NUMERO")));
									contr_I.setDenTipo(new Integer(rsSogg.getInt("TIP_DEN")));
									contr_I.setNumRiga(new Integer(rsSogg.getInt("NUM_RIGA")));
									contr_I.setDenMesiPossesso(new Integer(rsSogg.getInt("DEN_MESI_POSSESSO")));
									contr_I.setQuotaPossesso(super.tornaValoreRS(rsSogg, "QUOTA_POSSESSO"));
									contr_I.setDicPosseduto(super.tornaValoreRS(rsSogg, "DIC_POSSEDUTO"));
									contr_I.setDenominazione(rsSogg.getString("NOMINATIVO"));
									contr_I.setProvenienza(rsSogg.getString("PROV_CONTR"));
									contr_I.setRenditaCatastale(rsSogg.getString("RENDITA"));

									contr_I.setNome(rsSogg.getString("NOME"));
									contr_I.setCognome(rsSogg.getString("COGNOME"));
									contr_I.setDataNascita(rsSogg.getString("DATA_NASCITA"));
									contr_I.setCodFiscale(rsSogg.getString("CODICE_FISCALE"));
									contr_I.setPartitaIVA(rsSogg.getString("PARTITA_IVA"));
									
									
									contrList.add(contr_I);
								}
						}
						
						if(ici.getFoglioCatasto() != null && !ici.getFoglioCatasto().trim().equals("")
								&& ici.getParticellaCatasto() != null && !ici.getParticellaCatasto().trim().equals("")
								&& ici.getSubalterno() != null && !ici.getSubalterno().trim().equals("") )
						{
							PreparedStatement stm2 =null;
							ResultSet rs2 =null;
							try
							{							
								setIndirizzoCatastale(ici, conn, stm2, rs2);
								
								sql = "SELECT DISTINCT S.PREFISSO || ' ' || S.NOME || ' ' || c.civico as indirizzo " +
								"                  FROM sitiuiu u, siticivi_uiu iu, siticivi c, SITIDSTR S " +
								"                 WHERE iu.pkid_uiu = u.pkid_uiu " +
								"                   AND iu.pkid_civi = c.pkid_civi " +
								"                   and c.pkid_stra=s.pkid_stra " +
								"                   AND u.foglio = ? " +
								"                   AND u.particella = lpad(?,5,'0') " ;
								//"                   and u.unimm = ?"; 
								stm2 = conn.prepareStatement(sql);
								stm2.setInt(1, Integer.parseInt(ici.getFoglioCatasto()));
								stm2.setString(2, ici.getParticellaCatasto());
								//stm2.setInt(3, Integer.parseInt(ici.getSubalterno()));
								rs2 =  stm2.executeQuery();
								while(rs2.next())
								{
									if(rs2.getString("indirizzo") != null)
										ici.setIndirizzoViarioRif(ici.getIndirizzoViarioRif() != null ? ici.getIndirizzoViarioRif()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
								}
								if(ici.getIndirizzoViarioRif() == null)
									ici.setIndirizzoViarioRif("non trovato");
								
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
						else{
							ici.setIndirizzoCatastale("");
							ici.setIndirizzoViarioRif("");
						}
					}
					ht.put("CONTR_LIST",contrList);
					ht.put("ICI",ici);
				}
				else
				{
					ArrayList contrList = new ArrayList();
					ht.put("CONTR_LIST",contrList);
					
					ht.put("ICI",ici);
				}
				
				// docfa collegati
				if
				(
						(ici.getFoglioCatasto() != null && !ici.getFoglioCatasto().equals("") && !ici.getFoglioCatasto().equals("-")) &&
						(ici.getParticellaCatasto() != null && !ici.getParticellaCatasto().equals("") && !ici.getParticellaCatasto().equals("-")) &&
						(ici.getSubalterno() != null && !ici.getSubalterno().equals("") && !ici.getSubalterno().equals("-"))

				)
				{
					//cerco dati 
					sql = "SELECT protocollo_registrazione protocollo,  fornitura,categoria,classe,rendita_euro FROM DOCFA_DATI_CENSUARI U WHERE  "+
							"  u.foglio = lpad(?,4,'0') "+
							"  AND u.numero = lpad(?,5,'0') "+
							"  AND u.subalterno = lpad(?,4,'0')";

					this.initialize();
					this.setString(1,ici.getFoglioCatasto().trim());
					this.setString(2,ici.getParticellaCatasto().trim());
					this.setString(3,ici.getSubalterno().trim());
					prepareStatement(sql);
					java.sql.ResultSet rsDocfa = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					ArrayList docfaCollegati = new ArrayList();
					while (rsDocfa.next())
					{
						Docfa docfa = new Docfa();
						docfa.setProtocollo(tornaValoreRS(rsDocfa,"protocollo"));
						docfa.setFornitura(tornaValoreRS(rsDocfa,"fornitura","YYYY-MM-DD"));
						docfa.setCategoria(tornaValoreRS(rsDocfa,"categoria"));
						docfa.setClasse(tornaValoreRS(rsDocfa,"classe"));
						docfa.setRendita(tornaValoreRS(rsDocfa,"rendita_euro"));
						docfaCollegati.add(docfa);
					}
					ht.put(ICI_DOCFA_COLLEGATI,docfaCollegati);
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
	
	private void setIndirizzoCatastale(OggettiICI ici, Connection conn, PreparedStatement stm2, ResultSet rs2) throws Exception {
		
		//VECCHIO CODICE
		//SI CERCAVA SOLO PER FOGLIO / PARTICELLA
		
		/*sql = "SELECT DISTINCT  t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
		//"               ,ind.progressivo, id.foglio, id.mappale, id.sub " +
		"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
		"          WHERE c.codi_fisc_luna = ? " +
		"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ID.sez = c.sezione_censuaria " +
		"            AND ID.foglio = LPAD (?, 4, '0') " +
		"            AND ID.mappale = LPAD (?, 5, '0') " +
		//"            AND nvl(ID.sub,'0000') = LPAD (?, 4, '0') " +
		"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ind.sezione = ID.sezione " +
		"            AND ind.id_imm = ID.id_imm " +
		"            AND ind.progressivo = ID.progressivo " +
		"            AND t.pk_id = ind.toponimo " ;
		stm2 = conn.prepareStatement(sql);
		stm2.setString(1, ici.getCodEnte());
		stm2.setString(2, ici.getFoglioCatasto());
		stm2.setString(3, ici.getParticellaCatasto());
		//stm2.setString(4, ici.getSubalterno());
		rs2 =  stm2.executeQuery();
		while(rs2.next())
		{
			if(rs2.getString("indirizzo") != null)
				ici.setIndirizzoCatastale(ici.getIndirizzoCatastale() != null ? ici.getIndirizzoCatastale()+"<br>"+rs2.getString("indirizzo"):rs2.getString("indirizzo") );
		}
		if(ici.getIndirizzoCatastale() == null)
			ici.setIndirizzoCatastale("non trovato");
		
		
		stm2.cancel();*/
		
		//SOSTITUITO DA:
		
		//PRIMA RICERCA PER FOGLIO / PARTICELLA / SUBALTERNO
		//SE NON TROVATO NULLA, SECONDA RICERCA PER FOGLIO / PARTICELLA
		//SI PRENDE L'ULTIMO INDIRIZZO COLLEGATO ALL'IMMOBILE E SI ESCLUDONO DALLA QUERY LE UIU CESSATE
		ici.setIndirizzoCatastale(null);
		
		sql = "SELECT DISTINCT ind.load_id, t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
		"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
		"          WHERE c.codi_fisc_luna = ? " +
		"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ID.sez = c.sezione_censuaria " +
		"            AND ID.foglio = LPAD (?, 4, '0') " +
		"            AND ID.mappale = LPAD (?, 5, '0') " +
		"            AND nvl(ID.sub,'0000') = LPAD (?, 4, '0') " +
		"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
		"            AND ind.sezione = ID.sezione " +
		"            AND ind.id_imm = ID.id_imm " +
		"            AND ind.progressivo = ID.progressivo " +
		"            AND t.pk_id = ind.toponimo " +
		"            AND NOT EXISTS (" +
		"			(SELECT 1 FROM sitiuiu " +
		"			WHERE sitiuiu.cod_nazionale = c.cod_nazionale " +
		"			and LPAD (sitiuiu.foglio, 4, '0')  = ID.foglio " +
		"			and LPAD (sitiuiu.particella, 5, '0') = ID.mappale " +
		"			and sitiuiu.sub = ' ' " +
		"			and LPAD (sitiuiu.unimm, 4, '0') = nvl(ID.sub,'0000') " +
		"			and ((data_inizio_val < sysdate and data_fine_val > sysdate) " +
		"			or not exists " +
		"			(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu " + 
		"			and b.cod_nazionale = sitiuiu.cod_nazionale " +
		"			and b.foglio = sitiuiu.foglio " + 
		"			and b.particella = sitiuiu.particella " + 
		"			and b.sub = sitiuiu.sub " + 
		"			and b.unimm = sitiuiu.unimm " + 
		"			and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) " + 
		"			and sitiuiu.ide_muta_fine is not null))" +		
		"			ORDER BY ind.load_id";
		stm2 = conn.prepareStatement(sql);
		stm2.setString(1, ici.getCodEnte());
		stm2.setString(2, ici.getFoglioCatasto());
		stm2.setString(3, ici.getParticellaCatasto());
		stm2.setString(4, ici.getSubalterno());
		rs2 =  stm2.executeQuery();
		while(rs2.next()) {
			//scorre tutto e prende l'ultimo (quello con load_id più alto, secondo l'order by)
			if(rs2.getString("indirizzo") != null) {
				ici.setIndirizzoCatastale(rs2.getString("indirizzo"));				
			}				
		}
		if(ici.getIndirizzoCatastale() == null) {
			stm2.cancel();
			sql = "SELECT DISTINCT ind.load_id, t.descr || ' ' || ind.ind  || ' ' || remove_lead_zero (ind.civ1) AS indirizzo " +
			"           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t " +
			"          WHERE c.codi_fisc_luna = ? " +
			"            AND ID.codi_fisc_luna = c.codi_fisc_luna " +
			"            AND ID.sez = c.sezione_censuaria " +
			"            AND ID.foglio = LPAD (?, 4, '0') " +
			"            AND ID.mappale = LPAD (?, 5, '0') " +
			"            AND ind.codi_fisc_luna = c.codi_fisc_luna " +
			"            AND ind.sezione = ID.sezione " +
			"            AND ind.id_imm = ID.id_imm " +
			"            AND ind.progressivo = ID.progressivo " +
			"            AND t.pk_id = ind.toponimo " +
			"            AND NOT EXISTS (" +
			"			(SELECT 1 FROM sitiuiu " +
			"			WHERE sitiuiu.cod_nazionale = c.cod_nazionale " +
			"			and LPAD (sitiuiu.foglio, 4, '0')  = ID.foglio " +
			"			and LPAD (sitiuiu.particella, 5, '0') = ID.mappale " +
			"			and sitiuiu.sub = ' ' " +
			"			and LPAD (sitiuiu.unimm, 4, '0') = nvl(ID.sub,'0000') " +
			"			and ((data_inizio_val < sysdate and data_fine_val > sysdate) " +
			"			or not exists " +
			"			(SELECT 1 FROM sitiuiu b where b.pkid_uiu <> sitiuiu.pkid_uiu " + 
			"			and b.cod_nazionale = sitiuiu.cod_nazionale " +
			"			and b.foglio = sitiuiu.foglio " + 
			"			and b.particella = sitiuiu.particella " + 
			"			and b.sub = sitiuiu.sub " + 
			"			and b.unimm = sitiuiu.unimm " + 
			"			and (b.data_inizio_val < sysdate and b.data_fine_val > sysdate))) " + 
			"			and sitiuiu.ide_muta_fine is not null))" +	
			"			ORDER BY ind.load_id";
			stm2 = conn.prepareStatement(sql);
			stm2.setString(1, ici.getCodEnte());
			stm2.setString(2, ici.getFoglioCatasto());
			stm2.setString(3, ici.getParticellaCatasto());
			rs2 =  stm2.executeQuery();
			while(rs2.next()) {
				//scorre tutto e prende l'ultimo (quello con load_id più alto, secondo l'order by)
				if(rs2.getString("indirizzo") != null) {
					ici.setIndirizzoCatastale(rs2.getString("indirizzo"));				
				}			
			}
		}
		
		if(ici.getIndirizzoCatastale() == null)
			ici.setIndirizzoCatastale("non trovato");
		
		stm2.cancel();
	}

	public Hashtable mCaricareListaOggettiICIPerTitolarita(String titolarita) throws Exception{

				Hashtable ht = new Hashtable();
				Vector vct = new Vector();
				String sql = "";
				String conteggio = "";
				long conteggione = 0;
				// faccio la connessione al db
				Connection conn = null;
		try {
					conn = this.getConnection();
						 sql = "select tri_oggetti_ici.PK_SEQU_OGGETTI_ICI, decode(ewg_tab_comuni.DESCRIZIONE,null,'-',ewg_tab_comuni.DESCRIZIONE) AS COMUNE, tri_oggetti_ici.FK_COMUNI, " +
								"decode(tri_oggetti_ici.FOGLIO_CATASTO,null,'-',tri_oggetti_ici.FOGLIO_CATASTO) AS FOGLIO," +
								"decode(tri_oggetti_ici.PARTICELLA_CATASTO,null,'-',tri_oggetti_ici.PARTICELLA_CATASTO) AS PARTICELLA, " +
								"decode(tri_oggetti_ici.SUBALTERNO_CATASTO,null,'-',tri_oggetti_ici.SUBALTERNO_CATASTO) AS SUBALTERNO," +
								"decode(tri_oggetti_ici.DESCR_INDIRIZZO,null,'-',tri_oggetti_ici.DESCR_INDIRIZZO) AS INDIRIZZO," +
								"decode(tri_oggetti_ici.SUPERFICIE,null,'-',tri_oggetti_ici.SUPERFICIE) AS SUPERFICIE, " +
								"decode(tri_oggetti_ici.N_VANI,null,'-',tri_oggetti_ici.N_VANI) AS VANI, " +
								"decode(tri_oggetti_ici.CATEGORIA_CATASTALE,null,'-',tri_oggetti_ici.CATEGORIA_CATASTALE) AS CATEGORIA, " +
								"decode(tri_oggetti_ici.CLASSE,null,'-',tri_oggetti_ici.CLASSE) AS CLASSE," +
								"decode(tri_oggetti_ici.RENDITA_CATASTALE,null,'-',tri_oggetti_ici.RENDITA_CATASTALE) AS RENDITA ," +
								//"nvl(to_char(tri_oggetti_ici.DATA_INIZIO_POSSESSO,'dd/mm/yyyy'),'-') AS DATA_INIZIO, " +
								"decode(tri_oggetti_ici.DATA_INIZIO_POSSESSO,null,'-',tri_oggetti_ici.DATA_INIZIO_POSSESSO) AS DATA_INIZIO, " +
								"decode(tri_oggetti_ici.DATA_FINE_POSSESSO,null,'-',tri_oggetti_ici.DATA_FINE_POSSESSO) AS DATA_FINE, " +
								//"nvl(to_char(tri_oggetti_ici.DATA_FINE_POSSESSO,'dd/mm/yyyy'),'-') AS DATA_FINE, " +
								"decode(tri_oggetti_ici.FK_CONTRIBUENTE,null,'-',tri_oggetti_ici.FK_CONTRIBUENTE) AS CONTRIBUENTE, " +
								"decode(tri_oggetti_ici.PROVENIENZA,null,'-',tri_oggetti_ici.PROVENIENZA) AS PROVENIENZA, " +
								"tri_oggetti_ici.IMMD," +
								"tri_oggetti_ici.PARTICELLA_CATASTO AS PARTICELLA ,"+
								"tri_oggetti_ici.DEN_NUMERO AS DEN_NUMERO, "+
								"tri_oggetti_ici.DEN_ANNO AS DEN_ANNO "+
								"from tri_oggetti_ici, ewg_tab_comuni, tri_contribuenti " +
								"where tri_oggetti_ici.FK_CONTRIBUENTE = tri_contribuenti.CODICE_CONTRIBUENTE " +
								//"where tri_oggetti_ici.FK_CONTRIBUENTE = tri_contribuenti.uk_codi_contribuente " +
								"and tri_oggetti_ici.PROVENIENZA = tri_contribuenti.PROVENIENZA " +
								"and tri_contribuenti.UK_CODI_CONTRIBUENTE = ? " +
								"and ewg_tab_comuni.UK_BELFIORE = tri_oggetti_ici.FK_COMUNI " +
								"and tri_contribuenti.CODENTE = tri_oggetti_ici.codent " + 
								"ORDER BY DEN_ANNO DESC , DEN_NUMERO, FOGLIO_CATASTO, PARTICELLA_CATASTO, SUBALTERNO_CATASTO";

						this.initialize();
						this.setString(1,titolarita);


						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						while (rs.next()){
							OggettiICI ici = new OggettiICI();

							ici.setChiave(rs.getString("PK_SEQU_OGGETTI_ICI"));
							ici.setComune(rs.getString("COMUNE"));
							ici.setFoglioCatasto(rs.getString("FOGLIO"));
							ici.setParticellaCatasto(rs.getString("PARTICELLA"));
							ici.setSubalterno(rs.getString("SUBALTERNO"));
							ici.setIndirizzo(rs.getString("INDIRIZZO"));
							ici.setSuperficie(rs.getString("SUPERFICIE"));
							ici.setVani(rs.getString("VANI"));
							ici.setCategoria(rs.getString("CATEGORIA"));
							ici.setClasse(rs.getString("CLASSE"));
							ici.setRendita(rs.getString("RENDITA"));
							ici.setDataInizio(rs.getString("DATA_INIZIO"));
							ici.setDataFine(rs.getString("DATA_FINE"));
							ici.setCodContribuente(rs.getString("CONTRIBUENTE"));
							ici.setProvenienza(rs.getString("PROVENIENZA"));
							ici.setImmd(rs.getString("IMMD"));
							ici.setDenAnno(rs.getInt("DEN_ANNO"));
							ici.setDenNumero(rs.getInt("DEN_NUMERO"));
							vct.add(ici);
						}
					ht.put("LISTA_ICI",vct);
					
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






	public Hashtable mCaricareListaOggettiICI(Vector listaPar, OggettiICIFinder finder) throws Exception{

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
					/*Enumeration en = listaPar.elements();
					while (en.hasMoreElements()){
						EscElementoFiltro filtro = (EscElementoFiltro)en.nextElement();
						if (!filtro.getValore().trim().equals("")){
							if (filtro.getOperatore().toLowerCase().trim().equals("like")){
								sql = sql + " and " + filtro.getCampoFiltrato().toUpperCase() + " LIKE '%'||?||'%'";
							}
							else{
								sql = sql + " and " + filtro.getCampoFiltrato() + " " + filtro.getOperatore() + " ? ";
							}
							if (filtro.getTipo().equals("N"))
								this.setInt(indice,new Integer(filtro.getValore()).intValue());
							else
								this.setString(indice,filtro.getValore().toUpperCase());
							indice ++;
						}
					}*/
				}
				else{
					//if (!finder.getKeyStr().equals("")){
					sql = sql + " AND pk_sequ_oggetti_ici in (" +finder.getKeyStr() + ")" ;
					//this.setString(indice,finder.getTitolarita());
					//indice ++;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				//sql = sql + " order by ewg_tab_comuni.DESCRIZIONE,FOGLIO_CATASTO,PARTICELLA_CATASTO,SUBALTERNO_CATASTO";
				//if (i ==1) sql = sql + ")order by comune, foglio, particella,subalterno ) where N > " + limInf + " and N <=" + limSup;
				if (i == 1) sql = sql + " order by DEN_ANNO desc, comune, foglio, particella, subalterno)) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						OggettiICI ici = new OggettiICI();
						ici.setChiave(rs.getString("PK_SEQU_OGGETTI_ICI"));
						ici.setComune(rs.getString("COMUNE"));
						ici.setFoglioCatasto(rs.getString("FOGLIO"));
						ici.setParticellaCatasto(rs.getString("PARTICELLA"));
						ici.setSubalterno(rs.getString("SUBALTERNO"));
						ici.setCategoria(rs.getString("CATEGORIA"));
						ici.setProvenienza(rs.getString("PROVENIENZA"));
						ici.setCodEnte(rs.getString("FK_COMUNI"));
						ici.setImmd(rs.getString("IMMD"));
						ici.setDenAnno(rs.getInt("DEN_ANNO"));
						ici.setDenNumero(rs.getInt("DEN_NUMERO"));
						setEvidenzia(conn, ici);
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(ici.getFoglioCatasto(), Utils.fillUpZeroInFront(ici.getParticellaCatasto(),5),ici.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							ici.setLatitudine(coord.firstObj);
							ici.setLongitudine(coord.secondObj);
						}

						
						
						vct.add(ici);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put("LISTA_ICI",vct);
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
	
	public Vector mCaricareListaOggettiICISoggetto(String codiceFiscaleDic, int annoImposta) throws Exception{

		Vector vct = new Vector();
	    sql = "";
		Connection conn = null;
		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			sql = SQL_SELECT_LISTA_SOGGETTO;
			
			indice = 1;
			this.initialize();
			this.setInt(indice,1);
			indice++;
			this.setString(indice, "31/12/" + annoImposta);
			indice++;
			this.setString(indice, "01/01/" + annoImposta);
			indice++;
			this.setString(indice, codiceFiscaleDic);
			indice++;	
			this.setString(indice, codiceFiscaleDic);
			indice++;	

			sql = sql + " ORDER BY DEN_ANNO DESC, DEN_NUMERO, RENDITA_CATASTALE ";


			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs.next()){
				Contribuente contr = new Contribuente();
				contr.setDenominazione(rs.getString("NOMINATIVO"));
				contr.setProvenienza(rs.getString("PROVENIENZA"));
				contr.setDenAnno(rs.getInt("DEN_ANNO"));
				contr.setDenRiferimento(new Integer(rs.getInt("DEN_RIFERIMENTO")));
				contr.setDenNumero(rs.getInt("DEN_NUMERO"));
				contr.setNumRiga(new Integer(rs.getInt("NUM_RIGA")));
				contr.setDenMesiPossesso(new Integer(rs.getInt("DEN_MESI_POSSESSO")));
				contr.setQuotaPossesso(super.tornaValoreRS(rs, "QUOTA_POSSESSO"));
				contr.setRenditaCatastale(rs.getString("RENDITA"));
				contr.setDicPosseduto(super.tornaValoreRS(rs, "DIC_POSSEDUTO"));
				contr.setDenTipo(new Integer(rs.getInt("TIP_DEN")));
				
				vct.add(contr);
			}

			return vct;
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
	
	public int getCountListaSoggetto(String codFiscaleDic, int annoImposta) throws Exception {
		
		Connection conn = null;
		int numOggettiICI = 0;
		try
		{
			conn = this.getConnection();
			//PreparedStatement pstmtOggettiICI = conn.prepareStatement("SELECT COUNT (*) AS CONTA FROM (" + SQL_SELECT_LISTA_SOGGETTO + ")");
			PreparedStatement pstmtOggettiICI = conn.prepareStatement(SQL_SELECT_LISTA_SOGGETTO_COUNT);
			int indice = 1;
			pstmtOggettiICI.setInt(indice,1);
			indice ++;
			pstmtOggettiICI.setString(indice, "31/12/" + annoImposta);
			indice++;
			pstmtOggettiICI.setString(indice, "01/01/" + annoImposta);
			indice++;
			pstmtOggettiICI.setString(indice, codFiscaleDic);
			indice++;
			pstmtOggettiICI.setString(indice, codFiscaleDic);
			indice++;
			ResultSet rsOggettiICI = pstmtOggettiICI.executeQuery();				
			while (rsOggettiICI.next()) {
				numOggettiICI = rsOggettiICI.getInt("CONTA");
			}
			return numOggettiICI;
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
	

	
	private void setEvidenzia(Connection conn, OggettiICI ici) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(SQL_SELECT_EVIDENZIA_IN_LISTA);
			pstmt.setString(1, ici.getCodEnte());
			pstmt.setString(2, ici.getFoglioCatasto());
			pstmt.setString(3, ici.getParticellaCatasto());
			pstmt.setString(4, ici.getSubalterno());
			pstmt.setString(5, ici.getChiave());
			rs = pstmt.executeQuery();
			ici.setEvidenzia(rs.next());
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
	}
	
}
