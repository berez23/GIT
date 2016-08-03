package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.Immobile;
import it.webred.docfa.model.DocfaDap;
import it.webred.mui.model.Possesore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

public class DocfaDapEvaluatorImpl extends SqlAdapter implements DocfaDapEvaluator {

	private static String sqlStatementAfter2006 = null;

	private static String sqlPossessoriStatement = null;

	private static String sqlPossedutiImmobiliStatement = null;

	private static Object stick = new Object();
		
	private	SimpleDateFormat dP1 = new SimpleDateFormat("dd/MM/yyyy");

	public DocfaDapEvaluatorImpl() throws ParseException {
		if (sqlStatementAfter2006 == null) {
			synchronized (stick) {
				if (sqlStatementAfter2006 == null) {
					try {
						sqlStatementAfter2006 = loadResourceInString("/sql/getFamiliariAfter2006.sql");
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
  
	public List<Familiare> getFamiliares(DocfaDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Familiare> res = new ArrayList<Familiare>();
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String qry = sqlStatementAfter2006;
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl getFamiliares query is \"" + qry.replace("\n", "")
							+ "\"");
			pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, dap.getIdSoggetto());
			Logger.log().info(this.getClass().getName(),"DocfaDapEvaluatorImpl adding parameter (String) "
							+ dap.getIdSoggetto() + " to getFamiliares query ");
			java.sql.Date sqlDapData = new java.sql.Date(dP1.parse(dap.getDapData()).getTime());

			pstmt.setDate(2, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl adding parameter (Date) " + sqlDapData+ " to getFamiliares query ");
			pstmt.setDate(3, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl adding parameter (Date) " + sqlDapData+ " to getFamiliares query ");
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Familiare f = new Familiare();
				f.setCodiceFiscale(rset.getString("CODICE_FISCALE"));
				res.add(f);
				Logger.log().info(this.getClass().getName(),"familiare found: "+f.getCodiceFiscale());
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
		} catch (Exception e) {
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

	public List<Possesore> getPossesores(DocfaDap dap) {
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
						"DocfaDapEvaluatorImpl getPossesores query is \"" + qry.replace("\n", "")
								+ "\"");
				pstmt = conn.prepareStatement(qry);
				Logger.log().info(
						this.getClass().getName(),
						"DocfaDapEvaluatorImpl adding parameter (int) "
								+ dap.getFoglio() + " to getPossesores query ");
				int i = 0;
				pstmt.setString(++i, MuiApplication.belfiore);
				if (dap.getFoglio() == null) {
					pstmt.setNull(++i, Types.INTEGER);
				} else {
					pstmt.setInt(++i, Integer.parseInt(dap.getFoglio()));
				}
				String spNumero = (dap.getParticella()!= null?dap.getParticella():"");
				while(spNumero.length()<5){
					spNumero = "0"+spNumero;
				}
				Logger.log().info(
						this.getClass().getName(),
						"DocfaDapEvaluatorImpl adding parameter (String) "
								+ spNumero + " to getPossesores query ");
				pstmt.setString(++i, spNumero);
				if (dap.getSubalterno() == null) {
					pstmt.setNull(++i, Types.INTEGER);
				} else {
					pstmt.setInt(++i, Integer.parseInt(dap.getSubalterno()));
				}
				Logger.log().info(
						this.getClass().getName(),
						"DocfaDapEvaluatorImpl adding parameter (int) "
								+ dap.getSubalterno() + " to getPossesores query ");
				java.sql.Date sqlDapData = new java.sql.Date(dP1.parse(dap.getDapData()).getTime());

				pstmt.setDate(++i, sqlDapData);
				Logger.log().info(
						this.getClass().getName(),
						"DocfaDapEvaluatorImpl adding parameter (Date) "
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
			} catch (Exception e) {
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

	public MultiPossessoType getPossedutoImmobiles(DocfaDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Set<Immobile> res = new HashSet<Immobile>();
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String qry = sqlPossedutiImmobiliStatement;
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl query is \"" + qry.replace("\n", "").replace("\r", "")
							+ "\"");
			pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, dap.getIdSoggetto());
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl adding parameter (String) "
							+ dap.getIdSoggetto() + " to query ");
			java.sql.Date sqlDapData = new java.sql.Date(dP1.parse(dap.getDapData()).getTime());

			pstmt.setDate(2, sqlDapData);
			Logger.log().info(
					this.getClass().getName(),
					"DocfaDapEvaluatorImpl adding parameter (Date) " + sqlDapData
							+ " to query ");
			rset = pstmt.executeQuery();
			int count = 0;
			while (rset.next()) {
				Immobile p = new Immobile();
				p.setFoglio(rset.getInt("FOGLIO"));
				p.setNumero(rset.getString("PARTICELLA"));
				p.setSubalterno(rset.getInt("UNIMM"));
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
		}catch (Exception e) {
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
