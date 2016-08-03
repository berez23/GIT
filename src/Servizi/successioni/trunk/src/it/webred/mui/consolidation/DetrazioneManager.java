package it.webred.mui.consolidation;

import it.webred.mui.MuiException;
import it.webred.mui.consolidation.DapEvaluator.MultiPossessoType;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.CodiceViaCivico;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTitolarita;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import net.skillbill.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;

public class DetrazioneManager extends SuperManager {
	MiDupSoggetti soggetto = null;

	private Boolean residenzaOltre90gg = false;
	private Set<MiConsOggetto> miConsOggettos = null;

	private MiConsOggetto oggettoConDetrazione = null;

	private MiConsDap dap = null;

	private Residenza residenzaConDetrazione = null;

	private MiConsComunicazione comunicazione = null;

	private Date dataRiferimento = null;

	private ComunicazioneConverter conv = new ComunicazioneConverter();


	


	public DetrazioneManager(MiDupSoggetti soggetto) throws IOException,
			MuiException {
		this.soggetto = soggetto;
		init();
	}

	public DetrazioneManager(MiDupSoggetti soggetto, MiConsDap dap)
			throws IOException, MuiException {
		super();
		this.soggetto = soggetto;
		this.dap = dap;
		init();
	}

	private void init() {
		this.dataRiferimento = soggetto.getMiDupNotaTras()
				.getDataValiditaAttoDate();
		comunicazione = conv.evalComunicazione(soggetto);
		miConsOggettos = comunicazione.getMiConsOggettos();

		/* mediando il codice dalle compravendite veniva aumentata la data di riferimento di 90gg
		 * sul mui si faceva per la detraziona ab. principale, mentre qui non si deve fare
		 * conta la data di validità dell'attto, senza aggiungere nulla
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataRiferimento);
		cal.add(Calendar.DAY_OF_MONTH, 90);
		dataRiferimento = cal.getTime();
		 */
		
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


	
	
	

	/**
	 * Restituisce le residenze prese entro fine anno rispetto alla data di riferimento
	 * @param resds
	 * @param datarif
	 */
	

	public boolean evalDetrazione() throws SQLException, NamingException {
			
		if (soggetto.isGiuridico()) {
			return false;
		}
		String idNota =null;
		try {
			idNota = soggetto.getMiDupNotaTras().getIdNota()+"/"+soggetto.getMiDupNotaTras().getAnnoNota();
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
		List<Residenza> resFineAnno = new ArrayList<Residenza>();
		Residenza[] resds = soggetto.getResidenzas();
		
		HashMap<Possesore, List<Residenza>> contitolari = getResidenzeContitolariOggettoDap();

		if (resds != null && resds.length > 0) {
			resAtTime = getResidezaAtTime(resds,dataRiferimento);
			// guardo se il soggetto è andato ha una residenza valida dalla nota a fine anno
			resFineAnno = getResidenzaFinoAFineAnno(resds, dataRiferimento);
			
			Logger.log().info(this.getClass().getName(),
					"DetrazioneManager, gli oggetti sono "+miConsOggettos.size() );
			Iterator<MiConsOggetto> itOgs = miConsOggettos.iterator();
			while (itOgs.hasNext()) {
				MiConsOggetto ogg = itOgs.next();
				
				boolean isResFineAnno = false;
				// verifica se residenze entro fine anno sono su oggetto in successione
				for (Iterator<Residenza> iter = resFineAnno.iterator(); iter.hasNext();) {
					Residenza element = iter.next();
					if(element.matchOggetto(ogg)){
						isResFineAnno = true;
						ogg.setDecorrenza(element.getDataDaDate());
						ogg.setFlagAbitazionePrincipale(true);
					}
				}
				

				
				for (Iterator<Residenza> iter = resAtTime.iterator(); iter.hasNext();) {
					Residenza element = iter.next();
					if(element.matchOggetto(ogg)){
						
						setOggettoConDetrazione(ogg);
						setResidenzaConDetrazione(element);
						ogg.setFlagAbitazionePrincipale(true);
						if(!isResFineAnno) 
							ogg.setDecorrenza(dap.getDapData());
							
						int numContitolariResidenti = 0;
							
						// se sto scorrendo l'oggetto corrente Dap allora 
						// vado a valutare i contitolari sull'oggetto
						boolean match = 
							compareNullableValue(dap.getFoglio(), ogg.getFoglio()==null?null:new Integer(ogg.getFoglio())) == 0 && 
							compareNullableValue(dap.getNumero(), ogg.getParticella()) == 0 &&
							compareNullableValue(dap.getSubalterno(), ogg.getSubalterno()==null?null:new Integer(ogg.getSubalterno())) == 0;
						if (match) {
								for (Possesore key : contitolari.keySet()) {
									boolean inResidenza=false;
									List<Residenza> resContit = contitolari.get(key);
									for (Iterator<Residenza> iterContit = resContit.iterator(); iterContit.hasNext();) {
										Residenza resContitolare = iterContit.next();
										if(resContitolare.matchOggetto(ogg)){
											inResidenza=true;
										}
									}
									if (inResidenza)
										numContitolariResidenti++;
						        }
								// metto null poi in fase di esportazione viene dato fuori 1 , con codice di errore 017
								if (numContitolariResidenti==0) {
									// sta ad indicare che non ho trovato contitolari anche se ho la detrazione='S'
									ogg.setContitolariAbitazionePrincipale(null);
									dap.setFlagRegoleDapNoDataComposizioneFamiliare(true);
								} else 
									ogg.setContitolariAbitazionePrincipale(new Integer(numContitolariResidenti));
						}
						
						res = true;
						Logger.log().info(
								this.getClass().getName(),
								"DetrazioneManager, nota "+idNota+",   HA Detrazione per l'oggetto: "
										+ ogg.getIid());
					} else{
						if(!isResFineAnno) 
							ogg.setFlagAbitazionePrincipale(false);
					}
				}
			}
			
			/* non c'è il discorso della residenza entro 90 gg come nel mui 
			 * nelle successioni il discorso è + semplice, basta l'algoritmo sopra
			if (!res && dap != null) {
				for (int idx = 0; idx < resds.length; idx++) {
					if (resds[idx].getDataDaDate() != null
							&& resds[idx].getDataDaDate()
									.after(dataRiferimento)) {
						itOgs = miConsOggettos.iterator();
						while (itOgs.hasNext()) {
							MiConsOggetto ogg = itOgs.next();
							if (resds[idx].matchOggetto(ogg)) {
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
			*/
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


	
	// QUANTO POSSESSORI SONO RESIDENTI NELLA DATA DI RIFERIMENTO E DOVE?
	// compreso il soggetto che sto trattando
	public HashMap<Possesore, List<Residenza>>  getResidenzeContitolariOggettoDap() {

		
		HashMap<Possesore, List<Residenza>> contitolari = new LinkedHashMap<Possesore, List<Residenza>>();
		
		List<Possesore> possesori = getPossesores(dap);
		try {
			for (Iterator<Possesore> iter = possesori.iterator(); iter.hasNext();) {
				Possesore possessore = iter.next();
						
				List<Residenza> resAtTime = new ArrayList<Residenza>();
				
				ArrayList<Residenza> residenze = new ArrayList<Residenza>();
				// cerco tutte le residenze dei possessori
				if (possessore.getCodiceFiscale()!=null) {
					residenze = getResidenzeByCodiceFiscale(possessore.getCodiceFiscale(), dap.getDapData());
					if (residenze.size()>0) {
						Residenza[] residenzeArray = residenzeArray(residenze);
						resAtTime = getResidezaAtTime(residenzeArray, dataRiferimento);
						contitolari.put(possessore, resAtTime);
					}
				}
					
			}
		
		} catch (NumberFormatException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getResidenzeContitolari ", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getResidenzeContitolari ", e);
			throw new RuntimeException(e);
		} catch (NamingException e) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating getResidenzeContitolari ", e);
			throw new RuntimeException(e);
		} finally {
		}
		return contitolari;

	}	
	
	

	private boolean evalResidenze() throws SQLException, NamingException {
		boolean hasResult = false;


		try {
			String valS;
			valS = "G".equals(soggetto.getTipoSoggetto()) ? soggetto
					.getCodiceFiscaleG() : soggetto.getCodiceFiscale();
			
			ArrayList<Residenza> res = this.getResidenzeByCodiceFiscale(valS,dap.getDapData());
			
			if (res!=null && res.size()>0)
				soggetto.addResidenzas(res);
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
		}
		if (soggetto.getResidenzas().length == 0) {
			hasResult = false;
			if (dap != null)
				dap.setFlagRegoleDapNoDataResidenza(true);
		} else 
			hasResult = true;


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
	
	public static void main(String[] s)
	throws Exception {
	
	}
	

}
