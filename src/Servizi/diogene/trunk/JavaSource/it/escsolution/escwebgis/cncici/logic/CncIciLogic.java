
package it.escsolution.escwebgis.cncici.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.cncici.bean.CncIci;
import it.escsolution.escwebgis.cncici.bean.CncIciFinder;
import it.escsolution.escwebgis.cncici.bean.CncIci.CncIciContitolari;
import it.escsolution.escwebgis.cncici.bean.CncIci.CncIciImmobili;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.docfa.bean.DocfaFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


public class CncIciLogic extends EscLogic
{
	private String appoggioDataSource;

	public CncIciLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_CNCICI = "LISTA_CNCICI@CncIciLogic";
	public final static String LISTA_DETTAGLIO_CNCICI = "LISTA_DETTAGLIO_CNCICI@CncIciLogic";
	public final static String CNCICI = "CNCICI@CncIciLogic";
	private final static String SQL_SELECT_LISTA = 
		" select * from ( SELECT fk_fornitura,protocollo,nr_pacco,progressivo,codice_fiscale,cognome_contribuente, nome, rownum as n  from (" +
		" SELECT DISTINCT cont.fk_fornitura, cont.protocollo, cont.nr_pacco, " +
		"                cont.progressivo, cont.codice_fiscale, " +
		"                cont.cognome_denom cognome_contribuente, cont.nome " +		
		"           FROM cnc_dichia_contribuenti cont, " +
		"                cnc_dichia_denuncianti den, " +
		"                cnc_dichia_contitolari con, " +
		"                cnc_dichia_immobili imm " +
		"          WHERE cont.fk_fornitura = den.fk_fornitura(+) " +
		"            AND cont.protocollo = den.protocollo(+) " +
		"            AND cont.nr_pacco = den.nr_pacco(+) " +
		"            AND cont.progressivo = den.progressivo(+) " +
		"            AND cont.fk_fornitura = con.fk_fornitura(+) " +
		"            AND cont.protocollo = con.protocollo(+) " +
		"            AND cont.nr_pacco = con.nr_pacco(+) " +
		"            AND cont.progressivo = con.progressivo(+) " +
		"            AND cont.fk_fornitura = imm.fk_fornitura(+) " +
		"            AND cont.protocollo = imm.protocollo(+) " +
		"            AND cont.nr_pacco = imm.nr_pacco(+) " +
		"            AND cont.progressivo = imm.progressivo(+) " +
		"            and 1 = ? "
		;

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from ("+SQL_SELECT_LISTA ;

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	public Hashtable mCaricareListaCncIci(Vector listaPar, CncIciFinder finder) throws Exception
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
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}
				if(i == 0 && finder.getKeyStr().equals(""))
					sql = sql + ")))";


				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						CncIci cncici = new CncIci();

