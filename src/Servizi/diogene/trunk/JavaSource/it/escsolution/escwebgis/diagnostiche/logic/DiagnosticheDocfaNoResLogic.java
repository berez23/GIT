package it.escsolution.escwebgis.diagnostiche.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.diagnostiche.bean.DateFornituraDocfa;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfaNoRes;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheDocfaNoResFinder;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.webred.utils.DateFormat;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DiagnosticheDocfaNoResLogic extends EscLogic
{

	public DiagnosticheDocfaNoResLogic(EnvUtente eu)
	{
		super(eu);
	}

	public static final String	FINDER					= "FINDER";
	public final static String	LISTA_DIAGNOSTICHE		= "LISTA_DIAGNOSTICHE_NORES";
	public final static String	DIAGNOSTICHE			= "DIAGNOSTICHE@DiagnosticheDocfaNoResLogic";
	public final static String	SMS_DIAGNOSTICHE_NORES	= "MESSAGGIO_DIAGNOSTICHE_DOCFA_NORES";
	public final static String	DA_DATA_FORNITURA		= "DA_DATA_FORNITURA_NORES";
	public final static String	A_DATA_FORNITURA		= "A_DATA_FORNITURA_NORES";
	public final static String	LISTA_CATEGORIE			= "LISTA_CATEGORIE@DiagnosticheDocfaNoResLogic";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM( " +
	"select rownum n,FORNITURA,NREC_DATI_CENSUARI, " +
	"NREC_DATAREG_NO_COER,NREC_TIPO_OPER_NO_UNICO, " +
	"NREC_TIPO_OPER_NULL,NREC_TIPO_OPER_CESS, " +
	"NREC_DOCFA_OK, NREC_UIU " +
	"FROM DOCFA_DIAGNOSTICHE_NORES " +
	"WHERE 1= ?  " ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio " +
	"FROM DOCFA_DIAGNOSTICHE_NORES where 1= ? " ;

	public Hashtable mCaricareLista(Vector listaPar, DiagnosticheDocfaNoResFinder finder) throws Exception
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

			for (int i=0;i<=1;i++)
			{
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
				}
				else{
					//controllo provenienza chiamata
					String chiavi = finder.getKeyStr();
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + " order by FORNITURA ";
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						DiagnosticheDocfaNoRes dd = new DiagnosticheDocfaNoRes();
						java.sql.Date dataF = DateFormat.stringToDate(rs.getString("FORNITURA"),"yyyy-MM-dd");
						String dtStr = DateFormat.dateToString(dataF, "dd/MM/yyyy"); 
						dd.setFornitura(dtStr);
						dd.setNrecDatiCensuari(rs.getString("NREC_DATI_CENSUARI"));
						dd.setNrecDataRegNoCoer(rs.getString("NREC_DATAREG_NO_COER"));
						dd.setNrecTipoOperNoUnico(rs.getString("NREC_TIPO_OPER_NO_UNICO"));
						dd.setNrecTipoOperNull(rs.getString("NREC_TIPO_OPER_NULL"));
						dd.setNrecTipoOperCess(rs.getString("NREC_TIPO_OPER_CESS"));
						dd.setNrecDocfaOK(rs.getString("NREC_DOCFA_OK"));
						dd.setNrecUIU(rs.getString("NREC_UIU"));
						vct.add(dd);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(DiagnosticheDocfaNoResLogic.LISTA_DIAGNOSTICHE,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);
			
			ht.put(DiagnosticheDocfaNoResLogic.FINDER,finder);
			
			//aggiungo elenco categorie non residenziali
			Vector vCat = this.mCaricareListaCategorie(conn);
			ht.put(DiagnosticheDocfaNoResLogic.LISTA_CATEGORIE, vCat);
			
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
	
	
	private Vector mCaricareListaCategorie(Connection conn) throws Exception
	{
		Vector vct = new Vector();
		String sqlC = "SELECT DISTINCT CAT FROM DOCFA_REPORT_NORES ORDER BY CAT";
		
		try
		{
			java.sql.ResultSet rs = null;

			this.initialize();
			Statement ps = conn.createStatement();
			rs = ps.executeQuery(sqlC);

			while (rs.next()){
				String cat = rs.getString("CAT");
				vct.add(cat);
			}
			rs.close();
			ps.close();
				
			return vct;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		
	}

	

	public Vector elencoForniture() throws Exception{
		Vector vct = new Vector();
		String sql = "SELECT DISTINCT FORNITURA FROM DOCFA_DIAGNOSTICHE_NORES ORDER BY FORNITURA ASC";
		Connection conn =  null;
		try{
			conn =this.getConnection();
			Statement st =conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			vct.add(new DateFornituraDocfa("",""));
			while (rs.next()){
				java.sql.Date dataF = DateFormat.stringToDate(rs.getString("FORNITURA"),"yyyy-MM-dd");
				String idStr = DateFormat.dateToString(dataF, "dd/MM/yyyy");
				String dtStr = idStr.substring(3);
				vct.add(new DateFornituraDocfa(idStr,dtStr));

			}
		
		
		return vct;
		}catch(Exception e){
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

	
	
	public String esportaDati(String pathDatiDiogene,HttpServletResponse response, String daDataFornitura, String aDataFornitura,String categoria) 
		throws Exception
	{
		
		/*Compone il percorso dei dati di diogene per l'ente corrente*/ 
		String pathModelloXls = "";
		if (pathDatiDiogene != null && !pathDatiDiogene.trim().equals("")) {
			pathModelloXls = pathDatiDiogene + File.separatorChar + this.getDirModelloXlsEnte();
			
		}
		
		log.debug("PATH MODELLO XSL: " + pathModelloXls);
		
		Connection conn = null;
		try {
			
			conn = this.getConnection();
			
			//controllo che i dati entrino nel foglio (<=65.000)
			String sqlTest = "SELECT COUNT(*) AS CONTA FROM DOCFA_REPORT_NORES R WHERE 1=1";
			String order = " ORDER BY FORN,PROT";
			if (daDataFornitura!= null && !daDataFornitura.equals(""))
				sqlTest = sqlTest + " AND FORN >= TO_DATE('"+daDataFornitura+"','DD/MM/YYYY')";
			if (aDataFornitura!= null && !aDataFornitura.equals(""))
				sqlTest = sqlTest + " AND FORN <= TO_DATE('"+aDataFornitura+"','DD/MM/YYYY')";
			if (categoria != null && !categoria.equals(""))
				sqlTest = sqlTest + " AND CAT = '"+categoria+"'";
			sqlTest = sqlTest + order;
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlTest);
			int controllo =0;
			if (rs.next()){
				controllo = rs.getInt("CONTA");
			}
			rs.close();
			st.close();
			
			if (controllo > 65500){
				return "ATTENZIONE - La selezione ha prodotto un risultato troppo grande per la rappresentazione su file Excel!";
			}
			else{
				//preparo il modello xls sul foglio 0 (report)
				POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream(pathModelloXls+"\\modello_acc_cat_no_res.xls"));
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				
				wb = scriviReportSheet(conn,wb,daDataFornitura,aDataFornitura,categoria);
				
				wb= scriviZcSheet(conn,wb,daDataFornitura,aDataFornitura,categoria); 
				
				// Write the output to a file
				FileOutputStream fileOut = new FileOutputStream(pathModelloXls+"\\docfa_acc_cat_no_res.xls");
				wb.write(fileOut);
				fileOut.close();
				
				//predispongo il download
				String afile = pathModelloXls+"\\docfa_acc_cat_no_res.xls";
				File f = new File(afile);
	
				response.setContentType("application/download");
				response.setHeader("Content-Disposition", "attachment; filename= \"docfa_acc_cat_no_res.xls\"");

				FileInputStream fin = new FileInputStream(f);
				int size = fin.available();
				response.setContentLength(size);
				byte[] ab = new byte[size];
				OutputStream os = response.getOutputStream();
				int bytesread;
	
				do{
					bytesread = fin.read(ab,0,size);
					if(bytesread >-1)
						os.write(ab,0,bytesread );
				}while(bytesread >-1);
				
				fin.close();
				os.flush();
				os.close();
				
				//elimino il file generato sul server
				if (f.exists())
					f.delete();
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
		
		return null;
	}
	
	private HSSFWorkbook scriviReportSheet(Connection conn,HSSFWorkbook wb,String daDataFornitura,String aDataFornitura,String categoria) throws Exception{
		
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		
		
		String sql = "SELECT * FROM DOCFA_REPORT_NORES R WHERE 1=1";
		String order = " ORDER BY FORN,PROT";
		if (daDataFornitura!= null && !daDataFornitura.equals(""))
			sql = sql + " AND FORN >= TO_DATE('"+daDataFornitura+"','DD/MM/YYYY')";
		if (aDataFornitura!= null && !aDataFornitura.equals(""))
			sql = sql + " AND FORN <= TO_DATE('"+aDataFornitura+"','DD/MM/YYYY')";
		if (categoria != null && !categoria.equals(""))
			sql = sql + " AND CAT = '"+categoria+"'";
		sql = sql + order;
		
		HSSFSheet sheet = wb.getSheet("REPORT");
		
		//recupero dati da inserire nel foglio
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		int riga = 0;
		
		while (rs.next()){
			riga++;
			int numCell = 0;
			// esportazione dati in formato CSV
			HSSFRow row = sheet.getRow(riga);
			if (row == null)
				row = sheet.createRow(riga);
			
			
			HSSFCell cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new java.util.Date(rs.getDate("FORN").getTime()));
			cell.setCellStyle(cellStyleDate);
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(Double.parseDouble( rs.getString("ID_IMM")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("PROT")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			String dtRegform = rs.getString("DATA_REG");
			cell.setCellValue(new HSSFRichTextString(dtRegform ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			String foglio = rs.getString("FOGLIO");
			try {
				if (foglio != null)
					cell.setCellValue(Double.parseDouble(foglio));
			} catch (Exception e) {
				cell.setCellValue(new HSSFRichTextString(foglio));
			}
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("PART")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("SUB")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("SUB")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("ZC_DOCFA")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("ZC_DOCFA")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("ZC_CAT")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("ZC_CAT")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("ZC_DIFF")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("ZC_DIFF")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("OLD_MZ")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("NEW_MZ")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("CAT")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("CLASSE")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("CLASSE_MIN")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("CLASSE_MIN_C06")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("DIFF")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("DIFF")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("DEST_USO")));
			  
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("CONSISTENZA")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("CONSISTENZA")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("SUPERFICIE")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("SUPERFICIE")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("RENDITA_DOCFA")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("RENDITA_DOCFA")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("TOPONIMO")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("INDIRIZZO")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("CIVICO_1")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("DIC_COGNOME")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("DIC_NOME")));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("TEC_COGNOME")));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("TEC_NOME")));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			if (rs.getString("NC")!= null)
				cell.setCellValue(Double.parseDouble( rs.getString("NC")));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("GRAFFATI")));

			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("TIPO_OPERAZIONE")));
			
		}
		
		rs.close();
		
		return wb;
	}
	
	
