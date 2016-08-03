package it.webred.mui.consolidation;

import it.webred.mui.http.MuiApplication;

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
	/**
	 * @param args
	 */
	public ViarioDecoder()  {
		loadStatement();
	}

	private void loadStatement() {
		try {
			decodeViaStatement = loadResourceInString("/sql/decodeViaStatement.sql");
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getCodiciViaStatement statement ",
					e);
			decodeViaStatement = null;
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

	public String decodeVia(String codiceVia) throws SQLException, NamingException {
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
