package it.webred.mui.http.codebehind;

import it.webred.docfa.model.DocfaFornitura;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

public class CodeBehinddocfaUploadListPage extends AbstractPage {
	
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		
		List<DocfaFornitura> docfaFornituras = getList();
		req.setAttribute("docfaFornituras", docfaFornituras);
		
		if (req.getMethod().equalsIgnoreCase("post") ) {
			//List<DocfaFornitura> docfaFornituras = getList();
			//req.setAttribute("docfaFornituras", docfaFornituras);
			return false;
		} 
		return true;
	}

	public List<DocfaFornitura> getList() {
		List<DocfaFornitura> docfaFornituras = new ArrayList<DocfaFornitura>();
		SimpleDateFormat dP = new SimpleDateFormat("yyyyMMdd");
		try {
			
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			/*String sqlForn = "select count(*) as numero_docfa, data as data_fornitura , caronte_data_inizio_val as data_inizio " +
							 "from doc_docfa_1_0 " +
							 "group by data ,caronte_data_inizio_val";
			*/
			String sqlForn = "SELECT   COUNT (*) AS numero_docfa, data_fornitura, data_inizio "
				 + "    FROM (SELECT DISTINCT DATA AS data_fornitura, protocollo, "
				 + "                          caronte_data_inizio_val AS data_inizio "
				 + "                     FROM doc_docfa_1_0) "
				 + "GROUP BY data_fornitura, data_inizio ";


			
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			Connection conn = theDataSource.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sqlForn);
			ResultSet rsForn = pstm.executeQuery();
			
			while(rsForn.next()) {
				DocfaFornitura docfaFornitura = new DocfaFornitura();
				docfaFornitura.setNumeroDocfa(rsForn.getLong("numero_docfa"));
				docfaFornitura.setDataFornitura(rsForn.getDate("data_fornitura"));
				docfaFornitura.setDataInizio(rsForn.getDate("data_inizio"));
				docfaFornituras.add(docfaFornitura);
			}
			
			rsForn.close();
			pstm.close();
			
			conn.close();
			
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(),
					"error while loading fornitura list", e);
			
		}
		return docfaFornituras;
	}

}
