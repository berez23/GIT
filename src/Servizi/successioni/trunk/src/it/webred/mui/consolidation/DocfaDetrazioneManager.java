package it.webred.mui.consolidation;

import it.webred.docfa.model.DocfaComunOggetto;
import it.webred.docfa.model.DocfaComunicazione;
import it.webred.docfa.model.Residenza;
import it.webred.docfa.model.UseDocfaComunOggetto;
import it.webred.docfa.model.UseDocfaComunicazione;
import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.CodiceViaCivico;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;

public class DocfaDetrazioneManager extends SqlAdapter {
	
	private String soggetto = null;
	private String dataRiferimento = null;
	
	private String protocollo = null;
	private String fornitura = null;

	private Boolean residenzaOltre90gg = false;
	
	private DocfaComunOggetto oggettoConDetrazione = null;
	
	private Set<DocfaComunOggetto> consOggettos = null;

	//private MiConsDap dap = null;

	private Residenza residenzaConDetrazione = null;

	private DocfaComunicazione comunicazione = null;

	private Date dataInizioAnno = null;

	private Date dataFineAnno = null;
	private Date dataRiferimentoD = null;

	public static String getResidenzeStatement = null;

	public static String getResidenzeSenzaStoricoIndirizziStatement = null;

	private Date aDayInThePast = null;

	private Date aDayInTheFuture = null;
	
	private SimpleDateFormat dateParser = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat dateParserF = new SimpleDateFormat("dd/MM/yyyy");

	public DocfaDetrazioneManager(String soggetto,String dataRif,String protocollo,String fornitura) throws IOException,
			MuiException {
		this.soggetto = soggetto;
		this.dataRiferimento = dataRif;
		this.protocollo=protocollo;
		this.fornitura = fornitura;
		init();
	}

	private void init() {
		try {
			aDayInThePast = dateParser.parse("01011800");
			aDayInTheFuture = dateParser.parse("01012200");
		
			Calendar cal = Calendar.getInstance();
			//cal.setTime(df.parse(dataRiferimento));
			cal.setTime(dateParserF.parse(dataRiferimento));
			cal.add(Calendar.DAY_OF_MONTH, 90);
			dataRiferimento = cal.getTime().toString();
			dataRiferimentoD = cal.getTime();

		} catch (ParseException e1) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating aDayInThePast/aDayInTheFuture", e1);
			throw new MuiException(e1);
		}
		
		comunicazione = evalComunicazione(soggetto,protocollo,fornitura);
		consOggettos = comunicazione.getDocfaComunOggettos();
		
		Calendar calBeginOfYear = Calendar.getInstance();
		calBeginOfYear.set(calBeginOfYear.get(Calendar.YEAR), 0, 1);
		dataInizioAnno = calBeginOfYear.getTime();

