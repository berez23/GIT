package it.escsolution.escwebgis.catasto.logic;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.TiffUtill;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;

public class CatastoPlanimetrieComma340Logic extends EscLogic {

	public final static String PLANIMETRIE_COMMA_340_CIM = "PLANIMETRIE_COMMA_340_CIM@CatastoPlanimetrieComma340Logic";
	public final static String PLANIMETRIE_COMMA_340_DOCFA = "PLANIMETRIE_COMMA_340_DOCFA@CatastoPlanimetrieComma340Logic";
	public final static String PLANIMETRIE_COMMA_340_DOCFA_CENS = "PLANIMETRIE_COMMA_340_DOCFA_CENS@CatastoPlanimetrieComma340Logic";
	public final static String PLANIMETRIE_COMMA_340_TARSU = "PLANIMETRIE_COMMA_340_TARSU@CatastoPlanimetrieComma340Logic";
		
	private final static String SQL_PLANIMETRIE_COMMA_340 = "select * from " +
	"(select nome_file_planimetrico, decode(nvl(subalterno, ' '), ' ', '9999', subalterno) as subalterno " +
	"from sit_cat_planimetrie_c340 " +
	"where foglio = lpad(?, 4, '0') " + 
	"and numero = lpad(?, 5, '0') " +
	"and subalterno = lpad(?, 4, '0') " +
	"union all " +
	"select nome_file_planimetrico, decode(nvl(subalterno, ' '), ' ', '9999', subalterno) as subalterno " +
	"from sit_cat_planimetrie_c340 " +
	"where foglio = lpad(?, 4, '0') " +
	"and numero = lpad(?, 5, '0') " +
	"and decode(nvl(subalterno, ' '), ' ', '9999', subalterno) = '9999') " +
	" order by subalterno";
	
	public CatastoPlanimetrieComma340Logic(EnvUtente eu) {
		super(eu);
	}
	
	public ArrayList<ArrayList<String>> getPlanimetrieComma340(String foglio, String particella, String unimm, String pathDatiDiogene) throws Exception {
		ArrayList<ArrayList<String>> planimetrieComma340 = new ArrayList<ArrayList<String>>();
		
		if (pathDatiDiogene == null || pathDatiDiogene.trim().equals("")) {
			return planimetrieComma340;
		}
		
		String pathPlanimetrieComma340 = pathDatiDiogene + File.separatorChar + this.getDirPlanimetrieComma340Ente();
		pathPlanimetrieComma340 = pathPlanimetrieComma340.replace('\\', File.separatorChar).replace('/', File.separatorChar);
		log.debug("PATH PLANIMETRIE C340: " + pathPlanimetrieComma340);
		
		Connection conn = null;		
		try {
			ArrayList<ArrayList<String>> prefissi = new ArrayList<ArrayList<String>>();
			
			String sql = SQL_PLANIMETRIE_COMMA_340;			
			this.setDatasource(JNDI_DIOGENE);
			conn = this.getConnection();
			this.initialize();			
			this.setString(1, foglio);
			this.setString(2, particella);
			this.setString(3, unimm);
			this.setString(4, foglio);
			this.setString(5, particella);
			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next()){
				ArrayList<String> prefisso = new ArrayList<String>();
				prefisso.add(rs.getString("nome_file_planimetrico"));
				prefisso.add(rs.getString("subalterno"));
				prefissi.add(prefisso);
			}
			
			for (ArrayList<String> prefisso : prefissi) {
				PrefissoComma340Filter filter = new PrefissoComma340Filter(prefisso.get(0));
				String[] files = new File(pathPlanimetrieComma340).list(filter);
				if (files != null) {
					for (String file : files) {
						ArrayList<String> link = new ArrayList<String>();
						link.add(file);
						link.add(pathPlanimetrieComma340 + File.separatorChar + file);
						link.add(prefisso.get(1));
						link.add(getFormato(conn, prefisso.get(0)));
						planimetrieComma340.add(link);
					}
				}
			}		
			
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return planimetrieComma340;
	}
	
	private String getFormato(Connection conn, String nomePlan) {
		String retVal = "" + TiffUtill.FORMATO_DEF;
		try {
			String sql = "select formato from docfa_planimetrie where nome_plan = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nomePlan);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				retVal = rs.getString("formato");
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			//in caso di eccezione, non fa nulla e restituisce il formato di default
		}
		return retVal;
	}
	
	class PrefissoComma340Filter implements FilenameFilter {
		
		private String prefisso;
		
		public PrefissoComma340Filter(String prefisso) {
			this.prefisso = prefisso;
		}
		
	    public boolean accept(File dir, String name) {
	        return (name.startsWith(prefisso + ".") || name.startsWith(prefisso + "_"));
	    }
	}
	
}