						cncici.setFkFornitura(tornaValoreRS(rs,"Fk_Fornitura")); 
						cncici.setProtocollo(tornaValoreRS(rs,"Protocollo"));
						cncici.setNrPacco(tornaValoreRS(rs,"Nr_Pacco"));   
						cncici.setProgressivo(tornaValoreRS(rs,"Progressivo"));
						cncici.setContribuenteCodiceFiscale(tornaValoreRS(rs,"Codice_Fiscale"));
						cncici.setContribuenteCognome(tornaValoreRS(rs,"Cognome_Contribuente"));
						cncici.setContribuenteNome(tornaValoreRS(rs,"Nome"));						
						vct.add(cncici);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_CNCICI, vct);
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
	
	public Hashtable mCaricareDettaglioCncIci(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();

		Connection conn = null;
		try
		{			
			StringTokenizer t = new StringTokenizer(chiave,"|");
			String fkFornitura = t.nextToken();
			String protocollo = t.nextToken();
			String nrPacco = t.nextToken();
			String progressivo = t.nextToken();
			conn = this.getConnection();
			
			
			//Contribuente e Dichiarante
			String sql = 		" Select          " +
								"       CONT.COGNOME_DENOM contribuenteCognome, " +
								"       CONT.NOME contribuenteNome, " +
								"       CONT.SESSO contribuenteSesso, " +
								"       CONT.CODICE_FISCALE contribuenteCodiceFiscale, " +
								"       CONT.NAS_DATA contribuenteNasData, " +
								"       CONT.NAS_COMUNE contribuenteNasComune, " +
								"       CONT.NAS_PROVINCIA contribuenteNasProvincia, " +
								"       CONT.DOMICILIO_FISC_INDIRIZZO contribuenteDFIndirizzo, " +
								"       CONT.DOMICILIO_FISC_CAP contribuenteDFCap, " +
								"       CONT.DOMICILIO_FISC_COMUNE contribuenteDFComune, " +
								"       CONT.DOMICILIO_FISC_PROVINCIA contribuenteDFProvincia,         " +
								"       den.COGNOME_DENOM denuncianteCognome,        " +
								"       den.CODICE_FISCALE denuncianteCodiceFiscale, " +
								"       den.DOMICILIO_FISC_INDIRIZZO denuncianteDFIndirizzo, " +
								"       den.DOMICILIO_FISC_CAP denuncianteDFCap, " +
								"       den.DOMICILIO_FISC_COMUNE denuncianteDFComune, " +
								"       den.DOMICILIO_FISC_PROVINCIA denuncianteDFProvincia           " +
								" FROM cnc_dichia_contribuenti cont,  " +
								"                cnc_dichia_denuncianti den  " +
								"          WHERE cont.fk_fornitura = den.fk_fornitura(+)  " +
								"            AND cont.protocollo = den.protocollo(+)  " +
								"            AND cont.nr_pacco = den.nr_pacco(+)  " +
								"            AND cont.progressivo = den.progressivo(+)  " +
								"            AND cont.fk_fornitura = ? " +
								"            AND cont.protocollo = ? " +
								"            AND cont.nr_pacco = ? " +
								"            AND cont.progressivo = ? " ;			
			this.initialize();
			this.setString(1, fkFornitura);
			this.setString(2, protocollo);
			this.setString(3, nrPacco);
			this.setString(4, progressivo);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			CncIci cncIci = new CncIci();

			if (rs.next())
			{ 
				cncIci.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
			    

				cncIci.setContribuenteCognome(tornaValoreRS(rs,"ContribuenteCognome"));
				cncIci.setContribuenteNome(tornaValoreRS(rs,"ContribuenteNome"));
				cncIci.setContribuenteSesso(tornaValoreRS(rs,"ContribuenteSesso"));
				cncIci.setContribuenteCodiceFiscale(tornaValoreRS(rs,"ContribuenteCodiceFiscale"));
				cncIci.setContribuenteNasData(tornaValoreRS(rs,"ContribuenteNasData"));
				cncIci.setContribuenteNasComune(tornaValoreRS(rs,"ContribuenteNasComune"));
				cncIci.setContribuenteNasProvincia(tornaValoreRS(rs,"ContribuenteNasProvincia"));
				cncIci.setContribuenteDFIndirizzo(tornaValoreRS(rs,"ContribuenteDFIndirizzo"));
				cncIci.setContribuenteDFCap(tornaValoreRS(rs,"ContribuenteDFCap"));
				cncIci.setContribuenteDFComune(tornaValoreRS(rs,"ContribuenteDFComune"));
				cncIci.setContribuenteDFProvincia(tornaValoreRS(rs,"ContribuenteDFProvincia"));        
				cncIci.setDenuncianteCognome(tornaValoreRS(rs,"DenuncianteCognome"));       
				cncIci.setDenuncianteCodiceFiscale(tornaValoreRS(rs,"DenuncianteCodiceFiscale"));
				cncIci.setDenuncianteDFIndirizzo(tornaValoreRS(rs,"DenuncianteDFIndirizzo"));
				cncIci.setDenuncianteDFCap(tornaValoreRS(rs,"DenuncianteDFCap"));
				cncIci.setDenuncianteDFComune(tornaValoreRS(rs,"DenuncianteDFComune"));
				cncIci.setDenuncianteDFProvincia(tornaValoreRS(rs,"DenuncianteDFProvincia")); 								
			}

			
			
			// CONTITOLARI
			sql =" Select " +
			"       CON.CODICE_FISCALE conCodiceFiscale,          " +
			"       CON.DOMICILIO_FISC_INDIRIZZO conDFIndirizzo,        " +
			"       CON.DOMICILIO_FISC_COMUNE conDFComune, " +
			"       CON.DOMICILIO_FISC_PROVINCIA conDFProvincia, " +
			"       CON.POSSESSO_MESI poss_mesi, " +
			"       CON.POSSESSO_PERC  poss_perc         " +
			" FROM CNC_DICHIA_CONTITOLARI CON  " +
			"            WHERE CON.fk_fornitura = ? " +
			"            AND CON.protocollo = ? " +
			"            AND CON.nr_pacco = ? " +
			"            AND CON.progressivo = ? " ;			
			this.initialize();
			this.setString(1, fkFornitura);
			this.setString(2, protocollo);
			this.setString(3, nrPacco);
			this.setString(4, progressivo);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList listaCon = new ArrayList();
			while (rs.next())
			{
				CncIciContitolari cncIciContitolari = cncIci.new CncIciContitolari();
				cncIciContitolari.setConCodiceFiscale(tornaValoreRS(rs,"ConCodiceFiscale"));         
				cncIciContitolari.setConDFIndirizzo(tornaValoreRS(rs,"ConDFIndirizzo"));        
				cncIciContitolari.setConDFComune(tornaValoreRS(rs,"ConDFComune")); 
				cncIciContitolari.setConDFProvincia(tornaValoreRS(rs,"ConDFProvincia")); 
				cncIciContitolari.setPossMesi(tornaValoreRS(rs,"Poss_mesi")); 
				cncIciContitolari.setPossPerc(tornaValoreRS(rs,"Poss_perc"));   				
				listaCon.add(cncIciContitolari);
			}
			cncIci.setContitolari(listaCon);
			
			
			
			
			// IMMOBILI
			sql =" Select  " +
			"       IMM.NR_ORDINE nrOrdine, " +
			"       IMM.SEZIONE sezione, " +
			"       IMM.FOGLIO foglio, " +
			"       IMM.NUMERO particella, " +
			"       IMM.SUBALTERNO subalterno, " +
			"       IMM.PROTOCOLLO_CAT protocolloCat, " +
			"       IMM.CATEGORIA categoria, " +
			"       IMM.CLASSE classe, " +
			"       IMM.INDIRIZZO indirizzo, " +
			"       IMM.VALORE valoreRendita, " +
			"       imm.POSSESSO_MESI possessoPerc, " +
			"       imm.POSSESSO_PERC possessoMesi        " +
			" FROM CNC_DICHIA_IMMOBILI IMM  "+
			"            WHERE  imm.fk_fornitura = ? " +
			"            AND imm.protocollo = ? " +
			"            AND imm.nr_pacco = ? " +
			"            AND imm.progressivo = ? " ;			
			this.initialize();
			this.setString(1, fkFornitura);
			this.setString(2, protocollo);
			this.setString(3, nrPacco);
			this.setString(4, progressivo);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList listaImm = new ArrayList();
			while (rs.next())
			{
				CncIciImmobili cncIciImmobili = cncIci.new CncIciImmobili();
			
				cncIciImmobili.setNrOrdine(tornaValoreRS(rs,"nrOrdine"));  
				cncIciImmobili.setSezione(tornaValoreRS(rs,"sezione"));  
				cncIciImmobili.setFoglio(tornaValoreRS(rs,"foglio"));  
				cncIciImmobili.setParticella(tornaValoreRS(rs,"particella"));   
				cncIciImmobili.setSubalterno(tornaValoreRS(rs,"subalterno"));  
				cncIciImmobili.setProtocolloCat(tornaValoreRS(rs,"protocolloCat"));  
				cncIciImmobili.setCategoria(tornaValoreRS(rs,"categoria"));  
				cncIciImmobili.setClasse(tornaValoreRS(rs,"classe"));  
				cncIciImmobili.setIndirizzo(tornaValoreRS(rs,"indirizzo"));  
				cncIciImmobili.setValoreRendita(tornaValoreRS(rs,"valoreRendita"));  
				cncIciImmobili.setPossessoPerc(tornaValoreRS(rs,"possessoPerc"));  
				cncIciImmobili.setPossessoMesi(tornaValoreRS(rs,"possessoMesi"));  		
				
				listaImm.add(cncIciImmobili);
			}
			cncIci.setImmobili(listaImm);
			
			
			ht.put(CNCICI, cncIci);
			
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

}