		Calendar calEndOfYear = Calendar.getInstance();
		calEndOfYear.set(calEndOfYear.get(Calendar.YEAR) + 1, 0, 1);
		dataFineAnno = calEndOfYear.getTime();

		
		try {
			getResidenzeStatement = loadResourceInString("/sql/getResidenzeByCodiceFiscale.sql");
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getResidenzeStatement statement ",
					e);
			throw new MuiException(e);
		}

		
		try {
			getResidenzeSenzaStoricoIndirizziStatement = loadResourceInString("/sql/getResidenzeSenzaStoricoIndirizzi.sql");
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
		Iterator<DocfaComunOggetto> itOgs = consOggettos.iterator();
		boolean res = false;
		while (itOgs.hasNext()) {
			res = res | evalCodiciOfOggetto(itOgs.next());
		}
		return res;
	}

	private boolean evalCodiciOfOggetto(DocfaComunOggetto oggetto) {
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
			String getCodiciViaStatement =  null;
			try {
				getCodiciViaStatement = new SqlAdapter().loadResourceInString("/sql/getCodiceCivicoByFoglioParticellaSupplemento.sql");
			} catch (Throwable e) {
				Logger.log().error("DetrazioneManager","error while evaluating getCodiceCivicoByFoglioParticellaSupplemento statement ",e);
				throw new MuiException(e);
			}	
			pstmt = conn.prepareStatement(getCodiciViaStatement);
			Logger.log().info(
					DocfaDetrazioneManager.class.getName(),
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
						DocfaDetrazioneManager.class.getName(),
						"DetrazioneManager adding parameter (int) " + valI
								+ "as FOGLIO to query ");
				pstmt.setInt(++i, valI);
			} else {
				Logger.log().info(DocfaDetrazioneManager.class.getName(),
						"DetrazioneManager adding null as FOGLIO to query ");
				pstmt.setNull(++i, Types.NUMERIC);
			}
			// PARTICELLA
			valS = particella;
			while (valS != null && valS.length() < 5) {
				valS = "0" + valS;
			}
			Logger.log().info(
					DocfaDetrazioneManager.class.getName(),
					"DetrazioneManager adding parameter as PARTICELLA " + valS
							+ " to query ");
			pstmt.setString(++i, valS);
			// SUBALTERNO
			if (subalterno != null) {
				valI = Integer.valueOf(subalterno).intValue();
				Logger.log().info(
						DocfaDetrazioneManager.class.getName(),
						"DetrazioneManager adding parameter (int) " + valI
								+ "as SUBALTERNO to query ");
				pstmt.setInt(++i, valI);
			} else {
				Logger
						.log()
						.info(DocfaDetrazioneManager.class.getName(),
								"DetrazioneManager adding null as SUBALTERNO to query ");
				pstmt.setNull(++i, Types.NUMERIC);
			}

			rset = pstmt.executeQuery();
			while (rset.next()) {
				CodiceViaCivico cvc = new CodiceViaCivico();
				cvc.setCodiceVia(rset.getString(2));
				cvc.setCivico(rset.getString(3));
				res.add(cvc);
			}
		} catch (NullPointerException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (NumberFormatException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (SQLException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			cvcs = new CodiceViaCivico[0];
		} catch (NamingException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
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
		if(String.valueOf(comunicazione.getFlagPersona()).equals("G")){
			return false;
		}
		String idDocfa =null;
		try {
			idDocfa = comunicazione.getIidProtocolloReg()+"-"+comunicazione.getIidFornitura();
		} catch (Throwable e) {
			Logger.log()
			.error(
					this.getClass().getName(),
					"DetrazioneManager, docfa "+idDocfa+",  unable to detect idDocfa",e);
		}
		evalCodiciVia();
		evalResidenze();
		Logger.log()
				.info(
						this.getClass().getName(),
						"DetrazioneManager, docfa "+idDocfa+",  dataRiferimento ="
								+ dateParser.format(dataRiferimentoD));
		boolean found = false;
		boolean res = false;
		List<Residenza> resAtTime = new ArrayList<Residenza>();
		Residenza[] resds = comunicazione.getResidenzas();

		if (resds != null && resds.length > 0) {
			for (int i = 0; i < resds.length; i++) {
				Logger.log().info(this.getClass().getName(),
						"DetrazioneManager, docfa "+idDocfa+",  checking for " + resds[i]);
				if(isbetween(resds[i].getDataDaDate(),
						resds[i].getDataADate(), dataRiferimentoD)){
					Logger.log().info(
							this.getClass().getName(),
							"DetrazioneManager, docfa "+idDocfa+",  residenza  "
									+ resds[i]+" per data di riferimento");
					resAtTime.add(resds[i]);
				}
			}
			Logger.log().info(this.getClass().getName(),
					"DetrazioneManager, gli oggetti sono "+consOggettos.size() );
			Iterator<DocfaComunOggetto> itOgs = consOggettos.iterator();
			while (itOgs.hasNext()) {
				DocfaComunOggetto ogg = itOgs.next();
				for (Iterator<Residenza> iter = resAtTime.iterator(); iter.hasNext();) {
					Residenza element = iter.next();
					if(element.matchOggetto(ogg)){
						setOggettoConDetrazione(ogg);
						setResidenzaConDetrazione(element);
						ogg.setFlagAbitPrincipale(true);
						res = true;
						Logger.log().info(
								this.getClass().getName(),
								"DetrazioneManager, docfa "+idDocfa+",   HA Detrazione per l'oggetto: "
										+ ogg.getIid());
					}
					else{
						ogg.setFlagAbitPrincipale(false);
					}
				}
			}
			//if (!res && dap != null) {
			if (!res ) {
				for (int idx = 0; idx < resds.length; idx++) {
					if (resds[idx].getDataDaDate() != null
						&& resds[idx].getDataDaDate().after(dataRiferimentoD)) {
						itOgs = consOggettos.iterator();
						while (itOgs.hasNext()) {
							DocfaComunOggetto ogg = itOgs.next();
							if (resds[idx].matchOggetto(ogg)) {
								setOggettoConDetrazione(ogg);
								setResidenzaConDetrazione(resds[idx]);
								setResidenzaOltre90gg(true);
								ogg.setFlagAbitPrincipale(true);
								res = true;
								//dap.setFlagRegoleDapDataResOltre90Giorni(true);
								Logger.log().info(
										this.getClass().getName(),
										"DetrazioneManager, docfa "+idDocfa+",  Residenza indicata presa dopo dap data!!! "
												+ ogg.getIid());
								Logger.log().info(
										this.getClass().getName(),
										"DetrazioneManager, docfa "+idDocfa+", ha diritto di detrazione (ma con residenza presa dopo 90 gg) per l'oggetto: "
												+ ogg.getIid()+ " docfa="+ogg.getIidProtocolloReg()+"-"+ogg.getIidFornitura());
							}
						}
					}
				}
			}
			if(getOggettoConDetrazione() == null){
				Logger.log().info(
						this.getClass().getName(),
						"DetrazioneManager, docfa "+idDocfa+", : Per nessuno oggetto della comunicazione ha diritto di detrazione ");					
			}
			/*
			Session session = HibernateUtil.currentSession();
			try {
				session.saveOrUpdate(comunicazione);
				Logger.log().info(this.getClass().getName(),
						"comunicazione saved " + comunicazione.getIidComunicazione());
				itOgs = consOggettos.iterator();
				while (itOgs.hasNext()) {
					DocfaComunOggetto ogg = itOgs.next();
					session.saveOrUpdate(ogg);
					Logger.log().info(this.getClass().getName(),
							"oggetto saved " + ogg.getIid());
				}
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(),
						"error while detrazione form comunicazione", e);
				throw new MuiException(
						"error while detrazione form comunicazione", e);
			} */
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
			String valS = comunicazione.getCodfiscalePiva();
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
				comunicazione.addResidenza(res);
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
		if (comunicazione.getResidenzas().length == 0) {
			//if (dap != null)
			//	dap.setFlagRegoleDapNoDataResidenza(true);
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
				String valS = comunicazione.getCodfiscalePiva() ;
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
						comunicazione.addResidenza(res);
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
						comunicazione.addResidenza(res);
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
						comunicazione.addResidenza(res);
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

	public DocfaComunicazione getComunicazione() {
		return comunicazione;
	}

	public String getDataRiferimento() {
		return dataRiferimento;
	}

	public Set<DocfaComunOggetto> getConsOggettos() {
		return consOggettos;
	}

	public DocfaComunOggetto getOggettoConDetrazione() {
		return oggettoConDetrazione;
	}

	private void setOggettoConDetrazione(DocfaComunOggetto oggettoConDetrazione) {
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
	public static Boolean checkSoggettoInAnagrafe(DocfaComunicazione comunicazione){
		return  checkSoggettoInAnagrafe(comunicazione.getCodfiscalePiva());
	}
	public static boolean checkSoggettoInAnagrafe(String codiceFiscale){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = MuiApplication.getMuiApplication().getConnection();
			pstmt = conn
					.prepareStatement(getResidenzeSenzaStoricoIndirizziStatement);
			Logger.log().info(DocfaDetrazioneManager.class.getName(),
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
							DocfaDetrazioneManager.class.getName(),
							"DetrazioneManager adding parameter "
									+ valS
									+ " to getResidenzeSenzaStoricoIndirizziStatement query ");
			rset = pstmt.executeQuery();
			return rset.next();		
		} catch (SQLException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
					"error while evaluating detrazione ", e);
			return false;
		} catch (NamingException e) {
			Logger.log().error(DocfaDetrazioneManager.class.getName(),
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
	
	private DocfaComunicazione evalComunicazione(String iidSogg,String protocollo,String fornitura) 
	{
		Query query = null;
		query = HibernateUtil.currentSession().createQuery("select comunicazione  from  it.webred.docfa.model.DocfaComunicazione as comunicazione where  iidProtocolloReg = :iidProtocolloReg and iidFornitura = :iidFornitura and codfiscalePiva = :codfiscalePiva");
		query.setString("iidProtocolloReg", protocollo);
		query.setString("iidFornitura", fornitura);
		query.setString("codfiscalePiva", iidSogg);
		
		Iterator cIterator = query.iterate();
		DocfaComunicazione comunicazione =(cIterator.hasNext()?(DocfaComunicazione)cIterator.next():null);
		
		return comunicazione;
	}
}
