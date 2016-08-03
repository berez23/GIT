package it.webred.ct.service.tsSoggiorno.data.access;

import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.dao.DichiarazioneDAO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.ModuloDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RifiutoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloDati;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloOspiti;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRiduzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiuto;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiutoGruppo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class DichiarazioneServiceBean extends TsSoggiornoServiceBaseBean implements DichiarazioneService {

	@Autowired
	private DichiarazioneDAO dichiarazioneDAO;

	public List<IsPeriodo> getPeriodi(DataInDTO dataIn) {
		return dichiarazioneDAO.getPeriodi();
	}

	public List<IsPeriodo> getPeriodiNuovaDich(DataInDTO dataIn) {
		return dichiarazioneDAO.getPeriodiNuovaDich();
	}

	public List<DichiarazioneDTO> getDichiarazioniByCodFisc(DataInDTO dataIn) {
		return dichiarazioneDAO.getDichiarazioniByCodFisc(dataIn
				.getCodFiscale());
	}

	public List<DichiarazioneDTO> getDichiarazioniByCriteria(DataInDTO dataIn) {
		return dichiarazioneDAO
				.getDichiarazioniByCriteria((DichiarazioneSearchCriteria) dataIn
						.getObj());
	}

	public Long getDichiarazioniCountByCriteria(DataInDTO dataIn) {
		return dichiarazioneDAO
				.getDichiarazioniCountByCriteria((DichiarazioneSearchCriteria) dataIn
						.getObj());
	}

	public DichiarazioneDTO getDichiarazioneById(DataInDTO dataIn) {
		return dichiarazioneDAO.getDichiarazioneById(dataIn.getId());
	}

	public List<DichiarazioneDTO> getDichiarazioniByPeriodoDalAl(
			DataInDTO dataIn) {
		return dichiarazioneDAO.getDichiarazioniByPeriodoDalAl(dataIn.getId(),
				dataIn.getId3());
	}
	
	public List<IsDichiarazione> getDichiarazioniByPeriodoDalClasse(
			DataInDTO dataIn) {
		return dichiarazioneDAO.getDichiarazioniByPeriodoDalClasse(dataIn.getId(),
				dataIn.getId2());
	}

	public IsDichiarazione getDichiarazioneByPeriodoStrClasse(DataInDTO dataIn) {
		List<IsDichiarazione> lista = dichiarazioneDAO.getDichiarazioniByPeriodoStrClasse(dataIn.getId3(), dataIn.getId(), dataIn.getId2());
		IsDichiarazione valore = null;
		boolean trovato=false;
		if(lista!=null && lista.size()>0){
		for(IsDichiarazione dich : lista){
			if(dich.getIntegrativa().intValue()==0)
				valore = dich;
		}
		
		if(valore==null)
			valore = lista.get(0);
		}
		
		return valore;
	}
	
	
	public BigDecimal getTotSanzioniDichByPeriodoStrClasse(DataInDTO dataIn){
		BigDecimal totSanzioniPagate = new BigDecimal(0);
		String idSel = dataIn.getObj()==null ? null :  (String)dataIn.getObj();
		List<IsDichiarazione> lista = dichiarazioneDAO.getDichiarazioniByPeriodoStrClasse(dataIn.getId3(), dataIn.getId(), dataIn.getId2());
		for(IsDichiarazione dich : lista){
			//Escludo dal calcolo la dichiarazione attualmente in modifica, se presente
			if(!(idSel!=null && idSel.equals(dich.getId().toString())) && dich.getValSanzione()!=null)
				totSanzioniPagate = totSanzioniPagate.add(dich.getValSanzione());
		}
		
		return totSanzioniPagate;
	}
	
	
	public BigDecimal getTotRimborsiDichByPeriodoStrClasse(DataInDTO dataIn){
		BigDecimal totRimborsi = new BigDecimal(0);
		String idSel = dataIn.getObj()==null ? null :  (String)dataIn.getObj();
		List<IsDichiarazione> lista = dichiarazioneDAO.getDichiarazioniByPeriodoStrClasse(dataIn.getId3(), dataIn.getId(), dataIn.getId2());
		for(IsDichiarazione dich : lista){
			//Escludo dal calcolo la dichiarazione attualmente in modifica, se presente
			if(!(idSel!=null && idSel.equals(dich.getId().toString())) && dich.getValRimborso()!=null)
				totRimborsi = totRimborsi.add(dich.getValRimborso());
		}
		
		return totRimborsi;
	}
	
	
	public BigDecimal  getTotSanzioniByPeriodoStrClasse(DataInDTO dataIn){
		BigDecimal totSanzioni = new BigDecimal(0);
		List<IsSanzione> lista = this.getSanzioniByPeriodoStrClasse(dataIn);
		for(IsSanzione s : lista){
			if(s.getValore()!=null)
				totSanzioni = totSanzioni.add(s.getValore());
		}
		
		return totSanzioni;
	}
	
	public List<IsSanzione> getSanzioniByPeriodoStrClasse(DataInDTO dataIn){
		return dichiarazioneDAO.getSanzioniByPeriodoStrClasse(dataIn.getId3(), dataIn.getId(), dataIn.getId2());
	}
	
	public List<IsRimborso> getRimborsiByPeriodoStrClasse(DataInDTO dataIn){
		return dichiarazioneDAO.getRimborsiByPeriodoStrClasse(dataIn.getId3(), dataIn.getId(), dataIn.getId2());
	}

	public BigDecimal  getTotRimborsiByPeriodoStrClasse(DataInDTO dataIn){
		BigDecimal tot = new BigDecimal(0);
		List<IsRimborso> lista = this.getRimborsiByPeriodoStrClasse(dataIn);
		for(IsRimborso s : lista){
			if(s.getValore()!=null)
				tot = tot.add(s.getValore());
		}
		
		return tot;
	}
	
	public BigDecimal getTariffaByPeriodoClasse(DataInDTO dataIn){
		
		IsTariffa tar = dichiarazioneDAO.getLastTariffaByPeriodoClasse(dataIn.getId3(), dataIn.getId2());
		if(tar!=null)
			return tar.getValore();
		else
			return BigDecimal.ONE;
	}

	public IsDichiarazione getDichiarazionePrecByPeriodoStrClasse(
			DataInDTO dataIn) {
		return dichiarazioneDAO.getDichiarazionePrecByPeriodoStrClasse(
				dataIn.getId3(), dataIn.getId(), dataIn.getId2());
	}

	public IsDichiarazione saveDichiarazione(DataInDTO dataIn) {
		return dichiarazioneDAO.saveDichiarazione((IsDichiarazione) dataIn
				.getObj());
	}

	public void updateDichiarazione(DataInDTO dataIn) {
		dichiarazioneDAO.updateDichiarazione((IsDichiarazione) dataIn.getObj());
	}

	public void deleteDichiarazioneById(DataInDTO dataIn) {
		dichiarazioneDAO.deleteDichiarazioneById(dataIn.getId());
	}

	public IsStrutturaSnap saveStrutturaSnap(DataInDTO dataIn) {
		return dichiarazioneDAO.saveStrutturaSnap((IsStrutturaSnap) dataIn
				.getObj());
	}

	public void updateStrutturaSnap(DataInDTO dataIn) {
		dichiarazioneDAO.updateStrutturaSnap((IsStrutturaSnap) dataIn.getObj());
	}

	public List<ModuloDTO> getNewModuliVersato(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getVersatoByPeriodo(dataIn.getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(new IsModuloOspiti());
			List<IsModuloEventi> listaEventi = new ArrayList<IsModuloEventi>();
			listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getLoadModuliVersato(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		DichiarazioneDTO dich = dichiarazioneDAO.getDichiarazioneById(dataIn
				.getId());
		List<IsModuloDati> listaMd = dichiarazioneDAO.getVersatoByPeriodo(dich
				.getPeriodo().getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(dichiarazioneDAO.getOspitiByDichiarazioneModulo(
					dataIn.getId(), String.valueOf(md.getId())));
			List<IsModuloEventi> listaEventi = dichiarazioneDAO
					.getEventiByDichiarazioneModulo(dataIn.getId(),
							String.valueOf(md.getId()));
			while (listaEventi.size() < 1)
				listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getNewModuliEsenzioni(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getEsenzioniByPeriodo(dataIn.getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(new IsModuloOspiti());
			List<IsModuloEventi> listaEventi = new ArrayList<IsModuloEventi>();
			listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getLoadModuliEsenzioni(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		DichiarazioneDTO dich = dichiarazioneDAO.getDichiarazioneById(dataIn
				.getId());
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getEsenzioniByPeriodo(dich.getPeriodo().getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(dichiarazioneDAO.getOspitiByDichiarazioneModulo(
					dataIn.getId(), String.valueOf(md.getId())));
			List<IsModuloEventi> listaEventi = dichiarazioneDAO
					.getEventiByDichiarazioneModulo(dataIn.getId(),
							String.valueOf(md.getId()));
			while (listaEventi.size() < 1)
				listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getNewModuliRiduzioni(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getRiduzioniByPeriodo(dataIn.getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(new IsModuloOspiti());
			List<IsModuloEventi> listaEventi = new ArrayList<IsModuloEventi>();
			listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);
			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getLoadModuliRiduzioni(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		DichiarazioneDTO dich = dichiarazioneDAO.getDichiarazioneById(dataIn
				.getId());
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getRiduzioniByPeriodo(dich.getPeriodo().getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(dichiarazioneDAO.getOspitiByDichiarazioneModulo(
					dataIn.getId(), String.valueOf(md.getId())));
			List<IsModuloEventi> listaEventi = dichiarazioneDAO
					.getEventiByDichiarazioneModulo(dataIn.getId(),
							String.valueOf(md.getId()));
			while (listaEventi.size() < 1)
				listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getNewModuliPagAnticipati(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getPagAnticipatiByPeriodo(dataIn.getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(new IsModuloOspiti());
			List<IsModuloEventi> listaEventi = new ArrayList<IsModuloEventi>();
			listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);
			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public List<ModuloDTO> getLoadModuliPagAnticipati(DataInDTO dataIn) {

		List<ModuloDTO> listaDTO = new ArrayList<ModuloDTO>();
		DichiarazioneDTO dich = dichiarazioneDAO.getDichiarazioneById(dataIn
				.getId());
		List<IsModuloDati> listaMd = dichiarazioneDAO
				.getPagAnticipatiByPeriodo(dich.getPeriodo().getId());
		for (IsModuloDati md : listaMd) {
			ModuloDTO dto = new ModuloDTO();
			dto.setDati(md);
			dto.setListaField(dichiarazioneDAO.getFieldByModulo(md.getId()));
			dto.setOspiti(dichiarazioneDAO.getOspitiByDichiarazioneModulo(
					dataIn.getId(), String.valueOf(md.getId())));
			List<IsModuloEventi> listaEventi = dichiarazioneDAO
					.getEventiByDichiarazioneModulo(dataIn.getId(),
							String.valueOf(md.getId()));
			while (listaEventi.size() < 1)
				listaEventi.add(new IsModuloEventi());
			dto.setEventi(listaEventi);
			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public void saveModuloOspiti(DataInDTO dataIn) {
		dichiarazioneDAO.saveModuloOspiti((IsModuloOspiti) dataIn.getObj());
	}

	public void updateModuloOspiti(DataInDTO dataIn) {
		dichiarazioneDAO.updateModuloOspiti((IsModuloOspiti) dataIn.getObj());
	}

	public void deleteModuloOspitiByDich(DataInDTO dataIn) {
		dichiarazioneDAO.deleteModuloOspitiByDich(dataIn.getId());
	}

	public void saveModuloEventi(DataInDTO dataIn) {
		dichiarazioneDAO.saveModuloEventi((IsModuloEventi) dataIn.getObj());
	}

	public void updateModuloEventi(DataInDTO dataIn) {
		dichiarazioneDAO.updateModuloEventi((IsModuloEventi) dataIn.getObj());
	}

	public void deleteModuloEventiByDich(DataInDTO dataIn) {
		dichiarazioneDAO.deleteModuloEventiByDich(dataIn.getId());
	}

	public IsImpostaDovuta getImpostaDovutaByDichiarazione(DataInDTO dataIn) {
		return dichiarazioneDAO.getImpostaDovutaByDichiarazione(dataIn.getId());
	}

	public void saveImpostaDovuta(DataInDTO dataIn) {
		dichiarazioneDAO.saveImpostaDovuta((IsImpostaDovuta) dataIn.getObj());
	}

	public void updateImpostaDovuta(DataInDTO dataIn) {
		dichiarazioneDAO.updateImpostaDovuta((IsImpostaDovuta) dataIn.getObj());
	}

	public IsImpostaIncassata getImpostaIncassataByDichiarazione(
			DataInDTO dataIn) {
		return dichiarazioneDAO.getImpostaIncassataByDichiarazione(dataIn
				.getId());
	}

	public void saveImpostaIncassata(DataInDTO dataIn) {
		dichiarazioneDAO.saveImpostaIncassata((IsImpostaIncassata) dataIn
				.getObj());
	}

	public void updateImpostaIncassata(DataInDTO dataIn) {
		dichiarazioneDAO.updateImpostaIncassata((IsImpostaIncassata) dataIn
				.getObj());
	}
	
	public List<IsConfig> getConfig(DataInDTO dataIn) {
		return dichiarazioneDAO.getConfig();
	}
	
	public IsConfig getConfigByChiave(DataInDTO dataIn) {
		return dichiarazioneDAO.getConfigByChiave(dataIn.getId2());
	}

	public BigDecimal getRiduzioneByCodModulo(DataInDTO dataIn) {
		BigDecimal rid = null;
		IsRiduzione ridMod = dichiarazioneDAO.getRiduzioneByCodModulo(dataIn.getId2());
		if(ridMod!=null)
			rid = ridMod.getPercRiduz();
		return rid;
	}
	
	public List<RifiutoDTO> getListaRifiutiByIdDich(DataInDTO dataIn){
		
		List<RifiutoDTO> listadto = new ArrayList<RifiutoDTO>();
		
		List<IsRifiuto> listaRif = dichiarazioneDAO.getListaRifiutiByIdDich(dataIn.getId());
		int index = 0;
		for(IsRifiuto r : listaRif){
			RifiutoDTO dto = new RifiutoDTO(Integer.toString(index));
			dto.setRifiuto(r);
			dto.setGruppo(dichiarazioneDAO.getListaGruppoByIdRifiuto(r.getId()));
			listadto.add(dto);
			index++;
		}
		
		return listadto;
	}

	public void updateRifiuto(DataInDTO dataIn) {
		
		RifiutoDTO dto = (RifiutoDTO)dataIn.getObj();
		
		dto.getRifiuto().setUsrMod(dataIn.getUserId());
		dto.getRifiuto().setDtMod(new Date());
		
		dichiarazioneDAO.updateIsRifiuto(dto.getRifiuto());
		
		List<IsRifiutoGruppo> precGruppo = dichiarazioneDAO.getListaGruppoByIdRifiuto(dto.getRifiuto().getId());
		
		//Prima rimuovo i componenti che non esistono più
		for(IsRifiutoGruppo prg : precGruppo){	
			Long idOld = prg.getId();
			if(componenteToDelete(dto.getGruppo(), idOld))
				dichiarazioneDAO.deleteIsRifiutoGruppo(idOld);
		}
		
		//Ora posso aggiornare gli altri ed inserire i nuovi
		for(IsRifiutoGruppo rg : dto.getGruppo()){
			if(rg.getId()!=null){
				rg.setUsrMod(dataIn.getUserId());
				rg.setDtMod(new Date());
				dichiarazioneDAO.updateIsRifiutoGruppo(rg);
			}else{
				rg.setIsRifiuto(dto.getRifiuto());
				rg.setUsrIns(dataIn.getUserId());
				rg.setDtIns(new Date());
				dichiarazioneDAO.saveIsRifiutoGruppo(rg);
			}
		}
	}
	
	private boolean rifiutoToDelete(List<RifiutoDTO> lstNuova, Long idRifOld){
		boolean toDelete = true;
		int i = 0;
		while(i<lstNuova.size() && !toDelete){
			
			IsRifiuto rg = lstNuova.get(i).getRifiuto();
			if(rg.getId()!=null && rg.getId()==idRifOld)
				toDelete = false;
			
			i++;
		}
		
		
		return toDelete;
	}
	
	private boolean componenteToDelete(List<IsRifiutoGruppo> gruppoNuovo, Long idGruppoOld){
		boolean toDelete = true;
		int i = 0;
		while(i<gruppoNuovo.size() && !toDelete){
			
			IsRifiutoGruppo rg = gruppoNuovo.get(i);
			if(rg.getId()!=null && rg.getId()==idGruppoOld)
				toDelete = false;
			
			i++;
		}
		
		
		return toDelete;
	}

	public void saveRifiuto(DataInDTO dataIn) {
		RifiutoDTO dto = (RifiutoDTO)dataIn.getObj();
		
		dto.getRifiuto().setUsrIns(dataIn.getUserId());
		dto.getRifiuto().setDtIns(new Date());
		
		IsRifiuto rifiuto = dichiarazioneDAO.saveIsRifiuto(dto.getRifiuto());
		
		for(IsRifiutoGruppo rg : dto.getGruppo()){
			rg.setIsRifiuto(rifiuto);
			rg.setIsRifiuto(dto.getRifiuto());
			rg.setUsrIns(dataIn.getUserId());
			rg.setDtIns(new Date());
			dichiarazioneDAO.saveIsRifiutoGruppo(rg);
		}
		
	}

	public void updateListaRifiuti(DataInDTO dataIn) {
		List<RifiutoDTO> listaRifiuti = (List<RifiutoDTO>)dataIn.getObj();
		List<IsRifiuto> lstOld =  dichiarazioneDAO.getListaRifiutiByIdDich(dataIn.getId());
		
		//Prima elimino i record non più esistenti
		for(IsRifiuto r : lstOld){	
			Long idOld = r.getId();
			if(this.rifiutoToDelete(listaRifiuti, idOld)){
				dichiarazioneDAO.deleteGruppoByRifiuto(idOld);
				dichiarazioneDAO.deleteIsRifiuto(idOld);
			}
		}
		
		for(RifiutoDTO dto : listaRifiuti){
			if(dto.getRifiuto().getId()!=null)
				this.updateRifiuto(dataIn);
			else
				this.saveRifiuto(dataIn);
	}
		
	}

	public IsPeriodo getPeriodoById(DataInDTO dataIn) {
		return dichiarazioneDAO.getPeriodoById(dataIn.getId());
	}

}
