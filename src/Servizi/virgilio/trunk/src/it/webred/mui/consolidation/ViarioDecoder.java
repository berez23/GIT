package it.webred.mui.consolidation;

import it.webred.docfa.model.Residenza;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.CodiceViaCivico;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

public class ViarioDecoder {

	private String decodeViaStatement = null;
	private String decodeViaStatement2 = null;
	private String decodeViaTotaleStatement = null;
	/**
	 * @param args
	 */
	public ViarioDecoder()  {
		loadStatement();
	}

	private void loadStatement() {
		try {
			decodeViaStatement = loadResourceInString("/sql/decodeViaStatement.sql");
			decodeViaStatement2 = loadResourceInString("/sql/decodeViaStatement2.sql");
			decodeViaTotaleStatement = loadResourceInString("/sql/decodeViaTotaleStatement.sql");
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getCodiciViaStatement statement ",
					e);
			decodeViaStatement = null;
			decodeViaStatement2 = null;
		}
	}

	private String loadResourceInString(String res) throws IOException {

		return loadFileInString(this.getClass().getClassLoader()
				.getResourceAsStream(res));
	}


	
	private static String loadFileInString(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		int chunkLength = 256;
		byte[] buf = new byte[chunkLength];
		int readen = -1;
		while ((readen = is.read(buf)) != -1) {
			sb.append(new String(buf, 0, readen));
		}
		return sb.toString();
	}

	private String decodeViaTotale(it.webred.mui.model.Residenza res) throws SQLException, NamingException {
		String cu = res.getCivicoUnico();
		return decodeViaInViario(cu, 1, 1);
	}
	
	private String decodeViaTotale(CodiceViaCivico cvc) throws SQLException, NamingException {
		String cu = cvc.getCivicoUnico();
		return decodeViaInViario(cu, 4, 1);
		
		
	}
	private String decodeViaTotale(Residenza res) throws SQLException, NamingException {
		String cu = res.getCivicoUnico();
		return decodeViaInViario(cu, 1, 1);
	}
	
	private String decodeViaInViario(String civicoUnico, int es, int progEs) throws SQLException, NamingException {
		
		if(civicoUnico == null || civicoUnico.trim().length() == 0){
			return null;
		}
		if(decodeViaTotaleStatement == null){
			loadStatement();
			if(decodeViaTotaleStatement == null){
				return null;
			}
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn.prepareStatement(decodeViaTotaleStatement);
			Logger.log().info(
					this.getClass().getName(),
					"ViarioDecoder decodeViaTotaleStatement query is \"" + decodeViaTotaleStatement.replace("\n","")
							+ "\"");
			pstmt.setString(1, civicoUnico);
			pstmt.setInt(2, es);
			pstmt.setInt(3, progEs);
			
			pstmt.setString(1, civicoUnico);
			
			Logger.log()
					.info(
							this.getClass().getName(),
							"ViarioDecoder adding parameter " + civicoUnico
									+ " to decodeViaStatement query ");
			rset = pstmt.executeQuery();
			String result = null;
			if (rset.next()) {
				result = rset.getString(1);
			}
			return result;
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via totale", e);
			return null;
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via totale ", e);
			return null;
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via totale ", e);
			return null;
		} finally {
			try {
				rset.close();
			} catch (Throwable e) {
			}
			try {
				pstmt.close();
			} catch (Throwable e) {
			}
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}
	
	private String decodeViaInAnagrafe(String codiceVia) throws SQLException, NamingException {
		
		if(codiceVia == null || codiceVia.trim().length() == 0){
			return null;
		}
		if(decodeViaStatement2 == null){
			loadStatement();
			if(decodeViaStatement2 == null){
				return null;
			}
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn.prepareStatement(decodeViaStatement2);
			Logger.log().info(
					this.getClass().getName(),
					"ViarioDecoder decodeViaStatement query is \"" + decodeViaStatement2.replace("\n","")
							+ "\"");
			pstmt.setString(1, codiceVia);
			Logger.log()
					.info(
							this.getClass().getName(),
							"ViarioDecoder adding parameter " + codiceVia
									+ " to decodeViaStatement query ");
			rset = pstmt.executeQuery();
			String result = null;
			while (rset.next()) {
				result = rset.getString(1);
			}
			return result;
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} finally {
			try {
				rset.close();
			} catch (Throwable e) {
			}
			try {
				pstmt.close();
			} catch (Throwable e) {
			}
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}

	public String decodeViaAna(it.webred.mui.model.Residenza res) throws SQLException, NamingException {
		if (res.getCivicoUnico()!= null)
			return decodeViaTotale(res);
		else
			return decodeViaInAnagrafe(res.getCodiceVia());
		
	}
		
	public String decodeViaAna(Residenza res) throws SQLException, NamingException {
		if (res.getCivicoUnico()!= null)
			return decodeViaTotale(res);
		else
			return decodeViaInAnagrafe(res.getCodiceVia());
	}
	
	public String decodeViaSIT(CodiceViaCivico cvc) throws SQLException, NamingException {
		if (cvc.getCivicoUnico()!=null)
			return decodeViaTotale(cvc);
			
		String codiceVia = cvc.getCodiceVia();
		
		if(codiceVia == null || codiceVia.trim().length() == 0){
			return null;
		}
		if(decodeViaStatement == null){
			loadStatement();
			if(decodeViaStatement == null){
				return null;
			}
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn.prepareStatement(decodeViaStatement);
			Logger.log().info(
					this.getClass().getName(),
					"ViarioDecoder decodeViaStatement query is \"" + decodeViaStatement.replace("\n","")
							+ "\"");
			pstmt.setString(1, codiceVia);
			Logger.log()
					.info(
							this.getClass().getName(),
							"ViarioDecoder adding parameter " + codiceVia
									+ " to decodeViaStatement query ");
			rset = pstmt.executeQuery();
			String result = null;
			while (rset.next()) {
				result = rset.getString(1);
			}
			return result;
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while decoding via ", e);
			return null;
		} finally {
			try {
				rset.close();
			} catch (Throwable e) {
			}
			try {
				pstmt.close();
			} catch (Throwable e) {
			}
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}

}
