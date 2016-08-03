package it.escsolution.escwebgis.diagnostiche.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsu;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuFinder;
import it.escsolution.escwebgis.diagnostiche.bean.DiagnosticheTarsuTot;
import it.webred.utils.DateFormat;

import java.beans.BeanDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DiagnosticheTarsuLogic extends EscLogic
{

	public DiagnosticheTarsuLogic(EnvUtente eu)
	{
		super(eu);
	}

	//private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from MUI_NOTA_TRAS WHERE 1=?";
	public static final String	FINDER					= "FINDER";
	public final static String	LISTA_DIAGNOSTICHE_PROP_RES	= "LISTA_DIAGNOSTICHE@DiagnosticheTarsuLogic";
	public final static String	DIAGNOSTICHE			= "DIAGNOSTICHE@DiagnosticheTarsuLogic";
	public final static String	SMS_DIAGNOSTICHE_RES	= "MESSAGGIO_DIAGNOSTICHE_TARSU_RES";
	public final static String	HEADER_EXPORT  			= "application/vnd.ms-excel";	
	public final static String	BEAN_DIAGNOSTICHE		= "BEAN_DIAGNOSTICHE@DiagnosticheTarsuLogic";
	

	private final static String SQL_PROP_RES_NO_TARSU = "SELECT DISTINCT foglio, particella, sub, unimm, categoria, sup_cat, " +
								"perc_poss, cuaa AS cfis, cognome, nome, denominazione, " +
								"data_nascita, posiz_ana, civi, topo, " +
								"anag_da,elimina " +
								"FROM PAM_UIU_PROP_TARSU_FAM " +
								"WHERE PROP_RES=1 " +
								"AND elimina IS NULL " +
								"AND PROP_PAGA IS NULL " +
								"AND FAMI_PAGA_CF IS NULL " +
								"AND FAMI_PAGA_PI IS NULL";


	private final static String SQL_SELECT_COUNT_LISTA_PRNT="select count(*) as conteggio " +
								"FROM ("+SQL_PROP_RES_NO_TARSU +") " ;



	public	Hashtable mCaricareDiagnostichePricipali()throws Exception
	{
		
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		DiagnosticheTarsuTot diaTot = new DiagnosticheTarsuTot();
		Connection conn = null;
		
		//tot proprietari residenti non risultanti in tarsu
		String sqlC = "SELECT COUNT(*) PROP_RES_NO_PAG FROM(" + SQL_PROP_RES_NO_TARSU +")";
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			java.sql.ResultSet rs = null;

			this.initialize();
			Statement ps = conn.createStatement();
			rs = ps.executeQuery(sqlC);

			if (rs.next()){
				diaTot.setPropResNoTarsu(rs.getString("PROP_RES_NO_PAG"));
			}
			rs.close();
			ps.close();
			
			ht.put(this.BEAN_DIAGNOSTICHE, diaTot);
			return ht;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			
		}
	}
	
	
	public Hashtable mCaricareListaPropRes(Vector listaPar, DiagnosticheTarsuFinder finder) throws Exception
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
			Statement st = conn.createStatement();
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA_PRNT;
				else
					sql = "SELECT * FROM(SELECT ROWNUM N,foglio, particella, sub, unimm, categoria, sup_cat, " +
								"perc_poss, cfis, cognome, nome, denominazione, " +
								"data_nascita, posiz_ana, civi, topo, " +
								"anag_da,elimina FROM (" +
							""+SQL_PROP_RES_NO_TARSU ;

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}

				
				rs = st.executeQuery(sql);

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						DiagnosticheTarsu dd = new DiagnosticheTarsu();
						
						dd.setAnagDa(rs.getString("anag_da"));
						dd.setCategoria(rs.getString("categoria"));
						dd.setCfis(rs.getString("cfis"));
						dd.setCivi(rs.getString("civi"));
						dd.setCognome(rs.getString("cognome"));
						dd.setDenominazione(rs.getString("denominazione"));
						dd.setDtNascita(rs.getString("data_nascita"));
						dd.setElimina(rs.getString("elimina"));
						dd.setFoglio(rs.getString("foglio"));
						dd.setNome(rs.getString("nome"));
						dd.setParticella(rs.getString("particella"));
						dd.setPercPoss(rs.getString("perc_poss"));
						dd.setPosAna(rs.getString("posiz_ana"));
						dd.setSub(rs.getString("sub"));
						dd.setSupCat(rs.getString("sup_cat"));
						dd.setTopo(rs.getString("topo"));
						dd.setUnimm(rs.getString("unimm"));
												
						vct.add(dd);
						
					}
					rs.close();
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
					rs.close();
				}
				
			}

			ht.put(DiagnosticheTarsuLogic.LISTA_DIAGNOSTICHE_PROP_RES,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(DiagnosticheTarsuLogic.FINDER,finder);
			
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
		String sqlC = "SELECT DISTINCT CAT FROM DOCFA_REPORT ORDER BY CAT";
		
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

	public String esportaDati(HttpServletResponse response) 
		throws Exception
	{
		Connection conn = null;
		try {
			
			conn = this.getConnection();
			
			//controllo che i dati entrino nel foglio (<=65.000)
			String sqlTest = SQL_SELECT_COUNT_LISTA_PRNT;
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlTest);
			int controllo =0;
			if (rs.next()){
				controllo = rs.getInt("conteggio");
			}
			rs.close();
			st.close();
			
			if (controllo > 65500){
				return "ATTENZIONE - La selezione ha prodotto un risultato troppo grande per la rappresentazione su file Excel!";
			}
			else{
				//preparo il modello xls sul foglio 0 (report)
				POIFSFileSystem fs  =  new POIFSFileSystem(new FileInputStream("prop_res_no_tarsu.xls"));
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				
				wb = scriviSheet(conn,wb);
				
				// Write the output to a file
				FileOutputStream fileOut = new FileOutputStream("c:\\prop_res_no_tarsu.xls");
				wb.write(fileOut);
				fileOut.close();
				
				//predispongo il download
				String afile = "c:\\prop_res_no_tarsu.xls";
				File f = new File(afile);
	
				response.setContentType("application/download");
				response.setHeader("Content-Disposition", "attachment; filename= \"prop_res_no_tarsu.xls\"");

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
	
	private HSSFWorkbook scriviSheet(Connection conn,HSSFWorkbook wb) throws Exception{
		
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		
		
		String sql = SQL_PROP_RES_NO_TARSU;
		
		HSSFSheet sheet = wb.getSheetAt(0);
		
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
			cell.setCellValue(new HSSFRichTextString(rs.getString("foglio")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("particella")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("sub")));
			
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString(rs.getString("unimm") ));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("categoria")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("sup_cat")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("perc_poss")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("cfis")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("cognome")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("nome")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("denominazione")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("data_nascita")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("posiz_ana")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("civi")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("topo")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("anag_da")));
			
			numCell++;
			cell = row.getCell(numCell);
			if (cell == null)
				cell = row.createCell((short)numCell);
			cell.setCellValue(new HSSFRichTextString( rs.getString("elimina")));
			
					
		}
		
		rs.close();
		
		return wb;
	}
	
	


}
