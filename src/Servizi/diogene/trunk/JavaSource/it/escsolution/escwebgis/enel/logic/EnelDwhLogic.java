/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.enel.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.enel.bean.EnelBean;
import it.escsolution.escwebgis.enel.bean.EnelBean2;
import it.escsolution.escwebgis.enel.bean.EnelDwhFinder;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.utils.StringUtils;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EnelDwhLogic extends EscLogic{
	private String appoggioDataSource;
	public EnelDwhLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
				SQL_SELECT_LISTA = getProperty("sql.SELECT_LISTA");
				SQL_SELECT_COUNT_LISTA = getProperty("sql.SELECT_COUNT_LISTA");
				SQL_SELECT_LISTA_FROM_SOGGETTO= getProperty("sql.SELECT_LISTA_FROM_SOGGETTO");
				
			}

	public static final String FINDER = "FINDER105";
	public static final String ENELDWH = EnelDwhLogic.class.getName() + "@ENELDWH";
	public static final String LISTA_ENELDWH = "LISTA_ENELDWH";
	public static final String LISTA_ENEL2DWH = "LISTA_ENEL2DWH";
	public static final String UTENZE = "UTENZE";


	
	private static String SQL_SELECT_LISTA = null;

	private static String SQL_SELECT_COUNT_LISTA= null;
	
	private static String SQL_SELECT_LISTA_FROM_SOGGETTO=null;

	private final static String SQL_SELECT_COUNT_ALL ="select count(*) as conteggio  from SIT_ENEL_UTENTE WHERE 1=?" ;

	public Hashtable mCaricareListaEnelFromSoggetto(String idSoggetto) throws Exception {
		
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		
		Connection conn = null;
		EnelDwhFinder finder = new EnelDwhFinder();
		// faccio la connessione al db
		try {
			
			sql=SQL_SELECT_LISTA_FROM_SOGGETTO;
			conn = this.getConnection();
			int indice=1;
			
			this.initialize();
			this.setInt(indice,1);
			indice ++;
			this.setString(indice,idSoggetto);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			int cont = 0;
 			while (rs.next()){
 				  cont++;
					//campi della lista
					EnelBean enel = new EnelBean();
					EnelBean2 enel2 = new EnelBean2();
								
					enel.setId(rs.getString("id"));
					enel.setIdExt(rs.getString("id_ext"));
					enel.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
					enel.setDenominazione(tornaValoreRS(rs,"denominazione"));

					enel2.setAnnoRiferimentoDati(tornaValoreRS(rs,"ANNO_RIFERIMENTO_DATI"));
					enel2.setCodiceUtenza(tornaValoreRS(rs, "codice_utenza"));
					enel2.setUtente(enel);
								
					vct.add(enel2);
				}
					
				ht.put(LISTA_ENELDWH, vct);
				finder.setTotaleRecordFiltrati(cont);
				// pagine totali
				finder.setPagineTotali(1);
				finder.setTotaleRecord(cont);
				finder.setRighePerPagina(cont);

				ht.put(FINDER, finder);
				
				/*INIZIO AUDIT*/
				try {
					Object[] arguments = new Object[1];
					arguments[0] = idSoggetto;
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
		finally {
		 if (conn != null && !conn.isClosed())
			conn.close();
		}
		
	}
	
	
	// Carico la lista proveniendo dai civici
	
	public Hashtable mCaricareListaEnelDaCiv(String oggettoSel) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		Connection conn = null;
		EnelDwhFinder finder = new EnelDwhFinder();
		// faccio la connessione al db
		try {
			
			conn = this.getConnection();
			sql = "SELECT utente.id, utente.id_ext, utente.codice_fiscale, utente.denominazione, utenza.anno_riferimento_dati, utenza.codice_utenza";
			sql += " FROM SIT_ENEL_UTENTE utente, SIT_ENEL_UTENZA utenza where utenza.id=? and utenza.id_ext_enel_utente=utente.ID_EXT";
			this.initialize();
			this.setString(1,oggettoSel);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			int cont = 0;
 			while (rs.next()){
 				  cont++;
					//campi della lista
					EnelBean enel = new EnelBean();
					EnelBean2 enel2 = new EnelBean2();
								
					enel.setId(rs.getString("id"));
					enel.setIdExt(rs.getString("id_ext"));
					enel.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
					enel.setDenominazione(tornaValoreRS(rs,"denominazione"));

					enel2.setAnnoRiferimentoDati(tornaValoreRS(rs,"ANNO_RIFERIMENTO_DATI"));
					enel2.setCodiceUtenza(tornaValoreRS(rs, "codice_utenza"));
					enel2.setUtente(enel);
								
					vct.add(enel2);
				}
					
				ht.put(LISTA_ENELDWH, vct);
				finder.setTotaleRecordFiltrati(cont);
				// pagine totali
				finder.setPagineTotali(1);
				finder.setTotaleRecord(cont);
				finder.setRighePerPagina(cont);

				ht.put(FINDER, finder);
				
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
		finally {
		 if (conn != null && !conn.isClosed())
			conn.close();
		}
}	


	public Hashtable mCaricareListaEnel(Vector listaPar, EnelDwhFinder finder) throws Exception{
					// carico la lista e la metto in un hashtable
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
								java.sql.ResultSet rs ;
								sql = SQL_SELECT_COUNT_ALL;
								this.initialize();
								this.setInt(indice,1);
								indice ++;
								prepareStatement(sql);
								rs =executeQuery(conn, this.getClass().getName(), getEnvUtente());
								if (rs.next()){
										conteggione = rs.getLong("conteggio");
								}


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

								//il primo passaggio esegue la select count
								//campi della search

								// il primo passaggio esegue la select count
								if (finder.getKeyStr().equals("")){
									sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
								}
								else{
									//controllo provenienza chiamata
									String controllo = finder.getKeyStr();
									if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA)>0){
										String ente = controllo.substring(0,controllo.indexOf("|"));
										ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length()+1);
										String chiavi= "'"+controllo.substring(controllo.indexOf("|")+1);
										sql = sql + " and SIT_ENEL_UTENTE.ID in (" +chiavi + ")" ;
										sql = sql + " and  CODENT = '" +ente + "'" ;
									}else
										sql = sql + " and SIT_ENEL_UTENTE.ID in (" +finder.getKeyStr() + ")" ;

								}


									long limInf, limSup;
									limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
									limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
									sql = sql + " order by DENOMINAZIONE, CODICE_UTENZA, ANNO_RIFERIMENTO_DATI ";
									if (i ==1) sql = sql + ")) where N > " + limInf + " and N <=" + limSup;

									prepareStatement(sql);
									rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

									if (i ==1) {
										while (rs.next()){
											//campi della lista
											EnelBean enel = new EnelBean();
											EnelBean2 enel2 = new EnelBean2();
											
											enel.setId(rs.getString("id"));
											enel.setIdExt(rs.getString("id_ext"));
											enel.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
											enel.setDenominazione(tornaValoreRS(rs,"denominazione"));

											enel2.setAnnoRiferimentoDati(tornaValoreRS(rs,"ANNO_RIFERIMENTO_DATI"));
											enel2.setCodiceUtenza(tornaValoreRS(rs, "codice_utenza"));
											enel2.setUtente(enel);
											
											vct.add(enel2);
										}
									}
									else{
										if (rs.next()){
											conteggio = rs.getString("conteggio");
										}
									}
								}
								ht.put(LISTA_ENELDWH, vct);
								finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
								// pagine totali
								finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
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


	public Hashtable mCaricareDettaglioEnel(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
						// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
						String sql = getProperty("sql.SELECT_DETTAGLIO");
						sql += " " + getProperty("sql.SELECT_DETTAGLIO_COND_ID");
						

						int indice = 1;
						this.setString(indice,chiave);

						prepareStatement(sql);
						java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

						EnelBean enel = new EnelBean();

						if (rs.next()){
							enel.setId(rs.getString("ID"));
							enel.setIdExt(rs.getString("ID_EXT"));
							enel.setDenominazione(tornaValoreRS(rs,"denominazione"));
							enel.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
							enel.setSesso(tornaValoreRS(rs,"sesso"));
							enel.setDataNascita(rs.getTimestamp("data_nascita"));
							enel.setComuneNascitaSede(tornaValoreRS(rs,"comune_nascita_sede"));
							enel.setProvinciaNascitaSede(tornaValoreRS(rs,"provincia_nascita_sede"));
							enel.setComuneDomFiscale(tornaValoreRS(rs,"comune_dom_fiscale"));
							enel.setProvinciaDomFiscale(tornaValoreRS(rs,"provincia_dom_fiscale"));
							enel.setQualificaTitolare(tornaValoreRS(rs,"qualifica_Titolare"));

							
								
							
						}
						
						Hashtable htUtenze = mCaricareUtenze(enel);
						
						ht.putAll(htUtenze);
						ht.put(ENELDWH, enel);
						
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
					if (conn != null)
						if (!conn.isClosed())
							conn.close();
				}
	}

	public Hashtable mCaricareDettaglioEnelDaCivViaOgg(String oggettoSel) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		Connection conn = null;
		EnelDwhFinder finder = new EnelDwhFinder();
		// faccio la connessione al db
		try {
			
			conn = this.getConnection();
			sql = "SELECT utente.id, utente.id_ext, utente.codice_fiscale, DECODE (UTENTE.DENOMINAZIONE, NULL, '-', UTENTE.DENOMINAZIONE ) AS DENOMINAZIONE, " +
					"sesso, data_nascita, comune_nascita_sede, provincia_nascita_sede, comune_dom_fiscale, provincia_dom_fiscale, qualifica_titolare ";
			sql += " FROM SIT_ENEL_UTENTE utente, SIT_ENEL_UTENZA utenza where utenza.id=? and utenza.id_ext_enel_utente=utente.ID_EXT";
			this.initialize();
			this.setString(1,oggettoSel);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			EnelBean enel = new EnelBean();

			if (rs.next()){
				enel.setId(rs.getString("ID"));
				enel.setIdExt(rs.getString("ID_EXT"));
				enel.setDenominazione(tornaValoreRS(rs,"denominazione"));
				enel.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
				enel.setSesso(tornaValoreRS(rs,"sesso"));
				enel.setDataNascita(rs.getTimestamp("data_nascita"));
				enel.setComuneNascitaSede(tornaValoreRS(rs,"comune_nascita_sede"));
				enel.setProvinciaNascitaSede(tornaValoreRS(rs,"provincia_nascita_sede"));
				enel.setComuneDomFiscale(tornaValoreRS(rs,"comune_dom_fiscale"));
				enel.setProvinciaDomFiscale(tornaValoreRS(rs,"provincia_dom_fiscale"));
				enel.setQualificaTitolare(tornaValoreRS(rs,"qualifica_titolare"));

				
					
				
			}
			
			Hashtable htUtenze = mCaricareUtenze(enel);
			
			ht.putAll(htUtenze);
			ht.put(ENELDWH, enel);
			
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
		finally {
		 if (conn != null && !conn.isClosed())
			conn.close();
		}
}	
	

	public Hashtable mCaricareUtenze(EnelBean enel) throws Exception{

		Hashtable ht = new Hashtable();
		Vector<EnelBean2> utenze = new Vector<EnelBean2>();
					// faccio la connessione al db
					Connection conn = null;
					try {
						conn = this.getConnection();
						this.initialize();
						String sql = getProperty("sql.SELECT_DETTAGLIO1");

						int indice = 1;
						this.setString(indice,enel.getIdExt());

						prepareStatement(sql);
						java.sql.ResultSet rs1 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						while (rs1.next()) {
							EnelBean2 enel2 = new EnelBean2();
							enel2.setAnnoRiferimentoDati(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("annoRiferimentoDati")));
							enel2.setCapUbicazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("capUbicazione")));
							enel2.setCodiceAssenzaCatasto(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("codiceAssenzaCatasto")));
							enel2.setCodiceComuneCatastale(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("codiceComuneCatastale")));
							enel2.setCodiceFiscaleErogante(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("codiceFiscaleErogante")));
							enel2.setCodiceMerceologico(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("codiceMerceologico")));
							enel2.setCodiceUtenza(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("codiceUtenza")));
							enel2.setComuneAmmUbicazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("comuneAmmUbicazione")));

							enel2.setComuneCatastale(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("comuneCatastale")));
							enel2.setDataPrimaAttivazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("dataPrimaAttivazione")));
							enel2.setEstensioneParticella(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("estensioneParticella")));
							enel2.setFoglio(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("foglio")));
							enel2.setIndirizzoUbicazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("indirizzoUbicazione")));
							enel2.setKwhFatturati(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("kwhFatturati")));
							enel2.setMesiFatturazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("mesiFatturazione")));
							enel2.setParticella(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("particella")));
							enel2.setProvinciaAmmUbicazione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("provinciaAmmUbicazione")));
							enel2.setSezione(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("sezione")));
							String spesaAnnua = tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("spesaAnnua"));
							//qui non si può usare tornaValoreRS perché restituisce "-" anche per il valore null e non solo per il segno meno!!!
							String segnoImporto = rs1.getString(StringUtils.JavaName2NomeCampo("segnoImporto"));
							if ("-".equals(segnoImporto)) {
								spesaAnnua = segnoImporto + spesaAnnua;
							}
							enel2.setSpesaAnnua(spesaAnnua);
							enel2.setSubalterno(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("subalterno")));
							enel2.setTipoParticella(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("tipoParticella")));
							enel2.setTipoUnita(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("tipoUnita")));
							enel2.setTipoUtenza(tornaValoreRS(rs1, "DECO_TIPO_UTENZA"));
							enel2.setId(tornaValoreRS(rs1, StringUtils.JavaName2NomeCampo("id")));

							enel2.setUtente(enel);
							
							utenze.add(enel2);

						}
						
						ht.put(UTENZE, utenze);
						return ht;
					}
				catch (Exception e) {
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
