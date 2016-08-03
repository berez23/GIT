package it.escsolution.escwebgis.diagnostiche.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.diagnostiche.bean.Diagnostiche;
import it.webred.utils.DateFormat;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DiagnosticheLogic extends EscLogic
{
	private String	appoggioDataSource;

	public DiagnosticheLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String	FINDER					= "FINDER";
	private final static int	MAX_RIGHE_VIS			= 1000;
	//public final static String	LISTA_DIAGNOSTICHE		= "LISTA_DIAGNOSTICHE@DiagnosticheLogic";
	//public final static String	LISTA_STORICO_DIAGNOSTICHE		= "LISTA_STORICO_DIAGNOSTICHE@DiagnosticheLogic";
	//il carattere @ sembra dare errore - verificare (Filippo Mazzini 08.05.07)
	public final static String	LISTA_DIAGNOSTICHE		= "LISTA_DIAGNOSTICHE";
	public final static String	LISTA_STORICO_DIAGNOSTICHE		= "LISTA_STORICO_DIAGNOSTICHE";
	public final static String	LISTA_STORICO_DIAGNOSTICHE_PAG	= "LISTA_STORICO_DIAGNOSTICHE_PAG";
	public final static String	RICERCA_DIAGNOSTICHE	= "RICERCA_DIAGNOSTICHE";
	public final static String	DIAGNOSTICHE			= "DIAGNOSTICHE@DiagnosticheLogic";
	public final static String	COD_COMMAND_SCELTO		= "COD_COMMAND_SCELTO";
	public final static String	DESC_COMMAND_SCELTO		= "DESC_COMMAND_SCELTO";
	public final static String	ID_COMMAND_LAUNCH       = "ID_COMMAND_LAUNCH";
	public final static String	HEADER_EXPORT  			= "text/csv";	
	public final static String	ESTENSIONE_FILE_EXPORT  = ".csv";
	public final static String	MSG_RIGHE_VIS		    = "MSG_RIGHE_VIS";
	public final static String	TOT_PAGINE		    	= "TOT_PAGINE";


	public LinkedHashMap<String, String> caricaListaDiagnostiche(HttpServletRequest request)
		throws Exception
	{
		LinkedHashMap<String, String> ht = new LinkedHashMap<String, String>();
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			String sqlStorico = "select ID, TO_CHAR(DATE_START,'dd/mm/yyyy') as STR_DATE_START from r_command_launch where fk_command = ? order by DATE_START";
			java.sql.ResultSet rs;
			this.initialize();
			prepareStatement(sqlStorico);
			this.setInt(1, Integer.parseInt(request.getParameter("cmd")));
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()) {
				ht.put(tornaValoreRS(rs, "ID"), tornaValoreRS(rs, "STR_DATE_START"));				
			}
			rs.close();
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
		return ht;
	}

	

	public Diagnostiche caricaDatiRicerca(HttpServletRequest request)
		throws Exception
	{
		sql = "select L.ID, L.FK_COMMAND , L.PROCESSID, C.COD_COMMAND, " +
				"D.FK_TIPO_DIA, C.LONG_DESCR DESC_COMMAND, c.ID id_command,  "+
				" d.FK_TEMPLATE_CFG, D.FK_ID_DIA_FONTE, L.ID ID_COMMAND_LAUNCH  "+
			" from R_COMMAND  C ,R_COMMAND_LAUNCH  L , SIT_F_DIA_TEMPLATE D where "+
			" L.DATE_END is not null "+
			" and C.ID = L.FK_COMMAND "+
			" and D.FK_COD_COMMAND = C.COD_COMMAND "+
			" and D.FK_ID_DIA_FONTE = ?";
		Diagnostiche diagnostiche = new Diagnostiche();
		LinkedHashMap<String, String> dateLancioController = new LinkedHashMap<String, String>();
		TreeMap<String, Diagnostiche> listaCommandDiagnostiche = new TreeMap<String, Diagnostiche>();
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			java.sql.ResultSet rs;
			
			if(request.getSession().getAttribute(RICERCA_DIAGNOSTICHE) != null && ((Diagnostiche)request.getSession().getAttribute(RICERCA_DIAGNOSTICHE)).getIdFonte().equals(request.getParameter("COD_FONTE")))
			{
				Diagnostiche dsession = (Diagnostiche)request.getSession().getAttribute(RICERCA_DIAGNOSTICHE);
				diagnostiche.setDateLancioController(dsession.getDateLancioController());
				diagnostiche.setListaCommandDiagnostiche(dsession.getListaCommandDiagnostiche());
				diagnostiche.setDescrizioneFonte(dsession.getDescrizioneFonte());
				diagnostiche.setIdFonte(dsession.getIdFonte());
			}
			else
			{
				this.initialize();
				prepareStatement(sql+"  ORDER BY L.DATE_START DESC ");
				this.setInt(1, Integer.parseInt(request.getParameter("COD_FONTE")));
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next())
				{
					String processIdsplited[] = rs.getString("PROCESSID").split("@");
					// lanciati da controller 
					if(processIdsplited.length == 4 && processIdsplited[0] != null && !processIdsplited[0].trim().equals(""))
					{
						dateLancioController.put(new Long(processIdsplited[1]).toString(), DateFormat.dateToString(new Date(new Long(processIdsplited[1]).longValue()), "dd/MM/yyyy HH:mm"));
					}
	
					//tutte le dia
					Diagnostiche dia = new Diagnostiche();
					dia.setCodCommand(tornaValoreRS(rs, "COD_COMMAND"));
					dia.setDescCommand(tornaValoreRS(rs, "DESC_COMMAND"));
					dia.setIdCommand(tornaValoreRS(rs, "ID_COMMAND"));
					dia.setIdCfgTemplate(tornaValoreRS(rs, "FK_TEMPLATE_CFG"));					
					dia.setTipo(tornaValoreRS(rs,"FK_TIPO_DIA").equals("1")?"Integrità":"Controllo");					
					dia.setIdFonte(tornaValoreRS(rs, "FK_ID_DIA_FONTE"));
					dia.setIdCommanLaunch(rs.getString("ID_COMMAND_LAUNCH"));
					
					if(!listaCommandDiagnostiche.containsKey(dia.getCodCommand()) || new Long(dia.getIdCommanLaunch()).longValue() > new Long(listaCommandDiagnostiche.get(dia.getCodCommand()).getIdCommanLaunch()).longValue() )
					{
						listaCommandDiagnostiche.put(rs.getString("COD_COMMAND"),dia);
					}
					
						
	
				}
				rs.close();
				diagnostiche.setDateLancioController(dateLancioController);
				diagnostiche.setListaCommandDiagnostiche(listaCommandDiagnostiche);
				this.initialize();
				prepareStatement("select upper(DESCR_FONTE) nome,COD_FONTE COD from c_fonte WHERE COD_FONTE=?");
				this.setInt(1, Integer.parseInt(request.getParameter("COD_FONTE")));
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				rs.next();
				diagnostiche.setDescrizioneFonte(tornaValoreRS(rs, "nome"));
				diagnostiche.setIdFonte(request.getParameter("COD_FONTE"));
				rs.close();
			}

			
			if(request.getParameter("dataController") != null && !request.getParameter("dataController").trim().equals(""))
			{
				diagnostiche.setTimeControllerSelected(request.getParameter("dataController"));

				sql += " and processid like ? ";
				List<Diagnostiche> ht = new ArrayList<Diagnostiche>();
				this.initialize();
				prepareStatement(sql+" ORDER BY FK_TIPO_DIA, COD_COMMAND");
				this.setInt(1, Integer.parseInt(request.getParameter("COD_FONTE")));
				this.setString(2, request.getParameter("COD_FONTE")+"@"+request.getParameter("dataController")+"%");
				
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				while (rs.next())
				{
					Diagnostiche dia = new Diagnostiche();
					dia.setCodCommand(tornaValoreRS(rs, "COD_COMMAND"));
					dia.setDescCommand(tornaValoreRS(rs, "DESC_COMMAND"));
					dia.setIdCommand(tornaValoreRS(rs, "ID_COMMAND"));
					dia.setIdCfgTemplate(tornaValoreRS(rs, "FK_TEMPLATE_CFG"));					
					dia.setTipo(tornaValoreRS(rs,"FK_TIPO_DIA").equals("1")?"Integrità":"Controllo");					
					dia.setIdFonte(tornaValoreRS(rs, "FK_ID_DIA_FONTE"));
					dia.setIdCommanLaunch(rs.getString("ID_COMMAND_LAUNCH"));
					ht.add(dia);
				}			
				diagnostiche.setListDiagnostiche(ht);
				
			}
			return diagnostiche;
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
	
	public String getMsgRighe(String idTempl, String idComLau)
		throws Exception
	{
		String retVal = "";
		Connection conn = null;		
		try
		{
			sql = "select SQL_LISTA" +
				" from sit_f_dia_template_cfg" +
				" where id = ?";
			String sqlLista = "";
			conn = this.getConnection();
			java.sql.ResultSet rs;
			this.initialize();
			//recupero la query dalla tabella di configurazione
			prepareStatement(sql);
			this.setInt(1, Integer.parseInt(idTempl));
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				sqlLista = tornaValoreRS(rs, "SQL_LISTA");
			}
			rs.close();
			//recupero il numero totale delle righe
			sqlLista = "select count(*) as numRighe from (" + sqlLista + ")";
			sql = sqlLista;
			this.initialize();
			prepareStatement(sql);
			this.setInt(1, Integer.parseInt(idComLau));
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());			
			int numRighe = 0;
			while (rs.next())
			{
				numRighe = rs.getInt("numRighe");
			}
			// se il numero delle righe è maggiore del massimo visualizzabile, il valore
			// di ritorno è il messaggio da mostrare a video (quindi è diverso da "")
			if (numRighe > MAX_RIGHE_VIS) {
				if (MAX_RIGHE_VIS == 1) {
					//caso comunque abbastanza assurdo...
					retVal = "E' visualizzabile solo una riga";
				}else{
					retVal = "Sono visualizzabili solo " + MAX_RIGHE_VIS + " righe";
				}
				retVal += " delle " + numRighe + " righe complessivamente presenti.";
			}
			return retVal;
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
	
	public List<LinkedHashMap<String,String>> caricaStoricoDiagnostiche(String idTempl, String idComLau, boolean tutte, boolean export)
		throws Exception
	{
		List<LinkedHashMap<String,String>> retVal = new ArrayList<LinkedHashMap<String,String>>();
		Connection conn = null;		
		try
		{
			if (!export) {
				sql = "select SQL_LISTA" +
				" from sit_f_dia_template_cfg" +
				" where id = ?";
			}else{
				sql = "select SQL_EXPORT" +
				" from sit_f_dia_template_cfg" +
				" where id = ?";
			}		
			String sqlListaExport = "";
			conn = this.getConnection();
			java.sql.ResultSet rs;
			this.initialize();
			//recupero la query dalla tabella di configurazione
			prepareStatement(sql);
			this.setInt(1, Integer.parseInt(idTempl));
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				if (!export) {
					sqlListaExport = tornaValoreRS(rs, "SQL_LISTA");
				}else{
					sqlListaExport = tornaValoreRS(rs, "SQL_EXPORT");
				}				
			}
			rs.close();
			//si prendono tutte le righe solo per l'esportazione (o se non sono più del massimo previsto in visualizzazione)
			if (!tutte) {
				sqlListaExport = "select * from (" + sqlListaExport + ") where rownum <= " + MAX_RIGHE_VIS;
			}
			sql = sqlListaExport;
			this.initialize();
			//recupero i dati da visualizzare
			prepareStatement(sql);
			this.setInt(1, Integer.parseInt(idComLau));
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			//recupero i nomi dei campi (da visualizzare come intestazioni della tabella, primo elemento della list)
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			LinkedHashMap<String,String> intestazioni = new LinkedHashMap<String,String>();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				//aggiungo 1 perché i nomi dei campi sono in base 1
				String nomeCampo = rsmd.getColumnName(i + 1);
				//in questo caso chiave e valore coincidono
				intestazioni.put(nomeCampo, nomeCampo);
			}
			retVal.add(intestazioni);
			//recupero i valori (elementi successivi della list)
			while (rs.next())
			{
				LinkedHashMap<String,String> valori = new LinkedHashMap<String,String>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					//aggiungo 1 perché i nomi dei campi sono in base 1
					String nomeCampo = rsmd.getColumnName(i + 1);
					String valoreCampo = tornaValoreRS(rs, nomeCampo);
					//la chiave è il nome del campo, il valore  corrisponde al valore letto nel db
					valori.put(nomeCampo, valoreCampo);
				}
				retVal.add(valori);
			}
			return retVal;
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
	
	public List<LinkedHashMap<String,String>> caricaStoricoDiagnostichePagina(List<LinkedHashMap<String,String>> lisStoDia, String paramPagina)
		throws Exception
	{
		try 
		{
			List<LinkedHashMap<String,String>> retVal = new ArrayList<LinkedHashMap<String,String>>();
			if (lisStoDia == null || lisStoDia.size() == 0) {
				return retVal;
			}
			int pagina = (paramPagina == null || paramPagina.equals("")) ? 1 : Integer.parseInt(paramPagina);
			//la prima riga è costituita dalle intestazioni; la prendo sempre e poi scalo di una posizione
			retVal.add(lisStoDia.get(0));
			int indiceDa = ((pagina - 1) * RIGHE_PER_PAGINA) + 1;
			int indiceA = (indiceDa + (RIGHE_PER_PAGINA - 1)) > (lisStoDia.size() - 1) ? (lisStoDia.size() - 1) : (indiceDa + (RIGHE_PER_PAGINA - 1));
			//si intende indiceA compreso, quindi <=
			for (int i = indiceDa; i <= indiceA; i++) {
				retVal.add(lisStoDia.get(i));
			}
			return retVal;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}		
	}
	
	public Hashtable<String,String> getCodDescCommand(Object listDia, String command) {
		Hashtable<String,String> retVal = new Hashtable<String,String>();
		String codCommand = "";
		String descCommand = "";
		if (listDia != null) {
			List listDiaList = ((Diagnostiche)listDia).getListDiagnostiche();
			for (int i = 0; i < listDiaList.size(); i++) {
				Diagnostiche dia = (Diagnostiche)listDiaList.get(i);
				if (command.equals(dia.getIdCommand())) {
					codCommand = dia.getCodCommand();
					descCommand = dia.getDescCommand();
				}
			}
		}		
		retVal.put(COD_COMMAND_SCELTO, codCommand);
		retVal.put(DESC_COMMAND_SCELTO, descCommand);
		return retVal;
	}
	
	public void esportaDati(HttpServletResponse response, String codCommand, String descCommand, String idComLau, LinkedHashMap lisDia, List lisStoDia) 
		throws Exception
	{
		try {
			
			response.setContentType(HEADER_EXPORT);
			String strDate = (String)lisDia.get(idComLau);
			String nomeFile = codCommand + "." + strDate.substring(0, 2) + strDate.substring(3, 5) + strDate.substring(6, 10);
			nomeFile += ESTENSIONE_FILE_EXPORT;
			nomeFile = "prova.csv";
			response.setHeader("Content-disposition", "attachment; filename=\"" + nomeFile + "\"" );
			PrintWriter out  =  response.getWriter();
			
			if (lisStoDia.size() > 0 && ((LinkedHashMap)lisStoDia.get(0)).size() > 0) {
				int numColonne = ((LinkedHashMap)lisStoDia.get(0)).size();
				StringBuffer riga;
				String dato;
				int countDati;
				boolean primaCella = true;
				//righe con dati (la prima di queste è la riga delle intestazioni)
				for (Object lhmObj : lisStoDia) {
					riga = new StringBuffer();
					countDati = 0;
					LinkedHashMap lhm = (LinkedHashMap)lhmObj;
					Iterator it = lhm.keySet().iterator();
					while (it.hasNext()) {
						String key = (String)it.next();
						dato = (String)lhm.get(key);
						if (primaCella) {
							//c'è un errore (aprendo con Excel) se il valore della prima cella della prima riga è, o comincia per, "ID"
							//l'errore è di tipo "SYLK: File format is not valid"
							//questo caso si verificava davvero con i dati di SIT_D_PERSONA!!!!!!!
							primaCella = false;
							dato = " " + dato; //risoluzione del problema di cui sopra
						}
						riga.append(dato);
						if (countDati < numColonne - 1) {
							riga.append(";");
						}
						countDati++;
					}
					out.println(riga.toString());
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
	}

}
