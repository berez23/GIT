package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.CodiceViaCivico;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

import org.hibernate.Session;

public class DetrazioneManager extends SqlAdapter {
	MiDupSoggetti soggetto = null;

	private Boolean residenzaOltre90gg = false;
	private Set<MiConsOggetto> miConsOggettos = null;

	private MiConsOggetto oggettoConDetrazione = null;

	private MiConsDap dap = null;

	private Residenza residenzaConDetrazione = null;

	private MiConsComunicazione comunicazione = null;

	private Date dataRiferimento = null;

	private Date dataInizioAnno = null;

	private Date dataFineAnno = null;

	private ComunicazioneConverter conv = new ComunicazioneConverter();

	public static String getResidenzeStatement = null;

	public static String getResidenzeSenzaStoricoIndirizziStatement = null;

	private Date aDayInThePast = null;

	private Date aDayInTheFuture = null;

	public DetrazioneManager(MiDupSoggetti soggetto) throws IOException,
			MuiException {
		this.soggetto = soggetto;
		init();
	}

	public DetrazioneManager(MiDupSoggetti soggetto, MiConsDap dap)
			throws IOException, MuiException {
		this.soggetto = soggetto;
		this.dap = dap;
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
		this.dataRiferimento = soggetto.getMiDupNotaTras()
				.getDataValiditaAttoDate();
		comunicazione = conv.evalComunicazione(soggetto);
		miConsOggettos = comunicazione.getMiConsOggettos();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataRiferimento);
		cal.add(Calendar.DAY_OF_MONTH, 90);
		dataRiferimento = cal.getTime();

		Calendar calBeginOfYear = Calendar.getInstance();
		calBeginOfYear.set(calBeginOfYear.get(Calendar.YEAR), 0, 1);
		dataInizioAnno = calBeginOfYear.getTime();

		Calendar calEndOfYear = Calendar.getInstance();
		calEndOfYear.set(calEndOfYear.get(Calendar.YEAR) + 1, 0, 1);
		dataFineAnno = calEndOfYear.getTime();


