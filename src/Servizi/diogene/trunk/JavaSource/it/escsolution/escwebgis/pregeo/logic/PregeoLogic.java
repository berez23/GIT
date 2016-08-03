/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.pregeo.logic;

import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.pregeo.bean.ElementoListaFinder;
import it.escsolution.escwebgis.pregeo.bean.PregeoFornitura;
import it.escsolution.escwebgis.pregeo.bean.PregeoInfo;
import it.escsolution.escwebgis.pregeo.servlet.PregeoServlet;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.utils.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class PregeoLogic extends EscLogic
{
	private String appoggioDataSource;

	public PregeoLogic(EnvUtente eu){
		super(eu);
		appoggioDataSource = eu.getDataSource();
		SQL_SELECT_LISTA = getProperty("sql.SELECT_LISTA");
		SQL_SELECT_COUNT_LISTA = getProperty("sql.SELECT_COUNT_LISTA");

		SQL_SELECT_DETTAGLIO = getProperty("sql.SELECT_DETTAGLIO");
		SQL_SELECT_DETTAGLIO_FORNITURA = getProperty("sql.SELECT_DETTAGLIO_FORNITURA");
		SQL_SELECT_DETTAGLIO_ALTRO = getProperty("sql.SELECT_DETTAGLIO_ALTRO");

	}
	
	public  static String LISTA_DATI_PREGEO = PregeoLogic.class.getName() + "@LISTA_DATI_PREGEO";
	public  static String PREGEO_DETT = PregeoLogic.class.getName() + "@DETT_PREGEO";
	public  static String PREGEO_DETT_FOR = PregeoLogic.class.getName() + "@DETT_PREGEO_FOR";
	public  static String PREGEO_DETT_ALTRO = PregeoLogic.class.getName() + "@DETT_PREGEO_ALTRO";
	public static final String FINDER = "FINDER114";

	private  static String SQL_SELECT_LISTA = null;
	private  static String SQL_SELECT_COUNT_LISTA = null;
	private  static String SQL_SELECT_DETTAGLIO  = null;
	private  static String SQL_SELECT_DETTAGLIO_FORNITURA  = null;
	private  static String SQL_SELECT_DETTAGLIO_ALTRO  = null;

	public final String AUTORIZZAZIONI = "AUTORIZZAZIONI";
	public final String DIFFIDE = "DIFFIDE";

	public Hashtable mCaricareLista(Vector listaPar, ElementoListaFinder finder) throws Exception{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try{
			conn = this.getConnectionDiogene();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i = 0; i <= 1; i++){
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
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1)
				{
					sql = sql + "order by data_pregeo,codice_pregeo,nome_file_pdf)sel) where N > " + limInf + " and N <=" + limSup;
				}
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				log.debug("LISTA PREGEO SQL["+sql+"]");
				
				if (i == 1)
				{
					HashMap<String, PregeoInfo> mappa = new HashMap<String,PregeoInfo>();
					while (rs.next())
					{
						PregeoInfo bInfo = new PregeoInfo();
						//String idInfo = rs.getString("ID_INFO");
						String nomeFilePdf = rs.getString("NOME_FILE_PDF");
						java.util.Date dataPregeo = rs.getDate("DATA_PREGEO");
						String codicePregeo = rs.getString("CODICE_PREGEO");
						String nominativo = rs.getString("DENOMINAZIONE");
						String denominazione = nominativo.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
						String tecnico = rs.getString("TECNICO");
						String tipoTecnico = rs.getString("TIPO_TECNICO");
						String foglio = rs.getString("FOGLIO");
						//String particella = rs.getString("PARTICELLA");
						java.util.Date dataInserimento = rs.getDate("DATA_INSERIMENTO");
						String relazioneTecnica = rs.getString("RELAZIONE_TECNICA");
						String nota = rs.getString("NOTA");
						
						//Recuper la lista di idInfo
						List<String> lstId = this.getListaIdInfo(conn, nomeFilePdf);
						String idInfo = lstId.get(0);
						
						//Recupero la lista di particelle
						String particella = "";
						List<String> part = this.getListaParticellePdf(nomeFilePdf);
						for(String p : part)
							particella += p+"<br>";
						
						particella = particella.substring(0, particella.lastIndexOf("<br>"));
					
						bInfo.setIdInfo( !StringUtils.isEmpty(idInfo)?Long.parseLong(idInfo):0 );
						bInfo.setNomeFilePdf( Utils.checkNullString(nomeFilePdf) );
						bInfo.setDataPregeo(dataPregeo);
						bInfo.setCodicePregeo( Utils.checkNullString(codicePregeo) );
						bInfo.setDenominazione(Utils.checkNullString(denominazione) );
						bInfo.setTecnico( Utils.checkNullString(tecnico) );
						bInfo.setTipoTecnico( Utils.checkNullString(tipoTecnico) );
						bInfo.setFoglio( Utils.checkNullString(foglio) );
						bInfo.setParticella( Utils.checkNullString( particella) );
						bInfo.setDataInserimento(dataInserimento);
						bInfo.setRelazioneTecnica(Utils.checkNullString( relazioneTecnica ) );
						bInfo.setNota(Utils.checkNullString( nota ) );
						
						vct.add(bInfo);
					}
					
					java.util.Iterator<Entry<String, PregeoInfo>> iter = mappa.entrySet().iterator();
					while(iter.hasNext())
						vct.add(iter.next().getValue());
					
				}
				else
				{
					if (rs.next())
					{
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(LISTA_DATI_PREGEO, vct);
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
	
	private List<String> getListaIdInfo(Connection conn, String nomeFilePdf) throws SQLException, Exception {
		PreparedStatement pst=null ;
		ResultSet rs=null;
		
		List<String> lstId = new ArrayList<String>();
		try{
		
		
		String sql = "select distinct id_info from pregeo_info where nome_file_pdf=? ";
		
		pst = conn.prepareStatement(sql);
		pst.setString(1,nomeFilePdf);
		rs = pst.executeQuery();
		
		while(rs.next())
			lstId.add(rs.getString("id_info"));
		
		
		
		}catch(Exception e){
			log.error("Recupero idInfo Pregeo:"+e.getMessage(),e);
			throw e;
		}finally{
			DbUtils.close(rs);
			DbUtils.close(pst);
		}
		
		return lstId;
	}
	
	private List<String> getListaParticellePdf(String nomeFilePdf) throws NamingException{
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		PregeoService pregeoService = (PregeoService)getEjb("CT_Service", "CT_Service_Data_Access", "PregeoServiceBean");
		
		RicercaPregeoDTO rp = new RicercaPregeoDTO();
		rp.setEnteId(enteId);
		rp.setUserId(userId);
		rp.setSessionId(sessionId);
		rp.setNomeFilePdf(nomeFilePdf);
		
		return pregeoService.getListaParticelleByNomeFilePdf(rp);
		
	}

	public Hashtable mCaricareDettaglio(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		ArrayList<PregeoInfo> alInfo = new ArrayList<PregeoInfo>();
		ArrayList<PregeoFornitura> alForniture = new ArrayList<PregeoFornitura>();
		ArrayList<String> alAltro = new ArrayList<String>();

		// faccio la connessione al db
		Connection conn = null;

		java.sql.ResultSet rs = null;
		java.sql.ResultSet rs2 = null;
		java.sql.ResultSet rs3 = null;
		
		try
		{
			conn = this.getConnectionDiogene();
			this.initialize();
		       
			String sql = SQL_SELECT_DETTAGLIO;

			// recupero le chiavi di ricerca

			this.setString(1, chiave);

			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			String nomeFilePdf = "";
			String codicePregeo = "";
			String particella = "";
			while (rs.next())
			{
				PregeoInfo bInfo = new PregeoInfo();
				String idInfo = rs.getString("ID_INFO");
				nomeFilePdf = rs.getString("NOME_FILE_PDF");
				java.util.Date dataPregeo = rs.getDate("DATA_PREGEO");
				codicePregeo = rs.getString("CODICE_PREGEO");
				String nominativo = rs.getString("DENOMINAZIONE");
				String denominazione = nominativo.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
				String tecnico = rs.getString("TECNICO");
				String tipoTecnico = rs.getString("TIPO_TECNICO");
				String foglio = rs.getString("FOGLIO");
				particella = rs.getString("PARTICELLA");
				java.util.Date dataInserimento = rs.getDate("DATA_INSERIMENTO");
				String relazioneTecnica = rs.getString("RELAZIONE_TECNICA");
				String nota = rs.getString("NOTA");
				
				bInfo.setIdInfo( !StringUtils.isEmpty(idInfo)?Long.parseLong(idInfo):0 );
				bInfo.setNomeFilePdf( Utils.checkNullString(nomeFilePdf) );
				bInfo.setDataPregeo(dataPregeo);
				bInfo.setCodicePregeo( Utils.checkNullString(codicePregeo) );
				bInfo.setDenominazione(Utils.checkNullString(denominazione) );
				bInfo.setTecnico( Utils.checkNullString(tecnico) );
				bInfo.setTipoTecnico( Utils.checkNullString(tipoTecnico) );
				bInfo.setFoglio( Utils.checkNullString(foglio) );
				bInfo.setParticella( Utils.checkNullString( particella) );
				bInfo.setDataInserimento(dataInserimento);
				bInfo.setRelazioneTecnica(Utils.checkNullString( relazioneTecnica ) );
				bInfo.setNota(Utils.checkNullString( nota ) );
				
				alInfo.add(bInfo);
			}
			
			if ( !StringUtils.isEmpty(nomeFilePdf) ){
				this.initialize();
				sql = SQL_SELECT_DETTAGLIO_FORNITURA;

				// recupero le chiavi di ricerca

				this.setString(1, "%" + nomeFilePdf + "%");

				prepareStatement(sql);
				rs2 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				while (rs2.next())
				{
					PregeoFornitura bFornitura = new PregeoFornitura();
					String idFor = rs2.getString("ID_FOR");
					Date dataFornitura = rs2.getDate("DATA_FORNITURA");
					String nomeFileZip = rs2.getString("NOME_FILE_ZIP");
					String contenutoZip = rs2.getString("CONTENUTO_ZIP");
					Date dataInserimento = rs2.getDate("DATA_INSERIMENTO");

					bFornitura.setIdFor( !StringUtils.isEmpty(idFor)?Long.parseLong(idFor):0 );
					bFornitura.setDataFornitura( dataFornitura );
					bFornitura.setNomeFileZip( Utils.checkNullString(nomeFileZip) );
					bFornitura.setContenutoZip( Utils.checkNullString(contenutoZip) );
					bFornitura.setDataInserimento(dataInserimento);
					
					alForniture.add(bFornitura);
				}				
			}
			if ( !StringUtils.isEmpty(codicePregeo) && !StringUtils.isEmpty(particella) ){
				this.initialize();
				sql = SQL_SELECT_DETTAGLIO_ALTRO;

				// recupero le chiavi di ricerca

				this.setString(1, nomeFilePdf);
				this.setString(2, particella);

				prepareStatement(sql);
				rs3 = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
				while (rs3.next())
				{
					String part = rs3.getString("PARTICELLA");
					if(!alAltro.contains(part))
						alAltro.add(part);
				}				
			}
			
			ht.put(PregeoLogic.PREGEO_DETT, alInfo);
			ht.put(PregeoLogic.PREGEO_DETT_FOR, alForniture);
			ht.put(PregeoLogic.PREGEO_DETT_ALTRO, alAltro);
			
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
			
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}
	
	public Vector<PregeoInfo> caricaListaPregeoSuFoglio(String idCatalogo, HttpServletRequest request) throws Exception {
		
		Vector<PregeoInfo> vct = new Vector<PregeoInfo>();
		
		
		String sql = "select r.foglio from cat_pregeo_foglio r  where r.se_row_id = ? ";
		
		Connection conn = null;

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			conn =  this.getConnectionDiogene();

			log.info("caricaListaPregeoSuFoglio - SQL[" + sql + "]");
			log.info("caricaListaPregeoSuFoglio - IdCatalogo[" + idCatalogo+"]");
			pst = conn.prepareStatement(sql);
			pst.setString(1, idCatalogo);
			rs = pst.executeQuery();
			
			String foglio = "";
			if(rs.next()){
				foglio = rs.getString("foglio");
				
				log.info("Foglio: "+foglio);
			
				PregeoServlet serv = new PregeoServlet();
				String path = serv.getPathDatiDiogene() +  this.getDirPregeoEnte();
				
				sql = SQL_SELECT_LISTA + " AND ltrim(substr(foglio,0,3),'0')= ?  order by data_pregeo,codice_pregeo,nome_file_pdf)sel) ";
				
				log.info("SQL["+sql+"]");
				
				pst = conn.prepareStatement(sql);
				pst.setString(1,"1");
				pst.setString(2,foglio);
				
				rs = pst.executeQuery();

				while (rs.next())
				{
					PregeoInfo bInfo = new PregeoInfo();
					//String idInfo = rs.getString("ID_INFO");
					String nomeFilePdf = rs.getString("NOME_FILE_PDF");
					java.util.Date dataPregeo = rs.getDate("DATA_PREGEO");
					String codicePregeo = rs.getString("CODICE_PREGEO");
					String nominativo = rs.getString("DENOMINAZIONE");
					String denominazione = nominativo.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
					String tecnico = rs.getString("TECNICO");
					String tipoTecnico = rs.getString("TIPO_TECNICO");
					String fglc = rs.getString("FOGLIO");
					//String particella = rs.getString("PARTICELLA");
					java.util.Date dataInserimento = rs.getDate("DATA_INSERIMENTO");	
					String relazioneTecnica = rs.getString("RELAZIONE_TECNICA");
					String nota = rs.getString("NOTA");
					
					String nomeFile = Utils.checkNullString(nomeFilePdf).length()>0 ? path +  File.separatorChar + Utils.checkNullString(nomeFilePdf) : "";
					
					//Recuper la lista di idInfo
					List<String> lstId = this.getListaIdInfo(conn, nomeFilePdf);
					String idInfo = lstId.get(0);
					
					//Recupero la lista di particelle
					String particella = "";
					List<String> part = this.getListaParticellePdf(nomeFilePdf);
					for(String p : part)
						particella += p+"<br>";
					
					particella = particella.substring(0, particella.lastIndexOf("<br>"));
					
					bInfo.setIdInfo( !StringUtils.isEmpty(idInfo)?Long.parseLong(idInfo):0 );
					bInfo.setNomeFilePdf(Utils.checkNullString(nomeFilePdf));
					bInfo.setPathFilePdf(nomeFile);
					bInfo.setDataPregeo(dataPregeo);
					bInfo.setCodicePregeo( Utils.checkNullString(codicePregeo) );
					bInfo.setDenominazione(Utils.checkNullString(denominazione) );
					bInfo.setTecnico( Utils.checkNullString(tecnico) );
					bInfo.setTipoTecnico( Utils.checkNullString(tipoTecnico) );
					bInfo.setFoglio( Utils.checkNullString(fglc) );
					bInfo.setParticella( Utils.checkNullString( particella) );
					bInfo.setDataInserimento(dataInserimento);
					bInfo.setRelazioneTecnica(Utils.checkNullString( relazioneTecnica ) );
					bInfo.setNota(Utils.checkNullString( nota ) );
					
					vct.add(bInfo);
				}
					
				
			}
		}catch (Exception e)
			{
				log.error(e.getMessage(),e);
				throw e;
			}
			finally
			{
				if (rs!=null)
					rs.close();
				
				if (conn != null && !conn.isClosed())
					conn.close();
			}
		
		return vct;
	}

	
	public List<PregeoInfo> getListaPregeoFabbricato(String foglio, String numero, String pathPregeo, boolean isFoglioDaPregeo) throws Exception{
		
		List<PregeoInfo> alPregeoInfo = new ArrayList<PregeoInfo>();
		/*
		 * Recupero i dati di pregeo se il foglio e la particella sono
		 * valorizzati
		 */
		log.debug("getListaPregeoInfo-Par.1 foglio = " + foglio);
		log.debug("getListaPregeoInfo-Par.2 numero = " + numero);
		if (foglio != null && numero != null){
	
			EnvUtente eu = this.getEnvUtente();
			String enteId = eu.getEnte();
			String userId = eu.getUtente().getUsername();
			String sessionId = eu.getUtente().getSessionId();
			
			PregeoService pregeoService = (PregeoService)getEjb("CT_Service", "CT_Service_Data_Access", "PregeoServiceBean");
			
			RicercaPregeoDTO rp = new RicercaPregeoDTO();
			rp.setEnteId(enteId);
			rp.setUserId(userId);
			rp.setSessionId(sessionId);
			rp.setFoglio(StringUtils.padding(foglio, 4, '0', true));
			rp.setParticella(StringUtils.padding(numero, 5, '0', true));
			rp.setFoglioPregeo(isFoglioDaPregeo);
			
			List<it.webred.ct.data.model.pregeo.PregeoInfo> lsttmp = pregeoService.getDatiPregeo(rp);
			for(it.webred.ct.data.model.pregeo.PregeoInfo d : lsttmp){
				PregeoInfo bPregeoInfo = new PregeoInfo();
				
				String nominativo = d.getDenominazione();
				String denominazione = nominativo.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");

				bPregeoInfo.setCodicePregeo( Utils.checkNullString(d.getCodicePregeo()) );
				bPregeoInfo.setDataPregeo( d.getDataPregeo() );
				bPregeoInfo.setDenominazione(denominazione);
				bPregeoInfo.setFoglio(d.getFoglio()!=null ? d.getFoglio() : null);
				bPregeoInfo.setIdInfo(d.getIdInfo());
				bPregeoInfo.setNomeFilePdf(!StringUtils.isEmpty(d.getNomeFilePdf())?pathPregeo + "/" + d.getNomeFilePdf():"" );
				bPregeoInfo.setParticella(d.getParticella());
				bPregeoInfo.setTecnico(d.getTecnico());
				bPregeoInfo.setTipoTecnico(d.getTipoTecnico());
				bPregeoInfo.setRelazioneTecnica(d.getRelazioneTecnica());
				bPregeoInfo.setNota(d.getNota());
				
				alPregeoInfo.add(bPregeoInfo);
			}
			
		}
		
		return alPregeoInfo;
	}
		


}