private HSSFWorkbook scriviZcSheet(Connection conn,HSSFWorkbook wb,String daDataFornitura,String aDataFornitura,String categoria) throws Exception{
		
	
	String sqlZC = "SELECT DISTINCT TO_NUMBER(ZC_DOCFA) AS ZONA_DIC, TO_NUMBER(ZC_CAT) AS ZONA, TO_NUMBER(FOGLIO) AS FOGLIO," +
						"OLD_MZ, NEW_MZ, count(*) AS OCCORRENZE " +
						"FROM DOCFA_REPORT_NORES C2 " +
						"WHERE TO_NUMBER(C2.ZC_DOCFA) <> C2.ZC_CAT ";
	String groupOrder = " GROUP BY (TO_NUMBER(ZC_DOCFA),TO_NUMBER(ZC_CAT),TO_NUMBER(FOGLIO),OLD_MZ, NEW_MZ) " +
						"ORDER BY TO_NUMBER(FOGLIO)";
	
	if (daDataFornitura!= null && !daDataFornitura.equals(""))
		sqlZC = sqlZC + " and to_date(data_reg,'dd/mm/yyyy') >= to_date('"+daDataFornitura+"','dd/mm/yyyy') "; 
	if (aDataFornitura!= null && !aDataFornitura.equals(""))
		sqlZC = sqlZC + " and to_date(data_reg,'dd/mm/yyyy') <= to_date('"+aDataFornitura+"','dd/mm/yyyy') ";
	if (categoria != null && !categoria.equals(""))
		sqlZC = sqlZC + " AND CAT = '"+categoria+"'";

	sqlZC = sqlZC + groupOrder;
		
	HSSFSheet sheet = wb.getSheet("ZC_ERRATE");
		
	//recupero dati da inserire nel foglio
		
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(sqlZC);
	
	int riga = 0;
		
	while (rs.next()){
		riga++;
		int numCell = 0;
		
		HSSFRow row = sheet.getRow(riga);
		if (row == null)
			row = sheet.createRow(riga);
			
		HSSFCell cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		if (rs.getString("ZONA_DIC")!= null)
			cell.setCellValue(Double.parseDouble( rs.getString("ZONA_DIC")));
		
		
		numCell++;
		cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		if (rs.getString("ZONA")!= null)
			cell.setCellValue(Double.parseDouble( rs.getString("ZONA")));
		
		numCell++;
		cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		String foglio = rs.getString("FOGLIO");
		try {
			if (foglio != null)
				cell.setCellValue(Double.parseDouble(foglio));
		} catch (Exception e) {
			cell.setCellValue(new HSSFRichTextString(foglio));
		}
			
		numCell++;
		cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString( rs.getString("OLD_MZ")));
			
		numCell++;
		cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString( rs.getString("NEW_MZ")));
		
		numCell++;
		cell = row.getCell(numCell);
		if (cell == null)
			cell = row.createCell((short)numCell);
		if (rs.getString("OCCORRENZE")!= null)
			cell.setCellValue(Double.parseDouble( rs.getString("OCCORRENZE")));
	}
		
	rs.close();
	st.close();
		
	return wb;
	}

}
