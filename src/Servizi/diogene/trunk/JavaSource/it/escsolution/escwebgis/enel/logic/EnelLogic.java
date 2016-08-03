/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.enel.logic;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.enel.bean.Enel;
import it.escsolution.escwebgis.enel.bean.EnelFinder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class EnelLogic extends EscLogic
{
	private String appoggioDataSource;

	public EnelLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_ENEL = "LISTA_ENEL@EnelLogic";
	public final static String LISTA_DETTAGLIO_ENEL = "LISTA_DETTAGLIO_ENEL@EnelLogic";
	public final static String LISTA_SEMESTRALITA_UTENZE = "LISTA_SEMESTRALITA_UTENZE@EnelLogic";
	public final static String ENEL = "ENEL@EnelLogic";
	private final static String SQL_SELECT_LISTA =
		"SELECT * FROM ( SELECT ROWNUM AS N, SIT_E_UTENTI.pk_id, SIT_E_UTENTI.nominativo, SIT_E_UTENTI.PIVA_CODFISC" +
		" FROM SIT_E_UTENTI, SIT_E_UTENZE WHERE 1=? AND SIT_E_UTENTI.PK_ID = SIT_E_UTENZE.PK_ID  ";

	private final static String SQL_SELECT_COUNT_LISTA =
		"SELECT COUNT(*) conteggio " +
		" FROM SIT_E_UTENTI, SIT_E_UTENZE WHERE 1=? AND SIT_E_UTENTI.PK_ID = SIT_E_UTENZE.PK_ID";

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaEnel(Vector listaPar, EnelFinder finder)
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
			/*
			 * per ora disabilito il conteggione sql = SQL_SELECT_COUNT_ALL;
			 * this.initialize(); this.setInt(indice,1); indice ++;
			 * prepareStatement(sql); rs = executeQuery(conn); if (rs.next()){
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
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					 if(i == 0 )
						sql = sql + ") WHERE CHIAVE in (" + finder.getKeyStr() + ")";
					if (i == 1)
						sql = sql + " AND CHIAVE in (" + finder.getKeyStr() + ")";
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1 && finder.getKeyStr().equals(""))
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				/*if(i == 0 && finder.getKeyStr().equals(""))
					sql = sql + ")";*/

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Enel enel = new Enel();
						enel.setPkId(rs.getString("PK_ID"));
						if(rs.getString("NOMINATIVO")!=null)
							enel.setNominativo(rs.getString("NOMINATIVO"));
						else
							enel.setNominativo("-");
						if(rs.getString("PIVA_CODFISC")!=null)
							enel.setPivacodfisc(rs.getString("PIVA_CODFISC"));
						else
							enel.setPivacodfisc("-");
						vct.add(enel);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_ENEL, vct);
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

	public Hashtable mCaricareDettaglioEnel(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			this.initialize();
			String sql ="SELECT pk_id, nominativo, PIVA_CODFISC FROM SIT_E_UTENTI WHERE PK_ID=? ";

			int indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Enel enel = new Enel();
			if (rs.next())
			{
				enel.setPkId(rs.getString("PK_ID"));
				enel.setNominativo(rs.getString("NOMINATIVO"));
				enel.setPivacodfisc(rs.getString("PIVA_CODFISC"));
			}
			ht.put(ENEL, enel);
			// POI LEGGO I DATI RELATIVI ALLE UTENZE
			sql =
				"SELECT sit_e_utenze.identificativo IDENTIFICATIVO, sit_e_utenze.nominativo NOMINATIVO," +
				" sit_e_utenze.piva_codfisc PIVA_CODFISC, sit_e_utenze.sedime SEDIME, sit_e_utenze.indirizzo INDIRIZZO," +
				" sit_e_stato_utenza.su_des SU_DES" +
				" FROM sit_e_utenti, sit_e_utenze, sit_e_stato_utenza" +
				" WHERE sit_e_utenze.stato_utenza = sit_e_stato_utenza.su_cod" +
				" AND sit_e_utenti.pk_id = sit_e_utenze.pk_id AND sit_e_utenti.PK_ID=?";
			this.initialize();
			this.setString(1, enel.getChiave());
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				ArrayList listaDettEnel = new ArrayList();
				do
				{
					Enel enelList = new Enel();
					enelList.setIdentificativo(rs.getString("IDENTIFICATIVO"));
					enelList.setNominativo(rs.getString("NOMINATIVO"));
					enelList.setPivacodfisc(rs.getString("PIVA_CODFISC"));
					enelList.setIndirizzo(rs.getString("SEDIME")+" "+rs.getString("INDIRIZZO"));
					enelList.setStatoUtenza(rs.getString("SU_DES"));
					listaDettEnel.add(enelList);
				} while (rs.next());
				ht.put(LISTA_DETTAGLIO_ENEL, listaDettEnel);
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
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}

	public Hashtable mCaricareDatiFormRicerca(String utente)
		throws NamingException, SQLException
	{
		Hashtable ht = new Hashtable();
		this.setDatasource(JNDI_CATCOSPOLETO);
		Connection conn = this.getConnection();
		try
		{
			//ht.put("LISTA_TIPI_CONC",mCaricareTipiConc(conn));
			ht.put("LISTA_COMUNI",new ComuniLogic(this.envUtente).getListaComuniUtente(utente));
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

		return ht;
	}

	public Hashtable mCaricareListaSemestralitaUtenze(String identificativo) throws Exception
	{

		Hashtable ht = new Hashtable();
		ArrayList vct = new ArrayList();
		String sql = "";
		String conteggio = "";
		long conteggione = 0;
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			sql =
				"SELECT SIT_E_SEMESTRI.FK_IDENTIFICATIVO, SIT_E_SEMESTRI.POTENZA_IMPEGNATA," +
				" SIT_E_SEMESTRI.CONSUMO_MEDIO_MENSILE, SIT_E_SEMESTRI.DATA_ALLACCIO," +
				" SIT_E_SEMESTRI.DATA_CONTRATTO, SIT_E_SEMESTRI.CODICE_CONTRATTO," +
				" SIT_E_SEMESTRI.RECAPITO_NOMINATIVO, SIT_E_SEMESTRI.RECAPITO_INDIRIZZO," +
				" SIT_E_SEMESTRI.RECAPITO_LOCALITA, SIT_E_SEMESTRI.RECAPITO_CAP," +
				" SIT_E_SEMESTRI.SEMESTRE, SIT_E_STATO_UTENZA.SU_DES STATO," +
				" SIT_E_SEMESTRI.CATEG_MERCE" +
				" FROM SIT_E_UTENZE, SIT_E_SEMESTRI, SIT_E_STATO_UTENZA" +
				" WHERE SIT_E_UTENZE.IDENTIFICATIVO = SIT_E_SEMESTRI.FK_IDENTIFICATIVO" +
				" AND SIT_E_SEMESTRI.STATO_UTENZA = SIT_E_STATO_UTENZA.SU_COD" +
				" AND SIT_E_UTENZE.IDENTIFICATIVO =?";
			this.initialize();
			this.setString(1, identificativo);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				Enel enel = new Enel();
				enel.setIdentificativo(rs.getString("FK_IDENTIFICATIVO"));
				enel.setPotenzaImpegnata(rs.getString("POTENZA_IMPEGNATA"));
				enel.setConsumoMedioMensile(rs.getString("CONSUMO_MEDIO_MENSILE"));
				enel.setDataAllaccio(rs.getString("DATA_ALLACCIO"));
				enel.setDataContratto(rs.getString("DATA_CONTRATTO"));
				enel.setCodiceContratto(rs.getString("CODICE_CONTRATTO"));
				enel.setRecapitoNominativo(rs.getString("RECAPITO_NOMINATIVO"));
				enel.setRecapitoIndirizzo(rs.getString("RECAPITO_INDIRIZZO"));
				enel.setRecapitoLocalita(rs.getString("RECAPITO_LOCALITA"));
				enel.setRecapitoCap(rs.getString("RECAPITO_CAP"));
				enel.setSemestre(rs.getString("SEMESTRE"));
				enel.setStatoUtenza(rs.getString("STATO"));
				enel.setCategMerce(rs.getString("CATEG_MERCE"));
				vct.add(enel);
			}
			ht.put(EnelLogic.LISTA_SEMESTRALITA_UTENZE, vct);

			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = identificativo;
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




}
