/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.dup.bean.DupElementoListaNote;
import it.escsolution.escwebgis.dup.bean.DupElementoListaNoteFinder;
import it.escsolution.escwebgis.dup.bean.DupImmobile;
import it.escsolution.escwebgis.dup.bean.DupNota;
import it.escsolution.escwebgis.dup.bean.DupSoggetto;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class DupNoteLogic extends EscLogic
{

	public DupNoteLogic(EnvUtente eu)
	{
		super(eu);
	}



	private final static String vista_percentuali_poss="(select t.* ,       ROUND(DECODE (quota_denominatore,NULL, NULL,0, NULL,quota_numeratore / quota_denominatore / 10),3) perce "
+"from (select IID_FORNITURA,IID_NOTA,ID_SOGGETTO_NOTA,ID_SOGGETTO_CATASTALE,"
		+"decode(Sc_FLAG_TIPO_TITOL_NOTA,null,SF_FLAG_TIPO_TITOL_NOTA,Sc_FLAG_TIPO_TITOL_NOTA) TIPO_TITOL_NOTA,decode(SC_CODICE_DIRITTO,null,Sf_CODICE_DIRITTO,SC_CODICE_DIRITTO) CODICE_DIRITTO,"
		+"DECODE(sc_flag_tipo_titol_nota,'C',DECODE (sc_quota_numeratore,NULL, null,sc_quota_numeratore),DECODE (sf_quota_numeratore,NULL, null,sf_quota_numeratore)) quota_numeratore, "
		+"decode(sc_flag_tipo_titol_nota,'C',DECODE (sc_quota_denominatore,NULL, null,sc_quota_denominatore), DECODE (sf_quota_denominatore,NULL, null,sf_quota_denominatore)) quota_denominatore,"
		+"decode(SC_REGIME,null,Sf_REGIME,SC_REGIME) REGIME,decode(SC_SOGGETTO_RIF,null,SC_SOGGETTO_RIF,SC_SOGGETTO_RIF) SOGGETTO_RIF "
		+"from mui_titolarita )t) TITO2 ";



	private final static String SQL_SELECT_LISTA = "select * from (" + "select t.*,  ROWNUM AS n from (" + "SELECT distinct sit_ente.CODENT as codente, nota.IID_FORNITURA, nota.iId, nota.flag_rettifica," + "sogg.cognome, sogg.nome, sogg.codice_fiscale, " + "sogg.denominazione, sogg.sede, sogg.codice_fiscale_g," + "fabbr.foglio, fabbr.numero, fabbr.subalterno," + "fabInfo.t_indirizzo,  TOPO.DESCR AS t_toponimo, fabinfo.t_civico1,"
			+ "tito.sf_regime, tito.sc_regime,tito.sc_flag_tipo_titol_nota , tito.sf_flag_tipo_titol_nota," + "fabinfo.t_civico2, fabinfo.t_civico3 " + "FROM sit_ente, mui_fabbricati_identifica fabbr," + "mui_nota_tras nota," + "mui_soggetti sogg," + "mui_fabbricati_info fabInfo," + "mui_titolarita tito , cat_d_toponimi topo, "+vista_percentuali_poss
			/*
			 * + "WHERE (fabbr.IID_FORNITURA = sogg.IID_FORNITURA)" + "AND
			 * (fabbr.id_nota = sogg.id_nota)" + "AND (sogg.id_nota =
			 * nota.id_nota)" + "AND (sogg.IID_FORNITURA = nota.IID_FORNITURA)" +
			 * "AND (nota.IID_FORNITURA = fabInfo.IID_FORNITURA)" + "AND
			 * (nota.id_nota = fabInfo.id_nota)" + "AND (tito.id_nota =
			 * fabInfo.id_nota)" + "AND (tito.IID_FORNITURA
			 * =fabInfo.IID_FORNITURA)" + "AND (tito.id_soggetto_nota =
			 * sogg.id_soggetto_nota) "
			 */
			+ "where  (NVL (tito.id_soggetto_nota, 'vuoto') =NVL (tito2.id_soggetto_nota, 'vuoto') AND tito.iId_nota = TITO2.iId_nota AND tito.IID_FORNITURA = tito2.IID_FORNITURA) and "
			+"nvl(tito.id_soggetto_nota,'vuoto') = nvl(sogg.id_soggetto_nota,'vuoto') " + "AND TITO.IID_NOTA = SOGG.IID_NOTA " + "AND TITO.IID_FORNITURA = SOGG.IID_FORNITURA " + "AND nvl(tito.id_soggetto_nota,'vuoto') = nvl(sogg.id_soggetto_nota,'vuoto')" + "AND sogg.iId_nota = NOTA.IID " + "AND sogg.IID_FORNITURA = NOTA.IID_FORNITURA " + "AND TITO.IID_NOTA = FABBR.IID_NOTA(+) "
			+ "AND TITO.IID_FORNITURA = FABBR.IID_FORNITURA(+) " + "AND TITO.ID_IMMOBILE = FABBR.ID_IMMOBILE(+) " + "AND FABBR.IID_FORNITURA= FABINFO.IID_FORNITURA(+) " + "AND FABBR.IID_NOTA = FABINFO.IID_NOTA(+) " + "AND FABBR.ID_IMMOBILE = FABINFO.ID_IMMOBILE(+) AND FABINFO.T_TOPONIMO= to_char(TOPO.pk_id (+)) " + "AND 1=?";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from( " + "SELECT  distinct nota.IID_FORNITURA, nota.iId, nota.flag_rettifica," + "sogg.cognome, sogg.nome, sogg.codice_fiscale, " + "sogg.denominazione, sogg.sede, sogg.codice_fiscale_g," + "fabbr.foglio, fabbr.numero, fabbr.subalterno," + "fabInfo.t_indirizzo, fabinfo.t_toponimo, fabinfo.t_civico1,"
			+ "tito.sf_regime, tito.sc_regime,tito.sc_flag_tipo_titol_nota , tito.sf_flag_tipo_titol_nota," + "fabinfo.t_civico2, fabinfo.t_civico3 " + "FROM mui_fabbricati_identifica fabbr," + "mui_nota_tras nota," + "mui_soggetti sogg," + "mui_fabbricati_info fabInfo," + "mui_titolarita tito ,"+vista_percentuali_poss
			/*
			 * + "WHERE (fabbr.IID_FORNITURA = sogg.IID_FORNITURA)" + "AND
			 * (fabbr.id_nota = sogg.id_nota)" + "AND (sogg.id_nota =
			 * nota.id_nota)" + "AND (sogg.IID_FORNITURA = nota.IID_FORNITURA)" +
			 * "AND (nota.IID_FORNITURA = fabInfo.IID_FORNITURA)" + "AND
			 * (nota.id_nota = fabInfo.id_nota)" + "AND (tito.id_nota =
			 * fabInfo.id_nota)" + "AND (tito.IID_FORNITURA
			 * =fabInfo.IID_FORNITURA)" + "AND (tito.id_soggetto_nota =
			 * sogg.id_soggetto_nota) "
			 */
			+ "where  (NVL (tito.id_soggetto_nota, 'vuoto') =NVL (tito2.id_soggetto_nota, 'vuoto') AND tito.iId_nota = TITO2.iId_nota AND tito.IID_FORNITURA = tito2.IID_FORNITURA) and "
			+" nvl(tito.id_soggetto_nota,'vuoto') = nvl(sogg.id_soggetto_nota,'vuoto') " + "AND TITO.IID_NOTA = SOGG.IID_NOTA " + "AND TITO.IID_FORNITURA = SOGG.IID_FORNITURA " + "AND nvl(tito.id_soggetto_nota,'vuoto') = nvl(sogg.id_soggetto_nota,'vuoto') " + "AND sogg.iId_nota = NOTA.IID " + "AND sogg.IID_FORNITURA = NOTA.IID_FORNITURA " + "AND TITO.IID_NOTA = FABBR.IID_NOTA(+) "
			+ "AND TITO.IID_FORNITURA = FABBR.IID_FORNITURA(+) " + "AND TITO.ID_IMMOBILE = FABBR.ID_IMMOBILE(+) " + "AND FABBR.IID_FORNITURA= FABINFO.IID_FORNITURA(+) " + "AND FABBR.IID_NOTA = FABINFO.IID_NOTA(+) " + "AND FABBR.ID_IMMOBILE = FABINFO.ID_IMMOBILE(+) " + "AND 1=?";

	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from MUI_NOTA_TRAS WHERE 1=?";

	public Hashtable mCaricareListacNote(Vector listaPar, DupElementoListaNoteFinder finder) throws Exception
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
			/*
			 * sql = SQL_SELECT_COUNT_ALL; int indice = 1; this.initialize();
			 * this.setInt(indice, 1); indice++; prepareStatement(sql);
			 * java.sql.ResultSet rs = executeQuery(conn); if (rs.next()) {
			 * conteggione = rs.getLong("conteggio"); }
			 */

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
				// campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					String[] filtriDaEliminare =new String[3];
					// /////////////FAVORE O CONTRO////////////////////////////////////////////////////////////
					EscElementoFiltro elementoFiltro = (EscElementoFiltro) listaPar.get(4);
					if (i == 0 && elementoFiltro.getValore()!=null && !elementoFiltro.getValore().trim().equals(""))
					{
						Vector operatoriStringaRid = new Vector();
						operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
						if (elementoFiltro.getValore().equals("C"))
						{
							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("CONTRO");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("SC_FLAG_TIPO_TITOL_NOTA");
							elementoFiltro.setOperatore("=");
							elementoFiltro.setValore("C");
							listaPar.add(elementoFiltro);
						}
						else if (elementoFiltro.getValore().equals("F"))
						{
							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("FAVORE");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("SF_FLAG_TIPO_TITOL_NOTA");
							elementoFiltro.setOperatore("=");
							elementoFiltro.setValore("F");
							listaPar.add(elementoFiltro);
						}
						else if (elementoFiltro.getValore().equals("NC"))
						{
							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("NON COINVOLTO");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("SC_FLAG_TIPO_TITOL_NOTA");
							elementoFiltro.setValore("C");
							elementoFiltro.setOperatore("<>");
							listaPar.add(elementoFiltro);

							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("NON COINVOLTO");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("SF_FLAG_TIPO_TITOL_NOTA");
							elementoFiltro.setValore("F");
							elementoFiltro.setOperatore("<>");
							listaPar.add(elementoFiltro);
						}

						//elementoFiltro = (EscElementoFiltro) listaPar.remove(4);
						filtriDaEliminare[0]="4";
					}
					// ///////////codice diritto///////////////////////////////////
					elementoFiltro = (EscElementoFiltro) listaPar.get(5);
					if (i == 0 && elementoFiltro.getValore()!=null && !elementoFiltro.getValore().trim().equals(""))
					{
						String valore=elementoFiltro.getValore().trim();
						Vector operatoriStringaRid = new Vector();
						operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("codice_diritto");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("sf_codice_diritto='"+valore+"' OR sC_codice_diritto");
							elementoFiltro.setOperatore("=");
							elementoFiltro.setValore(valore);
							listaPar.add(elementoFiltro);

						//elementoFiltro = (EscElementoFiltro) listaPar.remove(5);
						filtriDaEliminare[1]="5";
					}
					// ///////////GRAFFATI///////////////////////////////////
					elementoFiltro = (EscElementoFiltro) listaPar.get(15);
					if (i == 0 && elementoFiltro.getValore()!=null && !elementoFiltro.getValore().trim().equals(""))
					{
						Vector operatoriStringaRid = new Vector();
						operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
						if (elementoFiltro.getValore().equals("S"))
						{
							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("GRAFFATE");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("decode(FLAG_GRAFFATO,'0',null,FLAG_GRAFFATO)");
							elementoFiltro.setOperatore("notNull");
							elementoFiltro.setValore("1");
							listaPar.add(elementoFiltro);
						}
						else if (elementoFiltro.getValore().equals("N"))
						{
							elementoFiltro = new EscElementoFiltro();
							elementoFiltro.setAttributeName("NO GRAFFATE");
							elementoFiltro.setTipo("S");
							elementoFiltro.setCampoFiltrato("decode(FLAG_GRAFFATO,'0',null,FLAG_GRAFFATO)");
							elementoFiltro.setOperatore("null");
							elementoFiltro.setValore("1");
							listaPar.add(elementoFiltro);
						}


						//elementoFiltro = (EscElementoFiltro) listaPar.remove(15);
						filtriDaEliminare[2]="15";
					}

					if(i==0)
					{
						if(filtriDaEliminare[2]!=null)
							listaPar.remove(15);
						if(filtriDaEliminare[1]!=null)
							listaPar.remove(5);
						if(filtriDaEliminare[0]!=null)
							listaPar.remove(4);

					}

					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					String chiavi = "";
					boolean soggfascicolo = false;

					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA) > 0)
					{
						soggfascicolo = true;
						String ente = controllo.substring(0, controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length() + 1);
						chiavi = "'" + controllo.substring(controllo.indexOf("|") + 1);
					}
					else
						chiavi = controllo;

					if (!soggfascicolo) {
						//Caricamento dati da crosslink CIVICI
						if(chiavi.indexOf("|")>0)
						{
							sql = sql + " AND fabbr.foglio||'|'||"+
										" fabbr.numero||'|'||"+
										" fabbr.subalterno IN( " + chiavi + ")";
						}
						//Caricamento dati da crosslink CONTRIBUENTE e ANAGRAFE
						else if (chiavi.length() > 13)
							sql = sql + " and sogg.CODICE_FISCALE IN( " + chiavi + ")";
						else
							sql = sql + " and sogg.CODICE_FISCALE_G IN( " + chiavi + ")";
					} else {
						sql = sql + " and sogg.ID_SOGGETTO_CATASTALE IN( " + chiavi + ")";
					}
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 0)
					sql += ")";

				if (i == 1)
				{
					sql = sql + "order by Cognome, nome ";
					sql = sql + ")t) where N > " + limInf + " and N <=" + limSup;
				}
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						DupElementoListaNote elem = new DupElementoListaNote();
						elem.setIdFornitura(rs.getString("IID_FORNITURA"));
						elem.setIdNota(tornaValoreRS(rs, "IID"));
						String toponimo = tornaValoreRS(rs, "t_toponimo", "VUOTO");
						try {
							toponimo = Integer.parseInt(toponimo) + "";
							if (toponimo.equals("0")) {
								toponimo = "";
							}
						} catch (Exception e) {}
						if (toponimo != null && !toponimo.equals("")) {
							toponimo = decodificaToponimo(conn, toponimo) + " ";
						} else {
							toponimo = "";
						}
						elem.setIndirizzoImmobile(toponimo + tornaValoreRS(rs, "T_INDIRIZZO") + " " + tornaValoreRS(rs, "t_civico1") + " " + tornaValoreRS(rs, "t_civico2", "VUOTO") + " " + tornaValoreRS(rs, "t_civico3", "VUOTO"));
						elem.setCognome(tornaValoreRS(rs, "COGNOME"));
						elem.setNome(tornaValoreRS(rs, "NOME"));

						elem.setCodiceFiscale(tornaValoreRS(rs, "CODICE_FISCALE"));
						if (elem.getCodiceFiscale().equals("-"))
							elem.setCodiceFiscale(tornaValoreRS(rs, "CODICE_FISCALE_G"));
						elem.setDenominazione(tornaValoreRS(rs, "DENOMINAZIONE"));
						elem.setSede(tornaValoreRS(rs, "SEDE"));

						elem.setFoglio(tornaValoreRS(rs, "FOGLIO"));
						elem.setNumero(tornaValoreRS(rs, "NUMERO"));
						elem.setSubalterno(tornaValoreRS(rs, "SUBALTERNO"));

						if (!tornaValoreRS(rs, "SC_REGIME").equals("-"))
							elem.setRegimePatrimoniale(tornaValoreRS(rs, "SC_REGIME"));
						else if (!tornaValoreRS(rs, "SF_REGIME").equals("-"))
							elem.setRegimePatrimoniale(tornaValoreRS(rs, "SF_REGIME"));
						else
							elem.setRegimePatrimoniale("-");

						if (!tornaValoreRS(rs, "SC_FLAG_TIPO_TITOL_NOTA").equals("-"))
							elem.setFavoreContro("CONTRO");
						else if (!tornaValoreRS(rs, "SF_FLAG_TIPO_TITOL_NOTA").equals("-"))
							elem.setFavoreContro("FAVORE");
						else
							elem.setFavoreContro("NON COINVOLTO");

						elem.setFlagRettifica(tornaValoreRS(rs, "flag_rettifica", "FLAG"));
						
						elem.setCodEnte(tornaValoreRS(rs, "CODENTE"));
						
						
						
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(elem.getFoglio(), Utils.fillUpZeroInFront(elem.getNumero(),5), elem.getCodEnte());
						} catch (Exception e) {
						}
						if (coord!=null) {
							elem.setLatitudine(coord.firstObj);
							elem.setLongitudine(coord.secondObj);
						}
						
						vct.add(elem);
					}
				}
				else
				{
					if (rs.next())
					{
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put("LISTA_NOTE", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER", finder);
			
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
	
	public Hashtable mCaricareDettaglioNotaFromSoggetto(String idSoggetto) throws Exception
	{
		
		
		String chiave= getChiave(idSoggetto);
		
		return mCaricareDettaglioNota( chiave); 
	}
	
	public Hashtable mCaricareDettaglioNotaFromViaImmobile(String idVia) throws Exception
	{
		
		
		String chiave= getChiaveFromViaImmobile(idVia);
		
		return mCaricareDettaglioNota( chiave); 
	}
	
	
	public Hashtable mCaricareDettaglioNotaFromViaSoggetto(String idVia) throws Exception
	{
		
		
		String chiave= getChiaveFromViaSoggetto(idVia);
		
		return mCaricareDettaglioNota( chiave); 
	}
	
	
	private String getChiave(String idSoggetto) throws Exception
	{

		Hashtable ht = new Hashtable();
		String chiave="";
		// faccio la connessione al db
		Connection conn = null;
		java.sql.ResultSet rs =null;
		try
		{
			
			conn = this.getConnection();
			this.initialize();

			String sql = "select IID_FORNITURA,IID_NOTA from MUI_SOGGETTI where  IID=? ";

			// recupero le chiavi di ricerca

			this.setString(1, idSoggetto);
			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				String iidFornitura=tornaValoreRS(rs, "IID_FORNITURA");
				String iidNota=tornaValoreRS(rs, "IID_NOTA");
				chiave=iidFornitura+ "@"+ iidNota;
			}
			
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
			return chiave;
		}
	
	private String getChiaveFromViaImmobile(String idVia) throws Exception
	{

		Hashtable ht = new Hashtable();
		String chiave="";
		// faccio la connessione al db
		Connection conn = null;
		java.sql.ResultSet rs = null;
		try
		{
			
			conn = this.getConnection();
			this.initialize();

			String sql = "select IID_FORNITURA,IID_NOTA from MUI_FABBRICATI_IDENTIFICA where  IID=? ";

			// recupero le chiavi di ricerca

			this.setString(1, idVia);
			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				String iidFornitura=tornaValoreRS(rs, "IID_FORNITURA");
				String iidNota=tornaValoreRS(rs, "IID_NOTA");
				chiave=iidFornitura+ "@"+ iidNota;
			}
			
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
			return chiave;
		}
	
	private String getChiaveFromViaSoggetto(String idVia) throws Exception
	{

		Hashtable ht = new Hashtable();
		String chiave="";
		// faccio la connessione al db
		Connection conn = null;
		java.sql.ResultSet rs = null;
		try
		{
			
			conn = this.getConnection();
			this.initialize();

			String sql = "select IID_FORNITURA,IID_NOTA from MUI_INDIRIZZI_SOG where  IID=? ";

			// recupero le chiavi di ricerca

			this.setString(1, idVia);
			

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				String iidFornitura=tornaValoreRS(rs, "IID_FORNITURA");
				String iidNota=tornaValoreRS(rs, "IID_NOTA");
				chiave=iidFornitura+ "@"+ iidNota;
			}
			
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
			return chiave;
		}


	public Hashtable mCaricareDettaglioNota(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			if (chiave.indexOf("@")> -1){
			String idFornitura = chiave.substring(0, chiave.indexOf("@"));
			String idNota = chiave.substring(chiave.indexOf("@") + 1);
			conn = this.getConnection();
			this.initialize();

			String sql = "select * from MUI_NOTA_TRAS, EWG_TAB_COMUNI where  IID_FORNITURA=? AND IID= ? and sede_rog= uk_belfiore(+)";

			// recupero le chiavi di ricerca

			this.setString(1, idFornitura);
			this.setString(2, idNota);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			DupNota nota = new DupNota();

			while (rs.next())
			{
				// campi della lista
				nota.setIdFornitura(tornaValoreRS(rs, "IID_FORNITURA"));
				nota.setIdNota(tornaValoreRS(rs, "IID"));
				nota.setTipoNota(tornaValoreRS(rs, "TIPO_NOTA"));
				nota.setNumeroNotaTras(tornaValoreRS(rs, "NUMERO_NOTA_TRAS", "NUM"));
				nota.setProgressivoNota(tornaValoreRS(rs, "PROGRESSIVO_NOTA"));
				nota.setAnnoNota(tornaValoreRS(rs, "ANNO_NOTA"));
				nota.setDataValiditaAtto(tornaValoreRS(rs, "DATA_VALIDITA_ATTO", "DDMMYYYY"));
				nota.setDataPresAtto(tornaValoreRS(rs, "DATA_PRES_ATTO", "DDMMYYYY"));
				nota.setEsitoNota(tornaValoreRS(rs, "ESITO_NOTA"));
				nota.setEsitoNotaNonReg(tornaValoreRS(rs, "ESITO_NOTA_NON_REG"));
				nota.setDataRegInAtti(tornaValoreRS(rs, "DATA_REG_IN_ATTI", "DDMMYYYY"));
				nota.setNumeroRepertorio(tornaValoreRS(rs, "NUMERO_REPERTORIO", "NUM"));
				nota.setFlagRettifica(tornaValoreRS(rs, "FLAG_RETTIFICA", "FLAG"));
				nota.setTipoNotaRet(tornaValoreRS(rs, "TIPO_NOTA_RET"));
				nota.setNumeroNotaRet(tornaValoreRS(rs, "NUMERO_NOTA_RET", "NUM"));
				nota.setDataPresAttoRet(tornaValoreRS(rs, "DATA_PRES_ATTO_RET", "DDMMYYYY"));
				nota.setCognomeNomeRog(tornaValoreRS(rs, "COGNOME_NOME_ROG"));
				nota.setCodFiscRog(tornaValoreRS(rs, "COD_FISC_ROG"));
				if(rs.getString("DESCRIZIONE")!=null)
					nota.setSedeRog(tornaValoreRS(rs, "DESCRIZIONE"));
				else
					nota.setSedeRog(tornaValoreRS(rs, "SEDE_ROG"));

				nota.setRegistrazioneDif(tornaValoreRS(rs, "REGISTRAZIONE_DIF", "FLAG"));
				nota.setDataInDif(tornaValoreRS(rs, "DATA_IN_DIF", "DDMMYYYY"));
			}
			listaSoggetti(conn, nota);
			listaSoggettiNonCoinvolti(conn, nota);
			listaImmobili(conn, nota);
			
			ArrayList listaImmobili= (ArrayList)nota.getListImmobili();
			
			if (listaImmobili != null){
				for (int i=0; i< listaImmobili.size(); i++){
					
					DupImmobile immo = (DupImmobile)listaImmobili.get(i);
					
					//valorizzo il toponimo con la descrizione
					try{
					immo.setTToponimo(decodificaToponimo( conn, immo.getTToponimo()));
					}catch (Exception e){
						
					}
					//valorizzo il toponimo con la descrizione
					try{
					immo.setCToponimo(decodificaToponimo( conn,immo.getCToponimo()));
					}catch (Exception e){
						
					}
				}
			}
			

			ht.put("NOTE", nota);
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

	public DupNota listaSoggetti(Connection conn, DupNota nota) throws Exception
	{

		// faccio la connessione al db
		try
		{
			List listaFavore = new ArrayList();
			List listaContro = new ArrayList();
			List listaNonCoinvolti = new ArrayList();

			this.initialize();

			String sql = "SELECT sogg.iid, sogg.id_soggetto_nota, sogg.id_soggetto_catastale, sogg.tipo_soggetto," + "sogg.cognome, sogg.nome, sogg.sesso, sogg.data_nascita," + "sogg.luogo_nascita, sogg.codice_fiscale, sogg.denominazione, sogg.sede," + "sogg.codice_fiscale_g, ind.iid iid_indirizzo, ind.tipo_indirizzo, ind.comune, ind.provincia," + "ind.indirizzo, ind.cap, tito.tipo_soggetto, tito.tipologia_immobile,"
					+ "tito.sc_flag_tipo_titol_nota, tito.sc_codice_diritto," + "tito.sc_quota_numeratore, tito.sc_quota_denominatore, tito.sc_regime," + "tito.sc_soggetto_rif, tito.sf_flag_tipo_titol_nota," + "tito.sf_codice_diritto, tito.sf_quota_numeratore," + "tito.sf_quota_denominatore, tito.sf_regime, tito.sf_soggetto_rif," + "tito.id_immobile, fabb.foglio, fabb.numero, fabb.subalterno, "
					+ "dir1.DESCR_DIRITTO SC_DESCR_DIRITTO, dir2.DESCR_DIRITTO SF_DESCR_DIRITTO " + "FROM mui_soggetti sogg," + "mui_indirizzi_sog ind," + "mui_titolarita tito, " + "mui_fabbricati_identifica fabb, " + "mui_DIRITTI_DECO DIR1," + "mui_DIRITTI_DECO DIR2 " + "WHERE ( sogg.IID_FORNITURA =? AND sogg.iId_nota=?"
					+ "and (sogg.IID_FORNITURA = ind.IID_FORNITURA)" + "AND (sogg.iId_nota = ind.iId_nota)" + "AND (ind.IID_FORNITURA = tito.IID_FORNITURA)" + "AND (ind.iId_nota = tito.iId_nota)" + "AND (sogg.id_soggetto_nota = ind.id_soggetto_nota)" + "AND (ind.id_soggetto_nota = tito.id_soggetto_nota)" + "AND (fabb.IID_FORNITURA = tito.IID_FORNITURA)" + "AND (fabb.iId_nota = tito.iId_nota)"
					+ "AND (fabb.id_immobile = tito.id_immobile) " + "AND (TITO.SC_CODICE_DIRITTO IS NOT NULL OR TITO.SF_CODICE_DIRITTO IS NOT NULL)" + "AND dir1.CODI_DIRITTO (+) =  UPPER(TITO.SC_CODICE_DIRITTO)" + "AND dir2.CODI_DIRITTO   (+) = UPPER(TITO.SF_CODICE_DIRITTO)" + ") " + "ORDER BY COGNOME, NOME,FOGLIO,NUMERO,SUBALTERNO";

			// recupero le chiavi di ricerca

			this.setString(1, nota.getIdFornitura());
			this.setString(2, nota.getIdNota());

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next())
			{
				// campi della lista
				DupSoggetto sogg = new DupSoggetto();
				sogg.setIdSoggettoNota(tornaValoreRS(rs, "id_soggetto_nota"));
				sogg.setIdSoggettoCatastale(tornaValoreRS(rs, "id_soggetto_catastale"));
				sogg.setTipoSoggetto(tornaValoreRS(rs, "tipo_soggetto"));
				sogg.setCognome(tornaValoreRS(rs, "cognome"));
				sogg.setNome(tornaValoreRS(rs, "nome"));
				sogg.setSesso(tornaValoreRS(rs, "sesso"));
				sogg.setDataNascita(tornaValoreRS(rs, "data_nascita", "DDMMYYYY"));
				sogg.setLuogoNascita(tornaValoreRS(rs, "luogo_nascita"));
				sogg.setCodiceFiscale(tornaValoreRS(rs, "codice_fiscale"));
				sogg.setDenominazione(tornaValoreRS(rs, "denominazione"));
				sogg.setSede(tornaValoreRS(rs, "sede"));
				sogg.setCodiceFiscaleG(tornaValoreRS(rs, "codice_fiscale_g"));
				sogg.setIidIndirizzo(tornaValoreRS(rs, "iid_indirizzo"));
				sogg.setTipoIndirizzo(tornaValoreRS(rs, "tipo_indirizzo"));
				sogg.setComune(tornaValoreRS(rs, "comune"));
				sogg.setProvincia(tornaValoreRS(rs, "provincia"));
				sogg.setIndirizzo(tornaValoreRS(rs, "indirizzo"));
				sogg.setCap(tornaValoreRS(rs, "cap"));
				sogg.setTipoSoggetto(tornaValoreRS(rs, "tipo_soggetto"));
				sogg.setTipologiaImmobile(tornaValoreRS(rs, "tipologia_immobile"));
				sogg.setIdImmobile(tornaValoreRS(rs, "id_immobile"));

				sogg.setFoglio(tornaValoreRS(rs, "FOGLIO"));
				sogg.setNumero(tornaValoreRS(rs, "NUMERO"));
				sogg.setSub(tornaValoreRS(rs, "SUBALTERNO"));

				sogg.setScFlagTipoTitolNota(tornaValoreRS(rs, "sc_flag_tipo_titol_nota"));
				sogg.setSfFlagTipoTitolNota(tornaValoreRS(rs, "sf_flag_tipo_titol_nota"));
				
				sogg.setIid(tornaValoreRS(rs, "iid"));

				// Controllo sui flag per determinare in che modo è coinvolto il
				// soggetto
				if (!sogg.getScFlagTipoTitolNota().equals("-"))
				{
					sogg.setScCodiceDiritto(tornaValoreRS(rs, "sc_codice_diritto"));
					sogg.setScQuotaNumeratore(tornaValoreRS(rs, "sc_quota_numeratore"));
					sogg.setScQuotaDenominatore(tornaValoreRS(rs, "sc_quota_denominatore"));
					sogg.setScRegime(tornaValoreRS(rs, "sc_regime"));
					sogg.setScSoggettoRif(tornaValoreRS(rs, "sc_soggetto_rif"));
					sogg.setDescrDiritto(tornaValoreRS(rs, "SC_DESCR_DIRITTO"));
					listaContro.add(sogg);
				}
				else if (!sogg.getSfFlagTipoTitolNota().equals("-"))
				{
					sogg.setSfCodiceDiritto(tornaValoreRS(rs, "sf_codice_diritto"));
					sogg.setSfQuotaNumeratore(tornaValoreRS(rs, "sf_quota_numeratore"));
					sogg.setSfQuotaDenominatore(tornaValoreRS(rs, "sf_quota_denominatore"));
					sogg.setSfRegime(tornaValoreRS(rs, "sf_regime"));
					sogg.setSfSoggettoRif(tornaValoreRS(rs, "sf_soggetto_rif"));
					sogg.setDescrDiritto(tornaValoreRS(rs, "Sf_DESCR_DIRITTO"));
					listaFavore.add(sogg);
				}
				else
					listaNonCoinvolti.add(sogg);
			}
			if (listaContro.size() > 0)
				nota.setListSogContro(listaContro);
			if (listaFavore.size() > 0)
				nota.setListSogFavore(listaFavore);
			if (listaNonCoinvolti.size() > 0)
				nota.setListSogNonCoinvolti(listaNonCoinvolti);
			return nota;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	public DupNota listaSoggettiNonCoinvolti(Connection conn, DupNota nota) throws Exception
	{

		// faccio la connessione al db
		try
		{
			List listaFavore = new ArrayList();
			List listaContro = new ArrayList();
			List listaNonCoinvolti = new ArrayList();

			this.initialize();

			String sql = "SELECT sogg.iid, sogg.id_soggetto_nota, sogg.id_soggetto_catastale," + "sogg.tipo_soggetto, sogg.cognome, sogg.nome, sogg.sesso," + "sogg.data_nascita, sogg.luogo_nascita, sogg.codice_fiscale," + "sogg.denominazione, sogg.sede, sogg.codice_fiscale_g," + "tito.tipo_soggetto, tito.tipologia_immobile," + "tito.id_immobile," + "fabb.foglio, fabb.numero, fabb.subalterno "
					+ "FROM mui_soggetti sogg," + "mui_titolarita tito," + "mui_fabbricati_identifica fabb " + "WHERE (sogg.IID_FORNITURA = ? " + "AND sogg.iId_nota = ? " + "AND (sogg.IID_FORNITURA = tito.IID_FORNITURA) " + "AND (sogg.iId_nota = tito.iId_nota) " + "AND sogg.id_soggetto_nota is null and  tito.id_soggetto_nota is null AND TITO.ID_SOGGETTO_CATASTALE = SOGG.ID_SOGGETTO_CATASTALE "
					+ "AND (fabb.IID_FORNITURA = tito.IID_FORNITURA) " + "AND (fabb.iId_nota = tito.iId_nota) " + "AND (fabb.id_immobile = tito.id_immobile))" + "ORDER BY cognome, nome, foglio, numero, subalterno";

			// recupero le chiavi di ricerca

			this.setString(1, nota.getIdFornitura());
			this.setString(2, nota.getIdNota());

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next())
			{
				// campi della lista
				DupSoggetto sogg = new DupSoggetto();
				sogg.setIdSoggettoNota(tornaValoreRS(rs, "id_soggetto_nota"));
				sogg.setIdSoggettoCatastale(tornaValoreRS(rs, "id_soggetto_catastale"));
				sogg.setTipoSoggetto(tornaValoreRS(rs, "tipo_soggetto"));
				sogg.setCognome(tornaValoreRS(rs, "cognome"));
				sogg.setNome(tornaValoreRS(rs, "nome"));
				sogg.setSesso(tornaValoreRS(rs, "sesso"));
				sogg.setDataNascita(tornaValoreRS(rs, "data_nascita", "DDMMYYYY"));
				sogg.setLuogoNascita(tornaValoreRS(rs, "luogo_nascita"));
				sogg.setCodiceFiscale(tornaValoreRS(rs, "codice_fiscale"));
				sogg.setDenominazione(tornaValoreRS(rs, "denominazione"));
				sogg.setSede(tornaValoreRS(rs, "sede"));
				sogg.setCodiceFiscaleG(tornaValoreRS(rs, "codice_fiscale_g"));
				sogg.setTipoSoggetto(tornaValoreRS(rs, "tipo_soggetto"));
				sogg.setTipologiaImmobile(tornaValoreRS(rs, "tipologia_immobile"));
				sogg.setIdImmobile(tornaValoreRS(rs, "id_immobile"));

				sogg.setFoglio(tornaValoreRS(rs, "FOGLIO"));
				sogg.setNumero(tornaValoreRS(rs, "NUMERO"));
				sogg.setSub(tornaValoreRS(rs, "SUBALTERNO"));
				
				sogg.setIid(tornaValoreRS(rs, "iid"));

				listaNonCoinvolti.add(sogg);
			}

			if (listaNonCoinvolti.size() > 0)
				nota.setListSogNonCoinvolti(listaNonCoinvolti);
			return nota;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	public DupNota listaImmobili(Connection conn, DupNota nota) throws Exception
	{

		// faccio la connessione al db
		try
		{
			List listaimmobili = new ArrayList();
			
			String codEnte = "";
			
			this.initialize();

			String sql = "SELECT CODENT FROM SIT_ENTE WHERE 1=?";
			
			this.setInt(1, 1);
			
			prepareStatement(sql);			
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next())
			{
				codEnte = rs.getString("CODENT");
			}

			this.initialize();

			sql = "SELECT * FROM mui_fabbricati_identifica fabb," + "mui_fabbricati_info fabinfo " + "WHERE (    (fabinfo.IID_FORNITURA = fabb.IID_FORNITURA)" + "AND (fabinfo.iId_nota = fabb.iId_nota)" + "AND (fabinfo.id_immobile = fabb.id_immobile)" + "AND fabb.IID_FORNITURA =? AND fabb.iId_nota=? )";

			// recupero le chiavi di ricerca

			this.setString(1, nota.getIdFornitura());
			this.setString(2, nota.getIdNota());

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			while (rs.next())
			{
				// campi della lista

				DupImmobile immo = new DupImmobile();
				// MI_DUP_FABBRICATI_IDENTIFICA
				immo.setIid(tornaValoreRS(rs, "IID"));
				immo.setSezioneCensuaria(tornaValoreRS(rs, "SEZIONE_CENSUARIA", "VUOTO"));
				immo.setSezioneUrbana(tornaValoreRS(rs, "SEZIONE_URBANA", "VUOTO"));
				immo.setFoglio(tornaValoreRS(rs, "FOGLIO"));
				immo.setNumero(tornaValoreRS(rs, "NUMERO"));
				immo.setDenominatore(tornaValoreRS(rs, "DENOMINATORE"));
				immo.setSubalterno(tornaValoreRS(rs, "SUBALTERNO"));
				immo.setEdificabilita(tornaValoreRS(rs, "EDIFICABILITA"));
				immo.setTipoDenuncia(tornaValoreRS(rs, "TIPO_DENUNCIA"));
				immo.setNumeroProtocollo(tornaValoreRS(rs, "NUMERO_PROTOCOLLO"));
				immo.setAnno(tornaValoreRS(rs, "ANNO"));
				immo.setIdCatastaleImmobile(tornaValoreRS(rs, "ID_CATASTALE_IMMOBILE"));
				String graffato=rs.getString("FLAG_GRAFFATO");
				if(graffato!=null && !graffato.equals("0"))
					immo.setFlagGraffato("SI");
				else
					immo.setFlagGraffato("NO");

				// MI_DUP_FABBRICATI_INFO
				immo.setAnnotazioni(tornaValoreRS(rs, "ANNOTAZIONI"));
				immo.setCategoria(tornaValoreRS(rs, "CATEGORIA"));
				immo.setClasse(tornaValoreRS(rs, "CLASSE"));
				immo.setCodiceEsito(tornaValoreRS(rs, "CODICE_ESITO"));
				immo.setCCivico1(tornaValoreRS(rs, "C_CIVICO1"));
				immo.setCCivico2(tornaValoreRS(rs, "C_CIVICO2", "VUOTO"));
				immo.setCCivico3(tornaValoreRS(rs, "C_CIVICO3", "VUOTO"));
				immo.setCEdificio(tornaValoreRS(rs, "C_EDIFICIO"));
				immo.setCIndirizzo(tornaValoreRS(rs, "C_INDIRIZZO"));
				immo.setCInterno1(tornaValoreRS(rs, "C_INTERNO1"));
				immo.setCInterno2(tornaValoreRS(rs, "C_INTERNO2"));
				immo.setCLotto(tornaValoreRS(rs, "C_LOTTO"));
				immo.setCPiano1(tornaValoreRS(rs, "C_PIANO1"));
				immo.setCPiano2(tornaValoreRS(rs, "C_PIANO2"));
				immo.setCPiano3(tornaValoreRS(rs, "C_PIANO3"));
				immo.setCPiano4(tornaValoreRS(rs, "C_PIANO4"));
				immo.setCScala(tornaValoreRS(rs, "C_SCALA"));
				immo.setCToponimo(tornaValoreRS(rs, "C_TOPONIMO"));
				
				immo.setMc(tornaValoreRS(rs, "MC"));
				immo.setMq(tornaValoreRS(rs, "MQ"));
				immo.setNaturaImmobile(tornaValoreRS(rs, "NATURA_IMMOBILE"));
				//immo.setRenditaEuro(tornaValoreRS(rs, "RENDITA_EURO"));
				//modificato Filippo Mazzini 09.09.2011 su segnalazione comune Chiari
				//il valore sarebbe in centesimi (da verifiche anche su altri DB, è plausibile...)
				String rendita = tornaValoreRS(rs, "RENDITA_EURO");
				try {
					DecimalFormat df = new DecimalFormat();
					df.setGroupingUsed(false);
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator(',');
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(2);
					df.setMinimumFractionDigits(2);
					rendita = df.format(Double.parseDouble(rendita) / 100);
				} catch (Exception e) {}
				immo.setRenditaEuro(rendita);
				//fine modifica Filippo Mazzini 09.09.2011 
				immo.setSuperficie(tornaValoreRS(rs, "SUPERFICIE"));
				immo.setTCivico1(tornaValoreRS(rs, "T_CIVICO1"));
				immo.setTCivico2(tornaValoreRS(rs, "T_CIVICO2", "VUOTO"));
				immo.setTCivico3(tornaValoreRS(rs, "T_CIVICO3", "VUOTO"));
				immo.setTEdificio(tornaValoreRS(rs, "T_EDIFICIO"));
				immo.setTIndirizzo(tornaValoreRS(rs, "T_INDIRIZZO"));
				immo.setTInterno1(tornaValoreRS(rs, "T_INTERNO1"));
				immo.setTInterno2(tornaValoreRS(rs, "T_INTERNO2", "VUOTO"));
				immo.setTLotto(tornaValoreRS(rs, "T_LOTTO"));
				immo.setTPiano1(tornaValoreRS(rs, "T_PIANO1"));
				immo.setTPiano2(tornaValoreRS(rs, "T_PIANO2", "VUOTO"));
				immo.setTPiano3(tornaValoreRS(rs, "T_PIANO3", "VUOTO"));
				immo.setTPiano4(tornaValoreRS(rs, "T_PIANO4", "VUOTO"));
				immo.setTScala(tornaValoreRS(rs, "T_SCALA"));
				immo.setTToponimo(tornaValoreRS(rs, "T_TOPONIMO"));
				
				immo.setVani(tornaValoreRS(rs, "VANI"));
				immo.setZona(tornaValoreRS(rs, "ZONA"));
				
				immo.setCodEnte(codEnte);

				GenericTuples.T2<String,String> coord = null;
				try {
					coord = getLatitudeLongitude(immo.getFoglio(), Utils.fillUpZeroInFront(immo.getNumero(),5), immo.getCodEnte());
				} catch (Exception e) {
				}
				if (coord!=null) {
					immo.setLatitudine(coord.firstObj);
					immo.setLongitudine(coord.secondObj);
				}
				
				listaimmobili.add(immo);
			}

			if (listaimmobili.size() > 0)
				nota.setListImmobili(listaimmobili);

			return nota;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	
	private String decodificaToponimo(Connection conn, String codTopo) throws Exception {
		String desTopo = "";
		if (codTopo == null || codTopo.equals("")) {
			return desTopo;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT DESCR FROM CAT_D_TOPONIMI WHERE PK_ID = ?";
			pstmt = conn.prepareStatement(sql);
			try {
				pstmt.setInt(1, Integer.parseInt(codTopo));
			} catch (Exception e) {
				return desTopo;
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				desTopo = rs.getString("DESCR");
			}
			return desTopo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}		
	}

}
