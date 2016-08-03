package it.webred.ct.service.carContrib.data.access.cc.dao;

import it.webred.ct.service.carContrib.data.access.cc.CarContribException;
import it.webred.ct.service.carContrib.data.access.cc.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.data.access.cc.dto.FonteNoteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.GesRicDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RisposteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.TipDocDTO;
import it.webred.ct.service.carContrib.data.model.CfgFonteNote;
import it.webred.ct.service.carContrib.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.service.carContrib.data.model.GesRic;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class CarContribJPAImpl extends CarContribBaseDAO implements
		CarContribDAO {

	private static final long serialVersionUID = 1L;

	public DecodTipDoc getTipDoc(TipDocDTO td) throws CarContribException {
		DecodTipDoc ogg = null;
		logger.debug("CarContribJPAImpl.getTipDoc: "
				+ td.getDecodTipDoc().getCodTipDoc());
		try {
			Query q = manager.createNamedQuery("DecodTipDoc.getTipDocByCod");
			q.setParameter("codTipDoc", td.getDecodTipDoc().getCodTipDoc());
			ogg = (DecodTipDoc) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("CarContribJPAImpl.getTipDoc- No Result "
					+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return ogg;
	}

	public void insertGesRichiesta(GesRicDTO gesRicDto)
			throws CarContribException {
		logger.debug("CarContribJPAImpl.insertGesRichiesta - IdRic: [ "
				+ gesRicDto.getGesRic().getId().getIdRic() + "];UserName: ["
				+ gesRicDto.getGesRic().getId().getUserName()
				+ "]; DtIniGes: ["
				+ gesRicDto.getGesRic().getId().getDtIniGes() + "]");
		try {
			manager.persist(gesRicDto.getGesRic());
			logger.debug("persist gesRichiesta. IdRic: "
					+ gesRicDto.getGesRic().getId().getIdRic());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}

	}

	public void insertRichiesta(RichiesteDTO richDto)
			throws CarContribException {
		logger.debug("CarContribJPAImpl.insertRichiesta - " +
				"Tipo ric: [ "+ richDto.getRich().getCodTipRic() + "];" +
				"IdSoggRic: ["+ richDto.getRich().getIdSoggRic() + "]; " +
				"IdSoggCar: ["+ richDto.getRich().getIdSoggCar() + "]; " +
				"CodTipDocRicon: ["+ richDto.getRich().getCodTipDocRicon()+"];" +
				"Storico:["+richDto.getRich().getFlgStorico()+"]");
		logger.debug("richDto.getRich().getCodTipMezRis() =="
				+ richDto.getRich().getCodTipMezRis());
		try {
			manager.persist(richDto.getRich());
			logger.debug("persist richiesta. Id: "
					+ richDto.getRich().getIdRic());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public void insertSoggetto(SoggettiCarContribDTO soggDto)
			throws CarContribException {
		logger.debug("CarContribJPAImpl.insertSoggetto: ["
				+ soggDto.getSogg().getCognome() + "];["
				+ soggDto.getSogg().getNome() + "];["
				+ soggDto.getSogg().getDtNas() + "];["
				+ soggDto.getSogg().getCodComNas());
		try {
			SoggettiCarContrib sogg = soggDto.getSogg();
			manager.persist(sogg);
			logger.debug("persist soggetto. Id: " + sogg.getIdSogg());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public SoggettiCarContrib getSoggetto(SoggettiCarContribDTO soggCarDTO)
			throws CarContribException {
		try {
			return manager.find(SoggettiCarContrib.class, soggCarDTO.getSogg()
					.getIdSogg());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public SoggettiCarContrib searchSoggettoPF(SoggettiCarContribDTO soggDto)
			throws CarContribException {
		SoggettiCarContrib sogg = null;
		logger.debug("CarContribJPAImpl.searchSoggettoPFByDatiAna: ["
				+ soggDto.getSogg().getCognome() + "];["
				+ soggDto.getSogg().getNome() + "];["
				+ soggDto.getSogg().getDtNas() + "];["
				+ soggDto.getSogg().getCodComNas());
		try {
			Query q = null;
			if (soggDto.getSogg().getCodFis() != null
					&& !soggDto.getSogg().getCodFis().equals(""))
				q = manager
						.createNamedQuery("SoggettiCarContrib.searchPFByDatiAnaCompleti");
			else
				q = manager
						.createNamedQuery("SoggettiCarContrib.searchPFByDatiAna");
			q.setParameter("nome", soggDto.getSogg().getNome());
			q.setParameter("cognome", soggDto.getSogg().getCognome());
			q.setParameter("dtNas", soggDto.getSogg().getDtNas());
			q.setParameter("codComNas", soggDto.getSogg().getCodComNas());
			if (soggDto.getSogg().getCodFis() != null
					&& !soggDto.getSogg().getCodFis().equals(""))
				q.setParameter("codFis", soggDto.getSogg().getCodFis());
			sogg = (SoggettiCarContrib) q.getSingleResult();
		} catch (NoResultException nre) {
			logger
					.warn("CarContribJPAImpl.searchSoggettoPFByDatiAna- No Result "
							+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return sogg;
	}

	public SoggettiCarContrib searchSoggettoPG(SoggettiCarContribDTO soggDto)
			throws CarContribException {
		SoggettiCarContrib sogg = null;
		logger.debug("CarContribJPAImpl.searchSoggettoPGByParIva: ["
				+ soggDto.getSogg().getParIva());
		try {
			Query q = null;
			if (soggDto.getSogg().getDenomSoc() != null
					&& !soggDto.getSogg().getDenomSoc().equals(""))
				q = manager
						.createNamedQuery("SoggettiCarContrib.searchPGByPIvaDenom");
			else
				q = manager
						.createNamedQuery("SoggettiCarContrib.searchPGByPIva");
			q.setParameter("parIva", soggDto.getSogg().getParIva());
			if (soggDto.getSogg().getDenomSoc() != null
					&& !soggDto.getSogg().getDenomSoc().equals(""))
				q.setParameter("denom", soggDto.getSogg().getDenomSoc());
			sogg = (SoggettiCarContrib) q.getSingleResult();
		} catch (NoResultException nre) {
			logger
					.warn("CarContribJPAImpl.searchSoggettoPGByParIva- No Result "
							+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return sogg;
	}

	public List<DecodTipDoc> getListaTipDoc(CeTBaseObject obj)
			throws CarContribException {
		List<DecodTipDoc> lista = null;
		logger.debug("CarContribJPAImpl.getListaTipDoc");
		try {
			Query q = manager.createNamedQuery("DecodTipDoc.getListaTipDoc");
			lista = (List) q.getResultList();
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return lista;
	}

	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj)
			throws CarContribException {
		List<CodiciTipoMezzoRisposta> lista = null;
		logger.debug("CarContribJPAImpl.getListaCodiciRisp");
		try {
			Query q = manager
					.createNamedQuery("CodiciTipoMezzoRisposta.getListaCodiciRisp");
			lista = (List) q.getResultList();
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return lista;
	}

	public CodiciTipoMezzoRisposta getDescCodiciRisp(
			CodiciTipoMezzoRispostaDTO codiceDTO) throws CarContribException {
		try {
			return manager.find(CodiciTipoMezzoRisposta.class, codiceDTO
					.getCodice().getCodice());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public void updateFilePdfRichiesta(RichiesteDTO richDto)
			throws CarContribException {

		try {
			Richieste r = manager.find(Richieste.class, richDto.getRich()
					.getIdRic());
			if (r != null)
				r.setNomePdf(richDto.getRich().getNomePdf());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public void insertRisposta(RisposteDTO rispDto) throws CarContribException {
		try {
			Query q = manager
					.createNamedQuery("Risposte.GetRispostaByIdRichiesta");
			q.setParameter("idRichiesta", rispDto.getRisp().getIdRic());
			List<Risposte> lista = (List<Risposte>) q.getResultList();

			if (lista == null || lista.size() == 0) {// Non c'è la riposta, la
														// inserisco
				// ALTRIMENTI NON FACCIO L'INSERT
				// INSERT RISPOSTA
				manager.persist(rispDto.getRisp());
			}

			// UPDATE DATA FINE GEST su tab S_CC_GES_RIC
			q = manager.createNamedQuery("GestRichieste.getDatiByIdRicAttiva");
			q.setParameter("idRic", rispDto.getRisp().getIdRic());
			// q.setParameter("utente", rispDto.getUserId());

			List<GesRic> listaGest = q.getResultList();

			if (listaGest.size() > 0) {

				logger.info(" ***********   insertRisposta per idRichiesta: " + rispDto.getRisp().getIdRic());
				GesRic gest = listaGest.get(0);
				logger.info(" insertRisposta UPDATE su S_CC_GES_RIC " + gest.getId().getUserName());
				gest.setDtFinGes(new Date());
			}
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public Richieste getRichiesta(RichiesteDTO richDTO)
			throws CarContribException {
		try {
			return manager.find(Richieste.class, richDTO.getRich().getIdRic());
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public Risposte getRisposta(RichiesteDTO richDTO)
			throws CarContribException {
		try {
			Query q = manager
					.createNamedQuery("Risposte.GetRispostaByIdRichiesta");
			q.setParameter("idRichiesta", richDTO.getRich().getIdRic());

			return (Risposte) q.getSingleResult();
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public List<String> getDistinctUserName(CeTBaseObject obj)
			throws CarContribException {
		List<String> lista = new ArrayList<String>();
		try {
			Query q = manager
					.createNamedQuery("GestRichieste.getDistinctUserName");
			List<String> result = (List<String>) q.getResultList();
			if (result != null && result.size() > 0) {
				for (String ele : result) {
					if (ele != null)
						lista.add((String) ele);
				}
			}
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
		return lista;
	}

	public List<FiltroRichiesteDTO> filtroRichiste(
			FiltroRichiesteSearchCriteria filtro, int start, int numberRecord) {
		List<FiltroRichiesteDTO> lista = new ArrayList<FiltroRichiesteDTO>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try {
			String strSql = " SELECT rich.DT_RIC, rich.COD_TIP_PROVEN, "
					+ " sogg.COD_TIP_SOGG, sogg.COGNOME, sogg.NOME, sogg.COD_FIS, sogg.DT_NAS, sogg.DENOM_SOC, sogg.PAR_IVA, "
					+ " (SELECT risp.DT_RIS FROM S_CC_RISPOSTE risp WHERE risp.ID_RIC=rich.ID_RIC) as evaso , "
					+ " rich.ID_RIC, rich.E_MAIL, rich.DES_NOT_RIC, rich.NOME_PDF, rich.COD_TIP_RIC, "
					+ " rich.ID_SOGG_RIC, rich.USER_NAME_RIC, rich.COD_TIP_DOC_RICON, rich.NUM_DOC_RICON, rich.DT_EMI_DOC_RICON, "
					+ " (SELECT doc.DES_TIP_DOC FROM S_CC_DECOD_TIP_DOC doc WHERE doc.COD_TIP_DOC=rich.COD_TIP_DOC_RICON) as descDoc, "
					+ " rich.NUM_TEL, "
					+ " (SELECT risp.RESPINTO FROM S_CC_RISPOSTE risp WHERE risp.ID_RIC=rich.ID_RIC) as respinto,"
					+ " (SELECT mezzo.DESCR FROM CODICI_TIPO_MEZZO_RISPOSTA mezzo WHERE rich.COD_TIP_MEZ_RIS=mezzo.CODICE) as descMezzo, "
					+ " rich.NUM_PROT , " 
					+ "  rich.COD_TIP_MEZ_RIS, rich.FLG_STORICO "
					+ " FROM S_CC_RICHIESTE rich, S_CC_SOGGETTI sogg"
					+ " WHERE rich.ID_SOGG_CAR=sogg.ID_SOGG ";

			strSql += this.getCondition(filtro);
			strSql += " ORDER BY rich.DT_RIC DESC, RICH.ID_RIC, sogg.COGNOME, sogg.NOME";

			logger.info(" QUERY = " + strSql);

			Query q = manager.createNativeQuery(strSql);
			q.setFirstResult(start);
			q.setMaxResults(numberRecord);

			List<Object[]> result = (List<Object[]>) q.getResultList();

			if (result == null || result.size() == 0)
				logger.info("NESSUN RISULTATO X FILTRO IMPOSTATO");

			for (Object[] ele : result) {
				FiltroRichiesteDTO f = new FiltroRichiesteDTO();

				if (ele[0] != null) {
					f.setDataRichiesta((Date) ele[0]);
					f.setStrDataRichiesta(df.format(f.getDataRichiesta()));
				} else
					f.setStrDataRichiesta("-");
				if (ele[1] != null)
					f.setTipoProvenienza(ele[1].toString().trim());

				if (ele[2] != null) {
					f.setCodTipoSogg(ele[2].toString());
					if (ele[2].toString().toUpperCase().equals("F")) {// PERSONA
						// FISICA
						f.setCognome(ele[3].toString().trim());
						f.setNome(ele[4].toString().trim());
						f.setIdentificativo(ele[3].toString().trim() + " "
								+ ele[4].toString().trim());
						f.setCodice(ele[5].toString().trim());
						f.setCodFiscale(ele[5].toString().trim());
						if (ele[6] != null) {
							f.setDataNascita((Date) ele[6]);
							f.setStrDataNascita(df.format(f.getDataNascita()));
						}
					} else {// PERSONA GIURIDICA
						f.setDenominazione(ele[7].toString().trim());
						f.setIdentificativo(ele[7].toString().trim());
						f.setCodice(ele[8].toString().trim());
						f.setPartIva(ele[8].toString().trim());
						f.setDataNascita(null);
						f.setStrDataNascita("-");
					}
				}
				f.setRichRespinta("0");
				if (ele[9] != null) {
					f.setDataEvasione((Date) ele[9]);
					f.setStrDataEvasione(df.format(f.getDataEvasione()));
					f.setRichEvasa("1");
					if (ele[22] != null && ele[22].toString().equals("1"))
						f.setRichRespinta("1"); // RICHIESTA EVASA MA RESPINTA
					else
						f.setRichRespinta("0"); // RICHIESTA EVASA
				} else {
					f.setRichEvasa("0");
					f.setStrDataEvasione("-");
				}

				if (ele[10] != null)
					f.setIdRichiesta(ele[10].toString());
				if (ele[11] != null)
					f.setEmail(ele[11].toString());
				if (ele[12] != null)
					f.setNote(ele[12].toString());
				if (ele[13] != null)
					f.setNomePdf(ele[13].toString());
				if (ele[14] != null)
					f.setTipoRichiesta(ele[14].toString().trim());
				if (ele[15] != null)
					f.setIdSoggettoRichiedente(ele[15].toString().trim());
				if (ele[16] != null)
					f.setUserNameRichiedente(ele[16].toString().trim());
				if (ele[17] != null)
					f.setCodTipoDocRicon(ele[17].toString().trim());
				if (ele[18] != null)
					f.setNumeroDocRicon(ele[18].toString().trim());
				if (ele[19] != null) {
					f.setDtEmissDocRicon((Date) ele[19]);
					f.setStrDtEmissDocRicon(df.format(f.getDtEmissDocRicon()));
				}
				if (ele[20] != null)
					f.setDescTipoDocRicon(ele[20].toString());
				if (ele[21] != null)
					f.setNumTel(ele[21].toString());
				if (ele[23] != null)
					f.setDescrMezzoRisposta(ele[23].toString().toUpperCase());
				if (ele[24] != null)
					f.setNumeroProtocollo(ele[24].toString().toUpperCase());
				if (ele[25] != null)
					f.setCodTipMezRis(ele[25].toString().toUpperCase());
								
				f.setStorico((ele[26]!=null && ele[26].toString().equals("1")) ? Boolean.TRUE : Boolean.FALSE);
				
				lista.add(f);
			}
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
		return lista;
	}

	public Long getRecordCount(FiltroRichiesteSearchCriteria filtro)
			throws CarContribException {
		try {
			String strSql = " SELECT COUNT(rich.ID_RIC)"
					+ " FROM S_CC_RICHIESTE rich, S_CC_SOGGETTI sogg"
					+ " WHERE rich.ID_SOGG_CAR=sogg.ID_SOGG ";

			strSql += this.getCondition(filtro);
			Query q = manager.createNativeQuery(strSql);
			BigDecimal res = (BigDecimal) q.getSingleResult();

			return res.longValue();
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	private String getCondition(FiltroRichiesteSearchCriteria filtro)
			throws CarContribException {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String cond = "";
			if (filtro != null) {
				if (!filtro.getCodFiscale().equals(""))
					cond += " AND UPPER(sogg.COD_FIS) = '"
							+ filtro.getCodFiscale().toUpperCase() + "' ";
				if (!filtro.getCognome().equals(""))
					cond += " AND UPPER(sogg.COGNOME) = '"
							+ filtro.getCognome().toUpperCase().replace("'", "''") + "' ";
				if (!filtro.getNome().equals(""))
					cond += " AND UPPER(sogg.NOME) = '"
							+ filtro.getNome().toUpperCase().replace("'", "''")  + "' ";
				if (filtro.getDataNascita() != null)
					cond += " AND sogg.DT_NAS=TO_DATE('"
							+ df.format(filtro.getDataNascita())
							+ "','dd/MM/yyyy')";
				if (!filtro.getCodFiscaleRichiedente().equals("") ||!filtro.getCognomeRichiedente().equals("") ||
					!filtro.getNomeRichiedente().equals("")|| filtro.getDataNascitaRichiedente() != null ) {
					cond += " AND EXISTS (SELECT * FROM S_CC_SOGGETTI soggR WHERE soggR.ID_SOGG = RICH.ID_SOGG_RIC ";
					if (!filtro.getCodFiscaleRichiedente().equals(""))
						cond += " AND UPPER(soggR.COD_FIS) = '"
							+ filtro.getCodFiscaleRichiedente().toUpperCase() + "' ";
					if (!filtro.getCognomeRichiedente().equals(""))
						cond += " AND UPPER(soggR.COGNOME) = '"
							+ filtro.getCognomeRichiedente().toUpperCase().replace("'", "''")  + "' ";
					if (!filtro.getNomeRichiedente().equals(""))
						cond += " AND UPPER(soggR.NOME) = '"
							+ filtro.getNomeRichiedente().toUpperCase().replace("'", "''")  + "' ";
					if(filtro.getDataNascitaRichiedente() != null)
						cond += " AND soggR.DT_NAS=TO_DATE('"
							+ df.format(filtro.getDataNascitaRichiedente())
							+ "','dd/MM/yyyy')";
					cond += " )";
				}
				if (!filtro.getPartitaIva().equals(""))
					cond += " AND UPPER(sogg.PAR_IVA) = '"
							+ filtro.getPartitaIva().toUpperCase() + "' ";
				if (!filtro.getDenominazione().equals(""))
					cond += " AND UPPER(sogg.DENOM_SOC) LIKE '%"
							+ filtro.getDenominazione().toUpperCase() + "%' ";
				if (!filtro.getIdRichiesta().equals(""))
					cond += " AND rich.ID_RIC = " + filtro.getIdRichiesta();
				if (!filtro.getTipoRichiesta().equals(""))
					cond += " AND UPPER(rich.COD_TIP_PROVEN) = '"
							+ filtro.getTipoRichiesta().toUpperCase() + "' ";
				if (filtro.getDataRichiestaDal() != null)
					cond += " AND rich.DT_RIC >= TO_DATE('"
							+ df.format(filtro.getDataRichiestaDal())
							+ "','dd/MM/yyyy')";
				if (filtro.getDataRichiestaAl() != null)
					cond += " AND rich.DT_RIC <= TO_DATE('"
							+ df.format(filtro.getDataRichiestaAl())
							+ "','dd/MM/yyyy')";
				/*
				 * if (filtro.getRichiesteEvase()) cond +=
				 * " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp)"
				 * ; if (filtro.getRichiesteNonEvase()) cond +=
				 * " AND rich.ID_RIC NOT IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp)"
				 * ;
				 */
				if (filtro.getRisposta().equals("0")) // RICHIESTA NON EVASA
					cond += " AND rich.ID_RIC NOT IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp)";
				if (filtro.getRisposta().equals("1")) // RICHIESTA EVASA
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp WHERE RESPINTO IS NULL OR RESPINTO='0')";
				if (filtro.getRisposta().equals("2")) // RICHIESTA EVASA MA
					// RIFIUTATA
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp WHERE RESPINTO = '1')";
				if (filtro.getDataEvasioneDal() != null)
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp WHERE risp.DT_RIS >= TO_DATE('"
							+ df.format(filtro.getDataEvasioneDal())
							+ "','dd/MM/yyyy'))";
				if (filtro.getDataEvasioneAl() != null)
					cond += " AND rich.ID_RIC IN (SELECT risp.ID_RIC FROM S_CC_RISPOSTE risp WHERE risp.DT_RIS <= TO_DATE('"
							+ df.format(filtro.getDataEvasioneAl())
							+ "','dd/MM/yyyy'))";
				if (filtro.getUserGesRic() != null
						&& !filtro.getUserGesRic().equals(""))
					cond += " AND EXISTS ( SELECT * FROM S_CC_GES_RIC GR WHERE RICH.ID_RIC = GR.ID_RIC AND USER_NAME = '"
							+ filtro.getUserGesRic() + "')";
			}

			return cond;
		} catch (Throwable t) {
			logger.error("", t);
			t.printStackTrace();
			throw new CarContribException(t);
		}
	}

	public CfgFonteNote getFonteNote(FonteNoteDTO obj) {
		CfgFonteNote ogg = null;
		logger.debug("CarContribJPAImpl.getFonteNote FONTE_LIV1 [" + obj.getCfgFonteNote().getFonteLiv1() + "]; FONTE_LIV2 [" +  obj.getCfgFonteNote().getFonteLiv2() + "]");
		try {
			Query q = null;
			if(obj.getCfgFonteNote().getFonteLiv2() != null && ! obj.getCfgFonteNote().getFonteLiv2().equals("") ){
				q = manager.createNamedQuery("CfgFonteNote.getNoteByFonteLiv1FonteLiv2");
				q.setParameter("fonteLiv1",obj.getCfgFonteNote().getFonteLiv1() );
				q.setParameter("fonteLiv2",obj.getCfgFonteNote().getFonteLiv2() );
			}
			else {
				q =manager.createNamedQuery("CfgFonteNote.getNoteByFonteLiv1");
				q.setParameter("fonteLiv1",obj.getCfgFonteNote().getFonteLiv1() );
			}
				
			ogg = (CfgFonteNote) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.debug("CarContribJPAImpl.getFonteNote- No Result "+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new CarContribException(t);
		}
		return ogg;
	}
}
