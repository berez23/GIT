package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

public class SuperManager extends SqlAdapter {
	
	public static String getResidenzeStatement = null;
	public static String sqlPossessoriStatementUntilDate = null;
	public static String sqlPossessoriStatementAfterDate = null;
	public static String getResidenzeSenzaStoricoIndirizziStatement = null;
	private Date aDayInThePast = null;
	private Date aDayInTheFuture = null;
	private Date dataInizioAnno = null;

	private Date dataFineAnno = null;

	public SuperManager()
	throws IOException, MuiException {
		
		init();
	}
	
	
	private void init() {
	try {

			aDayInThePast = MuiFornituraParser.dateParser.parse("01011800");
			aDayInTheFuture = MuiFornituraParser.dateParser.parse("01012200");
		} catch (ParseException e1) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating aDayInThePast/aDayInTheFuture", e1);
			throw new MuiException(e1);
		}
		Calendar calBeginOfYear = Calendar.getInstance();
		calBeginOfYear.set(calBeginOfYear.get(Calendar.YEAR), 0, 1);
		dataInizioAnno = calBeginOfYear.getTime();

		Calendar calEndOfYear = Calendar.getInstance();
		calEndOfYear.set(calEndOfYear.get(Calendar.YEAR) + 1, 0, 1);
		dataFineAnno = calEndOfYear.getTime();


