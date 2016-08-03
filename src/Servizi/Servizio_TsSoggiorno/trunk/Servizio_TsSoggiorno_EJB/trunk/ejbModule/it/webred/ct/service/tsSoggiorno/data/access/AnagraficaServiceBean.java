package it.webred.ct.service.tsSoggiorno.data.access;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dao.AnagraficaDAO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.StrutturaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.Sitidstr;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarSocialeServiceBean
 */
@Stateless
public class AnagraficaServiceBean extends TsSoggiornoServiceBaseBean implements
		AnagraficaService {
	/**
	 * 
	 */

	@Autowired
	private AnagraficaDAO anagraficaDAO;

	public IsSoggetto getSoggettoByCodFis(DataInDTO dataIn) {
		return anagraficaDAO.getSoggettoByCodFis(dataIn.getCodFiscale());
	}

	public void updateSoggetto(DataInDTO dataIn) {
		anagraficaDAO.updateSoggetto((IsSoggetto) dataIn.getObj());
	}

	public List<SocietaDTO> getSocieta(DataInDTO dataIn) {
		List<SocietaDTO> listaDTO = new ArrayList<SocietaDTO>();
		
		List<IsSocieta> listaSoc = anagraficaDAO.getSocieta();
		for(IsSocieta soc: listaSoc){
			SocietaDTO dto = new SocietaDTO();
			dto.setSocieta(soc);
			dto.setSocietaSogg(anagraficaDAO.getSocietaSoggById(soc.getId()));
			listaDTO.add(dto);
		}
		return listaDTO;
	}
	
	public List<SocietaDTO> getSuggestionSocieta(DataInDTO dataIn) {
		List<SocietaDTO> listaDTO = new ArrayList<SocietaDTO>();
		
		List<IsSocieta> listaSoc = anagraficaDAO.getSuggestionSocieta(dataIn.getObj().toString());
		for(IsSocieta soc: listaSoc){
			SocietaDTO dto = new SocietaDTO();
			dto.setSocieta(soc);
			dto.setSocietaSogg(anagraficaDAO.getSocietaSoggById(soc.getId()));
			listaDTO.add(dto);
		}
		return listaDTO;
	}
	
	public List<SocietaDTO> getSocietaByCodFis(DataInDTO dataIn) {
		List<SocietaDTO> listaDTO = new ArrayList<SocietaDTO>();
		
		List<IsSocieta> listaSoc = anagraficaDAO.getSocietaByCodFis(dataIn.getCodFiscale());
		for(IsSocieta soc: listaSoc){
			SocietaDTO dto = new SocietaDTO();
			dto.setSocieta(soc);
			dto.setSocietaSogg(anagraficaDAO.getSocietaSoggById(soc.getId()));
			listaDTO.add(dto);
		}
		return listaDTO;
	}
	
	
	public List<SocietaDTO> getSocietaByCfPi(DataInDTO dataIn) {		
		List<SocietaDTO> listaDTO = new ArrayList<SocietaDTO>();
		
		List<IsSocieta> listaSoc = anagraficaDAO.getSocietaByCfPi(dataIn.getCodFiscale());
		for(IsSocieta soc: listaSoc){
			SocietaDTO dto = new SocietaDTO();
			dto.setSocieta(soc);
			dto.setSocietaSogg(anagraficaDAO.getSocietaSoggById(soc.getId()));
			listaDTO.add(dto);
		}
		return listaDTO;
	}

	public IsSocieta getSocietaById(DataInDTO dataIn) {
		return anagraficaDAO.getSocietaById(dataIn.getId());
	}

	public void updateSocieta(DataInDTO dataIn) {
		anagraficaDAO.updateSocieta((IsSocieta) dataIn.getObj());
	}

	public IsSocieta saveSocieta(DataInDTO dataIn) {
		return anagraficaDAO.saveSocieta((IsSocieta) dataIn.getObj());
		
	}
	
	public IsSocietaSogg getSocietaSoggById(DataInDTO dataIn) {
		return anagraficaDAO.getSocietaSoggById(dataIn.getId());
	}
	
	public void updateSocietaSogg(DataInDTO dataIn) {
		anagraficaDAO.updateSocietaSogg((IsSocietaSogg) dataIn.getObj());
		
	}
	
	public void saveSocietaSogg(DataInDTO dataIn) {
		anagraficaDAO.saveSocietaSogg((IsSocietaSogg) dataIn.getObj());
		
	}

	public void deleteSocietaById(DataInDTO dataIn) {
		anagraficaDAO.deleteSocietaById(dataIn.getId());
	}

	public List<StrutturaDTO> getStrutture(DataInDTO dataIn) {
		List<StrutturaDTO> lista = new ArrayList<StrutturaDTO>();
		List<IsStruttura> listaStr = anagraficaDAO.getStrutture();
		for(IsStruttura str: listaStr){
			
			StrutturaDTO dto = new StrutturaDTO();
			dto.setStruttura(str);
			dto.setSocieta(anagraficaDAO.getSocietaById(str.getFkIsSocieta().longValue()));
			dto.setClassi(anagraficaDAO.getClassiByStruttura(str.getId()));
			
			lista.add(dto);
		}
		return lista;
	}
	
	public List<StrutturaDTO> getStrutturaByCodFis(DataInDTO dataIn) {
		List<StrutturaDTO> lista = new ArrayList<StrutturaDTO>();
		List<IsStruttura> listaStr = anagraficaDAO.getStrutturaByCodFis(dataIn.getCodFiscale());
		for(IsStruttura str: listaStr){
			
			StrutturaDTO dto = new StrutturaDTO();
			dto.setStruttura(str);
			dto.setSocieta(anagraficaDAO.getSocietaById(str.getFkIsSocieta().longValue()));
			dto.setClassi(anagraficaDAO.getClassiByStruttura(str.getId()));
			
			lista.add(dto);
		}
		return lista;
	}

	public StrutturaDTO getStrutturaById(DataInDTO dataIn) {
		StrutturaDTO dto = new StrutturaDTO();
		IsStruttura str = anagraficaDAO.getStrutturaById(dataIn.getId());
		dto.setStruttura(str);
		dto.setSocieta(anagraficaDAO.getSocietaById(str.getFkIsSocieta().longValue()));
		dto.setClassi(anagraficaDAO.getClassiByStruttura(str.getId()));
		
		return dto;
	}
	
	public List<StrutturaDTO> getStrutturaByCriteria(DataInDTO dataIn) {
		List<StrutturaDTO> lista = new ArrayList<StrutturaDTO>();
		List<IsStruttura> listaStr = anagraficaDAO.getStrutturaByCriteria((DichiarazioneSearchCriteria) dataIn.getObj());
		for(IsStruttura str: listaStr){
			
			StrutturaDTO dto = new StrutturaDTO();
			dto.setStruttura(str);
			dto.setSocieta(anagraficaDAO.getSocietaById(str.getFkIsSocieta().longValue()));
			dto.setClassi(anagraficaDAO.getClassiByStruttura(str.getId()));
			
			lista.add(dto);
		}
		return lista;
	}

	public void updateStruttura(DataInDTO dataIn) {
		anagraficaDAO.updateStruttura((IsStruttura) dataIn.getObj());
	}

	public IsStruttura saveStruttura(DataInDTO dataIn) {
		return anagraficaDAO.saveStruttura((IsStruttura) dataIn.getObj());
	}

	public void deleteStrutturaById(DataInDTO dataIn) {
		anagraficaDAO.deleteStrutturaById(dataIn.getId());
	}

	public List<IsClassiStruttura> getClassiByStruttura(DataInDTO dataIn) {
		return anagraficaDAO.getClassiByStruttura(dataIn.getId());
	}

	public void updateClasseStruttura(DataInDTO dataIn) {
		anagraficaDAO.updateClasseStruttura((IsClassiStruttura) dataIn.getObj());
	}
	
	public void saveClasseStruttura(DataInDTO dataIn) {
		anagraficaDAO.saveClasseStruttura((IsClassiStruttura) dataIn.getObj());
	}

	public void deleteClasseStrutturaByStr(DataInDTO dataIn) {
		anagraficaDAO.deleteClasseStrutturaByStr(dataIn.getId());
	}
	
	public void deleteClasseStrutturaByStrCls(DataInDTO dataIn) {
		anagraficaDAO.deleteClasseStrutturaByStrCls(dataIn.getId(),
				dataIn.getId2());
	}

	public List<IsClasse> getClassi(DataInDTO dataIn) {
		return anagraficaDAO.getClassi();
	}
	
	public List<Sitidstr> getSitidstrByLikeIndirizzo(DataInDTO dataIn) {
		return anagraficaDAO.getSitidstrByLikeIndirizzo(dataIn.getId2(), dataIn.isBool(), dataIn.getMaxNumber());
	}
	
	public Sitidstr getSitidstrByIndirizzo(DataInDTO dataIn) {
		return anagraficaDAO.getSitidstrByIndirizzo(dataIn.getId2());
	}
}
