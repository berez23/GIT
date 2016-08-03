package it.webred.fascicolo;

import it.escsolution.escwebgis.common.EnvBase;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.Utils;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.utils.GenericTuples;

import java.awt.Color;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.log4j.Logger;

import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

/**
 * @author dan
 * 
 */
public abstract class FascicoloActionRequestHandler extends EnvBase
{	
		private static final String sqlLatLong = "SELECT t.y lat, t.x lon" 
		    +"    FROM sitipart_3d p, "
		    +"    TABLE (sdo_util.getvertices (sdo_cs.transform (p.shape_pp, "
		    +"                                                  MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 1313328, 2820083, 0.0050),MDSYS.SDO_DIM_ELEMENT('Y', 3930191, 5220322.5, 0.0050)), "
		    +"                                                 8307 "
		    +"                                                ) "
		    +"                              ) "
		    +"        ) t "
		    +"    WHERE P.COD_NAZIONALE = ? "
		    +"    AND P.FOGLIO = ? "
		    +"    AND P.PARTICELLA = ?";
	
	
	
	abstract public LinkedList<Object> leggiDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
		throws Exception;
	abstract public LinkedList<Object> stampaDati(HttpServletRequest request, String codNazionale, String foglio, String particella, String sub, String situazione, String filtroData)
	throws Exception;
	protected Properties gedPropertiesReader(String fileName)
	throws Exception
	{
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties props = new Properties();
		props.loadFromXML(is);
		return props;

	}

	
	protected Connection getConnectionDbIntegrato(HttpServletRequest request)
		throws Exception
	{

		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource)initContext.lookup(es.getDataSourceIntegrato());
		Connection conn = ds.getConnection();
		return conn;
	}

	protected Connection getConnectionDbTotale(HttpServletRequest request)
		throws Exception
	{

		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource)initContext.lookup(es.getDataSource());
		Connection conn = ds.getConnection();
		return conn;
	}

	protected Connection getConnectionDiogene(HttpServletRequest request)
		throws Exception
	{

		Context initContext = new InitialContext();
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		EnvSource es = new EnvSource(eu.getEnte());
		DataSource ds = (DataSource)initContext.lookup(es.getDataSource());
		Connection conn = ds.getConnection();
		return conn;
	}
	public RowSetDynaClass genericFPQuery(Connection con, String sql, String codNazionale, String foglio, String particella, String sub)
	throws Exception
	{
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try
		{
			int i=1;
			stmt = con.prepareStatement(sql);
			if(codNazionale != null)
				stmt.setString(i++, codNazionale);
			stmt.setString(i++, foglio);
			stmt.setString(i++, particella);
			if(sub != null)
				stmt.setString(i++, sub);
			
			if (sql != null)
				log.debug("SQL: " + sql);
			if (codNazionale != null)
				log.debug("Cod. Nazionale: " + codNazionale);
			if (foglio != null)
				log.debug("Foglio: " + foglio);
			if (particella != null)
				log.debug("Particella: " + particella);
			if (sub != null)
				log.debug("Sub: " + sub);
			
			rs = stmt.executeQuery();
			
//			int cnt =0;
//			while(rs.next()){
//				cnt++;
//			}
//			log.debug("RS SIZE: " + cnt);
			
			return new RowSetDynaClass(rs);	
		}
		finally
		{			
				try
				{
					if(rs != null)
						rs.close();
					if(stmt != null)
						stmt.close();					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
	}
	
	public RowSetDynaClass genericFPQueryDouble(Connection con, String sql, String codNazionale, String foglio, String particella)
	throws Exception
	{
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try
		{
			int i=1;
			stmt = con.prepareStatement(sql);
			if(codNazionale != null)
				stmt.setString(i++, codNazionale);
			stmt.setString(i++, foglio);
			stmt.setString(i++, particella);
			if(codNazionale != null)
				stmt.setString(i++, codNazionale);
			stmt.setString(i++, foglio);
			stmt.setString(i++, particella);
			rs = stmt.executeQuery();
			return new RowSetDynaClass(rs);	
		}
		finally
		{			
				try
				{
					if(rs != null)
						rs.close();
					if(stmt != null)
						stmt.close();					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
	}
	
	public String levaZeri(String s)
	{
		try
		{
			if(s==null || s.equals(""))
				return null;
			return new Long(s).toString();
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public PdfPCell nullsafeCell(Object o)
	{
		return nullsafeCell(o, MY_PDF_DEFUALT_FONT);
	}
	public PdfPCell nullsafeCellH(Object o)
	{
		PdfPCell cell =  nullsafeCell(o, MY_PDF_DEFUALT_FONTH);		
		cell.setBackgroundColor(MY_HEADER_COLOR);	
		return cell;
	}	
	private PdfPCell nullsafeCell(Object o, Font font)
	{
		String s =  o==null?"":o.toString();
		return new PdfPCell(new Phrase(s, font));
	}
	
	public static final Font MY_PDF_DEFUALT_FONT = new Font(Font.COURIER, 7);
	public static final Font MY_PDF_DEFUALT_FONTH = new Font(Font.COURIER, 8);
	public static final Color MY_HEADER_COLOR = new Color(185, 185, 185);
	
	public String[] getLatitudeLongitude(HttpServletRequest request,String foglio, String particella, String codEnte) {
	
	  String[] latLon = {"0","0"};
		
	  CeTUser utente = (CeTUser) request.getSession().getAttribute("user");
		
	  String enteId = utente.getCurrentEnte();
	  String userId = utente.getUsername();
	  String sessionId =utente.getSessionId();
	  try{
		  	  
	  if (!foglio.equals("") && !particella.equals("")){
		  
			  CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
			  RicercaOggettoCatDTO roCat = new RicercaOggettoCatDTO();
				
				roCat.setEnteId(enteId);
				roCat.setUserId(userId);
				roCat.setFoglio(foglio);
				roCat.setParticella(particella);
				roCat.setCodEnte(codEnte);
				
				latLon = catastoService.getLatitudineLongitudine(roCat);
			  
		 
	  }
	  
	  }catch(Exception e){}
	  
	  return latLon;
	
	}
	
/*	public GenericTuples.T2<String, String> getLatitudeLongitude(Connection con,String foglio, String particella, String codEnte) throws Exception {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		HashMap ht = new LinkedHashMap();
		try {
			
			stmt = con.prepareStatement(sqlLatLong);

			stmt.setString(1,codEnte);
			stmt.setString(2,foglio);
			stmt.setString(3,particella);
			
	
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new GenericTuples.T2<String, String>(rs.getString(1), rs.getString(2));
			}
			return new GenericTuples.T2<String, String>("0","0");
		} catch (Exception e) {
			throw e;
		} 
		finally
		{			
				try
				{
					if(rs != null)
						rs.close();
					if(stmt != null)
						stmt.close();					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
	}
	*/
	
	
	
	
}
