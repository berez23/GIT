package it.escsolution.escwebgis.concessioni.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORM;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORMAnagrafe;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORMFinder;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORMOggetti;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ConcessioniINFORMLogic extends EscLogic
{
	private String appoggioDataSource;

	public ConcessioniINFORMLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER46";
	public final static String LISTA_CONCESSIONI_INFORM = "LISTA_CONCESSIONI_INFORM@ConcessioniINFORMLogic";
	public final static String CONCESSIONI_INFORM = "CONCESSIONI_INFORM@ConcessioniINFORMLogic";
	private final static String SQL_SELECT_LISTA = 	" SELECT * FROM (SELECT sel.*, rownum n FROM (SELECT DISTINCT c.pk_conc pk_conc,  c.rif_numero numero_protocollo, "+
        											" c.rif_anno anno_protocollo, c.data_protocollo, p.codice_fiscale, "+
													" p.cognome_ragsoc, p.nome , decode (cp.tipo_soggetto,1,'Proprietario','Richiedente') TIPO_SOGGETTO "+
													" FROM mi_concessioni c, "+
													" mi_concessioni_persona p, "+
													" mi_concessioni_conc_per cp,  "+
													" mi_concessioni_oggetto o  "+
													" WHERE p.pk_persona = cp.fk_persona " +
													" and O.FK_CONC = CP.FK_CONC "+
													" AND cp.fk_conc = c.pk_conc AND 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from (" +SQL_SELECT_LISTA;

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaConcessioni(Vector listaPar, ConcessioniINFORMFinder finder) 
		throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String orderByLista = "  ";
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
						sql = sql + ")) WHERE CHIAVE in (" + finder.getKeyStr() + ")";
					if (i == 1)
						sql = sql + ") WHERE CHIAVE in (" + finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1 && finder.getKeyStr().equals(""))
				{
					sql = sql + orderByLista;
					sql = sql + ") sel) where N > " + limInf + " and N <=" + limSup;
				}
				if(i == 0 && finder.getKeyStr().equals(""))
					sql = sql + ") sel ))";


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						ConcessioniINFORM conc = new ConcessioniINFORM();
						conc.setPkConc(tornaValoreRS(rs,"pk_conc"));
						conc.setNumeroProtocollo(tornaValoreRS(rs,"numero_protocollo"));
						conc.setAnnoProtocollo(tornaValoreRS(rs,"anno_protocollo"));
						conc.setDataProtocollo(tornaValoreRS(rs,"data_protocollo","YYYY-MM-DD"));
						conc.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
						conc.setCognomeRagSoc(tornaValoreRS(rs,"cognome_ragsoc"));
						conc.setNome(tornaValoreRS(rs,"nome"));
						conc.setTipoSoggetto(tornaValoreRS(rs,"TIPO_SOGGETTO"));
						vct.add(conc);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_CONCESSIONI_INFORM, vct);
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

	public Hashtable mCaricareDettaglioConcessioni(String chiave) throws Exception
	{
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
		
			//Dati generali
			conn = this.getConnection();
			String sql = " select m.CODICE_FASCICOLO FASICOLO, "+
				" m.RIF_NUMERO numero_protocollo, m.RIF_ANNO anno_protocollo, "+
				" m.DATA_PROTOCOLLO data_protocollo, m.TIPO_DOCUMENTO "+
				" from mi_concessioni m where m.PK_CONC = ? ";
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ConcessioniINFORM conc = new ConcessioniINFORM();
			conc.setPkConc(chiave);
			if (rs.next())
			{ 
				conc.setFascicolo(tornaValoreRS(rs,"FASICOLO"));
				conc.setNumeroProtocollo(tornaValoreRS(rs,"numero_protocollo"));
				conc.setAnnoProtocollo(tornaValoreRS(rs,"anno_protocollo"));
				conc.setDataProtocollo(tornaValoreRS(rs,"data_protocollo","YYYY-MM-DD"));
				conc.setTipoDocumento(tornaValoreRS(rs,"TIPO_DOCUMENTO"));
			}
			else 
				throw new Exception("Impossibile recuperare i dati");
			
			
			//contenuti dichiarativi
			sql = "select m.DESCRIZIONE_INTERVENTO,  "+
			" i.RECUPERO_SOTTOTETTO ,  "+
			" i.CAMBIO_USO ,  "+
			" i.INSERIMENTO_BAGNO ,  "+
			" i.INSTALLAZIONE_ASCENSORE ,  "+
			" i.FUSIONE_FRAZIONAMENTO ,  "+
			" i.DEMOLIZIONE ,  "+
			" i.MANUTENZIONE_STRAORDINARIA  "+
			" from mi_concessioni_intervento i, mi_concessioni m "+
			" where m.PK_CONC = i.FK_CONC  "+
			" and i.fk_conc = ?";	
			
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				conc.setDescrizioneIntervento(tornaValoreRS(rs,"DESCRIZIONE_INTERVENTO"));
				List<String> tipo = new ArrayList<String> ();
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"RECUPERO_SOTTOTETTO")))
				{
					tipo.add("RECUPERO SOTTOTETTO");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"CAMBIO_USO")))
				{
					tipo.add("CAMBIO USO");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"INSERIMENTO_BAGNO")))
				{
					tipo.add("INSERIMENTO BAGNO");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"INSTALLAZIONE_ASCENSORE")))
				{
					tipo.add("INSTALLAZIONE ASCENSORE");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"FUSIONE_FRAZIONAMENTO")))
				{
					tipo.add("FUSIONE FRAZIONAMENTO");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"DEMOLIZIONE")))
				{
					tipo.add("DEMOLIZIONE");
				}
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"MANUTENZIONE_STRAORDINARIA")))
				{
					tipo.add("MANUTENZIONE STRAORDINARIA");
				}
				conc.setTipoIntervento(tipo);
				
			}		
			
			
			//anagrafe			
			sql = "SELECT p.codice_fiscale, p.cognome_ragsoc, p.nome, "+
			" pr.data_res data_residenza, pr.indirizzo, pr.civico, pr.citta "+
			" FROM mi_concessioni c, "+
			" mi_concessioni_persona p, "+
			" mi_concessioni_conc_per cp, "+
			" mi_concessioni_pers_res pr "+
			" WHERE p.pk_persona = cp.fk_persona "+
			" AND cp.fk_conc = c.pk_conc "+
			" AND pr.fk_persona = p.pk_persona "+
			// marcoric 5-12-2008  assurdo che data protocollo = data_res - forse vale solo per milnao??
			// " AND c.data_protocollo = pr.data_res "+
			" AND cp.tipo_soggetto = ? "+
			" AND c.pk_conc = ?";	
			
			//richiedenti
			this.initialize();
			this.setString(1, "1");
			this.setString(2, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			List<ConcessioniINFORMAnagrafe> ric = new ArrayList<ConcessioniINFORMAnagrafe>();
			while (rs.next())
			{
				ConcessioniINFORMAnagrafe a = new ConcessioniINFORMAnagrafe();
				a.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
				a.setCognomeRagSoc(tornaValoreRS(rs,"cognome_ragsoc"));
				a.setNome(tornaValoreRS(rs,"nome"));
				a.setDataResidenza(tornaValoreRS(rs,"data_residenza","YYYY-MM-DD"));
				a.setIndirizzo(tornaValoreRS(rs,"indirizzo"));
				a.setCivico(tornaValoreRS(rs,"civico"));
				a.setCitta(tornaValoreRS(rs,"citta"));				
				ric.add(a);
			}
			conc.setRichiedenti(ric);
			
			
			//proprietari
			this.initialize();
			this.setString(1, "2");
			this.setString(2, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			List<ConcessioniINFORMAnagrafe> prop = new ArrayList<ConcessioniINFORMAnagrafe>();
			while (rs.next())
			{
				ConcessioniINFORMAnagrafe a = new ConcessioniINFORMAnagrafe();
				a.setCodiceFiscale(tornaValoreRS(rs,"codice_fiscale"));
				a.setCognomeRagSoc(tornaValoreRS(rs,"cognome_ragsoc"));
				a.setNome(tornaValoreRS(rs,"nome"));
				a.setDataResidenza(tornaValoreRS(rs,"data_residenza","YYYY-MM-DD"));
				a.setIndirizzo(tornaValoreRS(rs,"indirizzo"));
				a.setCivico(tornaValoreRS(rs,"civico"));
				a.setCitta(tornaValoreRS(rs,"citta"));				
				prop.add(a);
			}
			conc.setProprietari(prop);
			
			
			
			//oggetti
			/*sql = "SELECT foglio, particella, subalterno, indirizzo, civico, scala, piano, "+
				" destinazione_uso, tutela vincolo_soprintendenza "+
				" FROM mi_concessioni_oggetto "+
				" WHERE fk_conc = ? "+
				" order by indirizzo";	
			*/
			sql = "SELECT foglio, lpad(particella,5,'0') particella , subalterno, indirizzo, civico, scala, piano,  "+
					" destinazione_uso, tutela vincolo_soprintendenza , "+
					" ( "+
					" SELECT MAX( DATA_FINE_VAL) FROM sitiuiu , mi_concessioni "+
					" WHERE  "+
					" mi_concessioni.PK_CONC = c.fk_conc and "+
					" cod_nazionale = (select uk_belfiore from ewg_tab_comuni)  "+
					" and foglio = c.foglio "+
					" and particella = lpad(c.particella,5,'0')   "+
					" and sub = ' ' "+
					" and unimm = decode(c.subalterno,null,0,c.subalterno)  "+
					" and mi_concessioni.DATA_PROTOCOLLO BETWEEN DATA_INIZIO_VAL AND DATA_FINE_VAL "+
					" ) DATA_CATASTO, (select uk_belfiore from ewg_tab_comuni) COD_ENTE "+
					" FROM mi_concessioni_oggetto c "+
					" WHERE fk_conc = ? "+
					" order by indirizzo";
			
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			List<ConcessioniINFORMOggetti> oggetti = new ArrayList<ConcessioniINFORMOggetti>();
			while (rs.next())
			{
				ConcessioniINFORMOggetti oggetto = new ConcessioniINFORMOggetti();
				oggetto.setFoglio(tornaValoreRS(rs,"foglio"));
				oggetto.setParticella(tornaValoreRS(rs,"particella"));
				oggetto.setSubalterno(tornaValoreRS(rs,"subalterno"));
				oggetto.setIndirizzo(tornaValoreRS(rs,"indirizzo"));
				oggetto.setCivico(tornaValoreRS(rs,"civico"));
				oggetto.setScala(tornaValoreRS(rs,"scala"));
				oggetto.setPiano(tornaValoreRS(rs,"piano"));
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"destinazione_uso")))
					oggetto.setDestinazioneUso("SI");
				else
					oggetto.setDestinazioneUso(tornaValoreRS(rs,"destinazione_uso"));
				if("SI".equalsIgnoreCase(tornaValoreRS(rs,"vincolo_soprintendenza")))
					oggetto.setVincoloSoprintendenza("SI");
				else
					oggetto.setVincoloSoprintendenza("NO");	
				oggetto.setDataFineACatasto(tornaValoreRS(rs,"DATA_CATASTO","YYYY-MM-DD"));
				oggetto.setCodEnte(tornaValoreRS(rs,"COD_ENTE"));
				oggetti.add(oggetto);
			}
			conc.setOggetti(oggetti);
			
			
			
			//oggetti
			sql = "select PK_IMMAGINE,NOME_IMMAGINE from mi_concessioni_immagine where FK_CONC = ?";	
			
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			HashMap<String, String> tiff = new HashMap<String, String>();
			while (rs.next())
			{
				tiff.put(tornaValoreRS(rs,"PK_IMMAGINE"),tornaValoreRS(rs,"NOME_IMMAGINE"));
			}
			conc.setTiff(tiff);
			
			
			
			ht.put(CONCESSIONI_INFORM, conc);
			
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

	public String[] mCaricareDatoImg(String chiave) 
	throws Exception
	{
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		// faccio la connessione al db
		Connection conn = null;
		String[] val = new String[2];
		try
		{
		
			//Dati generali
			conn = this.getConnection();
			String sql = "select NOME_IMMAGINE, PATH_IMMAGINE from mi_concessioni_immagine where pk_immagine = ? ";
			this.initialize();
			this.setString(1, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				
				val[0] = tornaValoreRS(rs,"PATH_IMMAGINE");
				val[1] = tornaValoreRS(rs,"NOME_IMMAGINE");
			}
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
		return val;
	}
	


}
