package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.cache.CacheRefresher;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupFornituraCfr;
import it.webred.successioni.model.ResultSetSuccessioni;
import it.webred.successioni.model.SuccessioniA;
import it.webred.successioni.model.SuccessioniB;
import it.webred.successioni.model.SuccessioniC;
import it.webred.successioni.model.SuccessioniD;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CodeBehindcfrListPage extends AbstractPage implements
		CacheRefresher {
	
	private static String SQL_GET_DATI_SUCC = null;
	private static final DateFormat dateformatDB = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void init() {
		MuiApplication.getMuiApplication().add(CFR_LIST_VARNAME, this);
	}

	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		boolean daPaginazione = false;
		Enumeration<?> params = req.getParameterNames();
		while (params.hasMoreElements()) {
			String[] param = params.nextElement().toString().split("-");
			if (param.length == 3) {
				if (param[0].equals("d") && param[2].equals("p")) {
					daPaginazione = true;
					break;
				}
			}
		}
		if (!daPaginazione) {
			//solo se non vengo da ordinamento o paginazione
			MuiApplication.getMuiApplication().forceEntryUpdate(CFR_LIST_VARNAME);			
		}
		int numeroForniture = 0;
		int numeroFornitureCaricate = 0;
		List cfrFornituras = (List)MuiApplication.getMuiApplication().get(CFR_LIST_VARNAME);
		if (cfrFornituras != null) {
			Iterator<MiDupFornituraCfr> iterFornitura = cfrFornituras.iterator();
			while (iterFornitura.hasNext()) {
				MiDupFornituraCfr element = iterFornitura.next();
				if (element.isFornitura()) {
					numeroForniture++;
				}
				if (element.isFornituraCaricata()) {
					numeroFornitureCaricate++;
				}
			}
		}		
		req.setAttribute("N_FORNITURE", numeroForniture);
		req.setAttribute("N_FORNITURE_CARICATE", numeroFornitureCaricate);
		return true;
	}

	public Object doRefresh() {
		SQL_GET_DATI_SUCC = loadResourceInString("/sql/getDatiCfrForniture.sql");
		Session session;
		Transaction tx;
		session = HibernateUtil.currentSession();
		tx = session.beginTransaction();
		try {
			List<MiDupFornituraCfr> cfrFornituras = new ArrayList<MiDupFornituraCfr>();
			Query query = session
					.createQuery("select c from it.webred.mui.model.MiDupFornitura as c order by dataInizialeDate desc");
			Logger.log().info(this.getClass().getName(),
					"executing query " + query);
			for (Iterator it = query.iterate(); it.hasNext();) {
				try {
					MiDupFornitura miDupFornitura = (MiDupFornitura) it.next();
					MiDupFornituraCfr miDupFornituraCfr = new MiDupFornituraCfr(miDupFornitura);
					cfrFornituras.add(miDupFornituraCfr);
				} catch (org.hibernate.ObjectNotFoundException e) {
					Logger.log().info(this.getClass().getName(),
							"ignoring phantom record reference", e);
				}
			}
			tx.commit();
			evalNumeroAnno(cfrFornituras);
			matchForniture(cfrFornituras);
			return cfrFornituras;
		} catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while updating fornitura list", e);
			try {
				tx.rollback();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			throw new MuiException("error while updating fornitura list", e);
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	private void matchForniture(List<MiDupFornituraCfr> cfrFornituras) throws Throwable {
		Collections.sort(cfrFornituras);
		Iterator<MiDupFornituraCfr> iterFornitura = cfrFornituras.iterator();
		int contaNote;
		ArrayList<MiDupFornituraCfr> cfrForniturasBuchi = new ArrayList<MiDupFornituraCfr>();
		java.util.Date dataPrec = null;
		while (iterFornitura.hasNext()) {
			MiDupFornituraCfr element = iterFornitura.next();
			ArrayList<java.util.Date> dates = new ArrayList<java.util.Date>();
			contaNote = contaNote(element.getDataInizialeDate(), element.getDataFinaleDate(), dates);
			if (contaNote > 0) {
				element.setDataInizialeDateOrig(element.getDataInizialeDate());
				element.setDataFinaleDateOrig(element.getDataFinaleDate());
				element.setNumeroNoteSummaryOrig(contaNote);
				element.setFornitura(true);
			}
			//verifico se ci sono buchi
			if (dataPrec != null) {
				GregorianCalendar dataPrecCal = new GregorianCalendar();
				dataPrecCal.setTime(dataPrec);
				dataPrecCal.add(Calendar.DAY_OF_YEAR, -1);
				java.util.Date dataCfr = dataPrecCal.getTime();
				if (element.getDataFinaleDate().getTime() < dataCfr.getTime()) {
					MiDupFornituraCfr miDupFornituraCfr = new MiDupFornituraCfr();
					miDupFornituraCfr.setDataFinaleDateOrig(dataCfr);
					GregorianCalendar dataInizialeCal = new GregorianCalendar();
					dataInizialeCal.setTime(element.getDataFinaleDate());
					dataInizialeCal.add(Calendar.DAY_OF_YEAR, 1);
					java.util.Date dataIniziale = dataInizialeCal.getTime();
					miDupFornituraCfr.setDataInizialeDateOrig(dataIniziale);
					ArrayList<java.util.Date> dates1 = new ArrayList<java.util.Date>();
					contaNote = contaNote(miDupFornituraCfr.getDataInizialeDateOrig(), miDupFornituraCfr.getDataFinaleDateOrig(), dates1);
					if (contaNote > 0) {
						miDupFornituraCfr.setNumeroNoteSummaryOrig(contaNote);
						suddividiPerMeseEAggiungi(cfrForniturasBuchi, miDupFornituraCfr, false, 12, dates1);					
					}					
				}
			}
			dataPrec = element.getDataInizialeDate();
		}	
		
		//cerco la prima data iniziale e l'ultima data finale	
		Collections.sort(cfrFornituras);
		java.util.Date primaData = null;
		java.util.Date ultimaData = null;
		if (cfrFornituras.size() > 0) {
			//n.b. l'ordinamento è desc
			primaData = cfrFornituras.get(cfrFornituras.size() - 1).getDataInizialeDate();
			ultimaData = cfrFornituras.get(0).getDataFinaleDate();
			GregorianCalendar primaDataCal = new GregorianCalendar();
			primaDataCal.setTime(primaData);
			primaDataCal.add(Calendar.DAY_OF_YEAR, -1);
			primaData = primaDataCal.getTime();
			GregorianCalendar ultimaDataCal = new GregorianCalendar();
			ultimaDataCal.setTime(ultimaData);
			ultimaDataCal.add(Calendar.DAY_OF_YEAR, 1);
			ultimaData = ultimaDataCal.getTime();
		}
		if (primaData == null && ultimaData == null) {
			ArrayList<java.util.Date> dates = new ArrayList<java.util.Date>();
			contaNote = contaNote(null, null, dates);
			if (contaNote > 0) {
				MiDupFornituraCfr miDupFornituraCfr = new MiDupFornituraCfr();
				miDupFornituraCfr.setNumeroNoteSummaryOrig(contaNote);
				suddividiPerMeseEAggiungi(cfrFornituras, miDupFornituraCfr, false, 12, dates);
			}
		}else{
			ArrayList<java.util.Date> dates = new ArrayList<java.util.Date>();
			contaNote = contaNote(null, primaData, dates);
			if (contaNote > 0) {
				MiDupFornituraCfr miDupFornituraCfr = new MiDupFornituraCfr();
				miDupFornituraCfr.setDataFinaleDateOrig(primaData);
				miDupFornituraCfr.setNumeroNoteSummaryOrig(contaNote);
				suddividiPerMeseEAggiungi(cfrFornituras, miDupFornituraCfr, false, 12, dates);
			}
			ArrayList<java.util.Date> dates1 = new ArrayList<java.util.Date>();
			contaNote = contaNote(ultimaData, null, dates1);
			if (contaNote > 0) {
				MiDupFornituraCfr miDupFornituraCfr = new MiDupFornituraCfr();
				miDupFornituraCfr.setDataInizialeDateOrig(ultimaData);
				miDupFornituraCfr.setNumeroNoteSummaryOrig(contaNote);
				suddividiPerMeseEAggiungi(cfrFornituras, miDupFornituraCfr, false, 12, dates1);
			}	
		}
		
		//aggiungo i buchi
		Iterator<MiDupFornituraCfr> iterFornituraBuchi = cfrForniturasBuchi.iterator();
		while (iterFornituraBuchi.hasNext()) {
			MiDupFornituraCfr element = iterFornituraBuchi.next();
			ArrayList<java.util.Date> dates = new ArrayList<java.util.Date>();
			contaNote = contaNote(element.getDataInizialeDateOrig(), element.getDataFinaleDateOrig(), dates);
			if (contaNote > 0) {
				element.setNumeroNoteSummaryOrig(contaNote);
				cfrFornituras.add(element);
			}
		}
		
		Collections.sort(cfrFornituras);
	}

	private void evalNumeroAnno(List<MiDupFornituraCfr> cfrFornituras) {
		Iterator<MiDupFornituraCfr> iterFornitura = cfrFornituras.iterator();
		List<MiDupFornituraCfr> v = new ArrayList<MiDupFornituraCfr>();
		while (iterFornitura.hasNext()) {
			MiDupFornituraCfr element = iterFornitura.next();
			v.add(0, element);
		}

		int anno = -1;
		int numeroAnno = 1;
		iterFornitura = v.iterator();
		while (iterFornitura.hasNext()) {
			MiDupFornituraCfr element = iterFornitura.next();
			if (anno != element.getAnno()) {
				anno = element.getAnno();
				numeroAnno = 1;
			}
			element.setNumeroAnno(numeroAnno);
			numeroAnno++;
		}
	}
	
	private int contaNote(java.util.Date dataIni, java.util.Date dataFin, ArrayList<java.util.Date> dates) throws ServletException, IOException, it.webred.successioni.exceptions.MuiException {
		try {
			int contaNote = 0;
			Connection conn = MuiApplication.getMuiApplication().getConnection();
			String sql = SQL_GET_DATI_SUCC.substring(0, SQL_GET_DATI_SUCC.indexOf(" AND a.data_apertura >= ?"));
			if (dataIni != null) {
				sql += " AND a.data_apertura >= ?";
			}
			if (dataFin != null) {
				sql += " AND a.data_apertura <= ?";
			}
			sql += SQL_GET_DATI_SUCC.substring(SQL_GET_DATI_SUCC.indexOf(" ORDER BY"));
			PreparedStatement _getStmt = conn.prepareStatement(sql);
			_getStmt.clearParameters();
			if (dataIni != null) {
				_getStmt.setString(1, dateformatDB.format(dataIni));
			}
			if (dataFin != null) {
				if (dataIni != null) {
					_getStmt.setString(2, dateformatDB.format(dataFin));
				}else{
					_getStmt.setString(1, dateformatDB.format(dataFin));
				}
			}

			Statement st = conn.createStatement();
			st.execute("ALTER SESSION SET NLS_TERRITORY = 'ITALY'");
			
			ResultSet rs = _getStmt.executeQuery();

			String idFornituraOld = "";
			SuccessioniA succA = null;
			SuccessioniB succB = null;
			SuccessioniC succC = null;
			SuccessioniD succD = null;
			ResultSetSuccessioni rss;
			while (rs.next()) {
				rss = make(rs);

				String pk_succaCurr = rss.getPk_id_succa();
				String pk_succbCurr = rss.getPk_id_succb();
				String pk_succcCurr = rss.getId_succc();
				String pk_succdCurr = rss.getId_succd();
				
				boolean aToCreate = true;
				if (succA != null) {
					boolean bToCreate = true;
					boolean cToCreate = true;
					boolean dToCreate = true;
					String pk_succaOld = succA.getPk_id_succa();
					if (pk_succaOld.equalsIgnoreCase(pk_succaCurr)) {
						aToCreate = false;
						// NON RICREO A - VERIFICO SE DEVO CREARE B - VERIFICO SE
						// DEVO CREARE C - VERIFICO SE DEVO CREARE D
						Collection listaB = succA.getListSuccessioniB();
						for (Iterator iter = listaB.iterator(); iter.hasNext();) {
							SuccessioniB succBCurr = (SuccessioniB) iter.next();
							if (succBCurr.getPk_id_succb().equalsIgnoreCase(pk_succbCurr)) {
								bToCreate = false;
								break;
							}
						}
						Collection listaC = succA.getListSuccessioniC();
						for (Iterator iter = listaC.iterator(); iter.hasNext();) {
							SuccessioniC succCCurr = (SuccessioniC) iter.next();
							if (succCCurr.getId_succc().equalsIgnoreCase(pk_succcCurr)) {
								cToCreate = false;
								break;
							}
						}
						Collection listaD = succA.getListSuccessioniD();
						for (Iterator iter = listaD.iterator(); iter.hasNext();) {
							SuccessioniD succDCurr = (SuccessioniD) iter.next();
							if (succDCurr.getId_succd().equalsIgnoreCase(pk_succdCurr)) {
								dToCreate = false;
								break;
							}
						}
					} else {
						String idFornituraCurr = "";
						if (idFornituraCurr.equalsIgnoreCase(idFornituraOld)) {
							//++idNota;
							contaNote++;
						} else {
							//nrIdNota = idNota;
							//idNota = 1;
							contaNote++;
						}
						if (succA.getA_data_apertura() != null && !succA.getA_data_apertura().equals("")) {
							java.util.Date miaData = dateformatDB.parse(succA.getA_data_apertura());
							boolean trovata = false;
							for (java.util.Date dataCurr : dates) {
								if (dataCurr.getTime() == miaData.getTime()) {
									trovata = true;
									break;
								}
							}
							if (!trovata) {
								dates.add(miaData);
							}
						}

						idFornituraOld = idFornituraCurr;

						aToCreate = true;
					}
					if (!aToCreate && bToCreate) {
						succB = makeB(rss);
						succA.addSuccessioniB(succB);
					}
					if (!aToCreate && cToCreate) {
						succC = makeC(rss);
						succA.addSuccessioniC(succC);
					}
					if (!aToCreate && dToCreate) {
						succD = makeD(rss);
						succA.addSuccessioniD(succD);
					}
				}
				if (aToCreate) {
					succA = makeA(rss);
					succB = makeB(rss);
					succC = makeC(rss);
					succD = makeD(rss);
					succA.addSuccessioniB(succB);
					succA.addSuccessioniC(succC);
					succA.addSuccessioniD(succD);
				}
			}
			
			if (succA != null) {
				contaNote++;
				if (succA.getA_data_apertura() != null && !succA.getA_data_apertura().equals("")) {
					java.util.Date miaData = dateformatDB.parse(succA.getA_data_apertura());
					boolean trovata = false;
					for (java.util.Date dataCurr : dates) {
						if (dataCurr.getTime() == miaData.getTime()) {
							trovata = true;
							break;
						}
					}
					if (!trovata) {
						dates.add(miaData);
					}
				}
			}
			
			conn.close();
			
			return contaNote;
			
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), e);
			throw new it.webred.successioni.exceptions.MuiException("Errore dalla procedura di lettura dei dati");
		}		
	}
	
	private ResultSetSuccessioni make(ResultSet rs) throws Exception {
		ResultSetSuccessioni rss = new ResultSetSuccessioni();
		try {
			rss.setPk_id_succa(rs.getString("pk_id_succa"));
			rss.setA_ufficio(rs.getString("ufficio"));
			rss.setA_anno(rs.getString("anno"));
			rss.setA_volume(rs.getString("volume"));
			rss.setA_numero(rs.getString("numero"));
			rss.setA_sottonumero(rs.getString("sottonumero"));
			rss.setA_comune(rs.getString("comune"));
			rss.setA_progressivo(rs.getString("progressivo"));
			rss.setA_tipo_dichiarazione(rs.getString("tipo_dichiarazione"));
			rss.setA_data_apertura(rs.getString("data_apertura"));
			rss.setA_cf_defunto(rs.getString("cf_defunto"));
			rss.setA_cognome_defunto(rs.getString("cognome_defunto"));
			rss.setA_nome_defunto(rs.getString("nome_defunto"));
			rss.setA_sesso(rs.getString("a_sesso"));
			rss.setA_citta_nascita(rs.getString("a_citta_nascita"));
			rss.setA_prov_nascita(rs.getString("a_prov_nascita"));
			rss.setA_data_nascita(rs.getString("a_data_nascita"));
			rss.setA_citta_residenza(rs.getString("a_citta_residenza"));
			rss.setA_prov_residenza(rs.getString("a_prov_residenza"));
			rss.setA_indirizzo_residenza(rs.getString("a_indirizzo_residenza"));
			rss.setA_fornitura(rs.getString("fornitura"));

			rss.setPk_id_succb(rs.getString("pk_id_succb"));
			rss.setB_ufficio(rs.getString("ufficio"));
			rss.setB_anno(rs.getString("anno"));
			rss.setB_volume(rs.getString("volume"));
			rss.setB_numero(rs.getString("numero"));
			rss.setB_sottonumero(rs.getString("sottonumero"));
			rss.setB_comune(rs.getString("comune"));
			rss.setB_progressivo(rs.getString("b_progressivo"));
			rss.setB_progressivo_erede(rs.getString("b_progressivo_erede"));
			rss.setB_cf_erede(rs.getString("cf_erede"));
			rss.setB_denominazione(rs.getString("denominazione"));
			rss.setB_sesso(rs.getString("b_sesso"));
			rss.setB_citta_nascita(rs.getString("b_citta_nascita"));
			rss.setB_prov_nascita(rs.getString("b_prov_nascita"));
			rss.setB_data_nascita(rs.getString("b_data_nascita"));
			rss.setB_citta_residenza(rs.getString("b_citta_residenza"));
			rss.setB_prov_residenza(rs.getString("b_prov_residenza"));
			rss.setB_indirizzo_residenza(rs.getString("b_indirizzo_residenza"));
			rss.setB_fornitura(rs.getString("fornitura"));

			rss.setC_ufficio(rs.getString("ufficio"));
			rss.setC_anno(rs.getString("anno"));
			rss.setC_volume(rs.getString("volume"));
			rss.setC_numero(rs.getString("numero"));
			rss.setC_sottonumero(rs.getString("sottonumero"));
			rss.setC_comune(rs.getString("comune"));
			rss.setC_progressivo(rs.getString("c_progressivo"));
			rss.setC_progressivo_immobile(rs.getString("c_progressivo_immobile"));
			rss.setC_fornitura(rs.getString("fornitura"));
			rss.setC_numeratore_quota_def(rs.getString("numeratore_quota_def"));
			rss.setC_denominatore_quota_def(rs.getString("denominatore_quota_def"));
			rss.setC_diritto(rs.getString("diritto"));
			rss.setC_catasto(rs.getString("catasto"));
			rss.setC_tipo_dati(rs.getString("tipo_dati"));
			rss.setC_foglio(rs.getString("foglio"));
			rss.setC_particella1(rs.getString("particella1"));
			rss.setC_particella2(rs.getString("particella2"));
			rss.setC_subalterno1(rs.getString("subalterno1"));
			rss.setC_subalterno2(rs.getString("subalterno2"));
			rss.setC_denuncia1(rs.getString("denuncia1"));
			rss.setC_anno_denuncia(rs.getString("anno_denuncia"));
			rss.setC_natura(rs.getString("natura"));
			rss.setC_superficie_ettari(rs.getString("superficie_ettari"));
			rss.setC_superficie_mq(rs.getString("superficie_mq"));
			rss.setC_vani(rs.getString("vani"));
			rss.setC_indirizzo_immobile(rs.getString("indirizzo_immobile"));

			rss.setD_ufficio(rs.getString("ufficio"));
			rss.setD_anno(rs.getString("anno"));
			rss.setD_volume(rs.getString("volume"));
			rss.setD_numero(rs.getString("numero"));
			rss.setD_sottonumero(rs.getString("sottonumero"));
			rss.setD_comune(rs.getString("comune"));
			rss.setD_progressivo(rs.getString("d_progressivo"));
			rss.setD_progressivo_immobile(rs.getString("d_progressivo_immobile"));
			rss.setD_progressivo_erede(rs.getString("d_progressivo_erede"));
			rss.setD_fornitura(rs.getString("fornitura"));
			rss.setD_numeratore_quota(rs.getString("d_numeratore_quota"));
			rss.setD_denominatore_quota(rs.getString("d_denominatore_quota"));
			rss.setD_agevolazione_1_casa(rs.getString("d_agevolazione_1_casa"));
			rss.setD_tipo_record(rs.getString("d_tipo_record"));

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del resultset - " + ex);
			throw ex;
		}
		return rss;
	}
	
	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniA riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniA makeA(ResultSetSuccessioni rss) throws Exception {
		SuccessioniA succA = new SuccessioniA();
		try {
			succA.setPk_id_succa(rss.getPk_id_succa());
			succA.setA_ufficio(rss.getA_ufficio());
			succA.setA_anno(rss.getA_anno());
			succA.setA_volume(rss.getA_volume());
			succA.setA_numero(rss.getA_numero());
			succA.setA_sottonumero(rss.getA_sottonumero());
			succA.setA_comune(rss.getA_comune());
			succA.setA_progressivo(rss.getA_progressivo());
			succA.setA_tipo_dichiarazione(rss.getA_tipo_dichiarazione());
			succA.setA_data_apertura(rss.getA_data_apertura());
			succA.setA_cf_defunto(rss.getA_cf_defunto());
			succA.setA_cognome_defunto(rss.getA_cognome_defunto());
			succA.setA_nome_defunto(rss.getA_nome_defunto());
			succA.setA_sesso(rss.getA_sesso());
			succA.setA_citta_nascita(rss.getA_citta_nascita());
			succA.setA_prov_nascita(rss.getA_prov_nascita());
			succA.setA_data_nascita(rss.getA_data_nascita());
			succA.setA_citta_residenza(rss.getA_citta_residenza());
			succA.setA_prov_residenza(rss.getA_prov_residenza());
			succA.setA_indirizzo_residenza(rss.getA_indirizzo_residenza());
			succA.setA_fornitura(rss.getA_fornitura());
		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniA - " + ex);
			throw ex;
		}
		return succA;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniB riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniB makeB(ResultSetSuccessioni rss) throws Exception {
		SuccessioniB succB = new SuccessioniB();
		try {
			succB.setPk_id_succb(rss.getPk_id_succb());
			succB.setB_ufficio(rss.getB_ufficio());
			succB.setB_anno(rss.getB_anno());
			succB.setB_volume(rss.getB_volume());
			succB.setB_numero(rss.getB_numero());
			succB.setB_sottonumero(rss.getB_sottonumero());
			succB.setB_comune(rss.getB_comune());
			succB.setB_progressivo(rss.getB_progressivo());
			succB.setB_progressivo_erede(rss.getB_progressivo_erede());
			succB.setB_cf_erede(rss.getB_cf_erede());
			succB.setB_denominazione(rss.getB_denominazione());
			succB.setB_sesso(rss.getB_sesso());
			succB.setB_data_nascita(rss.getB_data_nascita());
			succB.setB_citta_nascita(rss.getB_citta_nascita());
			succB.setB_prov_nascita(rss.getB_prov_nascita());
			succB.setB_citta_residenza(rss.getB_citta_residenza());
			succB.setB_prov_residenza(rss.getB_prov_residenza());
			succB.setB_indirizzo_residenza(rss.getB_indirizzo_residenza());
			succB.setB_fornitura(rss.getB_fornitura());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniB - " + ex);
			throw ex;
		}
		return succB;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniC riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniC makeC(ResultSetSuccessioni rss) throws Exception {
		SuccessioniC succC = new SuccessioniC();
		try {
			succC.setC_ufficio(rss.getC_ufficio());
			succC.setC_anno(rss.getC_anno());
			succC.setC_volume(rss.getC_volume());
			succC.setC_numero(rss.getC_numero());
			succC.setC_sottonumero(rss.getC_sottonumero());
			succC.setC_comune(rss.getC_comune());
			succC.setC_progressivo(rss.getC_progressivo());
			succC.setC_progressivo_immobile(rss.getC_progressivo_immobile());
			succC.setC_fornitura(rss.getC_fornitura());
			succC.setC_numeratore_quota_def(rss.getC_numeratore_quota_def());
			succC.setC_denominatore_quota_def(rss.getC_denominatore_quota_def());
			succC.setC_diritto(rss.getC_diritto());
			succC.setC_catasto(rss.getC_catasto());
			succC.setC_tipo_dati(rss.getC_tipo_dati());
			succC.setC_foglio(rss.getC_foglio());
			succC.setC_particella1(rss.getC_particella1());
			succC.setC_particella2(rss.getC_particella2());
			succC.setC_subalterno1(rss.getC_subalterno1());
			succC.setC_subalterno2(rss.getC_subalterno2());
			succC.setC_denuncia1(rss.getC_denuncia1());
			succC.setC_anno_denuncia(rss.getC_anno_denuncia());
			succC.setC_natura(rss.getC_natura());
			succC.setC_superficie_ettari(rss.getC_superficie_ettari());
			succC.setC_superficie_mq(rss.getC_superficie_mq());
			succC.setC_vani(rss.getC_vani());
			succC.setC_indirizzo_immobile(rss.getC_indirizzo_immobile());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniC - " + ex);
			throw ex;
		}
		return succC;
	}

	/**
	 * Dato un oggetto ResultSetSuccessioni ritorna un oggetto SuccessioniD riempito coi dati dell'oggetto ResultSetSuccessioni passato in input
	 * 
	 * @param rss
	 * @return
	 * @throws Exception
	 */
	private SuccessioniD makeD(ResultSetSuccessioni rss) throws Exception {
		SuccessioniD succD = new SuccessioniD();
		try {
			succD.setD_ufficio(rss.getC_ufficio());
			succD.setD_anno(rss.getC_anno());
			succD.setD_volume(rss.getC_volume());
			succD.setD_numero(rss.getC_numero());
			succD.setD_sottonumero(rss.getC_sottonumero());
			succD.setD_comune(rss.getC_comune());
			succD.setD_progressivo(rss.getC_progressivo());
			succD.setD_progressivo_immobile(rss.getD_progressivo_immobile());
			succD.setD_progressivo_erede(rss.getD_progressivo_erede());
			succD.setD_fornitura(rss.getC_fornitura());
			succD.setD_numeratore_quota(rss.getD_numeratore_quota());
			succD.setD_denominatore_quota(rss.getD_denominatore_quota());
			succD.setD_agevolazione_1_casa(rss.getD_agevolazione_1_casa());
			succD.setD_tipo_record(rss.getD_tipo_record());

		} catch (Exception ex) {
			Logger.log().error(this.getClass().getName(), "Errore nella creazione del record SuccessioniD - " + ex);
			throw ex;
		}
		return succD;
	}
	
	private void suddividiPerMeseEAggiungi(List<MiDupFornituraCfr> cfrFornituras, MiDupFornituraCfr miDupFornituraCfr, boolean soloAdd, int numMaxMesi, ArrayList<java.util.Date> dates) throws ServletException, IOException, it.webred.successioni.exceptions.MuiException {
		try {
			if (soloAdd) {
				//si usa per gli eventuali inizio e fine, visto che dividere mese per mese potrebbe essere lunghissimo
				miDupFornituraCfr.setFornitura(true);
				cfrFornituras.add(miDupFornituraCfr);
				return;
			}
			int contaMesi = 0;
			java.util.Date dataIni = miDupFornituraCfr.getDataInizialeDateOrig();
			java.util.Date dataFin = miDupFornituraCfr.getDataFinaleDateOrig();
			java.util.Date dataIniCurr = null;
			java.util.Date dataFinCurr = null;
			GregorianCalendar dataCalCalcolo = new GregorianCalendar();
			int contaNoteRimaste = miDupFornituraCfr.getNumeroNoteSummaryOrig();
			boolean desc = false;
			while (contaNoteRimaste > 0) {
				if (dataIniCurr == null) {
					dataIniCurr = dataIni;
				}
				if (dataFinCurr == null) {
					dataFinCurr = dataFin;
				}
				if (dataIniCurr == null && dataFinCurr == null) {
					dataFinCurr = new java.util.Date();
				}
				if (dataIniCurr == null) {
					desc = true;
				}							
				if (desc) {
					dataCalCalcolo.setTime(dataFinCurr);
					dataCalCalcolo.add(Calendar.DAY_OF_YEAR, 1);	
					dataCalCalcolo.add(Calendar.MONTH, -1);							
					dataIniCurr = dataCalCalcolo.getTime();						
				}else{
					dataCalCalcolo.setTime(dataIniCurr);					
					dataCalCalcolo.add(Calendar.MONTH, 1);	
					dataCalCalcolo.add(Calendar.DAY_OF_YEAR, -1);
					dataFinCurr = dataCalCalcolo.getTime();			
				}
				if (numMaxMesi > 0 && contaMesi == numMaxMesi) {
					//aggiungo il resto in un'unica riga
					ArrayList<java.util.Date> dates1 = new ArrayList<java.util.Date>();
					int contaNote;
					if (desc) {
						dataIniCurr = dataIni;													
					}else{
						dataFinCurr = dataFin;
					}
					contaNote = contaNote(dataIniCurr, dataFinCurr, dates1);
					if (contaNote > 0) {						
						MiDupFornituraCfr miDupFornituraCfrAdd = new MiDupFornituraCfr();
						miDupFornituraCfrAdd.setDataInizialeDateOrig(dataIniCurr);
						miDupFornituraCfrAdd.setDataFinaleDateOrig(dataFinCurr);
						miDupFornituraCfrAdd.setNumeroNoteSummaryOrig(contaNote);
						miDupFornituraCfrAdd.setFornitura(true);
						cfrFornituras.add(miDupFornituraCfrAdd);
					}	
					return;
				}
				boolean trovata = false;
				for (java.util.Date dataCurr : dates) {
					if (dataCurr.getTime() >= dataIniCurr.getTime() && dataCurr.getTime() <= dataFinCurr.getTime()) {
						trovata = true;
						break;
					}
				}
				if (trovata) {
					ArrayList<java.util.Date> dates1 = new ArrayList<java.util.Date>();
					int contaNote = contaNote(dataIniCurr, dataFinCurr, dates1);
					if (contaNote > 0) {						
						MiDupFornituraCfr miDupFornituraCfrAdd = new MiDupFornituraCfr();
						miDupFornituraCfrAdd.setDataInizialeDateOrig(dataIniCurr);
						miDupFornituraCfrAdd.setDataFinaleDateOrig(dataFinCurr);
						miDupFornituraCfrAdd.setNumeroNoteSummaryOrig(contaNote);
						miDupFornituraCfrAdd.setFornitura(true);
						cfrFornituras.add(miDupFornituraCfrAdd);
						contaMesi++;
					}
					contaNoteRimaste -= contaNote;
				}
				if (desc) {
					dataCalCalcolo.setTime(dataIniCurr);
					dataCalCalcolo.add(Calendar.DAY_OF_YEAR, -1);	
					dataFinCurr = dataCalCalcolo.getTime();
				}else{
					dataCalCalcolo.setTime(dataFinCurr);
					dataCalCalcolo.add(Calendar.DAY_OF_YEAR, 1);	
					dataIniCurr = dataCalCalcolo.getTime();				
				}
			}
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(), e);
			throw new it.webred.successioni.exceptions.MuiException("Errore dalla procedura di lettura dei dati");
		}		
		
	}
	
	private String loadResourceInString(String res) {
		try {
			return loadFileInString(this.getClass().getClassLoader()
					.getResourceAsStream(res));
		}catch(IOException ioe) {}
		return "";		
	}

	private String loadFileInString(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		int chunkLength = 256;
		byte[] buf = new byte[chunkLength];
		int readen = -1;
		while ((readen = is.read(buf)) != -1) {
			sb.append(new String(buf, 0, readen));
		}
		return sb.toString();
	}
	
}

