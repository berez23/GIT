package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.Coniuge;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.Immobile;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;


import net.skillbill.logging.Logger;

public class DapEvaluatorImpl extends SuperManager implements DapEvaluator {

	private static String sqlStatementAfter2006 = null;
	private static String sqlStatementBefore2006 = null;
	private static String sqlPossedutiImmobiliStatement = null;
	private static String sqlGetConiugeAfter2006 = null; 
	private static String sqlGetConiugeBefore2006 = null; 
	
	private Date switchOffDate = null;

	private static Object stick = new Object();

	public DapEvaluatorImpl() throws IOException, ParseException {
		//switchOffDate = MuiFornituraParser.dateParser.parse("01012006");
		// 13/10/2007 - Modified by MaX sotto indicazione di Paolo
		super();
		switchOffDate = MuiFornituraParser.dateParser.parse("30042006");
		if (sqlStatementAfter2006 == null) {
			synchronized (stick) {
				if (sqlStatementAfter2006 == null) {
					try {
						sqlStatementAfter2006 = loadResourceInString("/sql/getFamiliariAfter2006.sql");
						sqlStatementBefore2006 = loadResourceInString("/sql/getFamiliariBefore2006.sql");
						sqlPossedutiImmobiliStatement = loadResourceInString("/sql/getImmobiliPosseduti.sql");
						sqlGetConiugeAfter2006 = loadResourceInString("/sql/getConiugeAfter2006.sql");
						sqlGetConiugeBefore2006 = loadResourceInString("/sql/getConiugeBefore2006.sql");
						// getCodiciViaStatement =
						// loadResourceInString("/sql/getCodiceCivicoByFoglioParticellaSupplementoTest.sql");
					} catch (Throwable e) {
						Logger.log().error(this.getClass().getName(),
								"error while evaluating sql statements ", e);
						throw new MuiException(e);
					}
				}
			}
		}
	}

	private List<Familiare> executeSqlForFamiliares(MiConsDap dap, String qry, boolean afterSwitchOffDate) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Familiare> res = new ArrayList<Familiare>();
		
		try {
				conn = MuiApplication.getMuiApplication().getConnection();
		
				pstmt = conn.prepareStatement(qry);
				// Added By MAX - 28/09/2007- Aggiunti IF sul afterSwitchOffDate in quanto i placeholder 
				// delle due query getFamiliariAfter2006.sql e getFamiliariBefore2006.sql non hanno la stessa
				// posizione
				java.sql.Date sqlDapData = new java.sql.Date(dap.getDapData().getTime());
				if (afterSwitchOffDate) {
					Logger.log().info(this.getClass().getName(), "Query getFamiliariAfter2006.sql");
					pstmt.setString(1, dap.getCodiceFiscale());
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (String) " + dap.getCodiceFiscale() + " to getFamiliares query ");
					pstmt.setDate(2, sqlDapData);
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (Date) " + sqlDapData + " to getFamiliares query ");
					pstmt.setDate(3, sqlDapData);
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (Date) " + sqlDapData + " to getFamiliares query ");
				} else {
					Logger.log().info(this.getClass().getName(), "Query getFamiliariBefore2006.sql");
					pstmt.setDate(1, sqlDapData);
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (Date) " + sqlDapData + " to getFamiliares query ");				
					pstmt.setString(2, dap.getCodiceFiscale());
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (String) " + dap.getCodiceFiscale() + " to getFamiliares query ");
					pstmt.setDate(3, sqlDapData);
					Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl adding parameter (Date) " + sqlDapData + " to getFamiliares query ");
				}
				
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
		}  catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		

