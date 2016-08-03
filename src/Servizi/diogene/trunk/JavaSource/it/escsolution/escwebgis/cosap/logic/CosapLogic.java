/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.cosap.logic;

import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.cosap.bean.Autorizzazione;
import it.escsolution.escwebgis.cosap.bean.CosapBean;
import it.escsolution.escwebgis.cosap.bean.Diffide;
import it.escsolution.escwebgis.cosap.bean.ElementoLista;
import it.escsolution.escwebgis.cosap.bean.ElementoListaFinder;
import it.escsolution.escwebgis.cosap.bean.InvitoPagamento;
import it.escsolution.escwebgis.cosap.bean.OccAbu;
import it.escsolution.escwebgis.cosap.bean.TipoOccupazione;
import it.escsolution.escwebgis.cosap.bean.Verbali;
import it.escsolution.escwebgis.cosap.bean.Versamenti;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class CosapLogic extends EscLogic
{
	private String appoggioDataSource;

	public CosapLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource=eu.getDataSource();
		SQL_SELECT_LISTA = getProperty("sql.SELECT_LISTA");
		SQL_SELECT_COUNT_LISTA = getProperty("sql.SELECT_COUNT_LISTA");
		SQL_SELECT_COUNT_ALL  =  SQL_SELECT_COUNT_LISTA;
		SQL_SELECT_TIPO_OCCUPAZIONE_A = getProperty("sql.SELECT_TIPI_OCCUPAZIONE_A");
		SQL_SELECT_TIPO_OCCUPAZIONE_D = getProperty("sql.SELECT_TIPI_OCCUPAZIONE_D");

		SQL_SELECT_DETTAGLIO1 = getProperty("sql.SELECT_DETTAGLIO1");
		SQL_SELECT_DETTAGLIO2 = getProperty("sql.SELECT_DETTAGLIO2");
		SQL_SELECT_DETTAGLIO3 = getProperty("sql.SELECT_DETTAGLIO3");
		SQL_SELECT_DETTAGLIO4 = getProperty("sql.SELECT_DETTAGLIO4");
		SQL_SELECT_DETTAGLIO5 = getProperty("sql.SELECT_DETTAGLIO5");
		SQL_SELECT_DETTAGLIO6 = getProperty("sql.SELECT_DETTAGLIO6");
		

	
	}

	public  static String LISTA_DATI_COSAP = CosapLogic.class.getName() + "@LISTA_DATI_COSAP";
	public  static String DETT_COSAP = CosapLogic.class.getName() + "@DETT_COSAP";
	public static final String FINDER = "FINDER49";


	private  static String SQL_SELECT_LISTA = null;

	private  static String SQL_SELECT_COUNT_LISTA = null;
	
	private  static String SQL_SELECT_COUNT_ALL = null;

	private  static String SQL_SELECT_TIPO_OCCUPAZIONE_A  = null;
	private  static String SQL_SELECT_TIPO_OCCUPAZIONE_D  = null;

	private  static String SQL_SELECT_DETTAGLIO1  = null;
	private  static String SQL_SELECT_DETTAGLIO2  = null;
	private  static String SQL_SELECT_DETTAGLIO3  = null;
	private  static String SQL_SELECT_DETTAGLIO4  = null;
	private  static String SQL_SELECT_DETTAGLIO5  = null;
	private  static String SQL_SELECT_DETTAGLIO6  = null;

	public final String AUTORIZZAZIONI = "AUTORIZZAZIONI";
	public final String DIFFIDE = "DIFFIDE";
	

	public Vector mCaricareTipoOccupazione(String tipoInformazione) throws Exception
	{
		Vector vct = new Vector();
		Connection conn= null;
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			if (tipoInformazione.equals(AUTORIZZAZIONI))
				sql = SQL_SELECT_TIPO_OCCUPAZIONE_A;
			else
				sql = SQL_SELECT_TIPO_OCCUPAZIONE_D;
			this.initialize();
			prepareStatement(sql);
			this.setInt(1, 1);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			vct.add(new TipoOccupazione("","Tutte"));
			while (rs.next()) {
				vct.add(new TipoOccupazione(rs.getString(1),rs.getString(2)));
			}
		}
		catch (Exception e) {
			
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
			
			return vct;
		}

		
	}

	
	public Hashtable mCaricareLista(Vector listaPar, ElementoListaFinder finder) throws Exception
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
					String chiavi = "";
					boolean soggfascicolo = false;

					if (controllo.indexOf(SoggettoFascicoloLogic.COST_PROCEDURA) > 0)
					{
						soggfascicolo = true;
						String ente = controllo.substring(0, controllo.indexOf("|"));
						ente = ente.substring(SoggettoFascicoloLogic.COST_PROCEDURA.length() + 1);
						chiavi = "'" + controllo.substring(controllo.indexOf("|") + 1);
						// qui non ancora implemenato
					}
					else
						sql = sql + "and vs_cod_cnt in (" +  finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1)
				{
					sql = sql + ")t) where N > " + limInf + " and N <=" + limSup;
				}
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						
						
						ElementoLista elem = new ElementoLista();
						
						elem.setCodiceContribuente(rs.getString("vs_cod_cnt"));
						elem.setCodiceFiscale(rs.getString("VS_COD_FIS"));
						elem.setDescrizoneContribuente(rs.getString("VS_DES_CNT"));
						elem.setPartitaIva(rs.getString("VS_PAR_IVA"));

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
			ht.put(LISTA_DATI_COSAP, vct);
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

	public Hashtable mCaricareDettaglio(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		java.sql.ResultSet rs6=null;
		java.sql.ResultSet rs5=null;
		java.sql.ResultSet rs4=null;
		java.sql.ResultSet rs3=null;
		java.sql.ResultSet rs2=null;
		java.sql.ResultSet rs=null;
		try
		{
			conn = this.getConnection();
			this.initialize();

			
		       
			String sql = SQL_SELECT_DETTAGLIO1;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			CosapBean cb = new CosapBean();

			while (rs.next())
			{
				Autorizzazione a = new Autorizzazione();
				a.setAnnoProtocollo(rs.getString("A_NUMERO_PROTOCOLLO"));
				a.setCivico(rs.getString("AC_NUM_CIV"));
				a.setIndirizzo(rs.getString("DES_VIA"));
				a.setNumeroProtocollo(rs.getString("A_NUMERO_PROTOCOLLO"));
				a.setTipoOccupazione(new TipoOccupazione(rs.getString("A_TIPO_OCCUPAZIONE"),rs.getString("DESC_TIPO_OCCUPAZIONE")));
				a.setDescrTipoDocumento(rs.getString("DESCR_TIPO_DOCUMENTO"));
				a.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
				a.setAnnoDocumento(rs.getString("ANNO_DOCUMENTO"));
				a.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				cb.addAutorizzazione(a);
				ElementoLista e = new ElementoLista();
				e.setCodiceContribuente(rs.getString("VS_COD_CNT"));
				e.setCodiceFiscale(rs.getString("VS_COD_FIS"));
				e.setDescrizoneContribuente(rs.getString("VS_DES_CNT"));
				e.setPartitaIva(rs.getString("VS_PAR_IVA"));
				cb.setDatiContribuente(e);
				
			}


			
			this.initialize();
			sql = SQL_SELECT_DETTAGLIO2;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs2.next())
			{
				Diffide d = new Diffide();
				d.setCivico(rs2.getString("AC_NUM_CIV"));
				d.setIndirizzo(rs2.getString("DES_VIA"));
				d.setTipoOccupazione(new TipoOccupazione(rs2.getString("D_TIPO_OCCUPAZIONE"),rs2.getString("DESCR_TIPO_OCCUPAZIONE")));
				d.setAnnoRiferimento(rs2.getString("ANNO_RIFERIMENTO"));
				d.setNumeroProvvedimento(rs2.getString("NUMERO_PROVVEDIMENTO"));
				d.setCodiceZona(rs2.getString("DESCR_COD_ZONA"));
				d.setStatoAttuale(rs2.getString("DESCR_STATO_ATTUALE"));
				d.setImportoOccupazione(rs2.getString("IMPORTO_OCCUPAZIONE"));
				cb.addDiffide(d);
			}

			this.initialize();
			sql = SQL_SELECT_DETTAGLIO4;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs4 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs4.next())
			{
				InvitoPagamento i = new InvitoPagamento();
				i.setCivico(rs4.getString("AC_NUM_CIV") + rs4.getString("AC_ESP_CIV"));
				i.setIndirizzo(rs4.getString("DES_VIA"));
				i.setCodOggetto(rs4.getString("COD_OGGETTO_ANAGRAFICO"));
				i.setTipoOccupazione(rs4.getString("DESCR_TIPO_OCCUPAZIONE"));
				i.setTipoProvvedimento(rs4.getString("DESCR_TIPO_PROVVEDIMENTO"));
				i.setStatoAttuale(rs4.getString("DESCR_STATO_ATTUALE"));
				i.setNumeroProvvedimento(rs4.getString("NUMERO_PROVVEDIMENTO"));
				i.setAnnoRiferimento(rs4.getString("ANNO_RIFERIMENTO"));
				cb.addInvitiPagamento(i);
			}
			
			this.initialize();
			sql = SQL_SELECT_DETTAGLIO5;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs5 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs5.next())
			{
				Verbali v = new Verbali();
				v.setCivico(rs5.getString("AC_NUM_CIV") + rs5.getString("AC_ESP_CIV"));
				v.setIndirizzo(rs5.getString("DES_VIA"));
				v.setCodOggetto(rs5.getString("COD_OGGETTO_ANAGRAFICO"));
				v.setTipoOccupazione(rs5.getString("DESCR_TIPO_OCCUPAZIONE"));
				v.setNumeroProtocollo(rs5.getString("NUMERO_PROTOCOLLO"));
				v.setAnnoProtocollo(rs5.getString("ANNO_PROTOCOLLO"));
				v.setAnnoRiferimento(rs5.getString("ANNO_RIFERIMENTO"));
				cb.addVerbali(v);
			}			

			this.initialize();
			sql = SQL_SELECT_DETTAGLIO6;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs6 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			while (rs6.next())
			{
				Versamenti v = new Versamenti();
				v.setAnnoVersamento(rs6.getString("ANNO_VERSAMENTO"));
				v.setDataVersamento(rs6.getString("DATA_VERSAMENTO"));
				v.setImportoVersamento(rs6.getString("IMPORTO_VERSAMENTO"));
				v.setNumeroVersamento(rs6.getString("NUMERO_VERSAMENTO"));
				
				cb.addVersamenti(v);
			}		
			
			ht.put(CosapLogic.DETT_COSAP, cb);
			
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
			if (rs!=null)
				rs.close();
			if (rs2!=null)
				rs2.close();
			if (rs3!=null)
				rs3.close();
			if (rs4!=null)
				rs4.close();
			if (rs5!=null)
				rs5.close();
			if (rs6!=null)
				rs6.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

		
		
		
	}



}
