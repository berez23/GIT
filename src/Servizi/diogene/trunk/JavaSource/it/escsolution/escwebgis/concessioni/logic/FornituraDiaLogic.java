package it.escsolution.escwebgis.concessioni.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;


import org.apache.log4j.Logger;


import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.FornituraDia;
import it.escsolution.escwebgis.concessioni.bean.FornituraDiaFinder;
import it.webred.utils.StringUtils;



public class FornituraDiaLogic extends EscLogic
{
	private String appoggioDataSource;

	private static  String SQL_SELECT_LISTA = null;
	private static String SQL_SELECT_COUNT_LISTA = null;
	private static String SQL_ENTE = null;
	

	public FornituraDiaLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
		SQL_SELECT_LISTA = getProperty("sql.DIA");
		SQL_ENTE = getProperty("sql.ENTE");
		SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from ("+SQL_SELECT_LISTA ;
		
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_DIA = "LISTA_DIA@FornituraDiaLogic";
	public final static String FORNITURADIA = "DIA@FornituraDiaLogic";
	

	private FornituraDia getFornituraDia(java.sql.ResultSet rs) throws Exception {
		FornituraDia dia = new FornituraDia();
		dia.setIndirizzo(tornaValoreRS(rs,"INDIRIZZO"));
		dia.setIntervento(tornaValoreRS(rs,"INTERVENTO"));
		dia.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
		dia.setQualifica(tornaValoreRS(rs,"QUALIFICA"));
		dia.setRicCodiceFiscale(tornaValoreRS(rs,"RIC_CODICE_FISCALE"));
		dia.setRicCognome(tornaValoreRS(rs,"RIC_COGNOME"));
		dia.setRicNome(tornaValoreRS(rs,"RIC_NOME"));
		dia.setTipoUnita(tornaValoreRS(rs,"TIPO_UNITA"));
		dia.setFkFornitura(tornaValoreRS(rs,"FK_FORNITURA"));
		return dia;
	}
	public Hashtable mCaricareListaDia(Vector listaPar, FornituraDiaFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String orderByLista = " ";
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
						FornituraDia dia = getFornituraDia(rs);
						vct.add(dia);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_DIA, vct);
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

	public Hashtable mCaricareDettaglioDia(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		// faccio la connessione al db
		Connection conn = this.getConnection();
		
		Statement st = conn.createStatement();
		java.sql.ResultSet rsE = st.executeQuery(SQL_ENTE);
		String codEnte = null;
		if (rsE.next())
			codEnte = rsE.getString("CODENT");
		rsE.close();
		st.close();
		
		try
		{
				FornituraDia dia = null;
				String fornitura = chiave.split("[|]")[0];
				String protocollo = chiave.split("[|]")[1];

				try
				{
					this.initialize();
					// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
					String sql = getProperty("sql.DIA_DETAIL3");
		
					int indice = 1;
					this.setString(indice, protocollo);
					this.setString(indice+1, fornitura);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs.next())
					{
						dia = getFornituraDia(rs);
						dia.setCodEnte(codEnte);
					}
				}
				catch (Exception e)
				{
					log.error(e.getMessage(),e);
					throw e;
				}

		
				try
				{
					this.initialize();
					// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
					String sql = getProperty("sql.PROFESSIONISTI");
		
					int indice = 1;
					this.setString(indice, protocollo);
					this.setString(indice+1, fornitura);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs.next())
					{
						String[] pro = {
								tornaValoreRS(rs,"PRO_CODICE_FISCALE"),
								tornaValoreRS(rs,"PRO_ALBO"),
								tornaValoreRS(rs,"PRO_ALBO_PROV"),
								tornaValoreRS(rs,"PRO_ALBO_NUM"),
								tornaValoreRS(rs,"PRO_QUALIFICA")
								};
						dia.addProfessionista(pro);
					}
				}
				catch (Exception e)
				{
					log.error(e.getMessage(),e);
					throw e;
				}
				
				try
				{
					this.initialize();
					// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
					String sql = getProperty("sql.IMP");
		
					int indice = 1;
					this.setString(indice, protocollo);
					this.setString(indice+1, fornitura);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (rs.next())
					{
						String[] imp = {
								tornaValoreRS(rs,"IMP_PARTITA_IVA"),
								tornaValoreRS(rs,"IMP_DENOMINAZIONE"),
								tornaValoreRS(rs,"IMP_SEDE")
								};
						dia.addImp(imp);
					}
				}
				catch (Exception e)
				{
					log.error(e.getMessage(),e);
					throw e;
				}
				
				try
				{
					this.initialize();
					// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
					String sql = getProperty("sql.DIA_DETAIL");
		
					//query catasto 
					String sql1 = getProperty("sql.UIU");
					String sql2 = getProperty("sql.SITITRKC");

					
					
					int indice = 1;
					this.setString(indice, protocollo);
					this.setString(indice+1, fornitura);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs.next())
					{
						String presenteCatastoFabbricati = "N";
						String presenteCatastoTerreni = "N";
						String tipoUnita = tornaValoreRS(rs,"TIPO_UNITA");
						String dataValiditaCatasto = null;
							String[] uiu = {
								tornaValoreRS(rs,"RIC_CODICE_FISCALE"),
								tipoUnita,
								tornaValoreRS(rs,"SEZIONE"),
								tornaValoreRS(rs,"FOGLIO"),
								StringUtils.padding( tornaValoreRS(rs,"PARTICELLA"), 5,'0', true),
								tornaValoreRS(rs,"SUBALTERNO"),
								presenteCatastoFabbricati, // Y o N
								dataValiditaCatasto,
								presenteCatastoTerreni		// Y o N
								};
						
							PreparedStatement ps = null;
							ResultSet rsCat =null;
							if (tipoUnita.equals("FABBRICATI")) {
								try {
									boolean ok =false;
									try {
										Integer.parseInt(uiu[3]);
										Integer.parseInt(uiu[5]);
										ok= true;
									} catch (Exception e) {
										
									}
									
									if (ok) {
										ps = conn.prepareStatement(sql1);
										ps.setString(1, codEnte);
										ps.setInt(2, Integer.parseInt(uiu[3]));
										ps.setString(3, StringUtils.padding(uiu[4], 5,'0', true));
										ps.setInt(4, Integer.parseInt(uiu[5]));
										rsCat = ps.executeQuery();
										if (rsCat.next()) {
											uiu[6] = "Y";	
											uiu[7] = tornaValoreRS(rsCat,"DATA_FINE_VAL","YYYY-MM-DD").equals("-")?"31/12/9999":tornaValoreRS(rsCat,"DATA_FINE_VAL","YYYY-MM-DD");
											
										}
									}
									
								} finally {
									if (ps!=null)
										ps.close();
									if (rsCat!=null)
										rsCat.close();
								}
								
							} 
							
							// a terreni ci vado sempre
								try {
									boolean ok =false;
									try {
										// FOGLIO
										Integer.parseInt(uiu[3]);
										ok= true;
									} catch (Exception e) {
										
									}
									
									if (ok) {
										ps = conn.prepareStatement(sql2);
										ps.setString(1, codEnte);
										ps.setInt(2, Integer.parseInt(uiu[3]));
										ps.setString(3, StringUtils.padding(uiu[4], 5,'0', true));
										rsCat = ps.executeQuery();
										if (rsCat.next()) {
											uiu[8] = "Y";	
										}
									}
									
								} finally {
									if (ps!=null)
										ps.close();
									if (rsCat!=null)
										rsCat.close();
								}
								
								
							
							
						
						dia.addUiu(uiu);
						
						
						
					}
				}
				catch (Exception e)
				{
					log.error(e.getMessage(),e);
					throw e;
				}
				try
				{
					this.initialize();
					// PRIMA LEGGO I DATI RELATIVI ALLA PERSONA
					String sql = getProperty("sql.DIA_DETAIL2");
		
					int indice = 1;
					this.setString(indice, protocollo);
					this.setString(indice+1, fornitura);
					prepareStatement(sql);
					java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					while (rs.next())
					{
						String[] beneficiario = {
								tornaValoreRS(rs,"BEN_COGNOME"),
								tornaValoreRS(rs,"BEN_NOME"),
								tornaValoreRS(rs,"BEN_SESSO"),
								tornaValoreRS(rs,"BEN_NA_DATA"),
								tornaValoreRS(rs,"BEN_NA_LUOGO"),
								tornaValoreRS(rs,"BEN_DENOMINAZIONE"),
								tornaValoreRS(rs,"BEN_SEDE"),
								};
						dia.addBeneficiari(beneficiario);
						
					}
				}
				catch (Exception e)
				{
					log.error(e.getMessage(),e);
					throw e;
				}
				ht.put(FORNITURADIA, dia);
				
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
