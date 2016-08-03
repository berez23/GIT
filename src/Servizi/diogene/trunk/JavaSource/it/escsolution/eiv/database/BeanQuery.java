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
import java.sql.Statement;
import java.util.Vector;

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
public class BeanQuery {
	public String Prova="Prova";
	public String Progetto="1";
	public Vector Temi;
	public Vector Temi2;
	public Vector Classi;
	public Vector ClassiKeyColumn;
	public Vector ClassiTableName;
	public Vector Zoom;
	public Vector ZoomVec;
	public Zoom zoom;
	public Vector ZoomColDesc;
	public String listLabel;
	public String listTipiParametri;
	public char separatore='/';
	public int count=0;
	public int count2;
	public String select;
	
	
	public void QueryTemi(HttpServletRequest request){
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
					PreparedStatement pstmt = conn.prepareStatement("SELECT PK_TEMA,NOME FROM EWG_TEMA WHERE FK_PROGETTO=?");
					pstmt.setString(1, Progetto);
					ResultSet rst = pstmt.executeQuery();
					Temi = new Vector();
					Temi2 = new Vector();
					while(rst.next()) {
						Temi.addElement(rst.getString("PK_TEMA"));
						Temi2.addElement(rst.getString("NOME"));
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return;		
	}
	
	
	public void QueryClassi(HttpServletRequest request){
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
					Classi = new Vector();
					ClassiKeyColumn = new Vector();
					ClassiTableName= new Vector();
					for(int i=0;i<Temi.size();i++){
						PreparedStatement pstmt = conn.prepareStatement("SELECT PK_CLASSE FROM EWG_CLASSE WHERE FK_TEMA=?");
						pstmt.setString(1, Temi.get(i).toString());
						ResultSet rst = pstmt.executeQuery();
						while(rst.next()) {
							Classi.addElement(rst.getString("PK_CLASSE"));
							/*ClassiKeyColumn.addElement(rst.getString("KEYCOLUMN"));
							ClassiTableName.addElement(rst.getString("TABLENAME"));*/
						}
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return;
	}
		
		
	public void ExeQuery(HttpServletRequest request){
		try{
			QueryTemi(request);
			QueryClassi(request);
			Context ctx = new InitialContext();
			if(ctx == null ) 
				throw new Exception("Boom - No Context");
			EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
			EnvSource es = new EnvSource(eu.getEnte());
			DataSource ds = (DataSource)ctx.lookup(es.getDataSourceIntegrato());
			if (ds != null) {
				Connection conn = ds.getConnection();
				if(conn != null)  {
					ZoomVec = new Vector();
					for(int i=0;i<Classi.size();i++){
						PreparedStatement pstmt = conn.prepareStatement("SELECT PK_ZOOM,NOME,COLDESC,WHERECLAUSE,DESCRIZIONE,TABLE_NAME, kEY_COLUMN FROM EWG_ZOOM WHERE FK_CLASSE=?");
						pstmt.setString(1, Classi.get(i).toString());
						ResultSet rst = pstmt.executeQuery();
	
						while(rst.next()) {
							Zoom zoom = new Zoom();
							zoom.pkZoom=(rst.getString("PK_ZOOM"));
							zoom.nome=(rst.getString("NOME"));
							zoom.colDesc=(rst.getString("COLDESC"));
							/*zoom.numeroParametri=(rst.getInt("NUMEROPARAMETRI"));*/
							zoom.whereClause=(rst.getString("WHERECLAUSE"));
							zoom.descrizione=(rst.getString("DESCRIZIONE"));
							zoom.pk_classe=Classi.get(i).toString();
							zoom.tablename=(rst.getString("TABLE_NAME"));
							zoom.keyColumn=(rst.getString("KEY_COLUMN"));
							/*zoom.tablename=ClassiTableName.get(i).toString();
							zoom.keyColumn=ClassiKeyColumn.get(i).toString();*/
							ZoomVec.add(zoom);
						}
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void GetParameter(HttpServletRequest request){
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
					Zoom z;
					for(int j=0;j<ZoomVec.size();j++){
						z = (Zoom) ZoomVec.get(j);
						z.CreaListParam();
						PreparedStatement pstmt = conn.prepareStatement("SELECT LABEL,TIPOPARAMETRO,OPTIONS FROM EWG_ZOOM_PARAM WHERE FK_ZOOM=? ORDER BY ORDINE");
						pstmt.setString(1,z.pkZoom.toString());
						ResultSet rst = pstmt.executeQuery();
						count=0;
						while(rst.next()) {
							Parametro p= new Parametro();
							p.label=rst.getString("LABEL");
							p.tipo=rst.getString("TIPOPARAMETRO");
							p.options=rst.getString("OPTIONS");
							count=count+1;
							z.listParam.addElement(p);
						}
						z.numeroParametri=count;	
					}
							conn.close();
						}
					}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return;
	}
	
	public void QueryCombo(Zoom z, int j, HttpServletRequest request){
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
					Parametro p;
					p=(Parametro) z.listParam.get(j);
					select=p.options;
					Statement pstmt = conn.createStatement();
					ResultSet rst = pstmt.executeQuery(select);
					p.CreaListCombo();
					count=0;
					while(rst.next()) {
						Combo c=new Combo();
							c.ValCombo=rst.getString(2);
							c.ValComboCod=rst.getString(1);
							p.listCombo.addElement(c);
							}
							conn.close();
						}
					}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return;
	}
	
	public void ZoomQuery(Zoom z, HttpServletRequest request){
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
					select="SELECT XCENTROID, YCENTROID, FWIDTH, FHEIGHT,"+" "+z.keyColumn+" , "+z.colDesc+" "+"FROM"+" "+ z.tablename+" "+ "WHERE"+" "+z.whereClause;
					PreparedStatement pstmt = conn.prepareStatement(select);
					int g=1;
					for(int j=0;j<z.numeroParametri;j++){
						pstmt.setString(g,z.parametri.get(j).toString());
						g=g+1;
					}
					ResultSet rst = pstmt.executeQuery();
					z.keyColumnVec = new Vector();
					z.descColumVec = new Vector();
					z.XCENTROID=new Vector();
					z.YCENTROID=new Vector();
					z.FHEIGHT=new Vector();
					z.FWIDTH=new Vector();
					while(rst.next()) {
						z.keyColumnVec.addElement(rst.getString(z.keyColumn));
						z.descColumVec.addElement(rst.getString(z.colDesc));
						z.XCENTROID.addElement(rst.getString("XCENTROID"));
						z.YCENTROID.addElement(rst.getString("YCENTROID"));
						z.FWIDTH.addElement(rst.getString("FWIDTH"));
						z.FHEIGHT.addElement(rst.getString("FHEIGHT"));
					}
					conn.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return;			
	}

}