		try {
			getResidenzeStatement = loadResourceInString("/sql/getResidenzeByCodiceFiscale.sql");
			// getResidenzeStatement =
			// loadResourceInString("/sql/getResidenzeByCodiceFiscaleTest.sql");
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getResidenzeStatement statement ",
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

	private boolean evalCodiciVia() throws SQLException, NamingException {
		Iterator<MiConsOggetto> itOgs = miConsOggettos.iterator();
		boolean res = false;
		while (itOgs.hasNext()) {
			res = res | evalCodiciOfOggetto(itOgs.next());
		}
		return res;
	}

	private boolean evalCodiciOfOggetto(MiConsOggetto oggetto) {
		String foglio = oggetto.getFoglio();
		String particella = oggetto.getParticella();
		String subalterno = oggetto.getSubalterno();
		CodiceViaCivico[] cvcs = getCodiceViaCivico(foglio, particella, subalterno);
		for (int i = 0; i < cvcs.length; i++) {
			oggetto.addCodiceViaCivico(cvcs[i]);
		}
		if (cvcs.length >0 ) {
			return true;
		} else
			return false;
	}

	public static CodiceViaCivico[] getCodiceViaCivico(String foglio,
			String particella, String subalterno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		CodiceViaCivico cvcs[] = null;
		List<CodiceViaCivico> res = new ArrayList<CodiceViaCivico>();
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			String getCodiciViaStatement = null;
			try {
					getCodiciViaStatement = new SqlAdapter().loadResourceInString("/sql/getCodiceCivicoByFoglioParticellaSupplemento.sql");
				} catch (Throwable e) {
					Logger.log().error("DetrazioneManager","error while evaluating getCodiceCivicoByFoglioParticellaSupplemento statement ",e);
					throw new MuiException(e);
				}				
			
			pstmt = conn.prepareStatement(getCodiciViaStatement);
			Logger.log().info(
					DetrazioneManager.class.getName(),
					"DetrazioneManager query is \""
							+ getCodiciViaStatement.replace("\n", "") + "\"");
			int i = 0;
			String valS;
			
			pstmt.setString(++i, MuiApplication.belfiore);
			int valI;
			// FOGLIO
			if (foglio != null) {
				valI = Integer.valueOf(foglio).intValue();
				Logger.log().info(
						DetrazioneManager.class.getName(),
						"DetrazioneManager adding parameter (int) " + valI
								+ "as FOGLIO to query ");
				pstmt.setInt(++i, valI);
			} else {
				Logger.log().info(DetrazioneManager.class.getName(),
						"DetrazioneManager adding null as FOGLIO to query ");
				pstmt.setNull(++i, Types.NUMERIC);
			}
			// PARTICELLA
			valS = particella;
			while (valS != null && valS.length() < 5) {
				valS = "0" + valS;
			}
			Logger.log().info(
					DetrazioneManager.class.getName(),
					"DetrazioneManager adding parameter as PARTICELLA " + valS
							+ " to query ");
			pstmt.setString(++i, valS);
			// SUBALTERNO
			if (subalterno != null) {
				valI = Integer.valueOf(subalterno).intValue();
				Logger.log().info(
						DetrazioneManager.class.getName(),
						"DetrazioneManager adding parameter (int) " + valI
								+ "as SUBALTERNO to query ");
				pstmt.setInt(++i, valI);
			} else {
				Logger
						.log()
						.info(DetrazioneManager.class.getName(),
								"DetrazioneManager adding null as SUBALTERNO to query ");
				pstmt.setNull(++i, Types.NUMERIC);
			}

			rset = pstmt.executeQuery();
			while (rset.next()) {
				CodiceViaCivico cvc = new CodiceViaCivico();
				cvc.setCodiceVia(rset.getString(2));
				cvc.setCivico(rset.getString(3));
				
				

				// recupero il civico unico per effettuare il successivo match con il civico dell'oggetto
				PreparedStatement pstmtJ = null;
				ResultSet rsetJ = null;
				try {
					pstmtJ = conn.prepareStatement(new SqlAdapter().loadResourceInString("/sql/selectJViarioCiviciCat.sql"));
					pstmtJ.setString(1,rset.getString(4));
					rsetJ = pstmtJ.executeQuery();
					if (rsetJ.next())
						cvc.setCivicoUnico(rsetJ.getString("FK_CIVICO"));
					
					
				} catch (Exception e) {
					cvc.setCivicoUnico(null);
					Logger.log().debug(DetrazioneManager.class.getName(), "ERRORE  - Problemi recuero civico su matrice civici - Se CIVICO UNICO non esiste in piattaforma allora Ignorare il problema",e);
				} finally {
					if (pstmtJ!=null)
						pstmtJ.close();
					if (rsetJ!=null)
						rsetJ.close();						
				}

				res.add(cvc);
			}
		} catch (NullPointerException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (NumberFormatException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (SQLException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (NamingException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
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
		if(cvcs == null){
			cvcs = new CodiceViaCivico[res.size()];
			int idx = 0;
			for (Iterator iterator = res.iterator(); iterator.hasNext(); idx++) {
				CodiceViaCivico element = (CodiceViaCivico) iterator.next();
				cvcs[idx] = element;
			}
		}
		return cvcs;
	}

	private boolean isbetween(Date a, Date b, Date q) {
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

	public boolean evalDetrazione() throws SQLException, NamingException {
		if (soggetto.isGiuridico()) {
			return false;
		}
		String idNota =null;
		try {
			idNota = soggetto.getMiDupNotaTras().getNumeroNotaTras()+"/"+soggetto.getMiDupNotaTras().getAnnoNota();
		} catch (Throwable e) {
			Logger.log()
			.error(
					this.getClass().getName(),
					"DetrazioneManager, nota "+idNota+",  unable to detect idNota",e);
		}
		evalCodiciVia();
		evalResidenze();
		Logger.log()
				.info(
						this.getClass().getName(),
						"DetrazioneManager, nota "+idNota+",  dataRiferimento ="
								+ MuiFornituraParser.dateParser
										.format(dataRiferimento));
		boolean found = false;
		boolean res = false;
		List<Residenza> resAtTime = new ArrayList<Residenza>();
		Residenza[] resds = soggetto.getResidenzas();

		if (resds != null && resds.length > 0) {
			for (int i = 0; i < resds.length; i++) {
				Logger.log().info(this.getClass().getName(),
						"DetrazioneManager, nota "+idNota+",  checking for " + resds[i]);
				if(isbetween(resds[i].getDataDaDate(),
						resds[i].getDataADate(), dataRiferimento)){
					Logger.log().info(
							this.getClass().getName(),
							"DetrazioneManager, nota "+idNota+",  residenza  "
									+ resds[i]+" per data di riferimento");
					resAtTime.add(resds[i]);
				}
			}
			Logger.log().info(this.getClass().getName(),
					"DetrazioneManager, gli oggetti sono "+miConsOggettos.size() );
			Iterator<MiConsOggetto> itOgs = miConsOggettos.iterator();
			while (itOgs.hasNext()) {
				MiConsOggetto ogg = itOgs.next();
				for (Iterator<Residenza> iter = resAtTime.iterator(); iter.hasNext();) {
					Residenza element = iter.next();
					if(element.matchOggetto(ogg) && ogg.isAbitativo()){ // solo gli abitativi hanno diritto a detrazione 24-9-2008 : ma prima non si era accorto mai nessuno che dava le detrazioni per gli oggetti non abitativi?
						setOggettoConDetrazione(ogg);
						setResidenzaConDetrazione(element);
						ogg.setFlagAbitazionePrincipale(true);
						res = true;
						Logger.log().info(
								this.getClass().getName(),
								"DetrazioneManager, nota "+idNota+",   HA Detrazione per l'oggetto: "
										+ ogg.getIid());
					}
					else{
						ogg.setFlagAbitazionePrincipale(false);
					}
				}
			}
			if (!res && dap != null) {
				for (int idx = 0; idx < resds.length; idx++) {
					if (resds[idx].getDataDaDate() != null
							&& resds[idx].getDataDaDate()
									.after(dataRiferimento)) {
						itOgs = miConsOggettos.iterator();
						while (itOgs.hasNext()) {
							MiConsOggetto ogg = itOgs.next();
							if (resds[idx].matchOggetto(ogg) && ogg.isAbitativo()) { // solo gli abitativi hanno diritto a detrazione 24-9-2008 : ma prima non si era accorto mai nessuno che dava le detrazioni per gli oggetti non abitativi?
								setOggettoConDetrazione(ogg);
								setResidenzaConDetrazione(resds[idx]);
								setResidenzaOltre90gg(true);
								ogg.setFlagAbitazionePrincipale(true);
								res = true;
								dap.setFlagRegoleDapDataResOltre90Giorni(true);
								Logger.log().info(
										this.getClass().getName(),
										"DetrazioneManager, nota "+idNota+",  Residenza indicata presa dopo dap data!!! "
												+ ogg.getIid());
								Logger.log().info(
										this.getClass().getName(),
										"DetrazioneManager, nota "+idNota+", ha diritto di detrazione (ma con residenza presa dopo 90 gg) per l'oggetto: "
												+ ogg.getIid()+ " nota="+ogg.getMiDupTitolarita().getMiDupNotaTras().getIid());
							}
						}
					}
				}
			}
			if(getOggettoConDetrazione() == null){
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneManager, nota "+idNota+", : Per nessuno oggetto della comunicazione ha diritto di detrazione ");					
			}
			Session session = HibernateUtil.currentSession();
			// Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(comunicazione);
				Logger.log().info(this.getClass().getName(),
						"comunicazione saved " + comunicazione.getIid());
				itOgs = miConsOggettos.iterator();
				while (itOgs.hasNext()) {
					MiConsOggetto ogg = itOgs.next();
					session.saveOrUpdate(ogg);
					Logger.log().info(this.getClass().getName(),
							"oggetto saved " + ogg.getIid());
				}
				// tx.commit();
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(),
						"error while detrazione form comunicazione", e);
				// try {
				// tx.rollback();
				// } catch (HibernateException e1) {
				// e1.printStackTrace();
				// }
				throw new MuiException(
						"error while detrazione form comunicazione", e);
			} finally {
				// HibernateUtil.closeSession();
			}
		}
		return res;
	}

	private boolean evalResidenze() throws SQLException, NamingException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
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
			valS = "G".equals(soggetto.getTipoSoggetto()) ? soggetto
					.getCodiceFiscaleG() : soggetto.getCodiceFiscale();
			pstmt.setString(++i, valS);
			pstmt.setString(++i, valS);
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

				// recupero il civico unico per effettuare il successivo match con il civico dell'oggetto
				PreparedStatement pstmtJ = null;
				ResultSet rsetJ = null;
				try {
					pstmtJ = conn.prepareStatement(new SqlAdapter().loadResourceInString("/sql/selectJViarioCiviciAna.sql"));
					pstmtJ.setString(1,rset.getString("id_ext_civico"));
					rsetJ = pstmtJ.executeQuery();
					if (rsetJ.next())
						res.setCivicoUnico(rsetJ.getString("FK_CIVICO"));
					
					
				} catch (Exception e) {
					res.setCivicoUnico(null);
					Logger.log().error(this.getClass().getName(), "Problemi recuero civico su matrice civici",e);
				} finally {
					if (pstmtJ!=null)
						pstmtJ.close();
					if (rsetJ!=null)
						rsetJ.close();						
				}
				
				
				soggetto.addResidenza(res);
				hasResult = true;
			}
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating detrazione ", e);
			return false;
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating detrazione ", e);
			return false;
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating detrazione ", e);
			return false;
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
		if (soggetto.getResidenzas().length == 0) {
			if (dap != null)
				dap.setFlagRegoleDapNoDataResidenza(true);
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
				String valS;
				valS = "G".equals(soggetto.getTipoSoggetto()) ? soggetto
						.getCodiceFiscaleG() : soggetto.getCodiceFiscale();
				pstmt.setString(++i, valS);
				Logger
						.log()
						.info(
								this.getClass().getName(),
								"DetrazioneManager adding parameter "
										+ valS
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
						soggetto.addResidenza(res);
						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + valS
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						hasResult = true;
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
						soggetto.addResidenza(res);
						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + valS
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						hasResult = true;
						break;

					case 'D':
						res = new Residenza();
						res.setCodiceVia(rset.getString("COD_VIA"));
						res.setNumeroCivico(rset.getString("NUMERO_CIV"));
						res.setDataDaDate(rset.getDate("IMMDATA"));
						res.setDataADate(aDayInTheFuture);// ? ? se è morto ha
															// detrazione?
						soggetto.addResidenza(res);
						Logger.log().info(
								this.getClass().getName(),
								"residenza trovata da anagrafica per " + valS
										+ "(posiana=" + posiana + "): "
										+ res.getCodiceVia() + " "
										+ res.getNumeroCivico() + " dal "
										+ res.getDataDa() + " al "
										+ res.getDataA());
						hasResult = true;
						break;

					default:
						continue;
					}
				}
			} catch (NumberFormatException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
				return false;
			} catch (SQLException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
				return false;
			} catch (NamingException e) {
				Logger.log().error(this.getClass().getName(),
						"error while evaluating detrazione ", e);
				return false;
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
		return hasResult;
	}

	public MiConsComunicazione getComunicazione() {
		return comunicazione;
	}

	public Date getDataRiferimento() {
		return dataRiferimento;
	}

	public Set<MiConsOggetto> getMiConsOggettos() {
		return miConsOggettos;
	}

	public MiConsOggetto getOggettoConDetrazione() {
		return oggettoConDetrazione;
	}

	private void setOggettoConDetrazione(MiConsOggetto oggettoConDetrazione) {
		this.oggettoConDetrazione = oggettoConDetrazione;
	}

	public Residenza getResidenzaConDetrazione() {
		return residenzaConDetrazione;
	}

	private void setResidenzaConDetrazione(Residenza residenzaConDetrazione) {
		this.residenzaConDetrazione = residenzaConDetrazione;
	}

	public Boolean isResidenzaOltre90gg() {
		return residenzaOltre90gg;
	}

	private void setResidenzaOltre90gg(boolean residenzaOltre90gg) {
		this.residenzaOltre90gg = residenzaOltre90gg;
	}
	public static Boolean checkSoggettoInAnagrafe(MiDupSoggetti soggetto){
		return  checkSoggettoInAnagrafe("G".equals(soggetto.getTipoSoggetto()) ? soggetto
				.getCodiceFiscaleG() : soggetto.getCodiceFiscale());
	}
	public static boolean checkSoggettoInAnagrafe(String codiceFiscale){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn
					.prepareStatement(getResidenzeSenzaStoricoIndirizziStatement);
			Logger.log().info(DetrazioneManager.class.getName(),
					"DetrazioneManager getResidenzeSenzaStoricoIndirizziStatement query is \""
							+ getResidenzeSenzaStoricoIndirizziStatement
									.replace("\n", "") + "\"");
			int i = 0;
			String valS;
			valS = codiceFiscale;
			pstmt.setString(++i, valS);
			Logger
					.log()
					.info(
							DetrazioneManager.class.getName(),
							"DetrazioneManager adding parameter "
									+ valS
									+ " to getResidenzeSenzaStoricoIndirizziStatement query ");
			rset = pstmt.executeQuery();
			return rset.next();		
		} catch (SQLException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			return false;
		} catch (NamingException e) {
			Logger.log().error(DetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			return false;
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
