package it.webred.cs.csa.ejb.ejb;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.ejb.user.UserService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.amprofiler.model.AmUser;
import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableAlertSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.IterDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.DataModelCostanti.Segnalibri;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsACasoOpeTipoOpePK;
import it.webred.cs.data.model.CsCfgAttr;
import it.webred.cs.data.model.CsCfgAttrOption;
import it.webred.cs.data.model.CsCfgItStato;
import it.webred.cs.data.model.CsCfgItTransizione;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsItStepAttrValue;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.mailing.MailUtils;
import it.webred.mailing.MailUtils.MailAddressList;
import it.webred.utilities.CommonUtils;
import it.webred.utilities.DateTimeUtils;
import it.webred.utilities.SegnalibriMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AccessTableIterSessionBean
 */

@Stateless
public class AccessTableIterStepSessionBean extends CarSocialeBaseSessionBean implements
		AccessTableIterStepSessionBeanRemote {

	@Autowired
	private IterDAO iterDao;

	@EJB
	public AccessTableAlertSessionBeanRemote alertSessionBean;

	@EJB
	public AccessTableCasoSessionBeanRemote casoSessionBean;

	@EJB
	public AccessTableDiarioSessionBeanRemote diarioSessionBean;

	@EJB
	public AccessTableOperatoreSessionBeanRemote operatoreSessionBean;

	@EJB(mappedName = "java:global/AmProfiler/AmProfilerEjb/AnagraficaServiceBean")
	protected AnagraficaService anagraficaService;
	
	@EJB(mappedName = "java:global/AmProfiler/AmProfilerEjb/UserServiceBean")
	protected UserService userService;

	public AccessTableIterStepSessionBean() {
	}

	@Override
	public List<CsItStep> getIterStepsByCaso(IterDTO dto) throws Exception {
		return iterDao.getIterStepListByCaso(dto.getIdCaso());
	}

	@Override
	public CsItStep getLastIterStepByCaso(IterDTO dto) throws Exception {
		return iterDao.getLastIterStepByCaso(dto.getIdCaso());
	}

	@Override
	public boolean newIter(IterDTO dto)
			throws Exception {
		
		dto.setIdStato(DataModelCostanti.IterStatoInfo.DEFAULT_ITER_STEP_ID);
		dto.setAttrNewStato(null);
		dto.setNota("");
		return addIterStep(dto);

	}

	private SegnalibriMap getSegnalibriMap(CsItStep iterStep) {
		String soggetto_Cf = iterStep.getCsACaso().getCsASoggetto()
				.getCsAAnagrafica().getCf();
		String soggetto_Nome = iterStep.getCsACaso().getCsASoggetto()
				.getCsAAnagrafica().getNome();
		String soggetto_Cognome = iterStep.getCsACaso().getCsASoggetto()
				.getCsAAnagrafica().getCognome();

		SegnalibriMap map = new SegnalibriMap();

		map.put(Segnalibri.CASO_DATA_CREAZIONE, DateTimeUtils.dateToString(
				iterStep.getCsACaso().getDtIns(), "dd/MM/yy"));
		map.put(Segnalibri.CASO_DATA_MODIFICA, DateTimeUtils.dateToString(
				iterStep.getCsACaso().getDtMod(), "dd/MM/yy"));
		map.put(Segnalibri.CASO_ID, iterStep.getCsACaso().getId().toString());
		map.put(Segnalibri.CASO_NOME, soggetto_Nome + "  " + soggetto_Cognome
				+ "  (" + soggetto_Cf + ")");
		map.put(Segnalibri.CASO_USERNAME_UTENTE_CREAZIONE, iterStep
				.getCsACaso().getUserIns());
		map.put(Segnalibri.CASO_USERNAME_UTENTE_MODIFICA, iterStep.getCsACaso()
				.getUsrMod());
		map.put(Segnalibri.ITERSTEP_DATA_CREAZIONE, DateTimeUtils.dateToString(
				iterStep.getDataCreazione(), "dd/MM/yy"));

		map.put(Segnalibri.ITERSTEP_USERNAME_SEGNALANTE, iterStep
				.getCsOOperatore1().getUsername());
		AmAnagrafica operatoreAna1 = anagraficaService
				.findAnagraficaByUserName(iterStep.getCsOOperatore1().getUsername());
		map.put(Segnalibri.ITERSTEP_NOME_SEGNALANTE, operatoreAna1.getNome());
		map.put(Segnalibri.ITERSTEP_COGNOME_SEGNALANTE,
				operatoreAna1.getCognome());

		if (iterStep.getCsOOperatore2() != null) {
			AmAnagrafica operatoreAna2 = anagraficaService
					.findAnagraficaByUserName(iterStep
							.getCsOOperatore2().getUsername());
			map.put(Segnalibri.ITERSTEP_USERNAME_SEGNALATO, iterStep
					.getCsOOperatore2().getUsername());
			map.put(Segnalibri.ITERSTEP_NOME_SEGNALATO, operatoreAna2.getNome());
			map.put(Segnalibri.ITERSTEP_COGNOME_SEGNALATO,
					operatoreAna2.getCognome());
		}
		String sItStepAttrValues = "";
		if (iterStep.getCsItStepAttrValues() != null
				&& iterStep.getCsItStepAttrValues().size() > 0) {
			sItStepAttrValues = "<strong>"
					+ iterStep.getCsCfgItStato().getSezioneAttributiLabel()
					+ "</strong>" + "<br/>";
			List<String> listValue = new LinkedList<String>();
			for (CsItStepAttrValue itVal : iterStep.getCsItStepAttrValues())
				listValue.add("<strong>" + itVal.getCsCfgAttr().getLabel()
						+ "</strong>" + " : " + itVal.getValore());

			sItStepAttrValues += CommonUtils.Join("<br/>", listValue.toArray());
		}
		map.put(Segnalibri.ITERSTEP_STATO_SEZIONE_ATTRIBUTI, sItStepAttrValues);

		return map;
	}

	@Override
	public boolean addIterStep(IterDTO dto) throws Exception {
		CsItStep iterStep = new CsItStep();

		OperatoreDTO opDto = new OperatoreDTO();
		opDto.setUserId(dto.getUserId());
		opDto.setEnteId(dto.getEnteId());
		opDto.setUsername(dto.getNomeOperatore());
		CsOOperatore operatore = operatoreSessionBean.findOperatoreByUsername(opDto);

		opDto.setIdOperatore(operatore.getId());
		opDto.setIdSettore(dto.getIdSettore());
		opDto.setDate(new Date());
		CsOOperatoreSettore currOpSett = operatoreSessionBean.findOperatoreSettoreById(opDto);
		CsOSettore settoreUtente = operatoreSessionBean.findSettoreById(opDto);
		
		CsACaso caso = casoSessionBean.findCasoById(dto);

		BaseDTO tipoDiarioIdDTO = new BaseDTO();
		tipoDiarioIdDTO.setUserId(dto.getUserId());
		tipoDiarioIdDTO.setEnteId(dto.getEnteId());

		// Set Id of TipoDiario: 1 = CambioStato
		long TipoDiarioId = 1;
		tipoDiarioIdDTO.setObj(TipoDiarioId);

		CsTbTipoDiario tipoDiario = diarioSessionBean.findTipoDiarioById(tipoDiarioIdDTO);

		CsDDiario diario = new CsDDiario();
		diario.setCsACaso(caso);
		diario.setCsTbTipoDiario(tipoDiario);
		diario.setCsOOperatoreSettore(currOpSett);
		
		BaseDTO bDto = new BaseDTO();
		bDto.setUserId(dto.getUserId());
		bDto.setEnteId(dto.getEnteId());
		bDto.setObj(diario);
		
		diario = diarioSessionBean.createDiario(bDto);
		
		CsCfgItStato newStato = iterDao.findStatoById(dto.getIdStato());

		iterStep.setCsACaso(caso);
		iterStep.setCsDDiario(diario);

		iterStep.setCsOOperatore1(currOpSett.getCsOOperatore());
		iterStep.setCsOSettore1(settoreUtente);
		iterStep.setCsOOrganizzazione1(currOpSett.getCsOSettore().getCsOOrganizzazione());

		String emailSegnalatoA = "";
		String emailSettoreSegnalante = "";
		if (dto.isNotificaSettoreSegnalante())
			emailSettoreSegnalante = currOpSett.getCsOSettore().getEmail();

		opDto.setIdOperatore(dto.getIdOpTo());
		opDto.setIdSettore(dto.getIdSettTo());
		opDto.setIdOrganizzazione(dto.getIdOrgTo());
		
		boolean bContainsOpTo = dto.getIdOpTo() != null && dto.getIdOpTo() > 0;
		
		if (bContainsOpTo) {
			CsOOperatoreSettore operSett = operatoreSessionBean
					.findOperatoreSettoreById(opDto);
			iterStep.setCsOOperatore2(operSett.getCsOOperatore());
			AmUser amUser = userService.getUserByName(operSett.getCsOOperatore().getUsername());
			emailSegnalatoA = amUser.getEmail();
		}

		boolean bContainsSettTo = dto.getIdSettTo() != null && dto.getIdSettTo() > 0;
		
		if (bContainsSettTo) {
			CsOSettore settore = operatoreSessionBean.findSettoreById(opDto);
			iterStep.setCsOSettore2(settore);
			emailSegnalatoA = settore.getEmail();
		}

		boolean bContainsOrgTo = dto.getIdOrgTo() != null && dto.getIdOrgTo() > 0;
		
		if (bContainsOrgTo) {
			CsOOrganizzazione ente = operatoreSessionBean.findOrganizzazioneById(opDto);
			iterStep.setCsOOrganizzazione2(ente);
			emailSegnalatoA = ente.getEmail();
		} else {
			if (bContainsSettTo) {
				CsOSettore settore = operatoreSessionBean.findSettoreById(opDto);
				
				dto.setIdOrgTo(settore.getCsOOrganizzazione().getId());
				
				iterStep.setCsOOrganizzazione2(settore.getCsOOrganizzazione());
			}
		}

		iterStep.setDataCreazione(new Date());
		iterStep.setCsCfgItStato(newStato);

		iterStep.setNota(dto.getNota());

		if (dto.getAttrNewStato() != null) {
			iterStep.setCsItStepAttrValues(new LinkedList<CsItStepAttrValue>());

			for (Long idAttr : dto.getAttrNewStato().keySet()) {

				Object val = dto.getAttrNewStato().get(idAttr);

				CsCfgAttr itStatoAttr = iterDao.findStatoAttrById(idAttr);
				CsItStepAttrValue itVal = new CsItStepAttrValue();

				itVal.setCsCfgAttr(itStatoAttr);
				itVal.setCsItStep( iterStep );

				if (val != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					if (DataModelCostanti.TipoAttributo.ToEnum(
							itStatoAttr.getTipoAttr()).equals(
							DataModelCostanti.TipoAttributo.Enum.LIST)) {
						for (CsCfgAttrOption itOption : itStatoAttr
								.getCsCfgAttrOptions()) {
							if (itOption.getId().equals(
									Long.parseLong(val.toString()))) {
								itVal.setValore(itOption.getValore());
								break;
							}
						}
					} else if(val instanceof Date) {
						itVal.setValore(sdf.format(val));
					} else
						itVal.setValore(val.toString());
				}

				iterStep.getCsItStepAttrValues().add(itVal);
			}
		}

		// Inserisci nuovo IterStep
		boolean bAdded = iterDao.addIterStep(iterStep);

		if (bAdded) {
			SegnalibriMap map = getSegnalibriMap(iterStep);

			if (bContainsOpTo || bContainsSettTo || bContainsOrgTo) {
				String descrizione = "Il caso " + map.get(":CASO_NOME")	+ " ha cambiato stato in " + newStato.getNome();
				String titoloDescrizione = "Il caso " + map.get(":CASO_NOME") + " ha cambiato stato";

				dto.setDescrizione(descrizione);
				dto.setTitoloDescrizione(titoloDescrizione);
				dto.setTipo("IT");
				dto.setLabelTipo("Iter Step");
				
				alertSessionBean.addAlert(dto);
			}

			// Salva responsabile in caso
			if (iterStep.getCsCfgItStato().getId().equals(DataModelCostanti.IterStatoInfo.PRESO_IN_CARICO)){
				bDto.setObj(iterStep.getCsACaso().getId());
				CsACasoOpeTipoOpe responsabileCorrente = casoSessionBean.findResponsabile(bDto);
				if(responsabileCorrente != null) {
					//storicizzo il responsabile corrente
					responsabileCorrente.setDtMod(new Date());
					responsabileCorrente.setUsrMod(dto.getUserId());
					responsabileCorrente.setFlagResponsabile(null);
					bDto.setObj(responsabileCorrente);
					casoSessionBean.updateOperatoreCaso(bDto);
				}
				bDto.setObj(iterStep.getCsACaso().getId());
				List<CsACasoOpeTipoOpe> listaCasoopTipoOp = casoSessionBean.getListaOperatoreTipoOpByCasoId(bDto);
				//se già esiste ed è attivo setto il flag responsabile
				boolean exist = false;
				for(CsACasoOpeTipoOpe casoOpTipoOp: listaCasoopTipoOp) {
					CsOOperatoreSettore opSettore = casoOpTipoOp.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore();
					if(opSettore.getCsOOperatore().getId().equals(iterStep.getCsOOperatore1().getId()) 
							&& opSettore.getCsOSettore().getId().equals(iterStep.getCsOSettore1().getId())
							&& casoOpTipoOp.getId().getDataFineApp().after(new Date())) {
						exist = true;
						casoOpTipoOp.setFlagResponsabile(true);
						casoOpTipoOp.setDtMod(new Date());
						casoOpTipoOp.setUsrMod(dto.getUserId());
						casoOpTipoOp.getId().setDataFineApp(DataModelCostanti.END_DATE);
						bDto.setObj(casoOpTipoOp);
						casoSessionBean.updateOperatoreCaso(bDto);
					}
				}
				if(!exist) {
					//creo nuovo responsabile
					bDto.setObj(iterStep.getCsOOperatore1().getId());
					bDto.setObj2(iterStep.getCsOSettore1().getId());
					CsOOperatoreTipoOperatore opTipoOp = casoSessionBean.findOperatoreTipoOperatoreByOpSettore(bDto);
					if(opTipoOp != null) {
						CsACasoOpeTipoOpePK newResponsabilePK = new CsACasoOpeTipoOpePK();
						CsACasoOpeTipoOpe newResponsabile = new CsACasoOpeTipoOpe();
						newResponsabilePK.setCasoId(iterStep.getCsACaso().getId());
						newResponsabilePK.setDataFineApp(DataModelCostanti.END_DATE);
						newResponsabilePK.setOperatoreTipoOperatoreId(opTipoOp.getId());
						newResponsabile.setId(newResponsabilePK);
						newResponsabile.setDataInizioApp(new Date());
						newResponsabile.setDataInizioSys(new Date());
						newResponsabile.setDtIns(new Date());
						newResponsabile.setUserIns(dto.getUserId());
						newResponsabile.setFlagResponsabile(true);
						bDto.setObj(newResponsabile);
						casoSessionBean.salvaOperatoreCaso(bDto);
					}
				}
				
			} else casoSessionBean.setDataModifica(dto);

			// Now try to send email
			MailAddressList addressTO = new MailAddressList(emailSegnalatoA);
			MailAddressList addressCC = new MailAddressList();
			MailAddressList addressBCC = new MailAddressList(emailSettoreSegnalante);

			// Segnalibri
			String subject = iterStep.getCsCfgItStato().getOggettoEmail();
			String messageBody = iterStep.getCsCfgItStato().getCorpoEmail();

			subject = map.replaceTokens(subject);
			messageBody = map.replaceTokens(messageBody);

			MailUtils.sendEmail(addressTO, addressCC, addressBCC, subject,
					messageBody, (FileDataSource[]) null);

		}

		return bAdded;
	}

	@Override
	public CsCfgItStato findStatoById(IterDTO dto) throws Exception {
		return iterDao.findStatoById(dto.getIdStato());
	}

	@Override
	public List<CsCfgItTransizione> getTransizionesByStatoRuolo(IterDTO dto) throws Exception {
		return iterDao.findTransizionesByStatoRuolo(dto.getIdStato(), dto.getOpRuolo());
	}

}