		try {
			sqlPossessoriStatementUntilDate = loadResourceInString("/sql/getPossessoriUntilDate.sql");
			sqlPossessoriStatementAfterDate= loadResourceInString("/sql/getPossessoriAfterDate.sql");

			getResidenzeStatement = loadResourceInString("/sql/getResidenzeByCodiceFiscale.sql");
			// getResidenzeStatement =
			// loadResourceInString("/sql/getResidenzeByCodiceFiscaleTest.sql");
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while loadResourceInString ",
					e);
			throw new MuiException(e);
		}

		try {
			getResidenzeSenzaStoricoIndirizziStatement = loadResourceInString("/sql/getResidenzeSenzaStoricoIndirizzi.sql");
			// getResidenzeStatement =
			// loadResourceInString("/sql/getResidenzeByCodiceFiscaleTest.sql");
		} catch (Throwable e) {
			Logger
					.log()
					.error(
							this.getClass().getName(),
							"error while evaluating getResidenzeSenzaStoricoIndirizziStatement statement ",
							e);
			throw new MuiException(e);
		}
		
	}
	
	
	protected int compareNullableValue(Comparable c1, Comparable c2) {
		int res;
		res = (c1 != null ? (c2 != null ? c1.compareTo(c2) : 1)
				: (c2 == null ? 0 : -1));
		return res;
	}
	
	
	protected boolean isbetween(Date a, Date b, Date q) {
		if (q == null) {
			return false;
		} else if (a == null && b == null) {
			return true;
		} else if (a == null) {
			return q.before(b);
		} else if (b == null) {
			return q.after(a);
		} else {
			return q.after(a) && q.before(b);
		}

	}
	
	
	public List<Possesore> getQuote(MiConsDap dap) {
		
		// le quote devono essere del giorno della nota
		String qry = sqlPossessoriStatementUntilDate;

		return getProprietari(dap, qry );
	}
	
	
	public List<Possesore> getPossesores(MiConsDap dap ) {
		String qry;
		// se è il defunto allora do i possessori fino alla data della nota
		// altrimenti do i possessori che non finiscono
		// la loro titolarità il giorno della nota
		if ("C".equals(dap.getMiDupTitolarita().getScFlagTipoTitolNota()))
			qry = sqlPossessoriStatementUntilDate;
		else
			qry= sqlPossessoriStatementAfterDate;
		Logger.log().info(
				this.getClass().getName(),
				"DetrazioneMnagr getPossesores query is \"" + qry.replace("\n", "")
						+ "\"");
		return getProprietari(dap, qry );
	}
	
	private  List<Possesore> getProprietari(MiConsDap dap , String qry) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
			
		List<Possesore> res = new ArrayList<Possesore>();
		if (dap != null) {
			try {
				conn = MuiApplication.getMuiApplication().getConnection();
				pstmt = conn.prepareStatement(qry);
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneMnagr adding parameter (int) "
								+ dap.getFoglio() + " to getPossesores query ");
				int i = 0;
				pstmt.setString(++i, MuiApplication.belfiore);
				if ( dap.getFoglio()== null) {
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
						"DetrazioneMnagr adding parameter (String) "
								+ spNumero + " to getPossesores query ");
				pstmt.setString(++i, spNumero);
				if (dap.getSubalterno() == null) {
					pstmt.setNull(++i, Types.INTEGER);
				} else {
					pstmt.setInt(++i, dap.getSubalterno());
				}
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneMnagr adding parameter (int) "
								+ dap.getSubalterno() + " to getPossesores query ");
				java.sql.Date sqlDapData = new java.sql.Date(dap.getDapData()
						.getTime());

				pstmt.setDate(++i, sqlDapData);
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneMnagr adding parameter (Date) "
								+ sqlDapData + " to getPossesores query ");
				
				int daySwitch = 1;

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
	
	protected ArrayList<Residenza> getResidenzeByCodiceFiscale(String codfisc, Date dataRif) throws SQLException, NamingException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Residenza> residenze = new ArrayList<Residenza>();
		
		boolean hasResult = false;
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn.prepareStatement(getResidenzeStatement);
			Logger.log().info(
					this.getClass().getName(),
					"DetrazioneManager evalResidenze query is \""
							+ getResidenzeStatement.replace("\n", "") + "\"");
			int i = 0;
			String valS;
			
			valS = codfisc;
			
			pstmt.setString(++i, valS);
			pstmt.setString(++i, valS);
			
			long timeDap = dataRif.getTime();


			pstmt.setDate(++i, new java.sql.Date(timeDap));
			pstmt.setDate(++i, new java.sql.Date(timeDap));
			pstmt.setDate(++i, new java.sql.Date(timeDap));
			pstmt.setDate(++i, new java.sql.Date(timeDap));
			Logger.log().info(
					this.getClass().getName(),
					"DetrazioneManager adding parameter " + valS
							+ " to evalResidenze query ");
			rset = pstmt.executeQuery();
			
			
			
			while (rset.next()) { 
				Residenza res = new Residenza();
						
					res.setCodiceVia(rset.getString("COD_VIA"));
					res.setNumeroCivico(rset.getString("NUMERO_CIV"));
					res.setDataDa(rset.getString("DATA_INIZIO"));
					res.setDataA(rset.getString("DATA_FINE"));
					
					residenze.add(res);
					
			}
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating residenze ", e);
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating residenze ", e);
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating resicenze ", e);
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
		
		if (residenze.size() == 0) {
			try {
				conn = MuiApplication.getMuiApplication().getConnection();
				pstmt = conn
						.prepareStatement(getResidenzeSenzaStoricoIndirizziStatement);
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneManager getResidenzeSenzaStoricoIndirizziStatement query is \""
								+ getResidenzeSenzaStoricoIndirizziStatement
										.replace("\n", "") + "\"");
				int i = 0;
				
				pstmt.setString(++i, codfisc);
				Logger
						.log()
						.info(
								this.getClass().getName(),
								"DetrazioneManager adding parameter "
										+ codfisc
										+ " to getResidenzeSenzaStoricoIndirizziStatement query ");
				rset = pstmt.executeQuery();
				while (rset.next()) {
					char posiana = rset.getString("POSIANA").charAt(0);
					Residenza res = null;
					switch (posiana) {
					case 'A':
						res = new Residenza();
						res.setCodiceVia(rset.getString("COD_VIA"));
						res.setNumeroCivico(rset.getString("NUMERO_CIV"));
						res
								.setDataDaDate(rset.getDate("IMMDATA") != null ? rset
										.getDate("IMMDATA")
										: dataInizioAnno);
						res.setDataADate(aDayInTheFuture);
						
						residenze.add(res);
						
						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + codfisc
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						break;

					case 'E':
						res = new Residenza();
						res.setCodiceVia(rset.getString("COD_VIA"));
						res.setNumeroCivico(rset.getString("NUMERO_CIV"));
						res
								.setDataDaDate(rset.getDate("IMMDATA") != null ? rset
										.getDate("IMMDATA")
										: dataInizioAnno);
						res.setDataADate(rset.getDate("EMIDATA") != null ? rset
								.getDate("EMIDATA") : dataFineAnno);

						residenze.add(res);

						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + codfisc
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						break;

					case 'D':
						res = new Residenza();
						res.setCodiceVia(rset.getString("COD_VIA"));
						res.setNumeroCivico(rset.getString("NUMERO_CIV"));
						res.setDataDaDate(rset.getDate("IMMDATA"));
						res.setDataADate(aDayInTheFuture);// ? ? se è morto ha
															// detrazione?
						residenze.add(res);

						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + codfisc
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						break;

					default:
						continue;
					}
				}
			} catch (NumberFormatException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
			} catch (SQLException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
			} catch (NamingException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
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
		return residenze;
		
	}
	
	
	
	
	/**
	 * Prende un insieme di residenze e dice quale ricade nella data di riferimento
	 * @param resds
	 * @param dataRif
	 */
	protected List<Residenza> getResidezaAtTime(Residenza[] resds, Date dataRif) {
		List<Residenza> resAtTime = new ArrayList<Residenza>();	
		int i = 0;
		try {
			for (i = 0; i < resds.length; i++) {
				if(isbetween(resds[i].getDataDaDate(),
						resds[i].getDataADate(), dataRif)){
					Logger.log().info(
							this.getClass().getName(),
							"DetrazioneManager, residenza  "
									+ resds[i]+" per data di riferimento");
					resAtTime.add(resds[i]);
				}
			}
		} catch (Exception e) {
			Logger.log()
			.error(
					this.getClass().getName(),
					"getResidezaAtTime i=" + i,e);
		}
		return resAtTime;
	}
	
	protected List<Residenza> getResidenzaFinoAFineAnno(Residenza[] resds, Date dataRif) {
		List<Residenza> resFinoAFineAnno = new ArrayList<Residenza>();	
		int i = 0;
		try {
			for (i = 0; i < resds.length; i++) {

					SimpleDateFormat  dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
					StringBuilder nowYYYYMMDD = new StringBuilder( dateformatYYYYMMDD.format( dataRif ));
					String year = nowYYYYMMDD.substring(0, 4);
					Date fineAnno = new SimpleDateFormat("ddMMyyyy").parse("3112"+year);

					if(isbetween(dataRif,
							fineAnno, resds[i].getDataDaDate())){
					Logger.log().info(
							this.getClass().getName(),
							"DetrazioneManager, presa residenza entro fine anno  "
									+ resds[i]+ " :" + year);
					resFinoAFineAnno.add(resds[i]);
				}
			}
		} catch (Exception e) {
			Logger.log()
			.error(
					this.getClass().getName(),
					"getResidezaAtTime i=" + i,e);
		}
		return resFinoAFineAnno;
	}

	
}
