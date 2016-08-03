package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.Immobile;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.Possesore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

public class DapEvaluatorImpl extends SqlAdapter implements DapEvaluator {

	private static String sqlStatementAfter2006 = null;

	private static String sqlStatementBefore2006 = null;

	private static String sqlPossessoriStatement = null;

	private static String sqlPossedutiImmobiliStatement = null;

	private Date switchOffDate = null;

	private static Object stick = new Object();

	public DapEvaluatorImpl() throws ParseException {
		switchOffDate = MuiFornituraParser.dateParser.parse("01012006");
		if (sqlStatementAfter2006 == null) {
			synchronized (stick) {
				if (sqlStatementAfter2006 == null) {
					try {
						sqlStatementAfter2006 = loadResourceInString("/sql/getFamiliariAfter2006.sql");
						sqlStatementBefore2006 = loadResourceInString("/sql/getFamiliariBefore2006.sql");
						sqlPossessoriStatement = loadResourceInString("/sql/getPossessori.sql");
						sqlPossedutiImmobiliStatement = loadResourceInString("/sql/getImmobiliPosseduti.sql");
					} catch (Throwable e) {
						Logger.log().error(this.getClass().getName(),
								"error while evaluating sql statements ", e);
						throw new MuiException(e);
					}
				}
			}
		}
	}

	public List<Familiare> getFamiliares(MiConsDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Familiare> res = new ArrayList<Familiare>();
		boolean afterSwitchOffDate = dap.getDapData().after(switchOffDate);
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String qry = afterSwitchOffDate ? sqlStatementAfter2006
					: sqlStatementBefore2006;
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl getFamiliares query is \"" + qry.replace("\n", "")
							+ "\"");
			pstmt = conn.prepareStatement(qry);
			java.sql.Date sqlDapData = new java.sql.Date(dap.getDapData()
					.getTime());

			pstmt.setDate(1, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl adding parameter (Date) " + sqlDapData
							+ " to getFamiliares query ");			
			
			pstmt.setString(2, dap.getCodiceFiscale());
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl adding parameter (String) "
							+ dap.getCodiceFiscale() + " to getFamiliares query ");

			pstmt.setDate(3, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl adding parameter (Date) " + sqlDapData
							+ " to getFamiliares query ");
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Familiare f = new Familiare();
				f.setCodiceFiscale(rset.getString("CODICE_FISCALE"));
				res.add(f);
				Logger.log().info(
						this.getClass().getName(),
						"familiare found: "+f.getCodiceFiscale());
			}
			return res;
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
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

	public List<Possesore> getPossesores(MiConsDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Possesore> res = new ArrayList<Possesore>();
		if (dap != null) {
			try {
				conn = MuiApplication.getMuiApplication().getConnection();
				String qry = sqlPossessoriStatement;
				Logger.log().info(
						this.getClass().getName(),
						"DapEvaluatorImpl getPossesores query is \"" + qry.replace("\n", "")
								+ "\"");
				pstmt = conn.prepareStatement(qry);
				Logger.log().info(
						this.getClass().getName(),
						"DapEvaluatorImpl adding parameter (int) "
								+ dap.getFoglio() + " to getPossesores query ");
				int i = 0;
				pstmt.setString(++i, MuiApplication.belfiore);
				if (dap.getFoglio() == null) {
					pstmt.setNull(++i, Types.INTEGER);
				} else {
					pstmt.setInt(++i, dap.getFoglio());
				}
				String spNumero = (dap.getNumero()!= null?dap.getNumero():"");
				while(spNumero.length()<5){
					spNumero = "0"+spNumero;
				}
				Logger.log().info(
						this.getClass().getName(),
						"DapEvaluatorImpl adding parameter (String) "
								+ spNumero + " to getPossesores query ");
				pstmt.setString(++i, spNumero);
				if (dap.getSubalterno() == null) {
					pstmt.setNull(++i, Types.INTEGER);
				} else {
					pstmt.setInt(++i, dap.getSubalterno());
				}
				Logger.log().info(
						this.getClass().getName(),
						"DapEvaluatorImpl adding parameter (int) "
								+ dap.getSubalterno() + " to getPossesores query ");
				java.sql.Date sqlDapData = new java.sql.Date(dap.getDapData()
						.getTime());

				pstmt.setDate(++i, sqlDapData);
				Logger.log().info(
						this.getClass().getName(),
						"DapEvaluatorImpl adding parameter (Date) "
								+ sqlDapData + " to getPossesores query ");
				rset = pstmt.executeQuery();
				while (rset.next()) {
					Possesore p = new Possesore();
					p.setCodiceFiscale(rset.getString("CODICE_FISCALE"));
					p.setPercentualePossesso(rset.getBigDecimal("PERC_POSS"));
					res.add(p);
					Logger.log().info(
							this.getClass().getName(),
							"possessore found: "+p.getCodiceFiscale()+ " PercentualePossesso: "+p.getPercentualePossesso());
				}
				return res;
			} catch (NumberFormatException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating Familiares ", e);
				throw new RuntimeException(e);
			} catch (SQLException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating Familiares ", e);
				throw new RuntimeException(e);
			} catch (NamingException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating Familiares ", e);
				throw new RuntimeException(e);
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
		return res;
	}

	public MultiPossessoType getPossedutoImmobiles(MiConsDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Set<Immobile> res = new HashSet<Immobile>();
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String qry = sqlPossedutiImmobiliStatement;
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl query is \"" + qry.replace("\n", "").replace("\r", "")
							+ "\"");
			pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, dap.getCodiceFiscale());
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl adding parameter (String) "
							+ dap.getCodiceFiscale() + " to query ");
			java.sql.Date sqlDapData = new java.sql.Date(dap.getDapData()
					.getTime());

			pstmt.setDate(2, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl adding parameter (Date) " + sqlDapData
							+ " to query ");
			rset = pstmt.executeQuery();
			int count = 0;
			while (rset.next()) {
				Immobile p = new Immobile();
				p.setFoglio(rset.getInt("FOGLIO"));
				p.setNumero(rset.getString("PARTICELLA"));
				p.setSubalterno(rset.getInt("UNIMM"));
				p.setAllegato(rset.getString("ALLEGATO"));
				res.add(p);
				count++;
			}
			if (count <= 1) {
				return MultiPossessoType.SINGLE;
			} else if (res.size() == count) {
				return MultiPossessoType.SEVERAL;
			} else {
				return MultiPossessoType.REPEATED;
			}
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating PossedutoImmobiles ", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating PossedutoImmobiles ", e);
			throw new RuntimeException(e);
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating PossedutoImmobiles ", e);
			throw new RuntimeException(e);
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