		finally {
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
	
	public List<Familiare> getFamiliares(MiConsDap dap) {
		List<Familiare> res = new ArrayList<Familiare>();
		boolean afterSwitchOffDate = dap.getDapData().after(switchOffDate);
		try {
			String qry = afterSwitchOffDate ? sqlStatementAfter2006
					: sqlStatementBefore2006;
			Logger.log().info(
					this.getClass().getName(),
					"DapEvaluatorImpl getFamiliares query is \"" + qry.replace("\n", "")
							+ "\"");
			res = executeSqlForFamiliares(dap, qry, afterSwitchOffDate);
			
			// se l'ho cercato prim del 2006 e non l'ho trovato allora cerco nell'archivio post 2006 se c'è una famiglia valida rispetto alla mia data 
			if (res.isEmpty() && !afterSwitchOffDate) {
				qry = !afterSwitchOffDate ? sqlStatementAfter2006
						: sqlStatementBefore2006;
				res = executeSqlForFamiliares(dap, qry, !afterSwitchOffDate);
			}
			
			return res;
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating Familiares ", e);
			throw new RuntimeException(e);
		}
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

	/*
	public Residenza getResidenzaByCodiceFiscale(String codFisc, MiConsDap dap) {
		PreparedStatement pstmtGetRes = null;
		ResultSet rsetGetRes = null;
		Connection conn = null;
		Residenza residenza = null;

		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String getResidenzeStatement = loadResourceInString("/sql/getResidenzeByCodiceFiscale.sql");
			pstmtGetRes = conn.prepareStatement(getResidenzeStatement);
			Logger.log().info(this.getClass().getName(), "Param[1]: " + codFisc);
			pstmtGetRes.setString(1, codFisc);
			pstmtGetRes.setString(2, codFisc);
			long timeDap = dap.getDapData().getTime();
			pstmtGetRes.setDate(3, new java.sql.Date(timeDap));
			pstmtGetRes.setDate(4, new java.sql.Date(timeDap));
			pstmtGetRes.setDate(5, new java.sql.Date(timeDap));
			pstmtGetRes.setDate(6, new java.sql.Date(timeDap));

			rsetGetRes = pstmtGetRes.executeQuery();
			while (rsetGetRes.next()) {
				residenza = new Residenza();
				residenza.setCodiceVia(rsetGetRes.getString("COD_VIA"));
				residenza.setNumeroCivico(rsetGetRes.getString("NUMERO_CIV"));
				residenza.setDataDa(rsetGetRes.getString("DATA_INIZIO"));
				residenza.setDataA(rsetGetRes.getString("DATA_FINE"));

				Logger.log().info(this.getClass().getName(), "Residenza Coniuge found");
			}
			return residenza;
			
		}
		catch (Exception e) {
			Logger.log().error(this.getClass().getName(), "Errore nel recupero residenza ", e);
			throw new RuntimeException(e);
		} finally {
		try {
			rsetGetRes.close();
		} catch (Throwable e) {
		}
		try {
			pstmtGetRes.close();
		} catch (Throwable e) {
		}
		try {
			conn.close();
		} catch (Throwable e) {
		}
	}
			
	}
*/
	
	/**
	 * Data una una MiConsDap ritorna il coniuge recuperandolo da tabelle diverse a seconda che la data sia precedente o meno 
	 * al 30/04/2006
	 */
	public Coniuge getConiugeInVita(MiConsDap dap) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rset = null;
		ResultSet rset2 = null;
		List<Familiare> res = new ArrayList<Familiare>();
		boolean afterSwitchOffDate = dap.getDapData().after(switchOffDate);
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String qry = afterSwitchOffDate ? sqlGetConiugeAfter2006 : sqlGetConiugeBefore2006;
			Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl getConiuge query is \"" + qry.replace("\n", "") + "\"");
			pstmt = conn.prepareStatement(qry);
			Logger.log().info(this.getClass().getName(), "Param[1]: " + dap.getCodiceFiscale());
			pstmt.setString(1, dap.getCodiceFiscale());
			if (afterSwitchOffDate)
				pstmt.setTimestamp(2, new Timestamp(switchOffDate.getTime()));
				

			rset = pstmt.executeQuery();
			Coniuge coniuge = null;
			String codFiscConiuge = null;
			while (rset.next()) {
				coniuge = new Coniuge();
				codFiscConiuge = rset.getString("CODICE_FISCALE");
				coniuge.setCodiceFiscale(codFiscConiuge);
				//coniuge.setIndirizzo(rset.getString("INDIRIZZO"));
				Logger.log().info(this.getClass().getName(), "Coniuge found: " + codFiscConiuge);
			}
			//return coniuge;
/*
			if (coniuge == null) {
				Logger.log().info(this.getClass().getName(), "Coniuge non trovato con la prima query, provo con l'altra");
				qry = afterSwitchOffDate ? sqlGetConiugeBefore2006 : sqlGetConiugeAfter2006;
				Logger.log().info(this.getClass().getName(), "DapEvaluatorImpl getConiuge query is \"" + qry.replace("\n", "") + "\"");
				pstmt2 = conn.prepareStatement(qry);
				Logger.log().info(this.getClass().getName(), "Param[1]: " + dap.getCodiceFiscale());
				pstmt2.setString(1, dap.getCodiceFiscale());

				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					coniuge = new Coniuge();
					codFiscConiuge = rset2.getString("CODICE_FISCALE");
					coniuge.setCodiceFiscale(codFiscConiuge);
					//coniuge.setIndirizzo(rset2.getString("INDIRIZZO"));
					Logger.log().info(this.getClass().getName(), "Coniuge found: " + codFiscConiuge);
				}				
			}
	*/

			// leggo la residenza del coniuge alla data della dap
			if (coniuge != null) { 
				ArrayList<Residenza> residenze = getResidenzeByCodiceFiscale(codFiscConiuge, dap.getDapData());
				Residenza[] residenzeArray = residenzeArray(residenze);
				List<Residenza> residenza =  getResidezaAtTime(residenzeArray, dap.getDapData());  //  getResidenzaByCodiceFiscale(codFiscConiuge, dap);
				int i=0;
				for (Iterator<Residenza> iter = residenza.iterator(); iter.hasNext();) {
					coniuge.setResidenza(iter.next());
					i++;
					if (i>1)
						Logger.log().info(this.getClass().getName(),"Coniuge" + coniuge.getCodiceFiscale() + " + di una residenza valida alla data " + dap.getDapData()); 
						
				}
			}
			return coniuge;
		
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), "Errore nel recupero del coniuge ", e);
			throw new RuntimeException(e);
		} finally {
			try {
				rset.close();
				rset2.close();
			} catch (Throwable e) {
			}
			try {
				pstmt.close();
				pstmt2.close();
			} catch (Throwable e) {
			}
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}





}
