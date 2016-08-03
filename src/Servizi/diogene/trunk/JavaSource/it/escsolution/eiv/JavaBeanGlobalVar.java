/*
 * Created on 17-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavaBeanGlobalVar {
	public String MWFNAME="";
	private String DSN;
	private String DSNLogon;
	private String OracleSource;
	private String DSN_SQL;
	private String GlobalPath;
	public String Homepage="http://www.sitweb.it/EscIntranetViewerDev";
	private String dxfpath;
	private String download;
	public  String SiteLogon="NomeGruppo";
	private  String appli;
	private String pkprogetto="1";
	private double xCentroid,yCentroid,fWidth,fHeight;
	private boolean isValisPosition;
	
	public JavaBeanGlobalVar(){
		DSN="EscIntranetViewerDev";
		DSNLogon="ESCLogon";
		OracleSource="strade";
		DSN_SQL="";
		GlobalPath="http://www.sitweb.it/EscIntranetViewerDev/";
		Homepage="http://www.sitweb.it/EscIntranetViewerDev";
		dxfpath= "c:/progetti/progetti/dxf";
		download= "http://www.sitweb.it/download/";		
	}
	
	public JavaBeanGlobalVar(EnvUtente eu){
		SetMWFNAME(eu);
		DSN="EscIntranetViewerDev";
		DSNLogon="ESCLogon";
		OracleSource="strade";
		DSN_SQL="";
		GlobalPath="http://www.sitweb.it/EscIntranetViewerDev/";
		Homepage="http://www.sitweb.it/EscIntranetViewerDev";
		dxfpath= "c:/progetti/progetti/dxf";
		download= "http://www.sitweb.it/download/";		
	}
	
	public void  SetMWFNAME(EnvUtente eu){
		try{
			Context ctx = new InitialContext();
			if(ctx == null) 
				throw new Exception("Boom - No Context");
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  
				{
					/*Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT MAPFILENAME FROM EWG_PROGETTO WHERE PK_PROGETTO=?");
					pstmt.setString(1, pkprogetto);
					ResultSet rst = pstmt.executeQuery();
					while(rst.next())
						{
						MWFNAME=rst.getString("MAPFILENAME");
						}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setSitLogon(String SiteLogon){
		this.SiteLogon=SiteLogon;
	}
	public void setAppli(String appli){
			this.appli=appli;
		}
	public void setMWFNAME(String MWFNAME){
		this.MWFNAME=MWFNAME;
	}
	public void setDSN(String DSN){
		this.DSN=DSN;
	}
	public void setDSNLogon(String DSNLogon){
			this.DSNLogon=DSNLogon;
		}
	public void setDSN_SQL(String DSN_SQL){
			this.DSN_SQL=DSN_SQL;
		}
	public void setOracleSource(String OracleSource){
		this.OracleSource=OracleSource;
	}
	public void setGlobalPath(String GlobalPath ){
		this.GlobalPath=GlobalPath;
	}
	public void setHomepage(String Homepage){
		this.Homepage=Homepage;
	}
	public void setdxfpath(String dxfpath){
		this.dxfpath=dxfpath;
	}
	public void setdownload(String download){
		this.download=download;
	}
	
		
	
	
	public String getMWFNAME(){
		return this.MWFNAME;
	}
	public String getDSN(){
		return this.DSN;
	}
	public String getappli(){
			return this.appli;
		}
	public String getDSNLogon(){
		return this.DSNLogon;
	}
	public String getDSN_SQL(){
		return this.DSN_SQL;
	}
	public String getOracleSource(){
		return this.OracleSource;
	}
	public String getGlobalPath(){
		return this.GlobalPath;
	}
	public String getHomepage(){
		return this.Homepage;
	}
	public String getdxfpath(){
		return this.dxfpath;
	}
	public String getdownload(){
		return this.download;
	}
	public String getSiteLogon(){
	return this.SiteLogon;							
	}
	/**
	 * @return
	 */
	public double getFHeight() {
		
		return fHeight;
	}

	/**
	 * @return
	 */
	public double getFWidth() {
		
		return fWidth;
	}

	/**
	 * @return
	 */
	public double getXCentroid() {
		
		return xCentroid;
	}

	/**
	 * @return
	 */
	public double getYCentroid() {
		
		return yCentroid;
	
	}

	/**
	 * @param d
	 */
	public void setFHeight(double d) {
		fHeight = d;
		isValisPosition = true;
	}

	/**
	 * @param d
	 */
	public void setFWidth(double d) {
		fWidth = d;
		isValisPosition = true;
	}

	/**
	 * @param d
	 */
	public void setXCentroid(double d) {
		xCentroid = d;
		isValisPosition = true;
	}

	/**
	 * @param d
	 */
	public void setYCentroid(double d) {
		yCentroid = d;
		isValisPosition = true;
	}

	/**
	 * @return
	 */
	public boolean isValisPosition() {
		return isValisPosition;
	}
	public double getWidth(){
		isValisPosition = false;
		return fWidth > fHeight ? fWidth : fHeight;
	}
}