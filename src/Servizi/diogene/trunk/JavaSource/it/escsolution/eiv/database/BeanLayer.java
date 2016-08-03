/*
 * Created on 13-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.database;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.webred.cet.permission.CeTUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BeanLayer {
	public String url="";
	public String nomeclasse="";
	public String label="";
	public String pk_classe;
	
	
	
	public String GetClasseLayer(String layer, HttpServletRequest request){
		try{
			Context ctx = new InitialContext();
			if(ctx == null ) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  {
					/*Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT FK_CLASSE FROM EWG_STILE WHERE NAME=?");
					pstmt.setString(1, layer);
					ResultSet rst = pstmt.executeQuery();
					pk_classe="inesistente";
					while(rst.next()) {
						pk_classe=rst.getString("FK_CLASSE");
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return pk_classe;	
	}	
	
	public String Interroga(String pk_classe, HttpServletRequest request){
		try{
			Context ctx = new InitialContext();
			if(ctx == null ) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  {
					/*Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT URLLISTVIEW FROM EWG_CLASSE WHERE PK_CLASSE=?");
					pstmt.setString(1, pk_classe);
					ResultSet rst = pstmt.executeQuery();
					while(rst.next()) {
						url=rst.getString("URLLISTVIEW");
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return url;		
	}
	
	public String InterrogaLabel(String pk_classe, HttpServletRequest request){
		try{
			Context ctx = new InitialContext();
			if(ctx == null ) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  {
					/*Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT LABEL FROM EWG_CLASSE WHERE PK_CLASSE=?");
					pstmt.setString(1, pk_classe);
					ResultSet rst = pstmt.executeQuery();
					while(rst.next()) {
						label=rst.getString("LABEL");
					}
					conn.close();
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
		}
		return label;		
	}
	
	
	
			
	public String GetNomeClasse(String pk_classe, HttpServletRequest request){
		try{
			Context ctx = new InitialContext();
			if(ctx == null ) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  {
					/*Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery("SELECT PK_TEMA FROM EWG_TEMA WHERE FK_PROGETTO=?");*/
					PreparedStatement pstmt = conn.prepareStatement("SELECT NOMECLASSE FROM EWG_CLASSE WHERE PK_CLASSE=?");
					pstmt.setString(1, pk_classe);
					ResultSet rst = pstmt.executeQuery();
					while(rst.next()) {
						nomeclasse=rst.getString("NOMECLASSE");
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}    
		return nomeclasse;	      	
	}
}